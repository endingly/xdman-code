package xdman;

import java.io.*;
import java.util.Properties;

import xdman.util.Logger;
import xdman.util.StringUtils;
import xdman.util.XDMUtils;

public class Config {

	private boolean monitoring = true;
	private String metadataFolder;
	private String temporaryFolder;
	private String downloadFolder;
	private String dataFolder;
	private int sortField;
	private boolean sortAsc;
	private int categoryFilter;
	private int stateFilter;
	private String searchText;
	private int maxSegments;
	private int minSegmentSize;
	private int speedLimit; // in kb/sec
	private boolean showDownloadWindow;
	private boolean showDownloadCompleteWindow;
	private int maxDownloads;
	private boolean autoShutdown;
	private int duplicateAction;
	private String[] blockedHosts, vidUrls, fileExts, vidExts;
	private String[] defaultFileTypes, defaultVideoTypes;
	private int networkTimeout, tcpWindowSize;
	private int proxyMode;// 0 no-proxy,1 pac, 2 http, 3 socks
	private String proxyPac, proxyHost, socksHost;
	private int proxyPort, socksPort;
	private String proxyUser, proxyPass;
	private boolean showVideoNotification;
	private int minVidSize;
	private boolean keepAwake, execCmd, execAntivir, autoStart;
	private String customCmd, antivirCmd, antivirExe;
	private boolean firstRun;
	private String language;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void save() {
		FileWriter fw = null;
		try {
			File file = new File(System.getProperty("user.home"), ".xdman/config.txt");
			fw = new FileWriter(file);

			String newLine = "\n";

			fw.write("monitoring:" + this.monitoring + newLine);
			fw.write("downloadFolder:" + this.downloadFolder + newLine);
			fw.write("maxDownloads:" + this.maxDownloads + newLine);
			fw.write("maxSegments:" + this.maxSegments + newLine);
			fw.write("networkTimeout:" + this.networkTimeout + newLine);
			fw.write("tcpWindowSize:" + this.tcpWindowSize + newLine);
			fw.write("minSegmentSize:" + this.minSegmentSize + newLine);
			fw.write("minVidSize:" + this.minVidSize + newLine);
			fw.write("duplicateAction:" + this.duplicateAction + newLine);
			fw.write("speedLimit:" + this.speedLimit + newLine);
			fw.write("showDownloadWindow:" + this.showDownloadWindow + newLine);
			fw.write("showDownloadCompleteWindow:" + this.showDownloadCompleteWindow + newLine);
			fw.write("blockedHosts:" + XDMUtils.appendArray2Str(this.blockedHosts) + newLine);
			fw.write("vidUrls:" + XDMUtils.appendArray2Str(this.vidUrls) + newLine);
			fw.write("fileExts:" + XDMUtils.appendArray2Str(this.fileExts) + newLine);
			fw.write("vidExts:" + XDMUtils.appendArray2Str(this.vidExts) + newLine);

			fw.write("proxyMode:" + this.proxyMode + newLine);
			fw.write("proxyPac:" + this.proxyPac + newLine);
			fw.write("proxyHost:" + this.proxyHost + newLine);
			fw.write("proxyPort:" + this.proxyPort + newLine);
			fw.write("socksHost:" + this.socksHost + newLine);
			fw.write("socksPort:" + this.socksPort + newLine);
			fw.write("proxyUser:" + this.proxyUser + newLine);
			fw.write("proxyPass:" + this.proxyPass + newLine);
			fw.write("autoShutdown:" + this.autoShutdown + newLine);
			fw.write("keepAwake:" + this.keepAwake + newLine);
			fw.write("execCmd:" + this.execCmd + newLine);
			fw.write("execAntivir:" + this.execAntivir + newLine);
			fw.write("version:" + XDMApp.APP_VERSION + newLine);
			fw.write("autoStart:" + this.autoStart + newLine);
			fw.write("language:" + this.language + newLine);
			if (!StringUtils.isNullOrEmptyOrBlank(this.antivirExe))
				fw.write("antivirExe:" + this.antivirExe + newLine);
			if (!StringUtils.isNullOrEmptyOrBlank(this.antivirCmd))
				fw.write("antivirCmd:" + this.antivirCmd + newLine);
			if (!StringUtils.isNullOrEmptyOrBlank(this.customCmd))
				fw.write("customCmd:" + this.customCmd + newLine);
			fw.write("showVideoNotification:" + this.showVideoNotification + newLine);

		} catch (Exception e) {
		}
		try {
			fw.close();
		} catch (Exception e) {
		}
	}

	public void load() {
		BufferedReader br = null;
		try {
			File file = new File(System.getProperty("user.home"), ".xdman/config.txt");
			if (!file.exists()) {
				return;
			}
			FileReader r = new FileReader(file);
			br = new BufferedReader(r);
			while (true) {
				String ln = br.readLine();
				if (ln == null)
					break;
				if (ln.startsWith("#"))
					continue;
				int index = ln.indexOf(":");
				if (index < 1)
					continue;
				String key = ln.substring(0, index);
				String val = ln.substring(index + 1);
				if (key.equals("monitoring")) {
					this.monitoring = val.equals("true");
				} else if (key.equals("downloadFolder")) {
					this.downloadFolder = val;
				} else if (key.equals("maxSegments")) {
					this.maxSegments = Integer.parseInt(val);
				} else if (key.equals("minSegmentSize")) {
					this.minSegmentSize = Integer.parseInt(val);
				} else if (key.equals("networkTimeout")) {
					this.networkTimeout = Integer.parseInt(val);
				} else if (key.equals("tcpWindowSize")) {
					this.tcpWindowSize = Integer.parseInt(val);
				} else if (key.equals("duplicateAction")) {
					this.duplicateAction = Integer.parseInt(val);
				} else if (key.equals("speedLimit")) {
					this.speedLimit = Integer.parseInt(val);
				} else if (key.equals("showDownloadWindow")) {
					this.showDownloadWindow = val.equals("true");
				} else if (key.equals("showDownloadCompleteWindow")) {
					this.showDownloadCompleteWindow = val.equals("true");
				} else if (key.equals("minVidSize")) {
					this.minVidSize = Integer.parseInt(val);
				} else if (key.equals("maxDownloads")) {
					this.maxDownloads = Integer.parseInt(val);
				} else if (key.equals("blockedHosts")) {
					this.blockedHosts = val.split(",");
				} else if (key.equals("vidUrls")) {
					this.vidUrls = val.split(",");
				} else if (key.equals("fileExts")) {
					this.fileExts = val.split(",");
				} else if (key.equals("vidExts")) {
					this.vidExts = val.split(",");
				} else if (key.equals("proxyMode")) {
					this.proxyMode = Integer.parseInt(val);
				} else if (key.equals("proxyPort")) {
					this.proxyPort = Integer.parseInt(val);
				} else if (key.equals("socksPort")) {
					this.socksPort = Integer.parseInt(val);
				} else if (key.equals("proxyPac")) {
					this.proxyPac = val;
				} else if (key.equals("proxyHost")) {
					this.proxyHost = val;
				} else if (key.equals("socksHost")) {
					this.socksHost = val;
				} else if (key.equals("proxyUser")) {
					this.proxyUser = val;
				} else if (key.equals("proxyPass")) {
					this.proxyPass = val;
				} else if (key.equals("showVideoNotification")) {
					this.showVideoNotification = "true".equals(val);
				} else if (key.equals("keepAwake")) {
					this.keepAwake = "true".equals(val);
				} else if (key.equals("autoStart")) {
					this.autoStart = "true".equals(val);
				} else if (key.equals("execAntivir")) {
					this.execAntivir = "true".equals(val);
				} else if (key.equals("execCmd")) {
					this.execCmd = "true".equals(val);
				} else if (key.equals("antivirExe")) {
					this.antivirExe = val;
				} else if (key.equals("antivirCmd")) {
					this.antivirCmd = val;
				} else if (key.equals("customCmd")) {
					this.customCmd = val;
				} else if (key.equals("autoShutdown")) {
					this.autoShutdown = "true".equals(val);
				} else if (key.equals("version")) {
					this.firstRun = !XDMApp.APP_VERSION.equals(val);
				} else if (key.equals("language")) {
					this.language = val;
				}
			}
		} catch (Exception e) {
			Logger.log(e);
		}
		try {
			br.close();
		} catch (Exception e) {
		}
	}

	private static Config _config;

	private Config() {
		File f = new File(System.getProperty("user.home"), ".xdman");
		if (!f.exists()) {
			f.mkdirs();
		}
		dataFolder = f.getAbsolutePath();
		f = new File(dataFolder, "metadata");
		if (!f.exists()) {
			f.mkdir();
		}
		this.metadataFolder = f.getAbsolutePath();
		f = new File(dataFolder, "temp");
		if (!f.exists()) {
			f.mkdir();
		}
		this.temporaryFolder = f.getAbsolutePath();
		this.downloadFolder = XDMUtils.getDownloadsFolder();
		if (!new File(this.downloadFolder).exists()) {
			this.downloadFolder = System.getProperty("user.home");
		}

		this.monitoring = true;
		this.showDownloadWindow = true;
		this.setMaxSegments(8);
		this.setMinSegmentSize(64 * 1024);
		this.maxDownloads = 100;
		this.minVidSize = 1 * 1024 * 1024;
		this.defaultFileTypes = new String[] { "3GP", "7Z", "AVI", "BZ2", "DEB", "DOC", "DOCX", "EXE", "GZ", "ISO",
				"MSI", "PDF", "PPT", "PPTX", "RAR", "RPM", "XLS", "XLSX", "SIT", "SITX", "TAR", "JAR", "ZIP" };
		this.fileExts = defaultFileTypes;
		this.autoShutdown = false;
		this.blockedHosts = new String[] { "update.microsoft.com", "windowsupdate.com", "thwawte.com" };
		this.defaultVideoTypes = new String[] { "MP4", "M3U8", "F4M", "WEBM", "OGG", "MP3", "AAC", "FLV", "MKV", "DIVX",
				"MOV", "MPG", "MPEG", "OPUS" };
		this.vidExts = defaultVideoTypes;
		this.vidUrls = new String[] { ".facebook.com|pagelet", "player.vimeo.com/", "instagram.com/p/" };
		this.networkTimeout = 60;
		this.tcpWindowSize = 8;
		this.speedLimit = 0;

		this.proxyMode = 0;
		this.proxyPort = 0;
		this.socksPort = 0;
		this.proxyPac = this.proxyHost = this.proxyUser = this.proxyPass = this.socksHost = "";
		this.showVideoNotification = true;
		this.showDownloadCompleteWindow = true;
		this.firstRun = true;
		this.language = "en";
	}

	public static Config getInstance() {
		if (_config == null) {
			_config = new Config();
		}
		return _config;
	}

	public final String getMetadataFolder() {
		return metadataFolder;
	}

	public final String getTemporaryFolder() {
		return temporaryFolder;
	}

	public final String getDataFolder() {
		return dataFolder;
	}

	public int getX() {
		return -1;
	}

	public int getY() {
		return -1;
	}

	public int getWidth() {
		return -1;
	}

	public int getHeight() {
		return -1;
	}

	public boolean getSortAsc() {
		return sortAsc;
	}

	public void setSortAsc(boolean sortAsc) {
		this.sortAsc = sortAsc;
	}

	public boolean isBrowserMonitoringEnabled() {
		return monitoring;
	}

	public void enableMonitoring(boolean enable) {
		monitoring = enable;
	}

	public int getSortField() {
		return sortField;
	}

	public void setSortField(int sortField) {
		this.sortField = sortField;
	}

	public int getCategoryFilter() {
		return categoryFilter;
	}

	public void setCategoryFilter(int categoryFilter) {
		this.categoryFilter = categoryFilter;
	}

	public int getStateFilter() {
		return stateFilter;
	}

	public void setStateFilter(int stateFilter) {
		this.stateFilter = stateFilter;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getDownloadFolder() {
		return downloadFolder;
	}

	public void setDownloadFolder(String downloadFolder) {
		this.downloadFolder = downloadFolder;
	}

	public int getMaxSegments() {
		return maxSegments;
	}

	public void setMaxSegments(int maxSegments) {
		this.maxSegments = maxSegments;
	}

	public int getMinSegmentSize() {
		return minSegmentSize;
	}

	public void setMinSegmentSize(int minSegmentSize) {
		this.minSegmentSize = minSegmentSize;
	}

	public final int getSpeedLimit() {
		return speedLimit;
	}

	public final void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}

	public final boolean showDownloadWindow() {
		return showDownloadWindow;
	}

	public final void setShowDownloadWindow(boolean show) {
		this.showDownloadWindow = show;
	}

	public final int getMaxDownloads() {
		return maxDownloads;
	}

	public final void setMaxDownloads(int maxDownloads) {
		this.maxDownloads = maxDownloads;
	}

	public final boolean isAutoShutdown() {
		return autoShutdown;
	}

	public final void setAutoShutdown(boolean autoShutdown) {
		this.autoShutdown = autoShutdown;
	}

	public String[] getBlockedHosts() {
		return blockedHosts;
	}

	public void setBlockedHosts(String[] blockedHosts) {
		this.blockedHosts = blockedHosts;
	}

	public String[] getVidUrls() {
		return vidUrls;
	}

	public void setVidUrls(String[] vidUrls) {
		this.vidUrls = vidUrls;
	}

	public String[] getFileExts() {
		return fileExts;
	}

	public void setFileExts(String[] fileExts) {
		this.fileExts = fileExts;
	}

	public String[] getVidExts() {
		return vidExts;
	}

	public void setVidExts(String[] vidExts) {
		this.vidExts = vidExts;
	}

	public final boolean showDownloadCompleteWindow() {
		return showDownloadCompleteWindow;
	}

	public final int getDuplicateAction() {
		return duplicateAction;
	}

	public final void setDuplicateAction(int duplicateAction) {
		this.duplicateAction = duplicateAction;
	}

	public final void setShowDownloadCompleteWindow(boolean show) {
		this.showDownloadCompleteWindow = show;
	}

	public final String[] getDefaultFileTypes() {
		return defaultFileTypes;
	}

	public final void setDefaultFileTypes(String[] defaultFileTypes) {
		this.defaultFileTypes = defaultFileTypes;
	}

	public final String[] getDefaultVideoTypes() {
		return defaultVideoTypes;
	}

	public final void setDefaultVideoTypes(String[] defaultVideoTypes) {
		this.defaultVideoTypes = defaultVideoTypes;
	}

	public final int getNetworkTimeout() {
		return networkTimeout;
	}

	public final void setNetworkTimeout(int networkTimeout) {
		this.networkTimeout = networkTimeout;
	}

	public final int getTcpWindowSize() {
		return tcpWindowSize;
	}

	public final void setTcpWindowSize(int tcpWindowSize) {
		this.tcpWindowSize = tcpWindowSize;
	}

	public final int getProxyMode() {
		return proxyMode;
	}

	public final void setProxyMode(int proxyMode) {
		this.proxyMode = proxyMode;
	}

	public final String getProxyUser() {
		return proxyUser;
	}

	public final void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public final String getProxyPass() {
		return proxyPass;
	}

	public final void setProxyPass(String proxyPass) {
		this.proxyPass = proxyPass;
	}

	public final String getProxyPac() {
		return proxyPac;
	}

	public final void setProxyPac(String proxyPac) {
		this.proxyPac = proxyPac;
	}

	public final String getProxyHost() {
		return proxyHost;
	}

	public final void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public final int getProxyPort() {
		return proxyPort;
	}

	public final void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean isShowVideoNotification() {
		return showVideoNotification;
	}

	public void setShowVideoNotification(boolean showVideoNotification) {
		this.showVideoNotification = showVideoNotification;
	}

	public int getMinVidSize() {
		return minVidSize;
	}

	public void setMinVidSize(int minVidSize) {
		this.minVidSize = minVidSize;
	}

	public String getSocksHost() {
		return socksHost;
	}

	public void setSocksHost(String socksHost) {
		this.socksHost = socksHost;
	}

	public int getSocksPort() {
		return socksPort;
	}

	public void setSocksPort(int socksPort) {
		this.socksPort = socksPort;
	}

	public boolean isKeepAwake() {
		return keepAwake;
	}

	public void setKeepAwake(boolean keepAwake) {
		this.keepAwake = keepAwake;
	}

	public boolean isExecCmd() {
		return execCmd;
	}

	public void setExecCmd(boolean execCmd) {
		this.execCmd = execCmd;
	}

	public boolean isExecAntivir() {
		return execAntivir;
	}

	public void setExecAntivir(boolean execAntivir) {
		this.execAntivir = execAntivir;
	}

	public boolean isAutoStart() {
		return autoStart;
	}

	public void setAutoStart(boolean autoStart) {
		this.autoStart = autoStart;
	}

	public String getCustomCmd() {
		return customCmd;
	}

	public void setCustomCmd(String customCmd) {
		this.customCmd = customCmd;
	}

	public String getAntivirCmd() {
		return antivirCmd;
	}

	public void setAntivirCmd(String antivirCmd) {
		this.antivirCmd = antivirCmd;
	}

	public String getAntivirExe() {
		return antivirExe;
	}

	public void setAntivirExe(String antivirExe) {
		this.antivirExe = antivirExe;
	}

	public boolean isFirstRun() {
		return firstRun;
	}
}
