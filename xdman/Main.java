package xdman;

import javax.swing.UIManager;

import xdman.ui.laf.XDMLookAndFeel;
import xdman.util.Logger;
import xdman.util.XDMUtils;
import xdman.win32.NativeMethods;

public class Main {
	static {
		System.setProperty("http.KeepAlive.remainingData", "0");
		System.setProperty("http.KeepAlive.queuedConnections", "0");
		System.setProperty("sun.net.http.errorstream.enableBuffering", "false");
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");
	}

	public static void main(String[] args) {
		Logger.log("loading...");
		//System.out.println(XDMUtils.getJarFile());
		//System.out.println("Folder: "+NativeMethods.getInstance().getDownloadsFolder());
		Logger.log(System.getProperty("java.version")+" "+System.getProperty("os.version"));
		try {
			UIManager.setLookAndFeel(new XDMLookAndFeel());
		} catch (Exception e) {
			Logger.log(e);
		}
		XDMApp.start(args);
	}

}
