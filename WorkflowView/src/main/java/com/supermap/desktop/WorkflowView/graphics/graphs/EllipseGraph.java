package com.supermap.desktop.WorkflowView.graphics.graphs;

import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.connection.IConnectable;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by highsad on 2017/1/19.
 */
public class EllipseGraph extends AbstractGraph implements IConnectable {

	public EllipseGraph(GraphCanvas canvas) {
		super(canvas, new Ellipse2D.Double(0, 0, 160, 60));
	}

	@Override
	public Ellipse2D getShape() {
		return (Ellipse2D) super.getShape();
	}

	@Override
	protected void applyLocation(Point point) {
		getShape().setFrame(point.getX(), point.getY(), getShape().getWidth(), getShape().getHeight());
	}

	@Override
	protected void applySize(int width, int height) {
		getShape().setFrame(getShape().getX(), getShape().getY(), width, height);
	}

	@Override
	public boolean contains(Point p) {
		return getShape().contains(p);
	}

	@Override
	protected void onPaint(Graphics g) {
		g.setColor(new Color(254, 244, 236));
		((Graphics2D) g).fill(this.shape);
	}

	@Override
	protected void toXmlHook(JSONObject jsonObject) {
		jsonObject.put("shape", getShape().getX() + "," + getShape().getY() + "," + getShape().getWidth() + "," + getShape().getHeight());
	}

	@Override
	protected void formXmlHook(JSONObject xml) {
		String shape = (String) xml.get("shape");
		String[] split = shape.split(",");
		this.shape = new Ellipse2D.Double(Double.valueOf(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2]), Double.valueOf(split[3]));
	}

	@Override
	public IGraph getConnector() {
		return this;
	}
}
