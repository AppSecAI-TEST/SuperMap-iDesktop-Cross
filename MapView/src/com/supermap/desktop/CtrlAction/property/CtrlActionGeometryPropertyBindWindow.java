package com.supermap.desktop.CtrlAction.property;


import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.GeometryPropertyBindWindow.BindUtilties;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.ui.docking.*;

public class CtrlActionGeometryPropertyBindWindow extends CtrlAction {

	public CtrlActionGeometryPropertyBindWindow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}
	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			TabWindow tabWindow = ((DockbarManager) (Application.getActiveApplication().getMainFrame()).getDockbarManager()).getChildFormsWindow();
			// 获取当前活动图层对应的数据集
			Dataset dataset = formMap.getActiveLayers()[0].getDataset();
			UICommonToolkit.getLayersManager().getLayersTree().getMouseListeners();
			if (null != dataset && dataset instanceof DatasetVector) {
				Recordset recordset = ((DatasetVector) dataset).getRecordset(false, CursorType.DYNAMIC);
				BindUtilties.openTabular(dataset, recordset);
				BindUtilties.windowBindProperty(formMap, tabWindow, dataset);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean enable() {
		return null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormMap;
	}
}
