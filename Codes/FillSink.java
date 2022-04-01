package algorithms;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Remove flats and depressions in the DEM based on a method similar to the theory of Barnes et al. [2014].
 * Reference:
 * Barnes, R., Lehman, C., & Mulla, D.
 * Priority-flood: an optimal depression-filling and watershed-labeling algorithm for digital elevation models
 * Computers & Geosciences, 62, 117-127, 2014
 * @author Pengfei Wu
 * @date 2020.8.21
 */
public class FillSink {
	 public static double[][] fill(double[][] dem, double noData) throws FileNotFoundException{
			
			double nodata1=noData+1;
			double nodata2=noData-1;
			
			int x=dem.length;
			int y=dem[1].length;
			
			int[][] cum=new int[x][y];  //Mark whether a cell is in a depression, 0:untreated, 2:not depression, 3:depression.
			int[][] dep1=new int[x][y];  //The Gradients from high terrain.
			
			int[] a={-1,-1,0,1,1,1,0,-1};
			int[] b={0,1,1,1,0,-1,-1,-1};
			
			
			Queue<block1> priQ=new PriorityQueue<>(11,zComparator);  //Create a priority queue.
			
			//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(int i=0;i<x;i++){  //Cells in the border are nodata.
				cum[i][0]=2;
				cum[i][y-1]=2;
			}
			for(int j=0;j<y;j++){
				cum[0][j]=2;
				cum[x-1][j]=2;
			}
			
			//Add the cell in the border of the valid region into priority queue.
			for(int i=1;i<x-1;i++){
				for(int j=1;j<y-1;j++){
					int o=0;
					
					if(dem[i][j]<nodata1 && dem[i][j]>nodata2){
						cum[i][j]=2;
					}else{
						for(int t=0;t<8;t++){
							if(dem[i+a[t]][j+b[t]]<nodata1 && dem[i+a[t]][j+b[t]]>nodata2){
								o++;
							}
						}
						if(o>0){
							priQ.add(new block1(i,j,dem[i][j]));
							cum[i][j]=2;
						}
					}
				}
			}
			//System.out.println("Sort finish!"+df.format(System.currentTimeMillis()));
			
			//Using a flooding method to find the depression cells, fill them, and giving the gradient towards low terrain.
			//The gradient unit here is 0.001 m.
			while(priQ.size()!=0){
				block1 blo=priQ.poll();
				int p=blo.getX();
				int q=blo.getY();
				for(int t=0;t<8;t++){
					if(dem[p+a[t]][q+b[t]]<dem[p][q]+0.0000001 && cum[p+a[t]][q+b[t]]<1){
						dem[p+a[t]][q+b[t]]=dem[p][q]+0.001;
						cum[p+a[t]][q+b[t]]=3;
						priQ.add(new block1(p+a[t],q+b[t],dem[p+a[t]][q+b[t]]));
						dep1[p+a[t]][q+b[t]]=1;
					}else if(cum[p+a[t]][q+b[t]]<1){
						cum[p+a[t]][q+b[t]]=2;
						priQ.add(new block1(p+a[t],q+b[t],dem[p+a[t]][q+b[t]]));
					}
				}			
			}
			
			return dem;
			
		}
		
		//Comparator to sort the cells with elevations from low to high.
	    public static Comparator<block1> zComparator = new Comparator<block1>(){
	        public int compare(block1 c1, block1 c2) {
	        	return  (int) ((c1.getZ() - c2.getZ())*10000000);
	        }
	    };

	}