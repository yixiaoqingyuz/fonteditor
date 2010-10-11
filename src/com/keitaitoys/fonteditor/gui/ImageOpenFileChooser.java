package com.keitaitoys.fonteditor.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.core.PropertiesManager;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;
import com.keitaitoys.util.GIFFileFilter;
import com.keitaitoys.util.ImageFileFilter;
import com.keitaitoys.util.ImagePreviewAccesory;
import com.keitaitoys.util.JPGFileFilter;
import com.keitaitoys.util.PNGFileFilter;

public class ImageOpenFileChooser extends JFileChooser implements LocaleChangeListener, PropertyChangeListener {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String TITLE_LOCALIZE_KEY = "filechooser.imageopen.title";

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public ImageOpenFileChooser() {
		
		Manager manager = Manager.getInstance();
		manager.registerImageOpenFileChooser(this);

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
		setAccessory(new ImagePreviewAccesory(this));

		FileFilter imageFileFilter = new ImageFileFilter(); 
		
		addChoosableFileFilter(imageFileFilter);
		addChoosableFileFilter(new PNGFileFilter());
		addChoosableFileFilter(new GIFFileFilter());
		addChoosableFileFilter(new JPGFileFilter());

		setFileFilter(imageFileFilter);
		
		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		String directory = propertiesManager.getProperty("filechooser.imageopen.directory", "");
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
			propertiesManager.setProperty("filechooser.imageopen.directory", directory);
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
