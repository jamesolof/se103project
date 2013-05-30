
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameMenuFrame extends JFrame implements ActionListener
{
	private GamePanel gp;
	
	private JLabel title;
	
	private JButton[] buttons = new JButton[4];
	private String[] gameNames = { "fire", "flagman", "oil", "helmet" };
	
	private int[] yPoints = { 120, 360, 120, 360 };
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
		title.setIcon( new ImageIcon( "GamePics/Icons/MenuIcons/titleIcon.png" ) );
		title.setBounds( 50, 10, 700, 100 );
		
		for( int i = 0; i < buttons.length; i++ )
		{
			buttons[i] = new JButton();
			buttons[i].setIcon( new ImageIcon( String.format( "GamePics/Icons/MenuIcons/%sIcon.png", gameNames[i] ) ) );
				
			if( i < 2 )
				buttons[i].setBounds( 50, yPoints[i], buttonWidth, buttonHeight );
			else
				buttons[i].setBounds( 425, yPoints[i], buttonWidth, buttonHeight );
			
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
