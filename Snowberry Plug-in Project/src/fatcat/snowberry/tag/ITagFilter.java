package fatcat.snowberry.tag;

/**
 * 标签过滤器。用于筛选特定的标签。
 * @author 张弓
 *
 */
public interface ITagFilter {
	
	/**
	 * 检测指定标签是否符合条件。
	 * 
	 * @param tag 指定标签
	 * @return {@code true} if is accepted, {@code false} otherwise
	 */
	public boolean isAccepted(ITag tag);

}
