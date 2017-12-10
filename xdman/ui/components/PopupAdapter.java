package xdman.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import xdman.ui.res.StringResource;

public class PopupAdapter extends MouseAdapter implements ActionListener {
	private JTextField txt;
	private JPopupMenu popup;
	private static PopupAdapter adapter;

	public static void registerTxtPopup(JTextField txt) {
		if (adapter == null) {
			adapter = new PopupAdapter();
			adapter.init();
		}
		txt.addMouseListener(adapter);
	}

	private void init() {
		popup = new JPopupMenu();
		popup.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				txt = null;
				System.out.println("set to null");
			}
		});
		JMenuItem menuCut = new JMenuItem(StringResource.get("CTX_CUT"));
		menuCut.addActionListener(this);
		menuCut.setName("MENU_CUT");
		JMenuItem menuCopy = new JMenuItem(StringResource.get("CTX_COPY"));
		menuCopy.addActionListener(this);
		menuCopy.setName("MENU_COPY");
		JMenuItem menuSelect = new JMenuItem(StringResource.get("CTX_SELECT_ALL"));
		menuSelect.addActionListener(this);
		menuSelect.setName("MENU_SELECT_ALL");
		JMenuItem menuPaste = new JMenuItem(StringResource.get("CTX_PASTE"));
		menuPaste.setName("MENU_PASTE");
		menuPaste.addActionListener(this);
		popup.add(menuCut);
		popup.add(menuCopy);
		popup.add(menuSelect);
		popup.add(menuPaste);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			if (e.getSource() instanceof JTextField) {
				this.txt = (JTextField) e.getSource();
				popup.show(txt, e.getX(), e.getY());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (txt == null)
			return;
		System.out.println(txt);
		String name = ((JComponent) e.getSource()).getName();
		if ("MENU_CUT".equals(name)) {
			txt.cut();
		} else if ("MENU_COPY".equals(name)) {
			txt.copy();
		} else if ("MENU_SELECT_ALL".equals(name)) {
			txt.selectAll();
		} else if ("MENU_PASTE".equals(name)) {
			txt.paste();
		}
		System.out.println("set to null");
		txt = null;
	}
}
