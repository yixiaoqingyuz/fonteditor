package com.keitaitoys.fonteditor.gui.plugins;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.core.PluginManager;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;
import com.keitaitoys.plugin.Plugin;

public class PluginsTableModel extends AbstractTableModel implements LocaleChangeListener {
	
	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String COLUMN_PROVIDER_TITLE_LOCALIZE_KEY = "plugins.table.column.provider.title";
	private static final String COLUMN_NAME_TITLE_LOCALIZE_KEY = "plugins.table.column.name.title";
	private static final String COLUMN_VERSION_TITLE_LOCALIZE_KEY = "plugins.table.column.version.title";
	private static final String COLUMN_DESCRIPTION_TITLE_LOCALIZE_KEY = "plugins.table.column.description.title";

	public static final int COLUMN_PROVIDER = 0;
	public static final int COLUMN_NAME = 1;
	public static final int COLUMN_VERSION = 2;
	public static final int COLUMN_DESCRIPTION = 3;
	
	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private String[] columnNames;
	private ArrayList<Plugin> pluginsArray;

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public PluginsTableModel() {
		
		Manager manager = Manager.getInstance();
		manager.registerPluginsTableModel(this);

		LocaleManager localeManager = LocaleManager.getInstance();
		localeManager.addLocaleChangeListener(this);
		
		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private void init() {
		
		PluginManager pluginManager = PluginManager.getInstance();
		pluginsArray = pluginManager.getPlugins();

		localize();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public int getRowCount() {
		
		return pluginsArray.size();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public int getColumnCount() {
		
		return columnNames.length;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public String getColumnName(int column) {
		
		return columnNames[column];
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public Class<? extends Object> getColumnClass(int column) {

		switch (column) {
		
            case COLUMN_PROVIDER: return String.class;
            case COLUMN_NAME: return String.class;
            case COLUMN_VERSION: return String.class;
            case COLUMN_DESCRIPTION: return String.class;
        }
		
		return super.getColumnClass(column);
    }

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
    public boolean isCellEditable(int row, int column) {
    	
		switch (column) {
		
        	case COLUMN_PROVIDER: return false;
            case COLUMN_NAME: return false;
            case COLUMN_VERSION: return false;
            case COLUMN_DESCRIPTION: return false;
		}

		return super.isCellEditable(row, column);
    }
    
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public Object getValueAt(int row, int column) {
		
		Plugin plugin = pluginsArray.get(row);
		
		switch(column) {
		
			case COLUMN_PROVIDER: return plugin.getProvider();
			case COLUMN_NAME: return plugin.getName();
			case COLUMN_VERSION: return plugin.getVersion();
			case COLUMN_DESCRIPTION: return plugin.getDescription();
		}

		return null;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void localize() {
		
		LocaleManager localeManager = LocaleManager.getInstance();

		final String[] columnNames = new String[] {
				
			localeManager.getValue(COLUMN_PROVIDER_TITLE_LOCALIZE_KEY),
			localeManager.getValue(COLUMN_NAME_TITLE_LOCALIZE_KEY),
			localeManager.getValue(COLUMN_VERSION_TITLE_LOCALIZE_KEY),
			localeManager.getValue(COLUMN_DESCRIPTION_TITLE_LOCALIZE_KEY),
		};

		this.columnNames = columnNames;
		
		fireTableStructureChanged();
	}
	
    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void localeChange(LocaleChangeEvent e) {

		localize();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

    public String toString() {

    	StringBuffer stringBuffer = new StringBuffer();
    	
    	stringBuffer.append(getClass().getName());
    	stringBuffer.append("[");
    	stringBuffer.append("rowCount=" + getRowCount());
    	stringBuffer.append(",");
    	stringBuffer.append("columnCount=" + getColumnCount());    	
    	stringBuffer.append(",");
    	stringBuffer.append("data=");
    	stringBuffer.append("\n");
    	
    	for(Plugin plugin : pluginsArray) {
        	stringBuffer.append("\t");
        	stringBuffer.append("plugin=" + plugin);
        	stringBuffer.append("\n");
    	}

    	stringBuffer.append("]");
    	
    	return stringBuffer.toString();
    }
}
