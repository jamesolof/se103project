import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class OilPlayer extends Sprite
{
	private BufferedImage[] playerImages = new BufferedImage[ 5 ];
	
	public OilPlayer( int x, int y, ImageObserver io, BufferedImage[] pImages ) 
	{
		super( x, y, io );
		
		playerImages = pImages;
	}

	public void update( int column )
	{
		int[] playerXPoints = { 0, 146, 326, 516, 504 };
		int playerYPoint;
		
		if( column == 0 || column == 4 )
			playerYPoint = 220;
		else
			playerYPoint = 200;
		
		setXPos( playerXPoints[ column ] );
		setYPos( playerYPoint );
		
		setCurrentImage( playerImages[ column ] );
	}
}
