package xdman.ui.components;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;

public class LayeredPanel extends JPanel {
	private Color bgColor;

	public LayeredPanel(int opacity) {
		bgColor = new Color(0, 0, 0, opacity);
		setOpaque(false);
		setLayout(null);

		MouseInputAdapter ma = new MouseInputAdapter() {
		};

		addMouseListener(ma);
		addMouseMotionListener(ma);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(bgColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
