package com.keitaitoys.fonteditor.gui.toolbar;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;

public class ExportButton extends JButton implements LocaleChangeListener, ActionListener {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String CAPTION_LOCALIZE_KEY = "toolbar.button.export.caption";
	private static final String TOOLTIP_LOCALIZE_KEY = "toolbar.button.export.tooltip";

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public ExportButton() {
		
		Manager manager = Manager.getInstance();
		manager.registerExportButton(this);
		
		LocaleManager localeManager = LocaleManager.getInstance();
		localeManager.addLocaleChangeListener(this);

		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		setEnabled(true);
		setFocusable(false);

		URL url = getClass().getResource("img/iconexport.png");
		setIcon(url != null ? new ImageIcon(url) : new ImageIcon());

		localize();

		addActionListener(this);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void actionPerformed(ActionEvent e) {
		
		Manager manager = Manager.getInstance();
		manager.executeExport();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void localize() {
		
		LocaleManager localeManager = LocaleManager.getInstance();
		
		setText(localeManager.getValue(CAPTION_LOCALIZE_KEY));
		
		String tooltip = localeManager.getValue(TOOLTIP_LOCALIZE_KEY); 
		setToolTipText(tooltip != null && tooltip.length() > 0 ? tooltip : null);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void localeChange(LocaleChangeEvent e) {
		
		localize();
	}
}