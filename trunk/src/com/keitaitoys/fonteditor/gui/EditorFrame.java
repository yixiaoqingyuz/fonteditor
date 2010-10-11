package com.keitaitoys.fonteditor.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.keitaitoys.fonteditor.core.FontManager;
import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.LookAndFeelManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.core.LookAndFeelManager.LookAndFeelInfo;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;
import com.keitaitoys.fonteditor.gui.about.AboutDialog;
import com.keitaitoys.fonteditor.gui.editor.AddButton;
import com.keitaitoys.fonteditor.gui.editor.CharLabel;
import com.keitaitoys.fonteditor.gui.editor.CharOffsetXLabel;
import com.keitaitoys.fonteditor.gui.editor.CharOffsetXTextField;
import com.keitaitoys.fonteditor.gui.editor.CharOffsetYLabel;
import com.keitaitoys.fonteditor.gui.editor.CharOffsetYTextField;
import com.keitaitoys.fonteditor.gui.editor.CharPopupMenu;
import com.keitaitoys.fonteditor.gui.editor.CharTextField;
import com.keitaitoys.fonteditor.gui.editor.DeleteButton;
import com.keitaitoys.fonteditor.gui.editor.EditorPanel;
import com.keitaitoys.fonteditor.gui.plugins.PluginsDialog;
import com.keitaitoys.fonteditor.gui.preferences.PreferencesDialog;
import com.keitaitoys.fonteditor.gui.preview.PreviewLabel;
import com.keitaitoys.fonteditor.gui.preview.SpacingLabel;
import com.keitaitoys.fonteditor.gui.preview.SpacingSpinner;
import com.keitaitoys.fonteditor.gui.preview.PreviewPanel;
import com.keitaitoys.fonteditor.gui.preview.PreviewTextArea;
import com.keitaitoys.fonteditor.gui.preview.SizeLabel;
import com.keitaitoys.fonteditor.gui.preview.SizeSpinner;
import com.keitaitoys.fonteditor.gui.preview.LeadingLabel;
import com.keitaitoys.fonteditor.gui.preview.LeadingSpinner;
import com.keitaitoys.fonteditor.gui.status.DimensionLabel;
import com.keitaitoys.fonteditor.gui.status.ScaleLabel;
import com.keitaitoys.fonteditor.gui.status.StatusLabel;
import com.keitaitoys.fonteditor.gui.status.CoordLabel;
import com.keitaitoys.fonteditor.gui.toolbar.EditorToolBar;
import com.keitaitoys.fonteditor.gui.toolbar.ExportButton;
import com.keitaitoys.fonteditor.gui.toolbar.MarqueeToolButton;
import com.keitaitoys.fonteditor.gui.toolbar.NewButton;
import com.keitaitoys.fonteditor.gui.toolbar.OpenButton;
import com.keitaitoys.fonteditor.gui.toolbar.OpenImageButton;
import com.keitaitoys.fonteditor.gui.toolbar.PreferencesButton;
import com.keitaitoys.fonteditor.gui.toolbar.RedoButton;
import com.keitaitoys.fonteditor.gui.toolbar.SaveButton;
import com.keitaitoys.fonteditor.gui.toolbar.SelectToolButton;
import com.keitaitoys.fonteditor.gui.toolbar.UndoButton;
import com.keitaitoys.fonteditor.gui.toolbar.ZoomActualButton;
import com.keitaitoys.fonteditor.gui.toolbar.ZoomInButton;
import com.keitaitoys.fonteditor.gui.toolbar.ZoomOutButton;

public class EditorFrame extends JFrame implements LocaleChangeListener, WindowListener {
	
	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String TITLE_LOCALIZE_KEY = "frame.editor.title";
	private static final String UNTITLED_NAME_LOCALIZE_KEY = "frame.editor.untitled.name";

	private static final String DIRTY_STRING = "*";			// Строка указывающая что шрифт был изменен 
	
	private static final int PANEL_BORDER_SIZE = 5;			// Размер бордюра панели
	private static final int COMPONENT_OFFSET_SIZE = 5;		// Размер отступа между компонентами
	
	private static final int ETCHED_BORDER_STYLE = EtchedBorder.LOWERED;
	
	private static final int TEXT_AREA_SCROLL_WIDTH = 300;
	private static final int TEXT_AREA_SCROLL_HEIGHT = 100;
	
	private static final int MINIMUM_WIDTH = 900;
	private static final int MINIMUM_HEIGHT = 520;
	
	private static final int EDITOR_MINIMUM_WIDTH = 220;
	private static final int PREVIEW_MINIMUM_WIDTH = 220;
	
	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public EditorFrame() {
		
		LocaleManager localeManager = LocaleManager.getInstance();
		localeManager.addLocaleChangeListener(this);
		
		Manager manager = Manager.getInstance();
		manager.registerEditorFrame(this);

		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		createGUI();
		
		setEnabled(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
// TODO: somebody remove me please!
//Action actionPerformedAction = new AbstractAction() {
//    public void actionPerformed(ActionEvent e) {
//		System.out.println("KUKU");
//		LocaleManager.getInstance().temp();
//    }
//};
//getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, ActionEvent.CTRL_MASK), "actionPerformed");
//getRootPane().getActionMap().put("actionPerformed", actionPerformedAction);

		localize();

		addWindowListener(this);

		Dimension size = new Dimension(getPreferredSize());
		size.width = Math.max(MINIMUM_WIDTH, size.width);
		size.height = Math.max(MINIMUM_HEIGHT, size.height);
		
		setSize(size);

		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private void createGUI() {
		
		// Создает меню
		createMenu();

		// Создает панели
		createPanel();

		// Создает диалоги
		createDialog();
		
		// Создает диалоги выбора файлов
		createFileChooser();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Создает меню
	//////////////////////////////////////////////////////////////////////
	
	private void createMenu() {
		
		JMenu menuFile = new FileMenu();
		JMenu menuEdit = new EditMenu();
		JMenu menuOptions = new OptionsMenu();
		JMenu menuView = new ViewMenu();
		JMenu menuHelp = new HelpMenu();
		JMenu menuLanguage = new LanguageMenu();
		JMenu menuLookAndFeel = new LookAndFeelMenu();

		JMenuItem menuItemNew = new NewMenuItem();
		JMenuItem menuItemOpen = new OpenMenuItem();
		JMenuItem menuItemSave = new SaveMenuItem();
		JMenuItem menuItemSaveAs = new SaveAsMenuItem();
		JMenuItem menuItemExport = new ExportMenuItem();
		JMenuItem menuItemExit = new ExitMenuItem();
		JMenuItem menuItemUndo = new UndoMenuItem();
		JMenuItem menuItemRedo = new RedoMenuItem();
		JMenuItem menuItemOpenImage = new OpenImageMenuItem();
		JMenuItem menuItemPreferenes = new PreferencesMenuItem();
		JMenuItem menuItemZoomIn = new ZoomInMenuItem();
		JMenuItem menuItemZoomOut = new ZoomOutMenuItem();
		JMenuItem menuItemZoomActual = new ZoomActualMenuItem();
		JCheckBoxMenuItem menuItemToggleGrid = new ShowGridCheckBoxMenuItem();
		JCheckBoxMenuItem menuItemToggleBounds = new ShowBoundsCheckBoxMenuItem();
		JMenuItem menuItemPlugins = new PluginsMenuItem();
		JMenuItem menuItemAbout = new AboutMenuItem();

		// Заполняем меню "Файл"
		menuFile.add(menuItemNew);
		menuFile.add(menuItemOpen);
		menuFile.addSeparator();
		menuFile.add(menuItemSave);
		menuFile.add(menuItemSaveAs);
		menuFile.addSeparator();
		menuFile.add(menuItemExport);
		menuFile.addSeparator();
		menuFile.add(menuItemExit);

		// Заполняем меню "Редактирование"
		menuEdit.add(menuItemUndo);
		menuEdit.add(menuItemRedo);
		menuEdit.addSeparator();
		menuEdit.add(menuItemOpenImage);

		// Заполняем меню "Свойства"
		menuOptions.add(menuLanguage);
		menuOptions.add(menuLookAndFeel);
		menuOptions.add(menuItemPreferenes);

		// Заполняем меню "Просмотр"
		menuView.add(menuItemZoomIn);
		menuView.add(menuItemZoomOut);
		menuView.add(menuItemZoomActual);
		menuView.addSeparator();
		menuView.add(menuItemToggleGrid);
		menuView.add(menuItemToggleBounds);

		// Заполняем меню "Помощь"
		menuHelp.add(menuItemPlugins);
		menuHelp.add(menuItemAbout);

		// Заполняем меню "Язык"
		List<Locale> locales = LocaleManager.getInstance().getAvailableLocales();
		ButtonGroup group = new ButtonGroup();
		
		for(Locale locale : locales) {
			
			JRadioButtonMenuItem radioButtonMenuItemLanguage = new LanguageRadioButtonMenuItem(locale);
			group.add(radioButtonMenuItemLanguage);
			
			menuLanguage.add(radioButtonMenuItemLanguage);
		}

		// Заполняем меню "Стили"
		List<LookAndFeelInfo> lookAndFeels = LookAndFeelManager.getInstance().getAvailableLookAndFeels();
		ButtonGroup lookAndFeelGroup = new ButtonGroup();
		
		for(LookAndFeelInfo lookAndFeel : lookAndFeels) {
			
			JRadioButtonMenuItem radioButtonMenuItemLookAndFeel = new LookAndFeelRadioButtonMenuItem(lookAndFeel);
			lookAndFeelGroup.add(radioButtonMenuItemLookAndFeel);
			
			menuLookAndFeel.add(radioButtonMenuItemLookAndFeel);
		}
		
		JMenuBar menuBar = new JMenuBar();
		
		// Заполняем планку меню
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuOptions);
        menuBar.add(menuView);
        menuBar.add(menuHelp);
        
		setJMenuBar(menuBar);
	}

	//////////////////////////////////////////////////////////////////////
	// Создает панель инструментов
	//////////////////////////////////////////////////////////////////////
	
	private Component createToolBar() {
		
		JToolBar toolBar = new EditorToolBar();

		JButton newButton = new NewButton();
		JButton openButton = new OpenButton();
		JButton saveButton = new SaveButton();
		JButton exportButton = new ExportButton();
		JButton undoButton = new UndoButton();
		JButton redoButton = new RedoButton();
//		JButton marqueeButton = new MarqueeToolButton();
//		JButton selectButton = new SelectToolButton();
		JButton openImageButton = new OpenImageButton();
		JButton zoomInButton = new ZoomInButton();
		JButton zoomOutButton = new ZoomOutButton();
		JButton zoomActualButton = new ZoomActualButton();
		JButton preferencesButton = new PreferencesButton();
		
		toolBar.add(newButton);
		toolBar.add(openButton);
		toolBar.add(saveButton);
		toolBar.add(exportButton);
		toolBar.addSeparator();
		toolBar.add(undoButton);
		toolBar.add(redoButton);
		toolBar.addSeparator();
//		toolBar.add(marqueeButton);
//		toolBar.add(selectButton);
		toolBar.add(openImageButton);
		toolBar.addSeparator();
		toolBar.add(zoomInButton);
		toolBar.add(zoomOutButton);
		toolBar.add(zoomActualButton);
		toolBar.addSeparator();
		toolBar.add(preferencesButton);
		
		return toolBar;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Создает панели
	//////////////////////////////////////////////////////////////////////
	
	private void createPanel() {
		
		Component toolBar = createToolBar();		
		Component editorPane = createEditorPanel();		
		Component previewPane = createPreviewPanel();
		Component statusPane = createStatusPanel();

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(toolBar, BorderLayout.NORTH);
		contentPane.add(editorPane, BorderLayout.CENTER);
		contentPane.add(previewPane, BorderLayout.EAST);
		contentPane.add(statusPane, BorderLayout.SOUTH);

		setContentPane(contentPane);
	}
	
	//////////////////////////////////////////////////////////////////////
	// 
	//////////////////////////////////////////////////////////////////////
	
	private Component createEditorPanel() {
		
		Component editorDataPane = createEditorDataPanel();
		Component editorFieldPane = createEditorFieldPanel();
		
		JPanel editorPane = new JPanel();
		editorPane.setLayout(new GridBagLayout());
		editorPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(ETCHED_BORDER_STYLE), BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE)));

		JScrollPane scrollPane = ((EditorPanel)editorFieldPane).getScrollPane();

		addComponent(editorPane, editorDataPane, 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		addComponent(editorPane, scrollPane, 0, 2, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 1.0, 1.0);
		
		addComponent(editorPane, Box.createVerticalStrut(COMPONENT_OFFSET_SIZE), 0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);

		return editorPane;
	}

	//////////////////////////////////////////////////////////////////////
	// 
	//////////////////////////////////////////////////////////////////////
	
	private Component createEditorDataPanel() {
		
		JLabel charLabel = new CharLabel();
		JLabel charOffsetXLabel = new CharOffsetXLabel();
		JLabel charOffsetYLabel = new CharOffsetYLabel();

		JTextField charTextField = new CharTextField();
		JTextField charOffsetXTextField = new CharOffsetXTextField();
		JTextField charOffsetYTextField = new CharOffsetYTextField();

		charLabel.setLabelFor(charTextField);
		charOffsetXLabel.setLabelFor(charOffsetXTextField);
		charOffsetYLabel.setLabelFor(charOffsetYTextField);
		
		JPanel inputPane = new JPanel();
		inputPane.setLayout(new GridLayout(2, 3, COMPONENT_OFFSET_SIZE, 0));
		
		inputPane.add(charLabel);
		inputPane.add(charOffsetXLabel);
		inputPane.add(charOffsetYLabel);
		inputPane.add(charTextField);
		inputPane.add(charOffsetXTextField);
		inputPane.add(charOffsetYTextField);
		
		JButton addButton = new AddButton();
		JButton deleteButton = new DeleteButton();
		
		getRootPane().setDefaultButton(addButton);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridLayout(1, 2, COMPONENT_OFFSET_SIZE, 0));
		
		buttonPane.add(addButton);
		buttonPane.add(deleteButton);
		
		JPanel editorDataPane = new JPanel();
		editorDataPane.setLayout(new GridBagLayout());
		
		addComponent(editorDataPane, inputPane, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		addComponent(editorDataPane, buttonPane, 0, 2, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		
		addComponent(editorDataPane, Box.createVerticalStrut(COMPONENT_OFFSET_SIZE), 0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		addComponent(editorDataPane, Box.createHorizontalStrut(EDITOR_MINIMUM_WIDTH), 0, 3, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		
		return editorDataPane;
	}
	
	//////////////////////////////////////////////////////////////////////
	// 
	//////////////////////////////////////////////////////////////////////
	
	private Component createEditorFieldPanel() {
		
		JPopupMenu charPopupMenu = new CharPopupMenu();
		
		JPanel editorFieldPane = new EditorPanel();
		
		return editorFieldPane;
	}
	
	//////////////////////////////////////////////////////////////////////
	// 
	//////////////////////////////////////////////////////////////////////
	
	private Component createPreviewPanel() {
		
		Component previewDataPane = createPreviewDataPanel();
		Component previewFieldPane = createPreviewFieldPanel();
		Component previewTextPane = createPreviewTextPanel();
		
		JPanel previewPane = new JPanel();
		previewPane.setLayout(new GridBagLayout());
		previewPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(ETCHED_BORDER_STYLE), BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE)));

		JScrollPane scrollPane = ((PreviewPanel)previewFieldPane).getScrollPane();
		
		addComponent(previewPane, previewDataPane, 0, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		addComponent(previewPane, scrollPane, 0, 2, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 1.0, 1.0);
		addComponent(previewPane, previewTextPane, 0, 4, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		
		addComponent(previewPane, Box.createVerticalStrut(COMPONENT_OFFSET_SIZE), 0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		addComponent(previewPane, Box.createVerticalStrut(COMPONENT_OFFSET_SIZE), 0, 3, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);

		return previewPane;
	}

	//////////////////////////////////////////////////////////////////////
	// 
	//////////////////////////////////////////////////////////////////////
	
	private Component createPreviewDataPanel() {
		
		JLabel sizeLabel = new SizeLabel();
		JLabel spacingLabel = new SpacingLabel();
		JLabel leadingLabel = new LeadingLabel();

		JSpinner sizeSpinner = new SizeSpinner();
		JSpinner spacingSpinner = new SpacingSpinner();
		JSpinner leadingSpinner = new LeadingSpinner();

		sizeLabel.setLabelFor(sizeSpinner);
		spacingLabel.setLabelFor(spacingSpinner);
		leadingLabel.setLabelFor(leadingSpinner);
		
		JPanel inputPane = new JPanel();
		inputPane.setLayout(new GridLayout(2, 3, COMPONENT_OFFSET_SIZE, 0));
		
		inputPane.add(sizeLabel);
		inputPane.add(spacingLabel);
		inputPane.add(leadingLabel);
		inputPane.add(sizeSpinner);
		inputPane.add(spacingSpinner);
		inputPane.add(leadingSpinner);
		
		JPanel previewDataPane = new JPanel();
		previewDataPane.setLayout(new GridBagLayout());
		
		addComponent(previewDataPane, inputPane, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		
		addComponent(previewDataPane, Box.createHorizontalStrut(PREVIEW_MINIMUM_WIDTH), 0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);

		return previewDataPane; 
	}

	//////////////////////////////////////////////////////////////////////
	// 
	//////////////////////////////////////////////////////////////////////
	
	private Component createPreviewFieldPanel() {
		
		JPanel previewFieldPane = new PreviewPanel();

		return previewFieldPane;
	}

	//////////////////////////////////////////////////////////////////////
	// 
	//////////////////////////////////////////////////////////////////////
	
	private Component createPreviewTextPanel() {
		
		JLabel previewLabel = new PreviewLabel();
		
		JTextArea previewTextArea = new PreviewTextArea();
		JScrollPane scrollPane = new JScrollPane(previewTextArea);

		previewLabel.setLabelFor(previewTextArea);

		scrollPane.setMinimumSize(new Dimension(TEXT_AREA_SCROLL_WIDTH, TEXT_AREA_SCROLL_HEIGHT));
		scrollPane.setPreferredSize(new Dimension(TEXT_AREA_SCROLL_WIDTH, TEXT_AREA_SCROLL_HEIGHT));
		scrollPane.setMaximumSize(new Dimension(TEXT_AREA_SCROLL_WIDTH, TEXT_AREA_SCROLL_HEIGHT));

		JPanel previewTextPane = new JPanel();
		previewTextPane.setLayout(new GridBagLayout());

		addComponent(previewTextPane, previewLabel, 0, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 1.0, 1.0);
		addComponent(previewTextPane, scrollPane, 0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 1.0, 1.0);

		return previewTextPane;
	}

	//////////////////////////////////////////////////////////////////////
	// Создает панель статуса
	//////////////////////////////////////////////////////////////////////
	
	private Component createStatusPanel() {
		
		JLabel scaleLabel = new ScaleLabel();
		JLabel statusLabel = new StatusLabel();
		JLabel coordLabel = new CoordLabel();
		JLabel dimensionLabel = new DimensionLabel();
//		JProgressBar progressBar = new ProcessProgressBar();

		JPanel statusPane = new JPanel();
		statusPane.setLayout(new GridBagLayout());
		statusPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(ETCHED_BORDER_STYLE), BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE)));
/*
		JPanel progressBarPane = new JPanel();
		GroupLayout layout = new GroupLayout(progressBarPane);
		progressBarPane.setLayout(layout);
		layout.setAutoCreateGaps(false);
		layout.setAutoCreateContainerGaps(false);
		layout.setHonorsVisibility(false);

		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(progressBar));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(progressBar));
*/
		addComponent(statusPane, scaleLabel, 0, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 1.0);
		addComponent(statusPane, new JSeparator(SwingConstants.VERTICAL), 2, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 1.0);
		addComponent(statusPane, statusLabel, 4, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 1.0, 1.0);
		addComponent(statusPane, new JSeparator(SwingConstants.VERTICAL), 6, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 1.0);
		addComponent(statusPane, coordLabel, 8, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 1.0);
		addComponent(statusPane, new JSeparator(SwingConstants.VERTICAL), 10, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 1.0);
		addComponent(statusPane, dimensionLabel, 12, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 1.0);
//		addComponent(statusPane, new JSeparator(SwingConstants.VERTICAL), 14, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 1.0);
//		addComponent(statusPane, progressBarPane, 16, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 1.0);

		addComponent(statusPane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 1, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		addComponent(statusPane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 3, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		addComponent(statusPane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 5, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		addComponent(statusPane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 7, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		addComponent(statusPane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 9, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		addComponent(statusPane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 11, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
//		addComponent(statusPane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 13, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
//		addComponent(statusPane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 15, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		
		return statusPane;
	}

	//////////////////////////////////////////////////////////////////////
	//
	//////////////////////////////////////////////////////////////////////
	
	private void addComponent(Container container, Component component, 
								int gridX,
								int gridY,
								int gridWidth,
								int gridHeight,
								int fill,
								int ipadX,
								int ipadY,
								int insetTop,
								int insetLeft,
								int insetBottom,
								int insetRight,
								int anchor,
								double weightX,
								double weightY) {
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = gridX; 
		constraints.gridy = gridY; 
		constraints.gridwidth = gridWidth;
		constraints.gridheight = gridHeight; 
		constraints.fill = fill; 
		constraints.ipadx = ipadX; 
		constraints.ipady = ipadY; 
		constraints.insets = new Insets(insetTop, insetLeft, insetBottom, insetRight);
		constraints.anchor = anchor;
		constraints.weightx = weightX;
		constraints.weighty = weightY;
		
		container.add(component, constraints);
	}

	//////////////////////////////////////////////////////////////////////
	// Создает панели
	//////////////////////////////////////////////////////////////////////
	
	private void createDialog() {
		
		JDialog preferencesDialog = new PreferencesDialog(this);
		JDialog pluginsDialog = new PluginsDialog(this);
		JDialog aboutDialog = new AboutDialog(this);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	private void createFileChooser() {
		
		JFileChooser fontOpenFileChooser = new FontOpenFileChooser();
		JFileChooser fontSaveFileChooser = new FontSaveFileChooser();
		JFileChooser fontExportFileChooser = new FontExportFileChooser();
		JFileChooser imageOpenFileChooser = new ImageOpenFileChooser();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void update() {
		
		localize();
    }

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void windowActivated(WindowEvent e) {

    }

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

    public void windowClosed(WindowEvent e) {

    }

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

    public void windowClosing(WindowEvent e) {

		Manager manager = Manager.getInstance();
    	manager.executeExit();
    }

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

    public void windowDeactivated(WindowEvent e) {

    }

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

    public void windowDeiconified(WindowEvent e) {

    }
    
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

    public void windowIconified(WindowEvent e) {

    }

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

    public void windowOpened(WindowEvent e) {

    }
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void localize() {
		
		FontManager fontManager = FontManager.getInstance();
		LocaleManager localeManager = LocaleManager.getInstance();
		
		String dirty = "";
		String name = "";
		
		File file = fontManager.getFile();
		
		if(fontManager.isDirty()) {
			dirty = DIRTY_STRING;
		}
		
		if(file != null) {
			name = file.getName();
		} else {
			name = localeManager.getValue(UNTITLED_NAME_LOCALIZE_KEY);
		}
		
		setTitle(localeManager.getValue(TITLE_LOCALIZE_KEY, dirty, name));
	}

    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void localeChange(LocaleChangeEvent e) {
		
		localize();
	}
} 