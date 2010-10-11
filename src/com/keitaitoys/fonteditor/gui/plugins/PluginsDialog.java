package com.keitaitoys.fonteditor.gui.plugins;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;

import com.keitaitoys.fonteditor.core.LocaleManager;
import com.keitaitoys.fonteditor.core.Manager;
import com.keitaitoys.fonteditor.event.LocaleChangeEvent;
import com.keitaitoys.fonteditor.event.LocaleChangeListener;

public class PluginsDialog extends JDialog implements LocaleChangeListener, WindowListener {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String TITLE_LOCALIZE_KEY = "plugins.dialog.title";
	
	private static final int PANEL_BORDER_SIZE = 5;
	private static final int COMPONENT_OFFSET_SIZE = 5;

	private static final int TABLE_SCROLL_WIDTH = 450;
	private static final int TABLE_SCROLL_HEIGHT = 200;

	private static final int ETCHED_BORDER_STYLE = EtchedBorder.LOWERED;

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private Frame parent;
	
    //////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public PluginsDialog(Frame parent) {
		
		super(parent, true);
		
		this.parent = parent;
		
		Manager manager = Manager.getInstance();
		manager.registerPluginsDialog(this);
		
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
	}


	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private void createGUI() {
		
		// Создает панели
		createPanel();
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private void createPanel() {
		
		JPanel textPane = createTablePanel();
		JPanel buttonPane = createButtonPanel();

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(textPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);

		setContentPane(contentPane);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private JPanel createTablePanel() {
		
		JTable table = new PluginsTable(); 
		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new Dimension(TABLE_SCROLL_WIDTH, TABLE_SCROLL_HEIGHT));
		
		JPanel tablePane = new JPanel();
		tablePane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(ETCHED_BORDER_STYLE), BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE)));
		tablePane.setLayout(new GridBagLayout());

		addComponent(tablePane, tableScrollPane, 0, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 1.0, 1.0);
		
		return tablePane;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private JPanel createButtonPanel() {

		JButton okButton = new PluginsOKButton();

		getRootPane().setDefaultButton(okButton);
		
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1, 1, COMPONENT_OFFSET_SIZE, 0));
		
		pane.add(okButton);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridBagLayout());
		buttonPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(ETCHED_BORDER_STYLE), BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE)));
		
		addComponent(buttonPane, pane, 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 1.0, 0);
		
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

	private void createKeyBindings() {
		
		Action cancelAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().executePluginsCancel();
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
    	manager.executePluginsCancel();
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