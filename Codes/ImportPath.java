package algorithms;

import javax.swing.JFileChooser;

public class ImportPath {
	
	public static String getPath(){
		String path="";
		JFileChooser chooser=new JFileChooser();
		chooser.setMultiSelectionEnabled(false);  
		chooser.setDialogTitle("Please select the ASCII DEM file!");  
		chooser.setAcceptAllFileFilterUsed(true); 
		int iwert = chooser.showOpenDialog(null);
	    if(iwert== chooser.APPROVE_OPTION){ 
	       path = chooser.getSelectedFile().getAbsolutePath();
	    }
	    return path;
	}

}
