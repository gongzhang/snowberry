package fatcat.snowberry.dp;

import java.util.LinkedList;
import java.util.UUID;

/**
 * 模式标签池。
 * <p>
 * 所有的标签池实例会被Snowberry维护，保证当前所有属于同一设计模式的标签均在同一个标签池中，并实时响应代码改变。
 * 
 * </p>
 * @author 张弓
 *
 */
public class PatternTagPool {
	
	private final UUID id;
	private final LinkedList<PatternTag> patternTags;
	
	PatternTagPool(UUID id) {
		this.id = id;
		patternTags = new LinkedList<PatternTag>();
	}
	
	/**
	 * 取得这个设计模式的ID。
	 * @return 设计模式ID
	 */
	public UUID getID() {
		return id;
	}
	
	/**
	 * 取得Schema的ID。
	 * @return Schema的ID，若不存在则返回{@code null}
	 */
	public UUID getSchemaID() {
		for (PatternTag tag : patternTags) {
			UUID uuid = tag.getSchemaID();
			if (uuid != null) return uuid;
		}
		return null;
	}
	
	/**
	 * 取得标签池中的所有标签。
	 * @return 标签数组
	 */
	public PatternTag[] getTags() {
		return patternTags.toArray(new PatternTag[0]);
	}
	
	/**
	 * 标签池中当前标签的个数。
	 * @return 标签个数
	 */
	public int size() {
		return patternTags.size();
	}
	
	// Callback
	void addTag(PatternTag tag) {
		patternTags.add(tag);
	}
	
	// Callback
	void removeTag(PatternTag tag) {
		patternTags.remove(tag);
	}

}
