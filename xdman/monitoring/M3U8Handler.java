package xdman.monitoring;

import java.io.File;
import java.util.ArrayList;

import xdman.XDMApp;
import xdman.downloaders.metadata.HlsMetadata;
import xdman.downloaders.metadata.HttpMetadata;
import xdman.downloaders.metadata.manifests.M3U8Manifest;
import xdman.downloaders.metadata.manifests.M3U8Manifest.M3U8MediaInfo;
import xdman.util.StringUtils;
import xdman.util.XDMUtils;

public class M3U8Handler {
	public static boolean handle(File m3u8file, ParsedHookData data) {
		try {
			M3U8Manifest manifest = new M3U8Manifest(m3u8file.getAbsolutePath(), data.getUrl());
			if (manifest.isEncrypted()) {
				return true;
			}
			if (!manifest.isMasterPlaylist()) {
				HlsMetadata metadata = new HlsMetadata();
				metadata.setUrl(data.getUrl());
				metadata.setHeaders(data.getRequestHeaders());
				String file = data.getFile();
				if (StringUtils.isNullOrEmptyOrBlank(file)) {
					file = XDMUtils.getFileName(data.getUrl());
				}
				XDMApp.getInstance().addMedia(metadata, file + ".ts", "HLS");
			} else {
				ArrayList<String> urls = manifest.getMediaUrls();
				if (urls != null) {
					for (int i = 0; i < urls.size(); i++) {
						String url = urls.get(i);
						M3U8MediaInfo info = manifest.getMediaProperty(i);
						HlsMetadata metadata = new HlsMetadata();
						metadata.setUrl(url);
						metadata.setHeaders(data.getRequestHeaders());
						String file = data.getFile();
						if (StringUtils.isNullOrEmptyOrBlank(file)) {
							file = XDMUtils.getFileName(data.getUrl());
						}
						StringBuilder infoStr = new StringBuilder();
						if (!StringUtils.isNullOrEmptyOrBlank(info.getBandwidth())) {
							infoStr.append(info.getBandwidth());
						}
						if (infoStr.length() > 0) {
							infoStr.append(" ");
						}
						if (!StringUtils.isNullOrEmptyOrBlank(info.getResolution())) {
							infoStr.append(info.getResolution());
						}
						XDMApp.getInstance().addMedia(metadata, file + ".ts", infoStr.toString());
					}
				}
			}
			return true;
		} catch (Exception e) {
		}
		return false;
	}
}
