import java.lang.*;
import java.util.*;

/** 
 *  test graph
 *
 *  @author helen du
 *  @version 12/4/17
 */

public class TestGraph {
    public static void main(String[] args) {
        Graph<String,Integer> mine = new Graph<String,Integer> ();

        Graph<String,Integer>.Node node1 = mine.addNode("1st node");
        Graph<String,Integer>.Node node2 = mine.addNode("2nd node");
        Graph<String,Integer>.Node node3 = mine.addNode("3rd node");
        Graph<String,Integer>.Node node4 = mine.addNode("4th node");
        Graph<String,Integer>.Node node5 = mine.addNode("5th node");

        Graph<String,Integer>.Edge edge1 = mine.addEdge(1, node1, node2);
        Graph<String,Integer>.Edge edge2 = mine.addEdge(2, node2, node3);
        Graph<String,Integer>.Edge edge3 = mine.addEdge(3, node3, node4);
        Graph<String,Integer>.Edge edge4 = mine.addEdge(4, node4, node5);
        Graph<String,Integer>.Edge edge5 = mine.addEdge(5, node5, node1);
        System.out.println("Graph after adding edges");
        mine.print();

        edge1.setDistance(1.0);
        edge2.setDistance(2.0);
        edge3.setDistance(3.0);
        edge4.setDistance(4.0);
        edge5.setDistance(5.0);

        mine.DFT(node1);
        mine.BFT(node1);
        mine.distances(node1);

    }
}
