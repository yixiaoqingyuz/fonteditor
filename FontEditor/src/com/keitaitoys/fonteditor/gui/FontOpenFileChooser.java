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
import com.keitaitoys.fonteditor.io.FontReader;

public class FontOpenFileChooser extends JFileChooser implements LocaleChangeListener, PropertyChangeListener {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String TITLE_LOCALIZE_KEY = "filechooser.fontopen.title";

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public FontOpenFileChooser() {
		
		Manager manager = Manager.getInstance();
		manager.registerFontOpenFileChooser(this);

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
		
		ArrayList<FontReader> fontReaders = IOManager.getInstance().getFontReaders();
		
		for(FontReader fontReader : fontReaders) {
			
			FileFilter fileFilter = fontReader.getFileFilter();
			
			if(fileFilter != null) {
				addChoosableFileFilter(fileFilter);
			}
		}
		
		setFileFilter(getAcceptAllFileFilter());

		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		String directory = propertiesManager.getProperty("filechooser.fontopen.directory", "");
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
			propertiesManager.setProperty("filechooser.fontopen.directory", directory);
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
