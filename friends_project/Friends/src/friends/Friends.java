package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		//Like dijkstra
		Person prev[] = new Person[g.members.length];
		boolean visited[] = new boolean[g.members.length];
		ArrayList<String> chain = new ArrayList<String>();
		Queue<Person> q = new Queue<Person>();
		int index;

		//If any input is null, return null b/c invalid inputs
		if (g == null) { System.out.println("Invalid input(s)."); return null; }
		if (p1 == null) { System.out.println("Invalid input(s)."); return null; }
		if (p2 == null) { System.out.println("Invalid input(s)."); return null; }

		//set all initial values to defaults
		for (int i = 0; i < g.members.length; i++) {
			prev[i] = null;
			visited[i] = false;
		}
		//set up to start at p1
		if (g.map.get(p1) == null) {
			System.out.println("p1 does not exist in the graph.");
			return null;
		} else if (g.map.get(p2) == null) {
			System.out.println("p2 does not exist in the graph.");
			return null;
		} else {
			index = g.map.get(p1);
		}
		visited[index] = true;

		//enqueue p1
		q.enqueue(g.members[index]);

		while (q.isEmpty() != true ){
			Person curr = q.dequeue();
			int ind = g.map.get(curr.name);
			visited[ind] = true;
			for (Friend f = curr.first; f != null; f = f.next) {
			 	//enqueue all friends of current person, mark them as visited and update their previous as curr
				if (visited[f.fnum] == false ){ 
					visited[f.fnum] = true;
					prev[f.fnum] = curr;
					q.enqueue(g.members[f.fnum]);

					if(g.members[f.fnum].name.equals(p2)) {
						//if p2 has been found on the path, trace back using prev[]
						Person reverse = g.members[f.fnum];
						while (reverse.name.equals(p1) == false ) {
							chain.add(0, reverse.name);
							reverse = prev[g.map.get(reverse.name)];
						}
						//add p1 to the list
						chain.add(0,p1);
						return chain;

					}
				}
			}

		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		//If any input is null, return null b/c invalid inputs
		if (g == null) { System.out.println("Invalid input(s)."); return null; }
		if (school == null) { System.out.println("Invalid input(s)."); return null; }
		
		//Variable set up
		ArrayList<ArrayList<String>> cliques = new ArrayList<ArrayList<String>>();
		boolean[] marked = new boolean[g.members.length];

		//use a BFS to check people in the graph
		return BFS(g, g.members[0], cliques, marked, school);
	}

	private static ArrayList<ArrayList<String>> BFS (Graph g, Person start, ArrayList<ArrayList<String>> cliques, boolean[] marked, String school) {
		ArrayList<String> cliqueResult = new ArrayList<String>(); //will store individual cliques too add to master list "cliques"
		Queue<Person> q = new Queue<Person>(); //BFS method using queues
		Person curr = new Person();
		Friend adj;

		//begin queue with whoever is at index 0 or g.members
		q.enqueue(start); 
		marked[g.map.get(start.name)] = true;

		if (start.school == null || start.school.equals(school) == false) { //if first person does not have the school we are looking for
			q.dequeue();
			for (int i = 0; i < marked.length; i++){
				if (marked[i] == false) {
					//recursively repeat process with all other people in graph to find someone with the school we are looking for
					return BFS(g, g.members[i], cliques, marked, school);
				}
			}
		}	

		while (q.isEmpty() == false) { //if the person has the school we are looking for, check through all connected people until the queue is empty
			curr = q.dequeue();
			adj = curr.first;
			cliqueResult.add(curr.name);
			while (adj != null) { //check all connected people to complete the clique who have not be visited before
				if (marked[adj.fnum] != true) {
					if (g.members[adj.fnum].school != null && g.members[adj.fnum].school.equals(school)) {
						//only enqueue the ones who have the same school to cut down on accesses
						q.enqueue(g.members[adj.fnum]);
					}
					marked[adj.fnum] = true;
				}
				adj = adj.next;
			}
		}

		if (cliques.isEmpty() && !cliqueResult.isEmpty()){
			cliques.add(cliqueResult);
		} else if (!cliqueResult.isEmpty()) {
			cliques.add(cliqueResult);
		}
		for (int j = 0; j < marked.length; j++) {
			//check for unconnected groups to loop through
			if (marked[j] == false) {
				return BFS(g, g.members[j], cliques, marked, school);
			}
		}

		return cliques;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		//If input is null, return null b/c invalid inputs
		if (g == null) { System.out.println("Invalid input(s)."); return null; }

		//Variable set up
		ArrayList<String> connectors = new ArrayList<String>();
		boolean[] marked = new boolean[g.members.length];
		ArrayList<String> prev = new ArrayList<String>();
		int[] dfsNum = new int[g.members.length];
		int[] back = new int[g.members.length];

		for (int i = 0; i < g.members.length; i++) {
			if (marked[i] == false) {
				//recursively repeat DFS until all persons have been touched
				connectors = DFS(connectors, g, g.members[i], marked, new int[] {0,0}, dfsNum, back, prev, true);
			}
		}

		return connectors;
	}

	private static ArrayList<String> DFS(ArrayList<String> connectors, Graph g, Person start, boolean[] marked, int[] count, int[] dfsNum, int[] back, ArrayList<String> prev, boolean starting) {
		//current item set up (mark as visited, get friend, and fill in back and dfsNum)
		marked[g.map.get(start.name)] = true;
		Friend adj = start.first;
		dfsNum[g.map.get(start.name)] = count[0];
		back[g.map.get(start.name)] = count[1];
		int curr = g.map.get(start.name);

		//dfs though adjacent friend, update dfsNum and back numbers as needed
		while(adj != null) {
			//v = curr, w = adj.fnum
			if (marked[adj.fnum] == false) {
				count[0]++;
				count[1]++;
				connectors = DFS(connectors, g, g.members[adj.fnum], marked, count, dfsNum, back, prev, false); //recursive dfs

				//begin dfsNum and back updates
				if(dfsNum[curr] <= back[adj.fnum]) {
					// if dfsnum(v) ≤ back(w), then v is identified as a connector
					if ((connectors.contains(start.name) ==  false && prev.contains(start.name)) || (connectors.contains(start.name) == false && starting == false)){ //add person to connector list if name is not already in list
						connectors.add(start.name);
					}
				} else {
					//if dfsnum(v) > back(w), then back(v) is set to min(back(v),back(w))
					if (back[adj.fnum] < back[curr]) {
						back[curr] = back[adj.fnum];
					} //else back(v) remains back(v)
				}
			prev.add(start.name);
			} else {
				//If a neighbor, w, is already visited then back(v) is set to min(back(v),dfsnum(w))
				if ( dfsNum[adj.fnum] < back[curr]) {
					back[curr] = dfsNum[adj.fnum];
				}
			}
			//move on to next adjacent friend
			adj = adj.next;
		}
		
		return connectors;
	}
}

