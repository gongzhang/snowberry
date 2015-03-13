package fatcat.snowberry.diagram.dp;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import fatcat.gui.GraphicsX;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import fatcat.gui.snail.Component;
import fatcat.gui.snail.Container;
import fatcat.gui.snail.Frame;

import fatcat.gui.snail.SnailShell;
import fatcat.snowberry.core.International;
import fatcat.snowberry.diagram.DiagramFrame;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.dp.DesignPatternCore;
import fatcat.snowberry.gui.Label;
import fatcat.snowberry.tag.IProjectModel;

public class DPFrame extends Frame {
	
	private final DiagramFrame diagramFrame;
	private final HashMap<DesignPattern, DPOverview> overviewMap;
	private final DesignPattern[] patterns;
	
	public DesignPattern[] getPatterns() {
		return patterns;
	}

	public DPFrame(SnailShell shell, DiagramFrame diagramFrame) {
		super(shell);
		this.diagramFrame = diagramFrame;
		
		final IProjectModel proj = diagramFrame.getProject();
		patterns = DesignPatternCore.getPatterns(proj);
		
		final Label title = new Label(this, International.Project + proj.getResource().getName());
		title.setAutoSize(true);
		title.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		title.setColor(Color.gray.darker().getRGB());
		title.setLocation(10, 6);

		final Label info = new Label(this, String.format(International.ProjectInfo, proj.getITypeModels().length, patterns.length));
		info.setAutoSize(true);
		info.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
		info.setColor(Color.black.getRGB());
		info.setLocation(10, title.getBottom() + 4);
		
		overviewMap = new HashMap<DesignPattern, DPOverview>();
		for (DesignPattern pattern : patterns) {
			DPOverview overview = new DPOverview(this, pattern);
			overviewMap.put(pattern, overview);
		}
		
	}
	
	@Override
	public void refreshLayout(Container c) {
		int left = 20, top = 90;
		if (overviewMap != null) {
			for (DesignPattern pattern : overviewMap.keySet()) {
				DPOverview overview = overviewMap.get(pattern);
				
				if (left + overview.getWidth() > getWidth() - 20) {
					left = 20;
					top += overview.getHeight() + 20;
				}
				
				overview.setLocation(left, top);
				left += overview.getWidth() + 28;
				
			}
		}
	}
	
	public DiagramFrame getDiagramFrame() {
		return diagramFrame;
	}
	
	private GradientPaint gradientPaint = null;
	private GradientPaint linePaint = null;
	private GradientPaint titlePaint = null;
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		gradientPaint = new GradientPaint(0, IMG_PROJECT.getHeight(), new Color(0xf5f5f5), 0, height - IMG_PROJECT.getHeight(), new Color(0xfdfdfd));
		linePaint = new GradientPaint(12, 0, new Color(0x4985d9).darker(), width - 32, 0, Color.white);
		titlePaint = new GradientPaint(12, 0, new Color(0xd8f2ff), width / 6, 0, Color.white);
	}
	
	public static final BufferedImage IMG_PROJECT = GraphicsX.createImage("/icons/new_proj.png");
	
	@Override
	public void repaintComponent(GraphicsX g2, Component c) {
		g2.setPaint(gradientPaint);
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setPaint(titlePaint);
		g2.fillRect(0, 0, getWidth(), IMG_PROJECT.getHeight());
		g2.setPaint(linePaint);
		g2.fillRect(0, IMG_PROJECT.getHeight(), getWidth(), 2);
		g2.drawImage(IMG_PROJECT, getWidth() - IMG_PROJECT.getWidth(), 0, null);
	}
	
	@Override
	protected void repaintComponent(GraphicsX g2) {
		super.repaintComponent(g2);
		
		// 右下角
		g2.drawImage(DiagramFrame.IMG_FATCAT, getWidth() - DiagramFrame.IMG_FATCAT.getWidth() - 20, getHeight() - 45, null);
	}

}
