package com.keitaitoys.fonteditor.gui.about;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.keitaitoys.fonteditor.FontEditor;
import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;

public class AboutTextPane extends JTextPane implements LocaleChangeListener, MouseListener, MouseMotionListener {
	
	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String TITLE_TEXT_LOCALIZE_KEY = "about.textpane.title.text";
	private static final String VERSION_TEXT_LOCALIZE_KEY = "about.textpane.version.text";
	private static final String COPYRIGHT_TEXT_LOCALIZE_KEY = "about.textpane.copyright.text";
	
	private static final int PREFERRED_WIDTH = 250;
	
	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private Style centerStyle;

	private Style logoStyle;
	private Style textStyle;
	private Style linkStyle;
	
	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public AboutTextPane() {
		
//		Manager manager = Manager.getInstance();
//		manager.registerAboutPanel(this);
		
		LocaleManager localeManager = LocaleManager.getInstance();
		localeManager.addLocaleChangeListener(this);

		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		setFocusable(false);
		setEditable(false);
		setOpaque(false);
		
		StyledDocument styledDocument = getStyledDocument();
		
		centerStyle = styledDocument.addStyle("CenterStyle", getLogicalStyle());
		StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_CENTER);
		
		URL url = getClass().getResource("img/logoktt.png");
		ImageIcon icon = url != null ? new ImageIcon(url) : new ImageIcon(); 

		logoStyle = styledDocument.addStyle("LogoStyle", getLogicalStyle());
		StyleConstants.setIcon(logoStyle, icon);
		
		textStyle = styledDocument.addStyle("TextStyle", getLogicalStyle());
		StyleConstants.setBold(textStyle, true);
		
		linkStyle = styledDocument.addStyle("LinkStyle", getLogicalStyle());
		StyleConstants.setForeground(linkStyle, Color.BLUE);
		StyleConstants.setUnderline(linkStyle, true);
			
		addMouseListener(this);
		addMouseMotionListener(this);
		
		localize();
	}
	
    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void mouseClicked(MouseEvent e) {
		
		StyledDocument styledDocument = getStyledDocument();
		Element element = styledDocument.getCharacterElement(viewToModel(e.getPoint()));
		
		AttributeSet attributeSet = element.getAttributes();
		
		if(attributeSet.isEqual(linkStyle)) {
			
			try {
				
				URI uri = new URI(FontEditor.URI);
				Desktop.getDesktop().browse(uri);
				
			} catch(URISyntaxException ex) {
			} catch(IOException ex) {}
		}
	}

    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void mouseMoved(MouseEvent e) {
		
		StyledDocument styledDocument = getStyledDocument();
		Element element = styledDocument.getCharacterElement(viewToModel(e.getPoint()));
		
		AttributeSet attributeSet = element.getAttributes();
		
		if(attributeSet.isEqual(linkStyle)) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else {
			setCursor(null);
		}
	}
	
    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void mouseReleased(MouseEvent e) {
		
	}

    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void mouseDragged(MouseEvent e) {
		
	}
	
    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void mouseEntered(MouseEvent e) {
		
	}
	
    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void mousePressed(MouseEvent e) {
		
	}
	
    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void mouseExited(MouseEvent e) {
		
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void localize() {
		
		LocaleManager localeManager = LocaleManager.getInstance();
		StyledDocument styledDocument = getStyledDocument();
		
		try {
			
			setPreferredSize(null);
			Dimension dimension = new Dimension(PREFERRED_WIDTH, Integer.MAX_VALUE);
			
			styledDocument.remove(0, styledDocument.getLength());

			styledDocument.setLogicalStyle(styledDocument.getLength(), centerStyle);
			
			styledDocument.insertString(styledDocument.getLength(), "ignored", logoStyle);
			styledDocument.insertString(styledDocument.getLength(), "\n", textStyle);
			styledDocument.insertString(styledDocument.getLength(), localeManager.getValue(TITLE_TEXT_LOCALIZE_KEY), textStyle);
			styledDocument.insertString(styledDocument.getLength(), "\n", textStyle);
			styledDocument.insertString(styledDocument.getLength(), localeManager.getValue(VERSION_TEXT_LOCALIZE_KEY, FontEditor.VERSION), textStyle);

			int year = Integer.parseInt(FontEditor.YEAR);
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			String range = year + (currentYear > year ? "-" + currentYear : "");
			
			styledDocument.insertString(styledDocument.getLength(), "\n\n", textStyle);
			styledDocument.insertString(styledDocument.getLength(), localeManager.getValue(COPYRIGHT_TEXT_LOCALIZE_KEY, range, FontEditor.COMPANY), textStyle);
			
			String prefix = "http://";
			String link = FontEditor.URI.startsWith(prefix) ? FontEditor.URI.substring(prefix.length()) : FontEditor.URI;
			
			styledDocument.insertString(styledDocument.getLength(), "\n", textStyle);
			styledDocument.insertString(styledDocument.getLength(), link, linkStyle);
			
			setSize(dimension);
			dimension.height = getPreferredSize().height;
			setPreferredSize(dimension);
			
		} catch(BadLocationException e) {
			e.printStackTrace();
		}
	}

    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void localeChange(LocaleChangeEvent e) {
		
		localize();
	}
} 