package fatcat.snowberry.dp.schema.internal;

import java.util.LinkedList;
import java.util.UUID;

import fatcat.snowberry.dp.schema.ISchema;
import fatcat.snowberry.dp.schema.Relationship;
import fatcat.snowberry.dp.schema.Role;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.internal.Tag;


public class Schema implements ISchema {
	
	private final UUID uuid;
	private final String patternName;
	private final String comment;
	private final LinkedList<Role> roles;
	private final LinkedList<Relationship> relationships;
	
	public Schema(UUID uuid, String patternName, String comment) {
		this.uuid = uuid;
		this.patternName = patternName;
		this.comment = comment;
		roles = new LinkedList<Role>();
		relationships = new LinkedList<Relationship>();
	}
	
	public Schema(String patternName, String comment) {
		this(UUID.randomUUID(), patternName, comment);
	}

	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public String getPatternName() {
		return patternName;
	}

	@Override
	public Relationship[] getRelationships() {
		return relationships.toArray(new Relationship[0]);
	}

	@Override
	public Role[] getRoles() {
		return roles.toArray(new Role[0]);
	}

	@Override
	public UUID getID() {
		return uuid;
	}
	
	public void addRole(Role r) {
		if (!roles.contains(r)) {
			roles.add(r);
		}
	}
	
	public void addRelationship(Relationship r) {
		relationships.add(r);
	}

	@Override
	public ITag toTag() {
		Tag tag = new Tag();
		tag.setName("schema");
		tag.addProperty("id", uuid.toString());
		tag.addProperty("name", patternName);
		tag.addProperty("comment", comment);
		StringBuffer str_roles = new StringBuffer();
		if (roles.size() > 0) {
			str_roles.append(roles.get(0).getName());
			for (int i = 1; i < roles.size(); i++) {
				str_roles.append(", ");
				str_roles.append(roles.get(i).getName());
			}
		}
		tag.addProperty("roles", str_roles.toString());
		int i = 1;
		for (Relationship r : relationships) {
			tag.addProperty("relationship-" + i, r.getQualifiedString());
			i++;
		}
		return tag;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		else if (obj instanceof ISchema) {
			return ((ISchema) obj).getID().equals(uuid);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

	@Override
	public Relationship getRelationship(Role r1, Role r2) {
		for (Relationship r : relationships) {
			if (r.getRole1().equals(r1) && r.getRole2().equals(r2)) {
				return r;
			}
		}
		return null;
	}

}
