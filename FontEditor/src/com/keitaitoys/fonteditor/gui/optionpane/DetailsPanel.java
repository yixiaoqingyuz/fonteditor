package com.keitaitoys.fonteditor.gui.optionpane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.keitaitoys.fonteditor.core.Manager;

public class DetailsPanel extends JPanel {
	
	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final int PANEL_BORDER_SIZE = 5;
	private static final int COMPONENT_OFFSET_SIZE = 5;
	
	private static final int ETCHED_BORDER_STYLE = EtchedBorder.LOWERED;

	private static final int PANEL_WIDTH = 300;
	private static final int SCROLL_HEIGHT = 200;
	
	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JDialog dialog;
	
	private JPanel textPane;
	private JTextArea detailsArea;
	
	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public DetailsPanel(JDialog dialog) {
		
		this.dialog = dialog;

		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		createGUI();
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
		
		JPanel dataPane = createDataPanel();

		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		
		addComponent(pane, new JSeparator(SwingConstants.HORIZONTAL), 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 1.0, 0);
		addComponent(pane, dataPane, 0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 1.0, 1.0);
		
		addComponent(pane, Box.createHorizontalStrut(PANEL_WIDTH), 0, 2, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);

		setLayout(new BorderLayout());
		add(pane, BorderLayout.CENTER);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private JPanel createDataPanel() {
		
		JPanel buttonPane = createButtonPanel();
		JPanel textPane = createTextPanel();
		
		this.textPane = textPane;
		textPane.setVisible(false);

		JPanel dataPane = new JPanel();
		dataPane.setLayout(new BorderLayout());
		dataPane.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE));

		dataPane.add(buttonPane, BorderLayout.NORTH);
		dataPane.add(textPane, BorderLayout.CENTER);
		
		return dataPane;
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private JPanel createButtonPanel() {
/*
		final JButton detailsButton = new JButton();
		
		detailsButton.setAction(new AbstractAction() {
			
			public void actionPerformed(ActionEvent e) {
				
				Dimension dimension = dialog.getSize();
				textPane.setVisible(!textPane.isVisible());
				
				detailsButton.setIcon(arg0)
				
				dimension.height = dialog.getPreferredSize().height;
				dialog.setSize(dimension);
			}
		});
		
		detailsButton.setText("Details");
*/		
		JButton detailsButton = new DetailsButton(this);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridBagLayout());
		
		addComponent(buttonPane, detailsButton, 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.LINE_END, 1.0, 0);

		return buttonPane;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private JPanel createTextPanel() {

		JTextArea detailsArea = new JTextArea();
		detailsArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(detailsArea);
		
		this.detailsArea = detailsArea;
		
		scrollPane.setMinimumSize(new Dimension(0, SCROLL_HEIGHT));
		scrollPane.setMaximumSize(new Dimension(0, SCROLL_HEIGHT));
		scrollPane.setPreferredSize(new Dimension(0, SCROLL_HEIGHT));

		JPanel textPane = new JPanel();
		textPane.setLayout(new GridBagLayout());
		
		addComponent(textPane, Box.createVerticalStrut(COMPONENT_OFFSET_SIZE), 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 0, 0);
		addComponent(textPane, scrollPane, 0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, 1.0, 1.0);

		return textPane;
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
	
	public void setDetails(String details) {
		
		detailsArea.setText(details);
		detailsArea.setCaretPosition(0);
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void setDetails(Throwable t) {
		
		StringBuffer error = new StringBuffer();
		setCauseError(t, error);
		
		setDetails(error.toString());
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private void setCauseError(Throwable t, StringBuffer error) {
		
		error.append("Message: " + t.getMessage());
		error.append("\n");
		error.append("Type: " + t.getClass().getName());
		error.append("\n");

		StackTraceElement[] trace = t.getStackTrace();
		
		for(int i = 0; i < trace.length; i++) {
			
			error.append("\t");			
			error.append("at " + trace[i]);			
			error.append("\n");
		}
		
        Throwable cause = t.getCause();

        if(cause != null) {
        	setCauseError(cause, error);
        }
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	public void executeDetails() {
		
		Dimension dimension = dialog.getSize();
		textPane.setVisible(!textPane.isVisible());
		dimension.height = dialog.getPreferredSize().height;

		dialog.setSize(dimension);
	}
} 