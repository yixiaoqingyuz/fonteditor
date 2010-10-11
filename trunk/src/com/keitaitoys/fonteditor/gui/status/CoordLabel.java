package com.keitaitoys.fonteditor.gui.status;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JLabel;

import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;

public class CoordLabel extends JLabel implements LocaleChangeListener {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String TEXT_LOCALIZE_KEY = "status.label.coord.caption";
	private static final int MIN_WIDTH = 80;

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private Point coord;

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public CoordLabel() {
		
		Manager manager = Manager.getInstance();
		manager.registerCoordLabel(this);
		
		LocaleManager localeManager = LocaleManager.getInstance();
		localeManager.addLocaleChangeListener(this);

		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		setEnabled(true);
		setHorizontalAlignment(JLabel.LEFT);

		localize();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public Point getCoord() {
		
		return coord;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void setCoord(Point coord) {
		
		this.coord = coord;
		
		localize();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void localize() {
		
		setPreferredSize(null);

		if(coord != null) {
			
			LocaleManager localeManager = LocaleManager.getInstance();
			setText(localeManager.getValue(TEXT_LOCALIZE_KEY, coord.x, coord.y));
			
		} else {
			setText(null);
		}
		
		Dimension preferredSize = getPreferredSize();
		
		if(preferredSize.width < MIN_WIDTH) {
			preferredSize.width = MIN_WIDTH;
		}
		
		setPreferredSize(preferredSize);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void localeChange(LocaleChangeEvent e) {
		
		localize();
	}
}
