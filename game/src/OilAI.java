import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class OilAI extends Sprite
{
	private BufferedImage[] images = new BufferedImage[ 4 ];

	private int column = 0;
	private boolean movingRight = true;
	private int frames = 0;
	private int framesBeforeUpdate = 75;
	
	public OilAI( int x, int y, ImageObserver io, BufferedImage[] cImages ) 
	{
		super( x, y, io );
		
		images = cImages;
	}

	public int getColumn()
	{
		return column;
	}
	
	public void update()
	{
		int[] xPoints = { 170, 276, 406, 512 };
		int[] yPoints = { 452, 440, 440, 452 };
		
		frames++;
		
		if( frames == framesBeforeUpdate )
		{
			if( column == 0 || column == 3 )
			{
				movingRight = !movingRight;
			}
				
			if( movingRight )
				column--;
			else
				column++;
				
			frames = 0;
		}
		
		setXPos( xPoints[ column ] );
		setYPos( yPoints[ column ] );
		setCurrentImage( images[ column ] );
	}	
}
