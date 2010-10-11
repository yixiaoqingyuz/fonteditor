package com.keitaitoys.fonteditor.core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import com.keitaitoys.fonteditor.FontEditor;
import com.keitaitoys.fonteditor.core.LookAndFeelManager.LookAndFeelInfo;
import com.keitaitoys.fonteditor.font.Symbol;
import com.keitaitoys.fonteditor.font.Font;
import com.keitaitoys.fonteditor.gui.ColorThumb;
import com.keitaitoys.fonteditor.gui.EditorFrame;
import com.keitaitoys.fonteditor.gui.RedoMenuItem;
import com.keitaitoys.fonteditor.gui.UndoMenuItem;
import com.keitaitoys.fonteditor.gui.editor.AddUndoableEdit;
import com.keitaitoys.fonteditor.gui.editor.CharPopupMenu;
import com.keitaitoys.fonteditor.gui.editor.DeleteUndoableEdit;
import com.keitaitoys.fonteditor.gui.editor.EditorPanel;
import com.keitaitoys.fonteditor.gui.optionpane.OptionPane;
import com.keitaitoys.fonteditor.gui.preferences.PreferencesEditorBackgroundMajorColorChooser;
import com.keitaitoys.fonteditor.gui.preferences.PreferencesEditorBackgroundMajorColorThumb;
import com.keitaitoys.fonteditor.gui.preferences.PreferencesEditorBackgroundMinorColorChooser;
import com.keitaitoys.fonteditor.gui.preferences.PreferencesEditorBackgroundMinorColorThumb;
import com.keitaitoys.fonteditor.gui.preferences.PreferencesEditorBoundsColorChooser;
import com.keitaitoys.fonteditor.gui.preferences.PreferencesEditorBoundsColorThumb;
import com.keitaitoys.fonteditor.gui.preferences.PreferencesPreviewBackgroundColorChooser;
import com.keitaitoys.fonteditor.gui.preferences.PreferencesPreviewBackgroundColorThumb;
import com.keitaitoys.fonteditor.gui.preview.SpacingSpinner;
import com.keitaitoys.fonteditor.gui.preview.PreviewPanel;
import com.keitaitoys.fonteditor.gui.preview.SizeSpinner;
import com.keitaitoys.fonteditor.gui.preview.LeadingSpinner;
import com.keitaitoys.fonteditor.gui.status.CoordLabel;
import com.keitaitoys.fonteditor.gui.status.DimensionLabel;
import com.keitaitoys.fonteditor.gui.status.ScaleLabel;
import com.keitaitoys.fonteditor.io.FontExportFailedException;
import com.keitaitoys.fonteditor.io.FontExporter;
import com.keitaitoys.fonteditor.io.FontReadFailedException;
import com.keitaitoys.fonteditor.io.FontReader;
import com.keitaitoys.fonteditor.io.FontWriteFailedException;
import com.keitaitoys.fonteditor.io.FontWriter;
import com.keitaitoys.util.Utilities;

public class Manager {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String DIALOG_PLAIN_TITLE_LOCALIZE_KEY = "dialog.plain.title";
	private static final String DIALOG_INFORMATION_TITLE_LOCALIZE_KEY = "dialog.information.title";
	private static final String DIALOG_QUESTION_TITLE_LOCALIZE_KEY = "dialog.question.title";
	private static final String DIALOG_WARNING_TITLE_LOCALIZE_KEY = "dialog.warning.title";
	private static final String DIALOG_ERROR_TITLE_LOCALIZE_KEY = "dialog.error.title";

	private static final String DIALOG_BUTTON_OK_CAPTION_LOCALIZE_KEY = "dialog.button.ok.caption";
	private static final String DIALOG_BUTTON_YES_CAPTION_LOCALIZE_KEY = "dialog.button.yes.caption";
	private static final String DIALOG_BUTTON_NO_CAPTION_LOCALIZE_KEY = "dialog.button.no.caption";
	private static final String DIALOG_BUTTON_CANCEL_CAPTION_LOCALIZE_KEY = "dialog.button.cancel.caption";
	private static final String DIALOG_BUTTON_SKIP_CAPTION_LOCALIZE_KEY = "dialog.button.skip.caption";
	private static final String DIALOG_BUTTON_RETRY_CAPTION_LOCALIZE_KEY = "dialog.button.retry.caption";
	private static final String DIALOG_BUTTON_IGNORE_CAPTION_LOCALIZE_KEY = "dialog.button.ignore.caption";
	private static final String DIALOG_BUTTON_ABORT_CAPTION_LOCALIZE_KEY = "dialog.button.abort.caption";

	private static final String MESSAGE_NOT_IMAGE_FILE_TEXT_LOCALIZE_KEY = "message.not.image.file.text";
	private static final String MESSAGE_CANT_READ_FILE_TEXT_LOCALIZE_KEY = "message.cant.read.file.text";
	private static final String MESSAGE_CANT_WRITE_FILE_TEXT_LOCALIZE_KEY = "message.cant.write.file.text";
	private static final String MESSAGE_CANT_EXPORT_FILE_TEXT_LOCALIZE_KEY = "message.cant.export.file.text";
	private static final String MESSAGE_CANT_FIND_FILE_TEXT_LOCALIZE_KEY = "message.cant.find.file.text";
	private static final String MESSAGE_FILE_ALREADY_EXIST_TEXT_LOCALIZE_KEY = "message.file.already.exist.text";
	private static final String MESSAGE_SAVE_CHANGES_TEXT_LOCALIZE_KEY = "message.save.changes.text";
	private static final String MESSAGE_MARQUEE_NOT_SPECIFIED_TEXT_LOCALIZE_KEY = "message.marquee.not.specified.text";
	private static final String MESSAGE_CHAR_NOT_SPECIFIED_TEXT_LOCALIZE_KEY = "message.char.not.specified.text";
	private static final String MESSAGE_CHAR_NOT_FOUND_TEXT_LOCALIZE_KEY = "message.char.not.found.text";
	private static final String MESSAGE_CHAR_OFFSET_NOT_SPECIFIED_TEXT_LOCALIZE_KEY = "message.char.offset.not.specified.text";
	private static final String MESSAGE_CHAR_OFFSET_NOT_INTEGER_TEXT_LOCALIZE_KEY = "message.char.offset.not.integer.text";
	private static final String MESSAGE_FONT_FORMAT_NOT_SUPPORTED_TEXT_LOCALIZE_KEY = "message.font.format.not.supported.text";
	private static final String MESSAGE_FONT_IMAGE_FILE_NOT_IMAGE_TEXT_LOCALIZE_KEY = "message.font.image.file.not.image.text";
	private static final String MESSAGE_CANT_READ_FONT_IMAGE_FILE_TEXT_LOCALIZE_KEY = "message.cant.read.font.image.file.text";
	private static final String MESSAGE_CANT_FIND_FONT_IMAGE_FILE_TEXT_LOCALIZE_KEY = "message.cant.find.font.image.file.text";
	private static final String MESSAGE_FONT_IMAGE_FILE_NOT_SPECIFIED_TEXT_LOCALIZE_KEY = "message.font.image.file.not.specified.text";
	private static final String MESSAGE_CANT_UNDO_ACTION_TEXT_LOCALIZE_KEY = "message.cant.undo.action.text";
	private static final String MESSAGE_CANT_REDO_ACTION_TEXT_LOCALIZE_KEY = "message.cant.redo.action.text";
	private static final String MESSAGE_CANT_SAVE_PROPERTIES_FILE_TEXT_LOCALIZE_KEY = "message.cant.save.properties.file.text";
	
	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static Manager manager;

	private PropertiesManager propertiesManager;
	private LocaleManager localeManager;
	private PreferencesManager preferencesManager;
	private PluginManager pluginManager;
	private LookAndFeelManager lookAndFeelManager;
	private IOManager ioManager;
	private UndoManager undoManager;
	private FontManager fontManager;
	
	//////////////////////////////////////////////////////////////////////
	// Frame components //////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JFrame editorFrame; 
	
	//////////////////////////////////////////////////////////////////////
	// Menu components ///////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu optionsMenu;
	private JMenu viewMenu;
	private JMenu helpMenu;
	private JMenu languageMenu;
	private JMenu lookAndFeelMenu;
	
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem exportMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem undoMenuItem;
	private JMenuItem redoMenuItem;
	private JMenuItem openImageMenuItem;
	private JMenuItem preferencesMenuItem;
	private JMenuItem zoomInMenuItem;
	private JMenuItem zoomOutMenuItem;
	private JMenuItem zoomActualMenuItem;
	private JCheckBoxMenuItem toggleGridCheckBoxMenuItem;
	private JCheckBoxMenuItem toggleBoundsCheckBoxMenuItem;
	private JMenuItem aboutMenuItem;
	private JMenuItem pluginsMenuItem;

	//////////////////////////////////////////////////////////////////////
	// ToolBar components ////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JToolBar toolBar; 

	private JButton newButton;
	private JButton openButton;
	private JButton saveButton;
	private JButton exportButton;
	private JButton undoButton;
	private JButton redoButton;
	private JButton marqueeToolButton;
	private JButton selectToolButton;
	private JButton openImageButton;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton zoomActualButton;
	private JButton preferencesButton;
	
	//////////////////////////////////////////////////////////////////////
	// Editor panel components ///////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JLabel charLabel;
	private JLabel charOffsetXLabel;
	private JLabel charOffsetYLabel;
	
	private JTextField charTextField;
	private JTextField charOffsetXTextField;
	private JTextField charOffsetYTextField;
	
	private JButton addButton;
	private JButton deleteButton;

	private JPanel editorPane;
	
	private JPopupMenu charPopupMenu;

	//////////////////////////////////////////////////////////////////////
	// Preview panel components //////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JLabel sizeLabel;
	private JLabel spacingLabel;
	private JLabel leadingLabel;
	private JLabel previewLabel;

	private JSpinner sizeSpinner;
	private JSpinner spacingSpinner;
	private JSpinner leadingSpinner;
	
	private JPanel previewPane;

	private JTextArea previewTextArea;
	
	//////////////////////////////////////////////////////////////////////
	// Status panel components ///////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JLabel scaleLabel;
	private JLabel statusLabel;
	private JLabel coordLabel;
	private JLabel dimensionLabel;
	
	private JProgressBar processProgressBar;
	
	//////////////////////////////////////////////////////////////////////
	// Preferences dialog components /////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JDialog preferencesDialog;

	private JTabbedPane preferencesTabbedPane; 
	
	private ColorThumb preferencesEditorBoundsColorThumb;
	private ColorThumb preferencesEditorBackgroundMajorColorThumb;
	private ColorThumb preferencesEditorBackgroundMinorColorThumb;

	private JColorChooser preferencesEditorBoundsColorChooser;
	private JColorChooser preferencesEditorBackgroundMajorColorChooser;
	private JColorChooser preferencesEditorBackgroundMinorColorChooser;
	
	private JRadioButton preferencesPreviewNoneRadioButton;
	private JRadioButton preferencesPreviewClassicRadioButton;
	private JRadioButton preferencesPreviewFontRadioButton;

	private ColorThumb preferencesPreviewBackgroundColorThumb;

	private JColorChooser preferencesPreviewBackgroundColorChooser;

	private JButton preferencesOKButton;
	private JButton preferencesCancelButton;
	private JButton preferencesApplyButton;

	//////////////////////////////////////////////////////////////////////
	// Plugins dialog components /////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JDialog pluginsDialog;
	
	private TableModel pluginsTableModel;
	
	private JButton pluginsOKButton;
	
	//////////////////////////////////////////////////////////////////////
	// About dialog components ///////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JDialog aboutDialog;
	
	private JButton aboutOKButton;

	//////////////////////////////////////////////////////////////////////
	// FileChooser components ////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JFileChooser fontOpenFileChooser;
	private JFileChooser fontSaveFileChooser;
	private JFileChooser fontExportFileChooser;
	private JFileChooser imageOpenFileChooser;
	
	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private Manager() {
		
		// Создаем менеджер настроек
		propertiesManager = PropertiesManager.getInstance();

		if(propertiesManager == null) {
			
//			printException(getClass().getName(), "Failed to load properties file!", e);
			
			// Выводим сообщение об ошибке (мы пока не имеем доступ к локализации!)
			String message = "Failed to load properties file!";
			String title = "Error!";
			showOptionDialog(message, title, OptionPane.DEFAULT_OPTION, OptionPane.ERROR_MESSAGE, null, null, null);

			// Отсутствие настроек является критической ошибкой, поэтому выходим из программы
			System.exit(0);
		}
		
		localeManager = LocaleManager.getInstance();
		preferencesManager = PreferencesManager.getInstance();
		pluginManager = PluginManager.getInstance();
		lookAndFeelManager = LookAndFeelManager.getInstance();
		ioManager = IOManager.getInstance();
		undoManager = UndoManager.getInstance();
		fontManager = FontManager.getInstance();
/*
		Runnable thread = new Runnable() {
			public void run() {
				try {
					Thread.sleep(10000);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
				executeLanguageSelect(new Locale("es"));
			}
		};
		
		(new Thread(thread)).start();
*/
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public static synchronized Manager getInstance() {
		
		if(manager == null) {
			manager = new Manager();
		}
		
		return manager;
	}

	//////////////////////////////////////////////////////////////////////
	// Register frame components ///////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void registerEditorFrame(JFrame editorFrame) {
		
		this.editorFrame = editorFrame;
		if(FontEditor.DEBUG) System.out.println("registered -> editorFrame");
	}

	//////////////////////////////////////////////////////////////////////
	// Register menu components ////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void registerFileMenu(JMenu fileMenu) {
		
		this.fileMenu = fileMenu;
		if(FontEditor.DEBUG) System.out.println("registered -> fileMenu");
	}

	public void registerEditMenu(JMenu editMenu) {
		
		this.editMenu = editMenu;
		if(FontEditor.DEBUG) System.out.println("registered -> editMenu");
	}

	public void registerOptionsMenu(JMenu optionsMenu) {
		
		this.optionsMenu = optionsMenu;
		if(FontEditor.DEBUG) System.out.println("registered -> optionsMenu");
	}

	public void registerViewMenu(JMenu viewMenu) {
		
		this.viewMenu = viewMenu;
		if(FontEditor.DEBUG) System.out.println("registered -> viewMenu");
	}

	public void registerHelpMenu(JMenu helpMenu) {
		
		this.helpMenu = helpMenu;
		if(FontEditor.DEBUG) System.out.println("registered -> helpMenu");
	}

	public void registerLanguageMenu(JMenu languageMenu) {
		
		this.languageMenu = languageMenu;
		if(FontEditor.DEBUG) System.out.println("registered -> languageMenu");
	}

	public void registerLookAndFeelMenu(JMenu lookAndFeelMenu) {
		
		this.lookAndFeelMenu = lookAndFeelMenu;
		if(FontEditor.DEBUG) System.out.println("registered -> lookAndFeelMenu");
	}
	
	public void registerNewMenuItem(JMenuItem newMenuItem) {
		
		this.newMenuItem = newMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> newMenuItem");
	}

	public void registerOpenMenuItem(JMenuItem openMenuItem) {
		
		this.openMenuItem = openMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> openMenuItem");
	}

	public void registerSaveMenuItem(JMenuItem saveMenuItem) {
		
		this.saveMenuItem = saveMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> saveMenuItem");
	}

	public void registerSaveAsMenuItem(JMenuItem saveAsMenuItem) {
		
		this.saveAsMenuItem = saveAsMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> saveAsMenuItem");
	}

	public void registerExportMenuItem(JMenuItem exportMenuItem) {
		
		this.exportMenuItem = exportMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> exportMenuItem");
	}

	public void registerExitMenuItem(JMenuItem exitMenuItem) {
		
		this.exitMenuItem = exitMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> exitMenuItem");
	}

	public void registerUndoMenuItem(JMenuItem undoMenuItem) {
		
		this.undoMenuItem = undoMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> undoMenuItem");
	}

	public void registerRedoMenuItem(JMenuItem redoMenuItem) {
		
		this.redoMenuItem = redoMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> redoMenuItem");
	}

	public void registerOpenImageMenuItem(JMenuItem openImageMenuItem) {
		
		this.openImageMenuItem = openImageMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> openImageMenuItem");
	}

	public void registerPreferencesMenuItem(JMenuItem preferencesMenuItem) {
		
		this.preferencesMenuItem = preferencesMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> preferencesMenuItem");
	}

	public void registerZoomInMenuItem(JMenuItem zoomInMenuItem) {
		
		this.zoomInMenuItem = zoomInMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> zoomInMenuItem");
	}

	public void registerZoomOutMenuItem(JMenuItem zoomOutMenuItem) {
		
		this.zoomOutMenuItem = zoomOutMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> zoomOutMenuItem");
	}
	
	public void registerZoomActualMenuItem(JMenuItem zoomActualMenuItem) {
		
		this.zoomActualMenuItem = zoomActualMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> zoomActualMenuItem");
	}
	
	public void registerToggleGridCheckBoxMenuItem(JCheckBoxMenuItem toggleGridCheckBoxMenuItem) {
		
		this.toggleGridCheckBoxMenuItem = toggleGridCheckBoxMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> toggleGridCheckBoxMenuItem");
	}
	
	public void registerToggleBoundsCheckBoxMenuItem(JCheckBoxMenuItem toggleBoundsCheckBoxMenuItem) {
		
		this.toggleBoundsCheckBoxMenuItem = toggleBoundsCheckBoxMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> toggleBoundsCheckBoxMenuItem");
	}
	
	public void registerAboutMenuItem(JMenuItem aboutMenuItem) {
		
		this.aboutMenuItem = aboutMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> aboutMenuItem");
	}
	
	public void registerPluginsMenuItem(JMenuItem pluginsMenuItem) {
		
		this.pluginsMenuItem = pluginsMenuItem;
		if(FontEditor.DEBUG) System.out.println("registered -> pluginsMenuItem");
	}

	//////////////////////////////////////////////////////////////////////
	// Register toolbar components /////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void registerEditorToolBar(JToolBar toolBar) {
		
		this.toolBar = toolBar;
		if(FontEditor.DEBUG) System.out.println("registered -> toolBar");
	}

	public void registerNewButton(JButton newButton) {
		
		this.newButton = newButton;
		if(FontEditor.DEBUG) System.out.println("registered -> newButton");
	}

	public void registerOpenButton(JButton openButton) {
		
		this.openButton = openButton;
		if(FontEditor.DEBUG) System.out.println("registered -> openButton");
	}

	public void registerSaveButton(JButton saveButton) {
		
		this.saveButton = saveButton;
		if(FontEditor.DEBUG) System.out.println("registered -> saveButton");
	}

	public void registerExportButton(JButton exportButton) {
		
		this.exportButton = exportButton;
		if(FontEditor.DEBUG) System.out.println("registered -> exportButton");
	}

	public void registerUndoButton(JButton undoButton) {
		
		this.undoButton = undoButton;
		if(FontEditor.DEBUG) System.out.println("registered -> undoButton");
	}

	public void registerRedoButton(JButton redoButton) {
		
		this.redoButton = redoButton;
		if(FontEditor.DEBUG) System.out.println("registered -> redoButton");
	}

	public void registerMarqueeToolButton(JButton marqueeToolButton) {
		
		this.marqueeToolButton = marqueeToolButton;
		if(FontEditor.DEBUG) System.out.println("registered -> marqueeToolButton");
	}

	public void registerSelectToolButton(JButton selectToolButton) {
		
		this.selectToolButton = selectToolButton;
		if(FontEditor.DEBUG) System.out.println("registered -> selectToolButton");
	}

	public void registerOpenImageButton(JButton openImageButton) {
		
		this.openImageButton = openImageButton;
		if(FontEditor.DEBUG) System.out.println("registered -> openImageButton");
	}

	public void registerZoomInButton(JButton zoomInButton) {
		
		this.zoomInButton = zoomInButton;
		if(FontEditor.DEBUG) System.out.println("registered -> zoomInButton");
	}

	public void registerZoomOutButton(JButton zoomOutButton) {
		
		this.zoomOutButton = zoomOutButton;
		if(FontEditor.DEBUG) System.out.println("registered -> zoomOutButton");
	}

	public void registerZoomActualButton(JButton zoomActualButton) {
		
		this.zoomActualButton = zoomActualButton;
		if(FontEditor.DEBUG) System.out.println("registered -> zoomActualButton");
	}

	public void registerPreferencesButton(JButton preferencesButton) {
		
		this.preferencesButton = preferencesButton;
		if(FontEditor.DEBUG) System.out.println("registered -> preferencesButton");
	}

	//////////////////////////////////////////////////////////////////////
	// Register editor panel components ////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void registerCharLabel(JLabel charLabel) {
		
		this.charLabel = charLabel;
		if(FontEditor.DEBUG) System.out.println("registered -> editor -> charLabel");
	}

	public void registerCharOffsetXLabel(JLabel charOffsetXLabel) {
		
		this.charOffsetXLabel = charOffsetXLabel;
		if(FontEditor.DEBUG) System.out.println("registered -> editor -> charOffsetXLabel");
	}

	public void registerCharOffsetYLabel(JLabel charOffsetYLabel) {
		
		this.charOffsetYLabel = charOffsetYLabel;
		if(FontEditor.DEBUG) System.out.println("registered -> editor -> charOffsetYLabel");
	}

	public void registerCharTextField(JTextField charTextField) {
		
		this.charTextField = charTextField;
		if(FontEditor.DEBUG) System.out.println("registered -> editor -> charTextField");
	}

	public void registerCharOffsetXTextField(JTextField charOffsetXTextField) {
		
		this.charOffsetXTextField = charOffsetXTextField;
		if(FontEditor.DEBUG) System.out.println("registered -> editor -> charOffsetXTextField");
	}

	public void registerCharOffsetYTextField(JTextField charOffsetYTextField) {
		
		this.charOffsetYTextField = charOffsetYTextField;
		if(FontEditor.DEBUG) System.out.println("registered -> editor -> charOffsetYTextField");
	}
	
	public void registerAddButton(JButton addButton) {
		
		this.addButton = addButton;
		if(FontEditor.DEBUG) System.out.println("registered -> editor -> addButton");
	}

	public void registerDeleteButton(JButton deleteButton) {
		
		this.deleteButton = deleteButton;
		if(FontEditor.DEBUG) System.out.println("registered -> editor -> deleteButton");
	}
	
	public void registerEditorPanel(JPanel editorPane) {
		
		this.editorPane = editorPane;
		if(FontEditor.DEBUG) System.out.println("registered -> editor -> editorPane");
	}
	
	public void registerCharPopupMenu(JPopupMenu charPopupMenu) {
		
		this.charPopupMenu = charPopupMenu;
		if(FontEditor.DEBUG) System.out.println("registered -> editor -> charPopupMenu");
	}
	
	//////////////////////////////////////////////////////////////////////
	// Register preview panel components ///////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void registerSizeLabel(JLabel sizeLabel) {
		
		this.sizeLabel = sizeLabel;
		if(FontEditor.DEBUG) System.out.println("registered -> preview -> sizeLabel");
	}

	public void registerSpacingLabel(JLabel spacingLabel) {
		
		this.spacingLabel = spacingLabel;
		if(FontEditor.DEBUG) System.out.println("registered -> preview -> spacingLabel");
	}

	public void registerLeadingLabel(JLabel leadingLabel) {
		
		this.leadingLabel = leadingLabel;
		if(FontEditor.DEBUG) System.out.println("registered -> preview -> leadingLabel");
	}

	public void registerPreviewLabel(JLabel previewLabel) {
		
		this.previewLabel = previewLabel;
		if(FontEditor.DEBUG) System.out.println("registered -> preview -> previewLabel");
	}

	public void registerSizeSpinner(JSpinner sizeSpinner) {
		
		this.sizeSpinner = sizeSpinner;
		if(FontEditor.DEBUG) System.out.println("registered -> preview -> sizeSpinner");
	}

	public void registerSpacingSpinner(JSpinner spacingSpinner) {
		
		this.spacingSpinner = spacingSpinner;
		if(FontEditor.DEBUG) System.out.println("registered -> preview -> spacingSpinner");
	}

	public void registerLeadingSpinner(JSpinner leadingSpinner) {
		
		this.leadingSpinner = leadingSpinner;
		if(FontEditor.DEBUG) System.out.println("registered -> preview -> leadingSpinner");
	}

	public void registerPreviewPanel(JPanel previewPane) {
		
		this.previewPane = previewPane;
		if(FontEditor.DEBUG) System.out.println("registered -> preview -> previewPane");
	}
	
	public void registerPreviewTextArea(JTextArea previewTextArea) {
		
		this.previewTextArea = previewTextArea;
		if(FontEditor.DEBUG) System.out.println("registered -> preview -> previewTextArea");
	}
	
	//////////////////////////////////////////////////////////////////////
	// Register status panel components ////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void registerScaleLabel(JLabel scaleLabel) {
		
		this.scaleLabel = scaleLabel;
		if(FontEditor.DEBUG) System.out.println("registered -> scaleLabel");
	}

	public void registerStatusLabel(JLabel statusLabel) {
		
		this.statusLabel = statusLabel;
		if(FontEditor.DEBUG) System.out.println("registered -> statusLabel");
	}

	public void registerCoordLabel(JLabel coordLabel) {
		
		this.coordLabel = coordLabel;
		if(FontEditor.DEBUG) System.out.println("registered -> coordLabel");
	}

	public void registerDimensionLabel(JLabel dimensionLabel) {
		
		this.dimensionLabel = dimensionLabel;
		if(FontEditor.DEBUG) System.out.println("registered -> dimensionLabel");
	}

	public void registerProcessProgressBar(JProgressBar processProgressBar) {
		
		this.processProgressBar = processProgressBar;
		if(FontEditor.DEBUG) System.out.println("registered -> processProgressBar");
	}

	//////////////////////////////////////////////////////////////////////
	// Register preferences dialog panel components ////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void registerPreferencesDialog(JDialog preferencesDialog) {
		
		this.preferencesDialog = preferencesDialog;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesDialog");
	}

	public void registerPreferencesTabbedPane(JTabbedPane preferencesTabbedPane) {
		
		this.preferencesTabbedPane = preferencesTabbedPane;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesTabbedPane");
	}
	
	public void registerPreferencesEditorBoundsColorThumb(ColorThumb preferencesEditorBoundsColorThumb) {
		
		this.preferencesEditorBoundsColorThumb = preferencesEditorBoundsColorThumb;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesEditorBoundsColorThumb");
	}
	
	public void registerPreferencesEditorBackgroundMajorColorThumb(ColorThumb preferencesEditorBackgroundMajorColorThumb) {
		
		this.preferencesEditorBackgroundMajorColorThumb = preferencesEditorBackgroundMajorColorThumb;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesEditorBackgroundMajorColorThumb");
	}
	
	public void registerPreferencesEditorBackgroundMinorColorThumb(ColorThumb preferencesEditorBackgroundMinorColorThumb) {
		
		this.preferencesEditorBackgroundMinorColorThumb = preferencesEditorBackgroundMinorColorThumb;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesEditorBackgroundMinorColorThumb");
	}
	
	public void registerPreferencesEditorBoundsColorChooser(JColorChooser preferencesEditorBoundsColorChooser) {
		
		this.preferencesEditorBoundsColorChooser = preferencesEditorBoundsColorChooser;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesEditorBoundsColorChooser");
	}
	
	public void registerPreferencesEditorBackgroundMajorColorChooser(JColorChooser preferencesEditorBackgroundMajorColorChooser) {
		
		this.preferencesEditorBackgroundMajorColorChooser = preferencesEditorBackgroundMajorColorChooser;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesEditorBackgroundMajorColorChooser");
	}
	
	public void registerPreferencesEditorBackgroundMinorColorChooser(JColorChooser preferencesEditorBackgroundMinorColorChooser) {
		
		this.preferencesEditorBackgroundMinorColorChooser = preferencesEditorBackgroundMinorColorChooser;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesEditorBackgroundMinorColorChooser");
	}
	
	public void registerPreferencesPreviewNoneRadioButton(JRadioButton preferencesPreviewNoneRadioButton) {
		
		this.preferencesPreviewNoneRadioButton = preferencesPreviewNoneRadioButton;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesPreviewNoneRadioButton");
	}

	public void registerPreferencesPreviewClassicRadioButton(JRadioButton preferencesPreviewClassicRadioButton) {
		
		this.preferencesPreviewClassicRadioButton = preferencesPreviewClassicRadioButton;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesPreviewClassicRadioButton");
	}

	public void registerPreferencesPreviewFontRadioButton(JRadioButton preferencesPreviewFontRadioButton) {
		
		this.preferencesPreviewFontRadioButton = preferencesPreviewFontRadioButton;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesPreviewFontRadioButton");
	}

	public void registerPreferencesPreviewBackgroundColorThumb(ColorThumb preferencesPreviewBackgroundColorThumb) {
		
		this.preferencesPreviewBackgroundColorThumb = preferencesPreviewBackgroundColorThumb;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesPreviewBackgroundColorThumb");
	}
	
	public void registerPreferencesPreviewBackgroundColorChooser(JColorChooser preferencesPreviewBackgroundColorChooser) {
		
		this.preferencesPreviewBackgroundColorChooser = preferencesPreviewBackgroundColorChooser;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesPreviewBackgroundColorChooser");
	}
	
	public void registerPreferencesOKButton(JButton preferencesOKButton) {
		
		this.preferencesOKButton = preferencesOKButton;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesOKButton");
	}

	public void registerPreferencesCancelButton(JButton preferencesCancelButton) {
		
		this.preferencesCancelButton = preferencesCancelButton;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesCancelButton");
	}

	public void registerPreferencesApplyButton(JButton preferencesApplyButton) {
		
		this.preferencesApplyButton = preferencesApplyButton;
		if(FontEditor.DEBUG) System.out.println("registered -> preferences -> preferencesApplyButton");
	}
	
	//////////////////////////////////////////////////////////////////////
	// Register plugins dialog components //////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void registerPluginsDialog(JDialog pluginsDialog) {
		
		this.pluginsDialog = pluginsDialog;
		if(FontEditor.DEBUG) System.out.println("registered -> plugins -> pluginsDialog");
	}

	public void registerPluginsTableModel(TableModel pluginsTableModel) {
		
		this.pluginsTableModel = pluginsTableModel;
		if(FontEditor.DEBUG) System.out.println("registered -> plugins -> pluginsTableModel");
	}

	public void registerPluginsOKButton(JButton pluginsOKButton) {
		
		this.pluginsOKButton = pluginsOKButton;
		if(FontEditor.DEBUG) System.out.println("registered -> plugins -> pluginsOKButton");
	}

	//////////////////////////////////////////////////////////////////////
	// Register about dialog components //////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void registerAboutDialog(JDialog aboutDialog) {
		
		this.aboutDialog = aboutDialog;
		if(FontEditor.DEBUG) System.out.println("registered -> about -> aboutDialog");
	}

	public void registerAboutOKButton(JButton aboutOKButton) {
		
		this.aboutOKButton = aboutOKButton;
		if(FontEditor.DEBUG) System.out.println("registered -> about -> aboutOKButton");
	}

	//////////////////////////////////////////////////////////////////////
	// Register filechooser components /////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void registerFontOpenFileChooser(JFileChooser fontOpenFileChooser) {
		
		this.fontOpenFileChooser = fontOpenFileChooser;
		if(FontEditor.DEBUG) System.out.println("registered -> fontOpenFileChooser");
	}

	public void registerFontSaveFileChooser(JFileChooser fontSaveFileChooser) {
		
		this.fontSaveFileChooser = fontSaveFileChooser;
		if(FontEditor.DEBUG) System.out.println("registered -> fontSaveFileChooser");
	}

	public void registerFontExportFileChooser(JFileChooser fontExportFileChooser) {
		
		this.fontExportFileChooser = fontExportFileChooser;
		if(FontEditor.DEBUG) System.out.println("registered -> fontExportFileChooser");
	}

	public void registerImageOpenFileChooser(JFileChooser imageOpenFileChooser) {
		
		this.imageOpenFileChooser = imageOpenFileChooser;
		if(FontEditor.DEBUG) System.out.println("registered -> imageOpenFileChooser");
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public int showPlainMessageDialog(Object message) {

		return showMessageDialog(message, OptionPane.PLAIN_MESSAGE);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public int showInformationMessageDialog(Object message) {

		return showMessageDialog(message, OptionPane.INFORMATION_MESSAGE);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public int showQuestionMessageDialog(Object message) {

		return showMessageDialog(message, OptionPane.QUESTION_MESSAGE);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public int showWarningMessageDialog(Object message) {

		return showMessageDialog(message, OptionPane.WARNING_MESSAGE);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public int showErrorMessageDialog(Object message) {

		return showMessageDialog(message, OptionPane.ERROR_MESSAGE);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public int showMessageDialog(Object message, int messageType) {
		
		String title = null;
		Object initialValue = null;

		switch(messageType) {
		
			case OptionPane.PLAIN_MESSAGE: title = localeManager.getValue(DIALOG_PLAIN_TITLE_LOCALIZE_KEY); break;
			case OptionPane.INFORMATION_MESSAGE: title = localeManager.getValue(DIALOG_INFORMATION_TITLE_LOCALIZE_KEY); break;
			case OptionPane.QUESTION_MESSAGE: title = localeManager.getValue(DIALOG_QUESTION_TITLE_LOCALIZE_KEY); break;
			case OptionPane.WARNING_MESSAGE: title = localeManager.getValue(DIALOG_WARNING_TITLE_LOCALIZE_KEY); break;
			case OptionPane.ERROR_MESSAGE: title = localeManager.getValue(DIALOG_ERROR_TITLE_LOCALIZE_KEY); break;
		}

		Object[] options = {
			localeManager.getValue(DIALOG_BUTTON_OK_CAPTION_LOCALIZE_KEY)
		};
		
		initialValue = options[0]; 
		
		return showOptionDialog(message, title, OptionPane.DEFAULT_OPTION, messageType, null, options, initialValue);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public int showConfirmDialog(Object message, int messageType, int optionType) {
		
		Object[] options = null;
		Object initialValue = null;
		
		switch(optionType) {
		
			case OptionPane.DEFAULT_OPTION:

				options = new Object[] {
						localeManager.getValue(DIALOG_BUTTON_OK_CAPTION_LOCALIZE_KEY)
				};
				
				initialValue = options[0];  
					
				break;

			case OptionPane.YES_NO_OPTION:

				options = new Object[] {
						localeManager.getValue(DIALOG_BUTTON_YES_CAPTION_LOCALIZE_KEY),
						localeManager.getValue(DIALOG_BUTTON_NO_CAPTION_LOCALIZE_KEY)
				};

				initialValue = options[0];  

				break;

			case OptionPane.YES_NO_CANCEL_OPTION:

				options = new Object[] {
						localeManager.getValue(DIALOG_BUTTON_YES_CAPTION_LOCALIZE_KEY),
						localeManager.getValue(DIALOG_BUTTON_NO_CAPTION_LOCALIZE_KEY),
						localeManager.getValue(DIALOG_BUTTON_CANCEL_CAPTION_LOCALIZE_KEY)
				};

				initialValue = options[0];  

				break;

			case OptionPane.OK_CANCEL_OPTION:

				options = new Object[] {
						localeManager.getValue(DIALOG_BUTTON_OK_CAPTION_LOCALIZE_KEY),
						localeManager.getValue(DIALOG_BUTTON_CANCEL_CAPTION_LOCALIZE_KEY)
				};
				
				initialValue = options[0];  

				break;
		}
		
		return showConfirmDialog(message, messageType, optionType, options, initialValue);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public int showConfirmDialog(Object message, int messageType, int optionType, Object[] options, Object initialValue) {
		
		String title = null;

		switch(messageType) {
		
			case OptionPane.PLAIN_MESSAGE: title = localeManager.getValue(DIALOG_PLAIN_TITLE_LOCALIZE_KEY); break;
			case OptionPane.INFORMATION_MESSAGE: title = localeManager.getValue(DIALOG_INFORMATION_TITLE_LOCALIZE_KEY); break;
			case OptionPane.QUESTION_MESSAGE: title = localeManager.getValue(DIALOG_QUESTION_TITLE_LOCALIZE_KEY); break;
			case OptionPane.WARNING_MESSAGE: title = localeManager.getValue(DIALOG_WARNING_TITLE_LOCALIZE_KEY); break;
			case OptionPane.ERROR_MESSAGE: title = localeManager.getValue(DIALOG_ERROR_TITLE_LOCALIZE_KEY); break;
		}
		
		return showOptionDialog(message, title, optionType, messageType, null, options, initialValue);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private int showOptionDialog(final Object message, final String title, final int optionType, final int messageType, final Icon icon, final Object[] options, final Object initialValue) {
		
		int option = OptionPane.CLOSED_OPTION;
		
		if(SwingUtilities.isEventDispatchThread()) {
			
			option = OptionPane.showOptionDialog(editorFrame, message, title, optionType, messageType, icon, options, initialValue);

		} else {
			
			try {
				
				DialogThread thread = new DialogThread(editorFrame, message, title, optionType, messageType, icon, options, initialValue);
				SwingUtilities.invokeAndWait(thread);

				option = thread.returnValue;
				
			} catch(InterruptedException e) {
				
				// Этого не может произойти т.к. другие
				// потоки не могут прервать текущий поток
				printException(getClass().getName(), "Thread was interrupted!", e);
				
			} catch(InvocationTargetException e) {

				// Этого не может произойти, т.к. выполнение
				// run() безопасно с точки зрения необработанных исключений
				printException(getClass().getName(), "Some exception was thrown during run() execution!", e);
			}
		}
		
		return option;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private void setProgress(int value) {
		
		final int progress = value;
		
		if(SwingUtilities.isEventDispatchThread()) {
			
			processProgressBar.setValue(progress);

		} else {
			
			try {
				
				SwingUtilities.invokeAndWait(new Runnable() {

					public void run() {
						processProgressBar.setValue(progress);
					}
		        });
				
			} catch(InterruptedException e) {
				
				// Этого не может произойти т.к. другие
				// потоки не могут прервать текущий поток
				printException(getClass().getName(), "Thread was interrupted!", e);
				
			} catch(InvocationTargetException e) {

				// Этого не может произойти, т.к. выполнение
				// run() безопасно с точки зрения необработанных исключений
				printException(getClass().getName(), "Some exception was thrown during run() execution!", e);
			}
/*
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					processProgressBar.setValue(progress);
				}
	        });
*/
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void printException(String location, String reason, Throwable exception) {
		
		if(FontEditor.DEBUG) {
			
			System.err.println("Location: " + location);
			System.err.println("Reason: " + reason);
			System.err.println("Message: " + exception.getMessage());
			
			exception.printStackTrace(System.err);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeNew() {
		
		boolean done = false; 
		
		if(fontManager.isDirty()) {
			
			String message = localeManager.getValue(MESSAGE_SAVE_CHANGES_TEXT_LOCALIZE_KEY);
			int option = showConfirmDialog(message, OptionPane.QUESTION_MESSAGE, OptionPane.YES_NO_CANCEL_OPTION);
			
			if(option == OptionPane.YES_OPTION) {
				executeSave();
			} else if(option == OptionPane.NO_OPTION) {
				done = true;
			}
		}

		if(!fontManager.isDirty() || done) {

			fontManager.setFont(new Font());
			((EditorPanel)editorPane).setImage(null);

			charTextField.setText(null);
			charOffsetXTextField.setText(null);
			charOffsetYTextField.setText(null);
			
			sizeSpinner.setValue(Font.DEFAULT_SIZE);
			spacingSpinner.setValue(Font.DEFAULT_SPACING);
			leadingSpinner.setValue(Font.DEFAULT_LEADING);

			undoManager.discardAllEdits();
			
			((UndoMenuItem)undoMenuItem).update();
			((RedoMenuItem)redoMenuItem).update();

			fontManager.setFile(null);
			fontManager.setDirty(false);
			fontManager.setFontReader(null);
			fontManager.setFontWriter(null);
			
			((EditorFrame)editorFrame).update();

			refresh();
info();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeOpen() {
		
		boolean done = false; 
		boolean open = false;
		
		if(fontManager.isDirty()) {
			
			String message = localeManager.getValue(MESSAGE_SAVE_CHANGES_TEXT_LOCALIZE_KEY);
			int option = showConfirmDialog(message, OptionPane.QUESTION_MESSAGE, OptionPane.YES_NO_CANCEL_OPTION);
			
			if(option == OptionPane.YES_OPTION) {
				executeSave();
			} else if(option == OptionPane.NO_OPTION) {
				done = true;
			}
		}
		
		if(!fontManager.isDirty() || done) {
			
			done = false;
			
			while(!done && !open) {
				
				// Открывает окно выбора файла шрифта
				int option = fontOpenFileChooser.showOpenDialog(editorFrame);
					
				// Если пользователь выбрал опцию "Открыть"
				if(option == JFileChooser.APPROVE_OPTION) {

					File selectedFile = fontOpenFileChooser.getSelectedFile();
					
					if(selectedFile != null && selectedFile.exists()) {
						open = true;
					} else {

						String message = localeManager.getValue(MESSAGE_CANT_FIND_FILE_TEXT_LOCALIZE_KEY, selectedFile.getName());
						showErrorMessageDialog(message);
					}

				} else {
					done = true;
				}
			}

			if(open) {
				
				File selectedFile = fontOpenFileChooser.getSelectedFile();
				FileFilter selectedFileFilter = fontOpenFileChooser.getFileFilter();

				FontReader fontReader = ioManager.getFontReader(selectedFileFilter);

				if(fontReader == null) {
					
					String extension = Utilities.getExtension(selectedFile.getName());
					fontReader = ioManager.getFontReader(extension);
				}
				
				if(fontReader != null) {
					
					try {
						
						Font font = fontReader.read(selectedFile);

						File imageFile = font.getImageFile();

						if(imageFile != null) {
							
							if(imageFile.exists()) {
								
								try {
									
									// Загружаем картинку
									BufferedImage image = ImageIO.read(imageFile);
									
									if(image != null) {
										
										fontManager.setFont(font);
										
										((EditorPanel)editorPane).setImage(image);
										((PreviewPanel)previewPane).setImage(image);
										
										((SizeSpinner)sizeSpinner).setValue(font.getSize());
										((SpacingSpinner)spacingSpinner).setValue(font.getSpacing());
										((LeadingSpinner)leadingSpinner).setValue(font.getLeading());

										charTextField.setText(null);
										charOffsetXTextField.setText(null);
										charOffsetYTextField.setText(null);
										
										undoManager.discardAllEdits();
										
										((UndoMenuItem)undoMenuItem).update();
										((RedoMenuItem)redoMenuItem).update();

										fontManager.setFile(selectedFile);
										fontManager.setDirty(false);
										fontManager.setFontReader(fontReader);
										fontManager.setFontWriter(null);
										
										((EditorFrame)editorFrame).update();

										refresh();
info();
									} else {
										
										String message = localeManager.getValue(MESSAGE_FONT_IMAGE_FILE_NOT_IMAGE_TEXT_LOCALIZE_KEY, imageFile.getName());
										showErrorMessageDialog(message);
									}

								} catch(IOException e) {

									printException(getClass().getName(), "Failed to read file!", e);

									String message = localeManager.getValue(MESSAGE_CANT_READ_FONT_IMAGE_FILE_TEXT_LOCALIZE_KEY, imageFile.getName());
									showErrorMessageDialog(message);
								}
									
							} else {
									
								String message = localeManager.getValue(MESSAGE_CANT_FIND_FONT_IMAGE_FILE_TEXT_LOCALIZE_KEY, imageFile.getName());
								showErrorMessageDialog(message);
							}

						} else {

							String message = localeManager.getValue(MESSAGE_FONT_IMAGE_FILE_NOT_SPECIFIED_TEXT_LOCALIZE_KEY);
							showErrorMessageDialog(message);
						}

					} catch(FontReadFailedException e) {
						
						String message = localeManager.getValue(MESSAGE_CANT_READ_FILE_TEXT_LOCALIZE_KEY, selectedFile.getName());
						showErrorMessageDialog(message);
					}
					
				} else {
					
					String message = localeManager.getValue(MESSAGE_FONT_FORMAT_NOT_SUPPORTED_TEXT_LOCALIZE_KEY);
					showErrorMessageDialog(message);
				}
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeOpen2() {
		
		boolean done = false; 
		boolean open = false;
		
		if(fontManager.isDirty()) {
			
			String message = localeManager.getValue(MESSAGE_SAVE_CHANGES_TEXT_LOCALIZE_KEY);
			int option = showConfirmDialog(message, OptionPane.QUESTION_MESSAGE, OptionPane.YES_NO_CANCEL_OPTION);
			
			if(option == OptionPane.YES_OPTION) {
				executeSave();
			} else if(option == OptionPane.NO_OPTION) {
				done = true;
			}
		}
		
		if(!fontManager.isDirty() || done) {
			
			done = false;
			
			while(!done && !open) {
				
				// Открывает окно выбора файла шрифта
				int option = fontOpenFileChooser.showOpenDialog(editorFrame);
					
				// Если пользователь выбрал опцию "Открыть"
				if(option == JFileChooser.APPROVE_OPTION) {

					File selectedFile = fontOpenFileChooser.getSelectedFile();
					
					if(selectedFile != null && selectedFile.exists()) {
						open = true;
					} else {

						String message = localeManager.getValue(MESSAGE_CANT_FIND_FILE_TEXT_LOCALIZE_KEY, selectedFile.getName());
						showErrorMessageDialog(message);
					}

				} else {
					done = true;
				}
			}

			if(open) {
				
				File selectedFile = fontOpenFileChooser.getSelectedFile();
				FileFilter selectedFileFilter = fontOpenFileChooser.getFileFilter();

				FontReader fontReader = ioManager.getFontReader(selectedFileFilter);
				
				if(fontReader == null) {
					
					String extension = Utilities.getExtension(selectedFile.getName());
					fontReader = ioManager.getFontReader(extension);
				}
				
				if(fontReader != null) {
					
					try {
						
						Font font = fontReader.read(selectedFile);

						File imageFile = font.getImageFile();
						BufferedImage image = null;
						
						// Here it supposed that if font have corrupted image
						// or no image at all, that's not a critical error.
						// So it's ok to work with such fonts since it's still
						// contains char data. If image supposed to be corrupted
						// then image file is set to null value

						if(imageFile != null) {
							
							if(imageFile.exists()) {
							
								try {
									
									// Загружаем картинку
									image = ImageIO.read(imageFile);
									
									if(image == null) {
										
										String message = localeManager.getValue(MESSAGE_FONT_IMAGE_FILE_NOT_IMAGE_TEXT_LOCALIZE_KEY, imageFile.getName());
										showErrorMessageDialog(message);
									}

								} catch(IOException e) {

									printException(getClass().getName(), "Failed to read file!", e);

									String message = localeManager.getValue(MESSAGE_CANT_READ_FONT_IMAGE_FILE_TEXT_LOCALIZE_KEY, imageFile.getName());
									showErrorMessageDialog(message);
								}
								
							} else {
								
								String message = localeManager.getValue(MESSAGE_CANT_FIND_FONT_IMAGE_FILE_TEXT_LOCALIZE_KEY, imageFile.getName());
								showErrorMessageDialog(message);
							}
						}
						
						if(image == null) {
							font.setImageFile(null);
						}
						
						fontManager.setFont(font);
						
						((EditorPanel)editorPane).setImage(image);
						((PreviewPanel)previewPane).setImage(image);
						
						((SizeSpinner)sizeSpinner).setValue(font.getSize());
						((SpacingSpinner)spacingSpinner).setValue(font.getSpacing());
						((LeadingSpinner)leadingSpinner).setValue(font.getLeading());

						charTextField.setText(null);
						charOffsetXTextField.setText(null);
						charOffsetYTextField.setText(null);
						
						undoManager.discardAllEdits();
						
						((UndoMenuItem)undoMenuItem).update();
						((RedoMenuItem)redoMenuItem).update();

						fontManager.setFile(selectedFile);
						fontManager.setDirty(false);
						fontManager.setFontReader(fontReader);
						fontManager.setFontWriter(null);
						
						((EditorFrame)editorFrame).update();

						refresh();
info();
					} catch(FontReadFailedException e) {
						
						String message = localeManager.getValue(MESSAGE_CANT_READ_FILE_TEXT_LOCALIZE_KEY, selectedFile.getName());
						showErrorMessageDialog(message);
					}
					
				} else {
					
					String message = localeManager.getValue(MESSAGE_FONT_FORMAT_NOT_SUPPORTED_TEXT_LOCALIZE_KEY);
					showErrorMessageDialog(message);
				}
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeSave() {
		
		File selectedFile = fontManager.getFile();

		if(selectedFile != null) {
			
			if(fontManager.isDirty()) {
				
				FontWriter fontWriter = fontManager.getFontWriter();

				if(fontWriter == null) {
					
					String extension = Utilities.getExtension(selectedFile.getName());
					fontWriter = ioManager.getFontWriter(extension);
				}
				
				if(fontWriter != null) {
					
					try {
						
						fontWriter.write(selectedFile, fontManager.getFont());
						
						fontManager.setFile(selectedFile);
						fontManager.setDirty(false);
						fontManager.setFontWriter(fontWriter);
						
						((EditorFrame)editorFrame).update();

						refresh();
info();
					} catch(FontWriteFailedException e) {
						
						String message = localeManager.getValue(MESSAGE_CANT_WRITE_FILE_TEXT_LOCALIZE_KEY, selectedFile.getName());
						showErrorMessageDialog(message);
					}

				} else {
					
					String message = localeManager.getValue(MESSAGE_FONT_FORMAT_NOT_SUPPORTED_TEXT_LOCALIZE_KEY);
					showErrorMessageDialog(message);
				}
			}
			
		} else {
			executeSaveAs();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeSaveAs() {
		
		boolean done = false;
		boolean save = false;

		while(!done && !save) {
			
			// Открывает окно указания файла шрифта
			int option = fontSaveFileChooser.showSaveDialog(editorFrame);
				
			// Если пользователь выбрал опцию "Сохранить"
			if(option == JFileChooser.APPROVE_OPTION) {

				File selectedFile = fontSaveFileChooser.getSelectedFile();
				
				if(selectedFile != null && selectedFile.exists()) {
					
					String message = localeManager.getValue(MESSAGE_FILE_ALREADY_EXIST_TEXT_LOCALIZE_KEY, selectedFile.getName());
					option = showConfirmDialog(message, OptionPane.QUESTION_MESSAGE, OptionPane.YES_NO_CANCEL_OPTION);
					
					if(option == OptionPane.YES_OPTION) {
						save = true;
					} else if(option == OptionPane.CANCEL_OPTION || option == OptionPane.CLOSED_OPTION) {
						done = true;
					}

				} else {
					save = true;
				}

			} else {
				done = true;
			}
		}
		
		if(save) {
			
			File selectedFile = fontSaveFileChooser.getSelectedFile();
			FileFilter selectedFileFilter = fontSaveFileChooser.getFileFilter();
			
			FontWriter fontWriter = ioManager.getFontWriter(selectedFileFilter);

			if(fontWriter == null) {
				
				String extension = Utilities.getExtension(selectedFile.getName());
				fontWriter = ioManager.getFontWriter(extension);
			}
			
			if(fontWriter != null) {
				
				try {

					fontWriter.write(selectedFile, fontManager.getFont());
					
					fontManager.setFile(selectedFile);
					fontManager.setDirty(false);
					fontManager.setFontWriter(fontWriter);
					
					((EditorFrame)editorFrame).update();

					refresh();
info();
				} catch(FontWriteFailedException e) {
					
					String message = localeManager.getValue(MESSAGE_CANT_WRITE_FILE_TEXT_LOCALIZE_KEY, selectedFile.getName());
					showErrorMessageDialog(message);
				}

			} else {
				
				String message = localeManager.getValue(MESSAGE_FONT_FORMAT_NOT_SUPPORTED_TEXT_LOCALIZE_KEY);
				showErrorMessageDialog(message);
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private void info() {
		
		if(FontEditor.DEBUG) {
			
			System.out.println("*******************************************************************");
			System.out.println("selectedFile = " + fontManager.getFile());
			System.out.println("dirty        = " + fontManager.isDirty());
			System.out.println("fontReader   = " + fontManager.getFontReader());
			System.out.println("fontWriter   = " + fontManager.getFontWriter());
			System.out.println("*******************************************************************");
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeExport() {
		
		boolean done = false;
		boolean save = false;

		while(!done && !save) {
			
			// Открывает окно указания файла шрифта для экспорта
			int option = fontExportFileChooser.showSaveDialog(editorFrame);
				
			// Если пользователь выбрал опцию "Сохранить"
			if(option == JFileChooser.APPROVE_OPTION) {

				File selectedFile = fontExportFileChooser.getSelectedFile();
				
				if(selectedFile != null && selectedFile.exists()) {
					
					String message = localeManager.getValue(MESSAGE_FILE_ALREADY_EXIST_TEXT_LOCALIZE_KEY, selectedFile.getName());
					option = showConfirmDialog(message, OptionPane.QUESTION_MESSAGE, OptionPane.YES_NO_CANCEL_OPTION);
					
					if(option == OptionPane.YES_OPTION) {
						save = true;
					} else if(option == OptionPane.CANCEL_OPTION || option == OptionPane.CLOSED_OPTION) {
						done = true;
					}

				} else {
					save = true;
				}

			} else {
				done = true;
			}
		}
		
		if(save) {
			
			File selectedFile = fontExportFileChooser.getSelectedFile();
			FileFilter selectedFileFilter = fontExportFileChooser.getFileFilter();
			
			FontExporter fontExporter = ioManager.getFontExporter(selectedFileFilter);

			if(fontExporter == null) {
				
				String extension = Utilities.getExtension(selectedFile.getName());
				fontExporter = ioManager.getFontExporter(extension);
			}
			
			if(fontExporter != null) {
				
				try {

					fontExporter.export(selectedFile, fontManager.getFont());
info();
				} catch(FontExportFailedException e) {
					
					String message = localeManager.getValue(MESSAGE_CANT_EXPORT_FILE_TEXT_LOCALIZE_KEY, selectedFile.getName());
					showErrorMessageDialog(message);
				}

			} else {
				
				String message = localeManager.getValue(MESSAGE_FONT_FORMAT_NOT_SUPPORTED_TEXT_LOCALIZE_KEY);
				showErrorMessageDialog(message);
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeExit() {
		
		boolean done = false; 
		
		if(fontManager.isDirty()) {
			
			String message = localeManager.getValue(MESSAGE_SAVE_CHANGES_TEXT_LOCALIZE_KEY);
			int option = showConfirmDialog(message, OptionPane.QUESTION_MESSAGE, OptionPane.YES_NO_CANCEL_OPTION);
			
			if(option == OptionPane.YES_OPTION) {
				executeSave();
			} else if(option == OptionPane.NO_OPTION) {
				done = true;
			}
		}

		if(!fontManager.isDirty() || done) {
			
			try {
				
				// Сохраняем настройки
				propertiesManager.save();

			} catch(PropertiesSaveFailedException e) {
				
				printException(getClass().getName(), "Failed to save properties!", e);
				
				String message = localeManager.getValue(MESSAGE_CANT_SAVE_PROPERTIES_FILE_TEXT_LOCALIZE_KEY);
				showErrorMessageDialog(message);
			}
			
			// Завершаем работу приложения
			System.exit(0);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeOpenImage() {
		
		boolean done = false;
		
		while(!done) {
			
			// Открывает окно выбора файла картинки
			int option = imageOpenFileChooser.showOpenDialog(editorFrame);
			
			// Если пользователь выбрал опцию "Открыть"
			if(option == JFileChooser.APPROVE_OPTION) {

				File selectedFile = imageOpenFileChooser.getSelectedFile();
				
				if(selectedFile != null && selectedFile.exists()) {
					
					try {
						
						// Загружаем картинку
						BufferedImage image = ImageIO.read(selectedFile);
						
						if(image != null) {
							
							Font font = fontManager.getFont();
							
							if(font != null) {
								
								font.setImageFile(selectedFile);
								((EditorPanel)editorPane).setImage(image);
								((PreviewPanel)previewPane).setImage(image);
								
								fontManager.setDirty(true);
								
								((EditorFrame)editorFrame).update();

								refresh();
info();
							}
							
							done = true;
							
						} else {

							String message = localeManager.getValue(MESSAGE_NOT_IMAGE_FILE_TEXT_LOCALIZE_KEY, selectedFile.getName());
							showErrorMessageDialog(message);
						}

					} catch(IOException e) {

						printException(getClass().getName(), "Failed to read file!", e);

						String message = localeManager.getValue(MESSAGE_CANT_READ_FILE_TEXT_LOCALIZE_KEY, selectedFile.getName());
						showErrorMessageDialog(message);
					}

				} else {

					String message = localeManager.getValue(MESSAGE_CANT_FIND_FILE_TEXT_LOCALIZE_KEY, selectedFile.getName());
					showErrorMessageDialog(message);
				}
				
			} else {
				done = true;
			}
		}
	}
/*
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeMarqueeTool() {
		
		((EditorPanel)editorPane).marqueeTool();	
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeSelectTool() {
		
		((EditorPanel)editorPane).selectTool();	
	}
*/
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeZoomIn() {
		
		((EditorPanel)editorPane).zoomIn();
		
		int scale = (int)(((EditorPanel)editorPane).getScale() * (double)100);
		((ScaleLabel)scaleLabel).setScale(scale);

		refresh();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeZoomOut() {
		
		((EditorPanel)editorPane).zoomOut();
		
		int scale = (int)(((EditorPanel)editorPane).getScale() * (double)100);
		((ScaleLabel)scaleLabel).setScale(scale);

		refresh();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeZoomActual() {
		
		((EditorPanel)editorPane).zoomActual();
		
		int scale = (int)(((EditorPanel)editorPane).getScale() * (double)100);
		((ScaleLabel)scaleLabel).setScale(scale);
		
		refresh();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeShowGrid() {
		
		preferencesManager.setEditorGrid(!preferencesManager.isEditorGrid());
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeShowBounds() {
		
		preferencesManager.setEditorBounds(!preferencesManager.isEditorBounds());
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeSetOutline(Rectangle outline) {
		
		((EditorPanel)editorPane).setOutline(outline);
		
		Dimension dimension = null;
		
		if(outline != null) {
			dimension = new Dimension(outline.width, outline.height);
		}
		
		((DimensionLabel)dimensionLabel).setDimension(dimension);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeSetCoord(Point coord) {
		
		((CoordLabel)coordLabel).setCoord(coord);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeSetSelected(Symbol symbol) {
		
		if(symbol != null) {
			
			char c = symbol.getChar();

			int offsetX = symbol.getOffsetX(); 
			int offsetY = symbol.getOffsetY();
			
			int x = symbol.getBoundsX();
			int y = symbol.getBoundsY();
			int width = symbol.getBoundsWidth(); 
			int height = symbol.getBoundsHeight();

			Rectangle outline = new Rectangle(x, y, width, height);
			
			charTextField.setText(String.valueOf(c));
			charOffsetXTextField.setText(String.valueOf(offsetX));
			charOffsetYTextField.setText(String.valueOf(offsetY));

			executeSetOutline(outline);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeCharPopupMenu(Set<Symbol> symbols, int x, int y) {
		
		if(symbols.size() > 0) {
			
			((CharPopupMenu)charPopupMenu).populate(symbols);
			charPopupMenu.show(editorPane, x, y);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeAdd() {
		
		Rectangle outline = ((EditorPanel)editorPane).getOutline();
		
		if(outline != null && !outline.isEmpty()) {
			
			if(charTextField.getText().length() > 0) {
				
				if(charOffsetXTextField.getText().length() > 0 && charOffsetYTextField.getText().length() > 0) {

					try {
						
						char c = charTextField.getText().charAt(0);

						int x = outline.x;
						int y = outline.y;
						int width = outline.width;
						int height = outline.height;
						
						int offsetX = Integer.valueOf(charOffsetXTextField.getText());
						int offsetY = Integer.valueOf(charOffsetYTextField.getText());

						Rectangle bounds = new Rectangle(x, y, width, height); 
						Point offset = new Point(offsetX, offsetY);
						
						Font font = fontManager.getFont();
						
						if(font != null) {
							
							Symbol addedSymbol = new Symbol(c, bounds, offset);
							Symbol removedSymbol = font.getSymbol(c);
							
							UndoableEdit addUndoableEdit = new AddUndoableEdit(addedSymbol, removedSymbol);

							font.removeSymbol(addedSymbol);
							font.addSymbol(addedSymbol);
							
							undoManager.postEdit(addUndoableEdit);
							
							((UndoMenuItem)undoMenuItem).update();
							((RedoMenuItem)redoMenuItem).update();

							fontManager.setDirty(true);
							
							((EditorFrame)editorFrame).update();

							refresh();
info();
						}

					} catch(NumberFormatException e) {
						
						String message = localeManager.getValue(MESSAGE_CHAR_OFFSET_NOT_INTEGER_TEXT_LOCALIZE_KEY);
						showErrorMessageDialog(message);
					}
					
				} else {
					
					String message = localeManager.getValue(MESSAGE_CHAR_OFFSET_NOT_SPECIFIED_TEXT_LOCALIZE_KEY);
					showErrorMessageDialog(message);
				}
			
			} else {
				
				String message = localeManager.getValue(MESSAGE_CHAR_NOT_SPECIFIED_TEXT_LOCALIZE_KEY);
				showErrorMessageDialog(message);
			}
			
		} else {
			
			String message = localeManager.getValue(MESSAGE_MARQUEE_NOT_SPECIFIED_TEXT_LOCALIZE_KEY);
			showErrorMessageDialog(message);
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeDelete() {

		if(charTextField.getText().length() > 0) {
			
			char c = charTextField.getText().charAt(0);
			
			Font font = fontManager.getFont();
			
			if(font != null) {
				
				Symbol symbol = font.getSymbol(c);
				
				if(symbol != null) {
					
					UndoableEdit deleteUndoableEdit = new DeleteUndoableEdit(symbol);
					
					font.removeSymbol(symbol);
				
					undoManager.postEdit(deleteUndoableEdit);
					
					((UndoMenuItem)undoMenuItem).update();
					((RedoMenuItem)redoMenuItem).update();
					
					fontManager.setDirty(true);
					
					((EditorFrame)editorFrame).update();

					refresh();
info();
				} else {
					
					String message = localeManager.getValue(MESSAGE_CHAR_NOT_FOUND_TEXT_LOCALIZE_KEY, c);
					showErrorMessageDialog(message);
				}
			}

		} else {
			
			String message = localeManager.getValue(MESSAGE_CHAR_NOT_SPECIFIED_TEXT_LOCALIZE_KEY);
			showErrorMessageDialog(message);
		}
/*
		Object[] options = null;
		Object initialValue = null;

		options = new Object[] {
				localeManager.getValue(DIALOG_BUTTON_OK_CAPTION_LOCALIZE_KEY)
		};
		
		initialValue = options[0];  

		Exception e = new Exception("Bounds must be byte value!", new IOException("Error!"));
//		e.printStackTrace();

		OptionPane.showOptionDialog(editorFrame, "Can't export to \"postal.fnt\" file!", "Error!", OptionPane.DEFAULT_OPTION, OptionPane.ERROR_MESSAGE, null, options, initialValue, e);
*/
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeSize() {
		
		int size = ((SizeSpinner)sizeSpinner).getSizeValue();
		fontManager.getFont().setSize(size);
		
		fontManager.setDirty(true);
		
		((EditorFrame)editorFrame).update();

		refresh();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeSpacing() {
		
		int spacing = ((SpacingSpinner)spacingSpinner).getSpacingValue();
		fontManager.getFont().setSpacing(spacing);

		fontManager.setDirty(true);
		
		((EditorFrame)editorFrame).update();

		refresh();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeLeading() {
		
		int leading = ((LeadingSpinner)leadingSpinner).getStringOffsetValue();
		fontManager.getFont().setLeading(leading);

		fontManager.setDirty(true);
		
		((EditorFrame)editorFrame).update();

		refresh();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeUndo() {
		
		if(undoManager.canUndo()) {
			
			UndoableEdit edit = undoManager.editToBeUndone();

			try {
				
				undoManager.undo();
				
				((UndoMenuItem)undoMenuItem).update();
				((RedoMenuItem)redoMenuItem).update();

				fontManager.setDirty(true);
				
				((EditorFrame)editorFrame).update();

				refresh();
				
			} catch(CannotUndoException e) {
				
				String message = localeManager.getValue(MESSAGE_CANT_UNDO_ACTION_TEXT_LOCALIZE_KEY, edit.getPresentationName());
				showErrorMessageDialog(message);
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeRedo() {
		
		if(undoManager.canRedo()) {
			
			UndoableEdit edit = undoManager.editToBeRedone();

			try {
				
				undoManager.redo();
				
				((UndoMenuItem)undoMenuItem).update();
				((RedoMenuItem)redoMenuItem).update();

				fontManager.setDirty(true);
				
				((EditorFrame)editorFrame).update();

				refresh();
				
			} catch(CannotRedoException e) {
				
				String message = localeManager.getValue(MESSAGE_CANT_REDO_ACTION_TEXT_LOCALIZE_KEY, edit.getPresentationName());
				showErrorMessageDialog(message);
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executePreviewText() {
		
		String previewText = previewTextArea.getText();
		((PreviewPanel)previewPane).setPreviewText(previewText);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeLanguageSelect(Locale locale) {
		
		localeManager.setLocale(locale);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeLookAndFeelSelect(LookAndFeelInfo lookAndFeel) {
		
		lookAndFeelManager.setLookAndFeel(lookAndFeel);
		
		SwingUtilities.updateComponentTreeUI(editorFrame);
		SwingUtilities.updateComponentTreeUI(preferencesDialog);
		SwingUtilities.updateComponentTreeUI(pluginsDialog);
		SwingUtilities.updateComponentTreeUI(aboutDialog);
		SwingUtilities.updateComponentTreeUI(fontOpenFileChooser);
		SwingUtilities.updateComponentTreeUI(fontSaveFileChooser);
		SwingUtilities.updateComponentTreeUI(fontExportFileChooser);
		SwingUtilities.updateComponentTreeUI(imageOpenFileChooser);
		SwingUtilities.updateComponentTreeUI(charPopupMenu);
		SwingUtilities.updateComponentTreeUI(preferencesEditorBoundsColorChooser);
		SwingUtilities.updateComponentTreeUI(preferencesEditorBackgroundMajorColorChooser);
		SwingUtilities.updateComponentTreeUI(preferencesEditorBackgroundMinorColorChooser);
		SwingUtilities.updateComponentTreeUI(preferencesPreviewBackgroundColorChooser);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executePreferences() {

		preferencesDialog.setVisible(true);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executePreferencesOK() {
		
		if(preferencesManager.isDirty()) {
			executePreferencesApply();
		}

		preferencesDialog.setVisible(false);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executePreferencesCancel() {

		Color editorBoundsColor = preferencesManager.getEditorBoundsColor();
		((PreferencesEditorBoundsColorThumb)preferencesEditorBoundsColorThumb).setColor(editorBoundsColor);
		((PreferencesEditorBoundsColorChooser)preferencesEditorBoundsColorChooser).setColor(editorBoundsColor);

		Color editorBackgroundMajorColor = preferencesManager.getEditorBackgroundMajorColor();
		((PreferencesEditorBackgroundMajorColorThumb)preferencesEditorBackgroundMajorColorThumb).setColor(editorBackgroundMajorColor);
		((PreferencesEditorBackgroundMajorColorChooser)preferencesEditorBackgroundMajorColorChooser).setColor(editorBackgroundMajorColor);
		
		Color editorBackgroundMinorColor = preferencesManager.getEditorBackgroundMinorColor();
		((PreferencesEditorBackgroundMinorColorThumb)preferencesEditorBackgroundMinorColorThumb).setColor(editorBackgroundMinorColor);
		((PreferencesEditorBackgroundMinorColorChooser)preferencesEditorBackgroundMinorColorChooser).setColor(editorBackgroundMinorColor);
		
		int mode = preferencesManager.getPreviewMode();
		
		preferencesPreviewNoneRadioButton.setSelected(mode == PreviewPanel.MODE_NONE);
		preferencesPreviewClassicRadioButton.setSelected(mode == PreviewPanel.MODE_CLASSIC);
		preferencesPreviewFontRadioButton.setSelected(mode == PreviewPanel.MODE_FONT);
			
		Color previewBackgroundColor = preferencesManager.getPreviewBackgroundColor();
		((PreferencesPreviewBackgroundColorThumb)preferencesPreviewBackgroundColorThumb).setColor(previewBackgroundColor);
		((PreferencesPreviewBackgroundColorChooser)preferencesPreviewBackgroundColorChooser).setColor(previewBackgroundColor);
		
		preferencesManager.setDirty(false);
		refresh();

		preferencesDialog.setVisible(false);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executePreferencesApply() {
		
		Color editorBoundsColor = ((PreferencesEditorBoundsColorThumb)preferencesEditorBoundsColorThumb).getColor();
		preferencesManager.setEditorBoundsColor(editorBoundsColor);

		Color editorBackgroundMajorColor = ((PreferencesEditorBackgroundMajorColorThumb)preferencesEditorBackgroundMajorColorThumb).getColor();
		preferencesManager.setEditorBackgroundMajorColor(editorBackgroundMajorColor);
		
		Color editorBackgroundMinorColor = ((PreferencesEditorBackgroundMinorColorThumb)preferencesEditorBackgroundMinorColorThumb).getColor();
		preferencesManager.setEditorBackgroundMinorColor(editorBackgroundMinorColor);

		if(preferencesPreviewNoneRadioButton.isSelected()) {
			preferencesManager.setPreviewMode(PreviewPanel.MODE_NONE);
		} else if(preferencesPreviewClassicRadioButton.isSelected()) {
			preferencesManager.setPreviewMode(PreviewPanel.MODE_CLASSIC);
		} else if(preferencesPreviewFontRadioButton.isSelected()) {
			preferencesManager.setPreviewMode(PreviewPanel.MODE_FONT);
		}
		
		Color previewBackgroundColor = ((PreferencesPreviewBackgroundColorThumb)preferencesPreviewBackgroundColorThumb).getColor();
		preferencesManager.setPreviewBackgroundColor(previewBackgroundColor);
		
		preferencesManager.setDirty(false);
		refresh();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executePlugins() {
		
		pluginsDialog.setVisible(true);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executePluginsOK() {
		
		pluginsDialog.setVisible(false);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executePluginsCancel() {
		
		pluginsDialog.setVisible(false);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeAbout() {
		
		aboutDialog.setVisible(true);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeAboutOK() {
		
		aboutDialog.setVisible(false);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeAboutCancel() {
		
		aboutDialog.setVisible(false);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeEditorBoundsColorSelect() {
		
		Color color = ((PreferencesEditorBoundsColorChooser)preferencesEditorBoundsColorChooser).showChooseDialog(preferencesDialog);
		
		if(color != null) {
			
			((PreferencesEditorBoundsColorThumb)preferencesEditorBoundsColorThumb).setColor(color);
			
			preferencesManager.setDirty(true);
			refresh();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeEditorBackgroundMajorColorSelect() {
		
		Color color = ((PreferencesEditorBackgroundMajorColorChooser)preferencesEditorBackgroundMajorColorChooser).showChooseDialog(preferencesDialog);
		
		if(color != null) {
			
			((PreferencesEditorBackgroundMajorColorThumb)preferencesEditorBackgroundMajorColorThumb).setColor(color);
			
			preferencesManager.setDirty(true);
			refresh();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executeEditorBackgroundMinorColorSelect() {
		
		Color color = ((PreferencesEditorBackgroundMinorColorChooser)preferencesEditorBackgroundMinorColorChooser).showChooseDialog(preferencesDialog);
		
		if(color != null) {
			
			((PreferencesEditorBackgroundMinorColorThumb)preferencesEditorBackgroundMinorColorThumb).setColor(color);
			
			preferencesManager.setDirty(true);
			refresh();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executePreviewModeSelect() {
		
		preferencesManager.setDirty(true);
		refresh();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public void executePreviewBackgroundColorSelect() {
		
		Color color = ((PreferencesPreviewBackgroundColorChooser)preferencesPreviewBackgroundColorChooser).showChooseDialog(preferencesDialog);
		
		if(color != null) {
			
			((PreferencesPreviewBackgroundColorThumb)preferencesPreviewBackgroundColorThumb).setColor(color);

			preferencesManager.setDirty(true);
			refresh();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	private void refresh() {
		
		saveButton.setEnabled(fontManager.isDirty() || fontManager.getFile() == null);
		saveMenuItem.setEnabled(fontManager.isDirty() || fontManager.getFile() == null);
		
		undoButton.setEnabled(undoManager.canUndo());
		undoMenuItem.setEnabled(undoManager.canUndo());
		
		redoButton.setEnabled(undoManager.canRedo());
		redoMenuItem.setEnabled(undoManager.canRedo());
		
		boolean canZoomIn = ((EditorPanel)editorPane).canZoomIn();
		
		zoomInButton.setEnabled(canZoomIn);
		zoomInMenuItem.setEnabled(canZoomIn);
		
		boolean canZoomOut = ((EditorPanel)editorPane).canZoomOut();
		
		zoomOutButton.setEnabled(canZoomOut);
		zoomOutMenuItem.setEnabled(canZoomOut);
		
		preferencesApplyButton.setEnabled(preferencesManager.isDirty());
	}

	//////////////////////////////////////////////////////////////////////
	// Inner class ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private class DialogThread implements Runnable {
		
		private final Component parent;
		private final Object message;
		private final String title;
		private final int optionType;
		private final int messageType;
		private final Icon icon;
		private final Object[] options;
		private final Object initialValue; 
		
		private int returnValue;
		
		//////////////////////////////////////////////////////////////////
		//
		//////////////////////////////////////////////////////////////////
		
		private DialogThread(Component parent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue) {
			
			this.parent = parent;
			this.message = message;
			this.title = title;
			this.optionType = optionType;
			this.messageType = messageType;
			this.icon = icon;
			this.options = options;
			this.initialValue = initialValue;
			
			returnValue = OptionPane.CLOSED_OPTION;
		}

		//////////////////////////////////////////////////////////////////
		//
		//////////////////////////////////////////////////////////////////
		
		public void run() {
			
			returnValue = OptionPane.showOptionDialog(parent, message, title, optionType, messageType, icon, options, initialValue);
		}
	}
}