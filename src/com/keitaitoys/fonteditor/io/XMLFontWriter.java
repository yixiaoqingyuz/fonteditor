package com.keitaitoys.fonteditor.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.keitaitoys.fonteditor.font.Symbol;
import com.keitaitoys.fonteditor.font.Font;
import com.keitaitoys.fonteditor.io.FontWriteFailedException;
import com.keitaitoys.util.Utilities;
import com.keitaitoys.util.XMLFileFilter;

public class XMLFontWriter implements FontWriter {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String[] formatNames = {
		"xml"
	};
	
	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private final FileFilter fileFilter;
	
	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public XMLFontWriter() {
		
		fileFilter = new XMLFileFilter();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public String[] getFormatNames() {
		
		return (String[])formatNames.clone();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public FileFilter getFileFilter() {
		
		return fileFilter;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void write(String path, Font font) throws FontWriteFailedException {
		
		write(new File(path), font);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void write(File file, Font font) throws FontWriteFailedException {
		
		Writer writer = null;

		try {
			
			File sourceImageFile = font.getImageFile();
			File destinationImageFile = null;

			// Копируем изображение
			if(sourceImageFile != null) {
				
				destinationImageFile = new File(Utilities.addExtension(Utilities.removeExtension(file), Utilities.getExtension(sourceImageFile)));
				Utilities.copyFile(sourceImageFile, destinationImageFile, false);
			}

			Set<Symbol> symbols = font.getSymbols();
			TreeSet<XMLSymbol> xmlSymbols = new TreeSet<XMLSymbol>();
			
			for(Symbol symbol : symbols) {
				
				char c = symbol.getChar();
				
				int x = symbol.getBoundsX();
				int y = symbol.getBoundsY();
				int width = symbol.getBoundsWidth();
				int height = symbol.getBoundsHeight();
				
				int offsetX = symbol.getOffsetX();
				int offsetY = symbol.getOffsetY();

				XMLSymbol xmlSymbol = new XMLSymbol(String.valueOf(c), x, y, width, height, offsetX, offsetY);
				
				xmlSymbols.add(xmlSymbol);
			}
			
			XMLFont xmlFont = new XMLFont();
			
			xmlFont.setImage(destinationImageFile != null ? destinationImageFile.getName() : "");
			xmlFont.setSize(font.getSize());
			xmlFont.setSpacing(font.getSpacing());
			xmlFont.setLeading(font.getLeading());
			xmlFont.setSymbols(xmlSymbols);
			
			JAXBContext context = JAXBContext.newInstance(XMLFont.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
//			marshaller.marshal(xmlFont, System.out);
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			marshaller.marshal(xmlFont, writer);
			
			writer.flush();
			
		} catch(Exception e) {

			throw new FontWriteFailedException(e);
			
		} finally {
			
			if(writer != null) {
				
				try {
					writer.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
