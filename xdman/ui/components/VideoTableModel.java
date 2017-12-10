package xdman.ui.components;

import java.util.*;
import javax.swing.table.*;

import xdman.videoparser.YdlResponse.YdlVideo;

public class VideoTableModel extends AbstractTableModel {

	public VideoTableModel() {
		list = new ArrayList<>();
	}

	public void setList(ArrayList<YdlVideo> list) {
		this.list.clear();
		this.list.addAll(list);
		this.fireTableDataChanged();
	}

	ArrayList<YdlVideo> list;

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public Object getValueAt(int r, int c) {
		return list.get(r);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return YdlVideo.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		System.out.println("Setting valye: " + aValue);
		list.set(rowIndex, (YdlVideo) aValue);
	}

	public void clear() {
		list.clear();
		fireTableDataChanged();
	}

}
