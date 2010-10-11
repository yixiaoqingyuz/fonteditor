package com.keitaitoys.fonteditor.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.EventListenerList;

import com.keitaitoys.fonteditor.FontEditor;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;
import com.keitaitoys.util.ClassUtils;

public class LocaleManager {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public static final String LOCALE_DIRECTORY = "locale";

	private static final String RESOURCE_BUNDLE_BASE_NAME = "fonteditor";
	private static final String RESOURCE_BUNDLE_EXTENSION = "properties";
	
	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static LocaleManager manager;
	
	protected EventListenerList listenerList = new EventListenerList();
	protected ResourceBundle resourceBundle;
	
	protected Locale locale;
	protected Locale defaultLocale;
	protected Locale fallbackLocale;
	
	private List<Locale> locales;
	protected Control control;
	
	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private LocaleManager() {
		
		init();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private void init() {
		
		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		File localeDirectory = new File(propertiesManager.getProperty("locale.directory", LOCALE_DIRECTORY));

		locales = new ArrayList<Locale>();

		try {
			
			ClassUtils.addClassPathFile(localeDirectory);
			
			// Находим все доступные локали
			locales = findAvailableLocales(localeDirectory);

		} catch(Exception ignore) {}

		fallbackLocale = Locale.ENGLISH;
		control = new Control();

		if(!locales.contains(Locale.ENGLISH)) {
			locales.add(0, fallbackLocale);
		}

		defaultLocale = locales.get(locales.indexOf(Locale.ENGLISH));
		
		String language = propertiesManager.getProperty("locale.language", "");
		String country = propertiesManager.getProperty("locale.country", "");
		String variant = propertiesManager.getProperty("locale.variant", "");
		
		Locale locale = new Locale(language, country, variant);
		
		if(locales.contains(locale)) {
			locale = locales.get(locales.indexOf(locale));
		} else {
			locale = defaultLocale;
		}

		setLocale(locale);
		
		if(FontEditor.DEBUG) {
			
			for(Locale l : locales) {
				System.out.println("locale found -> " + l.getDisplayLanguage(Locale.ENGLISH));
			}
			
			System.out.println("Total locale(s) found: " + locales.size());
			System.out.println();
		}
//System.out.println(">>> " + getValue("shitd"));
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public static synchronized LocaleManager getInstance() {
		
		if(manager == null) {
			manager = new LocaleManager();
		}
		
		return manager;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void temp() {
		
		String language = locale.getLanguage();
		System.out.println(language);
		
		if(language.equals("ru")) {
			setLocale(new Locale("en", "US"));
		} else {
			setLocale(new Locale("ru", "UA"));
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void setLocale(Locale locale) {
		
		if(locale == null) throw new IllegalArgumentException("locale must be not null!");
		
		Locale oldLocale = this.locale; 
		
		if(oldLocale != locale) {
			
			resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, locale, control);
			
			this.locale = locale;
			
			PropertiesManager propertiesManager = PropertiesManager.getInstance();
			
			String language = locale.getLanguage();
			String country = locale.getCountry();
			String variant = locale.getVariant();
			
			propertiesManager.setProperty("locale.language", language);
			propertiesManager.setProperty("locale.country", country);
			propertiesManager.setProperty("locale.variant", variant);

			fireLocaleChanged(oldLocale, locale);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

    protected void fireLocaleChanged(Locale oldValue, Locale newValue) {
    	
    	Object[] listeners = listenerList.getListenerList();
    	LocaleChangeEvent e = null;

    	for(int i = listeners.length - 2; i >= 0; i -= 2) {
    		
    	    if(listeners[i] == LocaleChangeListener.class) {
    	    	if(e == null) e = new LocaleChangeEvent(this, oldValue, newValue);
    	    	((LocaleChangeListener)listeners[i + 1]).localeChange(e);
    	    }
    	}
    }

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public String getValue(String key, Object... arguments) {

		String pattern = resourceBundle.getString(key);

		// Single quote has a special usage within MessageFormat
		// so we need to append it with another one to
		// make it evaluated as a regular single quote character
		pattern = pattern.replaceAll("'", "''");

		String result = MessageFormat.format(pattern, arguments);

		return result;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public String getLanguageValue(Locale locale) {

		String language = locale.getDisplayLanguage(this.locale);
		
		if(language.length() > 0) {
			language = language.substring(0, 1).toUpperCase(this.locale) + language.substring(1);
		}
		
		String country = locale.getDisplayCountry(); 
		
		if(country.length() > 0) {
			language = language + " - " + locale.getDisplayCountry(this.locale); 
		}
		
		String variant = locale.getDisplayVariant(); 

		if(variant.length() > 0) {
			language = language + " - " + locale.getDisplayVariant(this.locale); 
		}
		
		if(locale == fallbackLocale) {
			language = language + " (fallback)";
		}
		
		return language;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public Locale getLocale() {
		
		return locale;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public List<Locale> getAvailableLocales() {
		
		return locales;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private List<Locale> findAvailableLocales(File directory) {
		
		if(directory == null) throw new NullPointerException();
		
		ArrayList<Locale> localeList = new ArrayList<Locale>();

		if(!directory.exists() || !directory.isDirectory()) {
			return localeList;
		}

		File[] files = directory.listFiles();
		
		Pattern pattern01 = Pattern.compile(RESOURCE_BUNDLE_BASE_NAME + "(?:_([.[^_]]+)){1}(?:_([.[^_]]+)){1}(?:_(.+)){0,}" + "\\." + RESOURCE_BUNDLE_EXTENSION);
		Pattern pattern02 = Pattern.compile(RESOURCE_BUNDLE_BASE_NAME + "(?:_([.[^_]]+)){1}(?:_([.[^_]]+)){0,}" + "\\." + RESOURCE_BUNDLE_EXTENSION);
		
//		System.out.println("\n\n\n");

		for(File file : files) {
			
			String fileName = file.getName();
			
			String language = "";
			String country = "";
			String variant = "";
			
//			System.out.println("testing file \"" + fileName + "\"");

			Matcher matcher01 = pattern01.matcher(fileName);
			
			if(matcher01.matches()) {
				
//				for(int i = 0; i < matcher01.groupCount(); i++) {
//					System.out.println(i + " = " + matcher01.group(i + 1));
//				}
				
				language = matcher01.group(1);
				country = matcher01.group(2);
				variant = matcher01.group(3) != null ? matcher01.group(3) : "";
				
				localeList.add(new Locale(matcher01.group(1), matcher01.group(2), matcher01.group(3) != null ? matcher01.group(3) : ""));
			
			} else {

				Matcher matcher02 = pattern02.matcher(fileName);
				
				if(matcher02.matches()) {
					
//					for(int i = 0; i < matcher02.groupCount(); i++) {
//						System.out.println(i + " = " + matcher02.group(i + 1));
//					}

					language = matcher02.group(1);
					country = matcher02.group(2) != null ? matcher02.group(2) : "";

					localeList.add(new Locale(matcher02.group(1), matcher02.group(2) != null ? matcher02.group(2) : ""));

				} else {
//					System.out.println("not matches");
				}
			}
			
//			System.out.println("language = " + language);
//			System.out.println("country = " + country);
//			System.out.println("variant = " + variant);
			
//			System.out.println();
		}

//		for(Locale l : localeList) {
//			System.out.println("found -> " + "language=" + l.getLanguage() + ",country=" + l.getCountry() + ",variant=" + l.getVariant());
//		}
		
		return localeList;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void addLocaleChangeListener(LocaleChangeListener l) {
		
		listenerList.add(LocaleChangeListener.class, l);
    }

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void removeLocaleChangeListener(LocaleChangeListener l) {
		
		listenerList.remove(LocaleChangeListener.class, l);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public LocaleChangeListener[] getLocaleChangeListeners() {
		
		return (LocaleChangeListener[])listenerList.getListeners(LocaleChangeListener.class);
	}

	//////////////////////////////////////////////////////////////////////
	// Inner class ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private class Control extends ResourceBundle.Control {
		
		//////////////////////////////////////////////////////////////////////
		// Function ////////////////////////////////////////////////////////// 
		//////////////////////////////////////////////////////////////////////
		
        public String toBundleName(String baseName, Locale locale) {

        	if(locale == fallbackLocale) {
    	    	return DefaultResourceBundle.class.getName();
			}

			if(locale == Locale.ROOT) {
    	    	return DefaultResourceBundle.class.getName();
    	    }

			String language = locale.getLanguage();
			String country = locale.getCountry();
			String variant = locale.getVariant();

			if(language == "" && country == "" && variant == "") {
    	    	return DefaultResourceBundle.class.getName();
    	    }

			return super.toBundleName(baseName, locale);
        }
	}
}