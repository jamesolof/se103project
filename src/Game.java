import java.awt.Graphics;

import java.awt.image.ImageObserver;

//each game should extend Game 
public abstract class Game 
{
	private ImageObserver observer;
	private boolean gameOver;
	
	public Game( ImageObserver io )
	{
		observer = io;
		
		gameOver = false;
		//image observer io is necessary for drawing player images and such
		//see 'OilGame' constructor to see what to do with ImageObserver io
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
	
	//implement method to return "fire", "octopus", "flagman", "turtle", "oil", "cement", "helmet" or "chef"
	//depending on game
	public abstract String getGameName();
	
	//update ALL game objects' (Sprite, AI, etc) here
	public abstract void update();
	
	//draw ALL game stuff here
	public abstract void draw( Graphics g );
	
	public abstract void processKeyEvents( String key );
}
