package algorithms;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class iTFM_VS {
	//Code of locations of cells.
	//    8   1   2
	//    7   -1  3
	//    6   5   4
	//The cell 8 is also coded as 0, the cell 1 is also coded as 9.
	private static int[] a={-1,-1,-1,0,1,1,1,0,-1,-1};
	private static int[] b={-1,0,1,1,1,0,-1,-1,-1,0};
	private static int[] neighbor={5,4,7,6,1,8,3,2,5,4};
	private static double att=0.0000001;
	private static double sqr2=Math.sqrt(2.0);
	private static double pi=Math.PI;
	private static double pi2=Math.PI/2.0;
	private static double pi4=Math.PI/4.0;
	private static double pi45=Math.PI*1.25;
	private static double pi43=Math.PI*0.75;
	private static double pi23=Math.PI*1.5;
	
	public static double[][] TCA(double[][] dem, double cellsize){
		
		int x=dem.length-2;
		int y=dem[1].length-2;
		boolean[][] inQueue=new boolean[x+2][y+2];
		double[][] tca=new double[x][y];
		Queue<block1> priQ=new PriorityQueue<>(11,zComparator);  //Priority queue to sort the cells from high to low
		block1 blo;
		double area=cellsize*cellsize;
		double[] r=new double[10];
		double[] p=new double[9];
		int[] p1=new int[10];
		int[] p2=new int[10];
		double[][][] amount=new double[x+2][y+2][9];
		double[] diagonal=new double[9];
		double[] cardinal=new double[9];
		double[] center=new double[9];
		double[][] facet=new double[9][2];
		double[] cell=new double[9];
		double[] facet_out=new double[9];
		//double[] diagonal_final=new double[9];
		//double[] center_final=new double[9];
		boolean[] flow_out=new boolean[9];
		boolean[] treat=new boolean[9];
		int[] hasUp=new int[9];
		int[][] down_id=new int[2][9];
		int m,n;
		int xx,yy;
		int pp0,pp1,pp2,n1,n2;
		int num,out_num;
		double x1,x2,x3,y1,y2,y3,z1,z2,z3;
		double r1;
		boolean border;
		double fx,fy;
		double center_flow,total_out;
		
		double[][] data=new double[x][y];

		//Define the initial values.
		for(int i=0;i<=x+1;i++){
			inQueue[i][0]=true;
			inQueue[i][y+1]=true;
			for(int t=0;t<9;t++){
				amount[i][0][t]=0;
				amount[i][y+1][t]=0;
			}
		}
		for(int j=0;j<=y+1;j++){
			inQueue[0][j]=true;
			inQueue[x+1][j]=true;
			for(int t=0;t<9;t++){
				amount[0][j][t]=0;
				amount[x+1][j][t]=0;
			}
		}

		//The DEM cells are added into the queue one by one to avoid computation costs of sorting.
		for(int i=1;i<=x;i++){
			for(int j=1;j<=y;j++){
				data[i-1][j-1]=-9999;
				amount[i][j][0]=0;
				for(int t=1;t<9;t++){
					amount[i][j][t]=area/8.0;
				}
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
		
		x1=0.0;
		y1=0.0;
		x2=0.0;
		y2=1.0;
		x3=1.0;
		y3=1.0;

		//Distribute the flow
		while(priQ.size()!=0){
			blo=priQ.poll();
			xx=blo.getX();
			yy=blo.getY();
			
			border=false;
			for(int t=1;t<9;t++){
				diagonal[t]=0.0;
				cardinal[t]=0.0;
				center[t]=0.0;
				facet[t][0]=0.0;
				facet[t][1]=0.0;
				cell[t]=0.0;
				//diagonal_final[t]=0;
				//center_final[t]=0;
				if(dem[xx+a[t]][yy+b[t]]<-9000.0){
					border=true;
				}
			}

			//Calculate the aspect of facets
			for(int t=1;t<9;t++){
				p[t]=amount[xx][yy][t];
				treat[t]=false;
				hasUp[t]=0;
				z1=dem[xx][yy];
				if(t%2==1){
					p1[t]=t;
					p2[t]=t+1;
				}else{
					p1[t]=t+1;
					p2[t]=t;
				}
				z2=dem[xx+a[p1[t]]][yy+b[p1[t]]];
				z3=dem[xx+a[p2[t]]][yy+b[p2[t]]];
				fx=((y1-y3)*(z1-z2)-(y1-y2)*(z1-z3))/((x1-x2)*(y1-y3)-(x1-x3)*(y1-y2));
				fy=((x1-x2)*(z1-z3)-(x1-x3)*(z1-z2))/((x1-x2)*(y1-y3)-(x1-x3)*(y1-y2));
				if(Math.abs(fx)<att){
					if(fy>-att){
						r[t]=pi;;
					}else{
						r[t]=0.0;
					}
				}else{
					r[t]=pi-Math.atan(fy/fx)+pi2*fx/Math.abs(fx);
				}
			}
			
			r[0]=r[8];
			r[9]=r[1];
			
			//data[xx][yy]=r[1];

			//Calculate the distribution proportion to every targets of every facet.
			for(int t=1;t<9;t++){
				if(p1[t]<p2[t]){
					pp1=t+1;
					pp2=t-1;
					if(pp1==9)pp1=1;
					if(pp2==0)pp2=8;
				}else{
					pp1=t-1;
					pp2=t+1;
					if(pp1==0)pp1=8;
					if(pp2==9)pp2=1;
				}
				if(r[t]<pi4+att){
					cell[t]=1.0;
					//if(t==1 && yy==751 && xx<700)System.out.println(r[t]+"\t"+cell[t]);
				}else if(r[t]>=pi4+att && r[t]<=pi2-att){
					if(r[pp1]<pi4+att || r[pp1]>pi45-att){
						//facet[t][0]=(0.125-0.125/Math.tan(r[t]))/0.125;
						facet[t][0]=(Math.tan(r[t])-1.0)/(Math.tan(r[t])-1.0+sqr2);
						if(facet[t][0]<0.0){
							facet[t][0]=0.0;
						}else if(facet[t][0]>1.0){
							facet[t][0]=1.0;
						}
						cell[t]=1.0-facet[t][0];
						hasUp[pp1]++;
					}else{
						//diagonal[t]=(0.125-0.125/Math.tan(r[t]))/0.125;
						diagonal[t]=(Math.tan(r[t])-1.0)/(Math.tan(r[t])-1.0+sqr2);
						if(diagonal[t]<0.0){
							diagonal[t]=0.0;
						}else if(diagonal[t]>1.0){
							diagonal[t]=1.0;
						}
						cell[t]=1.0-diagonal[t];
					}
				}else if(r[t]>pi2-att && r[t]<pi+att){
					if(r[pp1]<pi4+att || r[pp1]>pi45-att){
						facet[t][0]=1.0;
						hasUp[pp1]++;
					}else{
						if(dem[xx+a[p2[t]]][yy+b[p2[t]]]<dem[xx][yy]-att){
							diagonal[t]=1.0;
						}else{
							center[t]=1.0;
						}
					}
				}else if(r[t]>=pi+att && r[t]<=pi45-att){
					r1=r[t]-pi;
					if(r[pp1]<pi4+att || r[pp1]>pi45-att){
						//facet[t][0]=1.0-Math.tan(r1);
						facet[t][0]=1.0/(sqr2*Math.tan(r1)/(1.0-Math.tan(r1))+1.0);
						hasUp[pp1]++;
					}else{
						//center[t]=1.0-Math.tan(r1);
						center[t]=center[t]+1.0/(sqr2*Math.tan(r1)/(1.0-Math.tan(r1))+1.0);
					}
					if(r[pp2]<pi+att){
						//facet[t][1]=Math.tan(r1);
						facet[t][1]=(sqr2*Math.tan(r1)/(1.0-Math.tan(r1)))/(sqr2*Math.tan(r1)/(1.0-Math.tan(r1))+1.0);
						hasUp[pp2]++;
					}else{
						//center[t]=Math.tan(r1);
						center[t]=center[t]+(sqr2*Math.tan(r1)/(1.0-Math.tan(r1)))/(sqr2*Math.tan(r1)/(1.0-Math.tan(r1))+1.0);
					}
				}else if(r[t]>pi45-att && r[t]<pi23+att){
					if(r[pp2]<pi+att){
						facet[t][1]=1.0;
						hasUp[pp2]++;
					}else{
						center[t]=1.0;
					}
				}else{
					r1=pi*2.0-r[t];
					if(r[pp2]<pi+att){
						//facet[t][1]=Math.tan(r1)/(1.0+Math.tan(r1));
						facet[t][1]=Math.tan(r1)/(1.0+Math.tan(r1));
						hasUp[pp2]++;
					}else{
						//cardinal[t]=Math.tan(r1)/(1.0+Math.tan(r1));
						cardinal[t]=Math.tan(r1)/(1.0+Math.tan(r1));
					}
					cell[t]=1.0-Math.tan(r1)/(1.0+Math.tan(r1));
				}
			}
			data[xx-1][yy-1]=facet[2][0];
			
			num=1;
			center_flow=amount[xx][yy][0];
			total_out=0;

			//Distribute the flow.
			while(num>0 && border==false){
				num=0;
				for(int t=1;t<9;t++){
					if(hasUp[t]==0 && treat[t]==false){
						treat[t]=true;
						pp0=p1[t];
						pp1=p2[t];
						if(pp0==9)pp0=1;
						if(p1[t]<p2[t]){
							pp2=pp0-1;
							if(pp2==0)pp2=8;
							n1=t+1;
							n2=t-1;
							if(n2==0)n2=8;
						}else{
							pp2=pp0+1;
							n1=t-1;
							n2=t+1;
							if(n2==9)n2=1;
						}
						if(cell[t]>att){
							tca[xx-1+a[pp0]][yy-1+b[pp0]]=tca[xx-1+a[pp0]][yy-1+b[pp0]]+p[t]*cell[t];
							amount[xx+a[pp0]][yy+b[pp0]][neighbor[t]]=amount[xx+a[pp0]][yy+b[pp0]][neighbor[t]]+p[t]*cell[t];
													
						}
						if(cardinal[t]>att){
							tca[xx-1+a[pp0]][yy-1+b[pp0]]=tca[xx-1+a[pp0]][yy-1+b[pp0]]+p[t]*cardinal[t];
							amount[xx+a[pp0]][yy+b[pp0]][0]=amount[xx+a[pp0]][yy+b[pp0]][0]+p[t]*cardinal[t];
							
						}
						if(diagonal[t]>att){
							tca[xx-1+a[pp1]][yy-1+b[pp1]]=tca[xx-1+a[pp1]][yy-1+b[pp1]]+p[t]*diagonal[t];
							amount[xx+a[pp1]][yy+b[pp1]][0]=amount[xx+a[pp1]][yy+b[pp1]][0]+p[t]*diagonal[t];
						}
						if(facet[t][0]>att){
							p[n1]=p[n1]+p[t]*facet[t][0];
							hasUp[n1]--;
							num++;
						}
						if(facet[t][1]>att){
							p[n2]=p[n2]+p[t]*facet[t][1];
							hasUp[n2]--;
							num++;
						}
						if(center[t]>att){
							center_flow=center_flow+p[t]*center[t];
						}
						if(cell[t]>att || cardinal[t]>att || diagonal[t]>att){
							total_out=total_out+p[t]*(cell[t]+cardinal[t]+diagonal[t]);
							facet_out[t]=p[t]*(cell[t]+cardinal[t]+diagonal[t]);
						}else{
							facet_out[t]=0.0;
						}
					}
				}
			}
			
			if(center_flow>att && border==false){
				
				for(int t=1;t<9;t++){
					if(facet_out[t]>att){
						pp0=p1[t];
						pp1=p2[t];
						if(pp0==9)pp0=1;
						if(cell[t]>att){
							tca[xx-1+a[pp0]][yy-1+b[pp0]]=tca[xx-1+a[pp0]][yy-1+b[pp0]]+(center_flow*facet_out[t]/total_out)*(cell[t]/(cell[t]+cardinal[t]+diagonal[t]));
							amount[xx+a[pp0]][yy+b[pp0]][neighbor[t]]=amount[xx+a[pp0]][yy+b[pp0]][neighbor[t]]+(center_flow*facet_out[t]/total_out)*(cell[t]/(cell[t]+cardinal[t]+diagonal[t]));
						}
						if(cardinal[t]>att){
							tca[xx-1+a[pp0]][yy-1+b[pp0]]=tca[xx-1+a[pp0]][yy-1+b[pp0]]+(center_flow*facet_out[t]/total_out)*(cardinal[t]/(cell[t]+cardinal[t]+diagonal[t]));
							amount[xx+a[pp0]][yy+b[pp0]][0]=amount[xx+a[pp0]][yy+b[pp0]][0]+(center_flow*facet_out[t]/total_out)*(cardinal[t]/(cell[t]+cardinal[t]+diagonal[t]));
						}
						if(diagonal[t]>att){
							tca[xx-1+a[pp1]][yy-1+b[pp1]]=tca[xx-1+a[pp1]][yy-1+b[pp1]]+(center_flow*facet_out[t]/total_out)*(diagonal[t]/(cell[t]+cardinal[t]+diagonal[t]));
							amount[xx+a[pp1]][yy+b[pp1]][0]=amount[xx+a[pp1]][yy+b[pp1]][0]+(center_flow*facet_out[t]/total_out)*(diagonal[t]/(cell[t]+cardinal[t]+diagonal[t]));
						}
					}
				}
			}
			
			for(int t=1;t<9;t++){
				if(inQueue[xx+a[t]][yy+b[t]]==false){
					priQ.add(new block1(xx+a[t],yy+b[t],dem[xx+a[t]][yy+b[t]]));
					inQueue[xx+a[t]][yy+b[t]]=true;
				}
			}
		}
		
		return tca;
		//return data;
		
	}
	
	//Comparator to sort the cells with elevations from high to low.
    public static Comparator<block1> zComparator = new Comparator<block1>(){
        public int compare(block1 c1, block1 c2) {
        	return  (int) ((c2.getZ() - c1.getZ())*1000000);
        }
    };

}
