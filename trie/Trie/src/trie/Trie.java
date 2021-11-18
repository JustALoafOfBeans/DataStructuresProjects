package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		TrieNode root = new TrieNode(null, null, null);
		String currentW;

		//For first word
		Indexes index = new Indexes(0, (short) 0, (short) (allWords[0].length()-1));
		TrieNode node = new TrieNode(index, null, null);
		root.firstChild = node;
		TrieNode mostSim = node;

		//Adding other words after
		for (int l = 1; l <= (allWords.length -1); l++) { //for all words that need to be added
			TrieNode curr = root.firstChild;
			currentW = allWords[l]; //the current word we're trying to add
			//System.out.println("Current word: " + currentW);
			//comparison to find where to insert
			String prefix = allWords[curr.substr.wordIndex].substring((int) curr.substr.startIndex, ((int) curr.substr.endIndex)+1); //endInd + 1 b/c exclusive
			//System.out.println("Prefix is : " + prefix);
			boolean searching = true;
			boolean tierOne = true;
			while (searching) {
				if ((currentW.startsWith(prefix)) && (curr.firstChild != null)) { //if full prefix match
					//System.out.println("search down");
					currentW = allWords[l].substring(((int) curr.substr.endIndex)+1);
					curr = curr.firstChild;
					mostSim = curr;
					tierOne = false;
				} else if (currentW.charAt(0) == prefix.charAt(0)) { //if partial prefix match
					//System.out.println("partial match with prefix.");
					mostSim = curr;
					searching = false;
				} else if (curr.sibling != null){ //if no match with prefix at all
					//System.out.println("search sibling");
					curr = curr.sibling; 
					mostSim = curr;
				} else { //if no theres no prefix match (full or semi) and no more siblings 
					//System.out.println("stop search");
					searching = false;
					mostSim = curr;
				}
				//update prefix
				int endInd = (int) curr.substr.endIndex;
				//System.out.println("pulling from: " + allWords[curr.substr.wordIndex]);
				//System.out.println("With indexes: " + curr.substr.startIndex + " and " + curr.substr.endIndex);
				prefix = allWords[curr.substr.wordIndex].substring((int) curr.substr.startIndex, (endInd+1));
				//System.out.println("New prefix is: " + prefix);
				//System.out.println("New word is: " + currentW);
			}

			//mostSim is where need to add word, now add it
			if (currentW.charAt(0) == prefix.charAt(0)) { //if word w/ first letter already exists
				//System.out.println("Add as prefix"); 
				//current word in the tree inserted word is being compared to
				String trieWord = allWords[mostSim.substr.wordIndex];
				//check if any more overlap
				int j = 0;
				while (currentW.charAt(j) == prefix.charAt(j)) { j++; } //this is how much overlap +1
				//if mostSim has children
				if(mostSim.firstChild != null) {
					int adjustBy = (int) mostSim.substr.startIndex;
					adjustBy = adjustBy + j;
					index = new Indexes(mostSim.substr.wordIndex, (short) adjustBy , mostSim.substr.endIndex);
					TrieNode remainder = new TrieNode(index, null, null);
					remainder.firstChild = mostSim.firstChild;
					mostSim.firstChild = remainder;
					//update prefix at mostSim
					adjustBy--;
					index = new Indexes(mostSim.substr.wordIndex, mostSim.substr.startIndex, (short) adjustBy);
					mostSim.substr = index;
					//add new word
					adjustBy++;
					index = new Indexes(l, (short) adjustBy, (short) (allWords[l].length()-1));
					TrieNode temp = new TrieNode(index, null, null);
					remainder.sibling = temp;
				} else {
					//update parent node and add old word as first child
					int adjustBy = (int) mostSim.substr.startIndex;
					adjustBy = adjustBy + j - 1;
					index = new Indexes(mostSim.substr.wordIndex, mostSim.substr.startIndex, (short) adjustBy);
					mostSim.substr = index;
					adjustBy++;
					index = new Indexes(mostSim.substr.wordIndex, (short) adjustBy, (short) (trieWord.length()-1));
					TrieNode temp = new TrieNode (index, null, null);
					mostSim.firstChild = temp;
					//add new word as sibling to first child (old word)
					index = new Indexes(l, (short) adjustBy, (short) (allWords[l].length()-1));
					TrieNode temp1 = new TrieNode(index, null, null);
					temp.sibling = temp1;
				}
			} else if (currentW.charAt(0) != allWords[mostSim.substr.wordIndex].charAt(0)) { // if word does not match any prefix
				//System.out.println("Add as new sibling.");
				if (tierOne) {
					index = new Indexes(l, (short) 0, (short) (allWords[l].length()-1));
				} else {
					index = new Indexes(l, mostSim.substr.startIndex, (short) (allWords[l].length()-1));
				}
				TrieNode newNode = new TrieNode(index, null, null);
				mostSim.sibling = newNode;
			}
		}
		return root;
	}
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		//if none of the word matches your prefix go to the right, if some but not all matches your prefix go down and adjust your prefix to remove the common part,
		// and as soon as the entire prefix matches just print out every leaf node under that node
		ArrayList<TrieNode> list = new ArrayList<TrieNode>();
		//to add to list --> list.add(node);
		TrieNode curr = root.firstChild;
		TrieNode preHere = root;
		boolean searching = true;
		String nodePre;

		while (searching) {
			nodePre = allWords[curr.substr.wordIndex].substring((int) curr.substr.startIndex, ((int) curr.substr.endIndex)+1); //endInd + 1 b/c exclusive
			//System.out.println("Comparing to: " + nodePre);
			//System.out.println("Prefix at node: " + nodePre);
			if (prefix.equals(nodePre)) { //full prefix match
				//System.out.println("Prefix found.");
				preHere = curr;
				searching = false;
			} else if ((prefix.charAt(0) == nodePre.charAt(0))) { //partial prefix match
				//System.out.println("Partial prefix found, need to move down.");
				//check to find overlap
				int preLength = prefix.length();
				int nodeLength = nodePre.length();
				int len, count=0, j=0;
				if (preLength < nodeLength) { len = preLength; }
				else { len = nodeLength; }
				while ((count < len) && (prefix.charAt(j) == nodePre.charAt(j))) { 
					j++;
					count++;
				} //over lap from start (0) to end (j-1)
				if (prefix.length() == j) { //If prefix is inside (found in) nodePre
					preHere = curr;
					searching = false;
				}else {
					//update prefix
					int newInd = ((int) curr.substr.endIndex) - ((int) curr.substr.startIndex);
					//System.out.println("New start index for prefix: " + newInd);
					prefix = prefix.substring(newInd+1);
					//System.out.println("Searching for: " + prefix);
					//move down and keep searching
					curr = curr.firstChild;
				}
			} else if (curr.sibling != null) { //no match but sibling can be checked
				//System.out.println("No match, check sibling.");
				curr = curr.sibling;
			} else { //no prefix match found to any capacity and no more siblings to check
				//System.out.println("No prefix match. Returned null list.");
				return null;
			}
		}

		//NOW NEED TO MAKE THE ARRAY WITH ALL OF THE NODES UNDER preHere!! Maybe recursive?
		if (preHere.firstChild != null) {
			addToList(list, preHere.firstChild);
		} else {
			list.add(preHere);
		}

		return list;
	}

	private static void addToList(ArrayList<TrieNode> list, TrieNode curr) {
		//base case
		if ((curr.sibling == null) && (curr.firstChild == null)) {
			//System.out.println("End of line, no more siblings.");
			list.add(curr);
			return;
		}

		//add the node to list
		//if sibling present, go to sibling
		//if child present, go to child
		if (curr.firstChild == null) {
			list.add(curr);
		}
		
		if (curr.sibling != null) {
			addToList(list, curr.sibling);
		}
		if (curr.firstChild != null) {
			addToList(list, curr.firstChild);
		}

	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
