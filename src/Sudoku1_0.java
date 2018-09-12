import java.util.*;
import java.io.*;


class Sudoku1_0
{
    /* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For 
     * a standard Sudoku puzzle, SIZE is 3 and N is 9. */
    int SIZE, N;

    /* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
     * not yet been revealed are stored as 0. */
    int Grid[][];


    /* The solve() method should remove all the unknown characters ('x') in the Grid
     * and replace them with the numbers from 1-9 that satisfy the Sudoku puzzle. */

    public boolean check(int x, int y, int k){ //A method to check if k can be insert into the Grid[x][y]
    	int m=x/this.SIZE;
    	int n=y/this.SIZE;
    	for(int i=0;i<this.N;i++){
    		if(this.Grid[x][i]==k){
    			return false;
    		}
    		else if(this.Grid[i][y]==k){
    			return false;
    		}
    		else if(this.Grid[this.SIZE*m+i/this.SIZE][this.SIZE*n+i%this.SIZE]==k){
    			return false;
    		}
    	}
    	return true;
    }
    public boolean backTrace(int x, int y){ //this is the x and y position of the specific one

    	if(y==N){ // plus one so go from the last column of the row to the first column of next row
    		x=x+1;
    		y=0;
    	}
    	if(x==N){ // if x==N means to the all the rows are finished, so everything is good, return a true to the previous
    		return true;
    	}
    	if(this.Grid[x][y]==0){
    		System.out.println(x+ ","+y);
    		//what to do??
    		for(int i=1;i<this.N+1;i++){
    			if(this.check(x,y,i)==true){ // so this can insert
    				this.Grid[x][y]=i; //put it in first
    				boolean n=this.backTrace(x,y+1); //check if it works for further unknowns
    				if(n!=true){
    					this.Grid[x][y]=0; 
    					//so this Grid will have this number for the final if all further numbers returns true;
    					//else this will stay 0;
    				}
    			}
    		}
    		if(this.Grid[x][y]==0){
    			return false;
    			//if this Grid remains zero that means the all the possibilities are not ok, tell the previous that number is incorrect
    		}
    	}
    	else{
    		return (this.backTrace(x,y+1)); // if it has already a number in it, don't do anything, just return the next one whatever that is. 
    	}
    	return true;//until here means everything is ok, return true to tell the previous one it is ok to have that number.
    }
    public void solve()
    {
    	
        this.backTrace(0,0);
        
    }


    /*****************************************************************************/
    /* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE FUNCTIONS BELOW THIS LINE. */
    /*****************************************************************************/
 
    /* Default constructor.  This will initialize all positions to the default 0
     * value.  Use the read() function to load the Sudoku puzzle from a file or
     * the standard input. */
    public Sudoku1_0( int size )
    {
        SIZE = size;
        N = size*size;

        Grid = new int[N][N];
        for( int i = 0; i < N; i++ ) 
            for( int j = 0; j < N; j++ ) 
                Grid[i][j] = 0;
    }


    /* readInteger is a helper function for the reading of the input file.  It reads
     * words until it finds one that represents an integer. For convenience, it will also
     * recognize the string "x" as equivalent to "0". */
    static int readInteger( InputStream in ) throws Exception
    {
        int result = 0;
        boolean success = false;

        while( !success ) {
            String word = readWord( in );

            try {
                result = Integer.parseInt( word );
                success = true;
            } catch( Exception e ) {
                // Convert 'x' words into 0's
                if( word.compareTo("x") == 0 ) {
                    result = 0;
                    success = true;
                }
                // Ignore all other words that are not integers
            }
        }

        return result;
    }


    /* readWord is a helper function that reads a word separated by white space. */
    static String readWord( InputStream in ) throws Exception
    {
        StringBuffer result = new StringBuffer();
        int currentChar = in.read();
	String whiteSpace = " \t\r\n";
        // Ignore any leading white space
        while( whiteSpace.indexOf(currentChar) > -1 ) {
            currentChar = in.read();
        }

        // Read all characters until you reach white space
        while( whiteSpace.indexOf(currentChar) == -1 ) {
            result.append( (char) currentChar );
            currentChar = in.read();
        }
        return result.toString();
    }


    /* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
     * grid is filled in one row at at time, from left to right.  All non-valid
     * characters are ignored by this function and may be used in the Sudoku file
     * to increase its legibility. */
    public void read( InputStream in ) throws Exception
    {
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                Grid[i][j] = readInteger( in );
            }
        }
    }


    /* Helper function for the printing of Sudoku puzzle.  This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    void printFixedWidth( String text, int width )
    {
        for( int i = 0; i < width - text.length(); i++ )
            System.out.print( " " );
        System.out.print( text );
    }


    /* The print() function outputs the Sudoku grid to the standard output, using
     * a bit of extra formatting to make the result clearly readable. */
    public void print()
    {
        // Compute the number of digits necessary to print out each number in the Sudoku puzzle
        int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

        // Create a dashed line to separate the boxes 
        int lineLength = (digits + 1) * N + 2 * SIZE - 3;
        StringBuffer line = new StringBuffer();
        for( int lineInit = 0; lineInit < lineLength; lineInit++ )
            line.append('-');

        // Go through the Grid, printing out its values separated by spaces
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                printFixedWidth( String.valueOf( Grid[i][j] ), digits );
                // Print the vertical lines between boxes 
                if( (j < N-1) && ((j+1) % SIZE == 0) )
                    System.out.print( " |" );
                System.out.print( " " );
            }
            System.out.println();

            // Print the horizontal line between boxes
            if( (i < N-1) && ((i+1) % SIZE == 0) )
                System.out.println( line.toString() );
        }
    }


    /* The main function reads in a Sudoku puzzle from the standard input, 
     * unless a file name is provided as a run-time argument, in which case the
     * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
     * outputs the completed puzzle to the standard output. */
    public static void main( String args[] ) throws Exception
    {
        InputStream in;
        if( args.length > 0 ) 
            in = new FileInputStream( args[0] );
        else
            in = System.in;

        // The first number in all Sudoku files must represent the size of the puzzle.  See
        // the example files for the file format.
        int puzzleSize = readInteger( in );
        if( puzzleSize > 100 || puzzleSize < 1 ) {
            System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
            System.exit(-1);
        }

        Sudoku1_0 s = new Sudoku1_0( puzzleSize );

        // read the rest of the Sudoku puzzle
        s.read( in );
        //System.out.println(s.Grid[6][3]);

        // Solve the puzzle.  We don't currently check to verify that the puzzle can be
        // successfully completed.  You may add that check if you want to, but it is not
        // necessary.
        long startTime = System.currentTimeMillis();
        s.solve();
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("It takes "+totalTime+" nanoseconds");

        // Print out the (hopefully completed!) puzzle
        s.print();
    }
}

