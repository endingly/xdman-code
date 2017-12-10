package xdman.ui.components;

import javax.swing.JButton;
import javax.swing.JFrame;

public class PopupNotification extends JFrame {
	PopupNotification() {
		setFocusableWindowState(false);
		setAlwaysOnTop(true);
		add(new JButton("test"));
		setSize(300, 100);
		setVisible(true);
	}
}
