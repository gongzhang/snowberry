package fatcat.snowberry.tag;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.BodyDeclaration;

/**
 * 类成员模型。
 * <p>
 * 类成员模型是可以附加标签的顶层类。
 * 已知的子类包括{@link IFieldModel}、{@link IMethodModel}和{@link ITypeModel}。
 * 调用{@link #getKind()}方法取得具体类别。
 * </p>
 * @author 张弓
 *
 */
public interface IMemberModel {
	
	/**
	 * 成员变量。
	 */
	public static final int FIELD = 0;
	
	/**
	 * 成员方法。
	 */
	public static final int METHOD = 1;
	
	/**
	 * 类或接口。
	 */
	public static final int TYPE = 2;
	
	/**
	 * 取得当前模型所在的Eclipse资源。
	 * @return Eclipse资源
	 */
	public IFile getResource();
	
	/**
	 * 取得对应的语法树节点
	 * @return 语法树节点
	 */
	public BodyDeclaration getASTNode();
	
	/**
	 * 取得对应的<code>IJavaElement</code>。
	 * <p>
	 * 此方法是Snowberry模型和JDT之间的桥梁。
	 * </p>
	 * @return
	 */
	public IMember getJavaElement();
	
	/**
	 * 取得所在的工程模型。
	 * @return 工程模型
	 */
	public IProjectModel getOwnerProject();
	
	/**
	 * 取得包含此模型的类型模型，如果当前模型是<code>ITypeModel</code>，则返回<code>null</code>。
	 * <p>
	 * 即使是内嵌类，此方法也返回<code>null</code>。
	 * </p>
	 * @return 类型模型或{@code null}
	 */
	public ITypeModel getOwnerType();
	
	/**
	 * 取得此模型上的所有标签。
	 * @return 标签数组
	 */
	public ITag[] getTags();
	
	/**
	 * 取得此模型上的所有指定标签名的标签。
	 * @param tag_name 指定的标签名
	 * @return 标签数组
	 */
	public ITag[] getTags(String tag_name);
	
	/**
	 * 添加标签监听器。
	 * @param listener 监听器
	 */
	public void addTagListener(ITagListener listener);
	
	/**
	 * 移除标签监听器。
	 * @param listener 监听器
	 */
	public void removeTagListener(ITagListener listener);
	
	/**
	 * 取得当前模型的具体类别。
	 * <p>
	 * 返回{@link IMemberModel#FIELD}一定是{@link IFieldModel}；
	 * </p>
	 * <p>
	 * 返回{@link IMemberModel#METHOD}一定是{@link IMethodModel}；
	 * </p>
	 * <p>
	 * 返回{@link IMemberModel#TYPE}一定是{@link ITypeModel}。
	 * </p>
	 * @return 类型值
	 */
	public int getKind();
	
	/**
	 * 在此成员上添加一个标签。
	 * <p>
	 * 调用此方法将直接写入源文件并触发相应更新事件。
	 * </p>
	 * @deprecated 参见{@link TagTask}
	 * @param tag 要添加的标签
	 * @throws SourceEditingException
	 * 
	 */
	@Deprecated
	public void addTag(ITag tag) throws SourceEditingException;
	
	/**
	 * 删除此成员上指定内容的标签。
	 * <p>
	 * 调用此方法将直接写入源文件并触发相应更新事件。
	 * </p>
	 * @deprecated 参见{@link TagTask}
	 * @param tag 指定的标签内容
	 * @throws SourceEditingException
	 */
	@Deprecated
	public void removeTag(ITag tag) throws SourceEditingException;

	/**
	 * 删除此成员上指定内容的标签。
	 * @deprecated 参见{@link TagTask}
	 * @param tag 指定的标签内容
	 * @param saveFlag 是否立即保存至文件
	 * @throws SourceEditingException
	 */
	@Deprecated
	public void removeTag(ITag t, boolean saveFlag) throws SourceEditingException;

	/**
	 * 在此成员上添加一个标签。
	 * @deprecated 参见{@link TagTask}
	 * @param tag 要添加的标签
	 * @param saveFlag 是否立即保存至文件
	 * @throws SourceEditingException
	 */
	@Deprecated
	public void addTag(ITag t, boolean saveFlag) throws SourceEditingException;

}
