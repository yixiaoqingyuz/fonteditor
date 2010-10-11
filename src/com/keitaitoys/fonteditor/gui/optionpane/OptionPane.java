package com.keitaitoys.fonteditor.gui.optionpane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.keitaitoys.fonteditor.core.Manager;

public class OptionPane extends JOptionPane {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	public OptionPane() {
		
		init();
	}

	//////////////////////////////////////////////////////////////////////
	// Functions /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private void init() {
		
		setEnabled(true);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	public static int showOptionDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue, Throwable error) {

		JOptionPane optionPane = new JOptionPane(message, messageType, optionType, icon, options, initialValue);
		
		optionPane.setInitialValue(initialValue);
		optionPane.setComponentOrientation(((parentComponent == null) ? getRootFrame() : parentComponent).getComponentOrientation());

		JDialog dialog = optionPane.createDialog(parentComponent, title);
        optionPane.selectInitialValue();
        
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		
		Component contentPane = dialog.getContentPane(); 
/*		
		JPanel p = new JPanel();
		p.setMaximumSize(new Dimension(200, 200));
		p.setMinimumSize(new Dimension(200, 200));
		p.setPreferredSize(new Dimension(200, 200));
		p.setBackground(Color.RED);
*/
		
		DetailsPanel p = new DetailsPanel(dialog);
		p.setDetails(error);
		
		pane.add(contentPane, BorderLayout.NORTH);
		pane.add(p, BorderLayout.CENTER);
		
		dialog.setContentPane(pane);
		dialog.pack();
		dialog.setLocationRelativeTo(parentComponent);
		

        dialog.setResizable(true);
        
        dialog.show();
        dialog.dispose();

        Object selectedValue = optionPane.getValue();

        if(selectedValue == null) {
            return CLOSED_OPTION;
        }
        
        if(options == null) {
        	
            if(selectedValue instanceof Integer) {
            	return ((Integer)selectedValue).intValue();
            }
            
            return CLOSED_OPTION;
        }
        
        for(int counter = 0, maxCounter = options.length; counter < maxCounter; counter++) {
        	
            if(options[counter].equals(selectedValue)) {
                return counter;
            }
        }
        
        return CLOSED_OPTION;
	}
}