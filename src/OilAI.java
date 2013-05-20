import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OilAI extends Sprite
{
	private BufferedImage[] images = new BufferedImage[ 4 ];

	private int column = 0;
	private boolean movingRight = true;
	private int frames = 0;
	private int framesBeforeUpdate = 75;
	
	public OilAI( int x, int y, ImageObserver io) 
	{
		super( x, y, io );
		
		loadImages();
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
	
	@Override
	public void loadImages()
	{
		try
		{
			for( int i = 0; i < images.length; i++ )
			{
				images[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/AI/oilAIColumn%d.png", i ) ) );
			}
		}
		
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}	
}
