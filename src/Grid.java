import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Grid {
    private final int cols;
    private final int rows;
    private final Cell[][] grid;
    private final JPanel gridPanel;
    private int mines;

    public Grid(int cols, int rows, double probability, int seed) {

        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 32));

        this.grid = new Cell[rows][cols];
        this.cols = cols;
        this.rows = rows;
        Random gen = new Random(seed);
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(rows, cols));
        // Create new cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell(gen.nextDouble() < probability, i, j);
                int finalJ = j;
                int finalI = i;
                grid[i][j].addActionListener(e -> openCell(finalI, finalJ));
                gridPanel.add(grid[i][j].getButton());
            }
        }
        // Count mines around every cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int acc = 0;
                if (isMine(i-1, j-1)) acc++;
                if (isMine(i-1, j)) acc++;
                if (isMine(i-1, j+1)) acc++;
                if (isMine(i, j-1)) acc++;
                if (isMine(i, j+1)) acc++;
                if (isMine(i+1, j-1)) acc++;
                if (isMine(i+1, j)) acc++;
                if (isMine(i+1, j+1)) acc++;

                grid[i][j].setMinesAround(acc);
            }
        }
        // Count total number of mines
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                if (isMine(i, j)) mines++;
            }
        }
    }
    private boolean isMine(int i, int j){
        if (!isValid(i, j, rows, cols)) return false;
        return grid[i][j].isMine();
    }
    public JPanel getGrid() {
        return gridPanel;
    }

    public void openCell(int i, int j) {
        if (!isValid(i, j, rows, cols) || grid[i][j].isOpened()) return;
        int status = grid[i][j].open();
        if (status == -1) gameOver();
        else if (status == 0) {
            openCell(i-1, j-1);
            openCell(i-1, j);
            openCell(i-1, j+1);
            openCell(i, j-1);
            openCell(i, j+1);
            openCell(i+1, j-1);
            openCell(i+1, j);
            openCell(i+1, j+1);
        }
    }

    public void gameOver() {
        revealGrid();
    }

    public boolean isValid(int i, int j, int rows, int cols) {
        return (i < rows && i >= 0 && j < cols && j >= 0);
    }
    public void revealGrid(){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                if (!grid[i][j].isOpened()) grid[i][j].open();
            }
        }
    }
    public int getMines(){
        return mines;
    }
}

class Cell {
    private final boolean isMine;
    private final int i;
    private final int j;
    private final JButton btn;
    private int minesAround;
    private boolean isOpened = false;
    public Cell(boolean isMine, int i, int j) {
        this.isMine = isMine;
        this.i = i;
        this.j = j;
        btn = new JButton();
        btn.setFocusPainted(false);
    }

    public void setMinesAround(int minesAround) {
        this.minesAround = minesAround;
    }

    public void addActionListener(ActionListener l) {
        btn.addActionListener(l);
    }

    public int open() {
        btn.setEnabled(false);
        isOpened = true;
        if (isMine) {
            btn.setText("X");
            btn.setBackground(Color.red);
            return -1;
        } else {
            btn.setText(String.valueOf(minesAround));
            btn.setUI(new MetalButtonUI() {
                protected Color getDisabledTextColor() {
                    switch(minesAround){
                        case 0:
                            return Color.gray;
                        case 1:
                            return Color.blue;
                        case 2:
                            return Color.green;
                        case 3:
                            return Color.red;

                    }
                    return Color.BLUE;
                }
            });
            return minesAround;
        }
    }

    public JButton getButton() {
        return btn;
    }
    public boolean isOpened(){
        return isOpened;
    }
    public boolean isMine(){
        return isMine;
    }

}