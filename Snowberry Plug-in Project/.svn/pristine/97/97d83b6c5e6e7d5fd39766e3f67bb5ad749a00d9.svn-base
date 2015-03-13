package fatcat.snowberry.dp.schema;

/**
 * 类与类之间的关系。
 * @author 张弓
 *
 */
public class Relationship {
	
	private final Role role1, role2;
	private final int kind;
	private final String name, label1, label2;
	private final ISchema schema;
	
	// http://en.wikipedia.org/wiki/Class_diagram
	// http://www.360doc.com/content/06/0804/11/10266_173133.shtmlRelationship
	
	/**
	 * 关联。
	 */
	public static final int ASSOCIATION = 0;
	
	/**
	 * 聚合。
	 */
	public static final int AGGREGATION = 1;
	
	/**
	 * 组合。
	 */
	public static final int COMPOSITION = 2;
	
	/**
	 * 范化。
	 */
	public static final int GENERALIZATION = 3;
	
	/**
	 * 依赖。
	 */
	public static final int DEPENDENCY = 4;
	
	private static String kindToString(int kind) {
		switch (kind) { // Do NOT change these strings!!!
		case ASSOCIATION:
			return "Association";
		case AGGREGATION:
			return "Aggregation";
		case COMPOSITION:
			return "Composition";
		case GENERALIZATION:
			return "Generalization";
		case DEPENDENCY:
			return "Dependency";
		default:
			return "Unknown";
		}
	}
	
	public Relationship(ISchema schema, int kind, Role role1, Role role2) {
		this(schema, kind, role1, role2, null, null, null);
	}
	
	public Relationship(ISchema schema, int kind, Role role1, Role role2, String label1, String label2) {
		this(schema, kind, role1, role2, null, label1, label2);
	}
	
	public Relationship(ISchema schema, int kind, Role role1, Role role2, String name) {
		this(schema, kind, role1, role2, name, null, null);
	}
	
	public Relationship(ISchema schema, int kind, Role role1, Role role2, String name, String label1, String label2) {
		this.schema = schema;
		this.role1 = role1;
		this.role2 = role2;
		this.kind = kind;
		if (name == null) name = "";
		if (label1 == null) label1 = "";
		if (label2 == null) label2 = "";
		this.name = name;
		this.label1 = label1;
		this.label2 = label2;
	}
	
	public ISchema getSchema() {
		return schema;
	}
	
	public int getKind() {
		return kind;
	}
	
	public Role getRole1() {
		return role1;
	}
	
	public Role getRole2() {
		return role2;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLabel1() {
		return label1;
	}
	
	public String getLabel2() {
		return label2;
	}
	
	public String getQualifiedString() {
		StringBuffer sb = new StringBuffer();
		sb.append(kindToString(kind));
		sb.append(", ");
		sb.append(role1.getName());
		sb.append(", ");
		sb.append(role2.getName());
		if (name.length() == 0 &&
			label1.length() == 0 &&
			label2.length() == 0) {
			return sb.toString();
		} else if (name.length() > 0 &&
				label1.length() == 0 &&
				label2.length() == 0) {
			sb.append(", ");
			sb.append(name);
			return sb.toString();
		} else if (name.length() == 0 &&
			(label1.length() > 0 ||
			label2.length() > 0)) {
			sb.append(", ");
			sb.append(label1);
			sb.append(", ");
			sb.append(label2);
			return sb.toString();
		} else {
			sb.append(", ");
			sb.append(name);
			sb.append(", ");
			sb.append(label1);
			sb.append(", ");
			sb.append(label2);
			return sb.toString();
		}
	}
	
	/**
	 * 通过规范字符串构造关系实例。
	 * <p>
	 * 若字符串格式错误，或<code>schema</code>中找不到相应的角色，抑或出现其他问题，均返回<code>null</code>。
	 * </p>
	 * @param qualifiedString 描述关系的规范字符串
	 * @param schema 指定的设计模式Schema
	 * @return 关系或{@code null}
	 */
	public static Relationship parseRelationship(String qualifiedString, ISchema schema) {
		Role[] roles = schema.getRoles();
		String[] parts = qualifiedString.split(",");
		for (int i = 0; i < parts.length; i++) {
			parts[i] = parts[i].trim();
		}
		Object[] v = parseKindAndRoles(parts, roles);
		if (v == null) return null;
		switch (parts.length) {
		case 3:
			return new Relationship(schema, (Integer) v[0], (Role) v[1], (Role) v[2]);
		case 4:
			return new Relationship(schema, (Integer) v[0], (Role) v[1], (Role) v[2], parts[3]);
		case 5:
			return new Relationship(schema, (Integer) v[0], (Role) v[1], (Role) v[2], parts[3], parts[4]);
		case 6:
			return new Relationship(schema, (Integer) v[0], (Role) v[1], (Role) v[2], parts[3], parts[4], parts[5]);
		default:
			return null;
		}
	}
	
	private static Object[] parseKindAndRoles(String[] parts, Role[] roles) {
		if (parts.length < 3) return null;
		if (parts[0].length() == 0 || parts[1].length() == 0 || parts[2].length() == 0) {
			return null;
		}
		
		Object[] rst = new Object[3];
		if (parts[0].equals("Association")) {
			rst[0] = ASSOCIATION;
		} else if (parts[0].equals("Aggregation")) {
			rst[0] = AGGREGATION;
		} else if (parts[0].equals("Composition")) {
			rst[0] = COMPOSITION;
		} else if (parts[0].equals("Generalization")) {
			rst[0] = GENERALIZATION;
		} else if (parts[0].equals("Dependency")) {
			rst[0] = DEPENDENCY;
		} else {
			return null;
		}
		
		FIND_ROLE: for (int i = 1; i <= 2; i++) {
			for (int j = 0; j < roles.length; j++) {
				if (roles[j].getName().equals(parts[i])) {
					rst[i] = roles[j];
					continue FIND_ROLE;
				}
			}
			return null;
		}
		
		return rst;
	}

}
