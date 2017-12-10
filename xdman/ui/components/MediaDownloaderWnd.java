package xdman.ui.components;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import xdman.XDMApp;
import xdman.downloaders.metadata.DashMetadata;
import xdman.downloaders.metadata.HdsMetadata;
import xdman.downloaders.metadata.HlsMetadata;
import xdman.downloaders.metadata.HttpMetadata;
import xdman.network.http.HttpHeader;
import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.ui.res.ImageResource;
import xdman.ui.res.StringResource;
import xdman.util.Logger;
import xdman.util.StringUtils;
import xdman.util.XDMUtils;
import xdman.videoparser.YdlResponse.YdlMediaFormat;
import xdman.videoparser.YdlResponse.YdlVideo;
import xdman.videoparser.YdlResponse;
import xdman.videoparser.YoutubeDLHandler;

public class MediaDownloaderWnd extends JFrame implements ActionListener {

	private JTextField txtURL;
	JButton btnDwn, btnBack, btnQ;
	JButton btnStart;
	JProgressBar prg;
	JScrollPane jsp;
	private boolean stop;
	// DefaultMutableTreeNode rootNode;
	// JTree tree;
	YoutubeDLHandler ydl;
	private VideoTableModel model;
	private JTable table;

	public MediaDownloaderWnd() {
		initUI();
	}

	private void initUI() {
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
		setSize(500, 420);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		getContentPane().setBackground(ColorResource.getDarkestBgColor());

		JPanel titlePanel = new TitlePanel(null, this);
		titlePanel.setOpaque(false);
		titlePanel.setBounds(0, 0, getWidth(), 50);

		JButton closeBtn = new CustomButton();
		closeBtn.setBounds(getWidth() - 35, 5, 30, 30);
		closeBtn.setBackground(ColorResource.getDarkestBgColor());
		closeBtn.setBorderPainted(false);
		closeBtn.setFocusPainted(false);
		closeBtn.setName("CLOSE");

		closeBtn.setIcon(ImageResource.get("close_btn.png"));
		closeBtn.addActionListener(this);
		titlePanel.add(closeBtn);

		JLabel titleLbl = new JLabel(StringResource.get("TITLE_DOWN_VID"));
		titleLbl.setFont(FontResource.getBiggerFont());
		titleLbl.setForeground(ColorResource.getSelectionColor());
		titleLbl.setBounds(25, 15, 200, 30);
		titlePanel.add(titleLbl);

		JLabel lineLbl = new JLabel();
		lineLbl.setBackground(ColorResource.getSelectionColor());
		lineLbl.setBounds(0, 55, getWidth(), 1);
		lineLbl.setOpaque(true);
		add(lineLbl);

		add(titlePanel);

		int y = 55;

		int h = 30;
		y += 15;

		prg = new JProgressBar();
		prg.setIndeterminate(true);
		prg.setBounds(15, y, getWidth() - 30, 5);
		prg.setBorder(null);
		prg.setVisible(false);
		add(prg);

		txtURL = new JTextField();
		PopupAdapter.registerTxtPopup(txtURL);
		txtURL.setBounds(15, y, getWidth() - 30 - 110, h);
		add(txtURL);

		btnStart = createButton("BTN_SEARCH_VIDEO");
		btnStart.setBounds(getWidth() - 15 - 100, y, 100, h);
		btnStart.setName("START");
		add(btnStart);

		VideoDownloadItem item1 = new VideoDownloadItem();
		item1.title = "First item for text test";
		item1.desc = "Sample description for text tesing description";
		h = 300;

		model = new VideoTableModel();
		table = new JTable(model);
		table.setRowHeight(70);
		table.setShowGrid(false);
		table.setOpaque(false);
		table.setBorder(new EmptyBorder(0, 0, 0, 0));
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setDefaultRenderer(YdlVideo.class, new VideoItemRenderer());
		table.setDefaultEditor(YdlVideo.class, new VideoItemEditor());
		table.setTableHeader(null);

		// rootNode = new DefaultMutableTreeNode("Videos");
		// tree = new JTree(rootNode);
		// tree.setOpaque(false);

		jsp = new JScrollPane();
		// jsp = new JScrollPane(list);
		jsp.setBounds(15, y, getWidth() - 30, h);
		jsp.setBorder(new LineBorder(ColorResource.getDarkBgColor()));
		jsp.getViewport().setOpaque(false);
		// jsp.setViewportView(tree);
		jsp.setViewportView(table);
		jsp.setOpaque(false);
		DarkScrollBar scrollBar = new DarkScrollBar(JScrollBar.VERTICAL);
		jsp.setVerticalScrollBar(scrollBar);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(jsp);
		jsp.setVisible(false);

		y += h;
		y += 10;
		h = 30;
		btnDwn = createButton("BTN_DOWNLOAD_NOW");
		btnDwn.setBounds(getWidth() - 15 - 150, y, 150, h);
		btnDwn.setName("DOWNLOAD");
		btnDwn.setVisible(false);
		add(btnDwn);

		btnQ = createButton("BTN_DOWNLOAD_LATER");
		btnQ.setBounds(getWidth() - 15 - 150 - 160, y, 150, h);
		btnQ.setName("BTN_Q");
		add(btnQ);
		btnQ.setVisible(false);

		btnBack = createButton("BTN_BACK");
		btnBack.setBounds(15, y, 130, h);
		btnBack.setName("BACK");
		add(btnBack);
		btnBack.setVisible(false);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				txtURL.requestFocus();
			}
		});

	}

	private JButton createButton(String name) {
		JButton btn = new CustomButton(StringResource.get(name));
		btn.setBackground(ColorResource.getDarkBtnColor());
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setForeground(Color.WHITE);
		btn.setFont(FontResource.getNormalFont());
		btn.addActionListener(this);
		return btn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent c = (JComponent) e.getSource();
		String name = c.getName();
		if ("START".equals(name)) {
			model.clear();
			if (txtURL.getText().length() < 1) {
				JOptionPane.showMessageDialog(this, StringResource.get("MSG_NO_URL"));
				return;
			}
			if(!XDMUtils.checkComponentsInstalled()) {
				JOptionPane.showMessageDialog(this, StringResource.get("LBL_COMPONENT_MISSING"));
				return;
			}
			if (!txtURL.getText().startsWith("http")) {
				txtURL.setText("http://" + txtURL.getText());
			}
			jsp.setVisible(false);
			prg.setVisible(true);
			btnDwn.setVisible(true);
			btnDwn.setText(StringResource.get("BTN_STOP_PROCESSING"));
			btnDwn.setName("STOP");
			btnStart.setVisible(false);
			txtURL.setVisible(false);
			stop = false;
			getVideoItems(txtURL.getText());
		}
		if ("DOWNLOAD".equals(name)) {
			table.getDefaultEditor(YdlVideo.class).stopCellEditing();
			downloadVideo();
		}
		if ("CLOSE".equals(name)) {
			stop();
			dispose();
		}
		if ("STOP".equals(name)) {
			stop();
		}
		if ("BACK".equals(name)) {
			table.getDefaultEditor(YdlVideo.class).stopCellEditing();
			model.clear();
			prg.setVisible(false);
			txtURL.setVisible(true);
			btnStart.setVisible(true);
			btnDwn.setName("DOWNLOAD");
			btnDwn.setText(StringResource.get("BTN_DOWNLOAD_NOW"));
			btnDwn.setVisible(false);
			btnQ.setVisible(false);
			jsp.setVisible(false);
			btnBack.setVisible(false);
		}
	}

	private void stop() {
		prg.setVisible(false);
		txtURL.setVisible(true);
		btnStart.setVisible(true);
		btnDwn.setName("DOWNLOAD");
		btnDwn.setText(StringResource.get("BTN_DOWNLOAD_NOW"));
		btnDwn.setVisible(false);
		btnQ.setVisible(false);
		jsp.setVisible(false);
		btnBack.setVisible(false);
		stop = true;
		if (ydl != null) {
			ydl.stop();
		}
	}

	private void showVideoList() {
		btnStart.setVisible(false);
		txtURL.setVisible(false);
		prg.setVisible(false);
		jsp.setVisible(true);
		btnDwn.setName("DOWNLOAD");
		btnDwn.setText(StringResource.get("BTN_DOWNLOAD_NOW"));
		// http://demo.unified-streaming.com/video/tears-of-steel/tears-of-steel.mp4/.m3u8
		btnDwn.setVisible(true);
		btnBack.setVisible(true);
		// btnQ.setVisible(true);
	}

	private void getVideoItems(final String url) {
		new Thread() {
			@Override
			public void run() {
				try {
					// rootNode.removeAllChildren();
					ydl = new YoutubeDLHandler(url);
					// https://www.youtube.com/watch?v=PMR0ld5h938
					// "C:\\Users\\subhro\\Desktop\\ytdl\\youtube-dl.exe",
					// url);//
					// "https://www.youtube.com/user/koushks");//
					// "https://www.youtube.com/watch?v=Yv2xctJxE-w&list=PL4AFF701184976B25");
					// "http://demo.unified-streaming.com/video/tears-of-steel/tears-of-steel.ism/.m3u8");//
					// "https://www.youtube.com/watch?v=Yv2xctJxE-w&list=PL4AFF701184976B25");
					ydl.start();
					if (ydl.getExitCode() == 0) {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								ArrayList<YdlVideo> list = new ArrayList<>();
								for (int i = 0; i < ydl.getVideos().size(); i++) {
									YdlVideo ydln = ydl.getVideos().get(i);
									if (ydln.mediaFormats == null || ydln.mediaFormats.size() < 1) {
										break;
									}
									list.add(ydln);
									// DefaultMutableTreeNode node = new DefaultMutableTreeNode(ydln.title);
									// rootNode.add(node);
									// for (int j = 0; j < ydln.mediaFormats.size(); j++) {
									// YdlMediaFormat fmt = ydln.mediaFormats.get(j);
									// DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(fmt);
									// node.add(node2);
									// }
								}
								// if (rootNode.getChildCount() > 1) {
								// tree.expandRow(0);
								// } else {
								// tree.expandRow(0);
								// tree.expandRow(1);
								// }
								model.setList(list);
							}
						});
					}
				} catch (Exception e) {
					Logger.log(e);
				}
				try {
					SwingUtilities.invokeAndWait(new Runnable() {

						@Override
						public void run() {
							if (!stop)
								showVideoList();

						}
					});
				} catch (InvocationTargetException |

						InterruptedException e) {
					Logger.log(e);
				}
			}
		}.start();
	}

	private void downloadVideo() {
		for (int index : table.getSelectedRows()) {
			YdlVideo video = (YdlVideo) model.getValueAt(index, 0);
			YdlMediaFormat fmt = video.mediaFormats.get(video.index);
			String title = video.title;
			System.out.println(title + " " + title + fmt.type);
			String file = XDMUtils.getFileName(title) + "." + fmt.ext;
			switch (fmt.type) {
			case YdlResponse.DASH_HTTP:
				DashMetadata dm = new DashMetadata();
				dm.setYdlUrl(txtURL.getText());
				dm.setUrl(fmt.videoSegments[0]);
				dm.setUrl2(fmt.audioSegments[0]);
				for (HttpHeader header : fmt.headers) {
					dm.getHeaders().addHeader(header);
				}
				for (HttpHeader header : fmt.headers2) {
					dm.getHeaders2().addHeader(header);
				}
				XDMApp.getInstance().addVideo(dm, file);
				break;
			case YdlResponse.HLS:
				HlsMetadata md = new HlsMetadata();
				md.setYdlUrl(txtURL.getText());
				md.setUrl(fmt.url);
				for (HttpHeader header : fmt.headers) {
					md.getHeaders().addHeader(header);
				}
				XDMApp.getInstance().addVideo(md, file);
				break;
			case YdlResponse.HDS:
				HdsMetadata hm = new HdsMetadata();
				hm.setYdlUrl(txtURL.getText());
				hm.setUrl(fmt.url);
				for (HttpHeader header : fmt.headers) {
					hm.getHeaders().addHeader(header);
				}
				XDMApp.getInstance().addVideo(hm, file);
				break;
			case YdlResponse.HTTP:
				HttpMetadata ht = new HttpMetadata();
				ht.setYdlUrl(txtURL.getText());
				ht.setUrl(fmt.url);
				for (HttpHeader header : fmt.headers) {
					ht.getHeaders().addHeader(header);
				}
				XDMApp.getInstance().addVideo(ht, file);
				break;
			}
		}
	}

	public void launchWithUrl(String url) {
		setVisible(true);
		txtURL.setText(url);
		btnStart.doClick();
	}

}
