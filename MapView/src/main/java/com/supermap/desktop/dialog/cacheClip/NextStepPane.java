package com.supermap.desktop.dialog.cacheClip;

import com.supermap.data.Geometry;
import com.supermap.data.Rectangle2D;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.data.processing.StorageType;
import com.supermap.data.processing.TileFormat;
import com.supermap.data.processing.TileSize;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.map.propertycontrols.PanelGroupBoxViewBounds;
import com.supermap.desktop.mapview.map.propertycontrols.SelectObjectListener;
import com.supermap.desktop.ui.SMSpinner;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

/**
 * Created by xie on 2017/4/27.
 * Panel for cache clipping(second step)
 */
public class NextStepPane extends JPanel implements IState {
    public Rectangle2D cacheRangeBounds;
    public Rectangle2D indexRangeBounds;
    private boolean validCacheRangeBounds;
    private boolean validIndexRangeBounds;
    public MapCacheBuilder mapCacheBuilder;
    private boolean singleProcessClip;
    public java.util.Map<Layer, java.util.List<Geometry>> selectedGeometryAndLayer;
    public Rectangle2D selectGeometryRectangle;
    private Vector<EnabledListener> enabledListeners;
    public boolean imagePixelChanged;

    // Parameter setting  Control
    public PanelGroupBoxViewBounds panelCacheRange;
    public PanelGroupBoxViewBounds panelIndexRange;
    private WaringTextField cacheRangeWaringTextFieldLeft;
    private WaringTextField cacheRangeWaringTextFieldTop;
    private WaringTextField cacheRangeWaringTextFieldRight;
    private WaringTextField cacheRangeWaringTextFieldBottom;
    private WaringTextField indexRangeWaringTextFieldLeft;
    private WaringTextField indexRangeWaringTextFieldTop;
    private WaringTextField indexRangeWaringTextFieldRight;
    private WaringTextField indexRangeWaringTextFieldBottom;
    private Map currentMap;
    private JLabel labelImageType;
    private JLabel labelPixel;
    private JLabel labelImageCompressionRatio;
    public JComboBox comboBoxImageType;
    public JComboBox comboBoxPixel;
    public SMSpinner smSpinnerImageCompressionRatio;
    public JCheckBox checkBoxBackgroundTransparency;
    public JCheckBox checkBoxFullFillCacheImage;

    private JLabel labelTaskStorePath;
    private WarningOrHelpProvider helpForTaskStorePath;
    public JFileChooserControl fileChooserControlTaskPath;
    private JCheckBox checkBoxClipOnThisComputer;
    private JCheckBox checkBoxMultiProcessClip;
    private JPanel panelMultiProcess;
    private DialogMapCacheClipBuilder parent;

    private CaretListener cacheRangeCareListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            if (StringUtilities.isNumber(cacheRangeWaringTextFieldBottom.getText().replace(",", "")) && StringUtilities.isNumber(cacheRangeWaringTextFieldLeft.getText().replace(",", ""))
                    && StringUtilities.isNumber(cacheRangeWaringTextFieldRight.getText().replace(",", "")) && StringUtilities.isNumber(cacheRangeWaringTextFieldTop.getText().replace(",", ""))) {
                Rectangle2D rectangle2D = panelCacheRange.getRangeBound();
                if (rectangle2D != null) {
                    validCacheRangeBounds = true;
                    cacheRangeBounds = rectangle2D;
                    mapCacheBuilder.setBounds(cacheRangeBounds);
                } else {
                    validCacheRangeBounds = false;
                }
            } else {
                validCacheRangeBounds = false;
            }
            fireEnabled(isRightRangeBounds());
        }
    };
    private CaretListener indexRangeCareListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            if (StringUtilities.isNumber(indexRangeWaringTextFieldTop.getText()) && StringUtilities.isNumber(indexRangeWaringTextFieldBottom.getText())
                    && StringUtilities.isNumber(indexRangeWaringTextFieldLeft.getText()) && StringUtilities.isNumber(indexRangeWaringTextFieldRight.getText())) {
                Rectangle2D rectangle2D = panelIndexRange.getRangeBound();
                if (rectangle2D != null) {
                    indexRangeBounds = rectangle2D;
                    mapCacheBuilder.setIndexBounds(indexRangeBounds);
                    validIndexRangeBounds = true;
                } else {
                    validIndexRangeBounds = false;
                }
            } else {
                validIndexRangeBounds = false;
            }
            fireEnabled(isRightRangeBounds());
        }
    };
    private SelectObjectListener selectObjectListener = new SelectObjectListener() {
        @Override
        public void selectObjectListener() {
            if (panelCacheRange.getSelectedGeometryAndLayer() != null && panelCacheRange.getSelectedGeometryAndLayer().size() > 0) {
                checkBoxFullFillCacheImage.setEnabled(true);
                selectedGeometryAndLayer = panelCacheRange.getSelectedGeometryAndLayer();
                selectGeometryRectangle = panelCacheRange.getRangeBound().clone();
                isSelectedGeometry();
            } else {
                checkBoxFullFillCacheImage.setSelected(false);
                checkBoxFullFillCacheImage.setEnabled(false);
                selectedGeometryAndLayer = null;
                selectGeometryRectangle = null;
            }
        }
    };

    private boolean isRightRangeBounds() {
        boolean result = true;
        isSelectedGeometry();
        if (!this.validIndexRangeBounds || !this.validCacheRangeBounds) {
            result = false;
        }
        return result;
    }

    public NextStepPane(DialogMapCacheClipBuilder parent, MapCacheBuilder mapCacheBuilder, boolean singleProcessClip) {
        super();
        this.parent = parent;
        this.mapCacheBuilder = mapCacheBuilder;
        this.currentMap = this.mapCacheBuilder.getMap();
        this.singleProcessClip = singleProcessClip;
        init();
    }

    private void init() {
        initComponents();
        initResources();
        initPanelImageState();
        initLayout();
        registEvents();
    }

    private ItemListener comboboxImageTypeListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getItem().toString().equals("PNG") || e.getItem().toString().equals("DXTZ") || e.getItem().toString().equals("PNG8") || e.getItem().toString().equals("GIF")) {
                checkBoxBackgroundTransparency.setSelected(false);
                checkBoxBackgroundTransparency.setEnabled(true);
                if (e.getItem().toString().equals("PNG")) {
                    mapCacheBuilder.setTileFormat(TileFormat.PNG);
                } else if (e.getItem().toString().equals("DXTZ")) {
                    mapCacheBuilder.setTileFormat(TileFormat.DXTZ);
                } else if (e.getItem().toString().equals("PNG8")) {
                    mapCacheBuilder.setTileFormat(TileFormat.PNG8);
                } else if (e.getItem().toString().equals("GIF")) {
                    mapCacheBuilder.setTileFormat(TileFormat.GIF);
                }
            } else if (e.getItem().toString().equals("JPG")) {
                checkBoxBackgroundTransparency.setSelected(false);
                checkBoxBackgroundTransparency.setEnabled(false);
                mapCacheBuilder.setTileFormat(TileFormat.JPG);
            } else if (e.getItem().toString().equals("JPG_PNG")) {
                checkBoxBackgroundTransparency.setSelected(true);
                checkBoxBackgroundTransparency.setEnabled(false);
                mapCacheBuilder.setTileFormat(TileFormat.JPG_PNG);
            }
        }
    };

    private ItemListener comboboxImagePixelListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                imagePixelChanged = true;
                if (e.getItem().toString().equals("64*64")) {
                    mapCacheBuilder.setTileSize(TileSize.SIZE64);
                } else if (e.getItem().toString().equals("128*128")) {
                    mapCacheBuilder.setTileSize(TileSize.SIZE128);
                } else if (e.getItem().toString().equals("256*256")) {
                    mapCacheBuilder.setTileSize(TileSize.SIZE256);
                } else if (e.getItem().toString().equals("512*512")) {
                    mapCacheBuilder.setTileSize(TileSize.SIZE512);
                } else if (e.getItem().toString().equals("1024*1024")) {
                    mapCacheBuilder.setTileSize(TileSize.SIZE1024);
                }
            }
        }
    };

    public void removeEvents() {
        this.comboBoxImageType.removeItemListener(this.comboboxImageTypeListener);
        this.comboBoxPixel.removeItemListener(this.comboboxImagePixelListener);
        this.panelCacheRange.removeSelectObjectListener(this.selectObjectListener);
        this.cacheRangeWaringTextFieldLeft.getTextField().removeCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldTop.getTextField().removeCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldRight.getTextField().removeCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldBottom.getTextField().removeCaretListener(this.cacheRangeCareListener);
        this.indexRangeWaringTextFieldLeft.getTextField().removeCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldLeft.getTextField().removeCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldTop.getTextField().removeCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldRight.getTextField().removeCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldBottom.getTextField().removeCaretListener(this.indexRangeCareListener);
    }

    private void registEvents() {
        removeEvents();
        this.comboBoxImageType.addItemListener(this.comboboxImageTypeListener);
        this.comboBoxPixel.addItemListener(this.comboboxImagePixelListener);
        this.panelCacheRange.addSelectObjectLitener(this.selectObjectListener);
        this.cacheRangeWaringTextFieldLeft.getTextField().addCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldTop.getTextField().addCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldRight.getTextField().addCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldBottom.getTextField().addCaretListener(this.cacheRangeCareListener);
        this.indexRangeWaringTextFieldLeft.getTextField().addCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldLeft.getTextField().addCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldTop.getTextField().addCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldRight.getTextField().addCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldBottom.getTextField().addCaretListener(this.indexRangeCareListener);
    }

    private void initLayout() {
        JPanel panelImageParam = new JPanel();
        panelImageParam.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("MapCache_ImageParameter")));
        panelImageParam.setLayout(new GridBagLayout());
        panelImageParam.add(this.labelImageType, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 10));
        panelImageParam.add(this.comboBoxImageType, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 5, 10).setWeight(1, 0));
        panelImageParam.add(this.labelPixel, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelImageParam.add(this.comboBoxPixel, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
        panelImageParam.add(this.labelImageCompressionRatio, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelImageParam.add(this.smSpinnerImageCompressionRatio, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
        panelImageParam.add(this.checkBoxBackgroundTransparency, new GridBagConstraintsHelper(0, 3, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelImageParam.add(this.checkBoxFullFillCacheImage, new GridBagConstraintsHelper(0, 4, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 10, 5, 10));
        panelImageParam.add(new JPanel(), new GridBagConstraintsHelper(0, 5, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        if (!singleProcessClip) {
            JPanel innerPanel = new JPanel();
            innerPanel.setLayout(new GridBagLayout());
            innerPanel.add(this.labelTaskStorePath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 10));
            innerPanel.add(this.helpForTaskStorePath, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 0, 5, 0));
            innerPanel.add(this.fileChooserControlTaskPath, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 5, 10).setWeight(1, 0));
            innerPanel.add(this.checkBoxClipOnThisComputer, new GridBagConstraintsHelper(0, 1, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
            innerPanel.add(new JPanel(), new GridBagConstraintsHelper(0, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
            panelMultiProcess = new CompTitledPane(this.checkBoxMultiProcessClip, innerPanel);
        }
        this.setLayout(new GridBagLayout());
        this.add(this.panelCacheRange, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 5, 5).setWeight(1, 0));
        this.add(this.panelIndexRange, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 0, 5, 10).setWeight(1, 0));
        this.add(panelImageParam, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 5).setWeight(1, 0));
        this.add(this.panelMultiProcess, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
        this.add(new JPanel(), new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.panelCacheRange.setPreferredSize(new Dimension(400, 300));
    }

    private void initPanelImageState() {
        this.comboBoxImageType.addItem("PNG");
        this.comboBoxImageType.addItem("DXTZ");
        this.comboBoxImageType.addItem("GIF");
        this.comboBoxImageType.addItem("JPG");
        this.comboBoxImageType.addItem("JPG_PNG");
        this.comboBoxImageType.addItem("PNG8");
        this.comboBoxImageType.setSelectedItem("JPG");
        this.comboBoxPixel.addItem("64*64");
        this.comboBoxPixel.addItem("128*128");
        this.comboBoxPixel.addItem("256*256");
        this.comboBoxPixel.addItem("512*512");
        this.comboBoxPixel.addItem("1024*1024");
        this.comboBoxPixel.setSelectedItem("256*256");
        this.checkBoxBackgroundTransparency.setEnabled(false);
        this.checkBoxFullFillCacheImage.setEnabled(false);
        this.mapCacheBuilder.setTileFormat(TileFormat.JPG);
        this.mapCacheBuilder.setTileSize(TileSize.SIZE256);
        this.mapCacheBuilder.setStorageType(StorageType.Original);
    }

    private void initResources() {
        this.labelImageType.setText(MapViewProperties.getString("MapCache_ImageType"));
        this.labelPixel.setText(MapViewProperties.getString("MapCache_Pixel"));
        this.labelImageCompressionRatio.setText(MapViewProperties.getString("MapCache_ImageCompressionRation"));
        this.checkBoxBackgroundTransparency.setText(MapViewProperties.getString("MapCache_BackgroundTransparency"));
        this.checkBoxFullFillCacheImage.setText(MapViewProperties.getString("MapCache_FullFillCacheImage"));
        this.labelTaskStorePath.setText(MapViewProperties.getString("String_TaskStorePath"));
        this.checkBoxMultiProcessClip.setText(MapViewProperties.getString("String_SplitMultiProcessClip"));
        this.checkBoxClipOnThisComputer.setText(MapViewProperties.getString("String_ClipOnThisComputer"));
        this.helpForTaskStorePath.setToolTipText(MapViewProperties.getString("String_HelpForTaskStorePath"));
    }

    private void initComponents() {
        this.enabledListeners = new Vector<>();
        this.panelMultiProcess = new JPanel();
        this.panelCacheRange = new PanelGroupBoxViewBounds(parent, MapViewProperties.getString("MapCache_CacheRange"), this.currentMap);
        this.panelIndexRange = new PanelGroupBoxViewBounds(parent, MapViewProperties.getString("MapCache_IndexRange"), this.currentMap);
        this.cacheRangeBounds = this.mapCacheBuilder.getBounds();
        this.indexRangeBounds = this.mapCacheBuilder.getIndexBounds();
        this.labelImageType = new JLabel();
        this.labelPixel = new JLabel();
        this.labelImageCompressionRatio = new JLabel();
        this.comboBoxImageType = new JComboBox();
        this.comboBoxPixel = new JComboBox();
        this.smSpinnerImageCompressionRatio = new SMSpinner(new SpinnerNumberModel(this.mapCacheBuilder.getImageCompress(), 0, 100, 1));
        this.checkBoxFullFillCacheImage = new JCheckBox();
        this.checkBoxBackgroundTransparency = new JCheckBox();
        this.labelTaskStorePath = new JLabel();
        this.helpForTaskStorePath = new WarningOrHelpProvider(MapViewProperties.getString("String_HelpForTaskStorePath"), false);
        this.fileChooserControlTaskPath = new JFileChooserControl();
        String moduleName = "ChooseCacheClipDirectories";
        if (!SmFileChoose.isModuleExist(moduleName)) {
            SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
                    moduleName, "GetDirectories");
        }

        SmFileChoose fileChoose = new SmFileChoose(moduleName);
        this.fileChooserControlTaskPath.setFileChooser(fileChoose);
        this.fileChooserControlTaskPath.setPath(System.getProperty("user.dir"));
        this.checkBoxClipOnThisComputer = new JCheckBox();
        this.checkBoxMultiProcessClip = new JCheckBox();
        this.checkBoxMultiProcessClip.setSelected(true);
        this.checkBoxMultiProcessClip.setEnabled(false);
        this.cacheRangeWaringTextFieldLeft = this.panelCacheRange.getTextFieldCurrentViewLeft();
        this.cacheRangeWaringTextFieldTop = this.panelCacheRange.getTextFieldCurrentViewTop();
        this.cacheRangeWaringTextFieldRight = this.panelCacheRange.getTextFieldCurrentViewRight();
        this.cacheRangeWaringTextFieldBottom = this.panelCacheRange.getTextFieldCurrentViewBottom();
        this.indexRangeWaringTextFieldLeft = this.panelIndexRange.getTextFieldCurrentViewLeft();
        this.indexRangeWaringTextFieldTop = this.panelIndexRange.getTextFieldCurrentViewTop();
        this.indexRangeWaringTextFieldRight = this.panelIndexRange.getTextFieldCurrentViewRight();
        this.indexRangeWaringTextFieldBottom = this.panelIndexRange.getTextFieldCurrentViewBottom();
        this.checkBoxClipOnThisComputer.setSelected(true);
    }

    private void isSelectedGeometry() {
        if (this.selectedGeometryAndLayer == null || this.selectedGeometryAndLayer.size() == 0 || !this.selectGeometryRectangle.equals(this.cacheRangeBounds)) {
            checkBoxFullFillCacheImage.setSelected(false);
            checkBoxFullFillCacheImage.setEnabled(false);
            this.selectedGeometryAndLayer = null;
        } else {
            checkBoxFullFillCacheImage.setEnabled(true);
            checkBoxFullFillCacheImage.setEnabled(true);
        }
    }

    @Override
    public void addEnabledListener(EnabledListener enabledListener) {
        if (null == enabledListeners) {
            enabledListeners = new Vector<>();
        }
        if (null != enabledListener && !enabledListeners.contains(enabledListener)) {
            enabledListeners.add(enabledListener);
        }
    }

    @Override
    public void removeEnabledListener(EnabledListener enabledListener) {
        if (null != enabledListener && enabledListeners.contains(enabledListener)) {
            enabledListeners.remove(enabledListener);
        }
    }

    private void fireEnabled(boolean enabled) {
        Vector<EnabledListener> listeners = this.enabledListeners;
        for (EnabledListener listener : listeners) {
            listener.setEnabled(new EnabledEvent(enabled));
        }
    }
}