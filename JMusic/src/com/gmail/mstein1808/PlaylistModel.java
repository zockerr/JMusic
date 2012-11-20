package com.gmail.mstein1808;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class PlaylistModel extends AbstractTableModel implements TableModelListener {

	private String[] columnNames = { Messages.getString("PlaylistModel.sync"),
			Messages.getString("PlaylistModel.PlName") };
	private ArrayList<Object[]> data;
	private ArrayList<Playlist> List;

	public PlaylistModel(ArrayList<Playlist> list) {
		addTableModelListener(this);
		List = new ArrayList<Playlist>();
		data = new ArrayList<Object[]>();
		int j=0;
		for(int i=0; i<list.size();i++){
			if(list.get(i).isSyncable()){
				data.add(new Object[2]);
				data.get(j)[0] = new Boolean(list.get(i).getSync());
				data.get(j)[1] = new String(list.get(i).Name);
				List.add(list.get(i));
				j++;
			}
			
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data.get(row)[col];
	}
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

	@Override
	public void tableChanged(TableModelEvent e) {
		int line = e.getFirstRow();
		int column = e.getColumn();
		if (column == 0){
			if(List.get(line).getSync()){
				List.get(line).setSync(false);
			}else{
				List.get(line).setSync(true);
			}
		}
	}
	public boolean isCellEditable(int row, int col){
		if(col==0){
			return true;
		}else{
			return false;
		}
	}
	public void	setValueAt(Object value, int row, int col){
		if(col==0){
			Boolean v=(Boolean)value;
			boolean b=v.booleanValue();
			List.get(row).setSync(b);
			data.get(row)[col]=b;
		}
	}
}