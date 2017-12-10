package xdman.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;

import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.ui.res.ImageResource;
import xdman.ui.res.StringResource;
import xdman.util.FormatUtilities;

public class PropertiesPage extends Page {
	private static PropertiesPage propPage;
	private JTextField txtDefFile, txtDefFolder, txtUrl;
	private JLabel lblSize, lblDate, lblType, lblReferer;
	JTextArea txtCookie;

	public void setDetails(String file, String folder, long size, String url, String referer, String date,
			String cookies, String type) {
		this.txtDefFile.setText(file);
		this.txtDefFolder.setText(folder);
		this.txtUrl.setText(url);
		this.lblSize.setText(FormatUtilities.formatSize(size));
		this.lblDate.setText(date);
		this.txtCookie.setText(cookies);
		this.lblType.setText(type);
		this.lblReferer.setText(referer);
	}

	private PropertiesPage(XDMFrame xframe) {
		super(StringResource.get("TITLE_PROP"), 350, xframe);
		initUI();
	}

	public static PropertiesPage getPage(XDMFrame xframe) {
		if (propPage == null) {
			propPage = new PropertiesPage(xframe);
		}
		return propPage;
	}

	private void initUI() {
		int y = 0;
		int h = 0;
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setOpaque(false);
		y += 10;
		h = 30;
		JLabel lblFileTitle = new JLabel(StringResource.get("ND_FILE"));
		lblFileTitle.setForeground(Color.WHITE);
		lblFileTitle.setFont(FontResource.getNormalFont());
		lblFileTitle.setBounds(15, y, 350 - 30, h);
		panel.add(lblFileTitle);
		y += h;
		h = 25;
		txtDefFile = new JTextField();
		txtDefFile.setBounds(15, y, 350 - 50, h);
		txtDefFile.setBorder(new LineBorder(ColorResource.getDarkBtnColor()));
		txtDefFile.setEditable(false);
		txtDefFile.setForeground(Color.WHITE);
		txtDefFile.setOpaque(false);
		panel.add(txtDefFile);
		y += h;

		h = 30;
		JLabel lblFolderTitle = new JLabel(StringResource.get("CD_LOC"));
		lblFolderTitle.setForeground(Color.WHITE);
		lblFolderTitle.setFont(FontResource.getNormalFont());
		lblFolderTitle.setBounds(15, y, 350 - 50, h);
		panel.add(lblFolderTitle);
		y += h;
		h = 25;
		txtDefFolder = new JTextField();
		txtDefFolder.setBounds(15, y, 350 - 50, h);
		txtDefFolder.setBorder(new LineBorder(ColorResource.getDarkBtnColor()));
		txtDefFolder.setEditable(false);
		txtDefFolder.setForeground(Color.WHITE);
		txtDefFolder.setOpaque(false);
		panel.add(txtDefFolder);
		y += h;

		h = 30;
		JLabel lblUrlTitle = new JLabel(StringResource.get("ND_ADDRESS"));
		lblUrlTitle.setForeground(Color.WHITE);
		lblUrlTitle.setFont(FontResource.getNormalFont());
		lblUrlTitle.setBounds(15, y, 350 - 50, h);
		panel.add(lblUrlTitle);
		y += h;
		h = 25;
		txtUrl = new JTextField();
		txtUrl.setBounds(15, y, 350 - 50, h);
		txtUrl.setBorder(new LineBorder(ColorResource.getDarkBtnColor()));
		txtUrl.setEditable(false);
		txtUrl.setForeground(Color.WHITE);
		txtUrl.setOpaque(false);
		panel.add(txtUrl);
		y += h;

		h = 30;
		JLabel lblSizeLabel = new JLabel(StringResource.get("PROP_SIZE"));
		lblSizeLabel.setForeground(Color.WHITE);
		lblSizeLabel.setFont(FontResource.getNormalFont());
		lblSizeLabel.setBounds(15, y, 100, h);
		panel.add(lblSizeLabel);

		lblSize = new JLabel();
		lblSize.setForeground(Color.WHITE);
		lblSize.setFont(FontResource.getNormalFont());
		lblSize.setBounds(115, y, 200, h);
		panel.add(lblSize);
		y += h;

		h = 30;
		JLabel lblDateLabel = new JLabel(StringResource.get("PROP_DATE"));
		lblDateLabel.setForeground(Color.WHITE);
		lblDateLabel.setFont(FontResource.getNormalFont());
		lblDateLabel.setBounds(15, y, 100, h);
		panel.add(lblDateLabel);

		lblDate = new JLabel();
		lblDate.setForeground(Color.WHITE);
		lblDate.setFont(FontResource.getNormalFont());
		lblDate.setBounds(115, y, 200, h);
		panel.add(lblDate);
		y += h;

		h = 30;
		JLabel lblTypeLabel = new JLabel(StringResource.get("PROP_TYPE"));
		lblTypeLabel.setForeground(Color.WHITE);
		lblTypeLabel.setFont(FontResource.getNormalFont());
		lblTypeLabel.setBounds(15, y, 100, h);
		panel.add(lblTypeLabel);

		lblType = new JLabel();
		lblType.setForeground(Color.WHITE);
		lblType.setFont(FontResource.getNormalFont());
		lblType.setBounds(115, y, 200, h);
		panel.add(lblType);
		y += h;

		h = 30;
		JLabel lblRefererLabel = new JLabel(StringResource.get("PROP_REFERER"));
		lblRefererLabel.setForeground(Color.WHITE);
		lblRefererLabel.setFont(FontResource.getNormalFont());
		lblRefererLabel.setBounds(15, y, 100, h);
		panel.add(lblRefererLabel);

		lblReferer = new JLabel();
		lblReferer.setForeground(Color.WHITE);
		lblReferer.setFont(FontResource.getNormalFont());
		lblReferer.setBounds(115, y, 200, h);
		panel.add(lblReferer);
		y += h;
		h = 30;
		JLabel lblCookieTitle = new JLabel(StringResource.get("PROP_COOKIE"));
		lblCookieTitle.setForeground(Color.WHITE);
		lblCookieTitle.setFont(FontResource.getNormalFont());
		lblCookieTitle.setBounds(15, y, 350 - 30, h);
		panel.add(lblCookieTitle);
		y += h;
		y += 10;
		h = 120;

		txtCookie = new JTextArea();
		txtCookie.setBounds(15, y, 350 - 50, h);
		txtCookie.setBorder(new LineBorder(ColorResource.getDarkBtnColor()));
		txtCookie.setEditable(false);
		txtCookie.setForeground(Color.WHITE);
		txtCookie.setOpaque(false);
		panel.add(txtCookie);
		y += h;
		y += 50;

		panel.setPreferredSize(new Dimension(350, y));
		panel.setBounds(0, 0, 350, y);

		jsp.setViewportView(panel);
	}
}
