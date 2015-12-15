package com.supermap.desktop;

import com.supermap.data.Environment;
import com.supermap.desktop.enums.InfoType;

public class GlobalParameters {

	private GlobalParameters() {
		// do nothing
	}

	/**
	 * 获取或设置一个值，指示是否显示启动画面，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_showSplash = true;

	public static Boolean isShowSplash() {
		return g_showSplash;
	}

	public static void setShowSplash(Boolean value) {
		g_showSplash = value;
	}

	/**
	 * 获取或设置启动画面的图像路径，仅在ShowSplash为true时有效，初始值为""。
	 * 
	 * @return
	 */
	private static String g_splashFilePath = "";

	public static String getSplashFilePath() {
		return g_splashFilePath;
	}

	public static void setSplashFilePath(String value) {
		g_splashFilePath = value;
	}

	/**
	 * 获取或设置一个值，指示是否自动新建窗口浏览数据集数据，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_autoNewWindow = true;

	public static Boolean isAutoNewWindow() {
		return g_autoNewWindow;
	}

	public static void setAutoNewWindow(Boolean value) {
		g_autoNewWindow = value;
	}

	/**
	 * 获取或设置一个值，指示是否自动关闭没有图层的地图窗口，初始值为false。
	 * 
	 * @return
	 */
	private static Boolean g_autoCloseWindow = false;

	public static Boolean isAutoCloseWindow() {
		return g_autoCloseWindow;
	}

	public static void setAutoCloseWindow(Boolean value) {
		g_autoCloseWindow = value;
	}

	/**
	 * 获取或设置一个值，指示窗口关闭时是否提示保存，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_showFormClosingInfo = true;

	public static Boolean isShowFormClosingInfo() {
		return g_showFormClosingInfo;
	}

	public static void setShowFormClosingInfo(Boolean value) {
		g_showFormClosingInfo = value;
	}

	/**
	 * 获取或设置一个值，指示工作空间关闭时是否提示保存，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_showWorkspaceClosingInfo = true;

	public static Boolean isShowWorkspaceClosingInfo() {
		return g_showWorkspaceClosingInfo;
	}

	public static void setShowWorkspaceClosingInfo(Boolean value) {
		g_showWorkspaceClosingInfo = value;
	}

	/**
	 * 获取或设置一个值，指示有输出提示时是否自动弹出输出窗口，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_autoShowOutputPanel = true;

	public static Boolean isAutoShowOutputPanel() {
		return g_autoShowOutputPanel;
	}

	public static void setAutoShowOutputPanel(Boolean value) {
		g_autoShowOutputPanel = value;
	}

	/**
	 * 获取或设置一个值，指示是否自动隐藏系统字段，初始值为false。
	 * 
	 * @return
	 */
	private static Boolean g_hideSysFields = false;

	public static Boolean isHideSysFields() {
		return g_hideSysFields;
	}

	public static void setHideSysFields(Boolean value) {
		g_hideSysFields = value;
	}

	/**
	 * 获取或设置一个值，指示是否参加用户体验计划，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_launchUserExperiencePlan = true;

	public static Boolean isLaunchUserExperiencePlan() {
		return g_launchUserExperiencePlan;
	}

	public static void setLaunchUserExperiencePlan(Boolean value) {
		g_launchUserExperiencePlan = value;
	}

	/**
	 * 获取或设置一个值，指示是否开启自动更新，初始值为true
	 * 
	 * @return
	 */
	private static Boolean g_launchAutoUpdate = true;

	public static Boolean isLaunchAutoUpdate() {
		return g_launchAutoUpdate;
	}

	public static void setLaunchAutoUpdate(Boolean value) {
		g_launchAutoUpdate = value;
	}

	/**
	 * 获取或设置一个值，指示新建场景时是否自动加载框架数据，初始值为false。
	 * 
	 * @return
	 */
	private static Boolean g_autoLoadFrameData = false;

	public static Boolean isAutoLoadFrameData() {
		return g_autoLoadFrameData;
	}

	public static void setAutoLoadFrameData(Boolean value) {
		g_autoLoadFrameData = value;
	}

	/**
	 * 获取或设置一个值，指示是否显示导航条，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_showNavigationBar = false;

	public static Boolean isShowNavigationBar() {
		return g_showNavigationBar;
	}

	public static void setShowNavigationBar(Boolean value) {
		g_showNavigationBar = value;
	}

	/**
	 * 获取或设置一个值，指示是否显示工具提示，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_showScreenTip = true;

	public static Boolean isShowScreenTip() {
		return g_showScreenTip;
	}

	public static void setShowScreenTip(Boolean value) {
		g_showScreenTip = value;
	}

	/**
	 * 获取或设置桌面标题，初始值为：SuperMap iDesktop 7C。
	 * 
	 * @return
	 */
	private static String g_desktopTitle = "";

	public static String getDesktopTitle() {
		return g_desktopTitle;
	}

	public static void setDesktopTitle(String value) {
		g_desktopTitle = value;
	}

	/**
	 * 获取或设置数值的可见小数位数，初始值为4。
	 * 
	 * @return
	 */
	private static int m_nDecimalPlaces = 4;

	public static int getDecimalPlaces() {
		return m_nDecimalPlaces;
	}

	public static void setDecimalPlaces(int value) {
		m_nDecimalPlaces = value;
	}

	/**
	 * 获取或设置文件缓存路径，初始值为：./Cache/DatasetCache/。
	 * 
	 * @return
	 */
	public static String getFileCacheFolder() {
		return Environment.getFileCacheFolder();
	}

	public static void setFileCacheFolder(String value) {
		Environment.setFileCacheFolder(value);
	}

	/**
	 * 获取或设置一个值，指示是否生成运行日志，初始值为false。
	 * 
	 * @return
	 */
	private static Boolean g_outputToLog = false;

	public static Boolean isOutputToLog() {
		return g_outputToLog;
	}

	public static void setOutputToLog(Boolean value) {
		g_outputToLog = value;
	}

	/**
	 * 获取或设置一个值，指示是否显示启动画面，初始值为true。
	 * 
	 * @return
	 */
	private static String g_logFolder = "../Log/";

	public static String getLogFolder() {
		return g_logFolder;
	}

	public static void setLogFolder(String value) {
		g_logFolder = value;
	}

	/**
	 * 获取或设置一个值，指示是否自动检查工作空间版本，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_autoCheckWorkspaceVersion = true;

	public static Boolean isAutoCheckWorkspaceVersion() {
		return g_autoCheckWorkspaceVersion;
	}

	public static void setAutoCheckWorkspaceVersion(Boolean value) {
		g_autoCheckWorkspaceVersion = value;
	}

	/**
	 * 获取或设置程序输出信息的类型，初始值为InfoType.Information。
	 * 
	 * @return
	 */
	private static InfoType m_outputInfoType;

	public static InfoType getOutputInfoType() {
		return m_outputInfoType;
	}

	public static void setOutputInfoType(InfoType value) {
		m_outputInfoType = value;
	}

	/**
	 * 获取或设置一个值，指示是否显示启动画面，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_useRebackItemCount = true;

	public static Boolean isUseRebackItemCount() {
		return g_useRebackItemCount;
	}

	public static void setUseRebackItemCount(Boolean value) {
		g_useRebackItemCount = value;
	}

	/**
	 * 获取或设置一个值，指示是否显示启动画面，初始值为true。
	 * 
	 * @return
	 */
	private static int g_rebackItemCount = 1000000;

	public static int getRebackItemCount() {
		return g_rebackItemCount;
	}

	public static void setRebackItemCount(int value) {
		g_rebackItemCount = value;
	}

	/**
	 * 获取或设置一个值，指示是否限制可回退次数，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_useRebackTimes = true;

	public static Boolean isUseRebackTimes() {
		return g_useRebackTimes;
	}

	public static void setUseRebackTimes(Boolean value) {
		g_useRebackTimes = value;
	}

	/**
	 * 获取或设置最大回退次数，仅在UseRebackTimes为true时有效，初始值为1000。
	 * 
	 * @return
	 */
	private static int g_rebackTimes = 1000;

	public static int getRebackTimes() {
		return g_rebackTimes;
	}

	public static void setRebackTimes(int value) {
		g_rebackTimes = value;
	}

	/**
	 * 获取或设置一个值，指示是否显示启动画面，初始值为true。
	 * 
	 * @return
	 */
	private static int g_maxVisibleNodeCount = 3600000;

	public static int getMaxVisibleNodeCount() {
		return g_maxVisibleNodeCount;
	}

	public static void setMaxVisibleNodeCount(int value) {
		g_maxVisibleNodeCount = value;
	}

	/**
	 * 获取或设置对象选择模式，初始值为0。
	 * 
	 * @return
	 */
	private static int g_positiveSelect = 0;

	public static int getPositiveSelect() {
		return g_positiveSelect;
	}

	public static void setPositiveSelect(int value) {
		g_positiveSelect = value;
	}

	/**
	 * 获取或设置一个值，指示对象绘制时是否开启参数化绘制，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_parameterEdit = true;

	public static Boolean isParameterEdit() {
		return g_parameterEdit;
	}

	public static void setParameterEdit(Boolean value) {
		g_parameterEdit = value;
	}

	/**
	 * 获取或设置一个值，指示专题图是否即时刷新，初始值为true。
	 * 
	 * @return
	 */
	private static Boolean g_themeInstantRefresh = true;

	public static Boolean isThemeInstantRefresh() {
		return g_themeInstantRefresh;
	}

	public static void setThemeInstantRefresh(Boolean value) {
		g_themeInstantRefresh = value;
	}
}