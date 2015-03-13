package fatcat.snowberry.tag.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IMemberModelListener;
import fatcat.snowberry.tag.IProjectModel;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.TagCore;


public class TypeModel extends MemberModel implements ITypeModel {
	
	private TypeDeclaration node;
	private IType type;
	private final ArrayList<MemberModel> members;
	private final ArrayList<IMemberModelListener> listeners;
	
	public TypeModel(IProjectModel project, IFile file, TypeDeclaration node, IType type) {
		super(null, project, file, node, type);
		members = new ArrayList<MemberModel>();
		listeners = new ArrayList<IMemberModelListener>();
		init();
	}

	@Override
	public TypeDeclaration getASTNode() {
		return node;
	}

	@Override
	public IType getJavaElement() {
		return type;
	}
	
	public void setASTNode(BodyDeclaration node) {
		assert !(node instanceof TypeDeclaration);
		this.node = (TypeDeclaration) node;
	}
	
	public void setJavaElement(IMember element) {
		assert !(element instanceof IType);
		this.type = (IType) element;
	}
	
	protected void init() {

		// fields
		FieldDeclaration[] fieldDeclarations = node.getFields();
		for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
			IField field = resolveJavaElement(fieldDeclaration);
			FieldModel fieldModel = new FieldModel(this, getOwnerProject(), getResource(), fieldDeclaration, field);
			members.add(fieldModel);
		}
		
		// methods
		MethodDeclaration[] methodDeclarations = node.getMethods();
		for (MethodDeclaration methodDeclaration : methodDeclarations) {
			assert !(methodDeclaration.resolveBinding().getJavaElement() instanceof IMethod);
			IMethod method = (IMethod) methodDeclaration.resolveBinding().getJavaElement();
			MethodModel methodModel = new MethodModel(this, getOwnerProject(), getResource(), methodDeclaration, method);
			members.add(methodModel);
		}
	}
	
	public void notifyContentChanged() {
		
		ArrayList<IMemberModel> restedMembers = new ArrayList<IMemberModel>();
		LinkedList<IMemberModel> membersAdded = new LinkedList<IMemberModel>();
		for (MemberModel model : members)
			restedMembers.add(model);
		
		// fields
		FieldDeclaration[] fieldDeclarations = node.getFields();
		for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
			IField field = resolveJavaElement(fieldDeclaration);
			MemberModel memberModel = (MemberModel) getMemberModel(field);
			if (memberModel != null) {
				ASTNode oldNode = memberModel.getASTNode();
				memberModel.setASTNode(fieldDeclaration);
				memberModel.setJavaElement(field);
				if (!oldNode.subtreeMatch(new MemberNodeMatcher(), fieldDeclaration)) {
					memberModel.notifyJavadocChanged();
					memberModel.notifyContentChanged();
					for (IMemberModelListener listener : listeners)
						listener.memberModelChanged(memberModel);
				}
				restedMembers.remove(memberModel);
			} else {
				FieldModel fieldModel = new FieldModel(this, getOwnerProject(), getResource(), fieldDeclaration, field);
				members.add(fieldModel);
				membersAdded.add(fieldModel);
			}
		}
		
		// methods
		MethodDeclaration[] methodDeclarations = node.getMethods();
		for (MethodDeclaration methodDeclaration : methodDeclarations) {
			assert !(methodDeclaration.resolveBinding().getJavaElement() instanceof IMethod);
			IMethod method = (IMethod) methodDeclaration.resolveBinding().getJavaElement();
			MemberModel memberModel = (MemberModel) getMemberModel(method);
			if (memberModel != null) {
				ASTNode oldNode = memberModel.getASTNode();
				memberModel.setASTNode(methodDeclaration);
				memberModel.setJavaElement(method);
				if (!oldNode.subtreeMatch(new MemberNodeMatcher(), methodDeclaration)) {
					memberModel.notifyJavadocChanged();
					memberModel.notifyContentChanged();
					for (IMemberModelListener listener : listeners)
						listener.memberModelChanged(memberModel);
				}
				restedMembers.remove(memberModel);
			} else {
				MethodModel methodModel = new MethodModel(this, getOwnerProject(), getResource(), methodDeclaration, method);
				members.add(methodModel);
				membersAdded.add(methodModel);
			}
		}
		
		// removed members
		for (IMemberModel model : restedMembers) {
			members.remove(model);
			for (IMemberModelListener listener : listeners)
				listener.memberModelRemoved(model);
		}
		
		// added members
		for (IMemberModel model : membersAdded) {
			for (IMemberModelListener listener : listeners)
				listener.memberModelAdded(model);
		}
		
	}
	
	static IField resolveJavaElement(FieldDeclaration member) {
		final class HostClosure {IJavaElement host = null;}
		final HostClosure hc = new HostClosure();
		member.accept(new ASTVisitor() {
			public boolean visit(SimpleName node) {
				if (hc.host == null) {
					hc.host = node.resolveBinding().getJavaElement();
					if (!(hc.host instanceof IField)) hc.host = null;
				}
				return true;
			}
		});
		assert hc.host != null;
		assert !(hc.host instanceof IField);
		return (IField) hc.host;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Type Model : " + type.getElementName() + " (" + members.size() + " Members)");
		for (IMemberModel member : members)
			buffer.append("\n\t" + member.getJavaElement().getElementName());
		return buffer.toString();
	}

	@Override
	public IMemberModel[] getMemberModels() {
		return members.toArray(new IMemberModel[0]);
	}

	@Override
	public void addMemberModelListener(IMemberModelListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeMemberModelListener(IMemberModelListener listener) {
		listeners.remove(listener);
	}

	@Override
	public IMemberModel getMemberModel(IMember javaElement) {
		for (IMemberModel model : members)
			if (model.getJavaElement().equals(javaElement))
				return model;
		return null;
	}
	
	@Override
	public int getKind() {
		return IMemberModel.TYPE;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof ITypeModel) {
			ITypeModel model = (ITypeModel) obj;
			return model.getJavaElement().equals(this.getJavaElement());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return getJavaElement().getFullyQualifiedName().hashCode();
	}

	@Override
	public boolean isPrimaryType() {
		String name = null;
		int dot_index = getResource().getName().lastIndexOf(".");
		if (dot_index != -1) {
			name = getResource().getName().substring(0, dot_index);
		} else {
			name = getResource().getName();
		}
		return getJavaElement().getElementName().equals(name);
	}
	
	public ITypeModel getSuperType() {
		
		final org.eclipse.jdt.core.dom.Type superClass = getASTNode().getSuperclassType();
		if (superClass == null) return null;

		IType je = (IType) superClass.resolveBinding().getJavaElement();
		if (je == null) return null;
		
		return TagCore.searchTypeModel(je, getOwnerProject());
	}
	
	public ITypeModel[] getInterfaces() {
		LinkedList<ITypeModel> rst = new LinkedList<ITypeModel>();
		ITypeBinding[] bindings = getASTNode().resolveBinding().getInterfaces();
		for (ITypeBinding binding : bindings) {
			IType je = (IType) binding.getJavaElement();
			if (je == null) continue;
			
			ITypeModel interfaze = TagCore.searchTypeModel(je, getOwnerProject());
			if (interfaze != null) rst.add(interfaze);
		}
		return rst.toArray(new ITypeModel[0]);
	}

	@Override
	public ITypeModel[] getKnownSubTypes() {
		LinkedList<ITypeModel> rst = new LinkedList<ITypeModel>();
		IProjectModel[] projects = TagCore.getProjectModels();
		for (IProjectModel proj : projects) {
			ITypeModel[] models = proj.getITypeModels();
			for (ITypeModel model : models) {
				if (model.isSubclassOf(this) || model.isImplementOf(this)) {
					rst.add(model);
				}
			}
		}
		return rst.toArray(new ITypeModel[0]);
	}

	@Override
	public String getAuthor() {
		if (getASTNode().getJavadoc() == null) return null;
		List<?> tags = (List<?>) getASTNode().getJavadoc().tags();
		for (Object tag : tags) {
			if (tag instanceof TagElement) {
				TagElement tagElement = (TagElement) tag;
				if (!("@author").equals(tagElement.getTagName()))
					continue;
				List<?> texts = tagElement.fragments();
				StringBuffer tagText = new StringBuffer();
				for (Object text : texts) {
					if (text instanceof TextElement) {
						tagText.append(((TextElement) text).getText());
						tagText.append(" ");
					}
				}
				return tagText.toString().trim();
			}
		}
		return null;
	}

	@Override
	public boolean isSubclassOf(ITypeModel model) {
		final org.eclipse.jdt.core.dom.Type superClass = getASTNode().getSuperclassType();
		if (superClass == null) return false;
		ITypeBinding binding = superClass.resolveBinding();
		if (binding == null) return false;
		IType je = (IType) binding.getJavaElement();
		if (je == null) return false;
		return model.getJavaElement().equals(je);
	}

	@Override
	public boolean isImplementOf(ITypeModel model) {
		ITypeBinding[] bindings = getASTNode().resolveBinding().getInterfaces();
		for (ITypeBinding binding : bindings) {
			IType je = (IType) binding.getJavaElement();
			if (je == null) continue;
			if (model.getJavaElement().equals(je)) return true;
		}
		return false;
	}

	@Override
	public IMemberModel searchMember(IJavaElement je) {
		IMemberModel[] models = getMemberModels();
		for (IMemberModel model : models) {
			if (model.getJavaElement().equals(je)) {
				return model;
			}
		}
		return null;
	}

}

final class MemberNodeMatcher extends ASTMatcher {
	
	MemberNodeMatcher() {
		super(true);
	}
	
}
