import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

//each game should extend Game 
public abstract class Game 
{
	private Scanner readHighScores;
	private Formatter writeHighScores;
	private int score;
	
	private ArrayList<HighScoreRecord> highScores = new ArrayList<HighScoreRecord>();
	
	private ImageObserver observer;
	private boolean gameOver;
	
	public Game( ImageObserver io )
	{
		observer = io;
		
		gameOver = false;
		//image observer io is necessary for drawing player images and such
		//see 'OilGame' constructor to see what to do with ImageObserver io
		
		try 
		{
			readHighScores = new Scanner( new File( String.format( "TextFiles/%sScores.txt", getGameName() ) ) );
			
			while( readHighScores.hasNext() )
			{
				HighScoreRecord rec = new HighScoreRecord( 0, "   ");
				
				rec.setScore( readHighScores.nextInt() );
				rec.setName( readHighScores.next() );
				
				highScores.add( rec ); 
			}
			
			if( readHighScores != null )
				readHighScores.close();
		} 
		
		catch( FileNotFoundException e ) 
		{
			e.printStackTrace();
		}
	}
	
	public void setScore( int s )
	{
		score = s;
	}
	
	public int getScore()
	{
		return score;	
	}
	
	public int sortHighScores()
	{
		System.out.println( getScore() );
		
		HighScoreRecord rec = new HighScoreRecord( getScore(), "   " );
		
		System.out.println( rec.toString() );
		
		highScores.add( rec );
		
		HighScoreRecord temp;
		
		for( int i = 0; i < highScores.size(); i++ )
		{
			temp = highScores.get( i );
			
			for( int j = i; j < highScores.size(); j++ )
			{
				if( temp.getScore() < highScores.get( j ).getScore() )
				{
					temp = highScores.get( j );
				}
			}
			
			highScores.set( i, temp );
		}
		
		while( highScores.size() > 10 )
		{
			highScores.remove( 10 );
		}
		
		return highScores.indexOf( rec );
	}
	
	public void writeHighScores( int index, String name )
	{
		highScores.get( index ).setName( name );
		
		try
		{
			writeHighScores = new Formatter( String.format( "TextFiles/%sScores.txt", getGameName() ) );
			
			for( int i = 0; i < highScores.size(); i++ )
			{
				highScores.get( i ).toString();
			}
			
			if( writeHighScores != null )
				writeHighScores.close();
		}
		
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
	
	public ImageObserver getImageObserver()
	{
		return observer;
	}
	
	public void setGameOver( boolean b )
	{
		gameOver = b;
	}
	
	public boolean getGameOver()
	{
		return gameOver;
	}
	
	public void drawScores( Graphics g, int x1, int x2, int[] yPoints )
	{
		g.setFont( new Font( "Sansserif", Font.BOLD, 18 ) );
		g.setColor( Color.BLACK );
		
		for( int i = 0; i < highScores.size(); i++ )
		{
			if( highScores.get( i ).getScore() >= 0 )
				g.drawString( String.format( "%s", highScores.get( i ).getScore() ), x1, yPoints[ i ] );
			
			if( highScores.get( i ).getName() != null )
				g.drawString( highScores.get( i ).getName(), x2, yPoints[ i ] );
		}
	}
	
	//implement method to return "fire", "octopus", "flagman", "turtle", "oil", "cement", "helmet" or "chef"
	//depending on game
	public abstract String getGameName();
	
	//update ALL game objects' (Sprite, AI, etc) here
	public abstract void update();
	
	//draw ALL game stuff here
	public abstract void draw( Graphics g );
	
	public abstract void processKeyEvents( String key );
}
