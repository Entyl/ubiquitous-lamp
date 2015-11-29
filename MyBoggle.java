import java.util.*;
import java.io.*;
import java.text.*;

public class MyBoggle
{
	static char[][] board;	
	static boolean[][] checker;
	static DictionaryInterface dictionary;
	static DictionaryInterface foundWord;
	static HashSet<String> validWord = new HashSet<String>();	//Holds valid words because DictionaryInterface does not have get method or addAll
	static StringBuilder currentWord;
	
	public static void main( String args[] ) throws IOException
	{	
		//INTERPRETTING COMMAND LINE ARGUMENTS, READING FILES, INITIALIZING DATA STRUCTURES
		board = createBoard(args);	//create board
		dictionary = createImplementation(args);	//the dictionary that well be checked against	
		foundWord = createImplementation(args);	//creates a second implementation to hold the words found
		
		printBoard(board);	//prints board
		System.out.println("Enter as many words as you can find in the board: (-1 to stop entering words)");	//prompt and then allows the algorithm to complete without it looking like lag
		
		//READ WORDS FROM DICTIONARY
		Scanner file = new Scanner(new FileInputStream("dictionary.txt"));	//scanner for dictionary.txt
		String word;	//string that will hold word to be added to dictionary
		while(file.hasNext())
		{
			word = file.nextLine();
			dictionary.add( word.toUpperCase() );	//converts to upper case for easier search in algorithm
		}
		file.close();	//closes the file
		
		//ALGORITHM FOR VALID WORDS ON CURRENT BOARD(IT CONTINUES IN wordSearch METHOD)
		for(int row = 0; row < 4; row++)
		{
			for(int col = 0; col < 4; col++)
			{
				checker = createBoard();	//create and recreate checker board full of falses
				currentWord = new StringBuilder();	//clears the string builder
				wordSearch(row, col);	//go through every starting tile to find words

			}
		}
		
		//PLAYER NOW MAKES THEIR WORD CHOICES
		Scanner input = new Scanner(System.in);	//scanner for input from player
		StringBuilder inputWord;	//word that the user inputs
		HashSet<String> userInput = new HashSet<String>();	//where user input will be stored
		while(input.hasNext())	//loops until player quits
		{
			inputWord = new StringBuilder( input.next().toLowerCase() );	//case in-sensitivity for the player also
			int onBoard = foundWord.search( inputWord );	//searches foundWord if word is there, changes it to lower case incase user enters capitals
			
			if(inputWord.toString().equals("-1") )	//-1 means to quit
			{
				System.out.println();	//new line
				break;
			}
			
			switch (onBoard)	//switch case to check if the word is in dictionary
			{
				case 0: 
				System.out.println("was not found");
				break;
				
				case 1:
				System.out.println("is a prefix");
				break;
				
				case 2:	
				System.out.println("is a word");
				userInput.add( inputWord.toString() );
				break;
				
				case 3:	
				System.out.println("is a word and prefix");
				userInput.add( inputWord.toString() );
				break;	//just for uniformity
			}
		
		}
		
		printStats( userInput );	//prints the of the board and how the player did
		
		return;
	}
	
	public static void wordSearch(int row, int col)	//algorithm that recursively looks for words for foundWord
	{
		if(board[row][col] == '*')	//wild card handling
		{
			char wild = 'A';
			
			while(wild <= 'Z')
			{
				board[row][col] = wild;
				wordSearch(row, col);
				wild++;				
			}
			
			board[row][col] = '*';	//reset wild card
			return; //return to last tile
		}
		
		currentWord.append( board[row][col] );	//append the character to currentWord
		checker[row][col] = true;	//current tile is now in use
		int inDictionary = dictionary.search(currentWord);	//searches to see if it is in dictionary
		
		switch (inDictionary)
		{
			case 0: //currentWord was not found, therefore do nothing and return
			checker[row][col] = false;
			currentWord.deleteCharAt( currentWord.length() - 1);
			return;
			
			case 1:	//currentWord is a prefix
			break;
			
			case 2:	//currentWord is a word
			if(currentWord.length() > 2)
			{
				foundWord.add( currentWord.toString().toLowerCase() );	//adds string to foundWord
				validWord.add( currentWord.toString().toLowerCase() );
			}
			checker[row][col] = false;
			currentWord.deleteCharAt( currentWord.length() - 1);
			return;
			
			case 3:	//currentWord is both a word and prefix
			if(currentWord.length() > 2)
			{
				foundWord.add( currentWord.toString().toLowerCase() );	//adds string to foundWord
				validWord.add( currentWord.toString().toLowerCase() );
			}
			break;	//just for uniformity
		}

		if(col != 3 && row != 0 && !checker[row-1][col+1] )
		{
			wordSearch( row - 1, col + 1);	//move one tile diagonally right and up
		}
		
		if(col != 3 &&  !checker[row][col+1] )
		{
			wordSearch( row, col + 1);	//move one tile right
		}
			
		if(row != 3 && col != 3 &&  !checker[row+1][col+1] )
		{
			wordSearch( row + 1, col + 1);	//move one tile diagonally right and down
		}
			
		if(row != 3 &&  !checker[row+1][col] )
		{
			wordSearch( row + 1, col);	//move one tile down
		}
			
		if(row != 3 && col != 0 &&  !checker[row+1][col-1] )
		{
			wordSearch( row + 1, col - 1);	//move one tile diagonally left and down
		}
			
		if(col != 0 &&  !checker[row][col-1] )
		{
			wordSearch( row, col - 1);	//move one tile left
		}
			
		if(row != 0 && col != 0 &&  !checker[row-1][col-1] )
		{
			wordSearch( row - 1, col -1);	//moves one tile diagonally left and up
		}
			
		if(row != 0 &&  !checker[row-1][col] )
		{
			wordSearch( row - 1, col);	//move one tile up
		}
		
		checker[row][col] = false;
		currentWord.deleteCharAt( currentWord.length() - 1);
		return;	//none of the tiles were possible
	}
	
	public static void printStats( HashSet<String> userInput )	//removes duplicates, prints words, and percentages
	{
		ArrayList<String> printValid = new ArrayList<String>();
		ArrayList<String> printUser = new ArrayList<String>();
		
		//preprocessing of valid words
		printValid.addAll( validWord );	
		Collections.sort( printValid );	
		int totalWords = printValid.size();
		
		System.out.println( "There are " + totalWords + " valid words:" );	//number of valid words
		
		for(int i = 0; i < totalWords; i++)	//print
		{
			System.out.println( printValid.get(i) );
		}
		
		//preprocessing of user's input
		printUser.addAll( userInput );	
		Collections.sort( printUser );
		int userWords = printUser.size();
		
		double percentage = (double) userWords / totalWords * 100;
		System.out.println( "\nYou entered " + userWords + " valid words (" + percentage + "% of total): " );	//number of user valid words
		
		for(int i = 0; i < userWords; i++)	//print
		{
			System.out.println( printUser.get(i) );
		}
		
		//PRINT STATEMENT COMMENTED OUT, UNCOMMENT TO SEE TOTAL AMOUNT OF WORDS IF COMMANDLINE DOES NOT SCROLL UP ENOUGH
		//System.out.println(totalWords);
		
		return;
	}
	
	public static char[][] createBoard(String args[]) throws IOException	//creates the board
	{
		char[][] board = new char[4][4];
		int row = 0, col = 0;

		BufferedReader boardFile = new BufferedReader( new FileReader( getArgs(args, "-b") ) );	//makes boardFile from getFile()
		while ( boardFile.ready() && row < 4)
		{
			board[row][col] = (char)boardFile.read();	//reads in all 16 characters into board
			col++;
			
			if(col == 4)	//goes to the next row
			{
				col = 0;
				row++;
			}
		}
		boardFile.close();
		
		return board;
	}
	
	public static boolean[][] createBoard() throws IOException //creates a boolean checker board
	{
		boolean[][] board = new boolean[4][4];
		
		for(int row = 0; row < 4; row ++)
		{
			for(int col = 0; col < 4; col++)
			{
				board[row][col] = false;
			}
		}
		
		return board;
	}
	
	public static DictionaryInterface createImplementation(String args[])	//creates the dictionary from implementation 
	{
		String check = getArgs(args, "-d");	//checks to see which interface is wanted
		
		if(check == "simple")
		{
			DictionaryInterface implementation = new SimpleDictionary();
			return implementation;
		}
		
		else
		{
			DictionaryInterface implementation = new DLB();
			return implementation;
		}

	}
	
	public static String getArgs(String args[], String check)
	{
		
		check = check.toLowerCase();
	
		if(args.length < 3)	//means that default simple interface because only 2 arguments
		{
			if(check.equals("-b"))
			{
				return args[1];	//returns the board#.txt if the function was passed "-b" from command line
			}
			
			else if(check.equals("-d"))
			{
				return "simple";
			}
		}
		
		else
		{
			if(args[0].equals(check) )	//checks to see if looking for file or dictionary type
			{						
				return args[1];		//then returns the argument after it
			}
			
			else if(args[2].equals(check) )
			{
				return args[3];
			}	
		}
		
		System.out.println("Error(s) in command line arguments.");
		System.exit(0);	//end program early if arguments are incorrect
		
		return "ERROR";
	}
	
	public static void printBoard(char board[][])	//prints the board with some formatting
	{
		for(int row = 0; row<4; row++)
		{
			System.out.println("---------");
			for(int col = 0; col<4; col++)
			{
				System.out.print("|");
				System.out.print(board[row][col]);
			}
			System.out.print("|");
			System.out.println();	//prints line for next row
		}
		System.out.println("---------");
		
		return;
	}
	
}