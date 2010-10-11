package com.keitaitoys.fonteditor.gui.editor;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import com.keitaitoys.fonteditor.core.FontManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.font.Symbol;
import com.keitaitoys.fonteditor.font.Font;

public class MarqueeTool extends Tool {
	
	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final Cursor cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR); 

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private EditorPanel editorPane;

	private Point startPoint;
	private Point endPoint;

	private Point popupPoint;

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public MarqueeTool(EditorPanel editorPane) {
		
		this.editorPane = editorPane;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void install() {
		
		editorPane.addMouseListener(this);
		editorPane.addMouseMotionListener(this);

		if(editorPane.getMousePosition() != null) {
			editorPane.setCursor(cursor);
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void uninstall() {
		
		editorPane.removeMouseListener(this);
		editorPane.removeMouseMotionListener(this);

		if(editorPane.getMousePosition() != null) {
			editorPane.setCursor(null);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private Rectangle createOutline() {
		
		BufferedImage scaledImage = editorPane.getScaledImage(); 
		
		Rectangle outlineRectangle = null;

		if(startPoint != null && endPoint != null && scaledImage != null) {
			
			double scale = editorPane.getScale();
			
			int scaledImageWidth = scaledImage.getWidth();
			int scaledImageHeight = scaledImage.getHeight();
			
			int scaledImageX = (editorPane.getWidth() - scaledImageWidth) / 2;
			int scaledImageY = (editorPane.getHeight() - scaledImageHeight) / 2;

			Point startPoint = new Point(Math.min(this.startPoint.x, this.endPoint.x), Math.min(this.startPoint.y, this.endPoint.y));
			Point endPoint = new Point(Math.max(this.startPoint.x, this.endPoint.x), Math.max(this.startPoint.y, this.endPoint.y));
			
			double offsetX = ((double)scaledImageX % scale) - scale;
			double offsetY = ((double)scaledImageY % scale) - scale;
			
//			System.out.println("scale = " + scale);
//			System.out.println("scaledImageX = " + scaledImageX);
//			System.out.println("scaledImageY = " + scaledImageY);
			
//			System.out.println("offsetX = " + offsetX);
//			System.out.println("offsetY = " + offsetY);
			
//			System.out.println("1) startPoint = " + startPoint);
			startPoint.x -= (int)(((double)startPoint.x - offsetX) % scale);
			startPoint.y -= (int)(((double)startPoint.y - offsetY) % scale);
//			System.out.println("2) startPoint = " + startPoint);

//			System.out.println("1) endPoint = " + endPoint);
			endPoint.x += (int)(scale - (((double)endPoint.x - offsetX) % scale));
			endPoint.y += (int)(scale - (((double)endPoint.y - offsetY) % scale));
//			System.out.println("2) endPoint = " + endPoint);
			
    		Rectangle scaledOutlineRectangle = new Rectangle(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
			Rectangle scaledImageRectangle = new Rectangle(scaledImageX, scaledImageY, scaledImageWidth, scaledImageHeight);
			
//			System.out.println("scaledOutlineRectangle = " + scaledOutlineRectangle);
//			System.out.println("scaledImageRectangle   = " + scaledImageRectangle);

			// Находим пересечение указанной пользователем зоны и зоны картинки шрифта 
			scaledOutlineRectangle = scaledOutlineRectangle.intersection(scaledImageRectangle);
//			System.out.println("interImageRectangle   = " + scaledOutlineRectangle);
			
			// Если найденное пересечение существует и оно не пустое
			if(scaledOutlineRectangle != null && !scaledOutlineRectangle.isEmpty()) {
				
    			// Переводим к системе координат картинки  
    			scaledOutlineRectangle.x -= scaledImageX;
    			scaledOutlineRectangle.y -= scaledImageY;
    			
    			int x = (int)((double)scaledOutlineRectangle.x / scale);
    			int y = (int)((double)scaledOutlineRectangle.y / scale);
    			int width = (int)((double)scaledOutlineRectangle.width / scale);
    			int height = (int)((double)scaledOutlineRectangle.height / scale);
    			
//    			System.out.println("x = " + x + ", y = " + y);
//       			System.out.println("w = " + width + ", h = " + height);
    			
    			outlineRectangle = new Rectangle(x, y, width, height);
//    			System.out.println("outlineRectangle   = " + outlineRectangle);
			}
			
//			System.out.println();    			
		}
		
		return outlineRectangle;
	}
		
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private Symbol getSelectedSymbol() {
		
		BufferedImage scaledImage = editorPane.getScaledImage(); 
		
		Symbol selectedSymbol = null;
		
		if(startPoint != null && scaledImage != null) {
			
			Font font = FontManager.getInstance().getFont();
			Set<Symbol> symbols = font.getSymbols();
			
			double scale = editorPane.getScale();

			int scaledImageWidth = scaledImage.getWidth();
			int scaledImageHeight = scaledImage.getHeight();
			
			int scaledImageX = (editorPane.getWidth() - scaledImageWidth) / 2;
			int scaledImageY = (editorPane.getHeight() - scaledImageHeight) / 2;
			
			Point point = new Point(startPoint.x, startPoint.y);
			
			// Переводим к системе координат картинки  
			point.x -= scaledImageX;
			point.y -= scaledImageY;
			
			for(Symbol symbol : symbols) {
				
				Rectangle symbolBounds = symbol.getBounds();
				
				int x = (int)((double)symbolBounds.x * scale);
				int y = (int)((double)symbolBounds.y * scale);
				int width = (int)((double)symbolBounds.width * scale);
				int height = (int)((double)symbolBounds.height * scale);
				
				Rectangle scaledSymbolBounds = new Rectangle(x, y, width, height);
				
				if(scaledSymbolBounds.contains(point)) {
					
					selectedSymbol = symbol;
					break;
				}
			}
		}
		
		return selectedSymbol;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private Set<Symbol> getSelectedSymbols() {
		
		BufferedImage scaledImage = editorPane.getScaledImage(); 
		
		Set<Symbol> selectedSymbols = new HashSet<Symbol>();
		
		if(popupPoint != null && scaledImage != null) {
			
			Font font = FontManager.getInstance().getFont();
			Set<Symbol> symbols = font.getSymbols();
			
			double scale = editorPane.getScale();

			int scaledImageWidth = scaledImage.getWidth();
			int scaledImageHeight = scaledImage.getHeight();
			
			int scaledImageX = (editorPane.getWidth() - scaledImageWidth) / 2;
			int scaledImageY = (editorPane.getHeight() - scaledImageHeight) / 2;
			
			Point point = new Point(popupPoint.x, popupPoint.y);
			
			// Переводим к системе координат картинки  
			point.x -= scaledImageX;
			point.y -= scaledImageY;
			
			for(Symbol symbol : symbols) {
				
				Rectangle symbolBounds = symbol.getBounds();
				
				int x = (int)((double)symbolBounds.x * scale);
				int y = (int)((double)symbolBounds.y * scale);
				int width = (int)((double)symbolBounds.width * scale);
				int height = (int)((double)symbolBounds.height * scale);
				
				Rectangle scaledSymbolBounds = new Rectangle(x, y, width, height);
				
				if(scaledSymbolBounds.contains(point)) {
					
					selectedSymbols.add(symbol);
				}
			}
		}
		
		return selectedSymbols;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void mouseEntered(MouseEvent e) {
		
		editorPane.setCursor(cursor);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void mouseExited(MouseEvent e) {
		
		editorPane.setCursor(null);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void mousePressed(MouseEvent e) {
		
		if(SwingUtilities.isLeftMouseButton(e)) {
			
			startPoint = new Point(e.getX(), e.getY());
			Manager.getInstance().executeSetOutline(null);
		}
		
		if(e.isPopupTrigger()) {

			popupPoint = new Point(e.getX(), e.getY());
			Set<Symbol> symbols = getSelectedSymbols(); 
			
			if(symbols.size() > 0) {
				Manager.getInstance().executeCharPopupMenu(symbols, popupPoint.x, popupPoint.y);
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void mouseDragged(MouseEvent e) {
		
		if(SwingUtilities.isLeftMouseButton(e)) {
			
			endPoint = new Point(e.getX(), e.getY());
			Manager.getInstance().executeSetOutline(createOutline());
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void mouseReleased(MouseEvent e) {

		if(e.isPopupTrigger()) {

			popupPoint = new Point(e.getX(), e.getY());
			Set<Symbol> symbols = getSelectedSymbols(); 
			
			if(symbols.size() > 0) {
				Manager.getInstance().executeCharPopupMenu(symbols, popupPoint.x, popupPoint.y);
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void mouseClicked(MouseEvent e) {

		if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 1) {
		
			Symbol c = getSelectedSymbol();
			
			if(c != null) {
				Manager.getInstance().executeSetSelected(c);
			}
		}
	}
}