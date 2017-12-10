package xdman.ui.components;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.ui.res.ImageResource;
import xdman.videoparser.YdlResponse.YdlVideo;

public class VideoItemEditor extends AbstractCellEditor implements TableCellEditor {
	private JPanel panel;
	private JLabel lbl;
	private JComboBox<String> cmb;
	private DefaultComboBoxModel<String> cmbModel;
	private YdlVideo obj;
	private JLabel lblIcon;
	private JPanel component;

	public VideoItemEditor() {
		component = new JPanel(new BorderLayout());
		component.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel = new JPanel(new BorderLayout());
		lbl = new JLabel();
		lbl.setFont(FontResource.getItemFont());
		lblIcon = new JLabel();
		lblIcon.setIcon(ImageResource.get("video.png"));
		lblIcon.setBorder(new EmptyBorder(5, 5, 5, 10));
		lblIcon.setVerticalAlignment(JLabel.CENTER);
		// lblIcon.setPreferredSize(new Dimension(53, 53));
		component.add(lblIcon, BorderLayout.WEST);
		cmbModel = new DefaultComboBoxModel<>();
		cmb = new JComboBox<>(cmbModel);
		cmb.setPreferredSize(new Dimension(200, 20));
		cmb.setOpaque(false);
		cmb.setBorder(null);
		panel.add(lbl);
		panel.add(cmb, BorderLayout.SOUTH);
		panel.setOpaque(false);
		component.add(panel);
		component.setBackground(ColorResource.getSelectionColor());
	}

	@Override
	public Object getCellEditorValue() {
		obj.index = cmb.getSelectedIndex();
		System.out.println("value " + obj.title + " " + obj.index);
		return obj;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean isCellEditable(EventObject e) {
		return true;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.obj = (YdlVideo) value;
		lbl.setText(obj.title);
		cmbModel.removeAllElements();
		for (int i = 0; i < obj.mediaFormats.size(); i++)
			cmbModel.addElement(obj.mediaFormats.get(i) + "");
		System.out.println(obj.title + " " + obj.index);
		cmb.setSelectedIndex(obj.index);
		// component.setBackground(isSelected ? ColorResource.getSelectionColor() :
		// ColorResource.getDarkestBgColor());
		return component;
	}

}