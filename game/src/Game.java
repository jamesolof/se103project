import java.awt.Graphics;
import java.awt.image.ImageObserver;

//each game should extend Game 
public abstract class Game 
{
	public Game( ImageObserver io )
	{
		//image observer io is necessary for drawing player images and such
	}
	
	//implement method to return "fire", "octopus", "flagman", "turtle", "oil", "cement", "helmet" or "chef"
	//depending on game
	public abstract String getGameName();
	
	//update ALL game objects' (Player, AI, etc) here
	public abstract void update();
	
	//draw ALL game stuff here
	public abstract void draw( Graphics g );
	
	public abstract void processKeyEvents( String key );
}
