package com.supermap.desktop.newtheme.commonPanel;

import com.supermap.data.TextStyle;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.MixedTextStyle;
import com.supermap.mapping.Theme;

import java.awt.*;

public class TextStyleDialog extends SmDialog {

	private static final long serialVersionUID = 1L;
	private transient TextStyleContainer textStyleContainer;

	public TextStyleDialog(TextStyle textStyle, Map map, Layer themeLayer) {
		this.textStyleContainer = new TextStyleContainer(textStyle, map, themeLayer);
		initComponents();
	}

	/**
	 * 构建界面
	 */
	private void initComponents() {
		setSize(420, 560);
		//  @formatter:off
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(textStyleContainer, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(2, 1));
		// @formatter:on
	}

	/**
	 * 设置是否及时刷新
	 * 
	 * @param isRefreshAtOnce
	 */
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		textStyleContainer.setRefreshAtOnce(isRefreshAtOnce);
	}

	public TextStyleContainer getTextStyleContainer() {
		return textStyleContainer;
	}

	/**
	 * 属性地图和图层
	 */
	public void refreshMapAndLayer() {
		textStyleContainer.refreshMapAndLayer();
	}

	public void setMixedTextStyle(MixedTextStyle mixedTextStyle){
		textStyleContainer.setMixedTextStyle(mixedTextStyle);
	}
	
	public void setTheme(Theme theme) {
		textStyleContainer.setTheme(theme);
	}
	
	public void setRows(int[] rows){
		textStyleContainer.setSelectRow(rows);
	}
}