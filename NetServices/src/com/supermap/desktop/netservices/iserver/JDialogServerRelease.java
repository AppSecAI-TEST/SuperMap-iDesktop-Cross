package com.supermap.desktop.netservices.iserver;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.supermap.data.DatasetType;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilties.CursorUtilties;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.StringUtilties;

public class JDialogServerRelease extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String LOCALHOST = "localhost";

	private JRadioButton radioButtonLocalHost;
	private JRadioButton radioButtonRemoteHost;
	private JLabel labelServer;
	private JTextField textFieldHost;
	private JLabel labelColon;
	private JTextField textFieldPort;

	private JLabel labelUserName;
	private JLabel labelPassword;
	private JTextField textFieldUserName;
	private JTextField textFieldPassword;

	private JCheckBox checkBoxRestData;
	private JCheckBox checkBoxRestRealspace;
	private JCheckBox checkBoxRestMap;
	private JCheckBox checkBoxRestTransAnalyst;
	private JCheckBox checkBoxRestSpatialAnalyst;

	private JCheckBox checkBoxWCS111;
	private JCheckBox checkBoxWMS111;
	private JCheckBox checkBoxWCS112;
	private JCheckBox checkBoxWMS130;
	private JCheckBox checkBoxWFS100;
	private JCheckBox checkBoxWMTS100;
	private JCheckBox checkBoxWPS100;
	private JCheckBox checkBoxWMTSCHINA;
	private JCheckBox checkBoxIsEditable;

	private JButton buttonRelease;
	private JButton buttonClose;

	private WorkspaceInfo workspaceInfo;
	private int servicesType;
	private boolean isEditable;
	private String remoteHost;
	private String port;
	private String adminName;
	private String adminPassword;
	private int hostType;
	// 能否发布：目前，只有文件型工作空间+文件型数据源发布到远程服务器，并且它们不在同一个目录下时，为false
	private boolean canRelease;
	private FormProgressTotal formProgress;

	private DocumentListener textFieldHostDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldHostChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldHostChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldHostChange();
		}
	};

	private DocumentListener textFieldPortDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldPortChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldPortChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldPortChange();
		}
	};

	private DocumentListener textFieldUserNameDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldUserNameChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldUserNameChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldUserNameChange();
		}
	};

	private DocumentListener textFieldPasswordDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldPasswordChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldPasswordChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldPasswordChange();
		}
	};

	/**
	 * Create the dialog.
	 */
	public JDialogServerRelease(WorkspaceInfo workspaceInfo) {
		initializeComponents();
		initializeResources();

		this.workspaceInfo = workspaceInfo;
		this.canRelease = true;

		initializeParameters();
		initializeControls();
		registerEvents();
		setButtonReleaseEnabled();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.radioButtonLocalHost) {
			radioButtonLocalHostSelectedChange();
		} else if (e.getSource() == this.radioButtonRemoteHost) {
			radioButtonRemoteHostSelectedChange();
		} else if (e.getSource() == this.buttonRelease) {
			buttonReleaseClicked();
		} else if (e.getSource() == this.buttonClose) {
			buttonCloseClicked();
		} else if (e.getSource() == this.checkBoxWPS100) {
			checkBoxWPS100CheckChange();
		} else if (e.getSource() == this.checkBoxWMTS100) {
			checkBoxWMTS100CheckChange();
		} else if (e.getSource() == this.checkBoxWMTSCHINA) {
			checkBoxWMTSCHINACheckChange();
		} else if (e.getSource() == this.checkBoxWMS130) {
			checkBoxWMS130CheckChange();
		} else if (e.getSource() == this.checkBoxWMS111) {
			checkBoxWMS111CheckChange();
		} else if (e.getSource() == this.checkBoxWFS100) {
			checkBoxWFS100CheckChange();
		} else if (e.getSource() == this.checkBoxWCS112) {
			checkBoxWCS112CheckChange();
		} else if (e.getSource() == this.checkBoxWCS111) {
			checkBoxWCS111CheckChange();
		} else if (e.getSource() == this.checkBoxRestData) {
			checkBoxRestDataCheckChange();
		} else if (e.getSource() == this.checkBoxIsEditable) {
			checkBoxIsEditableCheckChange();
		} else if (e.getSource() == this.checkBoxRestMap) {
			checkBoxRestMapCheckChange();
		} else if (e.getSource() == this.checkBoxRestRealspace) {
			checkBoxRestRealspaceCheckChange();
		} else if (e.getSource() == this.checkBoxRestSpatialAnalyst) {
			checkBoxRestSpatialAnalystCheckChange();
		} else if (e.getSource() == this.checkBoxRestTransAnalyst) {
			checkBoxRestTransAnalystCheckChange();
		}
	}

	private void initializeComponents() {

		// 服务器设置面板
		JPanel panelService = new JPanel();
		panelService.setBorder(BorderFactory.createTitledBorder("Service"));
		this.radioButtonLocalHost = new JRadioButton("LocalHost");
		this.radioButtonRemoteHost = new JRadioButton("RemoteHost");
		this.labelServer = new JLabel("Server:");
		this.textFieldHost = new JTextField("localhost");
		this.labelColon = new JLabel(":");
		this.textFieldPort = new JTextField("8090");
		this.textFieldPort.setEditable(false);
		this.textFieldPort.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);

		GridBagLayout gbl_panelService = new GridBagLayout();
		panelService.setLayout(gbl_panelService);

		panelService.add(this.radioButtonLocalHost, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,
				10, 0, 0), 0, 0));
		panelService.add(this.radioButtonRemoteHost, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,
				5, 0, 0), 0, 0));
		panelService.add(this.labelServer, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 10, 0),
				0, 0));
		panelService.add(this.textFieldHost, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 10, 0), 0, 0));
		panelService.add(this.labelColon, new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 0, 10, 0),
				0, 0));
		panelService.add(this.textFieldPort, new GridBagConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 10, 10), 0, 0));

		// 用户设置面板
		JPanel panelUser = new JPanel();
		panelUser.setBorder(BorderFactory.createTitledBorder("User"));
		this.labelUserName = new JLabel("UserName:");
		this.textFieldUserName = new JTextField();
		this.textFieldUserName.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		this.labelPassword = new JLabel("Password:");
		this.textFieldPassword = new JTextField();
		this.textFieldPassword.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);

		GridBagLayout gbl_panelUser = new GridBagLayout();
		panelUser.setLayout(gbl_panelUser);
		panelUser.add(this.labelUserName, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0),
				0, 0));
		panelUser.add(this.textFieldUserName, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 5, 0,
				10), 0, 0));
		panelUser.add(this.labelPassword, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 10, 0),
				0, 0));
		panelUser.add(this.textFieldPassword, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 10,
				10), 0, 0));

		// Rest服务类型设置面板
		JPanel panelRestService = new JPanel();
		panelRestService.setBorder(BorderFactory.createTitledBorder("panelRestService"));
		this.checkBoxRestData = new JCheckBox("RestData");
		this.checkBoxRestRealspace = new JCheckBox("Realspace");
		this.checkBoxRestMap = new JCheckBox("RestMap");
		this.checkBoxRestTransAnalyst = new JCheckBox("RestTransAnalyst");
		this.checkBoxRestSpatialAnalyst = new JCheckBox("RestSpatialAnalyst");

		GridBagLayout gbl_panelRestService = new GridBagLayout();
		panelRestService.setLayout(gbl_panelRestService);
		panelRestService.add(this.checkBoxRestData, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,
				10, 0, 0), 0, 0));
		panelRestService.add(this.checkBoxRestRealspace, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
				10, 5, 0, 10), 0, 0));
		panelRestService.add(this.checkBoxRestMap, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10,
				0, 0), 0, 0));
		panelRestService.add(this.checkBoxRestTransAnalyst, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 0, 10), 0, 0));
		panelRestService.add(this.checkBoxRestSpatialAnalyst, new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(5, 10, 10, 0), 0, 0));

		// OGC服务类型设置面板
		JPanel panelOGCService = new JPanel();
		panelOGCService.setBorder(BorderFactory.createTitledBorder("OGCService"));
		this.checkBoxWCS111 = new JCheckBox("WCS1.1.1");
		this.checkBoxWMS111 = new JCheckBox("WMS1.1.1");
		this.checkBoxWCS112 = new JCheckBox("WCS1.1.2");
		this.checkBoxWMS130 = new JCheckBox("WMS1.3.0");
		this.checkBoxWFS100 = new JCheckBox("WFS1.0.0");
		this.checkBoxWMTS100 = new JCheckBox("WMTS1.0.0");
		this.checkBoxWPS100 = new JCheckBox("WPS1.0.0");
		this.checkBoxWMTSCHINA = new JCheckBox("WMTS-CHINA");

		GridBagLayout gbl_panelOGCService = new GridBagLayout();
		panelOGCService.setLayout(gbl_panelOGCService);
		panelOGCService.add(this.checkBoxWCS111, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10,
				0, 0), 0, 0));
		panelOGCService.add(this.checkBoxWMS111, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 5,
				0, 10), 0, 0));
		panelOGCService.add(this.checkBoxWCS112, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10,
				0, 0), 0, 0));
		panelOGCService.add(this.checkBoxWMS130, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0,
				10), 0, 0));
		panelOGCService.add(this.checkBoxWFS100, new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10,
				0, 0), 0, 0));
		panelOGCService.add(this.checkBoxWMTS100, new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5,
				0, 10), 0, 0));
		panelOGCService.add(this.checkBoxWPS100, new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10,
				0, 0), 0, 0));
		panelOGCService.add(this.checkBoxWMTSCHINA, new GridBagConstraints(1, 3, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5,
				10, 10), 0, 0));

		// 附加设置面板
		JPanel panelAdditionalSetting = new JPanel();
		panelAdditionalSetting.setBorder(BorderFactory.createTitledBorder("AdditionalSetting"));
		this.checkBoxIsEditable = new JCheckBox("IsEditable");

		GridBagLayout gbl_panelAdditionalSetting = new GridBagLayout();
		panelAdditionalSetting.setLayout(gbl_panelAdditionalSetting);
		panelAdditionalSetting.add(this.checkBoxIsEditable, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(10, 10, 10, 10), 0, 0));

		// 主面板
		JPanel panelMain = new JPanel();
		setContentPane(panelMain);
		this.buttonRelease = new JButton("Release");
		this.buttonClose = new JButton("Close");

		GridBagLayout gbl_panelMain = new GridBagLayout();
		setLayout(gbl_panelMain);
		panelMain
				.add(panelService, new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 10), 0, 0));
		panelMain.add(panelUser, new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0, 10), 0, 0));
		panelMain.add(panelRestService, new GridBagConstraints(0, 2, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0, 10), 0,
				0));
		panelMain.add(panelOGCService, new GridBagConstraints(0, 3, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0, 10), 0,
				0));
		panelMain.add(panelAdditionalSetting, new GridBagConstraints(0, 4, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0,
				10), 0, 0));
		panelMain.add(this.buttonRelease, new GridBagConstraints(0, 5, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 10, 0),
				0, 0));
		panelMain.add(this.buttonClose, new GridBagConstraints(1, 5, 1, 1, 0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 10, 10), 0,
				0));
	}

	private void initializeResources() {

	}

	private void registerEvents() {
		this.radioButtonLocalHost.addActionListener(this);
		this.radioButtonRemoteHost.addActionListener(this);
		this.textFieldHost.getDocument().addDocumentListener(this.textFieldHostDocumentListener);
		this.textFieldPort.getDocument().addDocumentListener(this.textFieldPortDocumentListener);
		this.textFieldUserName.getDocument().addDocumentListener(this.textFieldUserNameDocumentListener);
		this.textFieldPassword.getDocument().addDocumentListener(this.textFieldPasswordDocumentListener);
		this.checkBoxRestData.addActionListener(this);
		this.checkBoxRestRealspace.addActionListener(this);
		this.checkBoxRestMap.addActionListener(this);
		this.checkBoxRestTransAnalyst.addActionListener(this);
		this.checkBoxRestSpatialAnalyst.addActionListener(this);
		this.checkBoxWCS111.addActionListener(this);
		this.checkBoxWMS111.addActionListener(this);
		this.checkBoxWCS112.addActionListener(this);
		this.checkBoxWMS130.addActionListener(this);
		this.checkBoxWFS100.addActionListener(this);
		this.checkBoxWMTS100.addActionListener(this);
		this.checkBoxWPS100.addActionListener(this);
		this.checkBoxWMTSCHINA.addActionListener(this);
		this.checkBoxIsEditable.addActionListener(this);
		this.buttonRelease.addActionListener(this);
		this.buttonClose.addActionListener(this);
	}

	// 发布文件型工作空间到远程服务器还没有API
	// 因此先处理为文件型的时候，远程选项不可能
	// 文件型工作空间+文件型数据源，需要工作空间和数据源在同一个目录下，打包上传
	// 文件型工作空间+数据库型数据源，只需要上传文件型工作空间
	private boolean canRemoteRelease() {
		Boolean canRemoteRelease = true;
		// try
		// {
		// if (!StringUtilties.isNullOrEmpty(this.workspaceInfo.getWorkspacePath()))
		// {
		// DirectoryInfo workspaceDirectory = Directory.GetParent(this.m_workspaceInfo.WorkspacePath);
		// foreach (String datasourcePath in this.m_workspaceInfo.DatasourcesPath)
		// {
		// DirectoryInfo datasourceDirectory = Directory.GetParent(datasourcePath);
		// if (workspaceDirectory.FullName.ToLower() != datasourceDirectory.FullName)
		// {
		// canRemoteRelease = false;
		// break;
		// }
		// }
		// }
		// }
		// catch (Exception ex)
		// {
		// Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
		// }

		return canRemoteRelease;
	}

	private boolean isFileWorkspace(WorkspaceType type) {
		return (type == WorkspaceType.SMW || type == WorkspaceType.SMWU || type == WorkspaceType.SXW || type == WorkspaceType.SXWU || type == WorkspaceType.DEFAULT);
	}

	// 根据工作空间的数据类型默认发布超图规范的REST服务
	// OGC服务默认不勾选
	private void initializeParameters() {
		try {
			this.isEditable = false;
			this.port = "8090";
			this.remoteHost = "";
			this.adminName = "";
			this.adminPassword = "";
			this.hostType = HostType.LOCAL;

			this.servicesType = ServiceType.NONE;
			if (this.workspaceInfo != null) {
				if (this.workspaceInfo.getContainTypes().size() > 0) {
					this.servicesType = this.servicesType | ServiceType.RESTDATA;
				}

				if (this.workspaceInfo.getMapCounts() > 0) {
					this.servicesType = this.servicesType | ServiceType.RESTMAP;
				}

				if (this.workspaceInfo.getSceneCounts() > 0) {
					this.servicesType = this.servicesType | ServiceType.RESTREALSPACE;
				}

				if (ListUtilties.IsListContainAny(this.workspaceInfo.getContainTypes(), DatasetType.CAD, DatasetType.GRID, DatasetType.LINE, DatasetType.LINEM,
						DatasetType.NETWORK, DatasetType.POINT, DatasetType.REGION, DatasetType.TABULAR)) {
					this.servicesType = this.servicesType | ServiceType.RESTSPATIALANALYST;
				}

				if (this.workspaceInfo.getContainTypes().contains(DatasetType.NETWORK)) {
					this.servicesType = this.servicesType | ServiceType.RESTTRANSPORTATIONANALYST;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void initializeControls() {
		initializeServicesTypes();
		initializeHostType();
		initializeHostValue();
	}

	// 根据工作空间的数据类型默认发布超图规范的REST服务
	// OGC服务默认不勾选
	private void initializeServicesTypes() {
		try {
			this.checkBoxRestData.setEnabled(false);
			this.checkBoxIsEditable.setEnabled(false);
			this.checkBoxRestMap.setEnabled(false);
			this.checkBoxRestRealspace.setEnabled(false);
			this.checkBoxRestSpatialAnalyst.setEnabled(false);
			this.checkBoxRestTransAnalyst.setEnabled(false);

			if ((this.servicesType & ServiceType.RESTDATA) == ServiceType.RESTDATA) {
				this.checkBoxRestData.setEnabled(true);
				this.checkBoxRestData.setSelected(true);
			}
			if ((this.servicesType & ServiceType.RESTMAP) == ServiceType.RESTMAP) {
				this.checkBoxRestMap.setEnabled(true);
				this.checkBoxRestMap.setSelected(true);
			}
			if ((this.servicesType & ServiceType.RESTREALSPACE) == ServiceType.RESTREALSPACE) {
				this.checkBoxRestRealspace.setEnabled(true);
				this.checkBoxRestRealspace.setSelected(true);
			}
			if ((this.servicesType & ServiceType.RESTSPATIALANALYST) == ServiceType.RESTSPATIALANALYST) {
				this.checkBoxRestSpatialAnalyst.setEnabled(true);
				this.checkBoxRestSpatialAnalyst.setSelected(true);
			}
			if ((this.servicesType & ServiceType.RESTTRANSPORTATIONANALYST) == ServiceType.RESTTRANSPORTATIONANALYST) {
				this.checkBoxRestTransAnalyst.setEnabled(true);
				this.checkBoxRestTransAnalyst.setSelected(true);
			}
			setCheckBoxEditableState();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void setCheckBoxEditableState() {
		if ((this.servicesType & ServiceType.RESTDATA) == ServiceType.RESTDATA || (this.servicesType & ServiceType.WFS100) == ServiceType.WFS100
				|| (this.servicesType & ServiceType.WCS111) == ServiceType.WCS111 || (this.servicesType & ServiceType.WCS112) == ServiceType.WCS112) {
			this.checkBoxIsEditable.setEnabled(true);
			this.checkBoxIsEditable.setSelected(this.isEditable);
		} else {
			this.checkBoxIsEditable.setEnabled(false);
			this.checkBoxIsEditable.setSelected(false);
		}
	}

	private void initializeHostType() {
		try {
			if (this.hostType == HostType.LOCAL) {
				this.radioButtonLocalHost.setSelected(true);
			} else if (this.hostType == HostType.REMOTE) {
				this.radioButtonRemoteHost.setSelected(true);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void initializeHostValue() {
		try {
			if (this.hostType == HostType.LOCAL) {
				this.textFieldHost.setText(LOCALHOST);
				this.textFieldHost.setEditable(false);
			} else if (this.hostType == HostType.REMOTE) {
				this.textFieldHost.setText(this.remoteHost);
				this.textFieldHost.setEditable(true);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void setButtonReleaseEnabled() {
		try {
			if (!this.canRelease || this.servicesType == ServiceType.NONE || StringUtilties.isNullOrEmpty(this.adminName)
					|| StringUtilties.isNullOrEmpty(this.adminPassword) || StringUtilties.isNullOrEmpty(this.port)
					|| (this.hostType == HostType.REMOTE && StringUtilties.isNullOrEmpty(this.remoteHost))) {
				this.buttonRelease.setEnabled(false);
			} else {
				this.buttonRelease.setEnabled(true);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void textFieldPasswordChange() {
		this.adminPassword = this.textFieldPassword.getText();
		setButtonReleaseEnabled();
	}

	private void textFieldUserNameChange() {
		this.adminName = this.textFieldUserName.getText();
		setButtonReleaseEnabled();
	}

	private void textFieldHostChange() {
		try {
			if (this.hostType == HostType.REMOTE) {
				this.remoteHost = this.textFieldHost.getText();
				setButtonReleaseEnabled();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void textFieldPortChange() {
		try {
			this.port = this.textFieldPort.getText();
			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWPS100CheckChange() {
		try {
			if (this.checkBoxWPS100.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WPS100;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WPS100;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWMTSCHINACheckChange() {
		try {
			if (this.checkBoxWMTSCHINA.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WMTSCHINA;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WMTSCHINA;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWMTS100CheckChange() {
		try {
			if (this.checkBoxWMTS100.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WMTS100;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WMTS100;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWMS130CheckChange() {
		try {
			if (this.checkBoxWMS130.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WMS130;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WMS130;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWMS111CheckChange() {
		try {
			if (this.checkBoxWMS111.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WMS111;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WMS111;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWFS100CheckChange() {
		try {
			if (this.checkBoxWFS100.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WFS100;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WFS100;
			}

			setCheckBoxEditableState();
			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWCS112CheckChange() {
		try {
			if (this.checkBoxWCS112.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WCS112;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WCS112;
			}

			setCheckBoxEditableState();
			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWCS111CheckChange() {
		try {
			if (this.checkBoxWCS111.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WCS111;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WCS111;
			}

			setCheckBoxEditableState();
			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxRestDataCheckChange() {
		try {
			if (this.checkBoxRestData.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.RESTDATA;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.RESTDATA;
			}

			setCheckBoxEditableState();
			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxIsEditableCheckChange() {
		this.isEditable = this.checkBoxIsEditable.isSelected();
		setButtonReleaseEnabled();
	}

	private void checkBoxRestMapCheckChange() {
		try {
			if (this.checkBoxRestMap.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.RESTMAP;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.RESTMAP;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxRestRealspaceCheckChange() {
		try {
			if (this.checkBoxRestRealspace.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.RESTREALSPACE;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.RESTREALSPACE;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxRestSpatialAnalystCheckChange() {
		try {
			if (this.checkBoxRestSpatialAnalyst.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.RESTSPATIALANALYST;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.RESTSPATIALANALYST;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxRestTransAnalystCheckChange() {
		try {
			if (this.checkBoxRestTransAnalyst.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.RESTTRANSPORTATIONANALYST;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.RESTTRANSPORTATIONANALYST;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void radioButtonLocalHostSelectedChange() {
		try {
			if (this.radioButtonLocalHost.isSelected()) {
				this.hostType = HostType.LOCAL;
				initializeHostValue();
				this.canRelease = true;
				// this.m_errorProvider.Clear();
				setButtonReleaseEnabled();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void radioButtonRemoteHostSelectedChange() {
		try {
			if (this.radioButtonRemoteHost.isSelected()) {
				this.hostType = HostType.REMOTE;
				initializeHostValue();

				if (!canRemoteRelease()) {
					this.canRelease = false;
					// this.m_errorProvider.SetError(this.m_radioButtonRemoteHost, "文件型工作空间下的文件型数据源必须与工作空间在同一目录下才能发布到远程服务器。");
				} else {
					this.canRelease = true;
					// this.m_errorProvider.Clear();
				}
				setButtonReleaseEnabled();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void buttonReleaseClicked() {
		CursorUtilties.setWaitCursor();
		try {
			// ServerRelease serverRelease = FillServerRelease();
			// //需要打包上传，需要展示将要打包的数据
			// if (!String.IsNullOrEmpty(serverRelease.WorkspacePath)
			// && serverRelease.HostType == HostType.RemoteHost)
			// {
			// _FormFolderSelector formFolderSelector = new _FormFolderSelector(serverRelease.WorkDirectory, serverRelease.Directories, serverRelease.Files);
			// if (formFolderSelector.ShowDialog() == DialogResult.OK)
			// {
			// serverRelease.Directories.Clear();
			// serverRelease.Directories.AddRange(formFolderSelector.Directories.ToArray());
			// serverRelease.Files.Clear();
			// serverRelease.Files.AddRange(formFolderSelector.Files.ToArray());
			// }
			// else
			// {
			// Application.ActiveApplication.Output.Output("操作已取消。");
			// return;
			// }
			// }
			//
			// //发布服务开始，给出提示，并开始统计时间
			// Application.ActiveApplication.Output.Output(Properties.NetServicesResources.String_iServer_Message_ReleaseStart);
			// DateTime startTime = DateTime.Now;
			//
			// this.m_formProgress = new FormProgressTotal();
			// this.m_formProgress.CancelButtonVisible = serverRelease.CanCancel;
			// this.m_formProgress.IsShowRemainTime = false;
			// this.m_formProgress.IsShowPercent = true;
			// Thread thread = CommonToolkit.ThreadWrap.CreateThread(() =>
			// {
			// this.m_formProgress.ShowDialog();
			// });
			//
			// thread.Start();
			//
			// Application.ActiveApplication.UserInfoManage.FunctionStart(FunctionID.ServerRelease);
			// serverRelease.FunctionProgress += new FunctionProgressEventHandler(serverRelease_FunctionProgress);
			// Boolean releaseResult = serverRelease.Release();
			// this.m_formProgress.Visible = false;
			// if (releaseResult)
			// {
			// ServerReleaseSetting();
			// Double totalTime = (DateTime.Now - startTime).TotalSeconds;
			// Application.ActiveApplication.UserInfoManage.FunctionSuccess(FunctionID.ServerRelease);
			// Application.ActiveApplication.Output.Output(String.Format(Properties.NetServicesResources.String_iServer_Message_ReleaseSuccess,totalTime.ToString()));
			// _FormResults formResult = new _FormResults(serverRelease.ResultURL);
			// Application.ActiveApplication.Output.Output("本次操作成功发布了以下服务");
			// for (Int32 i = 0; i < formResult.Results.Count; i++)
			// {
			// Application.ActiveApplication.Output.Output(formResult.Results[i]);
			// }
			// //formResult.ShowDialog(this);
			// this.DialogResult = DialogResult.OK;
			// }
			// else
			// {
			// Application.ActiveApplication.UserInfoManage.FunctionFailed(FunctionID.ServerRelease);
			// Application.ActiveApplication.Output.Output(Properties.NetServicesResources.String_iServer_Message_ReleaseFaild);
			// }
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			CursorUtilties.setDefaultCursor();
		}
	}

	private void buttonCloseClicked() {
		try {
			// this.DialogResult = DialogResult.Cancel;
			setVisible(false);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	void serverRelease_FunctionProgress() {
		// try
		// {
		// this.m_formProgress.UpdateProgress(String.Empty, e.CurrentMessage, e.CurrentProgress, e.TotalMessage, e.TotalProgress, String.Empty, TimeSpan.Zero);
		// e.Cancel = this.m_formProgress.IsCancel;
		// }
		// catch (Exception ex)
		// {
		// Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
		// }
	}

	private void serverReleaseSetting() {
		if (this.hostType == HostType.REMOTE) {
			// ServerReleaseSetting setting = new ServerReleaseSetting();
			// setting.RemoteHost = this.m_remoteHost;
			// setting.Save();
		}
	}

	private ServerRelease FillServerRelease() {
		ServerRelease serverRelease = new ServerRelease();
		serverRelease.setHostType(this.hostType);
		;
		if (this.hostType == HostType.LOCAL) {
			serverRelease.setHost(LOCALHOST);
		} else if (this.hostType == HostType.REMOTE) {
			serverRelease.setHost(this.remoteHost);
		}
		serverRelease.setPort(this.port);
		serverRelease.setServicesType(this.servicesType);
		serverRelease.setAdminName(this.adminName);
		serverRelease.setAdminPassword(this.adminPassword);
		serverRelease.setEditable(this.isEditable);
		serverRelease.setConnectionInfo(this.workspaceInfo.getWorkspaceConnectionInfo());
		serverRelease.setWorkspacePath(this.workspaceInfo.getWorkspacePath());
		// serverRelease.DatasourcesPath = this.m_workspaceInfo.DatasourcesPath;

		return serverRelease;
	}
}