package com.keitaitoys.fonteditor.gui;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;

import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;

public class LookAndFeelMenu extends JMenu implements LocaleChangeListener {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String CAPTION_LOCALIZE_KEY = "menu.lookandfeel.caption";

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public LookAndFeelMenu() {
		
		Manager manager = Manager.getInstance();
		manager.registerLookAndFeelMenu(this);

		LocaleManager localeManager = LocaleManager.getInstance();
		localeManager.addLocaleChangeListener(this);

		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		setEnabled(true);
		setMnemonic(KeyEvent.VK_F);

		localize();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void localize() {
		
		LocaleManager localeManager = LocaleManager.getInstance();
		setText(localeManager.getValue(CAPTION_LOCALIZE_KEY));
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void localeChange(LocaleChangeEvent e) {
		
		localize();
	}
}
