package algorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DataExport {
	
	public static void save(double[][] data, String path,String[] header) throws IOException{
		
    	BufferedWriter pout=new BufferedWriter(new FileWriter(path));
    	
    	for(int i=0;i<6;i++){  //write six parameters
			//pout.write(header[i]+"\r\n");
		}
    	
    	int p=data.length;
    	int q=data[1].length;
    	//write data
    	for(int i=0;i<p;i++){
    		for(int j=0;j<q;j++){
    			pout.write(data[i][j]+"\t");
    		}
    		pout.write("\r\n");
    	}
    	pout.close();
	}
	
public static void shortSave(short[][] data, String path,String[] header) throws IOException{
		
    	BufferedWriter pout=new BufferedWriter(new FileWriter(path));
    	
    	for(int i=0;i<6;i++){  //write six parameters
			//pout.write(header[i]+"\r\n");
		}
    	
    	int p=data.length;
    	int q=data[1].length;
    	//write data
    	for(int i=0;i<p;i++){
    		for(int j=0;j<q;j++){
    			pout.write(data[i][j]+"\t");
    		}
    		pout.write("\r\n");
    	}
    	pout.close();
	}

}
