package fatcat.snowberry.tag.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import fatcat.snowberry.tag.IFieldModel;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IProjectModel;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.TagCore;


public class FieldModel extends MemberModel implements IFieldModel {

	private FieldDeclaration node;
	private IField field;
	
	public FieldModel(ITypeModel owner, IProjectModel project, IFile file, FieldDeclaration node, IField type) {
		super(owner, project, file, node, type);
		init();
	}

	@Override
	public FieldDeclaration getASTNode() {
		return node;
	}

	@Override
	public IField getJavaElement() {
		return field;
	}
	
	public void setASTNode(BodyDeclaration node) {
		assert !(node instanceof FieldDeclaration);
		this.node = (FieldDeclaration) node;
	}
	
	public void setJavaElement(IMember element) {
		assert !(element instanceof IField);
		this.field = (IField) element;
	}
	
	protected void init() {
		// 初始化后会调用此方法
	}
	
	public void notifyContentChanged() {
		// 内容改变时会调用此方法
	}

	@Override
	public int getKind() {
		return IMemberModel.FIELD;
	}

	@Override
	public ITypeModel getFieldTypeModel() {
		org.eclipse.jdt.core.dom.Type type = getASTNode().getType();
		IType je = (IType) type.resolveBinding().getJavaElement();
		if (je == null) return null;
		return TagCore.searchTypeModel(je, getOwnerProject());
	}

}
