package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilities.JComboBoxUIUtilities;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalListener;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterFieldComboBox;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.ArrayUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.FIELD_COMBO_BOX)
public class ParameterFieldComboBoxPanel extends SwingPanel implements IParameterPanel {
	private ParameterFieldComboBox parameterFieldComboBox;
	private boolean isSelectingItem = false;
	private JLabel label = new JLabel();
	private JComboBox<FieldInfo> comboBox = new JComboBox<>();

	public ParameterFieldComboBoxPanel(IParameter parameter) {
		super(parameter);
		this.parameterFieldComboBox = (ParameterFieldComboBox) parameter;
		comboBox.setName("ParameterFieldComboBoxPanel_" + parameter.getDescribe());
		parameterFieldComboBox.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterFieldComboBox.DATASET_FIELD_NAME)) {

					DatasetVector newValue = (DatasetVector) evt.getNewValue();
					if (newValue != null) {
						resetComboBoxItems(newValue);
					} else {
						isSelectingItem = true;
						comboBox.removeAllItems();
						parameterFieldComboBox.setSelectedItem(null);
						isSelectingItem = false;
					}
				}
			}
		});
		initComponentState();
		initLayout();
		initComponentListener();
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		comboBox.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		panel.add(comboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
	}

	private void initComponentListener() {
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && (e.getStateChange() == ItemEvent.SELECTED || comboBox.getSelectedItem() == null)) {
					isSelectingItem = true;
					if (comboBox.getSelectedItem() instanceof FieldInfo) {
						comboBox.setSelectedItem(((FieldInfo) comboBox.getSelectedItem()).getName());
					}
					parameterFieldComboBox.setSelectedItem(comboBox.getSelectedItem());
					isSelectingItem = false;
				}
			}
		});
	}

	private void initComponentState() {

		comboBox.setEditable(parameterFieldComboBox.isEditable());
		comboBox.setRenderer(new ListCellRenderer<FieldInfo>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends FieldInfo> list, FieldInfo value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel jLabel = new JLabel();
				if (value != null) {
					jLabel.setText(value.getName());// 缺陷太多，先改回name
				} else {
					jLabel.setText(" ");
				}
				jLabel.setOpaque(true);
				if (isSelected) {
					jLabel.setBackground(list.getSelectionBackground());
					jLabel.setForeground(list.getSelectionForeground());
				} else {
					jLabel.setBackground(list.getBackground());
					jLabel.setForeground(list.getForeground());
				}
				return jLabel;
			}
		});
		String describe = parameterFieldComboBox.getDescribe();
		if (describe != null) {
			label.setText(describe);
			label.setText(getDescribe());
		}
		DatasetVector dataset = parameterFieldComboBox.getDataset();
		resetComboBoxItems(dataset);
	}

	@Override
	public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
		if (event.getFieldName().equals(ParameterFieldComboBox.DATASET_FIELD_NAME)) {
			resetComboBoxItems(parameterFieldComboBox.getDataset());
		}
	}

	private void resetComboBoxItems(DatasetVector dataset) {
		isSelectingItem = true;
		comboBox.removeAllItems();
		FieldType[] fieldTypes = parameterFieldComboBox.getFieldTypes();
		if (dataset != null) {
			FieldInfos fieldInfos;
			try {
				fieldInfos = dataset.getFieldInfos();
			} catch (Exception e) {
				parameterFieldComboBox.setSelectedItem(null);
				return;
				//ignore
			}
			if (parameterFieldComboBox.isShowNullValue()) {
				comboBox.addItem(null);
			}
			for (int i = 0; i < fieldInfos.getCount(); i++) {
				FieldInfo fieldInfo = fieldInfos.get(i);
				if ((fieldTypes == null || ArrayUtilities.isArrayContains(fieldTypes, fieldInfo.getType()))
						&& parameterFieldComboBox.isValueLegal(ParameterFieldComboBox.FILED_INFO_FILED_NAME, fieldInfos.get(i))) {
					if (!fieldInfos.get(i).isSystemField() || parameterFieldComboBox.isShowSystemField()) {
						comboBox.addItem(fieldInfos.get(i));
					}
				}
			}
			comboBox.setSelectedItem(null);
			if (comboBox.getItemCount() > 0) {
				// 先询问参数是否满意当前选项
				// Ask if the parameters are satisfied with the current option
				for (int i = 0; i < comboBox.getItemCount(); i++) {
					Object valueSelected = parameterFieldComboBox.isValueSelected(ParameterFieldComboBox.FILED_INFO_FILED_NAME, fieldInfos.get(i));
					if (valueSelected == ParameterValueLegalListener.DO_NOT_CARE) {
						break;
					} else if (valueSelected instanceof FieldInfo) {
						if (JComboBoxUIUtilities.getItemIndex(comboBox, valueSelected) != -1) {
							isSelectingItem = false;
							try {
								comboBox.setSelectedItem(valueSelected);
							} catch (Exception e) {
								isSelectingItem = true;
								Application.getActiveApplication().getOutput().output(e);
							}
							isSelectingItem = true;
						}
						break;
					} else if (valueSelected == ParameterValueLegalListener.NO) {
						// 注意条件
					}
				}
				if (comboBox.getSelectedItem() == null) {
					// 如果没有满意的选项则与当前已设置的值保持一致
					// If there is no satisfactory option, it is consistent with the current set value

					String fieldName = parameterFieldComboBox.getFieldName();
					for (int i = 0; i < comboBox.getItemCount(); i++) {
						if (comboBox.getItemAt(i) == null) {
							if (StringUtilities.isNullOrEmpty(fieldName)) {
								comboBox.setSelectedIndex(i);
								break;
							}
							continue;
						}
						if (comboBox.getItemAt(i).getCaption().equals(fieldName)) {
							comboBox.setSelectedItem(fieldName);
							break;
						} else if (StringUtilities.isNullOrEmpty(fieldName)) {
							comboBox.setSelectedItem(comboBox.getItemAt(i).getCaption());
							parameterFieldComboBox.setSelectedItem(comboBox.getItemAt(i));
							break;
						}
					}
				}
			} else {
				parameterFieldComboBox.setSelectedItem(null);
			}
		} else {
			parameterFieldComboBox.setSelectedItem(null);
		}
		isSelectingItem = false;
	}

	@Override
	protected void descriptionVisibleChanged(boolean newValue) {
		label.setVisible(newValue);
	}

	/**
	 * @return
	 */
	private String getDescribe() {
		String describe = parameterFieldComboBox.getDescribe();
		if (parameterFieldComboBox.isRequisite()) {
			return MessageFormat.format(CommonProperties.getString("String_IsRequiredLable"), describe);
		} else {
			return describe;
		}
	}
}
