package fatcat.snowberry.diagram;

import java.util.HashMap;

import fatcat.snowberry.core.International;
import fatcat.snowberry.gui.CategoryPanel;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.internal.Tag;

/**
 * 类别描述器。
 * <p>
 * “类别”是由类或类成员上特定的Tag所描述的信息。通过“类别”信息可以给类和类的成员进行归类处理。
 * </p><p>
 * 一个能够接受的类别标签格式应当为：标签名为“category”，拥有非空的“name”属性（类别名），可以有“color”属性描述显示颜色。
 * </p><p>
 * 如果有“color”属性，它的值应当是“Gray”、“Red”、“Yellow”、“Green”、“Blue”和“Purple”中的一种。
 * </p>
 * @see #parseCategory(IMemberModel)
 * @author 张弓
 *
 */
public class Category {
	
	private static final HashMap<String, Integer> COLOR_MAP;
	
	static {
		COLOR_MAP = new HashMap<String, Integer>();
		COLOR_MAP.put("Gray", CategoryPanel.COLOR_GRAY); //$NON-NLS-1$
		COLOR_MAP.put("Red", CategoryPanel.COLOR_RED); //$NON-NLS-1$
		COLOR_MAP.put("Yellow", CategoryPanel.COLOR_YELLOW); //$NON-NLS-1$
		COLOR_MAP.put("Green", CategoryPanel.COLOR_GREEN); //$NON-NLS-1$
		COLOR_MAP.put("Blue", CategoryPanel.COLOR_BLUE); //$NON-NLS-1$
		COLOR_MAP.put("Purple", CategoryPanel.COLOR_PURPLE); //$NON-NLS-1$
	}
	
	/**
	 * 名称为“Uncategorized”的类别。
	 */
	public static final Category UNCATEGORIED = new Category("Uncategorized", CategoryPanel.COLOR_GRAY); //$NON-NLS-1$
	
	/**
	 * 颜色信息，一定是以下常量中的一种：<p>
	 * <code>CategoryPanel.COLOR_GRAY</code>
	 * <code>CategoryPanel.COLOR_RED</code>
	 * <code>CategoryPanel.COLOR_YELLOW</code>
	 * <code>CategoryPanel.COLOR_GREEN</code>
	 * <code>CategoryPanel.COLOR_BLUE</code>
	 * <code>CategoryPanel.COLOR_PURPLE</code>
	 * </p>
	 */
	public final int color;
	
	/**
	 * 类别名称。一定是非空字符串。
	 */
	public final String name;
	
	public Category(String name, int color) {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException(International.CategoryNameCannotBeNull);
		}
		this.name = name;
		this.color = color;
	}
	
	public Category(String name) {
		this(name, CategoryPanel.COLOR_BLUE);
	}
	
	/**
	 * 从指定的成员模型上解读类别信息。
	 * <p>
	 * 如果该成员上没有描述类别信息的标签，或者标签不合法，均返回<code>Category.UNCATEGORIED</code>。
	 * </p>
	 * @param member 指定的成员模型
	 * @return 类别
	 */
	public static final Category parseCategory(IMemberModel member) {
		ITag[] tags = member.getTags("category"); //$NON-NLS-1$
		if (tags.length == 0) {
			return UNCATEGORIED;
		} else {
			String name = tags[0].getPropertyValue("name"); //$NON-NLS-1$
			String color = tags[0].getPropertyValue("color"); //$NON-NLS-1$
			if (name == null || name.length() == 0) {
				return UNCATEGORIED;
			} else {
				Integer c = COLOR_MAP.get(color == null ? "Gray" : color); //$NON-NLS-1$
				return new Category(name, c == null ? CategoryPanel.COLOR_GRAY : c);
			}
		}
	}
	
	public ITag toTag() {
		Tag tag = new Tag();
		tag.setName("category"); //$NON-NLS-1$
		tag.addProperty("name", name); //$NON-NLS-1$
		switch (color) {
		case CategoryPanel.COLOR_RED:
			tag.addProperty("color", "Red"); //$NON-NLS-1$ //$NON-NLS-2$
			break;
		case CategoryPanel.COLOR_YELLOW:
			tag.addProperty("color", "Yellow"); //$NON-NLS-1$ //$NON-NLS-2$
			break;
		case CategoryPanel.COLOR_GREEN:
			tag.addProperty("color", "Green"); //$NON-NLS-1$ //$NON-NLS-2$
			break;
		case CategoryPanel.COLOR_BLUE:
			tag.addProperty("color", "Blue"); //$NON-NLS-1$ //$NON-NLS-2$
			break;
		case CategoryPanel.COLOR_PURPLE:
			tag.addProperty("color", "Purple"); //$NON-NLS-1$ //$NON-NLS-2$
			break;
		}
		return tag;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Category) {
			Category c = (Category) obj;
			return c.name.equals(name);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
