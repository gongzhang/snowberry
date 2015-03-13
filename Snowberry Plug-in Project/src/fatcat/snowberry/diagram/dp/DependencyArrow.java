package fatcat.snowberry.diagram.dp;

import java.awt.Font;
import fatcat.gui.GraphicsX;

import fatcat.gui.snail.Container;
import fatcat.snowberry.diagram.ClassDiagram;
import fatcat.snowberry.diagram.HierarchyArrow;


public class DependencyArrow extends HierarchyArrow {
	
	public static final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.PLAIN, 12);
	private Font font;
	private String comment = null, label1 = null, label2 = null;

	public DependencyArrow(Container owner, ClassDiagram src, ClassDiagram dst) {
		super(owner, src, dst);
		setFont(DEFAULT_FONT);
	}
	
	public String getLabel1() {
		return label1;
	}
	
	public String getLabel2() {
		return label2;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setLabel1(String label1) {
		this.label1 = label1;
	}
	
	public void setLabel2(String label2) {
		this.label2 = label2;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	@Override
	protected void repaintComponent(GraphicsX g2) {
		super.repaintComponent(g2);
		repaintLabels(g2);
	}
	
	protected void repaintLabels(GraphicsX g2) {
		g2.setColor(getColor()); // TODO （低优先）绘制箭头两端的标签
		g2.setFont(font);
		if (comment != null) {
			g2.drawString(comment, (dst_x + src_x) / 2, (dst_y + src_y) / 2);
		}
	}
	
	@Override
	protected void repaintArrow(GraphicsX g2) {
		g2.drawLine(0, 0, -10, -7);
		g2.drawLine(0, 0, -10, 7);
	}

}
