package com.keitaitoys.fonteditor.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class DefaultResourceBundle extends ResourceBundle {

	//////////////////////////////////////////////////////////////////////
	// Description ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Consts ////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private static final String UNKNOWN_STRING = "localize_me_string";  
	
	//////////////////////////////////////////////////////////////////////
	// Variables /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Constructor ///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public Object handleGetObject(String key) {

		// Frame data
		if(key.equals("frame.editor.title")) return "{0}{1} - FontEditor";
		if(key.equals("frame.editor.untitled.name")) return "Untitled";

		// Menu data
		if(key.equals("menu.file.caption")) return "File";
		if(key.equals("menu.edit.caption")) return "Edit";
		if(key.equals("menu.options.caption")) return "Options";
		if(key.equals("menu.view.caption")) return "View";
		if(key.equals("menu.help.caption")) return "Help";
		if(key.equals("menu.language.caption")) return "Language";
		if(key.equals("menu.lookandfeel.caption")) return "Look & Feel";
		if(key.equals("menuitem.new.caption")) return "New";
		if(key.equals("menuitem.open.caption")) return "Open";
		if(key.equals("menuitem.save.caption")) return "Save";
		if(key.equals("menuitem.saveas.caption")) return "Save As...";
		if(key.equals("menuitem.export.caption")) return "Export";
		if(key.equals("menuitem.exit.caption")) return "Exit";
		if(key.equals("menuitem.undo.caption")) return "Undo";
		if(key.equals("menuitem.redo.caption")) return "Redo";
		if(key.equals("menuitem.openimage.caption")) return "Image";
		if(key.equals("menuitem.preferences.caption")) return "Preferences";
		if(key.equals("menuitem.zoomin.caption")) return "Zoom in";
		if(key.equals("menuitem.zoomout.caption")) return "Zoom out";
		if(key.equals("menuitem.zoomactual.caption")) return "Actual size";
		if(key.equals("checkboxmenuitem.showgrid.caption")) return "Show grid";
		if(key.equals("checkboxmenuitem.showbounds.caption")) return "Show bounds";
		if(key.equals("menuitem.plugins.caption")) return "Plugins";
		if(key.equals("menuitem.about.caption")) return "About";
		if(key.equals("menuitem.char.caption")) return "\"{0}\" ({1}) {2}";

		// ToolBar data
		if(key.equals("toolbar.editor.title")) return "Toolbar";
		if(key.equals("toolbar.button.new.caption")) return "";
		if(key.equals("toolbar.button.new.tooltip")) return "New";
		if(key.equals("toolbar.button.open.caption")) return "";
		if(key.equals("toolbar.button.open.tooltip")) return "Open";
		if(key.equals("toolbar.button.save.caption")) return "";
		if(key.equals("toolbar.button.save.tooltip")) return "Save";
		if(key.equals("toolbar.button.export.caption")) return "";
		if(key.equals("toolbar.button.export.tooltip")) return "Export";
		if(key.equals("toolbar.button.undo.caption")) return "";
		if(key.equals("toolbar.button.undo.tooltip")) return "Undo";
		if(key.equals("toolbar.button.redo.caption")) return "";
		if(key.equals("toolbar.button.redo.tooltip")) return "Redo";
		if(key.equals("toolbar.button.marqueetool.caption")) return "";
		if(key.equals("toolbar.button.marqueetool.tooltip")) return "Tool \"Marquee\"";
		if(key.equals("toolbar.button.selecttool.caption")) return "";
		if(key.equals("toolbar.button.selecttool.tooltip")) return "Tool \"Select\"";
		if(key.equals("toolbar.button.openimage.caption")) return "";
		if(key.equals("toolbar.button.openimage.tooltip")) return "Image";
		if(key.equals("toolbar.button.zoomin.caption")) return "";
		if(key.equals("toolbar.button.zoomin.tooltip")) return "Zoom in";
		if(key.equals("toolbar.button.zoomout.caption")) return "";
		if(key.equals("toolbar.button.zoomout.tooltip")) return "Zoom out";
		if(key.equals("toolbar.button.zoomactual.caption")) return "";
		if(key.equals("toolbar.button.zoomactual.tooltip")) return "Actual size";
		if(key.equals("toolbar.button.preferences.caption")) return "";
		if(key.equals("toolbar.button.preferences.tooltip")) return "Preferences";

		// Preferences data
		if(key.equals("preferences.dialog.title")) return "Preferences";
		if(key.equals("preferences.tabbedpane.tab.editor.caption")) return "Editor";
		if(key.equals("preferences.tabbedpane.tab.preview.caption")) return "Preview";
		if(key.equals("preferences.editor.titledborder.color.title")) return "Color";
		if(key.equals("preferences.editor.label.bounds.caption")) return "Bounds";
		if(key.equals("preferences.editor.colorchooser.bounds.title")) return "Choose bounds color";
		if(key.equals("preferences.editor.label.background.caption")) return "Background";
		if(key.equals("preferences.editor.colorchooser.background.major.title")) return "Choose editor background major color";
		if(key.equals("preferences.editor.colorchooser.background.minor.title")) return "Choose editor background minor color";
		if(key.equals("preferences.preview.titledborder.text.title")) return "Text";
		if(key.equals("preferences.preview.radiobutton.none.caption")) return "None";
		if(key.equals("preferences.preview.radiobutton.classic.caption")) return "Classic";
		if(key.equals("preferences.preview.radiobutton.font.caption")) return "Font";
		if(key.equals("preferences.preview.titledborder.color.title")) return "Color";
		if(key.equals("preferences.preview.label.background.caption")) return "Background";
		if(key.equals("preferences.preview.colorchooser.background.title")) return "Choose preview background color";
		if(key.equals("preferences.button.ok.caption")) return "OK";
		if(key.equals("preferences.button.cancel.caption")) return "Cancel";
		if(key.equals("preferences.button.apply.caption")) return "Apply";

		// Plugins data
		if(key.equals("plugins.dialog.title")) return "Plugins";
		if(key.equals("plugins.table.column.provider.title")) return "Provider";
		if(key.equals("plugins.table.column.name.title")) return "Name";
		if(key.equals("plugins.table.column.version.title")) return "Version";
		if(key.equals("plugins.table.column.description.title")) return "Description";
		if(key.equals("plugins.button.ok.caption")) return "OK";

		// About data
		if(key.equals("about.dialog.title")) return "About";
		if(key.equals("about.textpane.title.text")) return "FontEditor";
		if(key.equals("about.textpane.version.text")) return "version {0}";
		if(key.equals("about.textpane.copyright.text")) return "Copyright © {0} {1}. All rights reserved.";
		if(key.equals("about.button.ok.caption")) return "OK";

		// Editor data
		if(key.equals("editor.label.char.caption")) return "Char";
		if(key.equals("editor.label.charoffsetx.caption")) return "OffsetX";
		if(key.equals("editor.label.charoffsety.caption")) return "OffsetY";
		if(key.equals("editor.button.add.caption")) return "Add";
		if(key.equals("editor.button.delete.caption")) return "Delete";
		if(key.equals("editor.undoableedit.add.name")) return "Add \"{0}\"";
		if(key.equals("editor.undoableedit.undo.add.name")) return "Undo Add \"{0}\"";
		if(key.equals("editor.undoableedit.redo.add.name")) return "Redo Add \"{0}\"";
		if(key.equals("editor.undoableedit.delete.name")) return "Delete \"{0}\"";
		if(key.equals("editor.undoableedit.undo.delete.name")) return "Undo Delete \"{0}\"";
		if(key.equals("editor.undoableedit.redo.delete.name")) return "Redo Delete \"{0}\"";
		
		// Preview data
		if(key.equals("preview.label.size.caption")) return "Size";
		if(key.equals("preview.label.spacing.caption")) return "Spacing";
		if(key.equals("preview.label.leading.caption")) return "Leading";
		if(key.equals("preview.label.preview.caption")) return "Preview";
		if(key.equals("preview.text")) return "A quick brown fox jumps over the lazy dog";

		// Status data
		if(key.equals("status.label.status.caption")) return "";
		if(key.equals("status.label.coord.caption")) return "{0},{1}";
		if(key.equals("status.label.dimension.caption")) return "{0}x{1}";
		if(key.equals("status.label.scale.caption")) return "{0}%";
		
		// Filechooser data
		if(key.equals("filechooser.fontopen.title")) return "Select file to open font";
		if(key.equals("filechooser.fontsave.title")) return "Select file to save font";
		if(key.equals("filechooser.fontexport.title")) return "Select file to export font";
		if(key.equals("filechooser.imageopen.title")) return "Select file to open font image";

		// Optionpane buttons
		if(key.equals("optionpane.button.details.caption")) return "Details";

		// Dialog titles
		if(key.equals("dialog.plain.title")) return "Message!";
		if(key.equals("dialog.information.title")) return "Important!";
		if(key.equals("dialog.question.title")) return "Attention!";
		if(key.equals("dialog.warning.title")) return "Warning!";
		if(key.equals("dialog.error.title")) return "Error!";

		// Dialog buttons
		if(key.equals("dialog.button.ok.caption")) return "OK";
		if(key.equals("dialog.button.yes.caption")) return "Yes";
		if(key.equals("dialog.button.no.caption")) return "No";
		if(key.equals("dialog.button.cancel.caption")) return "Cancel";
		if(key.equals("dialog.button.skip.caption")) return "Skip";
		if(key.equals("dialog.button.retry.caption")) return "Retry";
		if(key.equals("dialog.button.ignore.caption")) return "Ignore";
		if(key.equals("dialog.button.abort.caption")) return "Abort";

		// Dialog messages
		if(key.equals("message.not.image.file.text")) return "File \"{0}\" is not an image file!";
		if(key.equals("message.cant.read.file.text")) return "Can't read \"{0}\" file!";
		if(key.equals("message.cant.write.file.text")) return "Can't write to \"{0}\" file!";
		if(key.equals("message.cant.export.file.text")) return "Can't export to \"{0}\" file!";
		if(key.equals("message.cant.find.file.text")) return "Can't find \"{0}\" file!";
		if(key.equals("message.file.already.exist.text")) return "File \"{0}\" already exists! Do you want to replace it?";
		if(key.equals("message.save.changes.text")) return "Do you want to save changes?";
		if(key.equals("message.marquee.not.specified.text")) return "No marquee area specified!";
		if(key.equals("message.char.not.specified.text")) return "No char specified!";
		if(key.equals("message.char.not.found.text")) return "Can't find char \"{0}\"!";
		if(key.equals("message.char.offset.not.specified.text")) return "No char offset specified!";
		if(key.equals("message.char.offset.not.integer.text")) return "Offset must be a numeric value!";
		if(key.equals("message.font.format.not.supported.text")) return "Font format is not supported!";
		if(key.equals("message.font.image.file.not.image.text")) return "Font image file \"{0}\" is not an image file!";
		if(key.equals("message.cant.read.font.image.file.text")) return "Can't read font image file \"{0}\"!";
		if(key.equals("message.cant.find.font.image.file.text")) return "Can't find font image file \"{0}\"!";
		if(key.equals("message.font.image.file.not.specified.text")) return "Font image file is not specified!";
		if(key.equals("message.cant.undo.action.text")) return "Can't undo \"{0}\" action!";
		if(key.equals("message.cant.redo.action.text")) return "Can't redo \"{0}\" action!";
		if(key.equals("message.cant.save.properties.file.text")) return "An error occured while saving properties file!";
		
		return UNKNOWN_STRING + " " + "(key=" + key + ")";
    }

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	public Enumeration<String> getKeys() {

		return Collections.enumeration(keySet());
    }

	//////////////////////////////////////////////////////////////////////
	// Functions ///////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////

	protected Set<String> handleKeySet() {

		return new HashSet<String>(Arrays.asList(

			// Frame data
			"frame.editor.title",
			"frame.editor.untitled.name",
			
			// Menu data
			"menu.file.caption",
			"menu.edit.caption",
			"menu.options.caption",
			"menu.view.caption",
			"menu.help.caption",
			"menu.language.caption",
			"menu.lookandfeel.caption",
			"menuitem.new.caption",
			"menuitem.open.caption",
			"menuitem.save.caption",
			"menuitem.saveas.caption",
			"menuitem.export.caption",
			"menuitem.exit.caption",
			"menuitem.undo.caption",
			"menuitem.redo.caption",
			"menuitem.openimage.caption",
			"menuitem.preferences.caption",
			"menuitem.zoomin.caption",
			"menuitem.zoomout.caption",
			"menuitem.zoomactual.caption",
			"checkboxmenuitem.showgrid.caption",
			"checkboxmenuitem.showbounds.caption",
			"menuitem.plugins.caption",
			"menuitem.about.caption",
			"menuitem.char.caption",
			
			// ToolBar data
			"toolbar.editor.title",
			"toolbar.button.new.caption",
			"toolbar.button.new.tooltip",
			"toolbar.button.open.caption",
			"toolbar.button.open.tooltip",
			"toolbar.button.save.caption",
			"toolbar.button.save.tooltip",
			"toolbar.button.export.caption",
			"toolbar.button.export.tooltip",
			"toolbar.button.undo.caption",
			"toolbar.button.undo.tooltip",
			"toolbar.button.redo.caption",
			"toolbar.button.redo.tooltip",
			"toolbar.button.marqueetool.caption",
			"toolbar.button.marqueetool.tooltip",
			"toolbar.button.selecttool.caption",
			"toolbar.button.selecttool.tooltip",
			"toolbar.button.openimage.caption",
			"toolbar.button.openimage.tooltip",
			"toolbar.button.zoomin.caption",
			"toolbar.button.zoomin.tooltip",
			"toolbar.button.zoomout.caption",
			"toolbar.button.zoomout.tooltip",
			"toolbar.button.zoomactual.caption",
			"toolbar.button.zoomactual.tooltip",
			"toolbar.button.preferences.caption",
			"toolbar.button.preferences.tooltip",
			
			// Preferences data
			"preferences.dialog.title",
			"preferences.tabbedpane.tab.editor.caption",
			"preferences.tabbedpane.tab.preview.caption",
			"preferences.editor.titledborder.color.title",
			"preferences.editor.label.bounds.caption",
			"preferences.editor.colorchooser.bounds.title",
			"preferences.editor.label.background.caption",
			"preferences.editor.colorchooser.background.major.title",
			"preferences.editor.colorchooser.background.minor.title",
			"preferences.preview.titledborder.text.title",
			"preferences.preview.radiobutton.none.caption",
			"preferences.preview.radiobutton.classic.caption",
			"preferences.preview.radiobutton.font.caption",
			"preferences.preview.titledborder.color.title",
			"preferences.preview.label.background.caption",
			"preferences.preview.colorchooser.background.title",
			"preferences.button.ok.caption",
			"preferences.button.cancel.caption",
			"preferences.button.apply.caption",

			// Plugins data
			"plugins.dialog.title",
			"plugins.table.column.provider.title",
			"plugins.table.column.name.title",
			"plugins.table.column.version.title",
			"plugins.table.column.description.title",
			"plugins.button.ok.caption",

			// About data
			"about.dialog.title",
			"about.textpane.title.text",
			"about.textpane.version.text",
			"about.textpane.copyright.text",
			"about.button.ok.caption",
			
			// Editor data
			"editor.label.char.caption",
			"editor.label.charoffsetx.caption",
			"editor.label.charoffsety.caption",
			"editor.button.add.caption",
			"editor.button.delete.caption",
			"editor.undoableedit.add.name",
			"editor.undoableedit.undo.add.name",
			"editor.undoableedit.redo.add.name",
			"editor.undoableedit.delete.name",
			"editor.undoableedit.undo.delete.name",
			"editor.undoableedit.redo.delete.name",
			
			// Preview data
			"preview.label.size.caption",
			"preview.label.spacing.caption",
			"preview.label.leading.caption",
			"preview.label.preview.caption",
			"preview.text",

			// Status data
			"status.label.status.caption",
			"status.label.coord.caption",
			"status.label.dimension.caption",
			"status.label.scale.caption",
			
			// Filechooser data
			"filechooser.fontopen.title",
			"filechooser.fontsave.title",
			"filechooser.fontexport.title",
			"filechooser.imageopen.title",

			// Optionpane buttons
			"optionpane.button.details.caption",
			
			// Dialog titles
			"dialog.plain.title",
			"dialog.information.title",
			"dialog.question.title",
			"dialog.warning.title",
			"dialog.error.title",

			// Dialog buttons
			"dialog.button.ok.caption",
			"dialog.button.yes.caption",
			"dialog.button.no.caption",
			"dialog.button.cancel.caption",
			"dialog.button.skip.caption",
			"dialog.button.retry.caption",
			"dialog.button.ignore.caption",
			"dialog.button.abort.caption",
			
			// Dialog messages
			"message.not.image.file.text",
			"message.cant.read.file.text",
			"message.cant.write.file.text",
			"message.cant.export.file.text",
			"message.cant.find.file.text",
			"message.file.already.exist.text",
			"message.save.changes.text",
			"message.marquee.not.specified.text",
			"message.char.not.specified.text",
			"message.char.not.found.text",
			"message.char.offset.not.specified.text",
			"message.char.offset.not.integer.text",
			"message.font.format.not.supported.text",			
			"message.font.image.file.not.image.text",
			"message.cant.read.font.image.file.text",
			"message.cant.find.font.image.file.text",
			"message.font.image.file.not.specified.text",
			"message.cant.undo.action.text",
			"message.cant.redo.action.text",
			"message.cant.save.properties.file.text"
		));
    }
}