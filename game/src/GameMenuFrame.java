
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameMenuFrame extends JFrame implements ActionListener
{
	private GamePanel gp;
	
	private JLabel title;
	
	private JButton[] buttons = new JButton[8];
	private String[] gameNames = { "fire", "octopus", "flagman", "turtle", "oil", "cement", "helmet", "chef" };
	
	private int[] yPoints = { 120, 240, 360, 480, 120, 240, 360, 480 };
	private int buttonWidth = 325;
	private int buttonHeight = 100;
	
	public GameMenuFrame()
	{
		super( "Game and Watch Collection" );
		
		setLayout( null );
		
		gp = new GamePanel();
		gp.setBounds( 0, 0, 800, 600 );
		gp.setVisible( false );
		
		title = new JLabel();
		title.setIcon( new ImageIcon( "GamePics/Icons/titleIcon.png" ) );
		title.setBounds( 50, 10, 700, 100 );
		
		for( int i = 0; i < buttons.length; i++ )
		{
			//if( i % 2 == 0)--else statement is temporarily in place to prevent exceptions
			//      		        **Will be removed upon completion of other icons**
			if( i % 2 == 0 )
			{
				buttons[i] = new JButton();
				buttons[i].setIcon( new ImageIcon( String.format( "GamePics/Icons/%sIcon.png", gameNames[i] ) ) );
				
				if( i <= 3 )
				{
					buttons[i].setBounds( 50, yPoints[i], buttonWidth, buttonHeight );
				}
				else
				{
					buttons[i].setBounds( 425, yPoints[i], buttonWidth, buttonHeight );
				}
			}
			else
			{
				buttons[i] = new JButton( gameNames[i] + " icon coming soon" );
				
				if( i <= 3 )
				{
					buttons[i].setBounds( 50, yPoints[i], buttonWidth, buttonHeight );
				}
				else
				{
					buttons[i].setBounds( 425, yPoints[i], buttonWidth, buttonHeight );
				}
			}
			
			buttons[i].addActionListener( this );
			add( buttons[i] );
		}
		
		add( title );
		add( gp );
	}

	@Override
	public void actionPerformed( ActionEvent event )
	{
		String gameToStart = new String();
		
		for( int i = 0; i < buttons.length; i++ )
		{
			if( event.getSource() == buttons[i] )
			{
				gameToStart = gameNames[i];
				
				gp.setGame( gameToStart );	
			}
			
			gp.setVisible( true );
			title.setVisible( false );
			buttons[i].setVisible( false );
		}
	}
}
