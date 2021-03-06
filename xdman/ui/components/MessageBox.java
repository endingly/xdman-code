package xdman.ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;

import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.ui.res.StringResource;

public class MessageBox extends JPanel implements ActionListener {
	private static MessageBox msgBox;
	public static final int OK = 10, YES = 20, NO = 30;
	public static final int OK_OPTION = 10, YES_NO_OPTION = 20;
	private JTextArea txtMessage;
	private JLabel lblTitle;
	private int diffx, diffy;
	private XDMFrame parent;
	private CustomButton cbBtnOk, cbBtnYes, cbBtnNo;
	private JPanel panel2, panel3;
	private int res;
	private static MsgBoxFocusTraversalPolicy focusPolicy;
	private int defaultButton;

	private MessageBox() {
		setLayout(null);
		MouseInputAdapter ma = new MouseInputAdapter() {
		};
		addMouseListener(ma);
		addMouseMotionListener(ma);
		init();
	}

	public static int show(XDMFrame parent, String title, String msg, int buttons, int defaultButton) {
		if (msgBox == null) {
			msgBox = new MessageBox();
		}

		msgBox.parent = parent;
		msgBox.lblTitle.setText(title);
		msgBox.txtMessage.setText(msg);
		msgBox.setLocation((parent.getWidth() - 350) / 2, (parent.getHeight() - 210) / 2);

		if (buttons == OK_OPTION) {
			msgBox.panel2.setVisible(false);
			msgBox.panel3.setVisible(true);
		} else {
			msgBox.panel2.setVisible(true);
			msgBox.panel3.setVisible(false);
		}
		msgBox.defaultButton = defaultButton;
		parent.showModal(msgBox);
		return msgBox.res;
	}

	public void selectDefaultButton() {
		if (defaultButton == YES) {
			msgBox.cbBtnYes.requestFocusInWindow();
		} else if (defaultButton == NO) {
			msgBox.cbBtnNo.requestFocusInWindow();
		} else if (defaultButton == OK) {
			msgBox.cbBtnOk.requestFocusInWindow();
		}
	}

	private void init() {
		lblTitle = new JLabel();
		txtMessage = new JTextArea();
		txtMessage.setWrapStyleWord(true);
		txtMessage.setLineWrap(true);
		txtMessage.setBackground(ColorResource.getDarkerBgColor());
		txtMessage.setForeground(Color.WHITE);
		txtMessage.setBorder(new EmptyBorder(new Insets(10, 10, 10, 30)));
		txtMessage.setEditable(false);

		setBackground(ColorResource.getDarkerBgColor());
		setBounds(0, 0, 350, 210);

		lblTitle.setBounds(25, 15, 300, 30);
		lblTitle.setFont(FontResource.getItemFont());
		lblTitle.setForeground(ColorResource.getSelectionColor());

		JLabel lineLbl = new JLabel();
		lineLbl.setBackground(ColorResource.getSelectionColor());
		lineLbl.setBounds(0, 52, 350, 2);
		lineLbl.setOpaque(true);

		JScrollPane jsp = new JScrollPane(txtMessage);

		JScrollBar sc1 = new DarkScrollBar(JScrollBar.VERTICAL);
		// sc1.putClientProperty("Scrollbar.darkMode", new Integer(1));

		jsp.setVerticalScrollBar(sc1);

		jsp.setBounds(0, 54, 350, 106);
		jsp.setBorder(null);

		panel2 = new JPanel(null);
		panel2.setBounds(0, 160, 350, 50);
		panel2.setBackground(ColorResource.getDarkBgColor());

		panel3 = new JPanel(null);
		panel3.setBounds(0, 160, 350, 50);
		panel3.setBackground(ColorResource.getDarkBgColor());

		cbBtnOk = new CustomButton(StringResource.get("MB_OK"));
		cbBtnYes = new CustomButton(StringResource.get("MB_YES"));
		cbBtnNo = new CustomButton(StringResource.get("MB_NO"));

		cbBtnOk.setBounds(0, 1, 350, 50);
		cbBtnOk.setName("MB_OK");
		applyStyle(cbBtnOk);

		cbBtnYes.setBounds(0, 1, 174, 50);
		cbBtnYes.setName("MB_YES");
		cbBtnYes.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				cbBtnYes.setMnemonic(-1);
			}

			@Override
			public void focusGained(FocusEvent e) {
				cbBtnYes.setMnemonic(KeyEvent.VK_Y);
			}
		});
		applyStyle(cbBtnYes);

		cbBtnNo.setBounds(175, 1, 175, 50);
		cbBtnNo.setName("MB_NO");
		cbBtnNo.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				cbBtnNo.setMnemonic(-1);
			}

			@Override
			public void focusGained(FocusEvent e) {
				cbBtnNo.setMnemonic(KeyEvent.VK_N);
			}
		});
		applyStyle(cbBtnNo);

		panel3.add(cbBtnOk);
		panel2.add(cbBtnYes);
		panel2.add(cbBtnNo);

		add(lblTitle);
		add(lineLbl);
		add(jsp);
		add(panel2);
		add(panel3);

		registerMouseListener();
		Vector<Component> order = new Vector<>();
		order.add(cbBtnYes);
		order.add(cbBtnNo);
		focusPolicy = new MsgBoxFocusTraversalPolicy(order);
		setFocusCycleRoot(true);
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(focusPolicy);
	}

	public static MsgBoxFocusTraversalPolicy getFocusPolicy() {
		return focusPolicy;
	}

	public static void setFocusPolicy(MsgBoxFocusTraversalPolicy fp) {
		focusPolicy = fp;
	}

	void applyStyle(JButton btn) {
		btn.addActionListener(this);
		btn.setBackground(ColorResource.getDarkerBgColor());// );
		btn.setForeground(Color.WHITE);
		btn.setFocusable(true);
		// btn.setForeground(Color.WHITE);
		btn.setFont(FontResource.getBigFont());
		btn.setBorderPainted(false);
		btn.setMargin(new Insets(0, 0, 0, 0));
		// btn.setFocusPainted(false);
		btn.getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		btn.getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke("released ENTER"), "released");
	}

	private void updatePositions() {

	}

	public void registerMouseListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				diffx = me.getXOnScreen() - getLocationOnScreen().x + parent.getLocationOnScreen().x;
				diffy = me.getYOnScreen() - getLocationOnScreen().y + parent.getLocationOnScreen().y;
				// diffx = me.getX(); // - panel.getLocation().x;
				// diffy = me.getY(); // - panel.getLocation().y;
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent me) {
				int left = me.getXOnScreen() - diffx;
				int top = me.getYOnScreen() - diffy;
				int right = left + getWidth();
				int bottom = top + getHeight();
				if (parent.contains(left, top) && parent.contains(right, bottom)) {
					setLocation(left, top);
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cbBtnOk) {
			res = OK;
		} else if (e.getSource() == cbBtnYes) {
			res = YES;
		} else {
			res = NO;
		}
		parent.hideModal(msgBox);
	}

	public static class MsgBoxFocusTraversalPolicy extends FocusTraversalPolicy {
		Vector<Component> order;

		public MsgBoxFocusTraversalPolicy(Vector<Component> order) {
			this.order = new Vector<Component>(order.size());
			this.order.addAll(order);
		}

		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			int idx = (order.indexOf(aComponent) + 1) % order.size();
			return order.get(idx);
		}

		public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
			int idx = order.indexOf(aComponent) - 1;
			if (idx < 0) {
				idx = order.size() - 1;
			}
			return order.get(idx);
		}

		public Component getDefaultComponent(Container focusCycleRoot) {
			return order.get(0);
		}

		public Component getLastComponent(Container focusCycleRoot) {
			return order.lastElement();
		}

		public Component getFirstComponent(Container focusCycleRoot) {
			return order.get(0);
		}
	}

}
