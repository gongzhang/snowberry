package fatcat.snowberry.diagram.dp;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;

import fatcat.gui.snail.Container;
import fatcat.snowberry.diagram.ClassDiagram;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.dp.schema.Relationship;
import fatcat.snowberry.dp.schema.Role;
import fatcat.snowberry.gui.Arrow;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITypeModel;


public class DPLayout {
	
	public static LinkedList<Arrow> doLayout(final Rectangle area, final DesignPattern pattern, final LinkedList<ClassDiagram> cds, final Container arrowContainer) {
		
		final HashMap<ITypeModel, ClassDiagram> cdMap = new HashMap<ITypeModel, ClassDiagram>();
		for (ClassDiagram cd : cds) cdMap.put(cd.getModel(), cd);
		final Relationship[] relationships = pattern.getRelationships();
		final LinkedList<Arrow> arrows = new LinkedList<Arrow>();
		final LinkedList<ClassDiagram> done = new LinkedList<ClassDiagram>();
		
		// arrows
		for (ClassDiagram i : cds) {
			for (ClassDiagram j : cds) {
				Arrow a = ArrowFactory.createArrow(pattern, i, j, arrowContainer);
				if (a != null) arrows.add(a);
			}
		}
		
		// deal with GENERALIZATION
		int tree_cnt = 0;
		final LinkedList<Role> roles = new LinkedList<Role>();
		for (Relationship relationship : relationships) {
			if (relationship.getKind() == Relationship.GENERALIZATION) {
				if (!roles.contains(relationship.getRole1())) {
					roles.add(relationship.getRole1());
					tree_cnt += getModels(pattern, relationship.getRole1()).length;
				}
			}
		}
		roles.clear();
		
		int[] tree_x = new int[tree_cnt];
		for (int i = 0; i < tree_cnt; i++) {
			tree_x[i] = area.width * (i + 1) / (tree_cnt + 1);
		}
		
		int i = 0;
		for (Relationship relationship : relationships) {
			if (relationship.getKind() == Relationship.GENERALIZATION) {
				if (!roles.contains(relationship.getRole1())) {
					roles.add(relationship.getRole1());
					ITypeModel[] roots = getModels(pattern, relationship.getRole1());
					for (ITypeModel root : roots) {
						moveTo(cdMap.get(root), tree_x[i], area.height / 3 + area.y);
						done.add(cdMap.get(root));
						i++;
					}
				}
			}
		}
		roles.clear();
		
		i = 0;
		final ClassDiagram[] roots = done.toArray(new ClassDiagram[0]);
		for (ClassDiagram root : roots) {
			final LinkedList<ITypeModel> children = new LinkedList<ITypeModel>();
			for (ClassDiagram cd : cds) {
				if (!done.contains(cd) && (cd.getModel().isSubclassOf(root.getModel()) || cd.getModel().isImplementOf(root.getModel()))) {
					children.add(cd.getModel());
					done.add(cd);
				}
			}
			
			int top = area.height * 2 / 3 + area.y;
			int left = tree_x[i] - (children.size()) * 90 + 15;
			for (ITypeModel sub : children) {
				cdMap.get(sub).moveTo(left, top);
				left += 180;
			}

			i++;
		}
		
		// reset
		if (done.size() > 0) {
			
			cds.removeAll(done);
			done.clear();
			
			int left = 30, top = 30 + area.y;
			for (ClassDiagram cd : cds) {
				cd.moveTo(left, top);
				top += cd.getPreferredHeight() + 30;
				if (top + 70 > area.height) {
					left += 180;
					top = 30 + area.y;
				}
			}
			
		} else {

			final Role[] roles2 = pattern.getRoles();
			int[] line_y = new int[roles2.length];
			for (int j = 0; j < line_y.length; j++) {
				line_y[j] = area.height * (j + 1) / (line_y.length + 1) + area.y;
			}
			i = 0;
			for (Role role : roles2) {
				final ITypeModel[] models = getModels(pattern, role);
				int[] line_x = new int[models.length];
				for (int k = 0; k < line_x.length; k++) {
					line_x[k] = area.width * (k + 1) / (line_x.length + 1);
				}
				int k = 0;
				for (ITypeModel model : models) {
					moveTo(cdMap.get(model), line_x[k], line_y[i]);
					k++;
				}
				i++;
			}
			
		}
		
		return arrows;
	}
	
	private static ITypeModel[] getModels(final DesignPattern pattern, final Role role) {
		final LinkedList<ITypeModel> rst = new LinkedList<ITypeModel>();
		IMemberModel[] models = pattern.getModels();
		for (IMemberModel model : models) {
			if (model.getKind() == IMemberModel.TYPE && role.equals(pattern.getRole(model))) {
				rst.add((ITypeModel) model);
			}
		}
		return rst.toArray(new ITypeModel[0]);
	}
	
	private static void moveTo(final ClassDiagram cd, final int x, final int y) {
		cd.moveTo(x - 75, y - 35);
	}

}
