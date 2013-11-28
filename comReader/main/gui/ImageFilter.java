
package gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;


public class ImageFilter extends FileFilter {

    // Accepts all directories and all png files.
	
    public boolean accept(File file) {
    	
        if (file.isDirectory()) {
            return true;
        }

        String extension = null;
        String fileName = file.getName();
        
        int i = fileName.lastIndexOf('.');

        if (i > 0 &&  i < fileName.length() - 1) {
        	extension = fileName.substring(i+1).toLowerCase();
        }
        if (extension != null) {
        	
            return (extension.equals("png") ? true:false);
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Just Images";
    }
}