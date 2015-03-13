package fatcat.snowberry.diagram;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.JavaModelException;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.ILayout;

import fatcat.gui.snail.event.ContainerListener;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.gui.Button;
import fatcat.snowberry.gui.CategoryPanel;
import fatcat.snowberry.gui.DiagramPanel;
import fatcat.snowberry.gui.StandardContent;
import fatcat.snowberry.gui.StandardItem;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IMemberModelListener;
import fatcat.snowberry.tag.ITypeModel;


public class ClassDiagram extends DiagramPanel implements IMemberModelListener {
	
	public static final int STATE_MINIMIZED = 0;
	public static final int STATE_STANDARD = 1;
	
	private int state;
	private final Button cornerButton;
	
	public static final BufferedImage ICON_ADD = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/add_correction.gif");
	public static final BufferedImage ICON_SUB = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/remove_correction.gif");
	
	private final ITypeModel model;
	private final HashMap<IMemberModel, MemberItem<?>> itemMap;
	private final HashMap<String, SortableCategoryPanel> categoryMap;
	private final StandardContent content;
	private final DiagramToolBar toolBar;

	public ClassDiagram(final DiagramFrame owner, ITypeModel typeModel) {
		super(owner.getDiagramContainer());
		model = typeModel;
		model.addMemberModelListener(this);
		title.setSpace(24);
		title.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(Component c, MouseEvent e) {
				if (e.getClickCount() == 2) {
					owner.resetDiagramLayout(ClassDiagram.this);
				}
			}
		});
		cornerButton = new Button(title);
		cornerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(Component c, MouseEvent e) {
				switch (state) {
				case STATE_MINIMIZED:
					normalize();
//					setState(STATE_STANDARD, true);
					break;
				default:
					minimize();
//					setState(STATE_MINIMIZED, true);
					break;
				}
			}
		});
		resizer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDragged(Component c, MouseEvent e, int x, int y) {
				if (getHeight() <= 70) state = STATE_MINIMIZED;
				else state = STATE_STANDARD;
				if (content != null) {
					content.setVisible(state != STATE_MINIMIZED);
				}
				resetCornerButtonIcon();
			}
		});
		
		toolBar = new DiagramToolBar(title, this);
		
		modelChanged();
		setState(STATE_MINIMIZED , false);
		
		title.setLayout(new ILayout() {
			@Override
			public void refreshLayout(Container c) {
				cornerButton.setLocation(c.getWidth() - 25, 1);
				toolBar.setLocation(1, 24);
				toolBar.setSize(c.getWidth() - 2, 21);
			}
		});
		
		itemMap = new HashMap<IMemberModel, MemberItem<?>>();
		categoryMap = new HashMap<String, SortableCategoryPanel>();
		super.content.scrollPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(Component c, MouseEvent e) {
				content.clearSelection();
			}
		});
		content = new StandardContent(getClientContainer());
		// CategoryPanel sorter
		final Comparator<Component> categoryPanelSorter = new Comparator<Component>() {

			@Override
			public int compare(Component o1, Component o2) {
				if (!(o1 instanceof CategoryPanel)) {
					return 1;
				} else if (!(o2 instanceof CategoryPanel)) {
					return -1;
				} else if (((CategoryPanel) o1).getText().endsWith(Category.UNCATEGORIED.name)) {
					return -1;
				} else if (((CategoryPanel) o2).getText().endsWith(Category.UNCATEGORIED.name)) {
					return 1;
				} else {
					return ((CategoryPanel) o1).getText().compareTo(((CategoryPanel) o2).getText());
				}
			}
			
		};
		content.addContainerListener(new ContainerListener() {
			
			@Override
			public void childRemoved(Container arg0, Component arg1) {
				arg0.sortChildren(categoryPanelSorter);
			}
			
			@Override
			public void childAdded(Container arg0, Component arg1) {
				arg0.sortChildren(categoryPanelSorter);
			}
			
		});
		categoryMap.put(Category.UNCATEGORIED.name, new SortableCategoryPanel(content, Category.UNCATEGORIED.name, CategoryPanel.COLOR_GRAY));
		IMemberModel[] memberModels = model.getMemberModels();
		for (IMemberModel memberModel : memberModels) {
			this.memberModelAdded(memberModel); // 手工触发
		}
	}
	
	public DiagramFrame getDiagramFrame() {
		return (DiagramFrame) getOwner().getOwner();
	}
	
	public final StandardItem[] getSelectedItems() {
		return content.getSelection();
	}
	
	public final IMemberModel[] getSelectedModels() {
		LinkedList<IMemberModel> rst = new LinkedList<IMemberModel>();
		StandardItem[] items = content.getSelection();
		for (StandardItem item : items) {
			if (item instanceof CompositeItem) {
				IMemberModel[] models = ((CompositeItem) item).getModels();
				for (IMemberModel model : models) rst.add(model);
			} else {
				rst.add((IMemberModel) ((MemberItem<?>) item).getModel());
			}
		}
		return rst.toArray(new IMemberModel[0]);
	}
	
	public final ITypeModel getModel() {
		return model;
	}
	
	public Category[] getAllCategories(boolean include_uncategorized) {
		LinkedList<Category> rst = new LinkedList<Category>();
		IMemberModel[] models = model.getMemberModels();
		for (IMemberModel model : models) {
			Category c = Category.parseCategory(model);
			if (!rst.contains(c)) {
				if (!include_uncategorized && c.name.equals(Category.UNCATEGORIED.name)) {
					continue;
				}
				rst.add(c);
			}
		}
		return rst.toArray(new Category[0]);
	}
	
	public final int getState() {
		return state;
	}
	
	@Override
	public int preferredWidth(Component c) {
		switch (state) {
		case STATE_MINIMIZED:
			return 150;
		default:
			return 220;
		}
	}
	
	@Override
	public int preferredHeight(Component c) {
		switch (state) {
		case STATE_MINIMIZED:
			return 70;
		default:
			return 350;
		}
	}
	
	@Override
	protected boolean isLegalWidth(int width) {
		return width >= 120;
	}
	
	@Override
	protected boolean isLegalHeight(int height) {
		return height >= 70;
	}
	
	public void setState(int state, boolean animation) {
//		if (this.state != state) {
			this.state = state;
			if (animation) resizeTo(getPreferredWidth(), getPreferredHeight());
			else setSize();
			if (content != null) {
				content.setVisible(state != STATE_MINIMIZED);
			}
//		}
		resetCornerButtonIcon();
	}
	
	public void minimize() {
		if (state == STATE_STANDARD) {
			final int left = getLeft() - (150 - getWidth()) / 2;
			final int top = getTop() - (70 - getHeight()) / 2;
			moveTo(left, top);
			setState(STATE_MINIMIZED, true);
		}
	}
	
	public void normalize() {
		if (state == STATE_MINIMIZED) {
			final int left = getLeft() + (getWidth() - 220) / 2;
			final int top = getTop() + (getHeight() - 350) / 2;
			moveTo(left, top);
			setState(STATE_STANDARD, true);
		}
	}
	
	private void resetCornerButtonIcon() {
		switch (state) {
		case STATE_MINIMIZED:
			cornerButton.setImage(ICON_ADD);
			break;
		default:
			cornerButton.setImage(ICON_SUB);
			break;
		}
	}
	
	public static final Font TITLE_NORMAL = new Font(Font.DIALOG, Font.PLAIN, 12);
	public static final Font TITLE_ITALIC = new Font(Font.DIALOG, Font.ITALIC, 12);
	public static final Font TITLE_BOLD = new Font(Font.DIALOG, Font.BOLD, 12);
	
	public static final BufferedImage ICON_CLASS = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/class_obj(1).gif");
	public static final BufferedImage ICON_INTERFACE = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/int_obj.gif");
	
	public void modelChanged() {
		title.setText(model.getJavaElement().getTypeQualifiedName());
		int f;
		try {
			f = model.getJavaElement().getFlags();
		} catch (JavaModelException e) {
			f = 0;
		}
		if (Flags.isAbstract(f) || Flags.isInterface(f)) {
			title.setFont(TITLE_ITALIC);
		} else if (Flags.isPublic(f)) {
			title.setFont(TITLE_BOLD);
		} else {
			title.setFont(TITLE_NORMAL);
		}
		if (Flags.isInterface(f)) {
			title.setImage(ICON_INTERFACE);
		} else {
			title.setImage(ICON_CLASS);
		}
	}
	
	public void dispose() {
		for (IMemberModel model : itemMap.keySet()) {
			itemMap.get(model).dispose();
		}
		model.removeMemberModelListener(this);
	}

	@Override
	public void memberModelAdded(final IMemberModel memberModel) {
		getShell().syncExec(new Runnable() {
			@Override
			public void run() {
				addItem(memberModel);
			}
		});
	}

	@Override
	public void memberModelChanged(final IMemberModel memberModel) {
		getShell().syncExec(new Runnable() {
			@Override
			public void run() {
				MemberItem<?> item = itemMap.get(memberModel);
                if (item != null) {
                	
                	if (item instanceof CompositeItem) {
                		
                		IMemberModel[] models = ((CompositeItem) item).getModels();
                		for (IMemberModel model : models) {
                			ClassDiagram.this.memberModelRemoved(model);
                		}
                		for (IMemberModel model : models) {
                			ClassDiagram.this.memberModelAdded(model);
                		}
                		
                	} else {
                		
                		ClassDiagram.this.memberModelRemoved(memberModel);
						ClassDiagram.this.memberModelAdded(memberModel);
						
                	}
    					
                }
			}
		});
	}

	@Override
	public void memberModelRemoved(final IMemberModel memberModel) {
		getShell().syncExec(new Runnable() {
			@Override
			public void run() {
				
				// 检测是否是成组对象
				MemberItem<?> item = itemMap.get(memberModel);
				if (item instanceof CompositeItem) {
					
					CompositeItem group = (CompositeItem) item;
					if (group.size() > 2) {
						
						group.removeMember(memberModel);
						itemMap.remove(memberModel);
						
					} else if (group.size() == 2) {
						
						// 去除组中成员
						group.removeMember(memberModel);
						itemMap.remove(memberModel);
						
						// 去掉分组
						IMemberModel model = group.getModel(0);
						removeItem(model);
						addItem(model);
						
					} else {
						
						assert false;
						
					}
					
				} else {
					removeItem(memberModel);
				}
			}
		});
	}
	
	private void addItem(IMemberModel memberModel) {
		// 取得类别面板
		final SortableCategoryPanel panel = prepareCategoryPanel(memberModel);
		
		// 判断是否属于某个分组
		for (IMemberModel model : itemMap.keySet()) {
			MemberItem<?> item = itemMap.get(model);
			if (item.inSameGroupWith(memberModel)) {
				if (item instanceof CompositeItem) {
					
					// 找到CompositeItem
					((CompositeItem) item).addMember(memberModel);
					itemMap.put(memberModel, item);
					
				} else {
					
					// 需要创建CompositeItem
					CompositeItem groupItem = CompositeItem.createCompositeItem(panel, model, memberModel);
					item.remove();
					itemMap.put(model, groupItem);
					itemMap.put(memberModel, groupItem);
					
				}
				
				return;
			}
		}
		
		// 创建成员对象
		MemberItem<?> item = MemberItem.createMemberItem(panel, memberModel);
		itemMap.put(memberModel, item);
	}
	
	private SortableCategoryPanel prepareCategoryPanel(IMemberModel memberModel) {
		// 解析成员类别信息
		Category category = Category.parseCategory(memberModel);
		
		// 查找当前的类别面板
		SortableCategoryPanel panel = categoryMap.get(category.name);
		
		// 未找到则立即创建新面板
		if (panel == null) {
			panel = new SortableCategoryPanel(content, category.name, category.color); // DONE （低优先）成员排序功能
			categoryMap.put(category.name, panel);
		}
		
		// 设置面板颜色
		if (category.color != CategoryPanel.COLOR_GRAY) {
			panel.setColor(category.color);
		}
		
		return panel;
	}
	
	private void removeItem(IMemberModel memberModel) {
		MemberItem<?> item = itemMap.get(memberModel);
		if (item != null) {
			
			// 取消当前选择的成员对象
			content.clearSelection();
			
			// 取得类别面板，然后删除成员对象
			SortableCategoryPanel panel = (SortableCategoryPanel) item.getOwner();
			item.remove();
			item.dispose();
			itemMap.remove(memberModel);
			
			// 如果同一个类别里已经没有成员，则删除这个类别（“UNCATEGORIED”除外）
			if (panel.size() == 0 &&
				!panel.getText().equals(Category.UNCATEGORIED.name)) {
				panel.remove();
				categoryMap.remove(panel.getText());
			}
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof ClassDiagram) {
			return getModel().equals(((ClassDiagram) obj).getModel());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return getModel().hashCode();
	}
	
}

class SortableCategoryPanel extends CategoryPanel implements ContainerListener, Comparator<Component>, Runnable {

	public SortableCategoryPanel(StandardContent owner, String text, int color) {
		super(owner, text, color);
		addContainerListener(this);
	}

	@Override
	public void childAdded(Container parent, Component c) {
		getShell().syncExec(this);
	}

	@Override
	public void childRemoved(Container parent, Component c) {
		getShell().syncExec(this);
	}

	@Override
	public int compare(Component o1, Component o2) {
		if (!(o1 instanceof MemberItem<?>)) {
			return 1;
		} else if (!(o2 instanceof MemberItem<?>)) {
			return -1;
		} else {
			// both o1 and o2 are MemberItem
			if (o1 instanceof FieldItem) {
				if (o2 instanceof FieldItem) {
					return ((FieldItem) o1).getText().compareToIgnoreCase(((FieldItem) o2).getText());
				} else {
					return -1;
				}
			} else {
				if (o2 instanceof FieldItem) {
					return 1;
				} else {
					return ((MemberItem<?>) o1).getText().compareToIgnoreCase(((MemberItem<?>) o2).getText());
				}
			}
		}
	}

	@Override
	public void run() {
		sortChildren(this);
	}
	
}
