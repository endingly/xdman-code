package xdman.ui.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import xdman.Config;
import xdman.XDMApp;
import xdman.ui.res.FontResource;
import xdman.ui.res.StringResource;

public class AboutPage extends Page {
	public AboutPage(XDMFrame xframe) {
		super(StringResource.get("TITLE_ABOUT"), 350, xframe);
		int y = 0;
		int h = 0;
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setOpaque(false);
		y += 10;
		h = 50;

		JLabel lblTitle = new JLabel(StringResource.get("FULL_NAME"));
		lblTitle.setFont(FontResource.getBiggerFont());
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setBounds(15, y, 350 - 30, h);
		panel.add(lblTitle);

		y += h;
		y += 20;

		String details = String.format(StringResource.get("ABOUT_DETAILS"), XDMApp.APP_VERSION,
				System.getProperty("java.version"), System.getProperty("os.name"), "http://xdman.sourceforge.net");

		h = 250;
		JTextArea lblDetails = new JTextArea();
		lblDetails.setOpaque(false);
		lblDetails.setWrapStyleWord(true);
		lblDetails.setLineWrap(true);
		lblDetails.setEditable(false);
		lblDetails.setForeground(Color.WHITE);
		lblDetails.setText(details);
		lblDetails.setFont(FontResource.getBigFont());
		lblDetails.setBounds(15, y, 350 - 30, h);
		panel.add(lblDetails);
		y += h;

		panel.setPreferredSize(new Dimension(350, y));
		panel.setBounds(0, 0, 350, y);

		jsp.setViewportView(panel);
	}
}
