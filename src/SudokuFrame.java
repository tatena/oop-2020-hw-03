import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {

	 JTextArea left;
	 JTextArea right;
	 JButton check;
	 JCheckBox autoCheck;

	 public SudokuFrame() {
		super("Sudoku Solver");

		JPanel main = new JPanel();
		main.setLayout(new BorderLayout(4, 4));

		left = new JTextArea(15, 20);
		left.setBorder(new TitledBorder("Puzzle"));
		main.add(left, BorderLayout.WEST);

		right = new JTextArea(15, 20);
		right.setBorder(new TitledBorder("Solution"));
		main.add(right, BorderLayout.EAST);

		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BorderLayout());

		check = new JButton("Check");
		lowerPanel.add(check, BorderLayout.WEST);

		autoCheck = new JCheckBox("Auto Check", true);
		lowerPanel.add(autoCheck);

		this.add(main);
		this.add(lowerPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);

		addListeners();
	}

	 private void addListeners() {
		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				updateGUI();
			}
		});

		Document initial = left.getDocument();
		initial.addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent documentEvent) {
				updateGUI();
			}

			@Override
			public void removeUpdate(DocumentEvent documentEvent) {
				updateGUI();
			}

			@Override
			public void changedUpdate(DocumentEvent documentEvent) {
				updateGUI();
			}
		});

	 }

	 private void updateGUI() {
		 String initial = left.getText();
		 int[][] grid;
		 try {
		 	grid = Sudoku.textToGrid(initial);
		 } catch (RuntimeException ex) {
		 	right.setText("Parsing Problem :(");
		 	return;
		 }
		 Sudoku board = new Sudoku(grid);
		 int numSolutions = board.solve();
		 String solution = board.getSolutionText();
		 right.setText(solution);
		 right.append("\nSolutions: " + numSolutions + "\n");
		 right.append("Time elapsed: " + board.getElapsed() + "ms");

	 }


	 public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
