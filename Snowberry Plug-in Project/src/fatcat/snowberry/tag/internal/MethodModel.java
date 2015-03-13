package fatcat.snowberry.tag.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IMethodModel;
import fatcat.snowberry.tag.IProjectModel;
import fatcat.snowberry.tag.ITypeModel;


public class MethodModel extends MemberModel implements IMethodModel {

	private MethodDeclaration node;
	private IMethod method;
	
	public MethodModel(ITypeModel owner, IProjectModel project, IFile file, MethodDeclaration node, IMethod method) {
		super(owner, project, file, node, method);
		init();
	}

	@Override
	public MethodDeclaration getASTNode() {
		return node;
	}

	@Override
	public IMethod getJavaElement() {
		return method;
	}
	
	public void setASTNode(BodyDeclaration node) {
		assert !(node instanceof MethodDeclaration);
		this.node = (MethodDeclaration) node;
	}
	
	public void setJavaElement(IMember element) {
		assert !(element instanceof IMethod);
		this.method = (IMethod) element;
	}
	
	protected void init() {
		// 初始化后会调用此方法
	}
	
	public void notifyContentChanged() {
		// 内容改变时会调用此方法
	}
	
	@Override
	public int getKind() {
		return IMemberModel.METHOD;
	}

}
