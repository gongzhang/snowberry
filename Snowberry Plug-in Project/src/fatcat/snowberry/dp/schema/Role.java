package fatcat.snowberry.dp.schema;


public class Role {
	
	private final ISchema schema;
	private final String name;
	
	public Role(ISchema schema, String name) {
		this.schema = schema;
		this.name = name;
	}
	
	public ISchema getSchema() {
		return schema;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		else if (obj instanceof Role) {
			return name.equals(((Role) obj).name);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
