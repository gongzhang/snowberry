package fatcat.snowberry.tag.internal;

import java.util.ArrayList;

import fatcat.snowberry.core.International;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.TagResolveException;


public class Tag implements ITag {
	
	private IMemberModel host;
	private String name;
	private final ArrayList<String> propertyNames, propertyValues;	
	
	{
		host = null;
		name = ""; //$NON-NLS-1$
		propertyNames = new ArrayList<String>();
		propertyValues = new ArrayList<String>();
	}

	@Override
	public final IMemberModel getHostModel() {
		return host;
	}
	
	public void setHostElement(IMemberModel host) {
		if (host == null) throw new IllegalArgumentException();
		this.host = host; 
	}

	@Override
	public final String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (name == null) throw new IllegalArgumentException();
		this.name = name; 
	}
	
	public void addProperty(String name, String value) {
		int i = propertyNames.indexOf(name);
		if (i != -1) throw new IllegalArgumentException(International.Attribute + name + International.AlreadyExist);
		propertyNames.add(name);
		propertyValues.add(value);
	}
	
	public void removeProperty(int i) {
		propertyNames.remove(i);
		propertyValues.remove(i);
	}
	
	public void removeProperty(String name) {
		int i = propertyNames.indexOf(name);
		if (i != -1) {
			removeProperty(i);
		}
	}
	
	public void setProperty(int index, String value) {
		propertyValues.set(index, value);
	}
	
	public void setProperty(String name, String value) {
		int i = propertyNames.indexOf(name);
		if (i == -1) throw new IllegalArgumentException(International.CannotFindAttribute + name);
		setProperty(i, value);
	}

	@Override
	public final String getPropertyName(int index) {
		return propertyNames.get(index);
	}

	@Override
	public final String[] getPropertyNames() {
		return propertyNames.toArray(new String[0]);
	}

	@Override
	public final String getPropertyValue(int index) {
		return propertyValues.get(index);
	}

	@Override
	public final String getPropertyValue(String name) {
		int i = propertyNames.indexOf(name);
		if (i == -1) return null;
		return propertyValues.get(i);
	}

	@Override
	public final String[] getPropertyValues() {
		return propertyValues.toArray(new String[0]);
	}

	@Override
	public final int size() {
		return propertyNames.size();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof ITag) {
			ITag tag = (ITag) obj;
			if (!name.equals(tag.getName())) return false;
			if (size() != tag.size()) return false;
			for (int i = 0; i < size(); i++) {
				if (!propertyNames.get(i).equals(tag.getPropertyName(i)) ||
					!propertyValues.get(i).equals(tag.getPropertyValue(i)))
					return false;
			}
			return true;
		} else return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() + propertyNames.hashCode() + propertyValues.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("[Tag:" + name + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		for (int i = 0; i < size(); i++)
			str.append("\n\t" + propertyNames.get(i) + " = " + propertyValues.get(i)); //$NON-NLS-1$ //$NON-NLS-2$
		return str.toString();
	}

	@Override
	public boolean isLegal() {
		try {
			ITag tag = TagParser.parseTag(TagParser.parseString(this, false));
			return this.equals(tag);
		} catch (TagResolveException e) {
			return false;
		}
	}

}
