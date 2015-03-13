package fatcat.snowberry.tag;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;

/**
 * 工程模型。
 * <p>
 * 工程模型由该工程中定义的所有类和接口组成（包括内嵌类）。
 * 其中，类和接口已经被统一抽象为“类型模型”即{@link ITypeModel}。
 * </p>
 * <p>
 * 要监视工程的内容变更请参见{@link ITypeModelListener}。
 * </p>
 * @author 张弓
 *
 */
public interface IProjectModel {
	
	/**
	 * 取得工程模型对应的Eclipse资源。
	 * @return Eclipse资源
	 */
	public IProject getResource();
	
	/**
	 * 取得工程模型中来自指定资源的类型模型。
	 * @param file 指定的文件资源
	 * @return 类型模型数组
	 */
	public ITypeModel[] getTypeModels(IFile file);
	
	/**
	 * 取得工程模型中来自指定的<code>IJavaElement</code>的类型模型。
	 * <p>
	 * 此方法是Snowberry标签机制和JDT之间的桥梁。
	 * </p>
	 * @param element 指定的<code>IJavaElement</code>
	 * @return 类型模型或{@code null}
	 */
	public ITypeModel getTypeModel(IType element);
	
	/**
	 * 取得工程模型中来自指定的<code>IJavaElement</code>的成员模型。
	 * <p>
	 * 此方法是Snowberry标签机制和JDT之间的桥梁。
	 * </p>
	 * @param element 指定的<code>IJavaElement</code>
	 * @return 成员模型或{@code null}
	 */
	public IMemberModel getMemberModel(IMember element);
	
	/**
	 * 取得工程模型中当前所有的类型模型。
	 * @return 类型模型数组
	 */
	public ITypeModel[] getITypeModels();
	
	/**
	 * 添加一个类型模型监听器。
	 * @param listener 监听器
	 */
	public void addTypeModelListener(ITypeModelListener listener);
	
	/**
	 * 移除一个类型模型监听器。
	 * @param listener 监听器
	 */
	public void removeTypeModelListener(ITypeModelListener listener);

}
