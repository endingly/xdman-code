package xdman.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import xdman.DownloadEntry;
import xdman.XDMApp;
import xdman.util.Logger;

public class DownloadListView {
	private DownloadTableModel model;
	private JTable table;
	private String selectedId;

	public DownloadListView(JPanel container) {
		model = new DownloadTableModel();
		XDMApp.getInstance().addListener(model);
		table = new JTable(model);
		table.setTableHeader(null);
		table.setDefaultRenderer(DownloadEntry.class, new XDMTableCellRenderer());
		table.setRowHeight(70);
		table.setShowGrid(false);
		table.setFillsViewportHeight(true);
		table.setBorder(new EmptyBorder(0, 0, 0, 0));
		table.setDragEnabled(true);
//		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//			@Override
//			public void valueChanged(ListSelectionEvent e) {
//				int selectedRow = e.getFirstIndex();
//				selectedId = model.getIdAt(selectedRow);
//				Logger.log("Selected id1: " + selectedId+" row: "+selectedRow);
//			}
//		});
//
//		model.addTableModelListener(new TableModelListener() {
//			@Override
//			public void tableChanged(TableModelEvent e) {
//				if(selectedId!=null){
//					int index=model.getIndexOfId(selectedId);
//					Logger.log("Index of "+selectedId+" is: "+index);
//					if(index>-1){
//						table.setRowSelectionInterval(index, index);
//					}
//				}
//			}
//		});

		JScrollPane jsp = new JScrollPane(table);
		jsp.setBorder(new EmptyBorder(0, 0, 0, 0));

		container.add(jsp);
	}

	public JTable getTable() {
		return table;
	}

	public String[] getSelectedIds() {
		String[] arr = new String[table.getSelectedRowCount()];

		int[] selectedRows = table.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			arr[i] = model.getIdAt(selectedRows[i]);
		}
		return arr;
	}

	public void refresh() {
		model.listChanged();
	}
}
