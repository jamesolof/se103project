import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel 
{
	//will contain one object of each game
	Game[] games = new Game[4];
	Game runningGame;
	
	boolean running = true;
	boolean paused = false;

	boolean scoresSorted = false;
	
	private int scoreXPoint = 315;
	private int nameXPoint = 370;
	private int[] scoreRecordYPoints = { 241, 269, 296, 324, 351, 379, 407, 434, 462, 489 };
	
	private int thisIndex = -1;
	
	private BufferedImage gameOverScreen = null;
	
	StringBuilder initials = new StringBuilder();
	
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
		games[1] = new FireGame( this );
		games[2] = new HelmetGame( this );
		games[3] = new FlagmanGame( this );
		
		addKeyListener( new KeyHandler() );
		
		try
		{
			gameOverScreen = ImageIO.read( new File( "GamePics/Icons/GameOverScreen.png" ) );
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
	
	public void setGame( String gameToStart )
	{
		for( int i = 0; i < games.length; i++ )
		{
			if( games[i].getGameName() == gameToStart )
				runningGame = games[i];
		}
		
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
				Thread.sleep( Math.abs( ( lastLoopTime-System.nanoTime() + desiredUpdateTime ) / 1000000 ) );
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
			
			if( !runningGame.getGameOver() )
			{
				if( key.equalsIgnoreCase( "P" ) )
				{
					paused = !paused; 
				}
				else
				{
					runningGame.processKeyEvents( key );
				}
			}
			else
			{
				if( key.length() == 1 )
					initials.append( key );
				else if( key.equalsIgnoreCase( "backspace" ) && initials.length() > 0 )
					initials.deleteCharAt( initials.length() - 1 );
				
				if( initials.length() > 3 )
					initials.delete( 3, initials.length() );
				
				if( key.equals( "Enter" ) && thisIndex >= 0 )
					runningGame.writeHighScores( thisIndex, initials.toString() );
			}
		}
	}
	
	@Override
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		
		runningGame.draw( g );

		if( paused && !runningGame.getGameOver() )
		{
			g.setColor( Color.WHITE );
			g.fillRect( 100, 100, 600, 400 );
			g.setColor( Color.BLACK );
			g.drawString( "PAUSED", 400, 300 );
		}
		else if( runningGame.getGameOver() )
		{
			g.drawImage( gameOverScreen, 150, 100, this );
			
			if( scoresSorted == false )
			{
				thisIndex = runningGame.sortHighScores();
				scoresSorted = true;
			}
			
			runningGame.drawScores( g, scoreXPoint, nameXPoint, scoreRecordYPoints );
			
			if( thisIndex >= 0 && initials.toString() != null )
			{
				g.drawString( initials.toString(), nameXPoint, scoreRecordYPoints[ thisIndex ] );
			}
		}
	}
}
