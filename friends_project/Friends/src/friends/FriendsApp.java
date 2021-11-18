package friends;

import java.io.*;
import java.util.*;

// Testing client for Friends
public class FriendsApp {

    public static void main (String[] args) {

	if ( args.length < 1 ) {
	    System.out.println("Expecting graph text file as input");
	    return;
	}

	String filename = args[0];
	try {
	    Graph g = new Graph(new Scanner(new File(filename)));

	    // Update p1 and p2 to refer to people on Graph g
	    // sam and sergei are from sample graph
	    String p1 = "p301";
	    String p2 = "p198";
	    ArrayList<String> shortestChain = Friends.shortestChain(g, p1, p2);

	    // Testing Friends.shortestChain
	    System.out.println("Shortest chain from " + p1 + " to " + p2);
		if (shortestChain == null || shortestChain.size() == 0) {
			System.out.println("Returned null.");
		} else {
			for ( String s : shortestChain ) {
				System.out.println(s);
				}
		}
	     
	    // ADD test for Friends.cliques() here
		System.out.println("Cliques");
		ArrayList<ArrayList<String>> clique = Friends.cliques(g, "rutgers");
		if (clique == null || clique.size() == 0) {
			System.out.println("Returned null.");
		} else {
			for (ArrayList<String> a : clique) {
				System.out.print("[ ");
				for (String s : a){
					System.out.print(s + ", ");
				}
				System.out.println("]");
			}
		} 
	
	    // ADD test for Friends.connectors() here
		System.out.print("Connectors: ");
		ArrayList<String> connectors = Friends.connectors(g);
		for (String a : connectors ) {
			System.out.print(a + " ");
		}

	} catch (FileNotFoundException e) {

	    System.out.println(filename + " not found");
	}
    }
}
