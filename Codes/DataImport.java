package algorithms;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataImport {
	
	public static String[] readTop(String path) throws IOException{  //读取dem文件前6行
		FileInputStream file1 = new FileInputStream(path); 
		BufferedReader in1 = new BufferedReader(new InputStreamReader(file1));
		String[] data=new String[6];
		for(int i=0;i<6;i++){
			data[i]=in1.readLine().trim();
		}
		in1.close();
		return data;
	}
	
	public static int[] dataSize(String[] top){  //栅格数据行列数及无效值
		int[] size=new int[2];
		String[] str1=top[1].split("[\\p{Space}]+");
		String[] str2=top[0].split("[\\p{Space}]+");
		size[0]=Integer.valueOf(str1[1]);
		size[1]=Integer.valueOf(str2[1]);
		return size;
	}
	
	public static double[] gridSize(String[] top){  //栅格数据行列数及无效值
		double[] size=new double[2];
		String[] str1=top[4].split("[\\p{Space}]+");
		String[] str2=top[5].split("[\\p{Space}]+");
		size[0]=Double.valueOf(str1[1]);
		size[1]=Double.valueOf(str2[1]);
		return size;
	}
	
	public static double[] gridCorner(String[] top){  //左下角经纬度读取
		double[] corner=new double[2];
		String[] str1=top[2].split("[\\p{Space}]+");
		String[] str2=top[3].split("[\\p{Space}]+");
		corner[0]=Double.valueOf(str1[1]);
		corner[1]=Double.valueOf(str2[1]);
		return corner;
	}
	
	public static double[][] gridRead(String path,int x,int y,double noData) throws IOException{
		double[][] data=new double[x+2][y+2];
		FileInputStream file1 = new FileInputStream(path); 
		BufferedReader in1 = new BufferedReader(new InputStreamReader(file1));
		
		double nodata1=noData+1;
		double nodata2=noData-1;
		
		for(int i=0;i<6;i++){
			in1.readLine();
		}
		for(int i=0;i<x+2;i++){
			data[i][0]=-9999.0;
			data[i][y+1]=-9999.0;
		}
		for(int j=0;j<y+2;j++){
			data[0][j]=-9999.0;
			data[x+1][j]=-9999.0;
		}
		for(int i=1;i<=x;i++){
			String str=in1.readLine().trim();
			String[] str1=str.split("[\\p{Space}]+");
			for(int j=1;j<=y;j++){
				data[i][j]=Double.valueOf(str1[j-1]);
				if(data[i][j]<nodata1 && data[i][j]>nodata2){
					data[i][j]=-9999.0;
				}
			}
		}
		in1.close();
		return data;
	}

}
