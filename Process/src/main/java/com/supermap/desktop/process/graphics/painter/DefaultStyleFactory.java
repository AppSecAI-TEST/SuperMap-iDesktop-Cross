package com.supermap.desktop.process.graphics.painter;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import sun.swing.SwingUtilities2;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by highsad on 2017/2/25.
 */
public class DefaultStyleFactory implements IStyleFactory {

	public final static Color DEFAULT_BACKGROUND_COLOR = new Color(231, 231, 231);
	public final static DefaultStyleFactory INSTANCE = new DefaultStyleFactory();

	private DefaultStyleFactory() {

	}

	@Override
	public void normalPoint(Graphics graphics) {

	}

	@Override
	public void normalLine(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setColor(Color.BLACK);
		Stroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		graphics2D.setStroke(stroke);
	}

	@Override
	public void border(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setColor(Color.BLACK);
		Stroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		graphics2D.setStroke(stroke);
	}

	@Override
	public void normalRegion(Graphics graphics) {
		graphics.setColor(Color.decode("#AEEEEE"));
	}

	@Override
	public void normalText(Graphics graphics) {
		Font font = new Font("微软雅黑", Font.PLAIN, 20);
		graphics.setFont(font);
		graphics.setColor(Color.darkGray);
//
//		String text = this.data == null ? "未知数据" : this.data.toString();
//		int fontHeight = getCanvas().getFontMetrics(font).getHeight();
//		int fontWidth = SwingUtilities2.stringWidth(getCanvas(), getCanvas().getFontMetrics(font), text);
//		int fontDescent = getCanvas().getFontMetrics(font).getDescent();
//
//		// 字符绘制时，坐标点指定的是基线的位置，而实际上我们希望指定的坐标点是整个字符块最下边的位置，因此使用 fontDescent 做个处理
//		g.drawString(text, doubleToInt(getX() + (getWidth() - fontWidth) / 2), doubleToInt(getY() + getHeight() / 2 + fontHeight / 2 - fontDescent));
	}

	@Override
	public void hotPoint(Graphics graphics) {

	}

	@Override
	public void hotLine(Graphics graphics) {

	}

	@Override
	public void hotBorder(Graphics graphics) {

	}

	@Override
	public void hotRegion(Graphics graphics) {
		graphics.setColor(GraphicsUtil.transparentColor(DEFAULT_BACKGROUND_COLOR, 100));
	}

	@Override
	public void hotText(Graphics graphics) {

	}

	@Override
	public void selectedPoint(Graphics graphics) {

	}

	@Override
	public void selectedLine(Graphics graphics) {

	}

	@Override
	public void selectedBorder(Graphics graphics) {

	}

	@Override
	public void selectedRegion(Graphics graphics) {

	}

	@Override
	public void selectedText(Graphics graphics) {

	}

	@Override
	public void unablePoint(Graphics graphics) {

	}

	@Override
	public void unableLine(Graphics graphics) {

	}

	@Override
	public void unableRegion(Graphics graphics) {

	}

	@Override
	public void unableText(Graphics graphics) {
		Font font = new Font("微软雅黑", Font.BOLD | Font.PLAIN, 20);
		graphics.setFont(font);
		graphics.setColor(Color.darkGray);

//		String text = this.process == null ? "未知节点" : this.process.getTitle();
//		int fontHeight = getCanvas().getFontMetrics(font).getHeight();
//		int fontWidth = SwingUtilities2.stringWidth(getCanvas(), getCanvas().getFontMetrics(font), text);
//		int fontDescent = getCanvas().getFontMetrics(font).getDescent();
	}

	@Override
	public void previewPoint(Graphics graphics) {

	}

	@Override
	public void previewLine(Graphics graphics) {

	}

	@Override
	public void previewBorder(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setColor(Color.BLACK);
		Stroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		graphics2D.setStroke(stroke);
	}

	@Override
	public void previewRegion(Graphics graphics) {
		graphics.setColor(GraphicsUtil.transparentColor(DEFAULT_BACKGROUND_COLOR, 100));
	}

	@Override
	public void previewText(Graphics graphics) {

	}

	@Override
	public void progressLine(Graphics graphics) {

	}

	@Override
	public void progressRegion(Graphics graphics) {

	}

	@Override
	public void progressText(Graphics graphics) {

	}
}