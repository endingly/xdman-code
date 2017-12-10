package xdman.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import xdman.LinkRefreshCallback;
import xdman.XDMApp;
import xdman.downloaders.metadata.DashMetadata;
import xdman.downloaders.metadata.HdsMetadata;
import xdman.downloaders.metadata.HlsMetadata;
import xdman.downloaders.metadata.HttpMetadata;
import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.ui.res.StringResource;
import xdman.util.Logger;
import xdman.util.StringUtils;

public class RefreshUrlPage extends Page implements LinkRefreshCallback {
	private static RefreshUrlPage page;
	private HttpMetadata md;
	private JButton btnOpenPage;
	private JTextArea lblMonitoringTitle;

	private RefreshUrlPage(XDMFrame xframe) {
		super(StringResource.get("REF_TITLE"), 350, xframe);
		initUI();
	}

	public void setDetails(HttpMetadata md) {
		this.md = md;
		if (StringUtils.isNullOrEmptyOrBlank(md.getYdlUrl())) {
			btnOpenPage.setVisible(md.getHeaders().containsHeader("referer"));
		} else {
			btnOpenPage.setVisible(true);
		}
		System.out.println("ydlurl: " + md.getYdlUrl());
		lblMonitoringTitle.setText(StringUtils.isNullOrEmptyOrBlank(md.getYdlUrl()) ? StringResource.get("REF_DESC1")
				: StringResource.get("REF_DESC2"));

	}

	private void initUI() {
		int y = 0;
		int h = 0;
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setOpaque(false);
		y += 10;

		h = 40;
		JLabel lblMaxTitle = new JLabel(StringResource.get("REF_WAITING_FOR_LINK"));
		lblMaxTitle.setForeground(Color.WHITE);
		lblMaxTitle.setFont(FontResource.getItemFont());
		lblMaxTitle.setBounds(15, y, 350 - 30, h);
		panel.add(lblMaxTitle);
		y += h;
		y += 10;

		h = 80;

		lblMonitoringTitle = new JTextArea();
		lblMonitoringTitle.setOpaque(false);
		lblMonitoringTitle.setWrapStyleWord(true);
		lblMonitoringTitle.setLineWrap(true);
		lblMonitoringTitle.setEditable(false);
		lblMonitoringTitle.setForeground(Color.WHITE);
		lblMonitoringTitle.setFont(FontResource.getNormalFont());
		lblMonitoringTitle.setBounds(15, y, 350 - 30, h);
		panel.add(lblMonitoringTitle);
		y += h;

		btnOpenPage = createButton1("REF_OPEN_PAGE", 15, y);
		btnOpenPage.setName("REF_OPEN_PAGE");
		panel.add(btnOpenPage);
		y += btnOpenPage.getHeight();
		btnOpenPage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!StringUtils.isNullOrEmptyOrBlank(md.getYdlUrl())) {
					openLink();
				}
			}
		});

		panel.setPreferredSize(new Dimension(350, y));
		panel.setBounds(0, 0, 350, y);

		jsp.setViewportView(panel);
	}

	public static RefreshUrlPage getPage(XDMFrame xframe) {
		if (page == null) {
			page = new RefreshUrlPage(xframe);
		}
		return page;
	}

	private JButton createButton1(String name, int x, int y) {
		JButton btn = new JButton(StringResource.get(name));
		btn.setBackground(ColorResource.getDarkBtnColor());
		btn.setFont(FontResource.getNormalFont());
		Dimension d = btn.getPreferredSize();
		btn.setBounds(x, y, d.width, d.height);
		// btn.addActionListener(this);
		return btn;
	}

	@Override
	public String getId() {
		return md.getId();
	}

	@Override
	public boolean isValidLink(HttpMetadata metadata) {
		Logger.log("Checking refresh link with checking size " + md.getSize());
		if (md.getType() == metadata.getType()) {
			if (md instanceof DashMetadata) {
				DashMetadata dm1 = (DashMetadata) md;
				DashMetadata dm2 = (DashMetadata) metadata;
				if (dm1.getLen1() == dm2.getLen1() && dm1.getLen2() == dm2.getLen2()) {
					dm1.setUrl(dm2.getUrl());
					dm1.setUrl2(dm2.getUrl2());
					dm1.setHeaders(dm2.getHeaders());
					dm1.setLen1(dm2.getLen1());
					dm1.setLen2(dm2.getLen2());
					dm1.save();
					showOkMsgAndClose();
					return true;
				}
			} else if (md instanceof HlsMetadata) {
				HlsMetadata hm1 = (HlsMetadata) md;
				HlsMetadata hm2 = (HlsMetadata) metadata;
				if (confirmUrl("")) {
					hm1.setUrl(hm2.getUrl());
					hm1.setHeaders(hm2.getHeaders());
					hm1.save();
					showOkMsgAndClose();
					return true;
				}
			} else if (md instanceof HdsMetadata) {
				HdsMetadata hm1 = (HdsMetadata) md;
				HdsMetadata hm2 = (HdsMetadata) metadata;
				if (confirmUrl("")) {
					hm1.setUrl(hm2.getUrl());
					hm1.setHeaders(hm2.getHeaders());
					hm1.save();
					showOkMsgAndClose();
					return true;
				}
			} else {
				boolean confirmed = false;
				if (md.getSize() > 0) {
					confirmed = md.getSize() == metadata.getSize();
				} else {
					confirmed = confirmUrl(StringResource.get("MSG_REF_LINK_QUESTION"));
				}
				if (confirmed) {
					md.setUrl(metadata.getUrl());
					md.setHeaders(metadata.getHeaders());
					md.save();
					showOkMsgAndClose();
					return true;
				}
			}
		}
		return false;
	}

	private boolean confirmUrl(String msg) {
		return (MessageBox.show(super.getParentFrame(), StringResource.get("MSG_REF_LINK_CONFIRM"), msg,
				MessageBox.YES_NO_OPTION, MessageBox.YES) == MessageBox.YES);
	}

	private void showOkMsgAndClose() {
		MessageBox.show(getParentFrame(), StringResource.get("MSG_REF_LINK_CONFIRM"),
				StringResource.get("MSG_REF_LINK_MSG"), MessageBox.OK, MessageBox.OK);
	}

	@Override
	public void showPanel() {
		super.showPanel();
		XDMApp.getInstance().registerRefreshCallback(this);
	}

	@Override
	public void close() {
		XDMApp.getInstance().unregisterRefreshCallback();
		super.close();
	}

	private void openLink() {
		if (!StringUtils.isNullOrEmptyOrBlank(md.getYdlUrl())) {
			MediaDownloaderWnd wnd = new MediaDownloaderWnd();
			wnd.launchWithUrl(md.getYdlUrl());
		}
	}
}
