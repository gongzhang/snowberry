package fatcat.snowberry.views;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;

import fatcat.gui.GraphicsX;
import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.Frame;
import fatcat.gui.snail.SnailShell;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.core.International;
import fatcat.snowberry.diagram.ClassDiagram;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.dp.DesignPatternCore;
import fatcat.snowberry.gui.InfoPanel;
import fatcat.snowberry.gui.Label;
import fatcat.snowberry.gui.MultilineLabel;
import fatcat.snowberry.gui.ScrollPanel;
import fatcat.snowberry.gui.SwitchButton;
import fatcat.snowberry.search.IResultListener;
import fatcat.snowberry.search.SearchCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.views.internal.DPInfoItem;
import fatcat.snowberry.views.internal.Icon;
import fatcat.snowberry.views.internal.LabelItem;

public class HyperdocFrame extends Frame implements IResultListener<IMemberModel> {
	
	private final InfoPanel skin;
	private final ScrollPanel scrollPanel;
	private IMemberModel currentModel;
	private fatcat.snowberry.diagram.Label[] currentlabels;
	private final HyperdocView hyperdocViewPart;
	
	private boolean busy_flag = true;
	
	public IMemberModel getCurrentModel() {
		return currentModel;
	}
	
	public HyperdocView getHyperdocViewPart() {
		return hyperdocViewPart;
	}

	public HyperdocFrame(SnailShell shell, HyperdocView hyperdocViewPart) {
		super(shell);
		busy_flag = true;
		this.hyperdocViewPart = hyperdocViewPart;
		setClip(true);
		enableBuffer();
		skin = new InfoPanel(this);
		skin.setVisible(false);
		setSkin(skin);
		scrollPanel = new ScrollPanel(this);
		scrollPanel.setGradientVisible(false);
		btnShowHyperInfo = new SwitchButton(this);
		btnShowHyperInfo.setImage(IMG_JAVADOC);
		btnShowHyperInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(Component c, MouseEvent e, int x, int y) {
				btnShowHyperInfo_Clicked();
			}
		});
		busy_flag = false;
	}
	
	private void btnShowHyperInfo_Clicked() {
		if (btnShowHyperInfo.isSelected()) {
			Component[] cs = scrollPanel.getClientContainer().getComponents();
			for (Component c : cs) {
				if (c instanceof LabelItem || c instanceof DPInfoItem) {
					c.setVisible(false);
				}
			}
		} else {
			Component[] cs = scrollPanel.getClientContainer().getComponents();
			for (Component c : cs) {
				if (c instanceof LabelItem || c instanceof DPInfoItem) {
					c.setVisible(true);
				}
			}
		}
	}
	
	@Override
	public void refreshLayout(Container c) {
		if (skin != null && scrollPanel != null && btnShowHyperInfo != null) {
			skin.setBorder(0, 0, 0, 0);
			scrollPanel.setBorder(10, 10, 10, 10);
			btnShowHyperInfo.setLocation(getWidth() - 24, 5);
		}
	}
	
	private final SwitchButton btnShowHyperInfo;
	
	public static final BufferedImage IMG_JAVADOC = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/javadoc(2).gif");
	
	public static final Font FONT_DIALOG_15 = new Font(Font.DIALOG, Font.PLAIN, 15);
	public static final Font FONT_DIALOG_17B = new Font(Font.DIALOG, Font.BOLD, 17);
	public static final Font FONT_DIALOG_13B = new Font(Font.DIALOG, Font.BOLD, 13);
	public static final Font FONT_DIALOG_13 = new Font(Font.DIALOG, Font.PLAIN, 13);
	public static final Font FONT_DIALOG_11 = new Font(Font.DIALOG, Font.PLAIN, 11);
	
	public void showDoc(final IMemberModel model) {
		if (busy_flag) return;
		
		getShell().syncExec(new Runnable() {
			
			@Override
			public void run() {
				busy_flag = true;
				currentModel = model;
				final Container content = scrollPanel.getClientContainer();
				content.removeAll();
				
				// title icon
				Icon icon = new Icon(content, ClassDiagram.ICON_CLASS);
				icon.setTop(2);
				
				ITypeModel typeModel = null;
				if (model.getKind() == IMemberModel.TYPE) {
					typeModel = (ITypeModel) model;
				} else {
					typeModel = model.getOwnerType();
				}
				
				// title
				Label title = new Label(content);
				title.setFont(FONT_DIALOG_15);
				title.setText(typeModel.getJavaElement().getFullyQualifiedName());
				title.setLocation(icon.getRight() + 2, 0);
				
				String str_type = model.getKind() == IMemberModel.TYPE ? International.Class : model.getKind() == IMemberModel.METHOD ? International.Method : International.Field;
				
				Label name = new Label(content);
				name.setFont(FONT_DIALOG_17B);
				name.setText(str_type + model.getJavaElement().getElementName());
				name.setLocation(0, title.getBottom() + 2);
				
				// title icon
				Icon javadoc_icon = new Icon(content, IMG_JAVADOC);
				javadoc_icon.setLocation(0, name.getBottom() + 4);
				
				// javadoc
				Label javadoc_label = new Label(content);
				javadoc_label.setFont(FONT_DIALOG_13B);
				javadoc_label.setText("Javadoc");
				javadoc_label.setLocation(javadoc_icon.getRight() + 2, name.getBottom() + 4);
				
				MultilineLabel javadoc = new MultilineLabel(content);
				javadoc.setFont(FONT_DIALOG_13);
				javadoc.setLocation(javadoc_label.getLeft(), javadoc_label.getBottom() + 12);
				javadoc.setRightSpace(0);
				javadoc.setLineSpace(4);
				
				Javadoc node = model.getASTNode().getJavadoc();
				StringBuffer doc = new StringBuffer();
				if (node != null) {
					try {
						List<?> texts = node.tags();
						for (Object text : texts) {
							TagElement te = (TagElement) text;
							if (te.getTagName() == null) {
								List<?> frags = te.fragments();
								for (Object frag : frags) {
									doc.append(((TextElement) frag).getText());
									doc.append('\n');
								}
							} else if (!te.getTagName().endsWith("@tag")) {
								doc.append(te.getTagName());
								doc.append('\n');
								List<?> frags = te.fragments();
								for (Object frag : frags) {
									doc.append(((TextElement) frag).getText());
									doc.append('\n');
								}
							}
							doc.append('\n');
						}
					} catch (Exception e) {
					}
				} else {
					doc.append(International.NoJavadocOnThisElement);
				}
				
				javadoc.setText(doc.toString().trim());
				javadoc.setHeight(javadoc.getPreferredHeight());
				
				content.setHeight(javadoc.getBottom());
				
				currentlabels = fatcat.snowberry.diagram.Label.parseLabels(model);
				SearchCore.searchMember(HyperdocFrame.this, new ITagFilter() {
					@Override
					public boolean isAccepted(ITag tag) {
						if (!tag.getName().equals("label")) return false;
						for (fatcat.snowberry.diagram.Label label : currentlabels) {
							if (label.name.endsWith(tag.getPropertyValue("name"))) {
								return true;
							}
						}
						return false;
					}
				});
			}
		});
	}

	@Override
	public void done(boolean stoppedManually) {
		getShell().syncExec(new Runnable() {
			@Override
			public void run() {
				DesignPattern[] dps = DesignPatternCore.getPatterns(currentModel);
				for (DesignPattern dp : dps) {
					addDPResult(currentModel, dp);
				}
				busy_flag = false;
			}
		});
	}

	@Override
	public boolean gotResult(final IMemberModel result) {
		getShell().syncExec(new Runnable() {
			@Override
			public void run() {
				fatcat.snowberry.diagram.Label[] labels = fatcat.snowberry.diagram.Label.parseLabels(result);
				for (fatcat.snowberry.diagram.Label l1 : labels) {
					for (fatcat.snowberry.diagram.Label l2 : currentlabels) {
						if (l1.name.equals(l2.name)) {
							addLabelResult(result, l1);
						}
					}
				}
			}
		});
		return false;
	}
	
	private void addLabelResult(final IMemberModel result, fatcat.snowberry.diagram.Label label) {
		if (currentModel.equals(label.model)) return;
		Container content = scrollPanel.getClientContainer();
		LabelItem result2 = new LabelItem(content, result, label);
		result2.setLocation(0, content.getHeight() + 16);
		content.setHeight(result2.getBottom());
	}
	
	private void addDPResult(final IMemberModel model, DesignPattern pattern) {
		Container content = scrollPanel.getClientContainer();
		DPInfoItem item = new DPInfoItem(content, pattern, model, this);
		item.setLocation(0, content.getHeight() + 16);
		item.setWidth(content.getWidth());
		item.setHeight(item.getPreferredHeight());
		content.setHeight(item.getBottom());
	}

}


