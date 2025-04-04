package chapter28;
import java.util.*;

public class UnweightedGraph<V> implements Graph<V> {
  protected List<V> vertices = new ArrayList<>(); // Store vertices
  protected List<List<Edge>> neighbors 
    = new ArrayList<>(); // Adjacency lists

  /** Construct an empty graph */
  public UnweightedGraph() {
  }
  
  /** Construct a graph from vertices and edges stored in arrays */
  public UnweightedGraph(V[] vertices, int[][] edges) {
    for (int i = 0; i < vertices.length; i++)
      addVertex(vertices[i]);
    
    createAdjacencyLists(edges, vertices.length);
  }

  /** Construct a graph from vertices and edges stored in List */
  public UnweightedGraph(List<V> vertices, List<Edge> edges) {
    for (int i = 0; i < vertices.size(); i++)
      addVertex(vertices.get(i));
        
    createAdjacencyLists(edges, vertices.size());
  }

  /** Construct a graph for integer vertices 0, 1, 2 and edge list */
  public UnweightedGraph(List<Edge> edges, int numberOfVertices) {
    for (int i = 0; i < numberOfVertices; i++) 
      addVertex((V)(new Integer(i))); // vertices is {0, 1, ...}
    
    createAdjacencyLists(edges, numberOfVertices);
  }

  /** Construct a graph from integer vertices 0, 1, and edge array */
  public UnweightedGraph(int[][] edges, int numberOfVertices) {
    for (int i = 0; i < numberOfVertices; i++) 
      addVertex((V)(new Integer(i))); // vertices is {0, 1, ...}
    
    createAdjacencyLists(edges, numberOfVertices);
  }

  /** Create adjacency lists for each vertex */
  private void createAdjacencyLists(
      int[][] edges, int numberOfVertices) {
    for (int i = 0; i < edges.length; i++) {
      addEdge(edges[i][0], edges[i][1]);
    }
  }

  /** Create adjacency lists for each vertex */
  private void createAdjacencyLists(
      List<Edge> edges, int numberOfVertices) {
    for (Edge edge: edges) {
      addEdge(edge.u, edge.v);
    }
  }
  
  public List<Integer> getPath(int u, int v) {
      // Perform BFS starting from u
      SearchTree searchTree = bfs(u);

      // If v is unreachable from u, its parent will be -1
      if (searchTree.getParent(v) == -1) {
          return null;  // No path exists
      }

      // List to store the path from v to u
      List<Integer> path = new ArrayList<>();

      // Backtrack from v to u using the parent array
      int current = v;
      while (current != -1) {
          path.add(current);  // Add the current vertex to the path
          current = searchTree.getParent(current);  // Move to the parent vertex
      }

      // Reverse the path to get it from u to v
      Collections.reverse(path);

      return path;
  }
  
  public List<Integer> getACycle() {
	    boolean[] visited = new boolean[vertices.size()];
	    boolean[] inStack = new boolean[vertices.size()]; // Track the recursion stack
	    int[] parent = new int[vertices.size()]; // To track the parent of each vertex
	    Arrays.fill(parent, -1); // Initialize parent to -1 (no parent)

	    // Iterate over all vertices to handle disconnected graphs
	    for (int i = 0; i < vertices.size(); i++) {
	        if (!visited[i]) {
	            List<Integer> cycle = dfsCycleDetection(i, visited, inStack, parent);
	            if (cycle != null) {
	                return cycle;
	            }
	        }
	    }

	    return null; // No cycle found
	}

	private List<Integer> dfsCycleDetection(int v, boolean[] visited, boolean[] inStack, int[] parent) {
	    visited[v] = true;
	    inStack[v] = true;

	    for (Edge e : neighbors.get(v)) {
	        int neighbor = e.v;
	        
	        if (!visited[neighbor]) {
	            parent[neighbor] = v;
	            List<Integer> cycle = dfsCycleDetection(neighbor, visited, inStack, parent);
	            if (cycle != null) {
	                return cycle; // If a cycle is found, return it
	            }
	        }
	        // Check if the neighbor is on the recursion stack and not the parent
	        else if (inStack[neighbor] && parent[v] != neighbor) {
	            // A cycle is found, backtrack to construct the cycle path
	            List<Integer> cycle = new ArrayList<>();
	            cycle.add(neighbor);
	            int current = v;
	            while (current != neighbor) {
	                cycle.add(current);
	                current = parent[current];
	            }
	            cycle.add(neighbor);
	            Collections.reverse(cycle); // To get the cycle starting from the correct vertex
	            return cycle;
	        }
	    }

	    inStack[v] = false; // Unmark the vertex from the stack
	    return null; // No cycle found from this vertex
	}

  @Override /** Return the number of vertices in the graph */
  public int getSize() {
    return vertices.size();
  }

  @Override /** Return the vertices in the graph */
  public List<V> getVertices() {
    return vertices;
  }

  @Override /** Return the object for the specified vertex */
  public V getVertex(int index) {
    return vertices.get(index);
  }

  @Override /** Return the index for the specified vertex object */
  public int getIndex(V v) {
    return vertices.indexOf(v);
  }

  @Override /** Return the neighbors of the specified vertex */
  public List<Integer> getNeighbors(int index) {
    List<Integer> result = new ArrayList<>();
    for (Edge e: neighbors.get(index))
      result.add(e.v);
    
    return result;
  }

  @Override /** Return the degree for a specified vertex */
  public int getDegree(int v) {
    return neighbors.get(v).size();
  }

  @Override /** Print the edges */
  public void printEdges() {
    for (int u = 0; u < neighbors.size(); u++) {
      System.out.print(getVertex(u) + " (" + u + "): ");
      for (Edge e: neighbors.get(u)) {
        System.out.print("(" + getVertex(e.u) + ", " +
          getVertex(e.v) + ") ");
      }
      System.out.println();
    }
  }

  @Override /** Clear the graph */
  public void clear() {
    vertices.clear();
    neighbors.clear();
  }
  
  @Override /** Add a vertex to the graph */  
  public boolean addVertex(V vertex) {
    if (!vertices.contains(vertex)) {
      vertices.add(vertex);
      neighbors.add(new ArrayList<Edge>());
      return true;
    }
    else {
      return false;
    }
  }

  @Override /** Add an edge to the graph */  
  public boolean addEdge(Edge e) {
    if (e.u < 0 || e.u > getSize() - 1)
      throw new IllegalArgumentException("No such index: " + e.u);

    if (e.v < 0 || e.v > getSize() - 1)
      throw new IllegalArgumentException("No such index: " + e.v);
    
    if (!neighbors.get(e.u).contains(e)) {
      neighbors.get(e.u).add(e);
      return true;
    }
    else {
      return false;
    }
  }
  
  @Override /** Add an edge to the graph */  
  public boolean addEdge(int u, int v) {
    return addEdge(new Edge(u, v));
  }
  
  @Override /** Obtain a DFS tree starting from vertex u */
  /** To be discussed in Section 28.7 */
  public SearchTree dfs(int v) {
    List<Integer> searchOrder = new ArrayList<>();
    int[] parent = new int[vertices.size()];
    for (int i = 0; i < parent.length; i++)
      parent[i] = -1; // Initialize parent[i] to -1

    // Mark visited vertices
    boolean[] isVisited = new boolean[vertices.size()];

    // Recursively search
    dfs(v);

    // Return a search tree
    return new SearchTree(v, parent, searchOrder);
  }

  public SearchTree dfsNonRecurs(int v) {
	    // Will store the parent of each vertex
	    int[] parent = new int[vertices.size()];
	    for (int i = 0; i < parent.length; i++) {
	        parent[i] = -1; // Initialize parent[i] to -1
	    }
	    
	    // Mark visited vertices
	    boolean[] isVisited = new boolean[vertices.size()];
	    
	    // Finish dfs order
	    List<Integer> searchOrder = new ArrayList<>();
	    
	    // Stack to keep track of vertices to visit
	    Stack<Integer> stack = new Stack<>();
	    
	    // Push the starting vertex onto the stack
	    stack.push(v);
	    isVisited[v] = true; // Mark the starting vertex as visited
	    searchOrder.add(v); // Add the starting vertex to the search order
	    
	    // DFS loop using the stack
	    while (!stack.isEmpty()) {
	        int x = stack.peek(); // Get the top vertex in the stack
	        boolean hasUnvisitedNeighbor = false;
	        
	        // Iterate through neighbors of the current vertex in reverse order
	        for (int i = neighbors.get(x).size() - 1; i >= 0; i--) {
	            Edge e = neighbors.get(x).get(i); // Get the neighbor edge
	            int neighbor = e.v; // Get the neighboring vertex
	            
	            // If the neighbor has not been visited yet
	            if (!isVisited[neighbor]) {
	                // Mark the parent of the neighbor
	                parent[neighbor] = x;
	                // Mark the neighbor as visited
	                isVisited[neighbor] = true;
	                // Push the neighbor onto the stack
	                stack.push(neighbor);
	                // Add the neighbor to the search order
	                searchOrder.add(neighbor);
	                // Break out of the loop to process the neighbor first
	                hasUnvisitedNeighbor = true;
	                break;
	            }
	        }

	        // If the current vertex has no unvisited neighbors, pop it from the stack
	        if (!hasUnvisitedNeighbor) {
	            stack.pop();
	        }
	    }

	    // Return the DFS search tree
	    return new SearchTree(v, parent, searchOrder);
	}

  @Override /** Starting bfs search from vertex v */
  /** To be discussed in Section 28.9 */
  public SearchTree bfs(int v) {
    List<Integer> searchOrder = new ArrayList<>();
    int[] parent = new int[vertices.size()];
    for (int i = 0; i < parent.length; i++)
      parent[i] = -1; // Initialize parent[i] to -1

    java.util.LinkedList<Integer> queue =
      new java.util.LinkedList<>(); // list used as a queue
    boolean[] isVisited = new boolean[vertices.size()];
    queue.offer(v); // Enqueue v
    isVisited[v] = true; // Mark it visited

    while (!queue.isEmpty()) {
      int u = queue.poll(); // Dequeue to u
      searchOrder.add(u); // u searched
      for (Edge e: neighbors.get(u)) { // Note that e.u is u
        if (!isVisited[e.v]) { // e.v is w in Listing 28.11
          queue.offer(e.v); // Enqueue w
          parent[e.v] = u; // The parent of w is u
          isVisited[e.v] = true; // Mark w visited
        }
      }
    }

    return new SearchTree(v, parent, searchOrder);
  }

  /** Tree inner class inside the AbstractGraph class */
  /** To be discussed in Section 28.6 */
  public class SearchTree {
    private int root; // The root of the tree
    private int[] parent; // Store the parent of each vertex
    private List<Integer> searchOrder; // Store the search order

    /** Construct a tree with root, parent, and searchOrder */
    public SearchTree(int root, int[] parent, 
        List<Integer> searchOrder) {
      this.root = root;
      this.parent = parent;
      this.searchOrder = searchOrder;
    }

    /** Return the root of the tree */
    public int getRoot() {
      return root;
    }

    /** Return the parent of vertex v */
    public int getParent(int v) {
      return parent[v];
    }

    /** Return an array representing search order */
    public List<Integer> getSearchOrder() {
      return searchOrder;
    }

    /** Return number of vertices found */
    public int getNumberOfVerticesFound() {
      return searchOrder.size();
    }
    
    /** Return the path of vertices from a vertex to the root */
    public List<V> getPath(int index) {
      ArrayList<V> path = new ArrayList<>();

      do {
        path.add(vertices.get(index));
        index = parent[index];
      }
      while (index != -1);

      return path;
    }

    
    
    /** Print a path from the root to vertex v */
    public void printPath(int index) {
      List<V> path = getPath(index);
      System.out.print("A path from " + vertices.get(root) + " to " +
        vertices.get(index) + ": ");
      for (int i = path.size() - 1; i >= 0; i--)
        System.out.print(path.get(i) + " ");
    }

    /** Print the whole tree */
    public void printTree() {
      System.out.println("Root is: " + vertices.get(root));
      System.out.print("Edges: ");
      for (int i = 0; i < parent.length; i++) {
        if (parent[i] != -1) {
          // Display an edge
          System.out.print("(" + vertices.get(parent[i]) + ", " +
            vertices.get(i) + ") ");
        }
      }
      System.out.println();
    }
  }
  
  @Override /** Remove vertex v and return true if successful */  
  public boolean remove(V v) {
    return true; // Implementation left as an exercise
  }

  @Override /** Remove edge (u, v) and return true if successful */  
  public boolean remove(int u, int v) {
    return true; // Implementation left as an exercise
  }
}
