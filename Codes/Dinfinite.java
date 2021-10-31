package algorithms;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Dinfinite {

	//Code of locations of cells.
	//    8   1   2
	//    7   -1  3
	//    6   5   4
	//The cell 8 is also coded as 0, the cell 1 is also coded as 9.
	private static int[] a={1,0,-1,-1,-1,0,1,1,1,0};
	private static int[] b={1,1,1,0,-1,-1,-1,0,1,1};
	private static double pi4=Math.PI/4.0;
	private static double sqr2=Math.sqrt(2.0);
	private static double att=0.0000001;
	
	public static double[][] TCA(double[][] dem, double cellsize){
		
		int x=dem.length-2;
		int y=dem[1].length-2;
		//double[][] dir=new double[x][y];
		double[][] tca=new double[x][y];
		boolean[][] inQueue=new boolean[x+2][y+2];
		double e0,e1,e2;
		double r,r1,r2,s,s1,s2,s_max;
		double d=cellsize;
		double area=cellsize*cellsize;
		int m,n;
		int xx,yy;
		Queue<block1> priQ=new PriorityQueue<>(11,zComparator);  //Priority queue
		block1 blo;
		int q,d1,d2;
		
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
				if(dem[i][j]<-9990.0){
					tca[i-1][j-1]=-9999.0;
					inQueue[i][j]=true;
				}else{
					tca[i-1][j-1]=area;
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
				}
			}
		}
		
		while(priQ.size()!=0){
			blo=priQ.poll();
			xx=blo.getX();
			yy=blo.getY();
			e0=dem[xx][yy];
			s_max=0;
			r1=0;
			q=0;
			for(int p=1;p<9;p++){
				if(p%2==1){
					e1=dem[xx+a[p]][yy+b[p]];
					e2=dem[xx+a[p+1]][yy+b[p+1]];
				}else{
					e2=dem[xx+a[p]][yy+b[p]];
					e1=dem[xx+a[p+1]][yy+b[p+1]];
				}
				if(e1<e0-att || e2<e0-att){
					s1=(e0-e1)/d;
					s2=(e1-e2)/d;
					
					if(e1>e0-att){
						r=pi4;
						s=(e0-e2)/(sqr2*d);
					}else if(e2>e0-att){
						r=0;
						s=s1;
					}else{
						r=Math.atan(s2/s1);
						s=Math.sqrt(s1*s1+s2*s2);
						if(r<0){
							r=0;
							s=s1;
						}else if(r>pi4){
							r=pi4;
							s=(e0-e2)/(d*sqr2);
						}
					}
					
					//System.out.println(p+"\t"+r+"\t"+s+"\t"+s1+"\t"+s2+"\t");
					if(s>s_max){
						s_max=s;
						r1=r;
						//System.out.println(r1);
						q=p;
					}
				}
			}
			r2=pi4-r1;
			//System.out.println(r1);
			if(q%2==1){
				d1=q;
				d2=q+1;
			}else{
				d1=q+1;
				d2=q;
			}
			
			if(dem[xx+a[d1]][yy+b[d1]]>-9990.0 && dem[xx+a[d2]][yy+b[d2]]>-9990.0){
				tca[xx-1+a[d1]][yy-1+b[d1]]=tca[xx-1+a[d1]][yy-1+b[d1]]+tca[xx-1][yy-1]*r2/(r1+r2);
				tca[xx-1+a[d2]][yy-1+b[d2]]=tca[xx-1+a[d2]][yy-1+b[d2]]+tca[xx-1][yy-1]*r1/(r1+r2);
			}
			
			for(int p=1;p<9;p++){
				if(inQueue[xx+a[p]][yy+b[p]]==false){
					priQ.add(new block1(xx+a[p], yy+b[p], dem[xx+a[p]][yy+b[p]]));
					inQueue[xx+a[p]][yy+b[p]]=true;
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
