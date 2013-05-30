import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class HelmetPlayer extends Sprite
{
	private BufferedImage[] playerImages = new BufferedImage[ 7 ];
	
	public HelmetPlayer( int x, int y, ImageObserver io, BufferedImage[] pImages ) 
	{
		super( x, y, io );
		
		playerImages = pImages;
	}
	
	public void update( int runnerColumn )
	{
		int[] runnerXPoints = { -165, -75, 15, 105, 195, 285, 375};
		int runnerYPoint = 225;
		
		setXPos( runnerXPoints[ runnerColumn ] );
		setYPos( runnerYPoint );
		setCurrentImage( playerImages[ runnerColumn ] );
	}
}