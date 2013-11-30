package gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;


/**
 *  ImageFilter extends FileFilter and is supplied to the
 *  JFileChooser in order to display only specific files 
 *  and directories. Here it is used to filter everything
 *  except directories (so that the user can browse through 
 *  file system) and .png images.
 */
public class ImageFilter extends FileFilter {
	
    /** 
     * Overridden method in order to display only 
     * directories and .png files.
     * 
     * @param file One of the files in the file system
     * @return Is file accepted
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File file) {
    	
        if (file.isDirectory()) {
            return true;
        }

        String extension = null;
        String fileName = file.getName();
        
        int i = fileName.lastIndexOf('.');

        if (i > 0 &&  i < fileName.length() - 1) {
        	extension = fileName.substring(i + 1).toLowerCase();
        }
        if (extension != null) {
        	
            return (extension.equals("png") ? true : false);
        }

        return false;
    }

    
    public String getDescription() {
        return "PNG files only";
    }
}
