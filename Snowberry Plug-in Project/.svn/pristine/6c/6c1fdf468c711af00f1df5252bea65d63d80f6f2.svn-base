package fatcat.snowberry.search;

/**
 * 搜索结果监听器。
 * <p>
 * 使用<code>SearchCore</code>中的各种搜索功能时，均需要使用结果监听器来取得最终的搜索结果。
 * 客户应当自行实现这个接口以获得搜索结果。
 * </p>
 * 
 * @author 张弓
 *
 * @param <T> 搜索结果对象的类型
 */
public interface IResultListener<T> {

	/**
	 * 得到一个符合条件的搜索结果时此方法被调用。
	 * <p>
	 * 每次得到一个新的搜索结果时，均会调用此方法通知客户。具体搜索得到的对象通过参数<code>result</code>传达给客户。
	 * </p>
	 * <p>
	 * 实现此方法时应注意返回值。如果需要立即终止此次搜索任务，应当返回<code>true</code>；返回<code>false</code>则会继续搜索任务。
	 * 返回<code>true</code>时，此次搜索中此方法肯定不会被再次调用。
	 * </p>
	 * @param result 搜索得到的一个结果
	 * @return 若要终止搜索，返回{@code true}；否则返回{@code false}
	 */
	public boolean gotResult(T result);
	
	/**
	 * 搜索任务完成时此方法被调用。
	 * 
	 * @param stopped_manually 标记是手动停止还是自行结束
	 */
	public void done(boolean stopped_manually);
	
}
