package fatcat.snowberry.tag;


/**
 * 类型模型监听器。
 * @author 张弓
 * @see IProjectModel#addTypeModelListener(ITypeModelListener)
 * @see IProjectModel#removeTypeModelListener(ITypeModelListener)
 */
public interface ITypeModelListener {
	
	public void typeModelAdded(ITypeModel typeModel);
	
	public void typeModelChanged(ITypeModel typeModel);
	
	public void typeModelRemoved(ITypeModel typeModel);
	
}
