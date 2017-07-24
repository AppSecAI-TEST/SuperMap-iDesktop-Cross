package com.supermap.desktop.ui.controls;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 打开数据库型数据源
 *
 * @author XiaJT
 */
public class JPanelDatasourceInfoDatabase extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final int INPUT_NOT_COMPLITED = 0;
	public static final int LOAD_DATASOURCE_FAILD = 1;
	public static final int LOAD_DATASOURCE_SUCCESSFUL = 2;
	public static final int LOAD_DATASOURCE_EXCEPTION = 3;
	public static final int CREATE_DATASOURCE_SUCCESSFUL = 4;
	public static final int CREATE_DATASOURCE_FAILED = 5;

	/**
	 * Creates new form JPanelWorkspaceSaveAsSQL
	 */
	public JPanelDatasourceInfoDatabase() {
		initComponents();
		setComponentName();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	/**
	 * 公共的组件属性统一初始化
	 */
	private void initComponents() {

		jLabelServer = new JLabel();
		jLabelDatabaseName = new JLabel(ControlsProperties.getString("String_Label_DatabaseName"));
		jLabelUserName = new JLabel(ControlsProperties.getString("String_Label_UserName"));
		jLabelPassword = new JLabel(ControlsProperties.getString("String_Label_UserPassword"));
		jLabelDatasourceAlias = new JLabel(ControlsProperties.getString("String_Label_DatasourseAlias"));
		jLabelOpenType = new JLabel(ControlsProperties.getString("String_Label_OpenType"));
		jComboBoxServer = new JComboBox<String>();
		jTextFieldDatabaseName = new JTextField("");
		jTextFieldUserName = new JTextField("");
		jPasswordFieldPassword = new JPasswordField("");
		jTextFieldDatasourceAlias = new JTextField("");
		jCheckBoxReadonly = new JCheckBox(CoreProperties.getString("String_ReadOnly"));
		jComboBoxServer.setEditable(true);

		jLabelEmpty = new JLabel();
		jLabelEmptyAlias = new JLabel("!");
		jLabelEmptyServer = new JLabel("!");
		jLabelEmptyUser = new JLabel("!");

		jLabelEmptyAlias.setToolTipText(ControlsProperties.getString("String_ToolTipText_AliasShouldNotEmpty"));
		jLabelEmptyUser.setToolTipText(ControlsProperties.getString("String_ToolTipText_UserNameShouldNotEmpty"));

		jLabelEmptyAlias.setVisible(false);
		jLabelEmptyServer.setVisible(false);
		jLabelEmptyUser.setVisible(false);
		jComboBoxServer.getEditor().getEditorComponent().addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				jComboBoxServerChange();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// 默认实现，后续进行初始化操作
			}
		});

		jComboBoxServer.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				jTextFieldUserNameKeyEvent();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				jTextFieldUserNameKeyEvent();

			}

			@Override
			public void keyPressed(KeyEvent e) {
				jTextFieldUserNameKeyEvent();
			}
		});
		jComboBoxServer.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				jComboBoxServerChange();
			}
		});

		jTextFieldUserName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// do nothing
			}

			@Override
			public void keyReleased(KeyEvent e) {
				jTextFieldUserNameChange();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// do nothing
			}
		});
		jTextFieldUserName.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				jTextFieldUserNameChange();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// 默认实现，后续进行初始化操作
			}
		});

		jTextFieldDatasourceAlias.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// do nothing
			}

			@Override
			public void keyReleased(KeyEvent e) {
				jTextFieldDatasourceAliasChange();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// do nothing
			}
		});
		jTextFieldDatasourceAlias.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				jTextFieldDatasourceAliasChange();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// 默认实现，后续进行操作
			}
		});

		jTextFieldDatabaseName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				jTextFieldUserNameKeyEvent();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				jTextFieldUserNameKeyEvent();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				jTextFieldUserNameKeyEvent();
			}
		});
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
														layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jLabelServer)
																.addComponent(jLabelDatabaseName).addComponent(jLabelUserName).addComponent(jLabelPassword)
																.addComponent(jLabelDatasourceAlias).addComponent(jLabelOpenType))
												.addGap(20, 20, 20)
												.addGroup(
														layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jLabelEmptyServer)
																.addComponent(jLabelEmpty, 10, 10, 10).addComponent(jLabelEmptyUser)
																.addComponent(jLabelEmptyAlias))
												.addGroup(
														layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jComboBoxServer)
																.addComponent(jTextFieldDatabaseName).addComponent(jTextFieldUserName)
																.addComponent(jPasswordFieldPassword).addComponent(jTextFieldDatasourceAlias)
																.addComponent(jCheckBoxReadonly)))).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jLabelServer).addComponent(jLabelEmptyServer)
										.addComponent(jComboBoxServer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jTextFieldDatabaseName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabelDatabaseName))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jTextFieldUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabelEmptyUser).addComponent(jLabelUserName))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jPasswordFieldPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabelEmpty).addComponent(jLabelPassword))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jTextFieldDatasourceAlias, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(jLabelEmptyAlias).addComponent(jLabelDatasourceAlias))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jCheckBoxReadonly, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabelOpenType)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
	}// </editor-fold>

	// Variables declaration - do not modify
	private JComboBox<String> jComboBoxServer;
	private JLabel jLabelDatabaseName;
	private JLabel jLabelPassword;
	private JLabel jLabelServer;
	private JLabel jLabelUserName;
	private JLabel jLabelDatasourceAlias;
	private JLabel jLabelOpenType;
	private JTextField jTextFieldDatabaseName;
	private JPasswordField jPasswordFieldPassword;
	private JTextField jTextFieldUserName;
	private JTextField jTextFieldDatasourceAlias;
	private JCheckBox jCheckBoxReadonly;
	private String datasourceAlias;
	private transient EngineType engineType;

	private JLabel jLabelEmptyServer;
	// 撑开表格
	private JLabel jLabelEmpty;
	private JLabel jLabelEmptyUser;
	private JLabel jLabelEmptyAlias;
	private int connectionFlag;

	// End Variables
	private void setComponentName() {
		ComponentUIUtilities.setName(this.jComboBoxServer, "JPanelDatasourceInfoDatabase_jComboBoxServer");
		ComponentUIUtilities.setName(this.jLabelDatabaseName, "JPanelDatasourceInfoDatabase_jLabelDatabaseName");
		ComponentUIUtilities.setName(this.jLabelPassword, "JPanelDatasourceInfoDatabase_jLabelPassword");
		ComponentUIUtilities.setName(this.jLabelServer, "JPanelDatasourceInfoDatabase_jLabelServer");
		ComponentUIUtilities.setName(this.jLabelUserName, "JPanelDatasourceInfoDatabase_jLabelUserName");
		ComponentUIUtilities.setName(this.jLabelDatasourceAlias, "JPanelDatasourceInfoDatabase_jLabelDatasourceAlias");
		ComponentUIUtilities.setName(this.jLabelOpenType, "JPanelDatasourceInfoDatabase_jLabelOpenType");
		ComponentUIUtilities.setName(this.jTextFieldDatabaseName, "JPanelDatasourceInfoDatabase_jTextFieldDatabaseName");
		ComponentUIUtilities.setName(this.jPasswordFieldPassword, "JPanelDatasourceInfoDatabase_jPasswordFieldPassword");
		ComponentUIUtilities.setName(this.jTextFieldUserName, "JPanelDatasourceInfoDatabase_jTextFieldUserName");
		ComponentUIUtilities.setName(this.jTextFieldDatasourceAlias, "JPanelDatasourceInfoDatabase_jTextFieldDatasourceAlias");
		ComponentUIUtilities.setName(this.jCheckBoxReadonly, "JPanelDatasourceInfoDatabase_jCheckBoxReadonly");
		ComponentUIUtilities.setName(this.jLabelEmptyServer, "JPanelDatasourceInfoDatabase_jLabelEmptyServer");
		ComponentUIUtilities.setName(this.jLabelEmpty, "JPanelDatasourceInfoDatabase_jLabelEmpty");
		ComponentUIUtilities.setName(this.jLabelEmptyUser, "JPanelDatasourceInfoDatabase_jLabelEmptyUser");
		ComponentUIUtilities.setName(this.jLabelEmptyAlias, "JPanelDatasourceInfoDatabase_jLabelEmptyAlias");
	}
	/**
	 * 根据引擎类型设置jTextFieldDatasourceAlias
	 *
	 * @param engineType
	 */
	private void setjTextFieldDatasourceAlias(EngineType engineType) {

		datasourceAlias = engineType.name();
		jTextFieldDatasourceAlias.setText(datasourceAlias);
	}

	/**
	 * 设置引擎类型时修改组件信息
	 *
	 * @param engineType 引擎类型
	 */
	public void setDatasourceType(EngineType engineType) {
		this.engineType = engineType;

		jLabelEmptyAlias.setVisible(false);
		jLabelEmptyServer.setVisible(false);
		jLabelEmptyUser.setVisible(false);

		try {
			if (engineType == EngineType.ORACLEPLUS || engineType == EngineType.ORACLESPATIAL || engineType == EngineType.DB2) {
				jLabelServer.setText(ControlsProperties.getString("String_Label_InstanceName"));
				jLabelEmptyServer.setToolTipText(ControlsProperties.getString("String_ToolTipText_InstanceShouldNotEmpty"));
			} else {
				jLabelServer.setText(ControlsProperties.getString("String_Label_ServersName"));
				jLabelEmptyServer.setToolTipText(ControlsProperties.getString("String_ToolTipText_ServersNameShouldNotEmpty"));
			}
			jTextFieldDatabaseName.setText(null);
			jTextFieldUserName.setText(null);
			jPasswordFieldPassword.setText(null);
			setjTextFieldDatasourceAlias(engineType);
			jComboBoxServer.removeAllItems();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private DatasourceConnectionInfo getDatasourceConnectionInfo(int connFlag) {
		DatasourceConnectionInfo connInfo = new DatasourceConnectionInfo();
		setConnectionFlag(connFlag);
		try {
			String jComboBoxServerValue = (String) jComboBoxServer.getSelectedItem();
			if (null == jComboBoxServerValue || jComboBoxServerValue.length() <= 0) {
				setConnectionFlag(INPUT_NOT_COMPLITED);
			}
			String jTextFieldUserNameValue = jTextFieldUserName.getText();
			if (null == jTextFieldUserNameValue || jTextFieldUserNameValue.length() <= 0) {
				setConnectionFlag(INPUT_NOT_COMPLITED);
			}
			String jTextFieldDatasourceAliasValue = jTextFieldDatasourceAlias.getText();
			if (null == jTextFieldDatasourceAliasValue || jTextFieldDatasourceAliasValue.length() <= 0) {
				setConnectionFlag(INPUT_NOT_COMPLITED);
			}

			Datasources a = Application.getActiveApplication().getWorkspace().getDatasources();
			List<String> nameList = new ArrayList<String>();
			int i = 0;
			int dataSoursesLength = a.getCount();
			for (i = 0; i < dataSoursesLength; i++) {
				nameList.add(a.get(i).getAlias());
			}
			boolean isNoSameName = false;
			while (!isNoSameName) {
				isNoSameName = true;
				for (i = 0; i < dataSoursesLength; i++) {
					if (jTextFieldDatasourceAliasValue.equals(nameList.get(i))) {
						isNoSameName = false;
						jTextFieldDatasourceAliasValue += "_1";
					}
				}
			}

			String jTextFieldDatabaseValue = jTextFieldDatabaseName.getText();
			String jPasswordFieldPasswordValue = String.valueOf(jPasswordFieldPassword.getPassword());
			connInfo.setEngineType(engineType);
			connInfo.setServer(jComboBoxServerValue);
			connInfo.setDatabase(jTextFieldDatabaseValue);
			connInfo.setUser(jTextFieldUserNameValue);
			connInfo.setPassword(jPasswordFieldPasswordValue);
			connInfo.setReadOnly(jCheckBoxReadonly.isSelected());
			connInfo.setAlias(jTextFieldDatasourceAliasValue);
			if (EngineType.SQLPLUS == engineType) {
				connInfo.setDriver("SQL SERVER");
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return connInfo;
	}

	/**
	 * 加载数据源到工作空间
	 *
	 * @return
	 */
	public int loadDatasource() {
		int connFlag = -1;
		DatasourceConnectionInfo connInfo = getDatasourceConnectionInfo(connFlag);
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		boolean openFlag = false;
		for (int i = 0; i < datasources.getCount(); i++) {
			Datasource tempDatasource = datasources.get(i);
			if (connInfo == tempDatasource.getConnectionInfo()) {
				openFlag = true;
			}
		}
		if (!openFlag) {
			try {
				String resultInfo;
				// 进度条实现
				Datasource ds = datasources.open(connInfo);
				if (ds == null) {
					resultInfo = ControlsProperties.getString("String_OpenDatasourceFaild");
					connFlag = LOAD_DATASOURCE_FAILD;
				} else {
					resultInfo = ControlsProperties.getString("String_OpenDatasourceSuccessful");
					connFlag = LOAD_DATASOURCE_SUCCESSFUL;
					UICommonToolkit.refreshSelectedDatasourceNode(ds.getAlias());
				}
				Application.getActiveApplication().getOutput().output(resultInfo);
			} catch (Exception e) {
				String message = ControlsProperties.getString("String_OpenDatasetDatasourceFaild");
				Application.getActiveApplication().getOutput().output(message);
			}
		}

		return connFlag;
	}

	public int createDatasource() {
		int connFlag = -1;
		try {
			DatasourceConnectionInfo connectionInfo = getDatasourceConnectionInfo(connFlag);
			String resultInfo = "";
			Datasource newDatasource = Application.getActiveApplication().getWorkspace().getDatasources().create(connectionInfo);
			if (null == newDatasource) {
				resultInfo = "Create Datasourse Failed";
				connFlag = CREATE_DATASOURCE_FAILED;
			} else {
				resultInfo = "Create Datasource Success";
				connFlag = CREATE_DATASOURCE_SUCCESSFUL;
				UICommonToolkit.refreshSelectedDatasourceNode(newDatasource.getAlias());
			}
			Application.getActiveApplication().getOutput().output(resultInfo);
		} catch (Exception ex) {
			connFlag = LOAD_DATASOURCE_EXCEPTION;
			Application.getActiveApplication().getOutput().output(ex);
		}
		return connFlag;
	}

	/**
	 * jTextFieldDatabaseName键盘监听器，在内容改变时更改jTextFieldDatasourceAlias里面的内容
	 */
	private void jTextFieldUserNameKeyEvent() {
		String databaseName = jTextFieldDatabaseName.getText();
		String databaseSever = ((JTextField) jComboBoxServer.getEditor().getEditorComponent()).getText();
		if (null == databaseName || databaseName.length() <= 0) {
			jTextFieldDatasourceAlias.setText(databaseSever);
		} else {
			jTextFieldDatasourceAlias.setText(databaseSever + "_" + databaseName);
		}
	}

	private void jTextFieldUserNameChange() {
		String userName = jTextFieldUserName.getText();
		if (null == userName || userName.length() <= 0) {
			// 默认实现，后续进行初始化操作
		} else {
			jLabelEmptyUser.setVisible(false);
		}
	}

	private void jTextFieldDatasourceAliasChange() {
		String alias = jTextFieldDatasourceAlias.getText();
		if (null == alias || alias.length() <= 0) {
			// do nothing

		} else {
			jLabelEmptyAlias.setVisible(false);
		}
	}

	private void jComboBoxServerChange() {
		String server = ((JTextField) jComboBoxServer.getEditor().getEditorComponent()).getText();
		if (null == server || server.length() <= 0) {
			// 默认实现，后续进行初始化操作
		} else {
			jLabelEmptyServer.setVisible(false);
		}
	}

	public int getConnectionFlag() {
		return connectionFlag;
	}

	public void setConnectionFlag(int connectionFlag) {
		this.connectionFlag = connectionFlag;
	}

}
