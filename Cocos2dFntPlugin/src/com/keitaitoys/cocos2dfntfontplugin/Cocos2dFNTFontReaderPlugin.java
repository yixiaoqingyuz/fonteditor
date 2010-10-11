package com.keitaitoys.cocos2dfntfontplugin;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.swing.filechooser.FileFilter;

import com.keitaitoys.fonteditor.font.Symbol;
import com.keitaitoys.fonteditor.font.Font;
import com.keitaitoys.fonteditor.io.FontReadFailedException;
import com.keitaitoys.fonteditor.plugin.FontReaderPlugin;

public class Cocos2dFNTFontReaderPlugin implements FontReaderPlugin {

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

	public Cocos2dFNTFontReaderPlugin() {
		
		fileFilter = new Cocos2dFNTFileFilter();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public String getName() {
		
		return "Cocos2dFNTFontReaderPlugin";
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
		
		return "Reads font compatible with Cocos2d font format";
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
		BufferedReader reader = null;
		
		try {
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			// Читаем путь к файлу картинки
			String[] tokens = reader.readLine().split(" ");
			
			if(!tokens[0].equals("fontPath")) throw new Exception();

			if(removeQuotes(tokens[1]).length() > 0) {
				font.setImageFile(new File(file.getParent() + File.separator + removeQuotes(tokens[1])));
			} else {
				font.setImageFile(null);
			}

			// Читаем количество символов
			tokens = reader.readLine().split(" ");
			
			if(!tokens[0].equals("chars")) throw new Exception();
			int chars = Integer.parseInt(tokens[1]);

			// Читаем высоту шрифта
			tokens = reader.readLine().split(" ");
			
			if(!tokens[0].equals("height")) throw new Exception();
			font.setSize(Integer.parseInt(tokens[1]));

			// Читаем ширину пробела
			tokens = reader.readLine().split(" ");
			
			if(!tokens[0].equals("spaceWidth")) throw new Exception();
			int spaceWidth = Integer.parseInt(tokens[1]);
			Symbol space = new Symbol(' ', new Rectangle(0, 0, spaceWidth > 0 ? spaceWidth : DEFAULT_SPACE_WIDTH, 1), new Point(0, 0));
			font.addSymbol(space);

			// Читаем символы
			for(int i = 0; i < chars; i++) {

				tokens = reader.readLine().split(" ");
				
				String token = removeLeadingSlash(removeQuotes(tokens[0]));
				if(token.length() != 1) throw new Exception();
				
				char c = token.charAt(0); 
				
				int x = Integer.parseInt(tokens[1]);
				int y = Integer.parseInt(tokens[2]);
				int width = Integer.parseInt(tokens[3]);
				int height = Integer.parseInt(tokens[4]);
				
				int offsetX = Integer.parseInt(tokens[5]);
				int offsetY = Integer.parseInt(tokens[6]);
				
				Symbol symbol = font.getSymbol(c);
				if(symbol != null) font.removeSymbol(symbol);

				symbol = new Symbol(c, new Rectangle(x, y, width, height), new Point(offsetX, offsetY));
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

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public Font read2(File file) throws FontReadFailedException {
		
		Font font = new Font(); 
		BufferedReader reader = null;

		try {
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			StringTokenizer stringTokenizer = null;
			String line = null;
			String token  = null;
			
			// Читаем путь к файлу картинки
			line = reader.readLine();
			stringTokenizer = new StringTokenizer(line);
			
			token = stringTokenizer.nextToken(); 
			if(!token.equals("fontPath")) throw new Exception();
			
			token = removeQuotes(stringTokenizer.nextToken());
			font.setImageFile(new File(file.getParent() + File.separator + token));

			// Читаем количество символов
			line = reader.readLine();
			stringTokenizer = new StringTokenizer(line);
			
			token = stringTokenizer.nextToken(); 
			if(!token.equals("chars")) throw new Exception();
			
			token = stringTokenizer.nextToken(); 
			int chars = Integer.parseInt(token);

			// Читаем высоту шрифта
			line = reader.readLine();
			stringTokenizer = new StringTokenizer(line);
			
			token = stringTokenizer.nextToken(); 
			if(!token.equals("height")) throw new Exception();

			token = stringTokenizer.nextToken(); 
			font.setSize(Integer.parseInt(token));

			// Читаем ширину пробела
			line = reader.readLine();
			stringTokenizer = new StringTokenizer(line);
			
			token = stringTokenizer.nextToken(); 
			if(!token.equals("spaceWidth")) throw new Exception();

			token = stringTokenizer.nextToken(); 
			int spaceWidth = Integer.parseInt(token);
			Symbol space = new Symbol(' ', new Rectangle(0, 0, spaceWidth > 0 ? spaceWidth : DEFAULT_SPACE_WIDTH, 1), new Point(0, 0));
			font.addSymbol(space);

			// Читаем символы
			for(int i = 0; i < chars; i++) {
				
				line = reader.readLine();
				stringTokenizer = new StringTokenizer(line);
				
				token = removeLeadingSlash(removeQuotes(stringTokenizer.nextToken()));
				if(token.length() != 1) throw new Exception();
				
				char c = token.charAt(0); 
				
				int x = Integer.parseInt(stringTokenizer.nextToken());
				int y = Integer.parseInt(stringTokenizer.nextToken());
				int width = Integer.parseInt(stringTokenizer.nextToken());
				int height = Integer.parseInt(stringTokenizer.nextToken());
				int offsetX = Integer.parseInt(stringTokenizer.nextToken());
				int offsetY = Integer.parseInt(stringTokenizer.nextToken());
				
				Symbol symbol = font.getSymbol(c);
				if(symbol != null) font.removeSymbol(symbol);

				symbol = new Symbol(c, new Rectangle(x, y, width, height), new Point(offsetX, offsetY));
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

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private String removeQuotes(String string) {
		
		if(string.startsWith("\"")) string = string.substring(1, string.length());
		if(string.endsWith("\"")) string = string.substring(0, string.length() - 1);
		
		return string;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private String removeLeadingSlash(String string) {
		
		if(string.startsWith("\\")) string = string.substring(1, string.length());
		
		return string;
	}
}
