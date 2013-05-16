import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

public class GamePanel extends JPanel 
{
	//will contain one object of each game
	Game[] games = new Game[8];
	Game runningGame;
	
	boolean running = true;
	boolean paused = false;
	
	int counter = 0;
	
	Thread GameLoop = new Thread()
	{
		public void run()
		{
			refresh();
		}
	};
	
	public GamePanel()
	{
		super();
		
		setLayout( null );
		
		setFocusable( true );
		
		games[0] = new OilGame( this );
		
		addKeyListener( new KeyHandler() );
	}
	
	public void setGame( String gameToStart )
	{
		runningGame = games[0];
		
		GameLoop.start();
	}
	
	public void refresh()
	{
		long lastLoopTime = System.nanoTime();
		long oneSecondCheck = 0;
		final int desiredUpdateTime = 1000000000 / 60;
		
		while( running )
		{
			long now = System.nanoTime();
			long thisUpdateTime = now - lastLoopTime;
			lastLoopTime = now;
			
			oneSecondCheck = oneSecondCheck + thisUpdateTime;

			if( oneSecondCheck >= 1000000000 )
			{
				oneSecondCheck = 0;
			}
			
			if( !paused )
			{
				runningGame.update();
			}
			
			repaint();
			
			try 
			{
				Thread.sleep( ( lastLoopTime-System.nanoTime() + desiredUpdateTime ) / 1000000 );
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}	
		}
	}
	
	private class KeyHandler extends KeyAdapter
	{
		@Override
		public void keyPressed( KeyEvent e )
		{
			if( !runningGame.getGameOver() )
			{
				String key = KeyEvent.getKeyText( e.getKeyCode() );
			
				if( key.equalsIgnoreCase( "P" ) )
				{
					paused = !paused; 
				}
				else
				{
					runningGame.processKeyEvents( key );
				}
			}
		}
	}
	
	@Override
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		
		runningGame.draw( g );

		if( paused )
		{
			g.setColor( Color.WHITE );
			g.fillRect( 100, 100, 600, 400 );
			g.setColor( Color.BLACK );
			g.drawString( "PAUSED", 400, 300 );
		}
		else if( runningGame.getGameOver() )
		{
			g.setColor( Color.RED );
			g.drawString( "Game Over!", 400, 300 );
			
			//if player gets a score that is within the top ten scores
			    //draw a leaderboard with game name at the top, show their scores (and ask them to input 1st 3 initials) 
			    //and display the remaining 9 highest scores
			    //once 3 initials are input, pressing enter will return them to the game selection screen
			//else
			    //show a message that the player did not score within the top ten, and return them to the 
			    //game selection screen after some amount of time (somewhere between 5 and 15 seconds)
		}
	}
}
