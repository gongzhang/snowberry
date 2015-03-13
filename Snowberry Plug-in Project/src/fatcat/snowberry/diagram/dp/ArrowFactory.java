package fatcat.snowberry.diagram.dp;

import fatcat.gui.snail.Container;
import fatcat.snowberry.diagram.ClassDiagram;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.dp.schema.Relationship;
import fatcat.snowberry.gui.Arrow;

public class ArrowFactory {

	public static Arrow createArrow(DesignPattern pattern, ClassDiagram cd1, ClassDiagram cd2, Container owner) {
		Relationship relationship = pattern.getRelationship(cd1.getModel(), cd2.getModel());
		DependencyArrow rst = null;
		if (relationship == null)
			return null;

		switch (relationship.getKind()) {
		case Relationship.GENERALIZATION:
			rst = new GeneralizationArrow(owner, cd2, cd1);
			break;
			
		case Relationship.ASSOCIATION:
			rst = new AssociationArrow(owner, cd2, cd1);
			break;
			
		case Relationship.AGGREGATION:
		case Relationship.COMPOSITION:
			rst = new AggregationArrow(owner, cd2, cd1);
			break;

		default:
			rst = new DependencyArrow(owner, cd1, cd2);
			break;
		}
		rst.setComment(relationship.getName());
		rst.setLabel1(relationship.getLabel1());
		rst.setLabel2(relationship.getLabel2());
		return rst;
	}
}
