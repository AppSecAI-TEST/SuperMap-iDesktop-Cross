package com.supermap.desktop.ui.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.supermap.data.TextAlignment;
import com.supermap.data.TextStyle;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.mapping.Map;
import com.supermap.data.Enum;

public class TextStyleContainer extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelFontName = new JLabel();
	private transient FontComboBox comboBoxFontName = new FontComboBox();
	private JLabel labelAlign = new JLabel();
	private JComboBox<String> comboBoxAlign = new JComboBox<String>();
	private JLabel labelFontSize = new JLabel();
	private JComboBox<String> comboBoxFontSize = new JComboBox<String>();
	private JLabel labelFontHeight = new JLabel();
	private JLabel labelFontHeightUnity = new JLabel();
	private JSpinner spinnerFontHeight = new JSpinner();
	private JLabel labelFontWidth = new JLabel();
	private JLabel labelFontWidthUnity = new JLabel();
	private JSpinner spinnerFontWidth = new JSpinner();
	private JLabel labelRotationAngl = new JLabel();
	private JSpinner spinnerRotationAngl = new JSpinner();
	private JLabel labelInclinationAngl = new JLabel();
	private JSpinner spinnerInclinationAngl = new JSpinner();
	private JLabel labelFontColor = new JLabel();

	private transient ColorSelectButton buttonFontColorSelect;
	private JLabel labelBGColor = new JLabel();
	private transient ColorSelectButton buttonBGColorSelect;
	// panelFontEffect
	private JCheckBox checkBoxBorder = new JCheckBox();
	private JCheckBox checkBoxStrickout = new JCheckBox();
	private JCheckBox checkBoxItalic = new JCheckBox();
	private JCheckBox checkBoxUnderline = new JCheckBox();
	private JCheckBox checkBoxShadow = new JCheckBox();
	private JCheckBox checkBoxFixedSize = new JCheckBox();
	private JCheckBox checkBoxOutlook = new JCheckBox();
	private JCheckBox checkBoxBGTransparent = new JCheckBox();
	private transient Map map;
	private transient TextStyle textStyle;
	// 对齐方式名称和对齐方式值构成的HashMap
	private static HashMap<String, Integer> hashMapTextAlignment = new HashMap<String, Integer>();
	private JTextField textFieldFontSize;
	private JTextField textFieldFontHeight;
	private JTextField textFieldFontWidth;
	private JTextField textFieldFontItalicAngl;
	private JTextField textFieldFontRotationAngl;
	// 对齐方式名称
	private static final String[] TEXTALIGNMENT_NAMES = { ControlsProperties.getString("String_TextAlignment_LeftTop"),
			ControlsProperties.getString("String_TextAlignment_MidTop"),
			ControlsProperties.getString("String_TextAlignment_RightTop"),
			ControlsProperties.getString("String_TextAlignment_LeftBaseline"),
			ControlsProperties.getString("String_TextAlignment_MidBaseline"),
			ControlsProperties.getString("String_TextAlignment_RightBaseline"),
			ControlsProperties.getString("String_TextAlignment_LeftBottom"),
			ControlsProperties.getString("String_TextAlignment_MidBottom"),
			ControlsProperties.getString("String_TextAlignment_RightBottom"),
			ControlsProperties.getString("String_TextAlignment_LeftMid"),
			ControlsProperties.getString("String_TextAlignment_Mid"),
			ControlsProperties.getString("String_TextAlignment_RightMid"),
	};
	// 字号与字高之间的转换精度
	private final double fontPrecision = 3.535;
	// 显示精度
	private String numeric = "0.00";
	private boolean isRefreshAtOnece = true;

	private transient LocalItemListener itemListener = new LocalItemListener();
	private transient LocalChangedListener changedListener = new LocalChangedListener();
	private transient LocalKeyListener localKeyListener = new LocalKeyListener();
	private transient LocalCheckBoxActionListener checkBoxActionListener = new LocalCheckBoxActionListener();
	private transient LocalPropertyListener propertyListener = new LocalPropertyListener();

	public TextStyleContainer(TextStyle textStyle, Map map) {
		this.textStyle = textStyle;
		this.map = map;
		initComponent();
		initResources();
		registActionListener();
	}

	/**
	 * @wbp.parser.constructor
	 */
	public TextStyleContainer(TextStyle textStyle) {
		this.textStyle = textStyle;
		initComponent();
		initResources();
		registActionListener();
	}

	/**
	 * 资源化
	 */
	private void initResources() {

		this.labelFontName.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontName"));
		this.labelAlign.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelAlinement"));
		this.labelFontSize.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontSize"));
		this.labelFontHeight.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontHeight"));
		this.labelFontWidth.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontWidth"));
		this.labelRotationAngl.setText(ControlsProperties.getString("String_Label_SymbolAngle"));
		this.labelInclinationAngl.setText(ControlsProperties.getString("String_Label_ItalicAngle"));
		this.labelFontColor.setText(ControlsProperties.getString("String_Label_TextStyleForeColor"));
		this.labelBGColor.setText(ControlsProperties.getString("String_BackgroundColor") + ":");

		this.checkBoxBorder.setText(ControlsProperties.getString("String_OverStriking"));
		this.checkBoxStrickout.setText(ControlsProperties.getString("String_DeleteLine"));
		this.checkBoxItalic.setText(ControlsProperties.getString("String_Italic"));
		this.checkBoxUnderline.setText(ControlsProperties.getString("String_Underline"));
		this.checkBoxShadow.setText(ControlsProperties.getString("String_Shadow"));

		this.checkBoxFixedSize.setText(ControlsProperties.getString("String_FixedSize"));
		this.checkBoxOutlook.setText(ControlsProperties.getString("String_Contour"));
		this.checkBoxBGTransparent.setText(ControlsProperties.getString("String_BackgroundTransparency"));
		this.labelFontHeightUnity.setText("0.1mm");
		this.labelFontWidthUnity.setText("0.1mm");
	}

	/**
	 * 初始化风格界面布局
	 */
	private void initComponent() {
		//@formatter:off
			this.setLayout(new GridBagLayout());
			JPanel panelSytleContent = new JPanel();
			this.add(panelSytleContent, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
			panelSytleContent.setLayout(new GridBagLayout());
			
			this.buttonFontColorSelect = new ColorSelectButton(textStyle.getForeColor());
			this.buttonBGColorSelect = new ColorSelectButton(textStyle.getBackColor());
			this.comboBoxFontName.setSelectedItem(textStyle.getFontName());
			initComboBoxAlign();
			initComboBoxFontSize();
			initTextFieldFontHeight();
			initTextFieldFontWidth();
			initTextFieldFontItalicAngl();
			initTextFieldFontRotation();
			this.textFieldFontHeight.setPreferredSize(new Dimension(180,20));
			this.textFieldFontWidth.setPreferredSize(new Dimension(180, 20));
			this.textFieldFontRotationAngl.setPreferredSize(new Dimension(280,20));
			this.textFieldFontItalicAngl.setPreferredSize(new Dimension(280, 20));
			panelSytleContent.add(this.labelFontName,          new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelSytleContent.add(this.comboBoxFontName,       new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,40,2,10).setFill(GridBagConstraints.HORIZONTAL));
			panelSytleContent.add(this.labelAlign,             new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelSytleContent.add(this.comboBoxAlign,          new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,40,2,10).setFill(GridBagConstraints.HORIZONTAL));
			panelSytleContent.add(this.labelFontSize,          new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelSytleContent.add(this.comboBoxFontSize,       new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,40,2,10).setFill(GridBagConstraints.HORIZONTAL));
			panelSytleContent.add(this.labelFontHeight,        new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelSytleContent.add(this.spinnerFontHeight,      new GridBagConstraintsHelper(2, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,40,2,10).setFill(GridBagConstraints.HORIZONTAL));
			panelSytleContent.add(this.labelFontHeightUnity,   new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,0,2,10));
			panelSytleContent.add(this.labelFontWidth,         new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelSytleContent.add(this.spinnerFontWidth,       new GridBagConstraintsHelper(2, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,40,2,10).setFill(GridBagConstraints.HORIZONTAL));
			panelSytleContent.add(this.labelFontWidthUnity,    new GridBagConstraintsHelper(3, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,0,2,10));
			panelSytleContent.add(this.labelRotationAngl,      new GridBagConstraintsHelper(0, 5, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelSytleContent.add(this.spinnerRotationAngl,    new GridBagConstraintsHelper(2, 5, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,40,2,10).setFill(GridBagConstraints.HORIZONTAL));
			panelSytleContent.add(this.labelInclinationAngl,   new GridBagConstraintsHelper(0, 6, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelSytleContent.add(this.spinnerInclinationAngl, new GridBagConstraintsHelper(2, 6, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,40,2,10).setFill(GridBagConstraints.HORIZONTAL));
			panelSytleContent.add(this.labelFontColor,         new GridBagConstraintsHelper(0, 7, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelSytleContent.add(this.buttonFontColorSelect,  new GridBagConstraintsHelper(2, 7, 2, 1).setAnchor(GridBagConstraints.EAST).setWeight(1, 0).setInsets(2,40,2,10).setFill(GridBagConstraints.HORIZONTAL));
			panelSytleContent.add(this.labelBGColor,           new GridBagConstraintsHelper(0, 8, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelSytleContent.add(this.buttonBGColorSelect,    new GridBagConstraintsHelper(2, 8, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,40,2,10).setFill(GridBagConstraints.HORIZONTAL));
			JPanel panelFontEffect = new JPanel();
			panelFontEffect.setBorder(new TitledBorder(null, ControlsProperties.getString("String_GeometryPropertyTextControl_GroupBoxFontEffect"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
			initPanelFontEffect(panelFontEffect);
			panelSytleContent.add(panelFontEffect,             new GridBagConstraintsHelper(0, 9, 4, 1).setAnchor(GridBagConstraints.CENTER).setWeight(2, 0).setInsets(5).setFill(GridBagConstraints.HORIZONTAL));
			//@formatter:on
	}

	/**
	 * 初始化倾斜角度左侧textField值
	 */
	private void initTextFieldFontRotation() {
		this.spinnerRotationAngl.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerRotationAngl.getEditor();
		this.textFieldFontRotationAngl = numberEditor.getTextField();
		this.textFieldFontRotationAngl.setText(new DecimalFormat(numeric).format(textStyle.getRotation()));
	}

	/**
	 * 初始化旋转角度左侧textField值
	 */
	private void initTextFieldFontItalicAngl() {
		this.spinnerInclinationAngl.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		this.spinnerInclinationAngl.setEnabled(false);
		NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerInclinationAngl.getEditor();
		this.textFieldFontItalicAngl = numberEditor.getTextField();
		this.textFieldFontItalicAngl.setText(new DecimalFormat(numeric).format(textStyle.getItalicAngle()));
	}

	/**
	 * 初始化字宽左侧textField值
	 */
	private void initTextFieldFontWidth() {
		this.spinnerFontWidth.setModel(new SpinnerNumberModel(new Double(0.0), null, null, new Double(1.0)));
		NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerFontWidth.getEditor();
		this.textFieldFontWidth = numberEditor.getTextField();
		this.textFieldFontWidth.setText(new DecimalFormat(numeric).format(textStyle.getFontWidth()));
	}

	/**
	 * 初始化字高左侧textField值
	 */
	private void initTextFieldFontHeight() {
		this.spinnerFontHeight.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerFontHeight.getEditor();
		this.textFieldFontHeight = numberEditor.getTextField();
		this.textFieldFontHeight.setText(new DecimalFormat(numeric).format(textStyle.getFontHeight()));
	}

	/**
	 * 初始化字号下拉框
	 */
	private void initComboBoxFontSize() {
		this.comboBoxFontSize.setModel(new DefaultComboBoxModel<String>(new String[] { "1", "2", "3", "4", "5", "5.5", "6.5", "7.5", "8", "9", "10", "11",
				"12", "14",
				"16", "18", "20", "22", "24", "26", "28", "36", "48", "72" }));
		this.comboBoxFontSize.setEditable(true);
		this.textFieldFontSize = (JTextField) this.comboBoxFontSize.getEditor().getEditorComponent();
		this.textFieldFontSize.setText(new DecimalFormat(numeric).format(textStyle.getFontHeight() / fontPrecision));

	}

	/**
	 * 初始化文本对齐方式下拉框
	 */
	private void initComboBoxAlign() {
		this.comboBoxAlign.setModel(new DefaultComboBoxModel<String>(TEXTALIGNMENT_NAMES));
		initHashMapTextAlignment();
		Object[] textAlignmentValues = hashMapTextAlignment.values().toArray();
		Object[] textAlignmentNames = hashMapTextAlignment.keySet().toArray();
		String alignMent = "";
		for (int i = 0; i < textAlignmentValues.length; i++) {
			Integer temp = (Integer) textAlignmentValues[i];
			if (temp == this.textStyle.getAlignment().value()) {
				alignMent = (String) textAlignmentNames[i];
			}
		}
		this.comboBoxAlign.setSelectedItem(alignMent);
	}

	/**
	 * 字体效果界面布局
	 * 
	 * @param panelFontEffect
	 */
	private void initPanelFontEffect(JPanel panelFontEffect) {
		//@formatter:off
			initCheckBoxState();
			panelFontEffect.setLayout(new GridBagLayout());
			panelFontEffect.add(this.checkBoxBorder,         new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelFontEffect.add(this.checkBoxStrickout,      new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelFontEffect.add(this.checkBoxItalic,         new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelFontEffect.add(this.checkBoxUnderline,      new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelFontEffect.add(this.checkBoxShadow,         new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelFontEffect.add(this.checkBoxFixedSize,      new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelFontEffect.add(this.checkBoxOutlook,        new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			panelFontEffect.add(this.checkBoxBGTransparent,  new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
			//@formatter:on
	}

	private void initCheckBoxState() {
		this.checkBoxBorder.setSelected(textStyle.getBold());
		this.checkBoxStrickout.setSelected(textStyle.getStrikeout());
		this.checkBoxItalic.setSelected(textStyle.getItalic());
		this.spinnerInclinationAngl.setEnabled(textStyle.getItalic());
		this.checkBoxUnderline.setSelected(textStyle.getUnderline());
		this.checkBoxShadow.setSelected(textStyle.getShadow());
		this.checkBoxFixedSize.setSelected(textStyle.isSizeFixed());
		this.checkBoxOutlook.setSelected(textStyle.getOutline());
		this.checkBoxBGTransparent.setSelected(!textStyle.getBackOpaque());
	}

	/**
	 * 注册事件
	 */
	private void registActionListener() {
		this.comboBoxFontName.addItemListener(this.itemListener);
		this.comboBoxAlign.addItemListener(this.itemListener);
		this.comboBoxFontSize.addItemListener(this.itemListener);
		this.spinnerFontHeight.addChangeListener(this.changedListener);
		this.spinnerFontWidth.addChangeListener(this.changedListener);
		this.spinnerInclinationAngl.addChangeListener(this.changedListener);
		this.spinnerRotationAngl.addChangeListener(this.changedListener);
		this.textFieldFontSize.addKeyListener(this.localKeyListener);
		this.textFieldFontHeight.addKeyListener(this.localKeyListener);
		this.textFieldFontWidth.addKeyListener(this.localKeyListener);
		this.textFieldFontItalicAngl.addKeyListener(this.localKeyListener);
		this.textFieldFontRotationAngl.addKeyListener(this.localKeyListener);
		this.checkBoxBGTransparent.addActionListener(this.checkBoxActionListener);
		this.checkBoxBorder.addActionListener(this.checkBoxActionListener);
		this.checkBoxFixedSize.addActionListener(this.checkBoxActionListener);
		this.checkBoxItalic.addActionListener(this.checkBoxActionListener);
		this.checkBoxOutlook.addActionListener(this.checkBoxActionListener);
		this.checkBoxShadow.addActionListener(this.checkBoxActionListener);
		this.checkBoxUnderline.addActionListener(this.checkBoxActionListener);
		this.checkBoxStrickout.addActionListener(this.checkBoxActionListener);
		this.buttonFontColorSelect.addPropertyChangeListener("m_selectionColors", this.propertyListener);
		this.buttonBGColorSelect.addPropertyChangeListener("m_selectionColors", this.propertyListener);
		this.comboBoxFontSize.getEditor().getEditorComponent().addKeyListener(this.localKeyListener);
	}

	/**
	 * 注销事件
	 */
	public void unregistActionListener() {
		this.comboBoxFontName.removeItemListener(this.itemListener);
		this.comboBoxAlign.removeItemListener(this.itemListener);
		this.comboBoxFontSize.removeItemListener(this.itemListener);
		this.spinnerFontHeight.removeChangeListener(this.changedListener);
		this.spinnerFontWidth.removeChangeListener(this.changedListener);
		this.spinnerInclinationAngl.removeChangeListener(this.changedListener);
		this.spinnerRotationAngl.removeChangeListener(this.changedListener);
		this.textFieldFontSize.removeKeyListener(this.localKeyListener);
		this.textFieldFontHeight.removeKeyListener(this.localKeyListener);
		this.textFieldFontWidth.removeKeyListener(this.localKeyListener);
		this.textFieldFontItalicAngl.removeKeyListener(this.localKeyListener);
		this.textFieldFontRotationAngl.removeKeyListener(this.localKeyListener);
		this.checkBoxBGTransparent.removeActionListener(this.checkBoxActionListener);
		this.checkBoxBorder.removeActionListener(this.checkBoxActionListener);
		this.checkBoxFixedSize.removeActionListener(this.checkBoxActionListener);
		this.checkBoxItalic.removeActionListener(this.checkBoxActionListener);
		this.checkBoxOutlook.removeActionListener(this.checkBoxActionListener);
		this.checkBoxShadow.removeActionListener(this.checkBoxActionListener);
		this.checkBoxUnderline.removeActionListener(this.checkBoxActionListener);
		this.checkBoxStrickout.removeActionListener(this.checkBoxActionListener);
		this.buttonFontColorSelect.removePropertyChangeListener("m_selectionColors", this.propertyListener);
		this.buttonBGColorSelect.removePropertyChangeListener("m_selectionColors", this.propertyListener);
		this.comboBoxFontSize.getEditor().getEditorComponent().removeKeyListener(this.localKeyListener);
	}

	private void initHashMapTextAlignment() {
		int[] textAlignmentValues = Enum.getValues(TextAlignment.class);
		for (int i = 0; i < textAlignmentValues.length; i++) {
			hashMapTextAlignment.put(TEXTALIGNMENT_NAMES[i],
					textAlignmentValues[i]);
		}
	}

	class LocalItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == comboBoxFontName) {
				// 设置字体名称
				setFontName();
			} else if (e.getSource() == comboBoxAlign) {
				// 设置文本对齐方式
				setFontAlign();
			} else if (e.getSource() == comboBoxFontSize) {
				// 设置字号
				setFontSize();
			}
			if (isRefreshAtOnece && null != map) {
				map.refresh();
			}
		}

		/**
		 * 设置文本对齐方式
		 */
		private void setFontAlign() {
			String textAlignmentName = comboBoxAlign.getSelectedItem().toString();
			int value = hashMapTextAlignment.get(textAlignmentName);
			TextAlignment textAlignment = (TextAlignment) Enum.parse(TextAlignment.class, value);
			textStyle.setAlignment(textAlignment);
		}

		/**
		 * 设置字体名称
		 */
		private void setFontName() {
			String fontName = comboBoxFontName.getSelectedItem().toString();
			textStyle.setFontName(fontName);
		}

	}

	class LocalChangedListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == spinnerFontHeight) {
				// 设置字高
				setFontHeight();
			} else if (e.getSource() == spinnerFontWidth) {
				// 设置字宽
				setFontWidth();
			} else if (e.getSource() == spinnerInclinationAngl) {
				// 设置文本倾斜角度
				setFontInclinationAngl();
			} else if (e.getSource() == spinnerRotationAngl) {
				// 设置旋转角度
				setRotationAngl();
			}
			if (isRefreshAtOnece && null != map) {
				map.refresh();
			}
		}

	}

	class LocalKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getSource() == textFieldFontSize) {
				setFontSize();
				if (isRefreshAtOnece && null != map) {
					map.refresh();
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// 输入限制
			int keyChar = e.getKeyChar();
			if (keyChar != '.' && (keyChar < '0' || keyChar > '9')) {
				e.consume();
			}
		}

	}

	class LocalCheckBoxActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == checkBoxBorder) {
				// 设置字体加粗
				setFontBorder();
			} else if (e.getSource() == checkBoxStrickout) {
				// 设置添加删除线
				setFontStrickout();
			} else if (e.getSource() == checkBoxItalic) {
				// 设置字体样式为斜体
				setItalic();
			} else if (e.getSource() == checkBoxUnderline) {
				// 设置添加下划线
				setUnderline();
			} else if (e.getSource() == checkBoxShadow) {
				// 设置添加阴影
				setShadow();
			} else if (e.getSource() == checkBoxFixedSize) {
				// 设置字体固定大小
				setFixedSize();
			} else if (e.getSource() == checkBoxOutlook) {
				// 设置字体轮廓
				setOutLook();
			} else if (e.getSource() == checkBoxBGTransparent) {
				// 设置背景透明
				setBGOpare();
			}
			if (isRefreshAtOnece && null != map) {
				map.refresh();
			}
		}

		/**
		 * 设置背景透明
		 */
		private void setBGOpare() {
			boolean isOpare = checkBoxBGTransparent.isSelected();
			textStyle.setBackOpaque(!isOpare);
			checkBoxOutlook.setEnabled(isOpare);
			buttonBGColorSelect.setEnabled(!isOpare);
		}

		/**
		 * 设置字体轮廓
		 */
		private void setOutLook() {
			boolean isOutlook = checkBoxOutlook.isSelected();
			textStyle.setOutline(isOutlook);
			buttonBGColorSelect.setEnabled(isOutlook);
		}

		/**
		 * 设置字体固定大小
		 */
		private void setFixedSize() {
			boolean isFixedSize = checkBoxFixedSize.isSelected();
			textStyle.setSizeFixed(isFixedSize);
		}

		/**
		 * 设置添加阴影
		 */
		private void setShadow() {
			boolean isShadow = checkBoxShadow.isSelected();
			textStyle.setShadow(isShadow);
		}

		/**
		 * 设置添加下划线
		 */
		private void setUnderline() {
			boolean isUnderline = checkBoxUnderline.isSelected();
			textStyle.setUnderline(isUnderline);
		}

		/**
		 * 设置字体样式为斜体
		 */
		private void setItalic() {
			boolean isItalic = checkBoxItalic.isSelected();
			spinnerInclinationAngl.setEnabled(isItalic);
			textStyle.setItalic(isItalic);
		}

		/**
		 * 设置添加删除线
		 */
		private void setFontStrickout() {
			boolean isStrickout = checkBoxStrickout.isSelected();
			textStyle.setStrikeout(isStrickout);
		}

		/**
		 * 设置字体加粗
		 */
		private void setFontBorder() {
			boolean isBorder = checkBoxBorder.isSelected();
			textStyle.setBold(isBorder);
		}

	}

	class LocalPropertyListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			if (e.getSource() == buttonFontColorSelect) {
				// 设置文本颜色
				setFontColor();
			} else if (e.getSource() == buttonBGColorSelect) {
				// 设置背景颜色
				setBackgroundColor();
			}
			if (isRefreshAtOnece && null != map) {
				map.refresh();
			}
		}

		/**
		 * 设置背景颜色
		 */
		private void setBackgroundColor() {
			Color color = buttonBGColorSelect.getColor();
			if (color != null) {
				textStyle.setBackColor(color);
			}
		}

		/**
		 * 设置文本颜色
		 */
		private void setFontColor() {
			Color color = buttonFontColorSelect.getColor();
			if (color != null) {
				textStyle.setForeColor(color);
			}
		}

	}

	/**
	 * 设置旋转角度
	 */
	private void setRotationAngl() {
		if (null != spinnerRotationAngl.getValue()) {
			double rotationAngl = (double) spinnerRotationAngl.getValue();
			textStyle.setRotation(rotationAngl);
		}
	}

	/**
	 * 设置文本倾斜角度
	 */
	private void setFontInclinationAngl() {
		if (null != spinnerInclinationAngl.getValue()) {
			double italicAngl = (double) spinnerInclinationAngl.getValue();
			textStyle.setItalicAngle(italicAngl);
		}
	}

	/**
	 * 设置字宽
	 */
	private void setFontWidth() {
		if (null != spinnerFontWidth.getValue()) {
			double fontWidth = (double) spinnerFontWidth.getValue();
			textStyle.setFontWidth(fontWidth);
		}
	}

	/**
	 * 设置字高
	 */
	private void setFontHeight() {
		if (null != spinnerFontHeight.getValue()) {
			double fontHeight = (double) spinnerFontHeight.getValue();
			if (fontHeight > 0) {
				textStyle.setFontHeight(fontHeight);
				textFieldFontSize.setText(new DecimalFormat(numeric).format(fontHeight / fontPrecision));
			}
		}
	}

	/**
	 * 设置字号
	 */
	private void setFontSize() {
		if (!textFieldFontSize.getText().isEmpty()) {
			double fontHeight = Double.valueOf(textFieldFontSize.getText()) * fontPrecision;
			if (fontHeight > 0) {
				textFieldFontHeight.setText(new DecimalFormat(numeric).format(fontHeight));
				textStyle.setFontHeight(fontHeight);
			}
		}
	}

	public TextStyle getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
	}

}