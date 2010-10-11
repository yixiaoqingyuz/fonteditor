package com.keitaitoys.fonteditor.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.keitaitoys.fonteditor.core.IOManager;
import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.core.PropertiesManager;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;
import com.keitaitoys.fonteditor.io.FontExporter;
import com.keitaitoys.util.ExtensionFileChooser;

public class FontExportFileChooser extends ExtensionFileChooser implements LocaleChangeListener, PropertyChangeListener {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String TITLE_LOCALIZE_KEY = "filechooser.fontexport.title";

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public FontExportFileChooser() {
		
		Manager manager = Manager.getInstance();
		manager.registerFontExportFileChooser(this);

		LocaleManager localeManager = LocaleManager.getInstance();
		localeManager.addLocaleChangeListener(this);

		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setMultiSelectionEnabled(false);

		ArrayList<FontExporter> fontExporters = IOManager.getInstance().getFontExporters();
		
		for(FontExporter fontExporter : fontExporters) {
			
			FileFilter fileFilter = fontExporter.getFileFilter();
			
			if(fileFilter != null) {
				addChoosableFileFilter(fileFilter);
			}
		}
		
		setFileFilter(getAcceptAllFileFilter());

		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		String directory = propertiesManager.getProperty("filechooser.fontexport.directory", "");
		setCurrentDirectory(new File(directory));
		
		localize();

		addPropertyChangeListener(this);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
                             
    public void propertyChange(PropertyChangeEvent e) {
    	
        String property = e.getPropertyName();
        
        if(property.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
        	
			PropertiesManager propertiesManager = PropertiesManager.getInstance();
			
        	String directory = getCurrentDirectory().getAbsolutePath();
			propertiesManager.setProperty("filechooser.fontexport.directory", directory);
        }
    }

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void localize() {
		
		LocaleManager localeManager = LocaleManager.getInstance();
		setDialogTitle(localeManager.getValue(TITLE_LOCALIZE_KEY));
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void localeChange(LocaleChangeEvent e) {
		
		localize();
	}
}
