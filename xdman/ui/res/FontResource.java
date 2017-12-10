package xdman.ui.res;

import java.awt.*;
import java.io.*;

import xdman.util.Logger;

public class FontResource {
	private static Font loadNoto(String sfont) {
		try {
			InputStream inStream = StringResource.class
					.getResourceAsStream("/fonts/" + sfont);
			if (inStream == null) {
				inStream = new FileInputStream("fonts/" + sfont);
			}
			Logger.log("Loading "+sfont);
			Font font = Font.createFont(Font.TRUETYPE_FONT, inStream);
			Logger.log("Loaded "+font);
			return font;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Font loadNotoRegular() {
		if (notoNormal == null) {
			notoNormal = loadNoto("NotoSansUI-Regular.ttf");
			if (notoNormal == null) {
				Logger.log("Noto regular font could not be loaded");
				notoNormal = new Font(Font.DIALOG, Font.PLAIN, 12);
			}
		}
		return notoNormal;
	}

	private static Font loadNotoBold() {
		if (notoBold == null) {
			notoBold = loadNoto("NotoSansUI-Bold.ttf");
			if (notoBold == null) {
				Logger.log("Noto regular font could not be loaded");
				notoBold = new Font(Font.DIALOG, Font.BOLD, 12);
			}
		}
		return notoBold;
	}

	private static Font notoNormal, notoBold;

	public static Font getNormalFont() {
		if (plainFont == null) {
			plainFont = loadNotoRegular().deriveFont(12.0f);
		}
		return plainFont;
	}

	public static Font getBoldFont() {
		if (boldFont == null) {
			boldFont = loadNotoBold().deriveFont(12.0f);
		}
		return boldFont;
	}

	public static Font getBigFont() {
		if (plainFontBig == null) {
			plainFontBig = loadNotoRegular().deriveFont(14.0f);
		}
		return plainFontBig;
	}

	public static Font getBigBoldFont() {
		if (boldFont2 == null) {
			boldFont2 = loadNotoBold().deriveFont(14.0f);
		}
		return boldFont2;
	}

	public static Font getItemFont() {
		if (itemFont == null) {
			itemFont = loadNotoRegular().deriveFont(16.0f);
		}
		return itemFont;
	}

	public static Font getBiggerFont() {
		if (plainFontBig1 == null) {
			plainFontBig1 = loadNotoRegular().deriveFont(18.0f);
		}
		return plainFontBig1;
	}
	
	public static Font getBiggestFont() {
		if (plainFontBig2 == null) {
			plainFontBig2 = loadNotoRegular().deriveFont(24.0f);
		}
		return plainFontBig2;
	}

	private static Font plainFont;
	private static Font boldFont;
	private static Font boldFont2;

	private static Font plainFontBig;
	private static Font plainFontBig1;
	private static Font plainFontBig2;
	private static Font itemFont;
}
