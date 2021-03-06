package com.keitaitoys.fonteditor.gui.preferences;

import javax.swing.border.TitledBorder;

import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;

public class PreferencesPreviewTextTitledBorder extends TitledBorder implements LocaleChangeListener {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String TITLE_LOCALIZE_KEY = "preferences.preview.titledborder.text.title";

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public PreferencesPreviewTextTitledBorder() {
		
		super("");
		
		LocaleManager localeManager = LocaleManager.getInstance();
		localeManager.addLocaleChangeListener(this);

		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		localize();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void localize() {
		
		LocaleManager localeManager = LocaleManager.getInstance();
		setTitle(localeManager.getValue(TITLE_LOCALIZE_KEY));
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void localeChange(LocaleChangeEvent e) {
		
		localize();
	}
}