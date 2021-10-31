package algorithms;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class FMFD {

	//Code of locations of cells.
	//    8   1   2
	//    7   -1  3
	//    6   5   4
	//The cell 8 is also coded as 0, the cell 1 is also coded as 9.
	private static int[] a={-1,-1,-1,0,1,1,1,0,-1,-1};
	private static int[] b={-1,0,1,1,1,0,-1,-1,-1,0};
	private static double[] q={0,0.5,0.354,0.5,0.354,0.5,0.354,0.5,0.354};
	private static double att=0.0000000001;
	private static double sqr2=Math.sqrt(2.0);
	private static double[] length={0,1.0,sqr2,1.0,sqr2,1.0,sqr2,1.0,sqr2};
	
	public static double[][] TCA(double[][] dem, double cellsize){
		
		int x=dem.length-2;
		int y=dem[1].length-2;
		double[][] tca=new double[x][y];
		//double[][] sca=new double[x][y];
		//double[][] width=new double[x][y];
		boolean[][] inQueue=new boolean[x+2][y+2];
		boolean[][] border=new boolean[x+2][y+2];
		double area=cellsize*cellsize;
		
		Queue<block1> priQ=new PriorityQueue<>(11,zComparator);  //Priority queue
		
		block1 blo;
		int m,n,d,g,xx,yy,hasDown;
		double s,p,total_sp;
		boolean cardinal,diagonal;
		
		for(int i=0;i<=x+1;i++){
			inQueue[i][0]=true;
			inQueue[i][y+1]=true;
		}
		
		for(int j=0;j<=y+1;j++){
			inQueue[0][j]=true;
			inQueue[x+1][j]=true;
		}
		
		for(int i=0;i<=x+1;i++){
			for(int j=0;j<=y+1;j++){
				border[i][j]=false;
				if(dem[i][j]>-9990.0){
					g=0;
					for(int t=1;t<9;t++){
						if(dem[i+a[t]][j+b[t]]<-9000.0){
							g++;
						}
					}
					if(g>0){
						border[i][j]=true;
					}
				}
			}
		}
		
		for(int i=1;i<=x;i++){
			for(int j=1;j<=y;j++){
				//width[i-1][j-1]=0;
				if(dem[i][j]>-9999.0){
					tca[i-1][j-1]=area;
					//sca[i-1][j-1]=0;
					m=0;
					n=0;
					for(int t=1;t<9;t++){
						if(dem[i+a[t]][j+b[t]]<dem[i][j]+att || border[i+a[t]][j+b[t]]==true){
							m++;
						}
						if(dem[i+a[t]][j+b[t]]>dem[i][j]-att){
							n++;
						}
					}
					if(border[i][j]==true){
						inQueue[i][j]=true;
					}else if(m==8 && n!=8){
						priQ.add(new block1(i,j,dem[i][j]));
						inQueue[i][j]=true;
					}else if(n==8){
						inQueue[i][j]=true;
						//tca[i-1][j-1]=-9999.0;
					}else{
						inQueue[i][j]=false;
					}
				}else{
					inQueue[i][j]=true;
					tca[i-1][j-1]=-9999.0;
					//sca[i-1][j-1]=-9999.0;
				}
			}
		}
		
		while(priQ.size()!=0){
			blo=priQ.poll();
			xx=blo.getX();
			yy=blo.getY();
			
			n=0;
			for(int t=1;t<9;t++){
				if(dem[xx+a[t]][yy+b[t]]<-9000.0){
					n++;
				}
			}
			
			hasDown=0;
			cardinal=false;
			diagonal=false;
			if(n==0){
				total_sp=0;
				double[] sp=new double[9];
				double[] f=new double[9];
				for(int t=1;t<9;t++){
					if(dem[xx+a[t]][yy+b[t]]>dem[xx][yy]-att){
						sp[t]=0;
					}else{
						hasDown++;
						//if(t%2==1){
						//	cardinal=true;
						//}else{
						//	diagonal=true;
						//}
						s=(dem[xx][yy]-dem[xx+a[t]][yy+b[t]])/(length[t]*cellsize);
						sp[t]=Math.pow(s, 1.1);
						total_sp=total_sp+sp[t];
						//total_sp=total_sp+sp[t]*(cellsize*q[t]);
						//width[xx-1][yy-1]=width[xx-1][yy-1]+q[t]*cellsize;
					}
				}
				
				//if(cardinal==true){
				//	width[xx-1][yy-1]=width[xx-1][yy-1]+0.5*cellsize;
				//}
				
				//if(diagonal==true){
				//	width[xx-1][yy-1]=width[xx-1][yy-1]+0.354*cellsize;
				//}
				
				if(hasDown>0){
					//width[xx-1][yy-1]=0;
					for(int t=1;t<9;t++){
						f[t]=sp[t]/total_sp;
						//f[t]=(sp[t]*q[t]*cellsize)/total_sp;
						if(dem[xx+a[t]][yy+b[t]]>-9000.0){
							tca[xx-1+a[t]][yy-1+b[t]]=tca[xx-1+a[t]][yy-1+b[t]]+tca[xx-1][yy-1]*f[t];
							//sca[xx-1][yy-1]=sca[xx-1][yy-1]+tca[xx-1][yy-1]*f[t]/(length[t]*cellsize);
							//width[xx-1][yy-1]=width[xx-1][yy-1]+f[t]*cellsize*length[t];
						}
					}
					//if(width[xx-1][yy-1]>att){
						//sca[xx-1][yy-1]=tca[xx-1][yy-1]/width[xx-1][yy-1];
					//}else{
						//sca[xx-1][yy-1]=-9999.0;
					//}
					
				}else{
					//sca[xx-1][yy-1]=-9999.0;
				}
				
				//if(hasDown>0){
				//	sca[xx-1][yy-1]=tca[xx-1][yy-1]/width[xx-1][yy-1];
				//	//sca[xx-1][yy-1]=tca[xx-1][yy-1]/(0.854*cellsize);
				//}else{
				//	sca[xx-1][yy-1]=-9999.0;
				//}
				
			}else{
				//sca[xx-1][yy-1]=-9999.0;
			}
			
			for(int t=1;t<9;t++){
				if(inQueue[xx+a[t]][yy+b[t]]==false){
					priQ.add(new block1(xx+a[t],yy+b[t],dem[xx+a[t]][yy+b[t]]));
					inQueue[xx+a[t]][yy+b[t]]=true;
				}
			}
			
		}
		
		return tca;
		
	}
	
	//Comparator to sort the cells with elevations from high to low.
    public static Comparator<block1> zComparator = new Comparator<block1>(){
        public int compare(block1 c1, block1 c2) {
        	return  (int) ((c2.getZ() - c1.getZ())*1000000);
        }
    };

}
