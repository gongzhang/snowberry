package fatcat.snowberry.tag.internal;

import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IProjectModel;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.ITypeModelListener;


public class ProjectModel implements IProjectModel {
	
	private final IProject project;
	final ArrayList<TypeModel> typeModels;
	final ArrayList<ITypeModelListener> listeners;

	public ProjectModel(IProject project) {
		typeModels = new ArrayList<TypeModel>();
		listeners = new ArrayList<ITypeModelListener>();
		assert project != null;
		this.project = project;
		init();
	}
	
	private void init() {
		try {
			project.accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE) {
						IFile file = (IFile) resource;
						if ("java".equals(file.getFileExtension())) {
							ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
							TypeModelBuilder builder = new TypeModelBuilder(ProjectModel.this, cu);
							builder.build();
						}
					}
					return true;
				}
			});
		} catch (CoreException e) {
			SnowberryCore.log(Status.ERROR, International.ErrorWhenVisitProject, e);
		}
	}
	
	@Override
	public IProject getResource() {
		return project;
	}
	
	public void notifyContentChanged(IResourceDelta delta) {
		try {
			delta.accept(new TypeModelModifier(this));
		} catch (CoreException e) {
			SnowberryCore.log(Status.ERROR, International.ErrorWhenUpdateProject, e);
		}
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Project Model : " + project.getName() + " (" + typeModels.size() + " Types)");
		for (ITypeModel typeModel : typeModels)
			buffer.append("\n\t" + typeModel.getJavaElement().getElementName());
		return buffer.toString();
	}
	
	public void addTypeModelListener(ITypeModelListener listener) {
		listeners.add(listener);
	}
	
	public void removeTypeModelListener(ITypeModelListener listener) {
		listeners.remove(listener);
	}

	@Override
	public ITypeModel[] getTypeModels(IFile file) {
		if (!project.equals(file.getProject())) {
			throw new IllegalArgumentException("file not belong to this project");
		}
		LinkedList<ITypeModel> models = new LinkedList<ITypeModel>();
		for (ITypeModel model : typeModels)
			if (model.getResource().equals(file))
				models.add(model);
		return models.toArray(new ITypeModel[0]);
	}

	@Override
	public ITypeModel getTypeModel(IType element) {
		for (ITypeModel model : typeModels)
			if (model.getJavaElement().equals(element))
				return model;
		return null;
	}

	@Override
	public ITypeModel[] getITypeModels() {
		return typeModels.toArray(new ITypeModel[0]);
	}

	@Override
	public IMemberModel getMemberModel(IMember element) {
		if (element instanceof IType) {
			return getTypeModel((IType) element);
		} else {
			for (ITypeModel model : typeModels) {
				IMemberModel rst = model.getMemberModel(element);
				if (rst != null) return rst;
			}
			return null;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		else if (obj instanceof IProjectModel) {
			return project.getName().equals(((IProjectModel) obj).getResource().getName());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return project.getName().hashCode();
	}

}

final class TypeModelBuilder extends ASTVisitor {
	
	private final ProjectModel project;
	private final ICompilationUnit cu;
	private final ArrayList<TypeModel> builtModels;
	
	TypeModelBuilder(ProjectModel project, ICompilationUnit cu) {
		super(true);
		builtModels = new ArrayList<TypeModel>();
		this.project = project;
		this.cu = cu;
	}
	
	TypeModel[] build() {
		try {
			builtModels.clear();
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setResolveBindings(true);
			parser.setSource(cu);
			ASTNode root = parser.createAST(null);
			root.accept(this);
		} catch (Exception e) {
//			e.printStackTrace();
			SnowberryCore.log(Status.ERROR, International.ErrorWhenVisitProject, e);
		}
		return builtModels.toArray(new TypeModel[0]);
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		assert !(node.resolveBinding().getJavaElement() instanceof IType);
		IType type = (IType) node.resolveBinding().getJavaElement();
		TypeModel typeModel = new TypeModel(project, (IFile) cu.getResource(), node, type);
		project.typeModels.add(typeModel);
		builtModels.add(typeModel);
		return true;
	}
	
}

final class TypeModelModifier extends ASTVisitor implements IResourceDeltaVisitor {
	
	private final ProjectModel project;
	private ArrayList<ITypeModel> restedModels = null;
	private IFile currentFile = null;
	
	TypeModelModifier(ProjectModel project) {
		this.project = project;
		modelsAdded = new LinkedList<ITypeModel>();
	}
	
	private final LinkedList<ITypeModel> modelsAdded;

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		if (delta.getResource().getType() == IResource.FILE) {
			IFile file = (IFile) delta.getResource();
			if ("java".equals(file.getFileExtension())) {
				ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
				currentFile = file;
				restedModels = new ArrayList<ITypeModel>();
				for (ITypeModel model : project.getTypeModels(file))
					restedModels.add(model);
				ASTParser parser = ASTParser.newParser(AST.JLS3);
				parser.setResolveBindings(true);
				parser.setSource(cu);
				ASTNode root = parser.createAST(null);
				root.accept(this);
				for (ITypeModel model : restedModels) {
					project.typeModels.remove(model);
					for (ITypeModelListener listener : project.listeners)
						listener.typeModelRemoved(model);
				}
				
				for (ITypeModel model : modelsAdded) {
					for (ITypeModelListener listener : project.listeners)
						listener.typeModelAdded(model);
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		assert !(node.resolveBinding().getJavaElement() instanceof IType);
		modelsAdded.clear();
		IType type = (IType) node.resolveBinding().getJavaElement();
		TypeModel oldTypeModel = (TypeModel) project.getTypeModel(type);
		if (oldTypeModel != null) {
			oldTypeModel.setASTNode(node);
			oldTypeModel.setJavaElement(type);
			restedModels.remove(oldTypeModel);
			oldTypeModel.notifyJavadocChanged();
			oldTypeModel.notifyContentChanged();
			for (ITypeModelListener listener : project.listeners)
				listener.typeModelChanged(oldTypeModel);
		} else {
			TypeModel typeModel = new TypeModel(project, currentFile, node, type);
			project.typeModels.add(typeModel);
			modelsAdded.add(typeModel);
		}
		return true;
	}
	
}