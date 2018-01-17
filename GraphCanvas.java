import java.lang.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;        

/** 
 *  graph canvas
 *
 *  @author helen du
 *  @version 12/10/17
 */

public class GraphCanvas extends JComponent {

    /** The graph*/
    Graph<Point, Integer> mine;

    /** get data from all nodes */
    public ArrayList<Point> pointList(){
        ArrayList<Point> point = new ArrayList<Point> ();
        ArrayList<Graph<Point,Integer>.Node> nodeList = mine.getNodeList();
        for (Graph<Point,Integer>.Node node: nodeList){
            point.add(node.getData());
        }
        return point;
    }

    /** Constructor */
    public GraphCanvas() {
        mine = new Graph<Point, Integer>();
        setMinimumSize(new Dimension(500,300));
        setPreferredSize(new Dimension(500,300));
    }

    /** paint component*/
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLUE);
        for (int i = 0; i < mine.numNodes(); i++){
            Point point = mine.getNode(i).getData();
            int x = (int) point.getX();
            int y = (int) point.getY();
            g.fillRect(x,y,10,10);
        }
        
        for (int i = 0; i < mine.numEdges(); i ++){
            Graph<Point,Integer>.Edge edge = mine.getEdge(i);
            g.setColor(edge.getColor());
            Graph<Point,Integer>.Node headNode = edge.getHead();
            Graph<Point,Integer>.Node tailNode = edge.getTail();
            Point head = headNode.getData();
            Point tail = tailNode.getData();
            int x1 = (int) head.getX();
            int y1 = (int) head.getY();
            int x2 = (int) tail.getX();
            int y2 = (int) tail.getY();
            g.drawLine(x1, y1, x2, y2);
        }
    }
}
 
