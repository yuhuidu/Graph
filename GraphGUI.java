import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** 
 *  graph gui
 *
 *  @author helen du
 * (based on lab11 wrttien by Nicholas R. Howe)
 *  @version 12/10/17
 */

public class GraphGUI {

    /** Label for the input mode instructions */
    private JLabel instr;

    /** The graph canvas*/
    private static GraphCanvas canvas = new GraphCanvas();

    /** The input mode */
    InputMode mode = InputMode.ADD_POINTS;

    /** Remembers point where last mousedown event occurred */
    private Point recorded;
    private Point recordedEdgeHead;
    private Point recordedEdgeTail;
    private static ArrayList<Point> nodeList = canvas.pointList();

    /**
     *  Schedules a job for the event-dispatching thread
     *  creating and showing this application's GUI.
     */
    public static void main(String[] args) {
        final GraphGUI GUI = new GraphGUI();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    GUI.createAndShowGUI();
                }
            });
    }

    /** Sets up the GUI window */
    public void createAndShowGUI() {
        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        JFrame frame = new JFrame("Graph GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add components
        createComponents(frame);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /** Puts content in the GUI window */
    public void createComponents(JFrame frame) {
        // graph display
        Container pane = frame.getContentPane();
        pane.setLayout(new FlowLayout());
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        canvas = new GraphCanvas();
        PointMouseListener pml = new PointMouseListener();
        canvas.addMouseListener(pml);
        canvas.addMouseMotionListener(pml);
        panel1.add(canvas);
        instr = new JLabel("Click to add new points; drag to move.");
        panel1.add(instr,BorderLayout.NORTH);
        pane.add(panel1);

        // controls
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(2,1));
        JButton addPointButton = new JButton("Add/Move Points");
        panel2.add(addPointButton);
        addPointButton.addActionListener(new AddPointListener());
        JButton rmvPointButton = new JButton("Remove Points");
        panel2.add(rmvPointButton);
        rmvPointButton.addActionListener(new RmvPointListener());
        JButton addEdgeButton = new JButton("Add Edges");
        panel2.add(addEdgeButton);
        addEdgeButton.addActionListener(new AddEdgeListener());
        JButton rmvEdgeButton = new JButton("Remove Edges");
        panel2.add(rmvEdgeButton);
        rmvEdgeButton.addActionListener(new RmvEdgeListener());
        JButton BFTButton = new JButton("BFT");
        panel2.add(BFTButton);
        BFTButton.addActionListener(new BFTListener());
        JButton DFTButton = new JButton("DFT");
        panel2.add(DFTButton);
        DFTButton.addActionListener(new DFTListener());
        JButton resetButton = new JButton("Reset BFT/DFT");
        panel2.add(resetButton);
        resetButton.addActionListener(new resetListener());
        pane.add(panel2);
    }

    /** 
     * Returns a point found within the drawing radius of the given location, 
     * or null if none
     * 
     */
    public Point findNearbyPoint(int x, int y) {
        for (Point point: nodeList){
            if (point.distance(x,y) <= 50){
                return point;
            }
        }
        return null;
    }

    /** Constants for recording the input mode */
    enum InputMode {
        ADD_POINTS, RMV_POINTS, ADD_EDGES, RMV_EDGES, DFT_NODE, BFT_NODE
    }

    //----------------------------ACTION LISTENERS----------------------
    /** Listener for AddPoint button */
    private class AddPointListener implements ActionListener {
        /** Event handler for AddPoint button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.ADD_POINTS;
            instr.setText("Click to add new points or change their location.");
        }
    }

    /** Listener for RmvPoint button */
    private class RmvPointListener implements ActionListener {
        /** Event handler for RmvPoint button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.RMV_POINTS;
            instr.setText("Click to remove points.");
        }
    }

    /** Listener for AddEdge button */
    private class AddEdgeListener implements ActionListener {
        /** Event handler for AddPoint button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.ADD_EDGES;
            instr.setText("Drag from one point to another to add a new edge.");
        }
    }

    /** Listener for RmvEdge button */
    private class RmvEdgeListener implements ActionListener {
        /** Event handler for RmvPoint button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.RMV_EDGES;
            instr.setText("Drag from one point to another to remove an edge.");
        }
    } 

    /** Listener for BFT button */
    private class BFTListener implements ActionListener {
        /** Event handler for BFT button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.BFT_NODE;
            instr.setText("Click a point to run BFT.");
        }
    }

    /** Listener for DFT button */
    private class DFTListener implements ActionListener {
        /** Event handler for DFT button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.DFT_NODE;
            instr.setText("Click a point to run DFT.");
        }
    } 

    /** Listener for reset button */
    private class resetListener implements ActionListener {
        /** Event handler for reset button */
        public void actionPerformed(ActionEvent e) {
            for (Graph<Point,Integer>.Edge edge: canvas.mine.getEdgeList()){
                edge.setColor(Color.BLACK);
            }
            canvas.repaint();
        }
    } // END OF ACTION LISTENERS

    //--------------------------MOUSE LISTENERS-------------------------------
    /** Mouse listener for PointCanvas element */
    private class PointMouseListener extends MouseAdapter
        implements MouseMotionListener {
        /** Responds to click event depending on mode */
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            Point point = new Point(x,y);
            Point found = findNearbyPoint(x,y);
            Graph<Point,Integer>.Node node = canvas.mine.getNode(found);
            switch (mode) {
            case ADD_POINTS:
                if (!nodeList.contains(point)){
                    canvas.mine.addNode(point);
                    nodeList.add(point);
                }
                Toolkit.getDefaultToolkit().beep();
                break;
            case RMV_POINTS:
                if (nodeList.contains(found)){
                    canvas.mine.removeNode(canvas.mine.getNode(found));
                }
                Toolkit.getDefaultToolkit().beep();
                break;
            case BFT_NODE:
                canvas.mine.BFT(node);
                break;
            case DFT_NODE:
                canvas.mine.DFT(node);
                break;
            }
            canvas.repaint();
        }

        /** Records point under mousedown event in 
            anticipation of possible drag */
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            recorded = findNearbyPoint(x,y);
            switch(mode){
            case ADD_EDGES:
                recordedEdgeHead = findNearbyPoint(x,y);
                break;
            case RMV_EDGES:
                recordedEdgeHead = findNearbyPoint(x,y);
                break;
            }
            canvas.repaint();
        }

        /** Responds to mouseup event */
        public void mouseReleased(MouseEvent e) {
            recorded = null;
            int x = e.getX();
            int y = e.getY();
            if (recordedEdgeHead != null){
                recordedEdgeTail = findNearbyPoint(x,y);
            }
            Graph<Point,Integer>.Node head
                = canvas.mine.getNode(recordedEdgeHead);
            Graph<Point,Integer>.Node tail
                = canvas.mine.getNode(recordedEdgeTail);
            switch (mode) {
            case ADD_EDGES:
                canvas.mine.addEdge(5, head, tail);
                break;
            case RMV_EDGES:
                canvas.mine.removeEdge(head, tail);
                break;
            }
            canvas.repaint();
        }

        /** Responds to mouse drag event */
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            switch(mode){
            case ADD_POINTS:
                if(recorded != null){
                    recorded.move(x,y);
                }
            }
            canvas.repaint();
        }

        public void mouseMoved(MouseEvent e) {}
    } // END OF MOUSE LISTENERS
}
