package com.supermap.desktop.newtheme;

import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.TextStyle;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeLabel;
import com.supermap.ui.MapControl;

import javax.swing.*;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ThemeLabelUniformContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPane = new JTabbedPane();
	private transient ThemeLabelPropertyPanel panelProperty;
	private transient ThemeLabelAdvancePanel panelAdvance;

	private transient DatasetVector datasetVector;
	private transient Map map;
	private transient ThemeLabel themeLabel;
	private transient Layer themeLabelLayer;
	private transient TextStyle textStyle;
	private transient TextStyleContainer textStyleContainer;

	private boolean isRefreshAtOnece = true;
	
	private transient LocalPropertyChangeListener propertyChangeListener = new LocalPropertyChangeListener();

	public ThemeLabelUniformContainer(DatasetVector datasetVector, ThemeLabel themeLabel) {
		this.datasetVector = datasetVector;
		this.themeLabel = new ThemeLabel(themeLabel);
		this.map = initCurrentTheme(datasetVector);
		initComponents();
	}

	public ThemeLabelUniformContainer(Layer layer) {
		this.themeLabelLayer = layer;
		this.themeLabel = new ThemeLabel((ThemeLabel) layer.getTheme());
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.map = ThemeGuideFactory.getMapControl().getMap();
		this.textStyle = ((ThemeLabel) themeLabelLayer.getTheme()).getUniformStyle();
		initComponents();
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		this.setLayout(new GridBagLayout());
		this.panelProperty = new ThemeLabelPropertyPanel(themeLabelLayer);
		this.panelAdvance = new ThemeLabelAdvancePanel(themeLabelLayer);
		this.textStyleContainer = new TextStyleContainer(textStyle, map, themeLabelLayer);
		this.textStyleContainer.addPropertyChangeListener("ThemeChange", this.propertyChangeListener);
		this.panelProperty.addPropertyChangeListener("ThemeChange", this.propertyChangeListener);
		this.panelAdvance.addPropertyChangeListener("ThemeChange", this.propertyChangeListener);
		this.textStyleContainer.setUniformStyle(true);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Style"), textStyleContainer);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Advanced"), this.panelAdvance);
		this.tabbedPane.setSelectedIndex(1);
		this.add(this.tabbedPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH)
				.setWeight(1, 1));
	}

	/**
	 * 初始化单值专题图
	 *
	 * @param dataset
	 * @return
	 */
	private Map initCurrentTheme(DatasetVector dataset) {
		MapControl mapControl = ThemeGuideFactory.getMapControl();
		if (null != mapControl) {
			this.themeLabelLayer = mapControl.getMap().getLayers().add(dataset, themeLabel, true);
			FieldInfo fieldInfo = datasetVector.getFieldInfos().get(0);
			String item = datasetVector.getName() + "." + fieldInfo.getName();
			((ThemeLabel) this.themeLabelLayer.getTheme()).setLabelExpression(item);
			this.textStyle = themeLabel.getUniformStyle();
			UICommonToolkit.getLayersManager().getLayersTree().setSelectionRow(0);
			mapControl.getMap().refresh();
		}
		return mapControl.getMap();
	}

	public boolean isRefreshAtOnece() {
		return isRefreshAtOnece;
	}

	public void setRefreshAtOnece(boolean isRefreshAtOnece) {
		this.isRefreshAtOnece = isRefreshAtOnece;
	}

	public Layer getThemeLabelLayer() {
		return themeLabelLayer;
	}

	public void setThemeLabelLayer(Layer themeLabelLayer) {
		this.themeLabelLayer = themeLabelLayer;
	}

	@Override
	public Theme getCurrentTheme() {
		return themeLabel;
	}

	@Override
	void registActionListener() {
		// do nothing
		unregistActionListener();
	}

	@Override
	public void unregistActionListener() {
		panelProperty.unregistActionListener();
		panelAdvance.unregistActionListener();
		textStyleContainer.unregistActionListener();
	}

	@Override
	void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.textStyleContainer.setRefreshAtOnce(isRefreshAtOnce);
		this.panelProperty.setRefreshAtOnce(isRefreshAtOnce);
		this.panelAdvance.setRefreshAtOnce(isRefreshAtOnce);
	}

	@Override
	void refreshMapAndLayer() {
		this.textStyleContainer.refreshMapAndLayer();
		this.panelProperty.refreshMapAndLayer();
		this.panelAdvance.refreshMapAndLayer();
	}

	class LocalPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ThemeLabelUniformContainer.this.firePropertyChange("ThemeChange", null, null);
		}
	}
}
