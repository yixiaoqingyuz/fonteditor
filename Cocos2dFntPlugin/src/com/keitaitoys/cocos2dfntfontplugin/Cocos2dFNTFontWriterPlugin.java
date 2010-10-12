package com.keitaitoys.cocos2dfntfontplugin;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;

import com.keitaitoys.fonteditor.font.Symbol;
import com.keitaitoys.fonteditor.font.Font;
import com.keitaitoys.fonteditor.io.FontWriteFailedException;
import com.keitaitoys.fonteditor.plugin.FontWriterPlugin;
import com.keitaitoys.util.Utilities;

public class Cocos2dFNTFontWriterPlugin implements FontWriterPlugin {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final int DEFAULT_SPACE_WIDTH = 1; 
	
	private static final String[] formatNames = {
		"fnt"
	};

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private final FileFilter fileFilter;
	
	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public Cocos2dFNTFontWriterPlugin() {
		
		fileFilter = new Cocos2dFNTFileFilter();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public String getName() {
		
		return "Cocos2dFNTFontWriterPlugin";
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public String getVersion() {
		
		return "1.0.0";
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public String getProvider() {
		
		return "KeiTaiToys";
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public String getDescription() {
		
		return "Writes font to format compatible with Cocos2d format";
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
		
		OutputStream fileOutputStream = null;
		OutputStream outputStream = null;
		Writer writer = null;
		
		try {
			
			File sourceImageFile = font.getImageFile();
			File destinationImageFile = null;
			
			Image img = (new ImageIcon(sourceImageFile.getAbsolutePath())).getImage();

			Symbol space = font.getSymbol(' ');
			
			outputStream = new ByteArrayOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

			TreeSet<Symbol> symbols = new TreeSet<Symbol>(font.getSymbols());

			String s = "info face=\"Font\" size="+font.getSize()+" bold=0 italic=0 charset=\"\" unicode=0 stretchH="+img.getHeight(null)+" smooth=0 aa=0 padding=0,0,0,0 spacing="+font.getSpacing()+","+font.getSpacing()+"\n";
			s += "common lineHeight="+font.getLeading()+font.getSize()+" base="+font.getSize()+" scaleW="+img.getWidth(null)+" scaleH="+img.getHeight(null)+" pages=1 packed=0";
			s += "page id=0 file=\""+sourceImageFile.getName()+"\"";
			s += "chars count="+symbols.size();
			
			writer.write(s);
			
			for(Symbol symbol : symbols) {
				
				if(symbol.getChar() != ' ') {
					
					String str = "";

					str += "char id="+(int)symbol.getChar();
					str += " x="+symbol.getBoundsX();
					str += " y="+symbol.getBoundsY();
					str += " width="+symbol.getBoundsWidth();
					str += " height="+symbol.getBoundsHeight();
					str += " xoffset="+symbol.getOffsetX();
					str += " yoffset="+symbol.getOffsetY();
					str += " xadvance="+symbol.getBoundsWidth();
					str += " page=0 chnl=0\n";

					writer.write(str);
				}
			}
			writer.write("kernings count=-1");

			// Writes any uncommited content of the buffer to the stream
			writer.flush();
			
			// Writes all the content of the temporary stream to file stream
			fileOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			((ByteArrayOutputStream)outputStream).writeTo(fileOutputStream);
			
			// Writes any uncommited content of the buffer to the stream
			fileOutputStream.flush();
			
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
			
			if(outputStream != null) {
				
				try {
					outputStream.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}

			if(fileOutputStream != null) {
				
				try {
					fileOutputStream.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private String addQuotes(String string) {
		
		return "\"" + string + "\"";
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private String addLeadingSlash(String string) {
		
		if("\"".equals(string)) string = "\\" + string;
		
		return string;
	}
}
