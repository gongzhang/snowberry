package fatcat.snowberry.dp;

import java.util.UUID;

import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;

/**
 * 用于描述设计模式的标签。
 * <p>
 * 一个设计模式标签的名称必须为“pattern”，拥有名为“id”和“schema”的两个<code>UUID</code>属性，分别表示参与的设计模式的ID和Schema的ID。
 * 必须拥有名为“role”的非空属性，用于表示参与的角色。另外可以拥有“Comment”属性记录注释。
 * </p>
 * @author 张弓
 *
 */
public class PatternTag implements ITag {
	
	private final ITag tag;
	
	/**
	 * 通过现有标签构造一个设计模式标签。
	 * @param tag 名称为“pattern”的标签
	 */
	public PatternTag(ITag tag) {
		if (!tag.getName().equals("pattern")) {
			throw new IllegalArgumentException("tag name must be \"pattern\"");
		}
		this.tag = tag;
	}
	
	/**
	 * 取得设计模式ID。
	 * @return 模式ID，若标签不合法则返回{@code null}
	 */
	public UUID getID() {
		String str = getPropertyValue("id");
		if (str == null) return null;
		try {
			return UUID.fromString(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 取得Schema的ID。
	 * @return Schema的ID，若标签不合法则返回{@code null}
	 */
	public UUID getSchemaID() {
		String str = getPropertyValue("schema");
		if (str == null) return null;
		try {
			return UUID.fromString(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 取得角色名称。
	 * @return 角色名称，若标签不合法则返回{@code null}
	 */
	public String getRoleName() {
		String str = getPropertyValue("role");
		if (str == null) return null;
		else if (str.length() == 0) return null;
		else return str;
	}
	
	/**
	 * 取得注释。
	 * @return 注释内容（若不存在则返回长度为0的字符串）
	 */
	public String getComment() {
		String str = getPropertyValue("comment");
		if (str == null) str = "";
		return str;
	}

	@Override
	public IMemberModel getHostModel() {
		return tag.getHostModel();
	}

	@Override
	public String getName() {
		return tag.getName();
	}

	@Override
	public String getPropertyName(int index) {
		return tag.getPropertyName(index);
	}

	@Override
	public String[] getPropertyNames() {
		return tag.getPropertyNames();
	}

	@Override
	public String getPropertyValue(int index) {
		return tag.getPropertyValue(index);
	}

	@Override
	public String getPropertyValue(String name) {
		return tag.getPropertyValue(name);
	}

	@Override
	public String[] getPropertyValues() {
		return tag.getPropertyValues();
	}

	@Override
	public boolean isLegal() {
		return getID() != null && getSchemaID() != null && getRoleName() != null && tag.isLegal();
	}

	@Override
	public int size() {
		return tag.size();
	}
	
	@Override
	public boolean equals(Object obj) {
		return tag.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return tag.hashCode();
	}

}
