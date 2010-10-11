package com.keitaitoys.fonteditor.core;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.keitaitoys.fonteditor.FontEditor;
import com.keitaitoys.fonteditor.io.FontReader;

public class LookAndFeelManager {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static LookAndFeelManager manager;

	private List<LookAndFeelInfo> lookAndFeels;
	private LookAndFeelInfo lookAndFeel;
	
	private LookAndFeelInfo defaultLookAndFeel;
	
	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private LookAndFeelManager() {
		
		init();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private void init() {
		
		// Находим все доступные стили
		lookAndFeels = findAvailableLookAndFeels();

		for(LookAndFeelInfo lookAndFeel : lookAndFeels) {
			if(lookAndFeel.getClassName() == UIManager.getCrossPlatformLookAndFeelClassName()) {
				defaultLookAndFeel = lookAndFeel;
			}
		}

//		System.out.println("$$$ " + defaultLookAndFeel.getClassName());
		
		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		
		String name = propertiesManager.getProperty("lookandfeel.name", "");
		String className = propertiesManager.getProperty("lookandfeel.class", "");
		
//		System.out.println("###################################################");
//		System.out.println("name = " + name);
//		System.out.println("className = " + className);
		
		LookAndFeelInfo lookAndFeel = new LookAndFeelInfo(name, className);

//		System.out.println("$$$ " + lookAndFeel.getClassName());

		if(lookAndFeels.contains(lookAndFeel)) {
			lookAndFeel = lookAndFeels.get(lookAndFeels.indexOf(lookAndFeel));
		} else {
			lookAndFeel = defaultLookAndFeel;
		}

//		System.out.println("$$$ " + lookAndFeel.getClassName());

		setLookAndFeel(lookAndFeel);
		
		if(FontEditor.DEBUG) {
			
			for(LookAndFeelInfo lookAndFeelInfo : lookAndFeels) {
				System.out.println("look & feel found -> " + lookAndFeelInfo.getName());
			}
			
			System.out.println("Total look & feel(s) found: " + lookAndFeels.size());
			System.out.println();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public static synchronized LookAndFeelManager getInstance() {
		
		if(manager == null) {
			manager = new LookAndFeelManager();
		}
		
		return manager;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void setLookAndFeel(LookAndFeelInfo lookAndFeel) {
		
		if(lookAndFeel == null) throw new NullPointerException();
		
		if(this.lookAndFeel != lookAndFeel) {
			
			try {

				UIManager.setLookAndFeel(lookAndFeel.getClassName());

				JFrame.setDefaultLookAndFeelDecorated(UIManager.getLookAndFeel().getSupportsWindowDecorations());
				JDialog.setDefaultLookAndFeelDecorated(UIManager.getLookAndFeel().getSupportsWindowDecorations());
				
				PropertiesManager propertiesManager = PropertiesManager.getInstance();
				
				String name = lookAndFeel.getName();
				String className = lookAndFeel.getClassName();
				
				propertiesManager.setProperty("lookandfeel.name", name);
				propertiesManager.setProperty("lookandfeel.class", className);
				
				this.lookAndFeel = lookAndFeel;
				
			// TODO: add exception throw state
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			} catch(InstantiationException e) {
				e.printStackTrace();
			} catch(IllegalAccessException e) {
				e.printStackTrace();
			} catch(UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			} catch(ClassCastException e) {
				e.printStackTrace();
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public String getLookAndFeelValue(UIManager.LookAndFeelInfo lookAndFeel) {

		return lookAndFeel.getName();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public UIManager.LookAndFeelInfo getLookAndFeel() {
		
		return lookAndFeel;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public List<LookAndFeelInfo> getAvailableLookAndFeels() {
		
		return lookAndFeels;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private List<LookAndFeelInfo> findAvailableLookAndFeels() {
		
		ArrayList<LookAndFeelInfo> lookAndFeels = new ArrayList<LookAndFeelInfo>();

		for(UIManager.LookAndFeelInfo lookAndFeel : UIManager.getInstalledLookAndFeels()) {

			String name = lookAndFeel.getName();
			String className = lookAndFeel.getClassName();

			lookAndFeels.add(new LookAndFeelInfo(name, className));
		}
		
		return lookAndFeels;
	}

	//////////////////////////////////////////////////////////////////////
	// Inner class ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public class LookAndFeelInfo extends UIManager.LookAndFeelInfo {
		
    	//////////////////////////////////////////////////////////////////////
    	// Constructor ///////////////////////////////////////////////////////
    	//////////////////////////////////////////////////////////////////////
		
        public LookAndFeelInfo(String name, String className) {
        	super(name, className);
        }

    	//////////////////////////////////////////////////////////////////////
    	// Functions /////////////////////////////////////////////////////////
    	//////////////////////////////////////////////////////////////////////

        public boolean equals(Object obj) {

        	if(this == obj) return true;
            if(!(obj instanceof LookAndFeelInfo)) return false;

            LookAndFeelInfo other = (LookAndFeelInfo)obj;

    		return getName().equals(other.getName()) && getClassName().equals(other.getClassName());
        }
	}
}