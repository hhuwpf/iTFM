package algorithms;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class D8 {

	//Code of locations of cells.
	//    8   1   2
	//    7   -1  3
	//    6   5   4
	//The cell 8 is also coded as 0, the cell 1 is also coded as 9.
	private static int[] a={-1,-1,-1,0,1,1,1,0,-1,-1};
	private static int[] b={-1,0,1,1,1,0,-1,-1,-1,0};
	private static double sqr2=Math.sqrt(2.0);
	private static double[] length={0,1.0,sqr2,1.0,sqr2,1.0,sqr2,1.0,sqr2};
	private static double att=0.0000000001;
	
	public static short[][] direction(double[][] dem, double cellsize){
		
		int x=dem.length-2;
		int y=dem[1].length-2;
		short[][] dir=new short[x][y];
		
		double s,s_max;
		int d,m;
		
		for(int i=1;i<=x;i++){
			for(int j=1;j<=y;j++){
				
				if(dem[i][j]>-9000.0){
					s_max=0;
					d=0;
					for(int t=1;t<9;t++){
						//if(dem[i+a[t]][j+b[t]]>-9000.0){
							s=(dem[i][j]-dem[i+a[t]][j+b[t]])/length[t];
							if(s>s_max+att){
								s_max=s;
								d=t;
							}
						//}
					}
					if(d>0){
						dir[i-1][j-1]=(short)d;
					}else{
						m=-1;
						for(int t=1;t<9;t++){
							if(dem[i+a[t]][j+b[t]]<-9000.0 && m<0){
								dir[i-1][j-1]=(short)t;
								m=1;
							}
						}
						if(m<0){
							dir[i-1][j-1]=0;
						}
					}
				}else{
					dir[i-1][j-1]=-9999;
				}
			}
		}
		
		return dir;
		
	}
	
	public static double[][] TCA(double[][] dem, short[][] dir, double cellsize){
		
		int x=dem.length-2;
		int y=dem[1].length-2;
		double[][] tca=new double[x][y];
		boolean[][] inQueue=new boolean[x+2][y+2];
		double area=cellsize*cellsize;
		
		Queue<block1> priQ=new PriorityQueue<>(11,zComparator);  //Priority queue
		
		int m, n, x1, y1, d;
		block1 blo;
		
		for(int i=0;i<=x+1;i++){
			inQueue[i][0]=true;
			inQueue[i][y+1]=true;
		}
		
		for(int j=0;j<=y+1;j++){
			inQueue[0][j]=true;
			inQueue[x+1][j]=true;
		}
		
		for(int i=1;i<=x;i++){
			for(int j=1;j<=y;j++){
				if(dem[i][j]>-9999.0){
					tca[i-1][j-1]=1;
					m=0;
					n=0;
					for(int t=1;t<9;t++){
						if(dem[i+a[t]][j+b[t]]<dem[i][j]+att){
							m++;
						}
						if(dem[i+a[t]][j+b[t]]>dem[i][j]-att){
							n++;
						}
					}
					if(m==8 && n!=8){
						priQ.add(new block1(i,j,dem[i][j]));
						inQueue[i][j]=true;
					}else if(n==8){
						inQueue[i][j]=true;
					}else{
						inQueue[i][j]=false;
					}
				}else{
					inQueue[i][j]=true;
					tca[i-1][j-1]=-9999.0;
				}
			}
		}
		
		while(priQ.size()!=0){
			blo=priQ.poll();
			x1=blo.getX();
			y1=blo.getY();
			d=dir[x1-1][y1-1];
			if(dem[x1+a[d]][y1+b[d]]>-9000.0){
				tca[x1-1+a[d]][y1-1+b[d]]=tca[x1-1+a[d]][y1-1+b[d]]+tca[x1-1][y1-1];
			}
			for(int t=1;t<9;t++){
				if(inQueue[x1+a[t]][y1+b[t]]==false){
					priQ.add(new block1(x1+a[t], y1+b[t], dem[x1+a[t]][y1+b[t]]));
					inQueue[x1+a[t]][y1+b[t]]=true;
				}
			}
		}
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				if(tca[i][j]>-9000){
					tca[i][j]=tca[i][j]*area;
				}
			}
		}
		
		return tca;
	}
	
	public static double[][] SCA(short[][] dir, double[][] tca, double cellsize){
		int x=dir.length;
		int y=dir[1].length;
		double[][] sca=new double[x][y];
		int d;
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				if(tca[i][j]<-9000.0){
					sca[i][j]=-9999.0;
				}else if(dir[i][j]==0){
					sca[i][j]=-9999.0;
				}else{
					d=(int)dir[i][j];
					sca[i][j]=tca[i][j]/(length[d]*cellsize);
				}
			}
		}
		
		return sca;
	}
	
	//Comparator to sort the cells with elevations from high to low.
    public static Comparator<block1> zComparator = new Comparator<block1>(){
        public int compare(block1 c1, block1 c2) {
        	return  (int) ((c2.getZ() - c1.getZ())*1000000);
        }
    };

}

/**
 * A block that can store the 3-D coordinate of a DEM cell.
 */
class block1{
	private int x,y;
	private double z;
    /*设置长宽高*/
    public block1(int i,int j,double m){
    	this.x=i;
    	this.y=j;
    	this.z=m;
    }
	/*设置属性值返回*/
    public int getX(){
    	return x;
    }
    public int getY(){
    	return y;
    }
    public double getZ(){
    	return z;
    }
}