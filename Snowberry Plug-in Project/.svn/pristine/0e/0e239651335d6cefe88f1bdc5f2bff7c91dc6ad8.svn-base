package fatcat.snowberry.diagram;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import fatcat.gui.GraphicsX;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;

import fatcat.gui.snail.event.ComponentAdapter;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.dp.DesignPatternCore;
import fatcat.snowberry.dp.dialogs.DPCreator;
import fatcat.snowberry.gui.Button;
import fatcat.snowberry.gui.InfoPanel;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.MultiFileTask;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagTask;


public class InfoBox extends InfoPanel {
	
	private final ITypeModel model;
	private InfoContent content;
	private final ClassDiagram cd;
	private final ComponentAdapter COMPONENT_ADAPTER = new ComponentAdapter() {
		public void componentResized(Component c) {
			setLocation(c.getWidth() + 8, 0);
		}
	};

	public InfoBox(ClassDiagram owner) {
		super(owner);
		cd = owner;
		model = owner.getModel();
		cd.addComponentListener(COMPONENT_ADAPTER);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentRemoved(Component c) {
				cd.removeComponentListener(COMPONENT_ADAPTER);
			}
		});
		content = new InfoContent(this, model);
		getShell().syncExec(new Runnable() {

			@Override
			public void run() {
				content.setSize();
				setSize();
			}
			
		});
	}
	
	public ClassDiagram getDiagram() {
		return cd;
	}
	
	@Override
	public void refreshLayout(Container c) {
		super.refreshLayout(c);
		if (isInitialized()) {
			content.setBorder(6, 6, 6, 16);
		}
	}
	
	@Override
	public int preferredWidth(Component c) {
		return super.preferredWidth(c);
	}
	
	@Override
	public int preferredHeight(Component c) {
		if (isInitialized()) {
			return content.getBottom() + 40;
		} else {
			return super.preferredHeight(c);
		}
	}
	
	private float timer = 0.0f;
	
	@Override
	protected void update(double dt) {
		super.update(dt);
		if (timer + dt < 0.5) timer += dt;
		else timer = 0.5f;
	}
	
	@Override
	protected void repaintComponent(GraphicsX g2) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, timer * 2.0f));
		super.repaintComponent(g2);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
	
	@Override
	protected boolean isLegalWidth(int width) {
		return width > 100;
	}
	
	@Override
	protected boolean isLegalHeight(int height) {
		return height > 100;
	}
	
	public ITypeModel getModel() {
		return model;
	}

}

class InfoContent extends Container {
	
	private final ITypeModel model;
	private final DesignPattern[] patterns;
	private final fatcat.snowberry.gui.Label title;
	private final fatcat.snowberry.gui.Label proj;
	private final fatcat.snowberry.gui.Label author;
	private final fatcat.snowberry.gui.Label info;
	private final fatcat.snowberry.gui.Label dp;
	
	private final Button btnNewPattern;
	private final fatcat.snowberry.gui.Label lblNewPattern;

	public InfoContent(InfoBox owner, ITypeModel model) {
		super(owner);
		this.model = model;
		patterns = DesignPatternCore.getPatterns(model);
		setClip(true);
		
		title = new fatcat.snowberry.gui.Label(this);
		title.setAutoSize(true);
		title.setFont(new Font(Font.DIALOG, Font.PLAIN, 18)); //$NON-NLS-1$
		title.setText(model.getJavaElement().getFullyQualifiedName());
		title.setLocation(0, 0);
		
		proj = new fatcat.snowberry.gui.Label(this);
		proj.setAutoSize(true);
		proj.setFont(new Font(Font.DIALOG, Font.PLAIN, 12)); //$NON-NLS-1$
		proj.setColor(0x404040);
		proj.setText(International.FromProject + model.getOwnerProject().getResource().getName());
		proj.setLocation(0, title.getBottom() + 2);
		
		author = new fatcat.snowberry.gui.Label(this);
		author.setAutoSize(true);
		author.setFont(proj.getFont());
		author.setColor(proj.getColor());
		author.setText(International.Author + (model.getAuthor() == null ? International.Anonymous : model.getAuthor()));
		author.setLocation(0, proj.getBottom() + 2);
		
		info = new fatcat.snowberry.gui.Label(this);
		info.setAutoSize(true);
		info.setFont(proj.getFont());
		info.setColor(proj.getColor());
		StringBuffer info_buf = new StringBuffer();
		info_buf.append(International.Contain);
		info_buf.append(model.getMemberModels().length);
		info_buf.append(International.NumberOfMembers);
		info_buf.append(Label.parseLabels(model).length);
		info_buf.append(International.NumberOfLabels);
		info.setText(info_buf.toString());
		info.setLocation(0, author.getBottom() + 2);
		
		dp = new fatcat.snowberry.gui.Label(this);
		dp.setAutoSize(true);
		dp.setFont(title.getFont());
		dp.setText(String.format(International.NumberOfDesignPatterns, patterns.length));
		dp.setLocation(0, info.getBottom() + 12);
		
		////
		
		for (DesignPattern dp : patterns) {
			new DPInfoItem(this, dp, getModel());
		}
		
		////
		
		btnNewPattern = new Button(this);
		btnNewPattern.disableBuffer();
		btnNewPattern.setImage(ClassDiagram.ICON_ADD);
		btnNewPattern.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(Component c, MouseEvent e) {
				getOwner().remove();
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						Shell shell = SnowberryCore.getDefaultShell();
						DPCreator creator = new DPCreator(shell);
						creator.open(getModel());
					}
				});
			}
		});
		lblNewPattern = new fatcat.snowberry.gui.Label(this, International.CreateDesignPatternInstance);
		lblNewPattern.setAutoSize(true);
		lblNewPattern.setFont(proj.getFont());
		lblNewPattern.setColor(proj.getColor());
	}
	
	@Override
	public int preferredHeight(Component c) {
		if (preferred_height != 0) return preferred_height;
		else return super.preferredHeight(c);
	}
	
	private int preferred_height;
	
	@Override
	public void refreshLayout(Container c) {
		btnNewPattern.setLocation(0, getHeight() - btnNewPattern.getHeight());
		lblNewPattern.setLocation(btnNewPattern.getRight() + 2, btnNewPattern.getTop() + 3);
		
		preferred_height = dp.getBottom() + 4;
		Component[] cs = c.getComponents();
		for (Component component : cs) {
			if (component instanceof DPInfoItem) {
				component.setLocation(4, preferred_height);
				preferred_height += component.getHeight() + 4;
			}
		}
	}
	
	public ITypeModel getModel() {
		return model;
	}
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setColor(Color.gray);
		g2.drawLine(4, info.getBottom() + 4, getWidth() - 4, info.getBottom() + 4);
	}
	
}

class DPInfoItem extends Container implements ITagFilter {
	
	private final fatcat.snowberry.gui.Label title, des, cmt;
	private final Button btnDetails, btnExit;
	
	private final DesignPattern pattern;
	private final IMemberModel model;

	public DPInfoItem(InfoContent owner, DesignPattern pattern, IMemberModel model) {
		super(owner);
		
		this.pattern = pattern;
		this.model = model;
		
		title = new fatcat.snowberry.gui.Label(this);
		title.setAutoSize(true);
		title.setFont(new Font(Font.DIALOG, Font.BOLD, 14)); //$NON-NLS-1$
		title.setColor(0x404040);
		title.setText(pattern.getPatternName() + String.format(International.Members2, pattern.getModels().length));
		title.setLocation(18, 0);
		
		des = new fatcat.snowberry.gui.Label(this);
		des.setAutoSize(true);
		des.setFont(new Font(Font.DIALOG, Font.PLAIN, 12)); //$NON-NLS-1$
		des.setColor(title.getColor());
		des.setText(International.AS + pattern.getRole(model).getName() + International.ParticipateThisDesignPattern);
		des.setLocation(18, title.getBottom() + 1);
		
		cmt = new fatcat.snowberry.gui.Label(this);
		cmt.setAutoSize(true);
		cmt.setFont(des.getFont());
		cmt.setColor(title.getColor());
		String comment = pattern.getComment(model);
		cmt.setText(" - " + ((comment == null || comment.length() == 0) ? International.NoComment : comment)); //$NON-NLS-1$
		cmt.setLocation(18, des.getBottom() + 1);
		
		btnDetails = new Button(this);
		btnDetails.disableBuffer();
		btnDetails.setImage(ICON_DETAILS);
		btnDetails.setLocation(title.getRight() + 4, -1);
		btnDetails.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(Component c, MouseEvent e) {
				btnDetails_Clicked();
			}
		});
		
		btnExit = new Button(this);
		btnExit.disableBuffer();
		btnExit.setImage(ClosableDiagram.ICON_CLOSE);
		btnExit.setLocation(btnDetails.getRight() + 2, -1);
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(Component c, MouseEvent e) {
				btnExit_Clicked();
			}
		});
	}
	
	public final static BufferedImage ICON_DP = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/types.gif"); //$NON-NLS-1$
	public final static BufferedImage ICON_DETAILS = GraphicsX.createImage("/fatcat/snowberry/gui/res/eclipse-icons/discovery.gif"); //$NON-NLS-1$
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.drawImage(ICON_DP, 0, 0, null);
	}
	
	@Override
	public int preferredWidth(Component c) {
		if (btnExit != null) {
			final int w = Math.max(btnExit.getRight(), des.getRight());
			return Math.max(cmt.getRight(), w);
		} else {
			return 120;
		}
	}
	
	@Override
	public int preferredHeight(Component c) {
		return 54;
	}
	
	private void btnDetails_Clicked() {
		getOwner().getOwner().remove();
//		if (model.getKind() == IMemberModel.TYPE) {
//			((DiagramFrame) getFrame()).showDesignPattern(pattern, (ITypeModel) model);
//		} else {
//			((DiagramFrame) getFrame()).showDesignPattern(pattern, model.getOwnerType());
//		}
		((DiagramFrame) getFrame()).showDesignPattern(pattern);
	}
	
	private void btnExit_Clicked() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				int reply = SnowberryCore.showMsgBox(International.RemoveDesignPatternInstance, International.ConfirmDismissDesignPattern, SWT.YES | SWT.NO | SWT.CANCEL);
				if (reply == SWT.YES) {
					getOwner().getOwner().remove();
					MultiFileTask task = new MultiFileTask();
					IMemberModel[] models = pattern.getModels();
					for (IMemberModel model : models) {
						task.removeTag(model, DPInfoItem.this);
					}
					try {
						task.execute();
					} catch (SourceEditingException e) {
//						SnowberryCore.println(e.getMessage());
						SnowberryCore.log(Status.ERROR, International.CannotRemoveDesignPattern, e);
						SnowberryCore.showErrMsgBox(International.RemoveDesignPatternInstance, International.CannotRemoveDesignPattern);
					}
				} else if (reply == SWT.NO) {
					getOwner().getOwner().remove();
					TagTask task = new TagTask();
					task.removeTag(model, DPInfoItem.this);
					try {
						task.execute();
					} catch (SourceEditingException e) {
//						SnowberryCore.println(e.getMessage());
						SnowberryCore.log(Status.ERROR, International.CannotRemoveDesignPattern, e);
						SnowberryCore.showErrMsgBox(International.RemoveDesignPatternInstance, International.CannotRemoveDesignPattern);
					}
				}
			}
		});
		
	}

	@Override
	public boolean isAccepted(ITag tag) {
		if (tag.getName().equals("pattern")) { //$NON-NLS-1$
			String id = tag.getPropertyValue("id"); //$NON-NLS-1$
			if (id != null && id.equals(pattern.getID().toString())) {
				return true;
			}
		}
		return false;
	}
	
}
