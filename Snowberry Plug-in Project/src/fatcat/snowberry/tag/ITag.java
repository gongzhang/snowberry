package fatcat.snowberry.tag;


/**
 * Java元素标签。
 * <p>
 * 每个标签都必须依附在一个<code>IJavaElement</code>上（寄主）。
 * 每个标签包含一个标签名和一张属性表，它的示意图如下：
 * </p>
 * <p><i>
 * 标签名：sample tag
 * <br>
 * <b>属性名 属性值</b>
 * <br>......<br>......<br>......
 * </i></p>
 * @author 张弓
 *
 */
public interface ITag {
	
	/**
	 * 取得标签的名字。
	 * @return 标签名
	 */
	public String getName();

	/**
	 * 取得标签的寄主。
	 * @return 标签寄主
	 */
	public IMemberModel getHostModel();
	
	/**
	 * 取得属性表的属性个数。
	 * @return 属性个数
	 */
	public int size();

	/**
	 * 取得按索引顺序排列的属性名。
	 * @return 属性名
	 */
	public String[] getPropertyNames();
	
	/**
	 * 取得按索引顺序排列的属性值。
	 * @return 属性值
	 */
	public String[] getPropertyValues();
	
	/**
	 * 取得指定的属性名。
	 * @param index 索引
	 * @return 属性名
	 */
	public String getPropertyName(int index);
	
	/**
	 * 取得指定的属性值。
	 * @param index 索引
	 * @return 属性值
	 */
	public String getPropertyValue(int index);
	
	/**
	 * 取得指定的属性值。
	 * <p>
	 * 若不存在该属性名，则返回<code>null</code>。
	 * </p>
	 * @param name 属性名
	 * @return 属性值或{@code null}
	 */
	public String getPropertyValue(String name);
	
	/**
	 * 检查此标签内容是否合法。
	 * <p>
	 * 实际上是使用<code>TagPaser</code>编码后再尝试解码，再比对内容的一致性。
	 * </p>
	 * @return 标签是否合法
	 */
	public boolean isLegal();

}
