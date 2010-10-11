package com.keitaitoys.fonteditor.gui.preferences;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;

import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.core.PreferencesManager;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;

public class PreferencesDialog extends JDialog implements LocaleChangeListener, WindowListener {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String TITLE_LOCALIZE_KEY = "preferences.dialog.title";
	
	private static final int PANEL_BORDER_SIZE = 5;
	private static final int COMPONENT_OFFSET_SIZE = 5;

	private static final int ETCHED_BORDER_STYLE = EtchedBorder.LOWERED;

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private Frame parent;
	
    //////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public PreferencesDialog(Frame parent) {
		
		super(parent, true);
		
		this.parent = parent;
		
		Manager manager = Manager.getInstance();
		manager.registerPreferencesDialog(this);
		
		LocaleManager localeManager = LocaleManager.getInstance();
		localeManager.addLocaleChangeListener(this);

		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		createGUI();
		createKeyBindings();

		setEnabled(true);

		addWindowListener(this);

		localize();
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
	}


	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private void createGUI() {
		
		// Создает панели
		createPanel();

		// Создает диалоги выбора цветов
		createColorChooser();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private void createPanel() {
		
		JTabbedPane preferencesPane = createOptionsPanel();
		JPanel buttonPane = createButtonPanel();

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(preferencesPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);

		setContentPane(contentPane);
	}


	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private JTabbedPane createOptionsPanel() {
		
		JPanel preferencesEditorPane = createPreferencesEditorPanel();
		JPanel preferencesPreviewPane = createPreferencesPreviewPanel();
		
		JTabbedPane dataTabbedPane = new PreferencesTabbedPane();
		dataTabbedPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(ETCHED_BORDER_STYLE), BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE)));
		
		dataTabbedPane.addTab(null, preferencesEditorPane);
		dataTabbedPane.addTab(null, preferencesPreviewPane);
		
		return dataTabbedPane;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private JPanel createPreferencesEditorPanel() {
		
		JLabel boundsLabel = new PreferencesEditorBoundsLabel();
		JLabel backgroundLabel = new PreferencesEditorBackgroundLabel();

		PreferencesEditorBoundsColorThumb boundsColorThumb = new PreferencesEditorBoundsColorThumb();
		PreferencesEditorBackgroundMajorColorThumb backgroundMajorColorThumb = new PreferencesEditorBackgroundMajorColorThumb();
		PreferencesEditorBackgroundMinorColorThumb backgroundMinorColorThumb = new PreferencesEditorBackgroundMinorColorThumb();
		
		boundsLabel.setLabelFor(boundsColorThumb);
		backgroundLabel.setLabelFor(backgroundMajorColorThumb);
		
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE));
		pane.setLayout(new GridBagLayout());

		addComponent(pane, boundsLabel, 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_END, 0, 0);
		addComponent(pane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 1, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);
		addComponent(pane, boundsColorThumb, 2, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);
		addComponent(pane, Box.createVerticalStrut(COMPONENT_OFFSET_SIZE), 0, 1, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);
		addComponent(pane, backgroundLabel, 0, 2, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_END, 0, 0);
		addComponent(pane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 1, 2, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);
		addComponent(pane, backgroundMajorColorThumb, 2, 2, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);
		addComponent(pane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 3, 2, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);
		addComponent(pane, backgroundMinorColorThumb, 4, 2, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 1.0, 1.0);

		JPanel colorTitledPane = new JPanel();
		colorTitledPane.setLayout(new GridBagLayout());
		colorTitledPane.setBorder(new PreferencesEditorColorTitledBorder());

		addComponent(colorTitledPane, pane, 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.FIRST_LINE_START, 1.0, 0);

		JPanel preferencesEditorPane = new JPanel();
		preferencesEditorPane.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE));
		preferencesEditorPane.setLayout(new GridBagLayout());
		
		addComponent(preferencesEditorPane, colorTitledPane, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);

		return preferencesEditorPane;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private JPanel createPreferencesPreviewPanel() {
		
		JRadioButton preferencesPreviewNoneRadionButton = new PreferencesPreviewNoneRadioButton();
		JRadioButton preferencesPreviewClassicalRadionButton = new PreferencesPreviewClassicRadioButton();
		JRadioButton preferencesPreviewFontRadionButton = new PreferencesPreviewFontRadioButton();
		
		ButtonGroup group = new ButtonGroup();
		group.add(preferencesPreviewNoneRadionButton);
		group.add(preferencesPreviewClassicalRadionButton);
		group.add(preferencesPreviewFontRadionButton);

		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE));
		pane.setLayout(new GridBagLayout());
		
		addComponent(pane, preferencesPreviewNoneRadionButton, 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);
		addComponent(pane, preferencesPreviewClassicalRadionButton, 0, 1, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);
		addComponent(pane, preferencesPreviewFontRadionButton, 0, 2, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);
		
		JPanel textTitledPane = new JPanel();
		textTitledPane.setLayout(new GridBagLayout());
		textTitledPane.setBorder(new PreferencesPreviewTextTitledBorder());

		addComponent(textTitledPane, pane, 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.FIRST_LINE_START, 1.0, 0);
		
		JLabel backgroundLabel = new PreferencesPreviewBackgroundLabel();
		PreferencesPreviewBackgroundColorThumb backgroundColorThumb = new PreferencesPreviewBackgroundColorThumb();
		
		backgroundLabel.setLabelFor(backgroundColorThumb);
		
		pane = new JPanel();
		pane.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE));
		pane.setLayout(new GridBagLayout());

		addComponent(pane, backgroundLabel, 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_END, 0, 0);
		addComponent(pane, Box.createHorizontalStrut(COMPONENT_OFFSET_SIZE), 1, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);
		addComponent(pane, backgroundColorThumb, 2, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);

		JPanel colorTitledPane = new JPanel();
		colorTitledPane.setLayout(new GridBagLayout());
		colorTitledPane.setBorder(new PreferencesPreviewColorTitledBorder());

		addComponent(colorTitledPane, pane, 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.FIRST_LINE_START, 1.0, 0);
		
		JPanel preferencesPreviewPane = new JPanel();
		preferencesPreviewPane.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE));
		preferencesPreviewPane.setLayout(new GridBagLayout());
		
		addComponent(preferencesPreviewPane, textTitledPane, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0, GridBagConstraints.FIRST_LINE_START, 1.0, 0);
		addComponent(preferencesPreviewPane, Box.createVerticalStrut(COMPONENT_OFFSET_SIZE), 0, 1, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_START, 0, 0);
		addComponent(preferencesPreviewPane, colorTitledPane, 0, 1, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);

		return preferencesPreviewPane;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private JPanel createButtonPanel() {
		
		JButton preferencesOKButton = new PreferencesOKButton();
		JButton preferencesCancelButton = new PreferencesCancelButton();
		JButton preferencesApplyButton = new PreferencesApplyButton();

		getRootPane().setDefaultButton(preferencesOKButton);
		
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1, 3, COMPONENT_OFFSET_SIZE, 0));
		
		pane.add(preferencesOKButton);
		pane.add(preferencesCancelButton);
		pane.add(preferencesApplyButton);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridBagLayout());
		buttonPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(ETCHED_BORDER_STYLE), BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE)));
		
		addComponent(buttonPane, pane, 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_END, 1.0, 0);
		
		return buttonPane;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
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
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private void createColorChooser() {
		
		JColorChooser editorBoundsColorChooser = new PreferencesEditorBoundsColorChooser();
		JColorChooser editorBackgroundMajorColorChooser = new PreferencesEditorBackgroundMajorColorChooser();
		JColorChooser editorBackgroundMinorColorChooser = new PreferencesEditorBackgroundMinorColorChooser();
		JColorChooser previewBackgroundColorChooser = new PreferencesPreviewBackgroundColorChooser();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	private void createKeyBindings() {
		
		Action cancelAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().executePreferencesCancel();
			}
		};
		
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelAction");
		getRootPane().getActionMap().put("cancelAction", cancelAction);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public void setVisible(boolean visible) {
		
		if(visible) {
			
			pack();
			setLocationRelativeTo(parent);
		}

		super.setVisible(visible);
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
    	manager.executePreferencesCancel();
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
		
		LocaleManager localeManager = LocaleManager.getInstance();
		setTitle(localeManager.getValue(TITLE_LOCALIZE_KEY));
	}

    //////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public void localeChange(LocaleChangeEvent e) {
		
		localize();
	}
}
