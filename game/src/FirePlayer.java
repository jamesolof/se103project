import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class FirePlayer extends Sprite
{
	private BufferedImage[] playerImages = new BufferedImage[ 3 ];
	
	public FirePlayer( int x, int y, ImageObserver io, BufferedImage[] pImages )
	{
		super(x, y, io);
		
		playerImages = pImages;
	}
	
	public void update( int column )
	{
		int[] playerXPoints = { 70, 265, 460, };
		int playerYPoint = 400;
		
		setXPos( playerXPoints[ column ] );
		setYPos( playerYPoint );
		
		setCurrentImage( playerImages[ column ] );
	}
}