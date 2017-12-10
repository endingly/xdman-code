package xdman.ui.components;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import xdman.XDMApp;
import xdman.ui.res.ImageResource;
import xdman.ui.res.StringResource;
import xdman.util.Logger;
import xdman.util.XDMUtils;

public class TrayHandler {
	static ActionListener act;

	public static void createTray() {
		if (!SystemTray.isSupported()) {
			Logger.log("SystemTray is not supported");
			return;
		}

		if (XDMUtils.detectOS() == XDMUtils.LINUX) {
			return;
		}

		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(ImageResource.get("icon.png").getImage());
		trayIcon.setImageAutoSize(true);
		final SystemTray tray = SystemTray.getSystemTray();

		act = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MenuItem c = (MenuItem) e.getSource();
				String name = c.getName();
				System.out.println(name);
				if ("ADD_URL".equals(name)) {
					XDMApp.getInstance().addDownload(null, null);
				} else if ("RESTORE".equals(name)) {
					XDMApp.getInstance().showMainWindow();
				} else if ("EXIT".equals(name)) {
					XDMApp.getInstance().exit();
				}
			}
		};

		// Create a pop-up menu components
		MenuItem addUrlItem = new MenuItem(StringResource.get("MENU_ADD_URL"));
		addUrlItem.addActionListener(act);
		addUrlItem.setName("ADD_URL");
		MenuItem restoreItem = new MenuItem(StringResource.get("MSG_RESTORE"));
		restoreItem.addActionListener(act);
		restoreItem.setName("RESTORE");
		MenuItem exitItem = new MenuItem(StringResource.get("MENU_EXIT"));
		exitItem.addActionListener(act);
		exitItem.setName("EXIT");

		// Add components to pop-up menu
		popup.add(addUrlItem);
		popup.add(restoreItem);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			Logger.log("TrayIcon could not be added.");
		}
	}
}
