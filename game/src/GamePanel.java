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
		/*
		for( int i = 0; i < games.length; i++ )
		{
			if( gameToStart.equals( games[i].getGameName() ) )
			{
				runningGame = games[i];
			}
		}
		*/
		
		Thread GameLoop = new Thread()
		{
			public void run()
			{
				refresh();
			}
		};
		
		GameLoop.start();
	}
	
	public void refresh()
	{
		long lastLoopTime = System.nanoTime();
		long oneSecondCheck = 0;
		final int desiredUpdateTime = 1000000000 / 60;
		int fps = 0;
		
		while( running == true )
		{
			long now = System.nanoTime();
			long thisUpdateTime = now - lastLoopTime;
			lastLoopTime = now;
			
			oneSecondCheck = oneSecondCheck + thisUpdateTime;
			fps++;
			
			if( oneSecondCheck >= 1000000000 )
			{
				oneSecondCheck = 0;
				fps = 0;
			}
			
			if( paused == false )
			{
				counter++;
				
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
	
	@Override
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		
		//Uncomment stuff between /* and */ to see effects of gameloop
		
		
		/*
		if( counter <= 20 )
		{
			g.setColor( Color.RED );
			g.fillRect( 0, 0, 800, 600 );
		}
		else if( counter > 20 && counter <= 40 )
		{
			g.setColor( Color.BLUE );
			g.fillRect( 0, 0, 800, 600 );
		}
		else if( counter > 40 && counter <= 62 )
		{
			g.setColor( Color.GREEN );
			g.fillRect( 0, 0, 800, 600 );
		}
		else
		{
			counter = 0;
		}
		*/
		
		runningGame.draw( g );
		
		
		if( paused == true )
		{
			counter = counter;
			
			g.setColor( Color.WHITE );
			g.fillRect( 100, 100, 600, 400 );
			g.setColor( Color.BLACK );
			g.drawString( "PAUSED", 400, 300 );
		}
	}
}
