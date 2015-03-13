package fatcat.snowberry.diagram;

import java.awt.event.MouseEvent;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.IArea;
import fatcat.gui.snail.event.MouseAdapter;
import fatcat.snowberry.core.International;
import fatcat.snowberry.gui.InfoPanel;
import fatcat.snowberry.gui.util.Dragger;
import fatcat.snowberry.search.IConnection;
import fatcat.snowberry.tag.IMemberModel;

public class ConnectionPanel extends InfoPanel {

	private final IConnection connection;
	private boolean resize_lock = true;
	private final IArea def_location;

	public ConnectionPanel(Container owner, IConnection connection, IArea def_location) {
		super(owner);
		this.connection = connection;
		this.def_location = def_location;
		
		Dragger dragger = new Dragger(this) {
			@Override
			public void mouseDragged(Component c, MouseEvent e, int x, int y) {
				super.mouseDragged(c, e, x, y);
				ConnectionPanel.this.mouseDragged(c, e, x, y);
			}
		};
		this.addMouseListener(dragger);

		// create labels
		int top = 4;
		int max_right = 0;
		Label[] labels = connection.getLabels();
		for (Label label : labels) {
			LabelElement element = new LabelElement(this, label, connection
					.getResult());
			element.setSize();
			element.setLocation(4, top);
			element.addMouseListener(dragger);
			top += element.getHeight() + 4;
			if (element.getRight() > max_right) {
				max_right = element.getRight();
			}
		}

		if (connection.getReferencedCount() != 0) {
			fatcat.snowberry.gui.Label txt_ref = new fatcat.snowberry.gui.Label(
					this, String.format(International.ReferencedTimes, connection.getReferencedCount()));
			txt_ref.setLocation(4, top);
			txt_ref.addMouseListener(dragger);
			top += txt_ref.getHeight() + 4;
			if (txt_ref.getRight() > max_right) {
				max_right = txt_ref.getRight();
			}
		}

		if (connection.getPatterns().length != 0) {
			fatcat.snowberry.gui.Label txt_dp = new fatcat.snowberry.gui.Label(
					this, String.format(International.ParticipatePatterns, connection.getPatterns().length));
			txt_dp.setLocation(4, top);
			txt_dp.addMouseListener(dragger);
			top += txt_dp.getHeight() + 4;
			if (txt_dp.getRight() > max_right) {
				max_right = txt_dp.getRight();
			}
		}

		resize_lock = false;
		setSize(max_right + 16, top);
		resize_lock = true;
	}
	
	protected void mouseDragged(Component c, MouseEvent e, int x, int y) {
		
	}
	
	@Override
	protected boolean isLegalLeft(int left) {
		if (def_location != null) {
			return Math.abs(left + getWidth() / 2 - def_location.getLeft() - def_location.getWidth() / 2) < getWidth() / 2 + 8;
		}
		return true;
	}
	
	@Override
	protected boolean isLegalTop(int top) {
		if (def_location != null) {
			return Math.abs(top + getHeight() / 2 - def_location.getTop() - def_location.getHeight() / 2) < getHeight() / 2 + 8;
		}
		return true;
	}

	@Override
	protected boolean isLegalHeight(int height) {
		return !resize_lock;
	}

	@Override
	protected boolean isLegalWidth(int width) {
		return !resize_lock;
	}

	public IConnection getConnection() {
		return connection;
	}

	@Override
	public int preferredWidth(Component c) {
		return 0;
	}

	@Override
	public int preferredHeight(Component c) {
		return 0;
	}

}

class LabelElement extends LabelComponent {

	public LabelElement(Container owner, Label label, IMemberModel model) {
		super(owner, label, model);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(Component c) {
				LabelElement.this.mouseEntered();
			}

			@Override
			public void mouseExited(Component c) {
				LabelElement.this.mouseExited();
			}
		});
	}

	private CommentBubble bubble;

	private void mouseEntered() {
		if (getLabel().comment != null && getLabel().comment.length() > 0) {
			StringBuffer title = new StringBuffer("- ");
			switch (getLabel().model.getKind()) {
			case IMemberModel.FIELD:
				title.append(International.Field);
				break;
			case IMemberModel.METHOD:
				title.append(International.Method);
				break;
			case IMemberModel.TYPE:
				title.append(International.Class);
				break;
			}
			title.append(getLabel().model.getJavaElement().getElementName());
			title.append(International.ItsComment);
			bubble = new CommentBubble(getOwner(), title.toString(), getLabel().comment);
			bubble.setSize();
			bubble.setLocation(getLeft() - bubble.getWidth() - 8, getTop() + 8 - bubble.getHeight() / 2);
		}
	}

	private void mouseExited() {
		if (bubble != null) {
			bubble.remove();
			bubble = null;
		}
	}

}
