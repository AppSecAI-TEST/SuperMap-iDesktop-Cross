package com.supermap.desktop.ui.controls;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Huchenpu
 */
public class JPanelWorkspaceSaveAsFile extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;

	/**
	 * Creates new form JPanelWorkspaceInfoSaveAsFile
	 */
	public JPanelWorkspaceSaveAsFile() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initResources() {
		jLabelFileName.setText(CoreProperties.getString("String_WorkspaceFileName"));
		jLabelPassword.setText(CoreProperties.getString("String_WorkspacePassword"));
		jLabelPasswordConfrim.setText(CoreProperties.getString("String_LabelWorkspacePassword_Confirm"));
		jLabelVersion.setText(ControlsProperties.getString("String_Label_WorkspaceVersion"));
	}

	private void initComponents() {

		jButtonBrowser = new SmButton();
		jTextFieldFileName = new JTextField();
		jTextFieldFileName.setEditable(false);
		jTextFieldFileName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkPasswordEnable();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkPasswordEnable();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				checkPasswordEnable();
			}
		});

		jLabelFileName = new JLabel();
		jPasswordFieldPassword = new JPasswordField();
		jLabelPassword = new JLabel();
		jPasswordFieldPasswordConfrim = new JPasswordField();
		jLabelPasswordConfrim = new JLabel();
		jComboBoxVersion = new JComboBox<>();
		jLabelVersion = new JLabel();

		jButtonBrowser.setText("...");
		jButtonBrowser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OpenFileAction();
			}
		});
		initResources();

		jComboBoxVersion.setModel(new DefaultComboBoxModel<>(new String[]{"SuperMap UGC 7.0", "SuperMap UGC 6.0"}));
		jComboBoxVersion.setSelectedIndex(0);

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
										GroupLayout.Alignment.TRAILING,
										layout.createSequentialGroup()
												.addGroup(
														layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jLabelFileName)
																.addComponent(jLabelPassword).addComponent(jLabelPasswordConfrim).addComponent(jLabelVersion))
												.addGap(20, 20, 20)
												.addGroup(
														layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addGroup(
																		layout.createSequentialGroup()
																				.addComponent(jTextFieldFileName, GroupLayout.DEFAULT_SIZE, 190,
																						Short.MAX_VALUE)
																				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(jButtonBrowser)).addComponent(jPasswordFieldPassword)
																.addComponent(jPasswordFieldPasswordConfrim).addComponent(jComboBoxVersion))))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jTextFieldFileName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(jButtonBrowser).addComponent(jLabelFileName))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jPasswordFieldPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabelPassword))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jPasswordFieldPasswordConfrim, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(jLabelPasswordConfrim))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jComboBoxVersion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabelVersion)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
	}// </editor-fold>

	private void checkPasswordEnable() {
		if (jTextFieldFileName.getText().toLowerCase().endsWith(".smwu")) {
			jPasswordFieldPassword.setEditable(true);
			jPasswordFieldPasswordConfrim.setEnabled(true);
		} else if (jTextFieldFileName.getText().toLowerCase().endsWith(".sxwu")) {
			jPasswordFieldPassword.setEditable(false);
			jPasswordFieldPasswordConfrim.setEnabled(false);
		}
	}

	private void OpenFileAction() {
		String moduleName = ControlsProperties.getString("String_SmFileChooseName_WorkpaceSaveAsFile");
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.bulidFileFilters(
					SmFileChoose.createFileFilter(ControlsProperties.getString("String_WorkspaceSMWUFilterName"),
							ControlsProperties.getString("String_WorkspaceSMWUFilters")),
					SmFileChoose.createFileFilter(ControlsProperties.getString("String_WorkspaceSXWUFilterName"),
							ControlsProperties.getString("String_WorkspaceSXWUFilters")));
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					ControlsProperties.getString("String_Title_WorkSpaceSaveAs"), moduleName, "SaveOne");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);

		int state = smFileChoose.showDefaultDialog();
		if (state == JFileChooser.APPROVE_OPTION) {
			jTextFieldFileName.setText(smFileChoose.getFilePath());
			fileName = smFileChoose.getFileName();
		}
	}

	public int getSelectedVersion() {
		return this.jComboBoxVersion.getSelectedIndex();
	}

	// Variables declaration - do not modify
	private SmButton jButtonBrowser;
	private JComboBox<String> jComboBoxVersion;
	private JLabel jLabelFileName;
	private JLabel jLabelPassword;
	private JLabel jLabelPasswordConfrim;
	private JLabel jLabelVersion;
	private JTextField jTextFieldFileName;
	private JPasswordField jPasswordFieldPassword;
	private JPasswordField jPasswordFieldPasswordConfrim;

	// End of variables declaration

	public JComboBox<String> getjComboBoxVersion() {
		return jComboBoxVersion;
	}

	public JTextField getjTextFieldFileName() {
		return jTextFieldFileName;
	}

	public JPasswordField getjPasswordFieldPassword() {
		return jPasswordFieldPassword;
	}


	public JPasswordField getjPasswordFieldPasswordConfrim() {
		return jPasswordFieldPasswordConfrim;
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public JButton getjButtonBrowser() {
		return jButtonBrowser;
	}

}