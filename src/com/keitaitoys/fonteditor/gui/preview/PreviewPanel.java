package com.keitaitoys.fonteditor.gui.preview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.keitaitoys.fonteditor.core.FontManager;
import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.core.PreferencesManager;
import com.keitaitoys.fonteditor.event.FontManagerChangeEvent;
import com.keitaitoys.fonteditor.event.FontManagerChangeListener;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;
import com.keitaitoys.fonteditor.event.PreferencesChangeEvent;
import com.keitaitoys.fonteditor.event.PreferencesChangeListener;
import com.keitaitoys.fonteditor.event.FontManagerChangeEvent.FontManagerChangeType;
import com.keitaitoys.fonteditor.event.PreferencesChangeEvent.PreferencesChangeType;
import com.keitaitoys.fonteditor.font.Symbol;
import com.keitaitoys.fonteditor.font.Font;
import com.keitaitoys.fonteditor.font.FontChangeEvent;
import com.keitaitoys.fonteditor.font.FontChangeListener;

public class PreviewPanel extends JPanel implements LocaleChangeListener, PreferencesChangeListener, FontManagerChangeListener, FontChangeListener, ComponentListener {
	
	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String PREVIEW_TEXT_LOCALIZE_KEY = "preview.text";
	
	public static final int MODE_NONE = 0;
	public static final int MODE_CLASSIC = 1;
	public static final int MODE_FONT = 2;

	public static final int OFFSET = 5;
	
	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JScrollPane scrollPane;

	private BufferedImage image;
//	private String fox = "test nin cake spi{t";
//	private String fox = "test niR cake spi{t";
//	private String fox = "wesp in-t cake spi{t";
//	private String fox = "ieipoin-t cake spi{t";
//	private String fox = "anabcdefghijklmno pqrstuvwxyz";
//	private String fox = "ФkФ p Ф upФper on";
	
//	private String previewText = "A quick brown fox jumps over the lazy dog.";
	private String previewText;
	private String text;
	
	private Color backgroundColor;
	
	private int previewMode;
	
	private ArrayList<Symbol> symbols;
	private ArrayList<Point> coords;
	
	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public PreviewPanel() {
		
		Manager manager = Manager.getInstance();
		manager.registerPreviewPanel(this);

		LocaleManager localeManager = LocaleManager.getInstance();
		localeManager.addLocaleChangeListener(this);

		PreferencesManager preferencesManager = PreferencesManager.getInstance();
		preferencesManager.addPreferencesChangeListener(this);

		FontManager fontManager = FontManager.getInstance();
		fontManager.addFontManagerChangeListener(this);

		Font font = fontManager.getFont();
		
		if(font != null) {
			font.addFontChangeListener(this);
		}
		
		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		PreferencesManager preferencesManager = PreferencesManager.getInstance();
		
		previewMode = preferencesManager.getPreviewMode();
		backgroundColor = preferencesManager.getPreviewBackgroundColor();

//		localize();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public synchronized JScrollPane getScrollPane() {
		
		if(scrollPane == null) {
			
			scrollPane = new JScrollPane(this);
			scrollPane.getViewport().addComponentListener(this);
		}
		
		return scrollPane;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void update() {
		
		createText();
		parse();
		
		repaint();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g.create();
		
		int width = getWidth();
		int height = getHeight();
		
		g2.setColor(backgroundColor);
		g2.fillRect(0, 0, width, height);
		
		if(image != null && text != null) {
			
			Rectangle imageRectangle = new Rectangle(image.getWidth(), image.getHeight());

			for(int i = 0; i < symbols.size(); i++) {

				Symbol symbol = symbols.get(i);
				Point coord = coords.get(i);
				
				Rectangle symbolRectangle = symbol.getBounds();
				
				if(imageRectangle.contains(symbolRectangle)) {
					g2.drawImage(image.getSubimage(symbol.getBoundsX(), symbol.getBoundsY(), symbol.getBoundsWidth(), symbol.getBoundsHeight()), null, OFFSET + coord.x + symbol.getOffsetX(), OFFSET + coord.y + symbol.getOffsetY());
				} else {
					g2.setColor(Color.BLACK);
					g2.fillRect(coord.x + symbol.getOffsetX(), coord.y + symbol.getOffsetY(), symbol.getBoundsWidth(), symbol.getBoundsHeight());
				}
			}
		}

		g2.dispose();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private void parse() {
		
		if(text == null) return;
		
		Font font = FontManager.getInstance().getFont();
		
		int fontSize = font.getSize();
		int interCharSpace = font.getSpacing();
		int interStringSpace = font.getLeading();

		int placeHolderSize = 5;
		
		int currentCharX = 0;
		int currentCharY = 0;
		int currentCharWidth = 0;
		
		int nextCharX = 0;
		int nextCharY = 0;
		
		symbols = new ArrayList<Symbol>();
		coords = new ArrayList<Point>();

		int possibleLineStartIndex = -1;
		int rollbackCount = 0;

		int width = getWidth() - OFFSET * 2;
		
		boolean ignoreWidth = false;

//		System.out.println("fontHeight = " + fontHeight);
//		System.out.println("placeHolderSize = " + placeHolderSize);
//		System.out.println("width = " + width);

		for(int i = 0; i < text.length(); i++) {

//			System.out.println("i " + i);

			char c = text.charAt(i);
			Symbol symbol = font.getSymbol(c);

			currentCharX = nextCharX;
			currentCharY = nextCharY;

			if(symbol != null) {
				currentCharWidth = symbol.getBoundsWidth() + symbol.getOffsetX();
			} else {
				currentCharWidth = placeHolderSize;
			}

			nextCharX = currentCharX + currentCharWidth;

//			System.out.println("char = " + c + ", symbol = " + symbol);
//			System.out.println("currentCharX = " + currentCharX + ", nextCharX = " + nextCharX);

			// Если текущий символ перенос на новую строку
			if(c == '\n') {
				
//				System.out.println("we have end of string!");

				nextCharX = 0;
				nextCharY += (fontSize + interStringSpace);

				possibleLineStartIndex = -1;
				rollbackCount = 0;
				
			// Если текущий символ не помещается по ширине
			} else if(nextCharX > width && c != ' ' && !ignoreWidth) {

//				System.out.println("we are out of bounds!");

//				System.out.println("char remains on current line: " + (currentCharX == 0));
//				System.out.println("char marked as wide: " + (currentCharWidth > width));

				nextCharX = 0;
				nextCharY += currentCharX == 0 ? 0 : fontSize + interStringSpace;
				
				ignoreWidth = currentCharWidth > width;

				if(possibleLineStartIndex == -1) {

//					System.out.println("breaking is starting " + i);
					
					i = i - 1; 

				} else {
				
//					System.out.println("breaking is starting " + possibleLineStartIndex);
//					System.out.println("rollbackCount = " + rollbackCount + ", symbols.size() = " + symbols.size());

					i = possibleLineStartIndex - 1;
					
					for(int j = 0; j < rollbackCount; j++) {
						symbols.remove(symbols.size() - 1);
						coords.remove(coords.size() - 1);
					}
				}

				possibleLineStartIndex = -1;
				rollbackCount = 0;

			// Если текущий символ помещается по ширине
			} else {

//				System.out.println("we are OK!");
				
				nextCharX += interCharSpace;
				
				if(symbol != null) {
					
//					System.out.println("adding symbol " + symbol + " to point " + new Point(currentCharX, currentCharY));

					symbols.add(symbol);
					coords.add(new Point(currentCharX, currentCharY));
					rollbackCount++;
				}
				
				if(c == ' ' || c == '-') {
					
					possibleLineStartIndex = i + 1;
					
//					System.out.println("found possible break in " + possibleLineStartIndex);

					rollbackCount = 0;
				}
				
				ignoreWidth = false;
			}
		}
		
		// Set panel height to corresponding value
		// making vertical scroll bar appear if needed
		// We don't really care about the horizontal
		// scroll bar, so width is always set to 0
		// letting control the actual width
		// by viewport itself. In this case we would 
		// always work with currently available width
		Dimension size = new Dimension(0, nextCharY + fontSize + OFFSET * 2);
		setPreferredSize(size);
		revalidate();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void createText() {
		
		if(previewText != null && previewText.length() > 0) {
			
			text = previewText; 

		} else {
			
			if(previewMode == MODE_NONE) {
				
				text = null;
				
			} else if(previewMode == MODE_CLASSIC) {
				
				LocaleManager localeManager = LocaleManager.getInstance();
				text = localeManager.getValue(PREVIEW_TEXT_LOCALIZE_KEY);
				
			} else if(previewMode == MODE_FONT) {
				
				text = null;
				
				Font font = FontManager.getInstance().getFont();
				
				if(font != null) {
					
					Set<Symbol> symbols = font.getSymbols();
					Object[] sortedSymbols = symbols.toArray();
					Arrays.sort(sortedSymbols);

					StringBuffer stringBuffer = new StringBuffer();

					for(Object object : sortedSymbols) {
						stringBuffer.append(((Symbol)object).getChar());
					}

					text = stringBuffer.toString();
				}
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public BufferedImage getImage() {
		
		return image;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public String getPreviewText() {
		
		return previewText;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void setImage(BufferedImage image) {
		
		this.image = image;
		update();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void setPreviewText(String previewText) {
		
		this.previewText = previewText;
		update();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void preferencesChange(PreferencesChangeEvent e) {
		
		PreferencesManager preferencesManager = PreferencesManager.getInstance();
		
		PreferencesChangeType type = e.getType();
		
		if(type == PreferencesChangeType.PREVIEW_MODE_CHANGED) {
			
			previewMode = preferencesManager.getPreviewMode();
			update();

		} else if(type == PreferencesChangeType.PREVIEW_BACKGROUND_COLOR_CHANGED) {
			
			backgroundColor = preferencesManager.getPreviewBackgroundColor();
			repaint();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void fontChange(FontChangeEvent e) {
		
		update();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
    public void fontManagerChange(FontManagerChangeEvent e) {
    	
    	FontManagerChangeType type = e.getType();
    	
		if(type == FontManagerChangeType.FONT_CHANGED) {
			
    		Font font = FontManager.getInstance().getFont();
    		
    		if(font != null) {
        		font.addFontChangeListener(this);
    		}
    		
    		update();
		}
    }

    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void componentResized(ComponentEvent e) {

		update();
	}

    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void componentMoved(ComponentEvent e) {

	}

    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void componentShown(ComponentEvent e) {

	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void componentHidden(ComponentEvent e) {
	
	}

    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void localize() {
		
		update();
	}

    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void localeChange(LocaleChangeEvent e) {
		
		localize();
	}
} 