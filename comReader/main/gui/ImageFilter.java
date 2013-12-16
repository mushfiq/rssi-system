/*
 * 
 * 
 */
package gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * This class extends <code>FileFilter</code> and is supplied to the <code>JFileChooser</code> in order to display only
 * specific files and directories. Here it is used to filter everything except directories (so that the user can browse
 * through the file system) and .png images.
 * 
 * @author Danilo
 * @see JFileChooser
 */
public class ImageFilter extends FileFilter {

	/**
	 * Overridden method in order to display only directories and .png files.
	 * 
	 * @param file
	 *            One of the <code>File</code>s in the file system
	 * @return <code>boolean</code> is file accepted
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File file) {

		if (file.isDirectory()) {
			return true;
		}

		String extension = null;
		String fileName = file.getName();

		int i = fileName.lastIndexOf('.');

		if (i > 0 && i < fileName.length() - 1) {
			extension = fileName.substring(i + 1).toLowerCase();
		}
		if (extension != null) {

			return (extension.equals("png") ? true : false);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "PNG files only";
	}
}
