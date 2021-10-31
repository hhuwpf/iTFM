package algorithms;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class iFAD8 {

	//Code of locations of cells.
	//    8   1   2
	//    7   -1  3
	//    6   5   4
	//The cell 8 is also coded as 0, the cell 1 is also coded as 9.
	private static double att=0.0000000001;
	private static double pi=Math.PI;
	private static double pi4=Math.PI/4;
	private static double sqr2=Math.sqrt(2);
	private static int[] a={-1,-1,-1,0,1,1,1,0,-1,-1};
	private static int[] b={-1,0,1,1,1,0,-1,-1,-1,0};
	private static double[] length={0,1.0,sqr2,1.0,sqr2,1.0,sqr2,1.0,sqr2};
	
	/**
	 * The direction code is 1~8 firstly with the north direction is 1.
	 * The direction of a cell in a flat or depression bottom is -1.
	 * The direction of a nodata cell is -2.
	 * @param dem
	 * @param parameterPath
	 * @return directions in the Esri format
	 * @throws FileNotFoundException
	 */
	public static short[][] direction(double[][] dem, double cellsize){
		
		int y=dem.length;
		int x=dem[1].length;
		int xx=x-2;
		int yy=y-2;
		double noData=-9999;;
		double nodata1=noData+1;
		double nodata2=noData-1;
		int i,j,x1,y1,p;
		block1 blo;
		
		int[][] dir=new int[y][x];
		short[][] dir1=new short[y-2][x-2];
		double[][] dx=new double[y][x];
		double[][] dy=new double[y][x];
		int[][] acc=new int[y][x];
		double[] dinfi;
		double[] newDire;
		double[] res;
		
        Queue<block1> priQ=new PriorityQueue<>(11,zComparator);  //Priority queue
		
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for(i=1;i<=xx;i++){
			for(j=1;j<=yy;j++){
				if(dem[j][i]>nodata1 || dem[j][i]<nodata2){
					acc[j][i]=1;
					dir[j][i]=-10;
					int nn=0;
					int mm=0;
					for(int t=0;t<8;t++){
						if(dem[j+a[t]][i+b[t]]<dem[j][i]+att){
							nn++;
						}
						if(dem[j+a[t]][i+b[t]]>dem[j][i]-att){
							mm++;
						}
					}
					if(mm==8){
						dir[j][i]=-1;
						priQ.add(new block1(i,j,dem[j][i]));
					}else if(nn==8){
						priQ.add(new block1(i,j,dem[j][i]));
						dir[j][i]=-3;
					}
				}else{
					dir[j][i]=-2;
				}
			}
		}
		//System.out.println("Sort finish!"+df.format(System.currentTimeMillis()));
		
		while(priQ.size()!=0){
			blo=priQ.poll();
			x1=blo.getX();
			y1=blo.getY();
			if(dir[y1][x1]!=-1){
				dinfi=dinf(dem,x1,y1);
				newDire=newDir(dem,x1,y1,xx,yy,dinfi,cellsize,nodata1,nodata2);
				res=result(dinfi,newDire,dx[y1][x1],dy[y1][x1]);
				p=(int)res[0];
				dir[y1][x1]=p;
				dx[y1+a[p]][x1+b[p]]=(dx[y1+a[p]][x1+b[p]]*acc[y1+a[p]][x1+b[p]]+res[1]*acc[y1][x1])/(acc[y1+a[p]][x1+b[p]]+acc[y1][x1]);
				dy[y1+a[p]][x1+b[p]]=(dy[y1+a[p]][x1+b[p]]*acc[y1+a[p]][x1+b[p]]+res[2]*acc[y1][x1])/(acc[y1+a[p]][x1+b[p]]+acc[y1][x1]);
				acc[y1+a[p]][x1+b[p]]=acc[y1+a[p]][x1+b[p]]+acc[y1][x1];
			}
			
			for(int t=0;t<8;t++){
				if(dir[y1+a[t]][x1+b[t]]<-5){
					priQ.add(new block1(x1+b[t],y1+a[t],dem[y1+a[t]][x1+b[t]]));
					dir[y1+a[t]][x1+b[t]]=-3;
				}
			}
			
		}
		
		//System.out.println("Calculate direction finish!"+df.format(System.currentTimeMillis()));
		
		for(i=0;i<xx;i++){
			for(j=0;j<yy;j++){
				dir1[j][i]=(short) dir[j+1][i+1];
				if(dir1[j][i]<0)dir1[j][i]=-9999;
			}
		}
		
		return dir1;
		
	}
	
	
	/**
	 * determine the final direction and deviations between the flow position and the downstream cell center
	 */
	public static double[] result(double[] dinfi,double[] newDire,double dx,double dy){
		double[] res=new double[3];
		int p=(int)dinfi[1];
		double dd,abs,dev=0;
		dd=newDire[1];
		if(dd<0.0000000001){
			res[0]=dinfi[1];
			if(p==1 || p==5){
				res[1]=dx;
			}else{
				res[2]=dy;
			}
		}else{
			if(p==1){
				dev=dd*(1+dy)*dinfi[3]+dx;
				abs=Math.abs(dev);
				if(abs<0.5+att){
					res[0]=dinfi[1];
					res[1]=dev;
				}else if(abs<=1){
					res[0]=dinfi[2];
					res[1]=(abs-1)*dinfi[3];
				}else{
					res[0]=dinfi[2];
					res[2]=(abs-1)/Math.tan(newDire[0]);
				}
			}else if(p==5){
				dev=dd*(dy-1)*dinfi[3]+dx;
				abs=Math.abs(dev);
				if(abs<0.5+att){
					res[0]=dinfi[1];
					res[1]=dev;
				}else if(abs<=1){
					res[0]=dinfi[2];
					res[1]=(1-abs)*dinfi[3];
				}else{
					res[0]=dinfi[2];
					res[2]=(1-abs)/Math.tan(newDire[0]);
				}			
			}else if(p==3){
				dev=dd*(1-dx)*dinfi[3]+dy;
				abs=Math.abs(dev);
				if(abs<0.5+att){
					res[0]=dinfi[1];
					res[2]=dev;
				}else if(abs<=1){
					res[0]=dinfi[2];
					res[2]=(abs-1)*dinfi[3];
				}else{
					res[0]=dinfi[2];
					res[1]=(1-abs)/Math.tan(newDire[0]);
				}
			}else{
				dev=0-dd*(1+dx)*dinfi[3]+dy;
				abs=Math.abs(dev);
				if(abs<0.5+att){
					res[0]=dinfi[1];
					res[2]=dev;
				}else if(abs<=1){
					res[0]=dinfi[2];
					res[2]=(1-abs)*dinfi[3];
				}else{
					res[0]=dinfi[2];
					res[1]=(abs-1)/Math.tan(newDire[0]);
				}
			}
		}		
		
		return res;
	}
	
	
	/**
	 * calculate the new infinite direction,here the new direction is equal to the initial direction
	 */
	public static double[] newDir(double[][] dem,int x,int y,int xx,int yy,double[] dinfi,double cellsize,double nodata1,double nodata2){
		double[] nd=new double[2];
		int d=(int)dinfi[2];
		int c=(int)dinfi[1];
		int f,g;
		if(dinfi[3]>0){
			f=d+1;
			g=c-1;
		}else{
			f=d-1;
			g=c+1;
			if(f==-1){
				f=7;
			}else if(f==9){
				f=1;
			}
			if(g==0){
				g=8;
			}else if(g==10){
				g=2;
			}
		}
		double a1=dem[y][x];
		double a2=dem[y+a[c]][x+b[c]];
		double a3=dem[y+a[d]][x+b[d]];
		
		if(a2<nodata1 && a2>nodata2){
			nd[0]=0;
		}else if(a3<nodata1 && a3>nodata2){
			nd[0]=pi4;
		}else{
			double a4=dem[y+a[f]][x+b[f]];
			double a5=dem[y+a[g]][x+b[g]];
			double a6=(a3+a5)/2;
			
			double cc2=Tangential(dem,y+a[c],x+b[c],cellsize, nodata1, nodata2);
			double cc3=Tangential(dem,y+a[d],x+b[d],cellsize, nodata1, nodata2);
			
			double r3,r4;
			
			double r1,r2;
			if(a1>a2+att){
				r1=Math.atan((a2-a3)/(a1-a2));
			}else{
				r1=pi*3/4-Math.atan((a1-a3)/(2*a2-a1-a3));
			}
			if(a1>(a2+a4)/2+att){
				r2=pi4-Math.atan((a4-a2)/(2*a1-a2-a4));
			}else{
				r2=pi*3/4+Math.atan((a2-a4)/(a2+a4-2*a1));
			}
			if(a1>a6+att){
				r3=Math.atan((a6-a3)/(a1-a6));
			}else{
				r3=-pi/2-(a6-a1)/(a3-a6+att);
			}
			
			nd[0]=r1;
			if(cc2>att && cc3>att){
				if(cc2>cc3+att){
					r4=(r1+r3)/2;
						nd[0]=r4;
				}else if(cc2<cc3-att){
					r4=(r1+r2)/2;
						nd[0]=r4;
				}
			}else if(cc2<-att && cc3<-att){
				if(cc2<cc3-att){
					r4=(r1+r2)/2;
						nd[0]=r4;
				}else if(cc2>cc3+att){
					r4=(r1+r3)/2;
						nd[0]=r4;
				}
			}else{
				if(cc2>att){
					r4=(r1+r3)/2;
						nd[0]=r4;
				}else if(cc2<-att){
					r4=(r1+r2)/2;
						nd[0]=r4;
				}
			}
			
			if(a2>a1-att){
				nd[0]=pi4;
			}else if(a3>a1-att){
				nd[0]=0;
			}
			
			if(nd[0]<0){
				nd[0]=0;
			}else if(nd[0]>pi4){
				nd[0]=pi4;
			}
		}
		
		nd[1]=Math.tan(nd[0]);
		return nd;
	}

    /**
     * calculate tangential curvature according the definition of Mitasova and Hofierka [1993]:
     * Interpolation by regularized spline with tension: II. Application to terrain modeling and surface geometry analysis.
     * Math. Geol., 25(6), 657¨C669, doi:10.1007/BF00893172.
     * 3¡Á3 moving window is used to fit a polynomial function to provide variables of curvation definitions according to Evans [1980] method.
     * An Integrated System of Terrain Analysis and Slope Mapping.
     * Zeitschrift fur Gemorphologie, Suppl. Bd., 1980, 36, 274-295.
     */
	public static double Tangential(double[][] dem, int i,int j,double cellsize,double nodata1,double nodata2){
		double z1,z2,z3,z4,z5,z6,z7,z8,z9,fx,fy,fxx,fyy,fxy;
		double Tangential=0;
		if(i<1 || i>=dem.length-1 || j<1 || j>=dem[1].length-1 ){
			Tangential=0;
		}else{
			z1=dem[i-1][j-1];
			z2=dem[i-1][j];
			z3=dem[i-1][j+1];
			z4=dem[i][j-1];
			z5=dem[i][j];
			z6=dem[i][j+1];
			z7=dem[i+1][j-1];
			z8=dem[i+1][j];
			z9=dem[i+1][j+1];
			fx=(z3+z6+z9-z1-z4-z7)/(6*cellsize);
			fy=(z1+z2+z3-z7-z8-z9)/(6*cellsize);
			fxx=(z1+z3+z4+z6+z7+z9)/(6*cellsize*cellsize)-(z2+z5+z8)/(3*cellsize*cellsize);
			fyy=(z1+z2+z3+z7+z8+z9)/(6*cellsize*cellsize)-(z4+z5+z6)/(3*cellsize*cellsize);
			fxy=(z3+z7-z1-z9)/(4*cellsize*cellsize);
			if(fx*fx+fy*fy<0.0000000000001){
				Tangential=0;
			}else{
				Tangential=(fxx*fy*fy-2*fxy*fx*fy+fyy*fx*fx)/((fx*fx+fy*fy)*Math.sqrt(fx*fx+fy*fy+1));
			}
			for(int p=1;p<=8;p++){
				if(dem[i+a[p]][j+b[p]]<nodata1 && dem[i+a[p]][j+b[p]]>nodata2){
					Tangential=0;
				}
			}
		}
		
		return Tangential;
	}
	
	/**
	 * calculate the infinite direction proposed by Tarboton [1997].
	 * @param dem
	 */
	public static double[] dinf(double[][] dem,int x,int y){
		double[] d=new double[4];
		double smax=0;
		double e0=dem[y][x];
		double e1,e2,s,s1,s2;
		double r=0;
		for(int i=1;i<8;i=i+2){
			for(int j=-1;j<2;j=j+2){
				e1=dem[y+a[i]][x+b[i]];
				e2=dem[y+a[i+j]][x+b[i+j]];
				if(e1<e0-att && e2<e0-att){
					s1=e0-e1;
					s2=e1-e2;
					r=Math.atan(s2/s1);
					if(r>pi/4){
						r=pi/4;
						s=(e0-e2)/sqr2;
					}else if(r<0){
						r=0;
						s=s1;
					}else{
						s=Math.pow(s1*s1+s2*s2, 0.5);
					}
				}else if(e1<e0-att){
					r=0;
					s=e0-e1;
				}else if(e2<e0-att){
					r=pi/4;
					s=(e0-e2)/sqr2;
				}else{
					s=-1;
				}
				if(s>smax+att){
					smax=s;
					d[0]=r;
					d[1]=(double)i+0.1;
					d[2]=(double)(i+j)+0.1;
					d[3]=(double)j;
					if(d[2]<0.5)d[2]=8.1;
				}else if(s>smax-att){
					if(r>pi4-att || r<att){
						if(e1>dem[y+a[(int) d[1]]][x+b[(int) d[1]]] || e2>dem[y+a[(int) d[2]]][x+b[(int) d[2]]]){
							d[0]=r;
							d[1]=(double)i+0.1;
							d[2]=(double)(i+j)+0.1;
							d[3]=(double)j;
							if(d[2]<0.5)d[2]=8.1;
						}
					}
				}
			}			
		}
		return d;
	}
	
	//Comparator to sort the cells with elevations from high to low.
    public static Comparator<block1> zComparator = new Comparator<block1>(){
        public int compare(block1 c1, block1 c2) {
        	return  (int) ((c2.getZ() - c1.getZ())*1000000);
        }
    };
    
    /**
	 * change the direction into Esri format
	 */
	public static short directionChange(short a,double noData){
		short b=0;
		if(a<1){
			b=(short)noData;
		}else{
			b=a;
		}
		return b;
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
					if(m==8 && n!=8 && dir[i-1][j-1]>0){
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
			if(d>0 && dem[x1+a[d]][y1+b[d]]>-9000.0){
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
					if(d>0){
						sca[i][j]=tca[i][j]/(length[d]*cellsize);
					}else{
						sca[i][j]=-9999.0;
					}
					
				}
			}
		}
		
		return sca;
	}
	

}
