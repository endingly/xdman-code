package xdman.ui.components;

import javax.swing.table.*;

import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.ui.res.ImageResource;
import xdman.videoparser.YdlResponse.YdlVideo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class VideoItemRenderer implements TableCellRenderer {
	private JPanel panel;
	private JPanel component;
	private JLabel lbl;
	// private JLabel stat;
	private JLabel lblIcon;
	private JComboBox<String> cmb;
	private DefaultComboBoxModel<String> cmbModel;

	public VideoItemRenderer() {
		component = new JPanel(new BorderLayout());
		component.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel = new JPanel(new BorderLayout());
		lblIcon = new JLabel();
		lblIcon.setIcon(ImageResource.get("video.png"));
		lblIcon.setBorder(new EmptyBorder(5, 5, 5, 10));
		lblIcon.setVerticalAlignment(JLabel.CENTER);
		// lblIcon.setPreferredSize(new Dimension(53, 53));
		component.add(lblIcon, BorderLayout.WEST);
		lbl = new JLabel();
		lbl.setFont(FontResource.getItemFont());
		panel.add(lbl);
		cmbModel = new DefaultComboBoxModel<>();
		cmb = new JComboBox<>(cmbModel);
		cmb.setPreferredSize(new Dimension(200, 20));
		cmb.setOpaque(false);
		cmb.setBorder(null);
		panel.add(cmb, BorderLayout.SOUTH);
		panel.setOpaque(false);
		component.add(panel);
		component.setBackground(ColorResource.getSelectionColor());
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		YdlVideo obj = (YdlVideo) value;
		lbl.setText(obj.title);
		// stat.setText(obj.mediaFormats.get(obj.index) + "");
		cmbModel.removeAllElements();
		cmbModel.addElement(obj.mediaFormats.get(obj.index) + "");
		component.setBackground(isSelected ? ColorResource.getSelectionColor() : ColorResource.getDarkestBgColor());
		return component;
	}
}
