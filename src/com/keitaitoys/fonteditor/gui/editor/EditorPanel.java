package com.keitaitoys.fonteditor.gui.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.keitaitoys.fonteditor.core.FontManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.core.PreferencesManager;
import com.keitaitoys.fonteditor.event.FontManagerChangeEvent;
import com.keitaitoys.fonteditor.event.FontManagerChangeListener;
import com.keitaitoys.fonteditor.event.PreferencesChangeEvent;
import com.keitaitoys.fonteditor.event.PreferencesChangeListener;
import com.keitaitoys.fonteditor.event.FontManagerChangeEvent.FontManagerChangeType;
import com.keitaitoys.fonteditor.event.PreferencesChangeEvent.PreferencesChangeType;
import com.keitaitoys.fonteditor.font.Symbol;
import com.keitaitoys.fonteditor.font.Font;
import com.keitaitoys.fonteditor.font.FontChangeEvent;
import com.keitaitoys.fonteditor.font.FontChangeListener;

public class EditorPanel extends JPanel implements PreferencesChangeListener, FontManagerChangeListener, FontChangeListener {
	
	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final int DEFAULT_SCALE_INDEX = 0;

	private static final float DASH_SIZE = 4.0f;
	private static final float[] DASH_PATTERN = {DASH_SIZE, DASH_SIZE};
	
	private static final int CHAR_COLOR_ALPHA = 75; 
	
	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JScrollPane scrollPane;
	
	private BufferedImage image;
	private BufferedImage scaledImage;

	private Color boundsColor; 
	private Color backgroundMajorColor; 
	private Color backgroundMinorColor; 

	private Rectangle outline; 
	private Rectangle scaledOutline; 

	private GeneralPath marquee;

	private BufferedImage tile;
	
	private final double[] scaleFactor = {
			
		1.0d,
		2.0d,
		3.0d,
		4.0d,
		5.0d,
		6.0d,
		7.0d,
		8.0d,
		12.0d,
		16.0d,
		32.0d
	};

	private final int[] gridSize = {
			
		8,
		4,
		2,
		2,
		1,
		1,
		1,
		1,
		1,
		1,
		1
	};

	private int scaleIndex;

	private Tool tool;
	private Tool marqueeTool;
	private Tool selectTool;
	
	private Ticker ticker;
	private float dashOffset;
	
	private boolean grid;
	private boolean bounds;
	
	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public EditorPanel() {
		
		Manager manager = Manager.getInstance();
		manager.registerEditorPanel(this);

		PreferencesManager preferencesManager = PreferencesManager.getInstance();
		preferencesManager.addPreferencesChangeListener(this);
		
		FontManager fontManager = FontManager.getInstance();
		fontManager.addFontManagerChangeListener(this);

		Font font = fontManager.getFont();
		
		if(font != null) {
			font.addFontChangeListener(this);
		}
		
		createKeyBindings();
		
		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		scaleIndex = DEFAULT_SCALE_INDEX;
		
		setFocusable(true);

		PreferencesManager preferencesManager = PreferencesManager.getInstance();
		
		grid = preferencesManager.isEditorGrid();
		bounds = preferencesManager.isEditorBounds();

		boundsColor = preferencesManager.getEditorBoundsColor();
		backgroundMajorColor = preferencesManager.getEditorBackgroundMajorColor();
		backgroundMinorColor = preferencesManager.getEditorBackgroundMinorColor();
		
		tile = createBackgroundTile();
		
		Handler handler = new Handler();
		addMouseListener(handler);
		addMouseMotionListener(handler);
		addComponentListener(handler);
		
		marqueeTool = new MarqueeTool(this);
		selectTool = new SelectTool(this);

		tool = marqueeTool;
		tool.install();
		
		ticker = new Ticker();
		ticker.start();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void createKeyBindings() {
		
/*
		Action marqueeToolAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				manager.executeMarqueeTool();
			}
		};
		
		Action selectToolAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				manager.executeSelectTool();
			}
		};
*/
		Action addAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().executeAdd();
			}
		};
		
		Action deleteAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().executeDelete();
			}
		};
		
		Action selectAllAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		};
		
		Action clearAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		};
		
		Action outlineMoveUpAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				outlineMoveUp();
			}
		};
		
		Action outlineMoveDownAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				outlineMoveDown();
			}
		};
		
		Action outlineMoveLeftAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				outlineMoveLeft();
			}
		};
		
		Action outlineMoveRightAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				outlineMoveRight();
			}
		};
		
		Action outlineIncreaseWidthAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				outlineIncreaseWidth();
			}
		};
		
		Action outlineIncreaseHeightAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				outlineIncreaseHeight();
			}
		};
		
		Action outlineDecreaseWidthAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				outlineDecreaseWidth();
			}
		};
		
		Action outlineDecreaseHeightAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				outlineDecreaseHeight();
			}
		};
		
//		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "marqueeToolAction");
//		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0), "selectToolAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "addAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK), "selectAllAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK), "clearAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "outlineMoveUpAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "outlineMoveDownAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "outlineMoveLeftAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "outlineMoveRightAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.SHIFT_MASK), "outlineDecreaseHeightAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.SHIFT_MASK), "outlineIncreaseHeightAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.SHIFT_MASK), "outlineDecreaseWidthAction");
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.SHIFT_MASK), "outlineIncreaseWidthAction");

//		getActionMap().put("marqueeToolAction", marqueeToolAction);
//		getActionMap().put("selectToolAction", selectToolAction);
		getActionMap().put("addAction", addAction);
		getActionMap().put("deleteAction", deleteAction);
		getActionMap().put("clearAction", clearAction);
		getActionMap().put("selectAllAction", selectAllAction);
		getActionMap().put("outlineMoveUpAction", outlineMoveUpAction);
		getActionMap().put("outlineMoveDownAction", outlineMoveDownAction);
		getActionMap().put("outlineMoveLeftAction", outlineMoveLeftAction);
		getActionMap().put("outlineMoveRightAction", outlineMoveRightAction);
		getActionMap().put("outlineDecreaseHeightAction", outlineDecreaseHeightAction);
		getActionMap().put("outlineIncreaseHeightAction", outlineIncreaseHeightAction);
		getActionMap().put("outlineDecreaseWidthAction", outlineDecreaseWidthAction);
		getActionMap().put("outlineIncreaseWidthAction", outlineIncreaseWidthAction);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public synchronized JScrollPane getScrollPane() {
		
		if(scrollPane == null) {
			scrollPane = new JScrollPane(this);
		}
		
		return scrollPane;
	}
/*
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void marqueeTool() {
		
		if(tool != marqueeTool) {
			tool(marqueeTool);
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void selectTool() {
		
		if(tool != selectTool) {
			tool(selectTool);
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void tool(Tool tool) {
		
		this.tool.uninstall();
		this.tool = tool;
		this.tool.install();
		
//		clear();
	}
*/
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void selectAll() {
		
		Rectangle outline = null;
		
		if(image != null) {
			
			int x = 0; 
			int y = 0;
			int width = image.getWidth();
			int height = image.getHeight();
			
			outline = new Rectangle(x, y, width, height);
		}
		
		Manager manager = Manager.getInstance();
		manager.executeSetOutline(outline);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void clear() {
		
		Manager manager = Manager.getInstance();
		manager.executeSetOutline(null);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void outlineMoveUp() {
		
		if(image != null && outline != null && !outline.isEmpty()) {
			
			if(outline.y > 0) {
				outline.y -= 1;  
			}
			
			scaledOutline = createScaledOutline(outline);
			update();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void outlineMoveDown() {
		
		if(image != null && outline != null && !outline.isEmpty()) {
			
			if(outline.y + outline.height < image.getHeight()) {
				outline.y += 1;  
			}
			
			scaledOutline = createScaledOutline(outline);
			update();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void outlineMoveLeft() {
		
		if(image != null && outline != null && !outline.isEmpty()) {
			
			if(outline.x > 0) {
				outline.x -= 1;			
			}
			
			scaledOutline = createScaledOutline(outline);
			update();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void outlineMoveRight() {
		
		if(image != null && outline != null && !outline.isEmpty()) {
			
			if(outline.x + outline.width < image.getWidth()) {
				outline.x += 1;
			}
			
			scaledOutline = createScaledOutline(outline);
			update();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void outlineIncreaseWidth() {
		
		if(image != null && outline != null && !outline.isEmpty()) {
			
			if(outline.x + outline.width < image.getWidth()) {
				outline.width += 1;
			}
			
			Manager manager = Manager.getInstance();
			manager.executeSetOutline(outline);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void outlineIncreaseHeight() {
		
		if(image != null && outline != null && !outline.isEmpty()) {
			
			if(outline.y + outline.height < image.getHeight()) {
				outline.height += 1;
			}
			
			Manager manager = Manager.getInstance();
			manager.executeSetOutline(outline);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void outlineDecreaseWidth() {
		
		if(image != null && outline != null && !outline.isEmpty()) {
			
			if(outline.width > 1) {
				outline.width -= 1;
			}
			
			Manager manager = Manager.getInstance();
			manager.executeSetOutline(outline);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void outlineDecreaseHeight() {
		
		if(image != null && outline != null && !outline.isEmpty()) {
			
			if(outline.height > 1) {
				outline.height -= 1;
			}
			
			Manager manager = Manager.getInstance();
			manager.executeSetOutline(outline);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public boolean canZoomIn() {
		
		return scaleIndex < scaleFactor.length - 1;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public boolean canZoomOut() {
		
		return scaleIndex > 0;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void zoomIn() {
		
		if(canZoomIn()) {
			scaleIndex++;
			zoom();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void zoomOut() {
		
		if(canZoomOut()) {
			scaleIndex--;
			zoom();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void zoomActual() {
		
		if(scaleIndex != DEFAULT_SCALE_INDEX) {
			scaleIndex = DEFAULT_SCALE_INDEX;
			zoom();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void zoom() {
		
		scaledImage = createScaledImage(image);

		int width = 0;
		int height = 0;

		if(scaledImage != null) {
			
			width = scaledImage.getWidth();
			height = scaledImage.getHeight();
		}
		
		setPreferredSize(new Dimension(width, height));
		
		// We want component listener to react on any zoom action
		// even when component size is not actually going to change.
		// This may happened when component used as a client inside
		// scrollpane's view. In this case component size would be
		// always not smaller than the view size
		setSize(new Dimension(0, 0));

		scaledOutline = createScaledOutline(outline);

		if(scrollPane != null) {
			
			JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
			JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
			
			double scale = scaleFactor[scaleIndex];
			
			if(horizontalScrollBar != null) {
				horizontalScrollBar.setUnitIncrement((int)scale);
			}
			
			if(verticalScrollBar != null) {
				verticalScrollBar.setUnitIncrement((int)scale);
			}
		}

		revalidate();
		update();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void update() {
		
		repaint();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g.create();

		if(scaledImage != null) {
			
			int translateX = (getWidth() - scaledImage.getWidth()) / 2;
			int translateY = (getHeight() - scaledImage.getHeight()) / 2;

			g2.translate(translateX, translateY);

			int width = scaledImage.getWidth();
			int height = scaledImage.getHeight();
			
			// Draw background texture fill
			g2.setPaint(new TexturePaint(tile, new Rectangle(0, 0, tile.getWidth(), tile.getHeight())));
			g2.fillRect(0, 0, width, height);

			// Draw outer image border
			g2.setColor(Color.BLACK);
			g2.drawRect(-1, -1, scaledImage.getWidth() + 1, scaledImage.getHeight() + 1);

			// Draw image
			g2.drawImage(scaledImage, null, 0, 0);

			// Draw grid
			if(grid) {
				
				g2.setColor(Color.GRAY);
				int step = (int)((double)gridSize[scaleIndex] * scaleFactor[scaleIndex]); 
				
				for(int i = 0; i < scaledImage.getWidth(); i += step) {
					g2.drawLine(i, 0, i, scaledImage.getHeight() - 1);
				}
				
				for(int i = 0; i < scaledImage.getHeight(); i += step) {
					g2.drawLine(0, i, scaledImage.getWidth() - 1, i);
				}
			}
			
			// Draw bounds
			if(bounds) {
				
				Set<Symbol> symbols = FontManager.getInstance().getFont().getSymbols();
				
				for(Symbol symbol : symbols) {
					
					Rectangle scaledBounds = createScaledBounds(symbol.getBounds());
					
					if(scaledBounds != null && !scaledBounds.isEmpty()) {
						
						g2.setColor(new Color(boundsColor.getRed(), boundsColor.getGreen(), boundsColor.getBlue(), CHAR_COLOR_ALPHA));
						g2.fill(scaledBounds);

						scaledBounds.width--;
						scaledBounds.height--;

						g2.setColor(boundsColor);
						g2.draw(scaledBounds);
					}
				}
			}

			// Draw marquee
			marquee = createMarquee();
			
			if(marquee != null) {
				
				Stroke defaultStroke = g2.getStroke();
				
				g2.setColor(new Color(0, 0, 0));
				g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, DASH_PATTERN, dashOffset));
				g2.draw(marquee);
				
				g2.setColor(new Color(255, 255, 255));
				g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, DASH_PATTERN, dashOffset + DASH_SIZE));
				g2.draw(marquee);
				
				g2.setStroke(defaultStroke);
			}
		}
		
		g2.dispose();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	protected BufferedImage createScaledImage(BufferedImage image) {
		
		BufferedImage scaledImage = null;
		
		if(image != null) {
			
			double scale = scaleFactor[scaleIndex];
			
			AffineTransform at = new AffineTransform();
			at.scale(scale, scale);

			AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			scaledImage = ato.filter(image, null);
		}
		
		return scaledImage;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	protected Rectangle createScaledOutline(Rectangle outline) {
		
		Rectangle scaledRectangle = null;
		
		if(outline != null && !outline.isEmpty()) {
			
			double scale = scaleFactor[scaleIndex];
			
			int x = (int)((double)outline.x * scale);
			int y = (int)((double)outline.y * scale);
			
			int width = (int)((double)outline.width * scale);
			int height = (int)((double)outline.height * scale);
			
			scaledRectangle = new Rectangle(x, y, width, height);
		}
		
		return scaledRectangle;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	protected Rectangle createScaledBounds(Rectangle bounds) {
		
		Rectangle scaledBounds = null;
		
		if(bounds != null && !bounds.isEmpty()) {
			
			double scale = scaleFactor[scaleIndex];
			
			int x = (int)((double)bounds.x * scale);
			int y = (int)((double)bounds.y * scale);
			
			int width = (int)((double)bounds.width * scale);
			int height = (int)((double)bounds.height * scale);
			
			scaledBounds = new Rectangle(x, y, width, height);
		}
		
		return scaledBounds;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	protected BufferedImage createBackgroundTile() {
		
		BufferedImage backgroundTile = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = backgroundTile.createGraphics();
		
		g2.setColor(backgroundMajorColor);
		g2.fillRect(0, 0, 8, 8);
		g2.fillRect(8, 8, 8, 8);
		
		g2.setColor(backgroundMinorColor);
		g2.fillRect(0, 8, 8, 8);
		g2.fillRect(8, 0, 8, 8);
		
		g2.dispose();
		
		return backgroundTile; 
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	protected GeneralPath createMarquee() {
		
		GeneralPath marquee = null;
		
		if(scaledOutline != null && !scaledOutline.isEmpty()) {
			
			marquee = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
			
			marquee.moveTo(scaledOutline.x, scaledOutline.y);
			marquee.lineTo(scaledOutline.x, scaledOutline.y + scaledOutline.height - 1.0d);
			marquee.lineTo(scaledOutline.x + scaledOutline.width - 1.0d, scaledOutline.y + scaledOutline.height - 1.0d);
			
			marquee.moveTo(scaledOutline.x, scaledOutline.y);
			marquee.lineTo(scaledOutline.x + scaledOutline.width - 1.0d, scaledOutline.y);
			marquee.lineTo(scaledOutline.x + scaledOutline.width - 1.0d, scaledOutline.y + scaledOutline.height - 1.0d);
		}
		
		return marquee;
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

	public BufferedImage getScaledImage() {
		
		return scaledImage;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public Rectangle getOutline() {
		
		return outline;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public Rectangle getScaledOutline() {
		
		return scaledOutline;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void setImage(BufferedImage image) {
		
		this.image = image;
		scaledImage = createScaledImage(image);
		
		int width = 0;
		int height = 0;
		
		if(scaledImage != null) {
			
			width = scaledImage.getWidth();
			height = scaledImage.getHeight();
		}
		
		setPreferredSize(new Dimension(width, height));

		clear();
		
		revalidate();
		update();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void setOutline(Rectangle outline) {
		
		this.outline = outline;
		scaledOutline = createScaledOutline(outline);
		
		ticker.pause(outline == null || outline.isEmpty());
		
		update();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public double getScale() {
		
		return scaleFactor[scaleIndex];
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void preferencesChange(PreferencesChangeEvent e) {
		
		PreferencesManager preferencesManager = PreferencesManager.getInstance();
		
		PreferencesChangeType type = e.getType();
		
		if(type == PreferencesChangeType.EDITOR_GRID_CHANGE) {

			grid = preferencesManager.isEditorGrid();
			repaint();

		} else if(type == PreferencesChangeType.EDITOR_BOUNDS_CHANGE) {
			
			bounds = preferencesManager.isEditorBounds();
			repaint();
			
		} else if(type == PreferencesChangeType.EDITOR_BOUNDS_COLOR_CHANGE) {
			
			boundsColor = preferencesManager.getEditorBoundsColor();
			update();

		} else if(type == PreferencesChangeType.EDITOR_BACKGROUND_COLOR_CHANGED) {
			
			backgroundMajorColor = preferencesManager.getEditorBackgroundMajorColor();
			backgroundMinorColor = preferencesManager.getEditorBackgroundMinorColor();
			tile = createBackgroundTile();
			
			update();
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
    		
			Manager manager = Manager.getInstance();
    		manager.executeZoomActual();
    		
    		update();
    	}
    }

	//////////////////////////////////////////////////////////////////////
	// Inner class ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	class Handler implements MouseListener, MouseMotionListener, ComponentListener {
		
		//////////////////////////////////////////////////////////////////////
		// Functions /////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////

		private Point createPoint(Point scaledPoint) {
			
			Point point = null;
			
			if(scaledPoint != null && scaledImage != null) {
				
				int scaledImageWidth = scaledImage.getWidth();
				int scaledImageHeight = scaledImage.getHeight();

				int scaledImageX = (getWidth() - scaledImageWidth) / 2;
				int scaledImageY = (getHeight() - scaledImageHeight) / 2;

				Rectangle scaledImageRectangle = new Rectangle(scaledImageX, scaledImageY, scaledImageWidth, scaledImageHeight);
				
				if(scaledImageRectangle.contains(scaledPoint)) {
					
					double scale = scaleFactor[scaleIndex];

					int x = (int)((double)(scaledPoint.x - scaledImageX) / scale); 
					int y = (int)((double)(scaledPoint.y - scaledImageY) / scale); 
					
					point = new Point(x, y); 
				}
			}
			
			return point;
		}

		//////////////////////////////////////////////////////////////////////
    	// Functions /////////////////////////////////////////////////////////
    	//////////////////////////////////////////////////////////////////////

		public void mouseEntered(MouseEvent e) {
			
			if(isEnabled() && !hasFocus() && isRequestFocusEnabled()) {
				requestFocus();
			}            
		}

		//////////////////////////////////////////////////////////////////////
		// Functions /////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////

		public void mouseExited(MouseEvent e) {
			
			Manager manager = Manager.getInstance();
			manager.executeSetCoord(null);
		}
		
		//////////////////////////////////////////////////////////////////////
		// Functions /////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////

		public void mouseDragged(MouseEvent e) {
			
			Point point = createPoint(e.getPoint());
			
			Manager manager = Manager.getInstance();
			manager.executeSetCoord(point);
		}

		//////////////////////////////////////////////////////////////////////
		// Functions /////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////

		public void mouseMoved(MouseEvent e) {

			Point point = createPoint(e.getPoint()); 
			
			Manager manager = Manager.getInstance();
			manager.executeSetCoord(point);
		}

		//////////////////////////////////////////////////////////////////////
    	// Functions /////////////////////////////////////////////////////////
    	//////////////////////////////////////////////////////////////////////

		public void mousePressed(MouseEvent e) {
			
//			if(isEnabled() && !hasFocus() && isRequestFocusEnabled()) {
//				requestFocus();
//			}            
		}
		
		//////////////////////////////////////////////////////////////////////
    	// Functions /////////////////////////////////////////////////////////
    	//////////////////////////////////////////////////////////////////////

		public void mouseReleased(MouseEvent e) {
			
		}

		//////////////////////////////////////////////////////////////////////
    	// Functions /////////////////////////////////////////////////////////
    	//////////////////////////////////////////////////////////////////////

		public void mouseClicked(MouseEvent e) {
			
		}

	    //////////////////////////////////////////////////////////////////////
		// Functions /////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////

		public void componentResized(ComponentEvent e) {

			Manager manager = Manager.getInstance();
			manager.executeSetCoord(createPoint(getMousePosition()));
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
	}
	
	//////////////////////////////////////////////////////////////////////
	// Inner class ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

    class Ticker extends Thread {
    	
    	//////////////////////////////////////////////////////////////////////
    	// Variables /////////////////////////////////////////////////////////
    	//////////////////////////////////////////////////////////////////////

    	private boolean pause = true;
    	
    	//////////////////////////////////////////////////////////////////////
    	// Functions /////////////////////////////////////////////////////////
    	//////////////////////////////////////////////////////////////////////

    	public synchronized void pause(boolean pause) {
    		
    		if(this.pause != pause) {
    			
        		this.pause = pause;
        		
        		if(!pause) {
        			notifyAll();
        		}
    		}
    	}
    	
    	//////////////////////////////////////////////////////////////////////
    	// Functions /////////////////////////////////////////////////////////
    	//////////////////////////////////////////////////////////////////////

    	public synchronized void check() throws InterruptedException {
    		
			if(pause) {
   	            wait();
    		}
    	}
    	
    	//////////////////////////////////////////////////////////////////////
    	// Functions /////////////////////////////////////////////////////////
    	//////////////////////////////////////////////////////////////////////

    	public void run() {
    		
			try {
				
	    		while(true) {
	    			
	    			if(isInterrupted()) {
	    				throw new InterruptedException();
	    			}

	    			check();
	    			dashOffset = (dashOffset - 1 > 0) ? dashOffset - 1 : DASH_SIZE * 2;
	    			update();
	    			
	    			sleep(70);
	    		}

			} catch(InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
} 