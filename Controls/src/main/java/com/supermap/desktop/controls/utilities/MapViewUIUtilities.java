package com.supermap.desktop.controls.utilities;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Point2D;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.JDialogConfirm;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.progress.callable.CreateImagePyramidCallable;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilities.ImagePyramidUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.ui.Action;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Vector;

public class MapViewUIUtilities {

	public static int CHINACENTERRANGEVALUEX = 107;
	public static int CHINACENTERRANGEVALUEY = 35;
	public static double INITSCALE = 0.00000002;

	private MapViewUIUtilities() {
		// 工具类，不提供构造方法
	}

	/**
	 * 将数据集添加到指定的地图
	 *
	 * @param map
	 * @param datasets
	 * @param addToHead
	 */
	public static Layer[] addDatasetsToMap(Map map, Dataset[] datasets, boolean addToHead) {
		Vector<Layer> layers = new Vector<>();

		if (datasets == null || datasets.length == 0) {
			return null;
		}

		// 预处理较大的栅格或者影像数据集，创建影像金字塔
		ArrayList<Dataset> needCreateImagePyramid = new ArrayList<Dataset>();
		boolean isUsedAsDefault = false; // 是否将当前选择作为后续的默认设置，不再提示
		JDialogConfirm dialogConfirm = new JDialogConfirm();

		for (Dataset dataset : datasets) {
			if (ImagePyramidUtilities.isNeedBuildPyramid(dataset)) {
				dialogConfirm.setMessage(MessageFormat.format(ControlsProperties.getString("String_IsBuildPyramid"), dataset.getName()));
				if (!isUsedAsDefault) {
					dialogConfirm.showDialog();
					isUsedAsDefault = dialogConfirm.isUsedAsDefault();
				}

				if (dialogConfirm.getDialogResult() == DialogResult.OK) {
					needCreateImagePyramid.add(dataset);
				}
			}
		}

		if (!needCreateImagePyramid.isEmpty()) {
			FormProgressTotal formProgressTotal = new FormProgressTotal(ControlsProperties.getString("String_Form_BuildDatasetPyramid"));
			formProgressTotal.doWork(new CreateImagePyramidCallable(needCreateImagePyramid.toArray(new Dataset[needCreateImagePyramid.size()])));
		}

		SortUIUtilities.sortList(datasets);
		// 添加到地图
		for (Dataset dataset : datasets) {
			if (dataset.getType() != DatasetType.TABULAR && dataset.getType() != DatasetType.TOPOLOGY) {
				layers.add(MapUtilities.addDatasetToMap(map, dataset, addToHead));
			}
		}
		MapUtilities.setDynamic(datasets, map);
		// 更新地图属性面板
		Application.getActiveApplication().resetActiveForm();
		map.refresh();
		UICommonToolkit.getLayersManager().setMap(map);
		return layers.toArray(new Layer[layers.size()]);
	}

	/**
	 * 将数据集添加到指定的地图
	 *
	 * @param map
	 * @param datasets
	 * @param index
	 */
	public static Layer[] insertDatasetsToMap(Map map, Dataset[] datasets, int index) {
		Vector<Layer> layers = new Vector<>();

		if (datasets == null || datasets.length == 0 || index < 0) {
			return null;
		}

		// 预处理较大的栅格或者影像数据集，创建影像金字塔
		ArrayList<Dataset> needCreateImagePyramid = new ArrayList<Dataset>();
		boolean isUsedAsDefault = false; // 是否将当前选择作为后续的默认设置，不再提示
		JDialogConfirm dialogConfirm = new JDialogConfirm();

		for (Dataset dataset : datasets) {
			if (ImagePyramidUtilities.isNeedBuildPyramid(dataset)) {
				dialogConfirm.setMessage(MessageFormat.format(ControlsProperties.getString("String_IsBuildPyramid"), dataset.getName()));
				if (!isUsedAsDefault) {
					dialogConfirm.showDialog();
					isUsedAsDefault = dialogConfirm.isUsedAsDefault();
				}

				if (dialogConfirm.getDialogResult() == DialogResult.OK) {
					needCreateImagePyramid.add(dataset);
				}
			}
		}

		if (!needCreateImagePyramid.isEmpty()) {
			FormProgressTotal formProgressTotal = new FormProgressTotal(ControlsProperties.getString("String_Form_BuildDatasetPyramid"));
			formProgressTotal.doWork(new CreateImagePyramidCallable(needCreateImagePyramid.toArray(new Dataset[needCreateImagePyramid.size()])));
		}

		SortUIUtilities.sortList(datasets);
		// 添加到地图
		for (Dataset dataset : datasets) {
			if (dataset.getType() != DatasetType.TABULAR && dataset.getType() != DatasetType.TOPOLOGY) {
				layers.add(MapUtilities.insertDatasetToMap(map, dataset, index));
			}
		}
		MapUtilities.setDynamic(datasets, map);
		// 更新地图属性面板
		Application.getActiveApplication().resetActiveForm();
		map.refresh();
		UICommonToolkit.getLayersManager().setMap(map);
		return layers.toArray(new Layer[layers.size()]);
	}

	/**
	 * 将数据集打开到新的窗口
	 *
	 * @param datasets
	 * @param addToHead
	 */
	public static void addDatasetsToNewWindow(Dataset[] datasets, boolean addToHead) {
		ArrayList<Dataset> datasetsToMap = new ArrayList<>(); // 可以添加到地图上的数据集

		for (Dataset dataset : datasets) {
			if (dataset.getType() == DatasetType.TABULAR) {
				// 如果带有纯属性数据集，在单独的属性窗口中打开
				TabularUtilities.openDatasetVectorFormTabular(dataset);
			} else if (dataset.getType() == DatasetType.LINKTABLE) {
				// todo 暂时什么都不做
			} else if (dataset.getType() == DatasetType.TOPOLOGY) {
				// todo 暂时什么都不做
			} else {
				datasetsToMap.add(dataset);
			}
		}

		if (!datasetsToMap.isEmpty()) {
			String name = MapUtilities.getAvailableMapName(
					MessageFormat.format("{0}@{1}", datasetsToMap.get(0).getName(), datasetsToMap.get(0).getDatasource().getAlias()), true);
			IFormMap formMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP, name);
			Map map = formMap.getMapControl().getMap();
			addDatasetsToMap(map, datasetsToMap.toArray(new Dataset[datasetsToMap.size()]), addToHead);

			// 打开到新地图时，全幅显示，不使用 EntireView，因为窗口打开之后会动态调整 MapControl 的大小，从而导致此前设置的全幅效果不对
			Rectangle2D viewBounds = getDatasetsBounds(datasetsToMap.toArray(new Dataset[datasetsToMap.size()]));
			if (viewBounds != null && Double.compare(viewBounds.getWidth(), 0.0) != 0 && Double.compare(viewBounds.getHeight(), 0.0) != 0) {
				map.setViewBounds(viewBounds);
			}
			//判断所打开的数据集是否需要手动设置合理范围--yuanR 17.1.11
			boolean isResetRange = isResetRange(datasetsToMap.toArray(new Dataset[datasetsToMap.size()]));
			if (isResetRange) {
				//当要打开的数据集为地理数据集时，并且其范围超出合理值，给其设置坐标系为中国范围坐标系--yuanR 17.1.10
				resetMapRange(map);
			}
			// 新建的地图窗口，修改默认的Action为漫游
			formMap.getMapControl().setAction(Action.PAN);
		}
	}

	/**
	 * 设置坐标系为中国范围坐标系
	 *
	 * @param map
	 */
	public static void resetMapRange(Map map) {
		map.setScale(INITSCALE);
		map.setCenter(new Point2D(CHINACENTERRANGEVALUEX, CHINACENTERRANGEVALUEY));
	}

	/**
	 * 获取数据集的 bounds
	 *
	 * @param datasets 需要获取bounds的数据集数组
	 * @return bounds 如果数据集全为空，返回Null
	 */
	public static Rectangle2D getDatasetsBounds(Dataset[] datasets) {
		Rectangle2D bounds = null;
		try {
			for (Dataset dataset : datasets) {
				if (bounds == null) {
					bounds = dataset.getBounds().clone();
				} else {
					bounds.union(dataset.getBounds());
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return bounds;

	}

	/**
	 * @param datasets
	 * @return
	 * @arther yuanR 17.1.11
	 * 判断是否需要重新设置地图范围方法
	 */
	public static boolean isResetRange(Dataset[] datasets) {
		if (datasets == null || datasets.length <= 0) {
			return false;
		}
		for (Dataset dataset : datasets) {
			//满足地理坐标系数据集
			if ((dataset.getPrjCoordSys() == null || dataset.getPrjCoordSys().getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE)
					|| dataset.getBounds().getTop() != 0 || dataset.getBounds().getBottom() != 0
					|| dataset.getBounds().getLeft() != 0 || dataset.getBounds().getRight() != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 全选指定 IFormMap 可编辑图层的 Geometry
	 *
	 * @param formMap 指定的 IFormMap
	 * @return 选中的对象数
	 */
	public static int selectAllGeometry(IFormMap formMap) {
		// 记录选择集
		int count = 0;

		try {
			ArrayList<Layer> layers = MapUtilities.getLayers(formMap.getMapControl().getMap());

			for (Layer layer : layers) {
				if (layer.isVisible() && layer.isSelectable()) {
					DatasetVector dataset = (DatasetVector) layer.getDataset();
					if (dataset != null) {
						Recordset recordset = dataset.getRecordset(false, CursorType.STATIC);
						layer.getSelection().fromRecordset(recordset);
						count += dataset.getRecordCount();
						recordset.dispose();
					}

				}
			}
			formMap.getMapControl().getMap().refresh();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return count;
	}

	public static int reverseSelection(IFormMap formMap) {
		// 记录选择集
		int count = 0;

		try {
			ArrayList<Layer> layers = MapUtilities.getLayers(formMap.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.isVisible() && layer.isSelectable() && layer.getSelection() != null) {
					Recordset preRecordset = layer.getSelection().toRecordset();
					DatasetVector dataset = (DatasetVector) layer.getDataset();
					Recordset recordset = dataset.getRecordset(false, CursorType.STATIC);
					layer.getSelection().fromRecordset(recordset);
					while (!preRecordset.isEOF()) {
						layer.getSelection().remove(preRecordset.getID());
						preRecordset.moveNext();
					}

					count += recordset.getRecordCount() - preRecordset.getRecordCount();
					preRecordset.dispose();
					recordset.dispose();
				}
			}
			formMap.getMapControl().getMap().refresh();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return count;
	}

	public static int clearAllSelection(IFormMap formMap) {
		try {
			ArrayList<Layer> layers = MapUtilities.getLayers(formMap.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
					layer.getSelection().clear();
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		formMap.getMapControl().getMap().refresh();
		return 0;
	}

	public static int calculateSelectNumber(IFormMap formMap) {
		ArrayList<Layer> layers = MapUtilities.getLayers(formMap.getMapControl().getMap());
		int count = 0;
		for (Layer layer : layers) {
			if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
				count += layer.getSelection().getCount();
			}
		}
		return count;
	}

	/**
	 * 打开工作空间中已存在的地图
	 *
	 * @param mapName 地图名称
	 * @return 是否打开
	 */
	public static boolean openMap(String mapName) {
		int index = -1;
		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
		for (int i = 0; i < formManager.getCount(); i++) {
			if (!StringUtilities.isNullOrEmpty(formManager.get(i).getText()) && formManager.get(i).getText().equals(mapName) && formManager.get(i).getWindowType() == WindowType.MAP) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			formManager.setActiveForm(formManager.get(index));
			return true;
		} else {
			IFormMap formMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP, mapName);
			if (formMap != null) {
				formMap.openMap(mapName);
				Application.getActiveApplication().resetActiveForm();
				return true;
			}
		}
		return false;
	}

	/**
	 * 刷新当前地图
	 *
	 * @return 是否成功
	 */
	public static boolean refreshCurrentMap() {
		boolean issuccss = false;
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormMap) {
			try {
				((IFormMap) activeForm).getMapControl().getMap().refresh();
				issuccss = true;
			} catch (Exception e) {
				issuccss = false;
			}
		}
		return issuccss;
	}
}
