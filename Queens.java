import java.io.*;
import java.util.*;
import java.math.*;
public class Queens {

	static int QueenPosArray[];
	static int NumberOfQueens;
	static char QueenMatrix[][];
	static int HCostMatrix[][];
	static int NumberOfRestarts=0;
	static int CurrentHCost;
	static int NumberOfStepsOfClimb=0;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Welcome to N Queens Problem!");
		System.out.println("This program can find the solution upto 70 queens within 3 mins 30 secs.");
		System.out.println("For queens greater than 70 it takes much longer.");
		System.out.println("Please enter the number of queens:");
		NumberOfQueens=Integer.parseInt(br.readLine());
		QueenPosArray=new int[NumberOfQueens];
		QueenMatrix=new char[NumberOfQueens][NumberOfQueens];
		HCostMatrix=new int[NumberOfQueens][NumberOfQueens];
		generateRandom();
		System.out.println("Queen Positions are: ");
		for(int i=0;i<NumberOfQueens;i++){
			System.out.print(QueenPosArray[i]+" ");
		}
		makeQueenMatrix(QueenPosArray);
		System.out.println("\nCurrent Board is as follows:");
		for(int i=0;i<NumberOfQueens;i++){
			for(int j=0;j<NumberOfQueens;j++){
				System.out.print(QueenMatrix[i][j]+" ");				
			}
			System.out.println();
		}
		chooseNeighbour();
	}

	private static void chooseNeighbour() {
		//int NextNeighbour[]=new int[NumberOfQueens];
		for(int i=0;i<NumberOfQueens;i++){
			for(int j=0;j<NumberOfQueens;j++){
				HCostMatrix[j][i]=findHcost(i,j,QueenPosArray);//Generates Heuristic values for each position in the HCostMatrix
			}
		}
		//Check for solution or next best node.
		checkForSolution();
		
	}

	private static void checkForSolution() {
		int minrow=0;
		int mincol=0;
		int solfound=0;
		int MinCostLocation[]=new int[3];
		MinCostLocation=findMinCostLocation();//Finds the row, col and value of least value in HCostMatrix.
		for(int i=0;i<NumberOfQueens;i++){
			for(int j=0;j<NumberOfQueens;j++){
				if(HCostMatrix[i][j]==0){//Checks if goal state is generated
					minrow=i;
					mincol=j;
					solfound=1;
					QueenPosArray[mincol]=minrow;//Changes the queen position to goal position.
					System.out.println("Solution is found:");
					System.out.println("Number of Restarts: "+NumberOfRestarts);
					System.out.println("Number of Steps of Climbing: "+NumberOfStepsOfClimb);
					makeQueenMatrix(QueenPosArray);
					for(int k=0;k<NumberOfQueens;k++){
						for(int l=0;l<NumberOfQueens;l++){
							System.out.print(QueenMatrix[k][l]+" ");							
						}
						System.out.println();
					}
				}
			}
		}
		if(solfound==0){//If no solution is found			
			if(MinCostLocation[2]<CurrentHCost){//Compares lowest cost in HCostMatrix to CurrentHCost
				QueenPosArray[MinCostLocation[1]]=MinCostLocation[0];//Updates the QueenPosArray with new location
				CurrentHCost=MinCostLocation[2];//Updates CurrentHCost to  new H cost. 
				NumberOfStepsOfClimb++;//Increases the steps climbed.
				chooseNeighbour();//Generates the neighbours again.
			}
			else if(CurrentHCost<=MinCostLocation[2]){//If lowest cost in HCostMatrix is greater than or equal to current cost
				generateRandom();//Performs random restart
				NumberOfRestarts++;
				chooseNeighbour();//Initiates neighbour generation for randomly restarted node.
			}			
		}
		else if(solfound==1){
			System.out.println("End of program");	
		}
	}
	
		
	

	private static int[] findMinCostLocation() {
		int minhcost=HCostMatrix[0][0];
		int rowpos=0;
		int colpos=0;
		int minloc[]=new int[3];
		//Finds the lowest cost in the matrix
		for(int i=0;i<NumberOfQueens;i++){
			for(int j=0;j<NumberOfQueens;j++){
				if(HCostMatrix[i][j]<minhcost){
					minhcost=HCostMatrix[i][j];
					rowpos=i;
					colpos=j;
				}
			}
		}
		minloc[0]=rowpos;//Stores position of row where lowest cost is found
		minloc[1]=colpos;//Stores position of col where lowest cost if found
		minloc[2]=minhcost;//Stores min cost value
		return minloc;//returns the minloc array contating above details
	}

	private static int findHcost(int col, int row, int[] queenPosArray2) {
		int temp_var1=0;
		int paircount=0;
		temp_var1=queenPosArray2[col];
		queenPosArray2[col]=row;//Changes position of Q to row.		
		for(int i=0;i<NumberOfQueens;i++){
			for(int j=0;j<NumberOfQueens;j++){
				//Checks for horizontal and diagonal Q if Q is found it increments number of pairs
				if(queenPosArray2[i]==queenPosArray2[j] || Math.abs(i-j)==Math.abs(queenPosArray2[i]-queenPosArray2[j])){
					paircount++;
				}
			}
		}
		queenPosArray2[col]=temp_var1;//returns Q to its original position 
		paircount=(paircount-NumberOfQueens)/2;//finds the number of attacking pairs
		
		return paircount;
	}

	private static void makeQueenMatrix(int[] queenPosArray2) {
		for(int i=0;i<NumberOfQueens;i++){
			for(int j=0;j<NumberOfQueens;j++){
				QueenMatrix[i][j]='*';//Places '*' symbol in all locations of array
			}
		}
		for(int i=0;i<NumberOfQueens;i++){
			for(int j=0;j<NumberOfQueens;j++){
				if(j==queenPosArray2[i]){//Finds the row of Q in each column
					QueenMatrix[j][i]='Q';//Replaces that position with a 'Q' symbol
					break;
				}
			}
		}
		
	}

	static void generateRandom() {
		Random RandomNum=new Random();
		for(int i=0;i<NumberOfQueens;i++){
			QueenPosArray[i]=RandomNum.nextInt(NumberOfQueens);//Generates random row positions for each queen in every column
		}
		findInitialheuristic(QueenPosArray);//Finds the initial heuristic cost for newly randomized QueenPosArray
	}

	private static void findInitialheuristic(int[] queenPosArray1) {
		for(int i=0;i<NumberOfQueens;i++){
			for(int j=0;j<NumberOfQueens;j++){
				//Checks the horizontal and diagonal positions of each Q if Q is found increments CurrentHCost.
				if(queenPosArray1[i]==queenPosArray1[j] || Math.abs(i-j)==Math.abs(queenPosArray1[i]-queenPosArray1[j])){
					CurrentHCost++;
				}
			}
		}
		CurrentHCost=(CurrentHCost-NumberOfQueens)/2; //Finds the number of attacking pairs.		
	}

}
