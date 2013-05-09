import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class OilAI extends Sprite
{
	private BufferedImage[] catcherImages = new BufferedImage[ 4 ];
	
	public OilAI( int x, int y, ImageObserver io) 
	{
		super( x, y, io );
		
		loadImages();
	}

	public void update( int AIColumn )
	{
		int[] catcherXPoints = { 150, 350, 550, 650 };
		int catcherYPoint = 550;
	}
	
	@Override
	public void loadImages()
	{
		try
		{
			for( int i = 0; i < catcherImages.length; i++ )
			{
				catcherImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/AI/oilAIColumn%d.png", i ) ) );
			}
		}
		
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}	
}
