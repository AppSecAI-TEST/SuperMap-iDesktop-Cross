package com.supermap.desktop.CtrlAction;

import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceClosingEvent;
import com.supermap.data.WorkspaceClosingListener;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceSavedAsEvent;
import com.supermap.data.WorkspaceSavedAsListener;
import com.supermap.data.WorkspaceSavedEvent;
import com.supermap.data.WorkspaceSavedListener;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.LogUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.WorkspaceConnectionInfoUtilities;
import com.supermap.desktop.utilities.WorkspaceUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author XiaJT
 */
public class WorkspaceTempSave {

	private static WorkspaceTempSave workspaceTempSave = null;
	private Timer timer;
	private TimerTask task;
	private int period = 60000; // 1 min
	private String defaultName = "tempWorkspace";
	private File autoSaveWorkspaceConfigFile;
	private FileLock fileLock;
	private RandomAccessFile randomAccessFile;
	private Workspace workspace;
	private WorkspaceClosingListener workspaceClosingListener;
	private String lastServer;
	private WorkspaceSavedListener workspaceSavedListener = new WorkspaceSavedListener() {
		@Override
		public void workspaceSaved(WorkspaceSavedEvent workspaceSavedEvent) {
			autoSave(true);
		}
	};

	private WorkspaceSavedAsListener workspaceSavedAsListener = new WorkspaceSavedAsListener() {
		@Override
		public void workspaceSavedAs(WorkspaceSavedAsEvent workspaceSavedAsEvent) {
			autoSave(true);
		}
	};


	private WorkspaceTempSave() {
		// 获取数据目录
		String filePath = getFilePath();
		if (StringUtilities.isNullOrEmpty(filePath)) {
			return;
		}
		autoSaveWorkspaceConfigFile = new File(filePath);
		try {
			if ((new File(autoSaveWorkspaceConfigFile.getParent()).exists() || new File(autoSaveWorkspaceConfigFile.getParent()).mkdirs()) && autoSaveWorkspaceConfigFile.createNewFile()) {
				randomAccessFile = new RandomAccessFile(autoSaveWorkspaceConfigFile, "rw");
				fileLock = randomAccessFile.getChannel().tryLock();
				if (fileLock == null || !fileLock.isValid()) {
					throw new IOException();
				}
			}
		} catch (IOException e) {
			exit();
			autoSaveWorkspaceConfigFile = null;
			LogUtilities.outPut("create new autoSaveWorkspaceConfigFile failed. AutoSave exit.");
			return;
		}
		workspaceClosingListener = new WorkspaceClosingListener() {
			@Override
			public void workspaceClosing(WorkspaceClosingEvent workspaceClosingEvent) {
				// 工作空间关闭可能是要打开当前保存的工作空间，所以先关闭一次
				closeTempWorkspace();
				workspaceClosingEvent.getWorkspace().removeClosingListener(workspaceClosingListener);
			}
		};

		timer = new Timer("WorkspaceSave", true);

	}

	public void start() {
		if (task != null) {
			task.cancel();
		}
		task = new TimerTask() {
			@Override
			public void run() {
				autoSave(false);
			}
		};
		addListeners();
		timer.schedule(task, period / 6, period);
	}

	private void addListeners() {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		workspace.addClosingListener(workspaceClosingListener);
		workspace.addSavedListener(workspaceSavedListener);
		workspace.addSavedAsListener(workspaceSavedAsListener);
	}

	private void removeListeners() {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		workspace.removeClosingListener(workspaceClosingListener);
		workspace.removeSavedListener(workspaceSavedListener);
		workspace.removeSavedAsListener(workspaceSavedAsListener);
	}
	private String getFilePath() {
		String appDataPath = FileUtilities.getAppDataPath();

		if (appDataPath == null) {
			LogUtilities.outPut("get AppData path failed. AutoSave exit.");
			return null;
		}
		appDataPath += "tempWorkspace" + File.separator;

		File file = new File(appDataPath);
		File[] listFiles = file.listFiles();
		ArrayList<String> list = new ArrayList<>();
		if (listFiles != null && listFiles.length > 0) {
			for (File file1 : listFiles) {
				if (file1.getName().toLowerCase().endsWith(".xml")) {
					list.add(file1.getName().substring(0, file1.getName().length() - 4));
				}
			}
		}
		return appDataPath + StringUtilities.getUniqueName(defaultName, list) + ".xml";
	}

	private void autoSave(boolean isIgnoreModified) {
		Workspace activeWorkspace = Application.getActiveApplication().getWorkspace();

		synchronized (activeWorkspace) {
			if (!isIgnoreModified && !WorkspaceUtilities.isWorkspaceModified()) {
				return;
			}
			activeWorkspace.addClosingListener(workspaceClosingListener);
			WorkspaceConnectionInfo connectionInfo = activeWorkspace.getConnectionInfo();
			WorkspaceType type = connectionInfo.getType();
			if (type != WorkspaceType.DEFAULT && type != WorkspaceType.SMWU && type != WorkspaceType.SXWU) {
				// 当前工作空间不属于需要保存的类型
				WorkspaceUtilities.deleteFileWorkspace(lastServer);
				lastServer = null;
				closeTempWorkspace();
				WorkspaceUtilities.deleteFileWorkspace(lastServer);
				lastServer = null;
				try {
					randomAccessFile.setLength(0);
				} catch (IOException e) {
					LogUtilities.outPut("database workspace delete config failed");
				}
			} else {
				if (workspace == null) {
					workspace = new Workspace();
				}

				WorkspaceConnectionInfo workspaceConnectionInfo = null;
				if (workspace.getType() != WorkspaceType.DEFAULT && workspace.getVersion() == activeWorkspace.getVersion() && (workspace.getType() == activeWorkspace.getType() || activeWorkspace.getType() == WorkspaceType.DEFAULT)) {
					if (!workspace.getConnectionInfo().getPassword().equals(activeWorkspace.getConnectionInfo().getPassword())) {
						workspace.changePassword(workspace.getConnectionInfo().getPassword(), activeWorkspace.getConnectionInfo().getPassword());
					}
				} else {
					// 之前手动保存的工作空间删除
					WorkspaceUtilities.deleteFileWorkspace(lastServer);
					lastServer = null;

					closeTempWorkspace();
					if (workspace == null) {
						workspace = new Workspace();
					}
					WorkspaceUtilities.deleteFileWorkspace(lastServer);
					lastServer = null;

					String tempWorkspaceFilePath = getTempWorkspaceFilePath(connectionInfo.getType());
					workspaceConnectionInfo = new WorkspaceConnectionInfo(tempWorkspaceFilePath);
					workspaceConnectionInfo.setServer(tempWorkspaceFilePath);
					workspaceConnectionInfo.setType(connectionInfo.getType() != WorkspaceType.SXWU ? WorkspaceType.SMWU : WorkspaceType.SXWU);
					workspaceConnectionInfo.setVersion(connectionInfo.getVersion());
					workspaceConnectionInfo.setUser(connectionInfo.getUser());
					workspaceConnectionInfo.setPassword(connectionInfo.getPassword());
				}

				workspace = WorkspaceUtilities.copyWorkspace(activeWorkspace, workspace);
				boolean saveSuccess;
				if (workspaceConnectionInfo != null) {
					saveSuccess = workspace.saveAs(workspaceConnectionInfo);
				} else {
					saveSuccess = workspace.save();
				}
				if (saveSuccess) {
					if (!saveToFile(activeWorkspace, workspace)) {
						LogUtilities.outPut("save config autoSaveWorkspaceConfigFile failed");
					}
				} else {
					LogUtilities.outPut("workspace autoSave failed");
				}
			}
		}
	}

	private void closeTempWorkspace() {
		if (workspace == null) {
			return;
		}
		if (!StringUtilities.isNullOrEmpty(workspace.getConnectionInfo().getServer())) {
			lastServer = workspace.getConnectionInfo().getServer();
			workspace.close();
			workspace = null;
		}
	}

	private boolean saveToFile(Workspace activeWorkspace, Workspace workspace) {
		Document document = XmlUtilities.getEmptyDocument();
		if (document == null) {
			return false;
		}
		Element rootNode = document.createElement("AutoSaveWorkspace");

		Element workspacePath = document.createElement("WorkspacePath");
		workspacePath.appendChild(document.createTextNode(activeWorkspace.getConnectionInfo().getServer() == null ? "" : activeWorkspace.getConnectionInfo().getServer()));
		rootNode.appendChild(workspacePath);

		// 工作空间连接信息
		Element workspaceConnectionNode = WorkspaceConnectionInfoUtilities.toXml(workspace.getConnectionInfo(), document);
		rootNode.appendChild(workspaceConnectionNode);

		// 数据源连接信息
		Element datasourcesNode = document.createElement("Datasources");
		Datasources datasources = activeWorkspace.getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).getEngineType() == EngineType.UDB && !datasources.get(i).isReadOnly() && datasources.get(i).isOpened()) {
				Element datasource = document.createElement("Datasource");
				byte[] bytes = null;
				try {
					bytes = datasources.get(i).getConnectionInfo().toXML().getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					LogUtilities.outPut("EncodingFailed");
					return false;
				}
				datasource.appendChild(document.createTextNode(new BASE64Encoder().encode(bytes)));
				datasourcesNode.appendChild(datasource);
			}
		}
		rootNode.appendChild(datasourcesNode);
		try {
			try {
				randomAccessFile.setLength(0);
			} catch (IOException e) {
				LogUtilities.outPut("file workspace delete config failed");
			}
			randomAccessFile.seek(0);
			String string = XmlUtilities.nodeToString(rootNode, "UTF-8");
			randomAccessFile.write(string.getBytes("UTF-8"));
		} catch (Exception e) {
			LogUtilities.outPut("write to config file failed.");
			return false;
		}
		return true;
	}

	private String getTempWorkspaceFilePath(WorkspaceType type) {
		String fileSuffixes = type == WorkspaceType.SXWU ? ".sxwu" : ".smwu";
		String prefixes = autoSaveWorkspaceConfigFile.getPath().substring(0, autoSaveWorkspaceConfigFile.getPath().length() - 4);// 去掉.xml
		int i = 0;
		for (; new File(prefixes + (i == 0 ? "" : "_" + i) + fileSuffixes).exists(); i++) ;
		return prefixes + (i == 0 ? "" : "_" + i) + fileSuffixes;
	}


	public boolean exit() {
		removeListeners();
		task.cancel();
		if (fileLock != null) {
			try {
				fileLock.release();
			} catch (IOException e) {
				// ignore
			}
		}
		if (randomAccessFile != null) {
			try {
				randomAccessFile.close();
			} catch (IOException e) {
				// ignore
			}
		}
		if (autoSaveWorkspaceConfigFile.exists()) {
			if (!autoSaveWorkspaceConfigFile.delete()) {
				LogUtilities.outPut("Delete AutoSaveWorkspaceConfigFile Failed On Exit ");
			}
		}
		if (workspace != null) {
			lastServer = workspace.getConnectionInfo().getServer();
			workspace.close();
			workspace = null;
		}
		if (!WorkspaceUtilities.deleteFileWorkspace(lastServer)) {
			LogUtilities.outPut("Delete TempWorkspace Failed On Exit ");
		}
		return false;
	}


	public static WorkspaceTempSave getInstance() {
		if (workspaceTempSave == null) {
			workspaceTempSave = new WorkspaceTempSave();
		}
		return workspaceTempSave;
	}

}
