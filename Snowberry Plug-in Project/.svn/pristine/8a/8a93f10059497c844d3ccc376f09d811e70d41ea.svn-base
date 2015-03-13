package fatcat.snowberry.diagram;

import java.awt.event.MouseEvent;

import org.eclipse.swt.widgets.Display;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.Frame;
import fatcat.gui.snail.event.FocusListener;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.core.International;
import fatcat.snowberry.diagram.actions.ViewCode;
import fatcat.snowberry.dp.debug.views.DesignPatternView;
import fatcat.snowberry.gui.CategoryPanel;
import fatcat.snowberry.gui.StandardItem;
import fatcat.snowberry.tag.IFieldModel;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IMethodModel;


abstract public class MemberItem<T> extends StandardItem {
	
	public static final MemberItem<?> createMemberItem(CategoryPanel owner, Object model) {
		if (model instanceof IMemberModel) {
			switch (((IMemberModel) model).getKind()) {
			
			case IMemberModel.FIELD:
				return new FieldItem(owner, (IFieldModel) model);
	
			case IMemberModel.METHOD:
				return new MethodItem(owner, (IMethodModel) model);
				
			default:
				return null;
			}
		} else {
			return null;
		}
	}
	
	protected final T model;

	public MemberItem(CategoryPanel owner, final T model) {
		super(owner);
		this.model = model;
		modelChanged();
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(Component c, MouseEvent e) {
				// 双击查看代码
				if (e.getButton() == 1 && e.getClickCount() == 2 &&
					model instanceof IMemberModel) {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							new ViewCode((IMemberModel) model).fireAction();
						}
					});
				} else if (e.getButton() == 1 && e.getClickCount() == 1 &&
						model instanceof IMemberModel) {
					if (DesignPatternView.getInstance() != null) {
						DesignPatternView.getInstance().showPatternOn((IMemberModel) model);
					}
				}
			}
		});
		
		// 查看标签
		if (model instanceof IMemberModel) {
			addFocusListener(new FocusListener() {
				
				private LabelBubble bubble = null;
				
				@Override
				public void lostFocus(Component c) {
					if (bubble != null) {
						bubble.remove();
						bubble = null;
					}
				}
				
				@Override
				public void gotFocus(Component c) {
					if (Label.parseLabels((IMemberModel) model).length != 0) {
						final ClassDiagram cd = MemberItem.this.getDiagram();
						bubble = new LabelBubble(cd, (IMemberModel) model);
						getShell().syncExec(new Runnable() {
							@Override
							public void run() {
								bubble.setLocation(
									getAbsLeft() - cd.getAbsLeft() - bubble.getWidth(),
									getAbsTop() - cd.getAbsTop() + (getHeight() - bubble.getHeight()) / 2
								);
							}
						});
						
					}
				}
				
			});
		}
		
	}
	
	public Category getCategory() {
		CategoryPanel panel = (CategoryPanel) getOwner();
		return new Category(panel.getText(), panel.getColor());
	}
	
	public final T getModel() {
		return model;
	}
	
	abstract public void modelChanged();
	
	public boolean inSameGroupWith(IMemberModel item) {
		return false;
	}
	
	public ClassDiagram getDiagram() {
		Container owner = getOwner();
		while (!(owner instanceof ClassDiagram) &&
				!(owner instanceof Frame)) {
			owner = owner.getOwner();
		}
		if (owner instanceof ClassDiagram) {
			return (ClassDiagram) owner;
		} else {
			throw new UnsupportedOperationException(International.MemberItemMustBeNestedInClassDiagram);
		}
	}
	
	/**
	 * 如果自身在逻辑上处于某个<code>CompositeItem</code>中，则返回这个<code>CompositeItem</code>；
	 * 否则返回自身。
	 * @return 顶层的<code>MemberItem</code>
	 */
	public MemberItem<?> getTopLevelItem() {
		Container c = getOwner();
		do {
			if (c instanceof CompositeItemViewer) {
				return ((CompositeItemViewer) c).getCompositeItem();
			} else if (c instanceof ClassDiagram) {
				return this;
			} else {
				c = c.getOwner();
			}
		} while (true);
	}
	
	public void dispose() {
		
	}

}
