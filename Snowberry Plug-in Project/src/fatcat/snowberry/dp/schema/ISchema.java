package fatcat.snowberry.dp.schema;

import java.util.UUID;

import fatcat.snowberry.tag.ITag;


public interface ISchema {
	
	public UUID getID();
	
	public String getPatternName();
	
	public String getComment();
	
	public Role[] getRoles();
	
	public Relationship[] getRelationships();
	
	public ITag toTag();
	
	public Relationship getRelationship(Role r1, Role r2);

}
