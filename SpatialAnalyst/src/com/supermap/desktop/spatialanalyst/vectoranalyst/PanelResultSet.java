package com.supermap.desktop.spatialanalyst.vectoranalyst;

import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.NumberFormatter;

import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.SMFormattedTextField;

public class PanelResultSet extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JCheckBox checkBoxUnionBuffer;
	private JCheckBox checkBoxRemainAttributes;
	private JCheckBox checkBoxDisplayInMap;
	private JCheckBox checkBoxDisplayInScene;
	private JLabel labelSemicircleLineSegment;
	private SMFormattedTextField textFieldSemicircleLineSegment;

	public JCheckBox getCheckBoxUnionBuffer() {
		return checkBoxUnionBuffer;
	}

	public void setCheckBoxUnionBuffer(JCheckBox checkBoxUnionBuffer) {
		this.checkBoxUnionBuffer = checkBoxUnionBuffer;
	}

	public JCheckBox getCheckBoxRemainAttributes() {
		return checkBoxRemainAttributes;
	}

	public void setCheckBoxRemainAttributes(JCheckBox checkBoxRemainAttributes) {
		this.checkBoxRemainAttributes = checkBoxRemainAttributes;
	}

	public JCheckBox getCheckBoxDisplayInMap() {
		return checkBoxDisplayInMap;
	}

	public void setCheckBoxDisplayInMap(JCheckBox checkBoxDisplayInMap) {
		this.checkBoxDisplayInMap = checkBoxDisplayInMap;
	}

	public JCheckBox getCheckBoxDisplayInScene() {
		return checkBoxDisplayInScene;
	}

	public void setCheckBoxDisplayInScene(JCheckBox checkBoxDisplayInScene) {
		this.checkBoxDisplayInScene = checkBoxDisplayInScene;
	}

	public JLabel getLabelSemicircleLineSegment() {
		return labelSemicircleLineSegment;
	}

	public void setLabelSemicircleLineSegment(JLabel labelSemicircleLineSegment) {
		this.labelSemicircleLineSegment = labelSemicircleLineSegment;
	}

	public SMFormattedTextField getTextFieldSemicircleLineSegment() {
		return textFieldSemicircleLineSegment;
	}

	public void setTextFieldSemicircleLineSegment(SMFormattedTextField textFieldSemicircleLineSegment) {
		this.textFieldSemicircleLineSegment = textFieldSemicircleLineSegment;
	}

	public PanelResultSet() {
		initComponent();
		initResources();
		setPanelResultSetLayout();
	}

	private void initComponent() {
		this.checkBoxUnionBuffer = new JCheckBox("UnionBuffer");
		this.checkBoxRemainAttributes = new JCheckBox("RemainInAttributes");
		this.checkBoxDisplayInMap = new JCheckBox("DisplayInMap");
		this.checkBoxDisplayInScene = new JCheckBox("DisPlayInScene");
		//暂时不实现
		this.checkBoxDisplayInScene.setVisible(false);
		this.labelSemicircleLineSegment = new JLabel("SemicircleLineSegment");

		NumberFormatter numberFormatter = new NumberFormatter(NumberFormat.getInstance());
		numberFormatter.setValueClass(Integer.class);
		numberFormatter.setMinimum(4);
		numberFormatter.setMaximum(200);
		this.textFieldSemicircleLineSegment = new SMFormattedTextField(numberFormatter);
		this.textFieldSemicircleLineSegment.setText("100");
	}

	private void initResources() {
		this.checkBoxUnionBuffer.setText(SpatialAnalystProperties.getString("String_UnionBufferItem"));
		this.checkBoxRemainAttributes.setText(SpatialAnalystProperties.getString("String_RetainAttribute"));
		this.checkBoxDisplayInMap.setText(SpatialAnalystProperties.getString("String_DisplayInMap"));
		this.checkBoxDisplayInScene.setText(SpatialAnalystProperties.getString("String_DisplayInScene"));
		this.labelSemicircleLineSegment.setText(SpatialAnalystProperties.getString("String_Label_SemicircleLineSegment"));
	}

	private void setPanelResultSetLayout() {
		this.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_ResultSet")));
		GroupLayout panelResultSetLayout = new GroupLayout(this);
		this.setLayout(panelResultSetLayout);

		//@formatter:off
		panelResultSetLayout.setHorizontalGroup(panelResultSetLayout.createSequentialGroup()
				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.checkBoxUnionBuffer)
						.addComponent(this.checkBoxDisplayInMap)
						.addComponent(this.labelSemicircleLineSegment)).addGap(5)
				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.checkBoxRemainAttributes)
						.addComponent(this.checkBoxDisplayInScene)
						.addComponent(this.textFieldSemicircleLineSegment)));
		
		panelResultSetLayout.setVerticalGroup(panelResultSetLayout.createSequentialGroup()
				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.checkBoxUnionBuffer)
						.addComponent(this.checkBoxRemainAttributes)).addGap(5)
				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.checkBoxDisplayInMap)
						.addComponent(this.checkBoxDisplayInScene)).addGap(5)
				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelSemicircleLineSegment)
						.addComponent(this.textFieldSemicircleLineSegment)).addContainerGap());
		
		//@formatter:on
	}
}