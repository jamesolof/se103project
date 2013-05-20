import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HelmetPlayer extends Sprite
{
	private BufferedImage[] runnerImages = new BufferedImage[ 8 ];
	
	public HelmetPlayer( int x, int y, ImageObserver io) 
	{
		super( x, y, io );
		
		loadImages();
	}
	
	public void update( int runnerColumn )
	{
		int[] runnerXPoints = { 0, 125, 250, 375, 500, 625, 750, 400  };
		int runnerYPoint;
		
		if( runnerColumn == 400)
			runnerYPoint = 600;
		else
			runnerYPoint = 575;
		
		setXPos( runnerXPoints[ runnerColumn ] );
		setYPos( runnerYPoint );
		setCurrentImage( runnerImages[ runnerColumn ] );
	}
	
	@Override
	public void loadImages()
	{
		try
		{
			for( int i = 0; i < runnerImages.length; i++ )
			{
				runnerImages[ i ] = ImageIO.read( new File( String.format( "bin/GamePics/Helmet/Player/Player%d.png", i ) ) );
			}
		}
		
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}	
}