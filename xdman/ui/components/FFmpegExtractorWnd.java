package xdman.ui.components;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import xdman.mediaconversion.FFmpeg;
import xdman.mediaconversion.MediaConversionListener;
import xdman.mediaconversion.MediaFormat;
import xdman.mediaconversion.MediaFormats;
import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.ui.res.ImageResource;
import xdman.ui.res.StringResource;
import xdman.util.FFExtractCallback;
import xdman.util.FormatUtilities;
import xdman.util.Logger;

public class FFmpegExtractorWnd extends JFrame implements ActionListener {
	private JPanel titlePanel, panel;
	private JButton closeBtn, minBtn;
	private JLabel titleLbl;
	private JProgressBar prg;
	private JLabel statLbl;
	private int lastProgress;
	private long prevTime;
	private CustomButton btnCN;
	private FFExtractCallback callback;

	public FFmpegExtractorWnd(FFExtractCallback callback) {
		init();
		this.callback = callback;
	}

	public void progress(int progress) {
		if (progress >= prg.getMinimum() && progress <= prg.getMaximum()) {
			prg.setValue(progress);
		}

		int prgDiff = progress - lastProgress;
		long now = System.currentTimeMillis();
		long timeSpend = now - prevTime;
		if (timeSpend > 0) {
			if (prgDiff > 0) {
				long eta = (timeSpend * (100 - progress) / 1000 * prgDiff);// prgDiff
				lastProgress = progress;
				statLbl.setText("ETA: " + FormatUtilities.hms((int) eta));
			}
			prevTime = now;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComponent) {
			String name = ((JComponent) e.getSource()).getName();
			if (name == null) {
				return;
			}
			if (name.equals("CLOSE")) {
				stop();
			}
			if (name.equals("MIN")) {
				this.setExtendedState(this.getExtendedState() | JFrame.ICONIFIED);
			}
		}
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

		setIconImage(ImageResource.get("icon.png").getImage());
		setSize(350, 200);
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
		closeBtn.setName("CLOSE");
		closeBtn.addActionListener(this);

		minBtn = new CustomButton();
		minBtn.setBounds(296, 5, 24, 24);
		minBtn.setIcon(ImageResource.get("min_btn.png"));
		minBtn.setBackground(ColorResource.getDarkestBgColor());
		minBtn.setBorderPainted(false);
		minBtn.setFocusPainted(false);
		minBtn.setName("MIN");
		minBtn.addActionListener(this);

		titleLbl = new JLabel(StringResource.get("TITLE_CONVERT"));
		titleLbl.setFont(FontResource.getBiggerFont());
		titleLbl.setForeground(ColorResource.getSelectionColor());
		titleLbl.setBounds(25, 15, 250, 30);

		JLabel lineLbl = new JLabel();
		lineLbl.setBackground(ColorResource.getSelectionColor());
		lineLbl.setBounds(0, 55, 400, 2);
		lineLbl.setOpaque(true);

		prg = new JProgressBar();
		prg.setBounds(20, 85, 350 - 40, 5);

		statLbl = new JLabel();
		statLbl.setForeground(Color.WHITE);
		statLbl.setBounds(20, 100, 350 - 40, 25);

		titlePanel.add(titleLbl);
		titlePanel.add(minBtn);
		titlePanel.add(closeBtn);

		add(lineLbl);
		add(titlePanel);
		add(prg);
		add(statLbl);

		panel = new JPanel(null);
		panel.setBounds(0, 150, 350, 50);
		panel.setBackground(Color.DARK_GRAY);

		btnCN = new CustomButton(StringResource.get("MENU_PAUSE"));
		btnCN.setBounds(0, 1, 350, 50);
		btnCN.setName("CLOSE");
		applyStyle(btnCN);
		panel.add(btnCN);
		add(panel);
	}

	private void applyStyle(CustomButton btn) {
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

	public void finished(int ret) {
		dispose();
	}

	private void stop() {
		try {
			if (callback != null) {
				callback.stop();
				callback = null;
			}
		} catch (Exception e) {
			Logger.log(e);
		}
		dispose();
	}
}
