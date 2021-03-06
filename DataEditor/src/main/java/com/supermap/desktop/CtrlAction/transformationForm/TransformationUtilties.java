package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.Enum;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Transformation;
import com.supermap.data.TransformationMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationTableDataBean;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class TransformationUtilties {

	private TransformationUtilties() {

	}

	public static String getXmlString(TransformationMode transformationMode, List<TransformationTableDataBean> beans) {
		Document emptyDocument = XmlUtilities.getEmptyDocument();
		if (emptyDocument != null) {
			Element root = emptyDocument.createElement("SuperMapTransformation");
			emptyDocument.appendChild(root);
			root.setAttribute("xmlns:sml", "http://www.supermap.com.cn/sml");
			root.setAttribute("version", "20100630");
			root.setAttribute("Description", "UGC settiong file create by SuperMap UGC 6.0");

			Element mode = emptyDocument.createElement("sml:TransformationMode");
			mode.appendChild(emptyDocument.createTextNode(String.valueOf(transformationMode.value())));
			root.appendChild(mode);
			for (TransformationTableDataBean bean : beans) {
				Element controlPoint = emptyDocument.createElement("sml:ContrlPoint");
				controlPoint.setAttribute("isEnable", String.valueOf(bean.isSelected()));
				controlPoint.setAttribute("ID", bean.getID());
				if (bean.getPointOriginal() != null) {
					Element originalPointX = emptyDocument.createElement("sml:OriginalPointX");
					originalPointX.appendChild(emptyDocument.createTextNode(DoubleUtilities.toString(bean.getPointOriginal().getX(), 10)));
					controlPoint.appendChild(originalPointX);

					Element originalPointY = emptyDocument.createElement("sml:OriginalPointY");
					originalPointY.appendChild(emptyDocument.createTextNode(DoubleUtilities.toString(bean.getPointOriginal().getY(), 10)));
					controlPoint.appendChild(originalPointY);
				}
				if (bean.getPointRefer() != null) {
					Element targetPointX = emptyDocument.createElement("sml:TargetPointX");
					targetPointX.appendChild(emptyDocument.createTextNode(DoubleUtilities.toString(bean.getPointRefer().getX(), 10)));
					controlPoint.appendChild(targetPointX);

					Element targetPointY = emptyDocument.createElement("sml:TargetPointY");
					targetPointY.appendChild(emptyDocument.createTextNode(DoubleUtilities.toString(bean.getPointRefer().getY(), 10)));
					controlPoint.appendChild(targetPointY);
				}
				root.appendChild(controlPoint);
			}
		}

		return XmlUtilities.nodeToString(emptyDocument, "UTF-8");
	}

	public static TransformationMode getTransformationMode(Document document) {
		Node root = XmlUtilities.getChildElementNodeByName(document, "SuperMapTransformation");
		Node mode = XmlUtilities.getChildElementNodeByName(root, "sml:TransformationMode");
		NodeList childNodes = mode.getChildNodes();
		Integer integer = Integer.valueOf(childNodes.item(0).getNodeValue());
		return (TransformationMode) Enum.parse(TransformationMode.class, integer);
	}


	public static List<TransformationTableDataBean> getTransformationTableDataBeans(Document document) {
		ArrayList<TransformationTableDataBean> transformationTableDataBeen = new ArrayList<>();
		Node root = XmlUtilities.getChildElementNodeByName(document, "SuperMapTransformation");
		NodeList controlNodes = root.getChildNodes();
		for (int i = 0; i < controlNodes.getLength(); i++) {
			Node item = controlNodes.item(i);
			if (item != null && item.getNodeType() == Node.ELEMENT_NODE && item.getNodeName().equalsIgnoreCase("sml:ContrlPoint")) {
				transformationTableDataBeen.add(getTransformationTableDataBean(item));
			}
		}
		return transformationTableDataBeen;
	}

	private static TransformationTableDataBean getTransformationTableDataBean(Node item) {
		TransformationTableDataBean transformationTableDataBean = new TransformationTableDataBean();
		if (item.getAttributes().getNamedItem("isEnable") != null) {
			transformationTableDataBean.setSelected(Boolean.valueOf(item.getAttributes().getNamedItem("isEnable").getNodeValue()));
		}
		if (item.getAttributes().getNamedItem("ID") != null) {
			transformationTableDataBean.setID(item.getAttributes().getNamedItem("ID").getNodeValue());
		}
		NodeList childNodes = item.getChildNodes();
		Point2D point2DOriginal = null;
		Point2D point2DRefer = null;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase("sml:OriginalPointX")) {
					if (point2DOriginal == null) {
						point2DOriginal = new Point2D();
					}
					point2DOriginal.setX(Double.valueOf(node.getChildNodes().item(0).getNodeValue()));
				} else if (node.getNodeName().equalsIgnoreCase("sml:OriginalPointY")) {
					if (point2DOriginal == null) {
						point2DOriginal = new Point2D();
					}
					point2DOriginal.setY(Double.valueOf(node.getChildNodes().item(0).getNodeValue()));
				} else if (node.getNodeName().equalsIgnoreCase("sml:TargetPointX")) {
					if (point2DRefer == null) {
						point2DRefer = new Point2D();
					}
					point2DRefer.setX(Double.valueOf(node.getChildNodes().item(0).getNodeValue()));

				} else if (node.getNodeName().equalsIgnoreCase("sml:TargetPointY")) {
					if (point2DRefer == null) {
						point2DRefer = new Point2D();
					}
					point2DRefer.setY(Double.valueOf(node.getChildNodes().item(0).getNodeValue()));
				}
			}
		}
		transformationTableDataBean.setPointOriginal(point2DOriginal);
		transformationTableDataBean.setPointRefer(point2DRefer);
		return transformationTableDataBean;
	}

	public static Transformation getTransformation(File selectedFile) {
		Document document = XmlUtilities.getDocument(selectedFile.getPath());
		if (document == null) {
			return null;
		}
		List<TransformationTableDataBean> transformationTableDataBeans = getTransformationTableDataBeans(document);
		TransformationMode transformationMode = getTransformationMode(document);
		Point2Ds target = new Point2Ds();
		Point2Ds refer = new Point2Ds();
		for (TransformationTableDataBean transformationTableDataBean : transformationTableDataBeans) {
			if (transformationTableDataBean.isSelected()) {
				if (transformationTableDataBean.getPointOriginal() == null || transformationTableDataBean.getPointRefer() == null) {
					return null;
				}
				target.add(transformationTableDataBean.getPointOriginal());
				refer.add(transformationTableDataBean.getPointRefer());
			}
		}
		int count = target.getCount();
		if (count == 0 || count == 3) {
			return null;
		} else if (count == 1) {
			transformationMode = TransformationMode.OFFSET;
		} else if (count == 2) {
			transformationMode = TransformationMode.RECT;
		} else if (count < 7) {
			transformationMode = TransformationMode.LINEAR;
		} else if (transformationMode == TransformationMode.OFFSET || transformationMode == TransformationMode.RECT) {
			return null;
		}
		Transformation transformation = new Transformation(target, refer, transformationMode);
		transformation.getError().dispose();
		return transformation;
	}

	public static Datasource getDefaultDatasource(Datasource datasource) {
		if (!datasource.isReadOnly()) {
			return datasource;
		}
		Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
		if (activeDatasources.length > 0) {
			for (Datasource activeDatasource : activeDatasources) {
				if (datasource.isOpened() && !activeDatasource.isReadOnly()) {
					return activeDatasource;
				}
			}
		}
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).isOpened() && !datasources.get(i).isReadOnly()) {
				return datasources.get(i);
			}
		}
		return null;
	}

	private static final DatasetType[] supportDatasetTypes = new DatasetType[]{
			DatasetType.POINT,
			DatasetType.LINE,
			DatasetType.REGION,
			DatasetType.POINT3D,
			DatasetType.LINE3D,
			DatasetType.REGION3D,
			DatasetType.TEXT,
			DatasetType.CAD,
			DatasetType.NETWORK,
			DatasetType.GRID,
			DatasetType.IMAGE
	};

	public static boolean isSupportDatasetType(DatasetType datasetType) {
		for (DatasetType supportDatasetType : supportDatasetTypes) {
			if (supportDatasetType == datasetType) {
				return true;
			}
		}
		return false;
	}

	public static List<Object> getMapDatasets(Map map) {
		List<Object> resultDataset = new ArrayList<>();
		int count = map.getLayers().getCount();
		for (int i = 0; i < count; i++) {
			Layer layer = map.getLayers().get(i);
			if (layer instanceof LayerGroup) {
				for (Object o : geLayerGroupDatasets((LayerGroup) layer)) {
					if (!resultDataset.contains(o)) {
						resultDataset.add(o);
					}
				}
			} else if (layer.getDataset() != null && !resultDataset.contains(layer.getDataset())) {
				resultDataset.add(layer.getDataset());
			}
		}
		return resultDataset;
	}

	private static List<Object> geLayerGroupDatasets(LayerGroup layerGroup) {
		List<Object> objects = new ArrayList<>();
		for (int i = 0; i < layerGroup.getCount(); i++) {
			Layer layer = layerGroup.get(i);
			if (layer instanceof LayerGroup) {
				List<Object> datasets = geLayerGroupDatasets(layerGroup);
				for (Object dataset : datasets) {
					if (!objects.contains(dataset)) {
						objects.add(dataset);
					}
				}
			} else if (layer.getDataset() != null && !objects.contains(layer.getDataset())) {
				objects.add(layer.getDataset());
			}
		}
		return objects;
	}
}
