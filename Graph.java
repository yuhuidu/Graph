import java.lang.*;
import java.util.*;
import java.awt.*;

/** 
 *  graph
 *
 *  @author helen du
 *  @version 12/4/17
 */
public class Graph<V,E> {

    /** list of edges*/
    public ArrayList<Edge> edges;

    /** list of nodes*/
    public ArrayList<Node> nodes;

    /** constructor */
    public Graph(){
        edges = new ArrayList<Edge> ();
        nodes = new ArrayList<Node> ();
    }

    /** Accessor for node list */
    public ArrayList<Node> getNodeList(){
        return nodes;
    }

    /** Accessor for edge list */
    public ArrayList<Edge> getEdgeList(){
        return edges;
    }

    /** Accessor for nodes */
    public Node getNode(int i){
        Node node = nodes.get(i);
        return node;
    }

    /** Accessor for nodes */
    public Node getNode(V data){
        for (Node node: nodes){
            if (node.getData() == data){
                return node;
            }
        }
        return null;
    }

    /** Accessor for edges */
    public Edge getEdge(int i){
        Edge edge = edges.get(i);
        return edge;
    }

    /** Accessor for specific edge */
    public Edge getEdgeRef(Node head, Node tail){
        return head.edgeTo(tail);
    }

    /** Accessor for number of nodes */
    public int numNodes(){
        return nodes.size();
    }

    /** Accessor for number of edges */
    public int numEdges(){
        return edges.size();
    }

    /** Adds a node */
    public Node addNode(V data){
        Node node = new Node(data);
        nodes.add(node);
        return node;
    }

    /** Adds an edge */
    public Edge addEdge(E data, Node head, Node tail){
        Edge edge = new Edge(data, head, tail);
        head.addEdgeRef(edge);
        tail.addEdgeRef(edge);
        edges.add(edge);
        return edge;
    }

    /** Removes a node */
    public void removeNode(Node node){
        while (!node.edgeList.isEmpty()) {
            Edge edge = node.getEdgeRef().get(0);
            removeEdge(edge);
        }
        nodes.remove(node);
    }

    /** Removes an edge */
    public void removeEdge(Edge edge){
        Node head = edge.getHead();
        Node tail = edge.getTail();
        edges.remove(edge);
        head.removeEdgeRef(edge);
        tail.removeEdgeRef(edge);
    }

    /** Removes an edge */
    public void removeEdge(Node head, Node tail){
        Edge edge = head.edgeTo(tail);;
        removeEdge(edge);
    }

    /** Returns nodes not on a given list */
    public HashSet<Node> otherNodes
        (HashSet<Node> group){
        HashSet<Node> others = new HashSet<Node>();
        for (Node node: group){
            if (!nodes.contains(node)){
                others.add(node);
            }
        }
        return others;
    }

    /** eturns nodes that are endpoints of a list of edges */
    public HashSet<Node> endpoints (HashSet<Edge> edges){
        HashSet<Node> endpoints = new HashSet<Node>();
        for (Edge edge: edges){
            Node head = edge.getHead();
            Node tail = edge.getTail();
            endpoints.add(head);
            endpoints.add(tail);
        }
        return endpoints;
    }

    /** Breadth-first traversal of graph */
    public HashSet<Edge> BFT(Node start){
        HashSet<Edge> set = new HashSet<Edge>();
        LinkedList<E> linkedlist = new LinkedList<E>();
        LinkedList<Node> q = new LinkedList<Node>();
        q.add(start);
        while ( !q.isEmpty()){
            Node first = q.remove();
            ArrayList<Node> neighbors = first.getNeighbors();
            for (Node node: neighbors){
                if ( !node.isVisited()){
                    q.add(node);
                    Edge edge = first.edgeTo(node);
                    linkedlist.add(edge.getData());
                    edge.setColor(Color.CYAN);
                    set.add(edge);
                } 
            }
            first.visited();
        }
        System.out.println("BFT IS " +linkedlist);
        clear();
        return set;
    }

    /** Depth-first traversal of graph -- public interface */
    public LinkedList<Edge> DFT(Node start){
        LinkedList<Edge> set = new LinkedList<Edge>();
        LinkedList<Node> unvisited = new LinkedList<Node>();
        set = recursion(set, unvisited, start);
        LinkedList<E> datalist = new LinkedList<E>();
        for (Edge edge: set){
            datalist.add(edge.getData());
        }
        System.out.println("DFT IS " +datalist);
        clear();
        return set;
    }

    /** recursive method for DFT */
    public LinkedList<Edge> recursion
        ( LinkedList<Edge> set, LinkedList<Node> unvisited, Node node){
        ArrayList<Node> neighbors = node.getNeighbors();
        for (Node n: nodes){
            if (!n.isVisited()){
                unvisited.add(n);
            }
        }
        if (!node.isVisited()){
            node.visited();
            for (Node neighbor: neighbors){
                Edge edge = neighbor.edgeTo(node);
                if (!set.contains(edge)){
                    edge.setColor(Color.CYAN);
                    set.add(edge);
                }
                recursion(set, unvisited, neighbor);
            }
        }
        return set;
    }

    /** set the status of all nodes to visited */
    public void clear(){
        for (Node node: nodes){
            node.visited = false;
        }
    }

    /** Dijkstra's shortest-path algorithm to compute distances to nodes */
    public ArrayList<Double> distances(Node start){
        double inf = Double.POSITIVE_INFINITY;
        double lowestcost =  Double.POSITIVE_INFINITY;
        ArrayList<Double> costList = new ArrayList<Double>();
        ArrayList<Node> unvisited = new ArrayList<Node>();
        Node lowest = start;
        for (Node node: nodes){
            int index = nodes.indexOf(node);
            costList.add(1.0);
            if (!node.isVisited()){
                unvisited.add(node);
            }
            if (node == start){
                costList.set(index,0.0);
            }
            else{
                costList.set(index,inf);
            }
        } // END OF INITIALIZING UNVISITED AND COST
        ArrayList<V> datalist = new ArrayList<V>();
        for (Node n: unvisited){
            datalist.add(n.getData());
        }
        System.out.println("unvisited " + datalist);
        System.out.println("cost " + costList);

        while(!unvisited.isEmpty()){
            //System.out.println("lowest " + lowest.getData());
            ArrayList<Node> neighbors = lowest.getNeighbors();
            lowest.visited();
            int indexoflowest = nodes.indexOf(lowest);
            double costoflowest = costList.get(indexoflowest);
            for (Node node: neighbors){
                if (!node.isVisited()){
                    int nodeindex = nodes.indexOf(node);
                    Edge edge = node.edgeTo(lowest);
                    double cost = edge.getDistance() + costoflowest;
                    if (cost < costList.get(nodeindex)){
                        costList.set(nodeindex,cost);
                    }
                    if (cost < lowestcost){
                        lowestcost = cost;
                    }
                    node.visited();
                }
            }
            unvisited.remove(lowest);
            int lowestindex = costList.indexOf(lowestcost);
            lowest = nodes.get(lowestindex);
        }
        System.out.println("cost list is: " + costList);
        return costList;
    }

    /** Prints a representation of the graph */
    public void print(){
        System.out.println("edges are: ");
        for (Edge edge: edges) {
            System.out.println(edge.getData());
        }

        System.out.println("nodes are: ");
        for (Node node: nodes) {
            System.out.println(node.getData());
        }
    }

    //------------------------CLASS EDGE-----------------------------
    /** nested class Edge*/
    public class Edge{

        /** the data */
        private E data;

        /** the head node */
        private Node head;

        /** the tail node */
        private Node tail;

        private double distance;

        private Color color = Color.BLACK;

        /** Constructor creates a new edge */
        public Edge(E data, Node head, Node tail){
            this.data = data;
            this.tail = tail;
            this.head = head;
        }

        /** Accessor for data */
        public E getData(){
            return data;
        }

        /** Accessor for endpoint #1 */
        public Node getHead(){
            return head;
        }

        /** Accessor for endpoint #2 */
        public Node getTail(){
            return tail;
        }

        public double getDistance(){
            return distance;
        }

        public void setDistance(double i){
            this.distance = i;
        }

        public Color getColor(){
            return color;
        }

        public void setColor(Color color){
            this.color = color;
        }

        /** Accessor for opposite node */
        public Node oppositeTo(Node node){
            Node tail = this.getTail();
            Node head = this.getHead();
            if (node == tail){
                return head;
            }
            else if (node == head){
                return tail;
            }
            return null;
        }

        /** Manipulator for data */
        public void setData(E data){
            this.data = data;
        }

        /** Two edges are equal if they connect the same
            endpoints regardless of the data they carry */
        public boolean equals(Edge o){
            Node head = this.getHead();
            Node tail = this.getTail();
            Node heado = o.getHead();
            Node tailo = o.getTail();
            if (head == heado && tail == tailo){
                return true;
            }
            else if (head == tailo && tail == heado){
                return true;
            }
            else {
                return false;
            }
        }

        /** Redefined hashcode to match redefined equals */
        public int hashCode(){
            int code = head.hashCode() * tail.hashCode();
            return code;
        }

    }// END OF CLASS EDGE

    //------------------------CLASS NODE-----------------------------
    /** nested class Node*/
    public class Node{

        /** the data*/
        private V data;

        /** the list of edges thats connected to the node*/
        private ArrayList<Edge> edgeList;

        /** hte status of the node */
        private boolean visited = false;

        /** constructor of nested class Node*/
        public Node(V data){
            this.data = data;
            edgeList = new ArrayList<Edge>();
        }

        /** Accessor for node data */
        public V getData(){
            return data;
        }

        /** manipulator for node data */
        public void setData(V data){
            this.data = data;
        }

        /** mark the node as visited*/
        public void visited(){
            visited = true;
        }

        /** return true if the node is visited*/
        public boolean isVisited(){
            if (visited == true){
                return true;
            }
            else {
                return false;
            }
        }

        /** Returns a list of neighbors */
        public ArrayList<Node> getNeighbors(){
            ArrayList<Node> nodes = new ArrayList<Node>();
            for (Edge edge: edgeList){
                Node tail = edge.getTail();
                Node head = edge.getHead();
                if (tail != this){
                    nodes.add(tail);
                }
                else if (head != this){
                    nodes.add(head);
                }
            }
            return nodes;
        }

        /** Returns true if there is an edge to the node in question */
        public boolean isNeighbor(Node node){
            ArrayList<Node> neighbor = new ArrayList<Node> ();
            neighbor = this.getNeighbors();
            if (neighbor.contains(node)){
                return true;
            }
            else {
                return false;
            }
        }

        /** Returns the edge to a specified node, or null if there is none */
        public Edge edgeTo(Node neighbor){
            for (Edge edge: edgeList){
                if (neighbor == edge.oppositeTo(this)){
                    return edge;
                }
            }
            return null;
        }

        /** Adds an edge to the edge list */
        protected void addEdgeRef(Edge edge){
            edgeList.add(edge);
        }

        /** Removes an edge from the edge list */
        protected void removeEdgeRef(Edge edge){
            edgeList.remove(edge);
        }

        /** Accessor of the edge list */
        protected ArrayList<Edge> getEdgeRef(){
            return edgeList;
        }
    }// END OF CLASS NODE

}// END OF CLASS GRAPH
