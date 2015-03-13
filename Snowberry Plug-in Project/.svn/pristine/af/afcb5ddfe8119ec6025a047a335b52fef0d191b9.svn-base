package fatcat.snowberry.dp.debug.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import fatcat.snowberry.core.International;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.dp.DesignPatternCore;
import fatcat.snowberry.tag.IMemberModel;


public class DesignPatternView extends ViewPart implements ControlListener {
	
	private Tree tree = null;
	TreeColumn nameColumn = null;
	TreeColumn valueColumn = null;
	
	private static DesignPatternView instance;
	
	public static DesignPatternView getInstance() {
		return instance;
	}
	
	public DesignPatternView() {
		instance = this;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		tree = new Tree(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		nameColumn = new TreeColumn(tree, SWT.NONE);
		nameColumn.setText(International.Name);
		valueColumn = new TreeColumn(tree, SWT.NONE);
		valueColumn.setText(International.Value);
		tree.addControlListener(this);
	}

	@Override
	public void setFocus() {
		instance = this;
		tree.setFocus();
	}

	@Override
	public void controlMoved(ControlEvent e) {
	}

	@Override
	public void controlResized(ControlEvent e) {
		nameColumn.setWidth(tree.getSize().x / 2);
		valueColumn.setWidth(tree.getSize().x / 2);
	}
	
	public void showPatternOn(IMemberModel model) {
		if (tree.isDisposed()) return;
		final DesignPattern[] dps = DesignPatternCore.getPatterns(model);
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				tree.removeAll();
				for (DesignPattern dp : dps) {
					addPatternToTree(dp);
				}
			}
		});
	}
	
	private void addPatternToTree(DesignPattern dp) {
		final TreeItem patternItem = new TreeItem(tree, SWT.NONE);
		patternItem.setText(0, dp.getPatternName() + International.DesignPattern);
		IMemberModel[] models = dp.getModels();
		patternItem.setText(1, models.length + International.Members);
		for (IMemberModel model : models) {
			TreeItem item = new TreeItem(patternItem, SWT.NONE);
			item.setText(0, dp.getRole(model).getName());
			item.setText(1, model.getJavaElement().getElementName());
		}
	}

}
