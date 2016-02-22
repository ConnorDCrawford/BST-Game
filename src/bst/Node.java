package bst;

import simplegui.AbstractDrawable;
import simplegui.SGMouseListener;
import simplegui.SimpleGUI;

import java.awt.*;

/**
 * Node.java
 * BST Game
 *
 * Created by Connor Crawford on 10/22/15.
 */
public class Node implements SGMouseListener {

    // Constants
    public static final int RADIUS = 14;
    private static final String LINE = "LINE";
    private static final String BORDER = "BORDER";
    private static final String FILL = "FILL";
    private static final String TEXT = "TEXT";

    // Variables
    int x = 0, y = 0;
    private int value;
    private boolean isFilled = false;
    private Node left, right;
    private Game game;
    private SimpleGUI simpleGUI;

    public Node(int value, SimpleGUI simpleGUI, Game game){
        this.value = value;
        if (simpleGUI != null) {
            this.simpleGUI = simpleGUI;
            this.simpleGUI.registerToMouse(this);
        }
        if (game != null)
            this.game = game;
    }

    // Getters
    public int getValue() {
        return value;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public boolean isFilled() {
        return isFilled;
    }

    // Setters
    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    // Draws the node's value in the center of node's circle
    void drawValue() {
        if (simpleGUI.getDrawable(value + TEXT) == null)
            simpleGUI.drawText(value + "", x - RADIUS + 5, y + RADIUS + 5, Color.BLACK, 1, value + TEXT);
    }

    // Draws the node
    void visualize(){
        if (simpleGUI != null) {
            Color border = Color.BLUE, fill = Color.CYAN;
            if (this.right != null)
                simpleGUI.drawLine(x, y + RADIUS, right.x, right.y + RADIUS, border, 1.0, 1, right.value + LINE);
            if (this.left != null)
                simpleGUI.drawLine(x, y + RADIUS, left.x, left.y + RADIUS, border, 1.0, 1, left.value + LINE);
            simpleGUI.drawFilledEllipse(x - RADIUS, y, RADIUS * 2, RADIUS * 2, fill, 1.0, value + FILL);
            simpleGUI.drawEllipse(x - RADIUS, y, RADIUS * 2, RADIUS * 2, border, 1.0, 2, value + BORDER);
        }
    }

    // Returns true if the specified point is within the node's circle, false otherwise
    private boolean isInside(int x, int y){
        int centerX = this.x , centerY = this.y + RADIUS;
        // Use Pythagorean theorem to see if (point's distance from center)^2 < radius^2
        return (Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2)) < Math.pow(RADIUS, 2);
    }

    // Redraws node as green
    public void drawGreen() {
        AbstractDrawable line = simpleGUI.getDrawable(value + LINE);
        AbstractDrawable border = simpleGUI.getDrawable(value + BORDER);
        AbstractDrawable fill = simpleGUI.getDrawable(value + FILL);
        Color borderC = new Color(64, 128, 0), fillC = Color.GREEN;
        if (line != null)
            line.color = borderC;
        if (border != null)
            border.color = borderC;
        if (fill != null)
            fill.color = fillC;
        simpleGUI.repaintPanel();
    }
    // Redraws node as red
    private void drawRed() {
        AbstractDrawable line = simpleGUI.getDrawable(value + LINE);
        AbstractDrawable border = simpleGUI.getDrawable(value + BORDER);
        AbstractDrawable fill = simpleGUI.getDrawable(value + FILL);
        Color borderC = new Color(128, 0, 0), fillC = Color.RED;
        if (line != null)
            line.color = borderC;
        if (border != null)
            border.color = borderC;
        if (fill != null)
            fill.color = fillC;
        simpleGUI.repaintPanel();
    }

    @Override
    public void reactToMouseClick(int i, int i1) {
        if (!isFilled && isInside(i, i1)){
            drawValue();
            isFilled = true;
            if (game.getCurrentValue(this) == value)
                drawGreen();
            else {
                drawRed();
                game.didLose();
            }
        }
    }
}
