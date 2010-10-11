package com.keitaitoys.fonteditor.core;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileFilter;

import com.keitaitoys.fonteditor.FontEditor;
import com.keitaitoys.fonteditor.io.FontExporter;
import com.keitaitoys.fonteditor.io.FontReader;
import com.keitaitoys.fonteditor.io.FontWriter;
import com.keitaitoys.fonteditor.io.TXTFontExporter;
import com.keitaitoys.fonteditor.io.XMLFontReader;
import com.keitaitoys.fonteditor.io.XMLFontWriter;
import com.keitaitoys.fonteditor.plugin.FontExporterPlugin;
import com.keitaitoys.fonteditor.plugin.FontReaderPlugin;
import com.keitaitoys.fonteditor.plugin.FontWriterPlugin;

public class IOManager {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static IOManager manager;

	protected EventListenerList listenerList = new EventListenerList();

	private ArrayList<FontReader> fontReaders;
	private ArrayList<FontWriter> fontWriters;
	private ArrayList<FontExporter> fontExporters;

	private ArrayList<FileFilter> fontReaderFileFilters;
	private ArrayList<FileFilter> fontWriterFileFilters;
	private ArrayList<FileFilter> fontExporterFileFilters;

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private IOManager() {
		
		init();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private void init() {
		
		PluginManager pluginManager = PluginManager.getInstance();
		
		ArrayList<FontReaderPlugin> fontReaderPlugins = pluginManager.getPlugin(FontReaderPlugin.class);
		
		fontReaders = new ArrayList<FontReader>();  
		fontReaderFileFilters = new ArrayList<FileFilter>();
		
		FontReader defaultFontReader = new XMLFontReader();

		fontReaders.add(defaultFontReader);
		fontReaderFileFilters.add(defaultFontReader.getFileFilter());

		for(FontReader fontReader : fontReaderPlugins) {
			fontReaders.add(fontReader);
			fontReaderFileFilters.add(fontReader.getFileFilter());
		}
		
		ArrayList<FontWriterPlugin> fontWriterPlugins = pluginManager.getPlugin(FontWriterPlugin.class);

		fontWriters = new ArrayList<FontWriter>();
		fontWriterFileFilters = new ArrayList<FileFilter>();

		FontWriter defaultFontWriter = new XMLFontWriter();

		fontWriters.add(defaultFontWriter);
		fontWriterFileFilters.add(defaultFontWriter.getFileFilter());

		for(FontWriter fontWriter : fontWriterPlugins) {
			fontWriters.add(fontWriter);
			fontWriterFileFilters.add(fontWriter.getFileFilter());
		}
		
		ArrayList<FontExporterPlugin> fontExporterPlugins = pluginManager.getPlugin(FontExporterPlugin.class);

		fontExporters = new ArrayList<FontExporter>();
		fontExporterFileFilters = new ArrayList<FileFilter>();

		FontExporter defaultFontExporter = new TXTFontExporter();

		fontExporters.add(defaultFontExporter);
		fontExporterFileFilters.add(defaultFontExporter.getFileFilter());

		for(FontExporter fontExporter : fontExporterPlugins) {
			fontExporters.add(fontExporter);
			fontExporterFileFilters.add(fontExporter.getFileFilter());
		}
		
		if(FontEditor.DEBUG) {
			
			for(FontReader fontReader : fontReaders) {
				System.out.println("font reader found -> " + fontReader.getClass());
			}
			
			System.out.println("Total font reader(s) found: " + fontReaders.size());
			System.out.println();

			for(FontWriter fontWriter : fontWriters) {
				System.out.println("font writer found -> " + fontWriter.getClass());
			}
			
			System.out.println("Total font writer(s) found: " + fontWriters.size());
			System.out.println();
			
			for(FontExporter fontExporter : fontExporters) {
				System.out.println("font exporter found -> " + fontExporter.getClass());
			}
			
			System.out.println("Total font exporter(s) found: " + fontExporters.size());
			System.out.println();
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public static synchronized IOManager getInstance() {
		
		if(manager == null) {
			manager = new IOManager();
		}
		
		return manager;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void addFontReader(FontReader fontReader) {
		
		if(!fontReaders.contains(fontReader)) {
			
			fontReaders.add(fontReader);
			fontReaderFileFilters.add(fontReader.getFileFilter());
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void addFontWriter(FontWriter fontWriter) {
		
		if(!fontWriters.contains(fontWriter)) {
			
			fontWriters.add(fontWriter);
			fontWriterFileFilters.add(fontWriter.getFileFilter());
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void addFontExporter(FontExporter fontExporter) {
		
		if(!fontExporters.contains(fontExporter)) {
			
			fontExporters.add(fontExporter);
			fontExporterFileFilters.add(fontExporter.getFileFilter());
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void removeFontReader(FontReader fontReader) {
		
		if(fontReaders.contains(fontReader)) {
			
			int index = fontReaders.indexOf(fontReader);
			
			fontReaders.remove(fontReader);
			fontReaderFileFilters.remove(index);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void removeFontWriter(FontWriter fontWriter) {
		
		if(fontWriters.contains(fontWriter)) {
			
			int index = fontWriters.indexOf(fontWriter);
			
			fontWriters.remove(fontWriter);
			fontWriterFileFilters.remove(index);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void removeFontExporter(FontExporter fontExporter) {
		
		if(fontExporters.contains(fontExporter)) {
			
			int index = fontExporters.indexOf(fontExporter);
			
			fontWriters.remove(fontExporter);
			fontWriterFileFilters.remove(index);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public ArrayList<FontReader> getFontReaders() {
		
		return fontReaders;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public FontReader getFontReader(FileFilter fileFilter) {
		
		FontReader fontReader = null;
		
		if(fileFilter != null && fontReaderFileFilters.contains(fileFilter)) {
			
			int index = fontReaderFileFilters.indexOf(fileFilter);
			fontReader = fontReaders.get(index);
		}
		
		return fontReader;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public FontReader getFontReader(String extension) {
		
		if(extension != null) {

			for(FontReader fontReader : fontReaders) {
				
				String[] formatNames = fontReader.getFormatNames();
				
				for(String format : formatNames) {
					
					if(extension.equalsIgnoreCase(format)) {
						
						return fontReader;
					}
				}
			}
		}
		
		return null;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public ArrayList<FontWriter> getFontWriters() {
		
		return fontWriters;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public FontWriter getFontWriter(FileFilter fileFilter) {
		
		FontWriter fontWriter = null;
		
		if(fileFilter != null && fontWriterFileFilters.contains(fileFilter)) {
			
			int index = fontWriterFileFilters.indexOf(fileFilter);
			fontWriter = fontWriters.get(index);
		}
		
		return fontWriter;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public FontWriter getFontWriter(String extension) {
		
		if(extension != null) {

			for(FontWriter fontWriter : fontWriters) {
				
				String[] formatNames = fontWriter.getFormatNames();
				
				for(String format : formatNames) {
					
					if(extension.equalsIgnoreCase(format)) {
						
						return fontWriter;
					}
				}
			}
		}
		
		return null;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public ArrayList<FontExporter> getFontExporters() {
		
		return fontExporters;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public FontExporter getFontExporter(FileFilter fileFilter) {
		
		FontExporter fontExporter = null;
		
		if(fileFilter != null && fontExporterFileFilters.contains(fileFilter)) {
			
			int index = fontExporterFileFilters.indexOf(fileFilter);
			fontExporter = fontExporters.get(index);
		}
		
		return fontExporter;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public FontExporter getFontExporter(String extension) {
		
		if(extension != null) {

			for(FontExporter fontExporter : fontExporters) {
				
				String[] formatNames = fontExporter.getFormatNames();
				
				for(String format : formatNames) {
					
					if(extension.equalsIgnoreCase(format)) {
						
						return fontExporter;
					}
				}
			}
		}
		
		return null;
	}
}