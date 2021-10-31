package algorithms;

import java.io.IOException;

public class MainClass {
	
	public static void main(String[] args) throws IOException{
		
		//Obtain the path of files
		String importPath=ImportPath.getPath();
		String exportPath=OutPath.getPath();
		
		//Read the header of the DEM file
		String[] header=DataImport.readTop(importPath);
		int[] size=DataImport.dataSize(header);
		double[] cellsize_nodata=DataImport.gridSize(header);
		int x=size[0];
		int y=size[1];
		double cellsize=cellsize_nodata[0];
		double nodata=cellsize_nodata[1];
				
		//Import the DEM
		//Here two rows and two cols are with noData value are added to the DEM for simple calculation.
		double[][] dem=DataImport.gridRead(importPath, x, y, nodata);
		
		//Calculate the TCA
		//short[][] dir=iFAD8.direction(dem, cellsize);
		//double[][] tca=iFAD8.TCA(dem, dir, cellsize);
		double[][] tca=iTFM_AS.TCA(dem, cellsize);
		//double[][] tca=Dinfinite.TCA(dem, cellsize);
		
		//Save data
		DataExport.save(tca, exportPath, header);
		//DataExport.shortSave(dir, exportPath, header);
				
		System.out.println("The result has been output!");
		
		
		
		
	}
}
