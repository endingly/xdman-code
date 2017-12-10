package xdman.ui.res;

import java.util.*;

import xdman.Config;
import xdman.util.Logger;
import xdman.util.XDMUtils;

import java.io.*;

public class StringResource {
	private static Properties strings;

	public static String get(String id) {
		if (strings == null) {
			try {
				boolean en = false;
				String lang = Config.getInstance().getLanguage();
				File langFile = null;
				if ("en".equals(lang)) {
					en = true;
				} else {
					File jarPath = XDMUtils.getJarFile().getParentFile();
					langFile = new File(jarPath, "lang/" + lang + ".txt");
					if (!langFile.exists()) {
						Logger.log("Unable to find language file: " + langFile);
						en = true;
						Config.getInstance().setLanguage("en");
					}
				}
				if (en) {
					loadDefaultLanguage();
				} else {
					loadLanguage(langFile);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return strings.getProperty(id);
	}

	private static void loadDefaultLanguage() throws Exception {
		strings = new Properties();
		InputStream inStream = StringResource.class.getResourceAsStream("/lang/en.txt");
		if (inStream == null) {
			inStream = new FileInputStream("lang/en.txt");
		}
		strings.load(inStream);
	}

	private static void loadLanguage(File f) throws Exception {
		InputStream inStream = new FileInputStream(f);
		strings.load(inStream);
	}
}
