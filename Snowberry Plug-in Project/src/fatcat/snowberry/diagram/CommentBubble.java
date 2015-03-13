package fatcat.snowberry.diagram;

import java.awt.Font;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.snowberry.gui.Bubble;
import fatcat.snowberry.gui.Label;
import fatcat.snowberry.gui.MultilineLabel;

public class CommentBubble extends Bubble {
	
//	private final Label label;
	private final Label title;
	private final MultilineLabel mLabel;

	public CommentBubble(Container owner, String title_text, String text) {
		super(owner);
		
		title = new Label(this);
		title.setAutoSize(true);
		title.setLocation(6, 6);
		title.setText(title_text);
		title.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		
		mLabel = new MultilineLabel(this);
		mLabel.setLocation(12, title.getBottom() + 12);
		setWidth(getPreferredWidth());
		mLabel.setRightSpace(6);
		setText(text);
		mLabel.setHeight(mLabel.getPreferredHeight());
	}
	
	@Override
	public int preferredHeight(Component c) {
		return mLabel == null ? 0 : mLabel.getBottom() + 6;
	}
	
	@Override
	public int preferredWidth(Component c) {
		return title == null ? 0 : Math.max(title.getWidth() + 12, 220);
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		if (mLabel != null) {
			mLabel.setText(text);
		}
	}

}
