import java.util.*;

public class DLB implements DictionaryInterface 
{

	private Node root;	//root node, holds no data
	private Node currentNode;	//private iterator

	public class Node
	{
		private char value;	//current char value
		private Node peer = null;	//peer nodes, left to right
		private Node child = null;	//next letter in word, goes down
		private boolean isAWord = false;	//is true if the current node is the end of a word
	}
	
	public DLB() //constructor
	{
		root = new Node();
	}

	public boolean add(String s)
	{
		currentNode = root;
		int index = 0;
		
		for(int i=0; i<s.length(); i++)
		{
			char currentChar = s.charAt(i);
			if(currentNode.child != null )	//there is a current child node to the currentNode, go down 1 level
			{
				currentNode = currentNode.child;
			}
			
			else	//there is no current child node, create one
			{
				Node temp = new Node();
				temp.value = currentChar;
				currentNode.child = temp;
				currentNode = currentNode.child;
			}
		
			while(currentNode.value != currentChar)	//gets to end of peer links or will end when currentNode.value == currentChar
			{
				if(currentNode.peer != null)	//means that the line of peers is longer, make currentNode the next peer
				{
					currentNode = currentNode.peer;
				}
				
				else
				{
					Node temp = new Node();
					temp.value = currentChar;
					currentNode.peer = temp;
					currentNode = currentNode.peer;
				}
			}
		}
		
		currentNode.isAWord = true;
		
		return true;
	}
	
	public int search(StringBuilder s) 
	{
		currentNode = root;	//currentNode is now root
		
		for(int i = 0; i < s.length(); i++ )
		{
			char currentChar = s.charAt(i);	//gets current char
			if(currentNode.child != null)
			{
				currentNode = currentNode.child;
			}
			
			else
			{
				return 0;	//if cannot go to child while in the for loop then break to test cases
			}
			
			while(currentNode.value != currentChar)	//go through all peers
			{
				if(currentNode.peer != null)
				{
					currentNode = currentNode.peer;	//move to next peer
				}
				
				else
				{
					return 0;	//if no more peers then the word is not in DLB
				}
			}
		}
		
		if(currentNode.child != null && currentNode.isAWord)
		{
			return 3;	//s is both a prefix and a word
		}
		
		else if(currentNode.isAWord)
		{
			return 2;	//s is a word
		}
		
		else if(currentNode.child != null)
		{
			return 1; //s is a prefix
		}
		
		else
		{
			return 0;	//s is not a word
		}
	}
	
}
