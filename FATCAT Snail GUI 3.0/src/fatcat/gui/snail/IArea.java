package fatcat.gui.snail;

public interface IArea {

	public int getLeft();

	public int getTop();

	public int getRight();
	
	public int getRightSpace();

	public int getBottom();
	
	public int getBottomSpace();

	public int getAbsLeft();

	public int getAbsTop();

	public int getAbsRight();

	public int getAbsBottom();

	public int getWidth();

	public int getHeight();

	public int getPreferredWidth();

	public int getPreferredHeight();

	public void setLeft(int left);
	
	public void setLeft(IArea area);

	public void setTop(int top);
	
	public void setTop(IArea area);

	public void setRight(int right);
	
	public void setRight(IArea area);
	
	public void setRightSpace(int right_space);

	public void setBottom(int bottom);
	
	public void setBottom(IArea area);
	
	public void setBottomSpace(int bottom_space);

	public void setWidth(int width);
	
	public void setWidth(IArea area);

	public void setHeight(int height);
	
	public void setHeight(IArea area);

	public void setLocation(int left, int top);
	
	public void setLocation(IArea area);

	public void setSize(int width, int height);
	
	public void setSize(IArea area);

	public void setSize();

	public void setBounds(int left, int top, int width, int height);
	
	public void setBounds(IArea area);

	public void setBorder(int left, int top, int right_space, int bottom_space);
	
	public int getCenterX();
	
	public int getCenterY();
	
	public int getAbsCenterX();
	
	public int getAbsCenterY();
	
	public int getLeftIn(IArea area);
	
	public int getTopIn(IArea area);
	
}

abstract class ComponentArea extends fatcat.gui.Component implements IArea {
	
	private int left, top, width, height;
	private final Container owner;
	
	ComponentArea() {
		this.owner = (Frame) this;
	}
	
	ComponentArea(Container owner) {
		this.owner = owner;
	}
	
	public final Container getOwner() {
		return owner;
	}

	@Override
	public final int getBottom() {
		return top + height;
	}
	
	@Override
	public final int getHeight() {
		return height;
	}

	@Override
	public final int getLeft() {
		return left;
	}

	@Override
	public final int getRight() {
		return left + width;
	}
	
	@Override
	public final int getTop() {
		return top;
	}

	@Override
	public final int getWidth() {
		return width;
	}

	@Override
	public final void setBottom(int bottom) {
		setHeight(bottom - top);
	}

	@Override
	public final void setBottom(IArea area) {
		setBottom(area.getBottom());
	}

	@Override
	public final void setBounds(int left, int top, int width, int height) {
		setLocation(left, top);
		setSize(width, height);
	}

	@Override
	public final void setBounds(IArea area) {
		setLocation(area);
		setSize(area);
	}

	@Override
	public final void setHeight(int height) {
		setSize(width, height);
	}

	@Override
	public final void setHeight(IArea area) {
		setSize(width, area.getHeight());
	}

	@Override
	public final void setLeft(int left) {
		setLocation(left, top);
	}

	@Override
	public final void setLeft(IArea area) {
		setLocation(area.getLeft(), top);
	}

	@Override
	public void setLocation(int left, int top) {
		this.left = left;
		this.top = top;
	}

	@Override
	public final void setLocation(IArea area) {
		setLocation(area.getLeft(), area.getTop());
	}

	@Override
	public final void setRight(int right) {
		setWidth(right - left);
	}

	@Override
	public final void setRight(IArea area) {
		setRight(area.getRight());
	}

	@Override
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public final void setSize(IArea area) {
		setSize(area.getWidth(), area.getHeight());
	}

	@Override
	public final void setSize() {
		setSize(getPreferredWidth(), getPreferredHeight());
	}

	@Override
	public final void setTop(int top) {
		setLocation(left, top);
	}

	@Override
	public final void setTop(IArea area) {
		setLocation(left, area.getTop());
	}

	@Override
	public final void setWidth(int width) {
		setSize(width, height);
	}

	@Override
	public final void setWidth(IArea area) {
		setSize(area.getWidth(), height);
	}

	@Override
	public final int getAbsBottom() {
		return getAbsTop() + height;
	}

	@Override
	public int getAbsLeft() {
		return owner.getAbsLeft() + left;
	}

	@Override
	public final int getAbsRight() {
		return getAbsLeft() + width;
	}

	@Override
	public int getAbsTop() {
		return owner.getAbsTop() + top;
	}

	@Override
	public final int getBottomSpace() {
		return owner.getHeight() - getBottom();
	}

	@Override
	public final int getRightSpace() {
		return owner.getWidth() - getRight();
	}

	@Override
	public final void setBorder(int left, int top, int rightSpace, int bottomSpace) {
		setLocation(left, top);
		setRightSpace(rightSpace);
		setBottomSpace(bottomSpace);
	}

	@Override
	public final void setBottomSpace(int bottomSpace) {
		setBottom(owner.getHeight() - bottomSpace);
	}

	@Override
	public final void setRightSpace(int rightSpace) {
		setRight(owner.getWidth() - rightSpace);
	}
	
	public final int getCenterX() {
		return getLeft() + getWidth() / 2;
	}
	
	public final int getCenterY() {
		return getTop() + getHeight() / 2;
	}
	
	public final int getAbsCenterX() {
		return getAbsLeft() + getWidth() / 2;
	}
	
	public final int getAbsCenterY() {
		return getAbsTop() + getHeight() / 2;
	}
	
	public final int getLeftIn(IArea area) {
		return getAbsLeft() - area.getAbsLeft();
	}
	
	public final int getTopIn(IArea area) {
		return getAbsTop() - area.getAbsTop();
	}
	
}
