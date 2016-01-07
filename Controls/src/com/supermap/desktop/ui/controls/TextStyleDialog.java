package com.supermap.desktop.ui.controls;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import com.supermap.data.GeoText;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;

/**
 * 文本风格对话框
 * 
 * @author xuzw
 *
 */
public class TextStyleDialog extends SmDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jPanelButton;
	private transient TextStylePanel textStylePanel;
	private transient ControlButton buttonCancel;
	private transient ControlButton buttonConfirm;

	private transient DialogResult dialogResult = DialogResult.CANCEL;

	/**
	 * Create the dialog
	 */
	public TextStyleDialog() {
		super();
		setSize(460, 350);
		setModal(true);
		this.setTitle(ControlsProperties.getString("String_TextStyleSet"));
		setResizable(false);
		getContentPane().add(getTextStylePanel(), BorderLayout.CENTER);
		getContentPane().add(getPanelButton(), BorderLayout.SOUTH);
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = this.getSize();
			if (frameSize.height > screenSize.height) {
				frameSize.height = screenSize.height;
			}
			if (frameSize.width > screenSize.width) {
				frameSize.width = screenSize.width;
			}
			this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		this.addWindowListener(new SetWindowListener());
	}

	/**
	 * 获取用户传入的GeoText
	 * 
	 * @return
	 */
	public GeoText getGeoText() {
		return textStylePanel.getGeoText();
	}

	/**
	 * 根据用户传入的GeoText来初始化文本风格面板
	 * 
	 * @param geoText
	 */
	public void setGeoText(GeoText geoText) {
		textStylePanel.setGeoText(geoText);
	}

	/**
	 * 获取用户传入的TextStyle
	 * 
	 * @return
	 */
	public TextStyle getTextStyle() {
		return textStylePanel.getTextStyle();
	}

	/**
	 * 根据用户传入的TextStyle来初始化文本风格面板
	 * 
	 * @param textStyle
	 */
	public void setTextStyle(TextStyle textStyle) {
		textStylePanel.setTextStyle(textStyle);
	}

	/**
	 * 获取一个值表示是否是设置专题图的文本风格
	 * 
	 * @return
	 */
	public boolean isThemeText() {
		return textStylePanel.isThemeText();
	}

	/**
	 * 设置一个值表示是否是设置专题图的文本风格
	 * 
	 * @param themeTextStyle
	 */
	public void setThemeText(boolean value) {
		textStylePanel.setThemeText(value);
	}

	/**
	 * 获取一个值表示是否是设置三维场景的文本风格
	 * 
	 * @return
	 */
	public boolean is3DText() {
		return textStylePanel.is3DText();
	}

	/**
	 * 设置一个值表示是否是设置三维场景的文本风格
	 * 
	 * @param value
	 */
	public void set3DText(boolean value) {
		textStylePanel.set3DText(value);
	}

	/**
	 * 获取地图或布局对象
	 * 
	 * @return
	 */
	public Object getMapObject() {
		return textStylePanel.getMapObject();
	}

	/**
	 * 设置地图或布局对象
	 * 
	 * @param object
	 */
	public void setMapObject(Object object) {
		textStylePanel.setMapObject(object);
	}

	/**
	 * 返回当前控件是否可编辑，false表示只能查看属性
	 * 
	 * @return
	 */
	public boolean getEditable() {
		return textStylePanel.getEditable();
	}

	/**
	 * 设置当前控件是否可编辑，false表示只能查看属性
	 * 
	 * @param value
	 */
	public void setEditable(boolean value) {
		textStylePanel.setEditable(value);
	}

	/**
	 * 获取用于预览的文字
	 * 
	 * @return
	 */
	public String getSampleText() {
		return textStylePanel.getSampleText();
	}

	/**
	 * 设置用于预览的文字
	 * 
	 * @param value
	 */
	public void setSampleText(String text) {
		textStylePanel.setSampleText(text);
	}

	/**
	 * 显示对话框
	 * 
	 * @return
	 */
	public DialogResult showDialog() {
		this.setVisible(true);
		return dialogResult;
	}

	/**
	 * 文本设置对话框的结果，如果点击"确定"则返回设置的文本，如果点击"取消"则返回null
	 * 
	 * @param geoText 需要设置的文本
	 * @param isThemeText 是否是专题图中的文本
	 * @param is3DText 是否是三维文本
	 * @return
	 */
	public static GeoText showDialog(GeoText geoText, boolean isThemeText, boolean is3DText) {
		GeoText result = null;

		TextStyleDialog dialog = new TextStyleDialog();
		dialog.setGeoText(geoText);
		dialog.setThemeText(isThemeText);
		dialog.set3DText(is3DText);
		DialogResult dialogResult = dialog.showDialog();
		if (dialogResult.equals(DialogResult.OK)) {
			result = dialog.getGeoText();
		}

		return result;
	}

	/**
	 * 文本风格设置对话框的结果，如果点击"确定"则返回设置的文本风格，如果点击"取消"则返回null
	 * 
	 * @param textStyle 文本风格
	 * @param isThemeText 是否是专题图中的文本
	 * @param is3DText 是否是三维文本
	 * @return
	 */
	public static TextStyle showDialog(TextStyle textStyle, boolean isThemeText, boolean is3DText) {
		TextStyle result = null;

		TextStyleDialog dialog = new TextStyleDialog();
		dialog.setTextStyle(textStyle);
		dialog.setThemeText(isThemeText);
		dialog.set3DText(is3DText);
		DialogResult dialogResult = dialog.showDialog();
		if (dialogResult.equals(DialogResult.OK)) {
			result = dialog.getTextStyle();
		}

		return result;
	}

	/**
	 * 获取按钮面板
	 * 
	 * @return
	 */
	protected JPanel getPanelButton() {
		if (jPanelButton == null) {
			jPanelButton = new JPanel();
			jPanelButton.add(getButtonConfirm());
			jPanelButton.add(getButtonCancel());
		}
		return jPanelButton;
	}

	/**
	 * 获取确定按钮
	 * 
	 * @return
	 */
	protected JButton getButtonConfirm() {
		if (buttonConfirm == null) {
			buttonConfirm = new ControlButton();
			buttonConfirm.setText(ControlsProperties.getString("String_Button_Ok"));
			buttonConfirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.OK;
					setVisible(false);
				}

			});
		}
		return buttonConfirm;
	}

	/**
	 * 获取取消按钮
	 * 
	 * @return
	 */
	protected JButton getButtonCancel() {
		if (buttonCancel == null) {
			buttonCancel = new ControlButton();
			buttonCancel.setText(ControlsProperties.getString("String_Button_Cancel"));
			buttonCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.CANCEL;
					setVisible(false);
				}

			});
		}
		return buttonCancel;
	}

	/**
	 * 获取文本风格面板
	 * 
	 * @return
	 */
	protected TextStylePanel getTextStylePanel() {
		if (textStylePanel == null) {
			textStylePanel = new TextStylePanel();
		}
		return textStylePanel;
	}

	class SetWindowListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			textStylePanel.refreshPreViewMapControl();
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// do nothing

		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// 默认实现，后续进行初始化

		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// do nothing
		}

		@Override
		public void windowClosing(WindowEvent e) {
			textStylePanel.removeMapDrawListener();
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// do nothing
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// do nothing
		}

	}
}
