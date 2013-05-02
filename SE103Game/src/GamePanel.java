import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public class GamePanel extends JPanel 
{
	AbstractGame game = null;
	
	public GamePanel()
	{
		super();
		
		setLayout( null );
		
		setFocusable( true );
		
		setSize( 800, 600 );

		addKeyListener( new KeyHandler() );
	}
	
	public void setGameType( )
	{
		
	}
	
	private class KeyHandler extends KeyAdapter
	{
		@Override
		public void keyPressed( KeyEvent e )
		{
			String key = KeyEvent.getKeyText( e.getKeyCode() );
			
		}
	}
	
	@Override
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );
	}
}
