# Boggle
The Boggle project is one of the prouder Java projects I wrote for a class.  
I used recursion to search through the Boggle board and wrote an add and search method for a De La Briandais trie.  
There are six different boards with "*" denoting a wild card and two types of dictionaries, simple and a De La Briandais one.  
Since there are so many possible words and all words are printed when the game is over, you may not be able to scroll up to the board when the game is over.  
It can be compiled in command prompt using "javac MyBoggle.java".  
It can be run by typing "java MyBoggle -b &lt;board text file&gt; -d &lt;dictionary type&gt;" in either order.  
For example, if you wanted to run it with board 1 using a simple dictionary you could type: "java MyBoggle -d simple -b board1.txt".  
Disclaimer: All boards.txt, dictionary.txt, DictionaryInterface.java, and SimpleDictionary.java were provided.
