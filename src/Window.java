import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window {
    private Grid grid;
    private final int height;
    private final int width;
    private final JFrame frame;
    private JLabel minesText;

    public Window(int height, int width){
        this.height = height;
        this.width = width;
        JPanel controlPanel = initializeControlPanel();
        frame = new JFrame("Minesweeper");
        frame.setLayout(new BorderLayout());
        frame.setSize(width, height + 100);
        frame.add(controlPanel, BorderLayout.NORTH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
    public void createGrid(int cols, int rows, double probability, int seed){
        if (this.grid != null) frame.remove(grid.getGrid());
        grid = new Grid(cols, rows, probability, seed);
        grid.getGrid().setSize(width, height);
        frame.add(grid.getGrid(), BorderLayout.CENTER);
        minesText.setText("Number of mines: " + grid.getMines());
        frame.setVisible(true);
    }

    private JPanel initializeControlPanel(){
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 16));
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel seedText = new JLabel("Seed: ");
        JTextField seedField = new JTextField("1234");
        seedField.setColumns(8);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> createGrid(10,10,0.1,Integer.parseInt(seedField.getText())));
        restartButton.setFocusPainted(false);

        minesText = new JLabel();

        controlPanel.add(restartButton);
        //controlPanel.add(Box.createHorizontalStrut(50));
        controlPanel.add(minesText, FlowLayout.CENTER);
        controlPanel.add(seedText);
        controlPanel.add(seedField);

        return controlPanel;
    }
}
