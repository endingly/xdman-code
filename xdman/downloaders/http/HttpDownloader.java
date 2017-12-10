package xdman.downloaders.http;

import xdman.XDMConstants;
import xdman.downloaders.AbstractChannel;
import xdman.downloaders.Segment;
import xdman.downloaders.SegmentDownloader;
import xdman.downloaders.metadata.DashMetadata;
import xdman.downloaders.metadata.HttpMetadata;
import xdman.network.ProxyResolver;
import xdman.util.Logger;
import xdman.util.NetUtils;
import xdman.util.XDMUtils;

public class HttpDownloader extends SegmentDownloader {
	private HttpMetadata metadata;
	private String newFileName;
	private boolean isJavaClientRequired;

	public HttpDownloader(String id, String folder, HttpMetadata metadata) {
		super(id, folder);
		this.metadata = metadata;
	}

	@Override
	public AbstractChannel createChannel(Segment segment) {
		HttpChannel hc = new HttpChannel(segment, metadata.getUrl(), metadata.getHeaders(), length,
				isJavaClientRequired);
		return hc;
	}

	@Override
	public int getType() {
		return XDMConstants.HTTP;
	}

	@Override
	public boolean isFileNameChanged() {
		Logger.log("Checking for filename change " + (newFileName != null));
		return newFileName != null;
	}

	@Override
	public String getNewFile() {
		return newFileName;
	}

	@Override
	protected void chunkConfirmed(Segment c) {
		HttpChannel hc = (HttpChannel) c.getChannel();
		this.isJavaClientRequired = hc.isJavaClientRequired();
		super.getLastModifiedDate(c);
		if (hc.isRedirected()) {
			metadata.setUrl(hc.getRedirectUrl());
			metadata.save();
			if (outputFormat == 0) {
				newFileName = XDMUtils.getFileName(metadata.getUrl());
				Logger.log("set new filename: " + newFileName);
				Logger.log("new file name: " + newFileName);
			}
		}
		String contentDispositionHeader = hc.getHeader("content-disposition");
		if (contentDispositionHeader != null) {
			if (outputFormat == 0) {
				String name = NetUtils.getNameFromContentDisposition(contentDispositionHeader);
				if (name != null) {
					this.newFileName = name;
					Logger.log("set new filename: " + newFileName);
				}
			}
		}
		if ((hc.getHeader("content-type") + "").contains("/html")) {
			if (this.newFileName != null) {
				String upperStr = this.newFileName.toUpperCase();
				if (!(upperStr.endsWith(".HTML") || upperStr.endsWith(".HTM"))) {
					outputFormat = 0;
					this.newFileName += ".html";
					Logger.log("set new filename: " + newFileName);
				}
			}
		}
		Logger.log("new filename: " + newFileName);
	}

	@Override
	public HttpMetadata getMetadata() {
		return this.metadata;
	}

}
