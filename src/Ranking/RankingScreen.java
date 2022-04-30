package Ranking;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RankingScreen extends JFrame {
	JButton button;
	JFrame screen;
	JLabel label;

	public static final int WIDTH = 650;
	public static final int HEIGHT = 650;

	public RankingScreen() {

		button = new JButton("Back");

		screen = new JFrame("SnakeGame v1.0");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		label = new JLabel("Hello World", JLabel.CENTER);
		label.setAlignmentX(0);
		label.setAlignmentY(0);
		add(button);
		add(label);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		RankingScreen window = new RankingScreen();
	}
}
