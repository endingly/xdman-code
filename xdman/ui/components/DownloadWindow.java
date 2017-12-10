package xdman.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.MouseInputAdapter;

import xdman.DownloadWindowListener;
import xdman.XDMApp;
import xdman.XDMConstants;
import xdman.downloaders.Downloader;
import xdman.downloaders.SegmentDetails;
import xdman.downloaders.http.HttpDownloader;
import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.ui.res.ImageResource;
import xdman.ui.res.StringResource;
import xdman.util.FormatUtilities;
import xdman.util.Logger;

public class DownloadWindow extends JFrame implements ActionListener {
	private String id;
	private CircleProgressBar prgCircle;
	private SegmentPanel segProgress;
	private int errCode, reason;
	private String errMsg;
	private JLabel titleLbl;
	private JLabel lblSpeed;
	private JLabel lblStat;
	private JLabel lblDet;
	private JLabel lblETA;
	private JPanel titlePanel;
	private JTextArea txtError;
	private JPanel panel;
	private JButton closeBtn, minBtn;
	private DownloadWindowListener listener;

	public DownloadWindow(String id, DownloadWindowListener listener) {
		this.id = id;
		this.listener = listener;
		init();
	}

	public void close(int code, int error) {
		this.errCode = error;
		this.reason = code;
		this.listener = null;
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (reason == XDMConstants.FAILED) {
					createP2();
					showErrorMsg(errCode);
				} else {
					dispose();
				}
			}
		});
	}

	public void update(Downloader d, String file) {
		titleLbl.setText(file);
		lblStat.setText(d.isAssembling() ? StringResource.get("STAT_ASSEMBLING") : StringResource.get("DWN_TITLE"));
		// StringBuilder sb = new StringBuilder();
		// sb.append((d.isAssembling() ? StringResource.get("STAT_ASSEMBLING")
		// : StringResource.get("DWN_DOWNLOAD")));
		// sb.append(" ");
		// sb.append(FormatUtilities.formatSize(d.getDownloaded()));
		// sb.append(" ");
		// sb.append(d.getType()==XDMConstants.HTTP?)
		lblDet.setText((d.isAssembling() ? StringResource.get("STAT_ASSEMBLING") : StringResource.get("DWN_DOWNLOAD"))
				+ " " + FormatUtilities.formatSize(d.getDownloaded()) + " "
				+ ((d.getType() == XDMConstants.HTTP || d.getType() == XDMConstants.DASH)
						? "/ " + FormatUtilities.formatSize(d.getSize())
						: "( " + d.getProgress() + " % )"));
		lblSpeed.setText(FormatUtilities.formatSize(d.getDownloadSpeed()) + "/s");
		lblETA.setText("ETA " + d.getEta());
		prgCircle.setValue(d.getProgress());
		SegmentDetails segDet = d.getSegmentDetails();
		long sz = ((d.getType() == XDMConstants.HTTP || d.getType() == XDMConstants.DASH) ? d.getSize() : 100);
		segProgress.setValues(segDet, sz);
	}

	private void createP2() {

		remove(prgCircle);
		remove(lblSpeed);
		remove(lblStat);
		remove(segProgress);
		remove(lblDet);
		remove(lblETA);
		remove(this.panel);

		titlePanel.remove(closeBtn);
		titlePanel.remove(minBtn);

		JPanel p2 = new JPanel(null);
		p2.setBounds(0, 60, 350, 190);
		p2.setBackground(ColorResource.getDarkestBgColor());

		txtError = new JTextArea(this.errMsg);
		txtError.setFont(FontResource.getBigFont());
		txtError.setEditable(false);
		txtError.setCaretPosition(0);
		txtError.setWrapStyleWord(true);
		txtError.setLineWrap(true);
		txtError.setBackground(ColorResource.getDarkestBgColor());
		txtError.setForeground(Color.WHITE);

		JScrollPane jsp = new JScrollPane(txtError);
		jsp.setBounds(25, 20, 300, 100);
		jsp.setBorder(null);

		CustomButton exitBtn = new CustomButton();
		exitBtn.setText(StringResource.get("MSG_OK"));
		applyStyle(exitBtn);
		exitBtn.setBounds(0, 1, 350, 50);
		exitBtn.setName("EXIT");

		JPanel panel2 = new JPanel(null);
		panel2.setBounds(0, 140, 350, 50);
		panel2.setBackground(Color.DARK_GRAY);
		panel2.add(exitBtn);

		p2.add(jsp);
		p2.add(panel2);

		add(p2);

		titleLbl.setText(StringResource.get("MSG_FAILED"));

		invalidate();
		repaint();
	}

	private void init() {
		setUndecorated(true);
		try {
			if (GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
					.isWindowTranslucencySupported(WindowTranslucency.TRANSLUCENT)) {
				setOpacity(0.85f);
			}
		} catch (Exception e) {
			Logger.log(e);
		}

		setTitle("Downloading...");
		setIconImage(ImageResource.get("icon.png").getImage());
		setSize(350, 250);
		setLocationRelativeTo(null);
		setResizable(false);

		getContentPane().setLayout(null);
		getContentPane().setBackground(ColorResource.getDarkestBgColor());

		titlePanel = new TitlePanel(null, this);
		titlePanel.setOpaque(false);
		titlePanel.setBounds(0, 0, 350, 50);

		closeBtn = new CustomButton();
		closeBtn.setBounds(320, 5, 24, 24);
		closeBtn.setIcon(ImageResource.get("close_btn.png"));
		closeBtn.setBackground(ColorResource.getDarkestBgColor());
		closeBtn.setBorderPainted(false);
		closeBtn.setFocusPainted(false);
		closeBtn.setName("PAUSE");
		closeBtn.addActionListener(this);

		minBtn = new CustomButton();
		minBtn.setBounds(296, 5, 24, 24);
		minBtn.setIcon(ImageResource.get("min_btn.png"));
		minBtn.setBackground(ColorResource.getDarkestBgColor());
		minBtn.setBorderPainted(false);
		minBtn.setFocusPainted(false);
		minBtn.setName("MIN");
		minBtn.addActionListener(this);

		titleLbl = new JLabel(StringResource.get("DWN_TITLE"));
		titleLbl.setFont(FontResource.getBiggerFont());
		titleLbl.setForeground(ColorResource.getSelectionColor());
		titleLbl.setBounds(25, 15, 250, 30);

		JLabel lineLbl = new JLabel();
		lineLbl.setBackground(ColorResource.getSelectionColor());
		lineLbl.setBounds(0, 55, 400, 2);
		lineLbl.setOpaque(true);

		prgCircle = new CircleProgressBar();
		prgCircle.setValue(0);

		prgCircle.setBounds(20, 80, 72, 72);

		titlePanel.add(titleLbl);
		titlePanel.add(minBtn);
		titlePanel.add(closeBtn);

		lblSpeed = new JLabel("---");
		lblSpeed.setHorizontalAlignment(JLabel.CENTER);
		lblSpeed.setBounds(15, 160, 80, 25);
		lblSpeed.setForeground(Color.WHITE);

		lblStat = new JLabel(StringResource.get("DWN_TITLE"));
		lblStat.setBounds(120, 85, 200, 25);
		lblStat.setForeground(Color.WHITE);

		segProgress = new SegmentPanel();
		segProgress.setBounds(120, 115, 200, 5);

		lblDet = new JLabel(StringResource.get("DWN_PLACEHOLDER"));
		lblDet.setBounds(120, 125, 200, 25);
		lblDet.setForeground(Color.WHITE);

		lblETA = new JLabel("---");
		lblETA.setBounds(120, 150, 200, 25);
		lblETA.setForeground(Color.WHITE);

		panel = new JPanel(null);
		panel.setBounds(0, 200, 350, 50);
		panel.setBackground(Color.DARK_GRAY);

		CustomButton btnMore = new CustomButton(StringResource.get("DWN_HIDE"));
		// CustomButton btnDN = new
		// CustomButton(StringResource.get("DWN_PREVIEW"));
		CustomButton btnCN = new CustomButton(StringResource.get("MENU_PAUSE"));

		btnMore.setBounds(0, 1, 175, 50);
		btnMore.setName("BACKGROUND");
		applyStyle(btnMore);

		// btnDN.setBounds(101, 1, 144, 50);
		// btnDN.setName("PREVIEW");
		// applyStyle(btnDN);

		btnCN.setBounds(176, 1, 175, 50);
		btnCN.setName("PAUSE");
		applyStyle(btnCN);

		add(titlePanel);
		add(lineLbl);
		add(prgCircle);
		add(lblSpeed);
		add(lblStat);
		add(segProgress);
		add(lblDet);
		add(lblETA);

		panel.add(btnMore);
		// panel.add(btnDN);
		panel.add(btnCN);

		add(panel);

	}

	void applyStyle(CustomButton btn) {
		btn.addActionListener(this);
		btn.setBackground(ColorResource.getDarkestBgColor());
		btn.setForeground(Color.WHITE);
		btn.setPressedBackground(ColorResource.getDarkerBgColor());
		btn.setFont(FontResource.getBigFont());
		btn.setBorderPainted(false);
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setFocusPainted(false);
		btn.setFocusPainted(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String name = ((JComponent) e.getSource()).getName();
		if ("PAUSE".equals(name)) {
			pause();
		} else if ("BACKGROUND".equals(name)) {
			hideWnd();
		} else if ("PREVIEW".equals(name)) {
			openPreviewFolder();
		} else if ("MIN".equals(name)) {
			this.setExtendedState(this.getExtendedState() | JFrame.ICONIFIED);
		} else if ("EXIT".equals(name)) {
			dispose();
		}
	}

	private void pause() {
		if (listener != null) {
			listener.pauseDownload(id);
		}
	}

	private void hideWnd() {
		if (listener != null) {
			listener.hidePrgWnd(id);
		}
	}

	private void openPreviewFolder() {

	}

	private void showErrorMsg(int code) {
		switch (code) {
		case XDMConstants.ERR_CONN_FAILED:
			txtError.setText(StringResource.get("ERR_CONN_FAILED"));
			return;
		case XDMConstants.ERR_SESSION_FAILED:
			txtError.setText(StringResource.get("ERR_SESSION_FAILED"));
			return;
		case XDMConstants.ERR_NO_RESUME:
			txtError.setText(StringResource.get("ERR_NO_RESUME"));
			return;
		case XDMConstants.ERR_INVALID_RESP:
			txtError.setText(StringResource.get("ERR_INVALID_RESP"));
			return;
		case XDMConstants.ERR_ASM_FAILED:
			txtError.setText(StringResource.get("ERR_ASM_FAILED"));
			return;
		case XDMConstants.RESUME_FAILED:
			txtError.setText(StringResource.get("RESUME_FAILED"));
			return;
		case XDMConstants.DISK_FAIURE:
			txtError.setText(StringResource.get("ERR_DISK_FAILED"));
			return;
		default:
			txtError.setText(StringResource.get("ERR_INTERNAL"));
			return;
		}
	}
}