package fatcat.snowberry.tag;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * 类型模型，是类和接口的统一抽象表示。
 * <p>
 * 类型模型由它的成员变量和成员函数构成，它们均被封装为{@link IMemberModel}。
 * </p>
 * <p>
 * 要监控类型模型内容的变化，请参见{@link IMemberModelListener}。
 * </p>
 * @author 张弓
 *
 */
public interface ITypeModel extends IMemberModel {
	
	public TypeDeclaration getASTNode();
	
	public IType getJavaElement();
	
	/**
	 * 返回当前类型模型内定义的所有成员。
	 * <p>
	 * 不会包括内嵌类，内嵌类会和普通类和接口一同保存在工程模型中。
	 * </p>
	 * @return 成员模型数组
	 */
	public IMemberModel[] getMemberModels();
	
	/**
	 * 添加成员模型监听器。
	 * @param listener 监听器
	 */
	public void addMemberModelListener(IMemberModelListener listener);
	
	/**
	 *移除成员模型监听器。
	 * @param listener 监听器
	 */
	public void removeMemberModelListener(IMemberModelListener listener);
	
	/**
	 * 取得模型中对应<code>IJavaElement</code>的成员模型。
	 * @param javaElement 指定的{@code IJavaElement}
	 * @return 成员模型或{@code null}
	 */
	public IMemberModel getMemberModel(IMember javaElement);
	
	/**
	 * 判断当前Type是否是所属文件中的第一个被定义的Type。
	 */
	public boolean isPrimaryType();
	
	/**
	 * 取得父类模型。
	 * <p>如果父类类型在当前Workspace中没有代码定义，则返回<code>null</code>。</p>
	 * @return 父类模型或{@code null}
	 */
	public ITypeModel getSuperType();
	
	/**
	 * 取得实现的接口。
	 * <p>返回的数组中只包含能够在当前Workspace中找到代码定义的接口。</p>
	 * @return 模型数组
	 */
	public ITypeModel[] getInterfaces();
	
	/**
	 * 取得已知的子类。
	 * @return 模型数组
	 */
	public ITypeModel[] getKnownSubTypes();
	
	/**
	 * 从Javadoc中读取作者姓名。
	 * @return 作者姓名或{@code null}
	 */
	public String getAuthor();
	
	/**
	 * 判断是否是指定类型的子类。
	 * @param model 指定类型
	 * @return 是否是子类
	 */
	public boolean isSubclassOf(ITypeModel model);
	
	/**
	 * 判断是否是指定类型的接口实现。
	 * @param model 指定类型
	 * @return 是否是接口实现
	 */
	public boolean isImplementOf(ITypeModel model);
	
	public IMemberModel searchMember(IJavaElement je);

}
