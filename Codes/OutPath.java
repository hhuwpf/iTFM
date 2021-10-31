package algorithms;

import javax.swing.JFileChooser;

public class OutPath {
	
	public static String getPath(){
		
		String path1="";
		JFileChooser fc = new JFileChooser();
		fc.setDialogType(JFileChooser.FILES_ONLY);
		fc.setDialogTitle("Please set the location and name for the export file!");
		fc.setMultiSelectionEnabled(false);
		fc.showSaveDialog(fc);
		if (fc.getSelectedFile()==null) {
			System.exit(0);
		}
		path1=fc.getSelectedFile().getPath();
		path1=path1+".txt";		
		return path1;
		
	}

}
