package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.ui.controls.SmFileChoose;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterFile extends AbstractParameter implements ISelectionParameter {

    @ParameterField(name = "value")
    private String selectedPath;
    private String describe;
    private SmFileChoose fileChoose;

    public ParameterFile(String describe) {
        this.describe = describe;
    }

    public ParameterFile() {
        this("");
    }

    @Override
    public String getType() {
        return ParameterType.FILE;
    }


    @Override
    public void setSelectedItem(Object value) {
        String oldValue = this.selectedPath;
        this.selectedPath = (String) value;
        firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, selectedPath));
    }

    @Override
    public Object getSelectedItem() {
        return selectedPath;
    }

    public String getDescribe() {
        return describe;
    }

    public ParameterFile setDescribe(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public void dispose() {

    }

    public SmFileChoose getFileChoose() {
        return fileChoose;
    }

    public void setFileChoose(SmFileChoose fileChoose) {
        this.fileChoose = fileChoose;
    }

}
