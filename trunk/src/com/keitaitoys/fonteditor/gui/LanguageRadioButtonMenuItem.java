package com.keitaitoys.fonteditor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JRadioButtonMenuItem;

import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;

public class LanguageRadioButtonMenuItem extends JRadioButtonMenuItem implements LocaleChangeListener, ActionListener {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	protected Locale locale;
	
	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public LanguageRadioButtonMenuItem(Locale locale) {
		
		this.locale = locale;
		
		LocaleManager localeManager = LocaleManager.getInstance();
		localeManager.addLocaleChangeListener(this);
		
// register here
		
		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		setEnabled(true);

		LocaleManager localeManager = LocaleManager.getInstance();
		setSelected(locale == localeManager.getLocale());
		
		localize();

		addActionListener(this);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void actionPerformed(ActionEvent e) {
		
		Manager manager = Manager.getInstance();
		manager.executeLanguageSelect(locale);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void localize() {
		
		LocaleManager localeManager = LocaleManager.getInstance();
		setText(localeManager.getLanguageValue(locale));
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void localeChange(LocaleChangeEvent e) {
		
		localize();
	}
}