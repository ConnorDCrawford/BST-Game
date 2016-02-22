package bst;

import simplegui.SimpleGUI;

import java.util.Stack;

/**
 * BST.java
 * BST Game
 *
 * Created by Connor Crawford on 10/22/15.
 */
public class BST {

    private Node root;
    private Game game;
    private SimpleGUI simpleGUI;

    public BST(int[] sequence) {
        createFromNumberSequence(sequence);
    }

    public BST(int[] sequence, SimpleGUI simpleGUI, Game game) {
        this.simpleGUI = simpleGUI;
        this.game = game;
        createFromNumberSequence(sequence);
        visualize(simpleGUI);
    }

    // Iteratively adds a node to BST
    private void add(int value){
        if (this.root == null) {
            this.root = new Node(value, simpleGUI, game);
            return;
        }
        Node root = this.root;
        while (root != null) {
            if (value < root.getValue()) {
                if (root.getLeft() == null) {
                    root.setLeft(new Node(value, simpleGUI, game));
                    return;
                } else
                    root = root.getLeft();
            } else if (value > root.getValue()){
                if (root.getRight() == null) {
                    root.setRight(new Node(value, simpleGUI, game));
                    return;
                } else
                    root = root.getRight();
            }
        }
    }
    // Adds all numbers from an array to BST
    private void createFromNumberSequence(int[] A){
        for (int num : A)
            add(num);
    }

    // Iteratively traverses and visualizes all nodes in BST
    private void visualizeNodes() {
        if (this.root != null) {
            Node root = this.root;
            Stack<Node> nodeStack = new Stack<>();
            nodeStack.push(root);
            while (!nodeStack.isEmpty()) {
                root = nodeStack.pop();
                root.visualize();
                if (root.getRight() != null)
                    nodeStack.push(root.getRight());
                if (root.getLeft() != null)
                    nodeStack.push(root.getLeft());
            }
        }
    }

    // Iteratively traverse tree and show each node's value
    public void showSolution() {
        if (this.root != null) {
            Node root = this.root;
            Stack<Node> nodeStack = new Stack<>();
            nodeStack.push(root);
            while (!nodeStack.isEmpty()) {
                root = nodeStack.pop();
                simpleGUI.removeFromMouse(root); // remove node from mouse listener since we don't need to register clicks anymore
                if (!root.isFilled())
                    root.drawValue();
                if (root.getRight() != null)
                    nodeStack.push(root.getRight());
                if (root.getLeft() != null)
                    nodeStack.push(root.getLeft());
            }
        }
    }

    int getDepth() {
        return getDepth(root);
    }

    // recursively finds the maximum height (depth) of the BST
    private int getDepth(Node root) {
        if (root == null)
            return 0;
        int leftDepth = getDepth(root.getLeft());
        int rightDepth = getDepth(root.getRight());
        return leftDepth > rightDepth ? leftDepth + 1 : rightDepth + 1;
    }

    // Recursively traverse nodes in pre-order and assigns an x and y value to it
    private void assignNodeCoordinates(Node root, int offsetX, int offsetY){
        if (root != null) {
            if (root.getLeft() != null) {
                root.getLeft().x = root.x - offsetX;
                root.getLeft().y = root.y + offsetY;
                assignNodeCoordinates(root.getLeft(), offsetX / 2, offsetY);
            }
            if (root.getRight() != null) {
                root.getRight().x = root.x + offsetX;
                root.getRight().y = root.y + offsetY;
                assignNodeCoordinates(root.getRight(), offsetX / 2, offsetY);
            }

        }
    }

    // Calculates maximum x offset to allow for all nodes to fit
    private int getOffsetX(int width, int radius) {
        return (width - (radius * 4)) / 4;
    }

    // Calculates maximum y offset to allow for all nodes to fit
    private int getOffsetY(int depth, int height, int radius) {
        return (height - (radius * 2)) / (depth - 1);
    }

    private void visualize(SimpleGUI simpleGUI) {
        if (root != null) {
            int depth = getDepth(this.root);
            int offsetX = getOffsetX(simpleGUI.getWidth(), Node.RADIUS);
            int offsetY = getOffsetY(depth, simpleGUI.getHeight() - 10, Node.RADIUS);
            this.root.x = simpleGUI.getWidth() / 2; // Center root
            this.root.y = 10; // Bring root down 10 pixels
            assignNodeCoordinates(this.root, offsetX, offsetY);
            visualizeNodes();
        }
    }
}
