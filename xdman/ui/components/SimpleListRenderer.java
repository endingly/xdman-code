package xdman.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;

import java.awt.*;

public class SimpleListRenderer extends JLabel implements
		ListCellRenderer<Object> {

	public SimpleListRenderer() {
		setForeground(Color.WHITE);
		setFont(FontResource.getNormalFont());
		setOpaque(true);
		setPreferredSize(new Dimension(100, 30));
		setBorder(new EmptyBorder(0, 5, 0, 0));
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(ColorResource.getSelectionColor());
		} else {
			setBackground(ColorResource.getDarkerBgColor());
		}
		setText(value == null ? "" : value.toString());
		return this;
	}

}
