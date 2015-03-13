package fatcat.snowberry.dp;

import java.util.HashMap;
import java.util.UUID;

import fatcat.snowberry.dp.schema.ISchema;
import fatcat.snowberry.dp.schema.Relationship;
import fatcat.snowberry.dp.schema.Role;
import fatcat.snowberry.tag.IMemberModel;

/**
 * 设计模式实例类。
 * <p>
 * 设计模式实例由一个特定的Schema生成，这个Schema包含了设计模式的名称、成员角色和角色间关系等信息。
 * 设计模式实例中包含一组具体的成员模型，来表示有哪些成员参与了这个设计模式。
 * </p>
 * 
 * @author 张弓
 *
 */
public class DesignPattern {
	
	private final ISchema schema;
	private final UUID id;
	private final HashMap<IMemberModel, Role> roleMap;
	private final HashMap<IMemberModel, String> commentMap;
	
	/**
	 * 从指定的标签池构造一个设计模式。
	 * @param pool 指定的标签池
	 * @return 设计模式，若信息不完整则返回{@code null}
	 */
	public static DesignPattern createFromPool(PatternTagPool pool) {
		UUID id = pool.getSchemaID();
		if (id == null) return null;
		ISchema schema = DesignPatternCore.getSchema(id);
		if (schema == null) return null;
		return new DesignPattern(schema, pool);
	}
	
	private DesignPattern(ISchema schema, PatternTagPool pool) {
		this.schema = schema;
		id = pool.getID();
		roleMap = new HashMap<IMemberModel, Role>();
		commentMap = new HashMap<IMemberModel, String>();
		
		// create content
		
		final Role[] roles = schema.getRoles();
		final PatternTag[] tags = pool.getTags();
		
		for (PatternTag tag : tags) {
			
			Role role = null;
			
			// find the role
			for (Role r : roles) {
				if (r.getName().equals(tag.getRoleName())) {
					role = r;
					break;
				}
			}
			
			// add to map
			if (role != null) {
				final IMemberModel model = tag.getHostModel();
				roleMap.put(model, role);
				commentMap.put(model, tag.getComment());
			}
			
		}
		
	}
	
	/**
	 * 取得这个设计模式的ID。
	 * @return 设计模式ID
	 */
	public UUID getID() {
		return id;
	}
	
	/**
	 * 取得这个设计模式的Schema。
	 * @return 设计模式的Schema
	 */
	public ISchema getSchema() {
		return schema;
	}

	/**
	 * 取得Schema的ID。
	 * @return Schema的ID
	 */
	public UUID getSchemaID() {
		return schema.getID();
	}

	/**
	 * 取得设计模式的名称。
	 * @return 设计模式的名称
	 */
	public String getPatternName() {
		return schema.getPatternName();
	}

	/**
	 * 取得设计模式中的所有关系。
	 * @return 关系数组
	 */
	public Relationship[] getRelationships() {
		return schema.getRelationships();
	}

	/**
	 * 取得设计模式中的所有角色。
	 * @return 角色数组
	 */
	public Role[] getRoles() {
		return schema.getRoles();
	}
	
	/**
	 * 取得参与此设计模式的所有成员模型。
	 * <p>
	 * <strong>
	 * 注意：
	 * </strong>
	 * 如果一个成员被标识为参与了此设计模式，但标签的信息有误（如发现未知的角色），此模型将不被列入返回的数组。
	 * </p>
	 * @return 成员模型数组
	 */
	public IMemberModel[] getModels() {
		return roleMap.keySet().toArray(new IMemberModel[0]);
	}
	
	/**
	 * 取得成员在此设计模式中的角色。
	 * <p>
	 * 若成员没有参与此设计模式，或成员的角色未知，均返回<code>null</code>。
	 * @param model 指定的成员
	 * @return 角色或{@code null}
	 */
	public Role getRole(IMemberModel model) {
		return roleMap.get(model);
	}
	
	/**
	 * 取得成员在此设计模式中的注释。
	 * <p>
	 * 若成员没有参与此设计模式，或成员的角色未知，均返回<code>null</code>。
	 * @param model 指定的成员
	 * @return 注释或{@code null}
	 */
	public String getComment(IMemberModel model) {
		return commentMap.get(model);
	}
	
	/**
	 * 取得此设计模式中两个角色之间的关系。
	 * <p>
	 * 若指定的角色间没有有效的关系描述信息，返回<code>null</code>。
	 * @param r1 角色1
	 * @param r2 角色2
	 * @return 关系或{@code null}
	 */
	public Relationship getRelationship(Role r1, Role r2) {
		return schema.getRelationship(r1, r2);
	}
	
	/**
	 * 取得此设计模式中两个成员之间的关系。
	 * <p>
	 * 若指定的成员间没有有效的关系描述信息，返回<code>null</code>。
	 * @param model1 成员1
	 * @param model2 成员2
	 * @return 关系或{@code null}
	 */
	public Relationship getRelationship(IMemberModel model1, IMemberModel model2) {
		return getRelationship(getRole(model1), getRole(model2));
	}
	
	/**
	 * 生成调试用的、可读的设计模式信息字符串。
	 */
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("[Design Pattern] ID = " + getID());
		str.append("\n\t");
		str.append("Schema: " + getSchema().getPatternName());
		IMemberModel[] models = getModels();
		for (IMemberModel model : models) {
			str.append("\n\t");
			str.append("[" + getRole(model).getName() + "]: ");
			str.append(model.getJavaElement().getElementName());
		}
		return str.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof DesignPattern) {
			return id.equals(((DesignPattern) obj).id);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
