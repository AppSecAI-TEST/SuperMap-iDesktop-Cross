package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterFile;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.FILE)
public class ParameterFilePanel extends SwingPanel {
    private ParameterFile parameterFile;
    private FileChooserControl fileChooserControl = new FileChooserControl();
    private boolean isSelectingFile = false;
    private JLabel label = new JLabel();

    public ParameterFilePanel(IParameter parameterFile) {
        super(parameterFile);
        this.parameterFile = (ParameterFile) parameterFile;
        // todo fileChooseControl不好用，需要重构
        if (this.parameterFile.getSelectedItem() != null) {
            fileChooserControl.setText(this.parameterFile.getSelectedItem().toString());
        }
        label.setText(this.parameterFile.getDescribe());
        initListener();
        initLayout();
    }

    public void addChooseFileListener(ActionListener actionListener) {
        this.fileChooserControl.getButton().addActionListener(actionListener);
    }

    public void removeChooseFileListener(ActionListener actionListener) {
        this.fileChooserControl.getButton().removeActionListener(actionListener);
    }

    public FileChooserControl getFileChooserControl() {
        return fileChooserControl;
    }

    private void initListener() {
        parameterFile.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!isSelectingFile && Objects.equals(evt.getPropertyName(), AbstractParameter.PROPERTY_VALE)) {
                    fileChooserControl.setText(evt.getNewValue().toString());
                }
            }
        });
    }

    private void initLayout() {
        label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
        fileChooserControl.setPreferredSize(new Dimension(20, 23));
        panel.setLayout(new GridBagLayout());
        panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE));
        panel.add(fileChooserControl, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
    }


}
