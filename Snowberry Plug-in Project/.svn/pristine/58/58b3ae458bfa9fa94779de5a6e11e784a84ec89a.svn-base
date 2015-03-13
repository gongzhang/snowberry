package fatcat.snowberry.diagram;

import java.util.LinkedList;

import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.internal.Tag;

/**
 * 标签（Label）信息。
 * <p>
 * 标签（Label）是名为“label”的Tag所描述的信息，它包含一个非空的“name”属性，和一个可选的“comment”属性。
 * </p>
 * <p>
 * 实际上此处的标签就是通常所说的“Tag”，但为了避免混淆，命名为“Label”。
 * </p>
 * 
 * @author 张弓
 *
 */
public class Label {
	
	public final String name;
	public final String comment;
	public final IMemberModel model;
	
	public Label(String name, String comment, IMemberModel model) {
		this.name = name;
		this.comment = comment;
		this.model = model;
	}
	
	public Label(String name, String comment) {
		this(name, comment, null);
	}
	
	public Label(String name, IMemberModel model) {
		this(name, "", model);
	}
	
	public Label(String name) {
		this(name, "");
	}
	
	/**
	 * 取得指定成员模型上的所有标签（Label）。
	 * @param member 指定的成员
	 * @return 标签（Label）数组
	 */
	public static final Label[] parseLabels(IMemberModel member) {
		LinkedList<Label> rst = new LinkedList<Label>();
		ITag[] tags = member.getTags("label");
		for (ITag tag : tags) {
			String name = tag.getPropertyValue("name");
			if (name != null && name.length() != 0) {
				String comment = tag.getPropertyValue("comment");
				if (comment != null) rst.add(new Label(name, comment, member));
				else rst.add(new Label(name, member));
			}
		}
		return rst.toArray(new Label[0]);
	}
	
	public ITag toTag() {
		Tag tag = new Tag();
		tag.setName("label");
		tag.addProperty("name", name);
		if (comment != null && comment.length() > 0) {
			tag.addProperty("comment", comment);
		}
		return tag;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof Label) {
			Label label = (Label) obj;
			if (comment != null) return comment.equals(label.comment) && name.equals(label.name);
			else return ((label.comment == null) || (label.comment.length() == 0)) && name.equals(label.name);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
