package com.supermap.desktop.process.graphics;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.RectangleGraph;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.geom.Geom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by highsad on 2017/1/17.
 * 画布单位1默认与屏幕像素1相等，画布缩放之后之后的画布单位1则与屏幕像素 1*scale 相等
 */
public class GraphCanvas extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener {
	public final static Color DEFAULT_BACKGROUNDCOLOR = new Color(11579568);
	public final static Color DEFAULT_CANVAS_COLOR = new Color(255, 255, 255);

	public final static Color GRID_MINOR_COLOR = new Color(15461355);
	public final static Color GRID_MAJOR_COLOR = new Color(13290186);
	private QuadTree<IGraph> graphQuadTree = new QuadTree<>();
	private double scale = 1.0;
	private IGraph toCreation;
	private IGraph hotGraph;
	private IGraph selectedGraph;

	private IGraph previewGraph;

	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		frame.setSize(1000, 650);
		final GraphCanvas canvas = new GraphCanvas();


		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(canvas, BorderLayout.CENTER);

		JButton button = new JButton("Rectangle");
		frame.getContentPane().add(button, BorderLayout.NORTH);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RectangleGraph graph = new RectangleGraph(canvas);
				graph.setWidth(200);
				graph.setHeight(80);
				graph.setArcHeight(5);
				graph.setArcWidth(5);

				canvas.createGraph(graph);
			}
		});

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}

	public GraphCanvas() {
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		setViewRenderingHints(graphics2D);
		paintBackground(graphics2D);
		paintCanvas(graphics2D);
		paintGraphs(graphics2D);
	}

	/**
	 * 绘制背景
	 *
	 * @param g
	 */
	private void paintBackground(Graphics2D g) {
		g.setColor(DEFAULT_BACKGROUNDCOLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * 绘制画布
	 *
	 * @param g
	 */
	private void paintCanvas(Graphics2D g) {
		Rectangle rect = getCanvasViewBounds();
		g.setColor(DEFAULT_CANVAS_COLOR);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
	}

	private void paintGraphs(Graphics2D g) {
		Collection c = this.graphQuadTree.findAll();
		Iterator iterator = c.iterator();

		while (iterator.hasNext()) {
			IGraph graph = (IGraph) iterator.next();
			graph.paint(g, graph == this.hotGraph, graph == this.selectedGraph);
		}

		if (this.previewGraph != null) {
			this.previewGraph.paint(g, false, false);
		}
	}

	protected Rectangle getCanvasViewBounds() {
		return new Rectangle(0, 0, getWidth(), getHeight());
	}

	/**
	 * 这是个借鉴方法，大约是一些抗锯齿的设置
	 *
	 * @param g
	 */
	private void setViewRenderingHints(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}

	public void createGraph(IGraph graph) {
		this.toCreation = graph;
		this.previewGraph = this.toCreation.clone();
	}

	private IGraph findGraph(Point p) {
		IGraph graph = null;

		return graph;
	}

	private Point2D panelToCanvas(Point point) {
		return new Point2D.Double(point.getX(), point.getY());
	}

	private Point canvasToPanel(Point2D point2D) {
		int panelX = Double.valueOf(point2D.getX()).intValue();
		int panelY = Double.valueOf(point2D.getY()).intValue();
		return new Point(panelX, panelY);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			Point point = e.getPoint();

			if (this.toCreation != null) {

				// toCreation 不为空，则新建
				this.previewGraph = null;
				repaint(this.toCreation, point);
				Rectangle bounds = this.toCreation.getBounds();
				this.graphQuadTree.add(this.toCreation, new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight()));
				this.toCreation = null;
			} else {

				// toCreation 为空，则查询
				Collection<IGraph> c = this.graphQuadTree.findContains(new Point2D.Double(point.getX(), point.getY()));

				if (c != null && c.size() > 0) {
					Iterator<IGraph> iterator = c.iterator();
					this.selectedGraph = iterator.next();
				}
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {
			this.previewGraph = null;

		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println(e.getPoint());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (this.previewGraph != null) {
			repaint(this.previewGraph, e.getPoint());
		}
	}

	private void repaint(IGraph graph, Point point) {
		if (graph.getX() != point.getX() && graph.getY() != point.getY()) {
			repaint(graph.getBounds());
			double x = point.getX() - graph.getWidth() / 2 - graph.getBorderWidth();
			double y = point.getY() - graph.getHeight() / 2 - graph.getBorderWidth();
			graph.setX(x);
			graph.setY(y);
			repaint(graph.getBounds());
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

	}
}
