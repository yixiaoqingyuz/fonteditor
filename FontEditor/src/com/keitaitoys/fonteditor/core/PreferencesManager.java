package com.keitaitoys.fonteditor.core;

import java.awt.Color;

import javax.swing.event.EventListenerList;

import com.keitaitoys.fonteditor.event.PreferencesChangeEvent;
import com.keitaitoys.fonteditor.event.PreferencesChangeListener;
import com.keitaitoys.fonteditor.event.PreferencesChangeEvent.PreferencesChangeType;
import com.keitaitoys.fonteditor.gui.preview.PreviewPanel;

public class PreferencesManager {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final boolean EDITOR_GRID = false;
	private static final boolean EDITOR_BOUNDS = true;

	private static final Color EDITOR_BOUNDS_COLOR = new Color(0, 0, 255);
	private static final Color EDITOR_BACKGROUND_MAJOR_COLOR = new Color(255, 255, 255);
	private static final Color EDITOR_BACKGROUND_MINOR_COLOR = new Color(204, 204, 204);
	
	private static final int PREVIEW_MODE = PreviewPanel.MODE_NONE;
	private static final Color PREVIEW_BACKGROUND_COLOR = new Color(255, 255, 255);
	
	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static PreferencesManager manager;

	protected EventListenerList listenerList = new EventListenerList();
	
	private boolean editorGrid;
	private boolean editorBounds;

	private Color editorBoundsColor;
	private Color editorBackgroundMajorColor;
	private Color editorBackgroundMinorColor;
	
	private int previewMode;
	private Color previewBackgroundColor;
	
	private boolean dirty;

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private PreferencesManager() {
		
		init();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private void init() {
		
		PropertiesManager propertiesManager = PropertiesManager.getInstance();

		editorGrid = propertiesManager.getBoolean("editor.grid", EDITOR_GRID);
		editorBounds = propertiesManager.getBoolean("editor.bounds", EDITOR_BOUNDS);

		int editorBoundsRed = propertiesManager.getInteger("editor.color.bounds.red", 0, 255, EDITOR_BOUNDS_COLOR.getRed());
		int editorBoundsGreen = propertiesManager.getInteger("editor.color.bounds.green", 0, 255, EDITOR_BOUNDS_COLOR.getGreen());
		int editorBoundsBlue = propertiesManager.getInteger("editor.color.bounds.blue", 0, 255, EDITOR_BOUNDS_COLOR.getBlue());
		
		editorBoundsColor = new Color(editorBoundsRed, editorBoundsGreen, editorBoundsBlue);

		int editorBackgroundMajorRed = propertiesManager.getInteger("editor.color.background.major.red", 0, 255, EDITOR_BACKGROUND_MAJOR_COLOR.getRed());
		int editorBackgroundMajorGreen = propertiesManager.getInteger("editor.color.background.major.green", 0, 255, EDITOR_BACKGROUND_MAJOR_COLOR.getGreen());
		int editorBackgroundMajorBlue = propertiesManager.getInteger("editor.color.background.major.blue", 0, 255, EDITOR_BACKGROUND_MAJOR_COLOR.getBlue());
		
		editorBackgroundMajorColor = new Color(editorBackgroundMajorRed, editorBackgroundMajorGreen, editorBackgroundMajorBlue);
		
		int editorBackgroundMinorRed = propertiesManager.getInteger("editor.color.background.minor.red", 0, 255, EDITOR_BACKGROUND_MINOR_COLOR.getRed());
		int editorBackgroundMinorGreen = propertiesManager.getInteger("editor.color.background.minor.green", 0, 255, EDITOR_BACKGROUND_MINOR_COLOR.getGreen());
		int editorBackgroundMinorBlue = propertiesManager.getInteger("editor.color.background.minor.blue", 0, 255, EDITOR_BACKGROUND_MINOR_COLOR.getBlue());

		editorBackgroundMinorColor = new Color(editorBackgroundMinorRed, editorBackgroundMinorGreen, editorBackgroundMinorBlue);
		
		previewMode = propertiesManager.getInteger("preview.mode", 0, 2, PREVIEW_MODE);

		int previewBackgroundRed = propertiesManager.getInteger("preview.color.background.red", 0, 255, PREVIEW_BACKGROUND_COLOR.getRed());
		int previewBackgroundGreen = propertiesManager.getInteger("preview.color.background.green", 0, 255, PREVIEW_BACKGROUND_COLOR.getGreen());
		int previewBackgroundBlue = propertiesManager.getInteger("preview.color.background.blue", 0, 255, PREVIEW_BACKGROUND_COLOR.getBlue());
			
		previewBackgroundColor = new Color(previewBackgroundRed, previewBackgroundGreen, previewBackgroundBlue);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public static synchronized PreferencesManager getInstance() {
		
		if(manager == null) {
			manager = new PreferencesManager();
		}
		
		return manager;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public boolean isEditorGrid() {
		
		return editorGrid;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public boolean isEditorBounds() {
		
		return editorBounds;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public Color getEditorBoundsColor() {
		
		return editorBoundsColor;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public Color getEditorBackgroundMajorColor() {
		
		return editorBackgroundMajorColor;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public Color getEditorBackgroundMinorColor() {
		
		return editorBackgroundMinorColor;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public int getPreviewMode() {
		
		return previewMode;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public Color getPreviewBackgroundColor() {
		
		return previewBackgroundColor;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void setEditorGrid(boolean editorGrid) {
		
		this.editorGrid = editorGrid;
		
		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		propertiesManager.setProperty("editor.grid", "" + editorGrid);
		
		firePreferencesChanged(PreferencesChangeType.EDITOR_GRID_CHANGE);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void setEditorBounds(boolean editorBounds) {
		
		this.editorBounds = editorBounds;
		
		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		propertiesManager.setProperty("editor.bounds", "" + editorBounds);
		
		firePreferencesChanged(PreferencesChangeType.EDITOR_BOUNDS_CHANGE);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void setEditorBoundsColor(Color editorBoundsColor) {
		
		this.editorBoundsColor = editorBoundsColor;
		
		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		
		propertiesManager.setProperty("editor.color.bounds.red", String.valueOf(editorBoundsColor.getRed()));
		propertiesManager.setProperty("editor.color.bounds.green", String.valueOf(editorBoundsColor.getGreen()));
		propertiesManager.setProperty("editor.color.bounds.blue", String.valueOf(editorBoundsColor.getBlue()));

		firePreferencesChanged(PreferencesChangeType.EDITOR_BOUNDS_COLOR_CHANGE);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void setEditorBackgroundMajorColor(Color editorBackgroundMajorColor) {
		
		this.editorBackgroundMajorColor = editorBackgroundMajorColor;
		
		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		
		propertiesManager.setProperty("editor.color.background.major.red", String.valueOf(editorBackgroundMajorColor.getRed()));
		propertiesManager.setProperty("editor.color.background.major.green", String.valueOf(editorBackgroundMajorColor.getGreen()));
		propertiesManager.setProperty("editor.color.background.major.blue", String.valueOf(editorBackgroundMajorColor.getBlue()));

		firePreferencesChanged(PreferencesChangeType.EDITOR_BACKGROUND_COLOR_CHANGED);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void setEditorBackgroundMinorColor(Color editorBackgroundMinorColor) {
		
		this.editorBackgroundMinorColor = editorBackgroundMinorColor;
		
		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		
		propertiesManager.setProperty("editor.color.background.minor.red", String.valueOf(editorBackgroundMinorColor.getRed()));
		propertiesManager.setProperty("editor.color.background.minor.green", String.valueOf(editorBackgroundMinorColor.getGreen()));
		propertiesManager.setProperty("editor.color.background.minor.blue", String.valueOf(editorBackgroundMinorColor.getBlue()));

		firePreferencesChanged(PreferencesChangeType.EDITOR_BACKGROUND_COLOR_CHANGED);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void setPreviewMode(int previewMode) {
		
		this.previewMode = previewMode;
		
		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		propertiesManager.setProperty("preview.mode", String.valueOf(previewMode));
		
		firePreferencesChanged(PreferencesChangeType.PREVIEW_MODE_CHANGED);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void setPreviewBackgroundColor(Color previewBackgroundColor) {
		
		this.previewBackgroundColor = previewBackgroundColor;
		
		PropertiesManager propertiesManager = PropertiesManager.getInstance();
		
		propertiesManager.setProperty("preview.color.background.red", String.valueOf(previewBackgroundColor.getRed()));
		propertiesManager.setProperty("preview.color.background.green", String.valueOf(previewBackgroundColor.getGreen()));
		propertiesManager.setProperty("preview.color.background.blue", String.valueOf(previewBackgroundColor.getBlue()));

		firePreferencesChanged(PreferencesChangeType.PREVIEW_BACKGROUND_COLOR_CHANGED);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public boolean isDirty() {
		
		return dirty;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void setDirty(boolean dirty) {
		
		this.dirty = dirty;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

    protected void firePreferencesChanged(PreferencesChangeType type) {
    	
    	Object[] listeners = listenerList.getListenerList();
    	PreferencesChangeEvent e = null;

    	for(int i = listeners.length - 2; i >= 0; i -= 2) {
    		
    	    if(listeners[i] == PreferencesChangeListener.class) {
    	    	if(e == null) e = new PreferencesChangeEvent(this, type);
    	    	((PreferencesChangeListener)listeners[i + 1]).preferencesChange(e);
    	    }
    	}
    }

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void addPreferencesChangeListener(PreferencesChangeListener l) {
		
		listenerList.add(PreferencesChangeListener.class, l);
    }

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void removePreferencesChangeListener(PreferencesChangeListener l) {
		
		listenerList.remove(PreferencesChangeListener.class, l);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public PreferencesChangeListener[] getPreferencesChangeListeners() {
		
		return (PreferencesChangeListener[])listenerList.getListeners(PreferencesChangeListener.class);
	}
}