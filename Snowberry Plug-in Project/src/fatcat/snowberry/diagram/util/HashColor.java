package fatcat.snowberry.diagram.util;

import java.awt.Color;


public class HashColor {
	
	public static Color getColor(Object o) {
		int hashcode = Math.abs(o.hashCode() - (o.toString() + "snowberry").hashCode());
		hashcode %= 360;
		return Color.getHSBColor(hashcode / 360.0f, 0.9f, 0.7f);
	}

}
