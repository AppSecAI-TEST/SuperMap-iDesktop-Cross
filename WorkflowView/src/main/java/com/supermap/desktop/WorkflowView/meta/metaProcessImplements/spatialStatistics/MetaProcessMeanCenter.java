package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.SpatialMeasure;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;

/**
 * @author XiaJT
 */
public class MetaProcessMeanCenter extends MetaProcessSpatialMeasure {

	public MetaProcessMeanCenter() {
		super();
	}

	protected void initHook() {
		OUTPUT_DATASET = "MeanCenterResult";
		resultName = "result_meanCenter";
	}

	@Override
	protected boolean doWork(DatasetVector datasetVector) {
		boolean isSuccessful = false;

		try {
			SpatialMeasure.addSteppedListener(steppedListener);
			// 调用平均中心方法，并获取结果矢量数据集
			DatasetVector result = SpatialMeasure.measureMeanCenter(
					datasetVector,
					parameterSaveDataset.getResultDatasource(),
					parameterSaveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(parameterSaveDataset.getDatasetName()),
					measureParameter.getMeasureParameter());
			this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(result);
			isSuccessful = result != null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			SpatialMeasure.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_MeanCenter");
	}

	@Override
	public String getKey() {
		return MetaKeys.MEAN_CENTER;
	}
}
