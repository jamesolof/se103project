import java.util.Random;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyAdapter;	
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

	public class Flagman extends JFrame {

		private JPanel panel;
		private ImageIcon one, oneSelect, two, twoSelect, three, threeSelect, four, fourSelect;
		private ImageIcon character, flag1, flag2, flag3, flag4;
		private JTextField livesAndScore;
		private int lives, score;
		Random random = new Random();
		private int flag;

		public Flagman() {
			super("Flagman");
			setLayout(null);

			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					int flagPressed = e.getKeyChar();

					if (flagPressed == flag) 
						gameContinue(true);
					else
						gameContinue(false);
				}
			});
			
			one = new ImageIcon(getClass().getResource("Images/1.jpg"));
			two = new ImageIcon(getClass().getResource("Images/2.jpg"));
			three = new ImageIcon(getClass().getResource("Images/3.jpg"));
			four = new ImageIcon(getClass().getResource("Images/4.jpg"));
			oneSelect = new ImageIcon(getClass().getResource("Images/1Select.jpg"));
			twoSelect = new ImageIcon(getClass().getResource("Images/2Select.jpg"));
			threeSelect = new ImageIcon(getClass().getResource("Images/3Select.jpg"));
			fourSelect = new ImageIcon(getClass().getResource("Images/4Select.jpg"));

			
		}
		
		public void play(){
			flag = 1 + random.nextInt(4);
			
		}
		
		public void gameContinue(boolean x){
			if (x == true){
				flag = 1 + random.nextInt(4);
			}
		}

	}

