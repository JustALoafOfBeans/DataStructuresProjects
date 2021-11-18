package lse;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/*
 *
 * Little Search Engine Test App (LSETest)
 * Test application for LSE
 *
 */
public class LSETest {

    public static void main (String[] args) throws FileNotFoundException{

	// Change these values to use other documents
	String docsFile       = "docs.txt";
	String noiseWordsFile = "noisewords.txt";
	
	LittleSearchEngine lse = new LittleSearchEngine();
	lse.makeIndex(docsFile, noiseWordsFile);
	
	// Find the top 5 search using the words deep and world
	// Do other tests here to test your top5search() code.
	//ArrayList<String> searchResult = lse.top5search("lick", "bleh");
	ArrayList<String> searchResult = lse.top5search("deep", "world");

	if ( searchResult != null ) {
	    for (String res : searchResult) {
		System.out.println(res);
	    }
	} else {
	    System.out.println("top5search is returning null");
	}
    }

}
