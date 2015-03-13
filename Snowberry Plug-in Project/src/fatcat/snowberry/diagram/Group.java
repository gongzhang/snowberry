package fatcat.snowberry.diagram;

import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;

/**
 * 分组描述器。
 * <p>
 * 多个类成员可以视为一个整体，如相应的Getter和Setter方法就可分为一组。
 * 这种分组信息依靠成员上的特定Tag所描述。
 * </p><p>
 * 一个能够接受的分组标签格式应当为：标签名为“group”，拥有非空的“name”属性（分组名）。
 * </p>
 *
 * @see #parseGroup(IMemberModel)
 * @author 张弓
 *
 */
public class Group {
	
	/**
	 * 分组名。
	 */
	public final String name;
	
	private Group(String name) {
		this.name = name;
	}
	
	/**
	 * 从指定的成员模型上解读分组信息。
	 * <p>
	 * 如果该成员上没有描述分组信息的标签，或者标签不合法，均返回<code>null</code>。
	 * </p>
	 * @param member 指定的成员模型
	 * @return 分组或{@code null}
	 */
	public static final Group parseGroup(IMemberModel member) {
		ITag[] tags = member.getTags("group");
		if (tags.length == 0) {
			return null;
		} else {
			String name = tags[0].getPropertyValue("name");
			if (name == null || name.length() == 0) {
				return null;
			} else {
				return new Group(name);
			}
		}
	}

}
