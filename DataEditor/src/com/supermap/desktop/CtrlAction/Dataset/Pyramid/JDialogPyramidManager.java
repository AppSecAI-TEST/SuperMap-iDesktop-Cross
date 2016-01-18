package com.supermap.desktop.CtrlAction.Dataset.Pyramid;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CellRenders.TabelDatasourceCellRender;
import com.supermap.desktop.ui.controls.CellRenders.TableDatasetCellRender;
import com.supermap.desktop.ui.controls.DatasetChooser;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SortTable.SortTable;
import com.supermap.desktop.utilties.TableUtilties;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * 栅格数据管理器
 * Created by XiaJt on 2016/1/1.
 */
public class JDialogPyramidManager extends SmDialog {

	//region 定义变量
	// 工具条
	private JToolBar toolBar;
	private JButton buttonAdd;
	private JButton buttonSelectAll;
	private JButton buttonSelectInvert;
	private JButton buttonDelete;

	// 表
	private JScrollPane scrollPaneTable;
	private SortTable tableDatasets;
	private PyramidManagerTableModel pyramidManagerTableModel;

	// 按钮栏
	private JPanel panelButtons;
	private JCheckBox checkBoxAutoClose;
	private JButton buttonCreate;
	/**
	 * 删除数据集影像金字塔按钮
	 */
	private JButton buttonRemove;
	private JButton buttonClose;


	public static final int ColumnSourceDatasetIndex = 0;
	public static final int ColumnSourceDatasourceIndex = 1;

	private DatasetChooser datasetChooser;

	private DatasetType[] supportDatasetTypes = new DatasetType[]{
			DatasetType.GRID, DatasetType.GRIDCOLLECTION,
			DatasetType.IMAGE, DatasetType.IMAGECOLLECTION
	};

	//endregion

	public JDialogPyramidManager() {
		initComponents();
		initResources();
		initLayouts();
		addListeners();
		initComponentStates();
		this.setTitle(DataEditorProperties.getString("String_FormDatasetPyramidManager_FormTitle"));
		this.setSize(677, 405);
		this.setLocationRelativeTo(null);
	}

	private void initComponents() {
		// 工具条
		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);
		this.buttonAdd = new JButton();
		this.buttonSelectAll = new JButton();
		this.buttonSelectInvert = new JButton();
		this.buttonDelete = new JButton();

		// 表
		this.scrollPaneTable = new JScrollPane();
		this.tableDatasets = new SortTable();
		this.pyramidManagerTableModel = new PyramidManagerTableModel();

		// 按钮栏
		this.panelButtons = new JPanel();
		this.checkBoxAutoClose = new JCheckBox();
		this.buttonCreate = new JButton();
		this.buttonRemove = new JButton();
		this.buttonClose = new JButton();


		datasetChooser = new DatasetChooser(this) {
			@Override
			protected boolean isSupportDatasource(Datasource datasource) {
				if (datasource.isReadOnly()) {
					if (datasource.getEngineType() == EngineType.IMAGEPLUGINS) {
						String server = datasource.getConnectionInfo().getServer().toLowerCase();
						if (!server.endsWith(".img") && !server.endsWith(".tif") && !server.endsWith(".tiff")) {
							return false;
						}
					} else {
						return false;
					}
				}
				return super.isSupportDatasource(datasource);
			}
		};
		datasetChooser.setSupportDatasetTypes(supportDatasetTypes);
	}


	private void initResources() {
		this.buttonAdd.setIcon(new ImageIcon(JDialogPyramidManager.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_AddMap.png")));
		this.buttonSelectAll.setIcon(new ImageIcon(JDialogPyramidManager.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		this.buttonSelectInvert.setIcon(new ImageIcon(JDialogPyramidManager.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		this.buttonDelete.setIcon(new ImageIcon(JDialogPyramidManager.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		this.checkBoxAutoClose.setText(CommonProperties.getString(CommonProperties.CloseDialog));
		this.buttonCreate.setText(CommonProperties.getString(CommonProperties.Create));
		this.buttonRemove.setText(CommonProperties.getString(CommonProperties.Delete));
		this.buttonClose.setText(CommonProperties.getString(CommonProperties.Close));
	}

	//region 初始化布局
	private void initLayouts() {
		initToolBar();
		initTable();
		initPanelButtons();

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 0));
		centerPanel.add(scrollPaneTable, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 98).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 5, 0));
		centerPanel.add(panelButtons, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));

		this.setLayout(new GridBagLayout());
		this.add(centerPanel, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10));
	}

	private void initToolBar() {
		this.toolBar.add(buttonAdd);
		this.toolBar.add(createSeparator());
		this.toolBar.add(buttonSelectAll);
		this.toolBar.add(buttonSelectInvert);
		this.toolBar.add(createSeparator());
		this.toolBar.add(buttonDelete);
	}

	private JToolBar.Separator createSeparator() {
		JToolBar.Separator separator = new JToolBar.Separator();
		separator.setOrientation(SwingConstants.VERTICAL);
		return separator;
	}

	private void initTable() {
		this.scrollPaneTable.setViewportView(tableDatasets);
		this.tableDatasets.setModel(pyramidManagerTableModel);
		this.tableDatasets.getColumnModel().getColumn(ColumnSourceDatasetIndex).setCellRenderer(new TableDatasetCellRender());

		this.tableDatasets.getColumnModel().getColumn(ColumnSourceDatasourceIndex).setCellRenderer(new TabelDatasourceCellRender());
	}

	private void initPanelButtons() {
		this.panelButtons.setLayout(new GridBagLayout());
		this.panelButtons.add(checkBoxAutoClose, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(97, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 0, 5));
		this.panelButtons.add(buttonCreate, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 0, 5));
		this.panelButtons.add(buttonRemove, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 0, 5));
		this.panelButtons.add(buttonClose, new GridBagConstraintsHelper(3, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST));
	}
	//endregion

	//region 添加监听事件
	private void addListeners() {
		this.buttonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showDatasetChooseDialog();
			}
		});

		this.buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogPyramidManager.this.tableDatasets.selectAll();
			}
		});

		this.buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilties.invertSelection(tableDatasets);
			}
		});

		this.buttonDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tableDatasets.getSelectedRow();
				pyramidManagerTableModel.deleteRows(tableDatasets.getSelectedRows());
				if (tableDatasets.getRowCount() > selectedRow) {
					tableDatasets.setRowSelectionInterval(selectedRow, selectedRow);
				} else if (tableDatasets.getRowCount() > 0) {
					tableDatasets.setRowSelectionInterval(tableDatasets.getRowCount() - 1, tableDatasets.getRowCount() - 1);
				}
			}
		});

		this.buttonCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bulidPyramid();
			}
		});

		this.buttonRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deletePyramid();
			}
		});

		this.buttonClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogPyramidManager.this.dispose();
			}
		});

		this.tableDatasets.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkButtonStates();
			}
		});

		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
					showDatasetChooseDialog();
				}
			}
		};
		this.tableDatasets.addMouseListener(mouseAdapter);
		this.scrollPaneTable.addMouseListener(mouseAdapter);
		this.pyramidManagerTableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				checkButtonStates();
			}
		});
	}

	private void showDatasetChooseDialog() {
		if (datasetChooser.showDialog() == DialogResult.OK) {
			int beforeRowCount = tableDatasets.getRowCount();
			pyramidManagerTableModel.addDataset(datasetChooser.getSelectedDatasets());
			int afterRowCount = tableDatasets.getRowCount();
			if (afterRowCount > beforeRowCount) {
				tableDatasets.setRowSelectionInterval(beforeRowCount, afterRowCount - 1);
				tableDatasets.scrollRectToVisible(tableDatasets.getCellRect(tableDatasets.getRowCount() - 1, 0, true));
			}
		}

	}

	private void bulidPyramid() {
		if (pyramidManagerTableModel.bulidPyramid() && checkBoxAutoClose.isSelected()) {
			this.dispose();
		}
	}

	private void deletePyramid() {
		try {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

			if (pyramidManagerTableModel.deletePyramid() && checkBoxAutoClose.isSelected()) {
				this.dispose();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.setCursor(Cursor.getDefaultCursor());
		}
	}


	//region 检查按钮是否可用
	private void checkButtonStates() {
		checkButtonSelectAllAndInvertState();
		checkButtonDeleteState();
		checkButtonCreateState();
		checkButtonRemoveState();
	}

	private void checkButtonSelectAllAndInvertState() {
		if (tableDatasets.getRowCount() > 0 != buttonSelectAll.isEnabled()) {
			buttonSelectAll.setEnabled(tableDatasets.getRowCount() > 0);
			buttonSelectInvert.setEnabled(tableDatasets.getRowCount() > 0);
		}
	}


	private void checkButtonDeleteState() {
		if (tableDatasets.getSelectedRows().length > 0 != buttonDelete.isEnabled()) {
			buttonDelete.setEnabled(tableDatasets.getSelectedRows().length > 0);
		}
	}

	private void checkButtonCreateState() {
		if (pyramidManagerTableModel.isCreateEnable() != buttonCreate.isEnabled()) {
			buttonCreate.setEnabled(pyramidManagerTableModel.isCreateEnable());
		}
	}

	private void checkButtonRemoveState() {
		if (pyramidManagerTableModel.isRemoveEnable() != buttonRemove.isEnabled()) {
			buttonRemove.setEnabled(pyramidManagerTableModel.isRemoveEnable());
		}
	}
	//endregion
	//endregion


	private void initComponentStates() {
		buttonSelectAll.setEnabled(false);
		buttonSelectInvert.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonCreate.setEnabled(false);
		buttonRemove.setEnabled(false);
		checkBoxAutoClose.setSelected(true);

		Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();
		java.util.List<Dataset> activeSupportDatasets = new ArrayList<>();
		java.util.List<DatasetType> supportDatasetTypeList = new ArrayList<>();
		for (DatasetType datasetType : supportDatasetTypes) {
			supportDatasetTypeList.add(datasetType);
		}
		for (Dataset activeDataset : activeDatasets) {
			if (activeDataset.getDatasource().getEngineType() == EngineType.IMAGEPLUGINS) {
				String server = activeDataset.getDatasource().getConnectionInfo().getServer().toLowerCase();
				if (!server.endsWith(".img") && !server.endsWith(".tif") && !server.endsWith(".tiff")) {
					continue;
				}
			} else if (activeDataset.getDatasource().isReadOnly()) {
				continue;
			}
			if (supportDatasetTypeList.contains(activeDataset.getType())) {
				activeSupportDatasets.add(activeDataset);
			}
		}
		pyramidManagerTableModel.setCurrentDatasets(activeSupportDatasets);

	}

	@Override
	public void dispose() {
		datasetChooser.dispose();
		super.dispose();
	}
}
