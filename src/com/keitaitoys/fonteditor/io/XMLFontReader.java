package com.keitaitoys.fonteditor.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.TreeSet;

import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.keitaitoys.fonteditor.font.Font;
import com.keitaitoys.fonteditor.font.Symbol;
import com.keitaitoys.fonteditor.io.FontReadFailedException;
import com.keitaitoys.util.XMLFileFilter;

public class XMLFontReader implements FontReader {

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

	public XMLFontReader() {
		
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
	
	public Font read(String path) throws FontReadFailedException {
		
		return read(new File(path));
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public Font read(File file) throws FontReadFailedException {
		
		Font font = new Font();
		Reader reader = null;
		
		try {
			
			JAXBContext context = JAXBContext.newInstance(XMLFont.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			XMLFont xmlFont = (XMLFont)unmarshaller.unmarshal(reader);

			String image = xmlFont.getImage(); 

			if(image.length() > 0) {
				font.setImageFile(new File(file.getParent() + File.separator + image));
			} else {
				font.setImageFile(null);
			}

			font.setSize(xmlFont.getSize());
			font.setSpacing(xmlFont.getSpacing());
			font.setLeading(xmlFont.getLeading());
			
			TreeSet<XMLSymbol> xmlSymbols = xmlFont.getSymbols();
			
			for(XMLSymbol xmlSymbol : xmlSymbols) {
				
				String c = xmlSymbol.getChar();
				
				int x = xmlSymbol.getX();
				int y = xmlSymbol.getY();
				int width = xmlSymbol.getWidth();
				int height = xmlSymbol.getHeight();

				int offsetX = xmlSymbol.getOffsetX();
				int offsetY = xmlSymbol.getOffsetY();

				Symbol symbol = new Symbol(c.charAt(0), x, y, width, height, offsetX, offsetY);
				font.addSymbol(symbol);
			}

		} catch(Exception e) {

			throw new FontReadFailedException(e);
			
		} finally {
			
			if(reader != null) {
				
				try {
					reader.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}

		return font;
	}
}
