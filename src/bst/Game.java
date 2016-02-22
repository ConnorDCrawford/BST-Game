package bst;

import simplegui.GUIListener;
import simplegui.SimpleGUI;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Game.java
 * BST Game
 *
 * Created by Connor Crawford on 10/24/15.
 */
public class Game implements GUIListener {

    private int[] sequence;
    private int current = 0;
    private BST bst;
    private SimpleGUI simpleGUI;

    public Game() {
        createNewGame();
    }

    private void createNewGame() {
        char difficulty = promptDifficulty();
        sequence = createSequence(difficulty);
        current = 0;
        if (simpleGUI == null) {
            simpleGUI = new SimpleGUI(1280, 549); // 21:9 aspect ratio
            simpleGUI.centerGUIonScreen();
            simpleGUI.registerToGUI(this);
            simpleGUI.labelButton1("Show Solution");
            simpleGUI.labelButton2("Reset Game");
        }
        else
            simpleGUI.eraseAllDrawables();
        bst = new BST(sequence, simpleGUI, this);
        shuffleArray(sequence);
        drawCurrentValue();
    }

    int getCurrentValue(Node node) {
        if (sequence == null)
            return 0;
        int value = sequence[current];
        current++;
        if (current < sequence.length)
            drawCurrentValue();
        else {
            // current == sequence.length, therefore user successfully completed entire sequence (win)
            node.drawGreen();
            didWin();
        }
        return value;
    }

    private char promptDifficulty() {
        String[] choices = {"Easy", "Medium", "Hard"};
        String difficulty = (String)(JOptionPane.showInputDialog(null, "Choose difficulty", "Difficulty", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]));
        if (difficulty == null)
            System.exit(0); // User cancelled out of screen
        switch (difficulty.charAt(0)) {
            case 'E':
                return 'E';
            case 'M':
                return 'M';
            case 'H':
                return 'H';
            default:
                return 'X';
        }

    }

    private void shuffleArray(int[] A) {
        Random random = new Random();
        for (int i = A.length - 1; i > 0; i--) {
            // Choose random index between 0 and 1 greater than current index
            int index = random.nextInt(i + 1);

            // Swap random index with current
            int t = A[index];
            A[index] = A[i];
            A[i] = t;
        }
    }

    private int[] createSequence(char difficulty) {
        int numNodes;

        // Determine range of sequence
        switch (difficulty) {
            case 'E':
                numNodes = 10;
                break;
            case 'M':
                numNodes = 20;
                break;
            case 'H':
                numNodes = 30;
                break;
            default:
                numNodes = 0;
                break;
        }

        // Create array and fill it with sequence
        int[] S = new int[numNodes];
        for (int i = 0; i < numNodes; i++)
            S[i] = i + 1;

        // Shuffle the sequence
        shuffleArray(S);

        // Prevent a sequence with depth greater than 6, as it would cause graphical issues
        BST bst = new BST(S);
        while (bst.getDepth() > 6) {
            shuffleArray(S);
            bst = new BST(S);
        }

        return S;
    }

    private void drawCurrentValue() {
        if (simpleGUI != null) {
            simpleGUI.eraseSingleDrawable("TEXT");
            simpleGUI.drawText("Current: " + sequence[current], 20, 20, Color.BLACK, 1, "TEXT");
        }
    }

    void didLose() {
        simpleGUI.eraseSingleDrawable("TEXT");
        simpleGUI.drawText("You lose!", 20, 20, Color.BLACK, 1, "TEXT");
        bst.showSolution();
        int shouldPlayAgain = JOptionPane.showConfirmDialog(null, "You lose!\nPlay again?");
        if (shouldPlayAgain == JOptionPane.YES_OPTION)
            createNewGame();
        else if (shouldPlayAgain == JOptionPane.NO_OPTION)
            System.exit(0);
    }

    private void didWin() {
        simpleGUI.eraseSingleDrawable("TEXT");
        simpleGUI.drawText("You win!", 20, 20, Color.GREEN, 1, "TEXT");
        bst.showSolution(); // Remove all nodes from SGMouseListener
        int shouldPlayAgain = JOptionPane.showConfirmDialog(null, "You win!!!\nPlay again?");
        if (shouldPlayAgain == JOptionPane.YES_OPTION)
            createNewGame();
        else if (shouldPlayAgain == JOptionPane.NO_OPTION)
            System.exit(0);
    }

    @Override
    public void reactToButton1() {
        didLose();
    }

    @Override
    public void reactToButton2() {
        createNewGame();
    }

    @Override
    public void reactToSwitch(boolean b) {

    }

    @Override
    public void reactToSlider(int i) {

    }
}
