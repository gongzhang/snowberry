package fatcat.snowberry.tag;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.FieldDeclaration;

/**
 * 成员变量模型。
 * @author 张弓
 *
 */
public interface IFieldModel extends IMemberModel {
	
	public FieldDeclaration getASTNode();
	
	public IField getJavaElement();
	
	/**
	 * 取得变量类型所对应的类型模型。
	 * <p>
	 * 该类型无法解析，或没有对应的源代码，均返回<code>null</code>。
	 * </p>
	 * @return 类型模型或{@code null}
	 */
	public ITypeModel getFieldTypeModel();

}
