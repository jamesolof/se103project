import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OilPlayer extends Sprite
{
	private BufferedImage[] playerImages = new BufferedImage[ 5 ];
	
	public OilPlayer( int x, int y, ImageObserver io) 
	{
		super( x, y, io );
		
		loadImages();
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
	
	@Override
	public void loadImages()
	{
		try 
		{
			for( int i = 0; i < playerImages.length; i++ )
			{
				playerImages[i] = ImageIO.read( new File( String.format( "bin/GamePics/Oil/Player/oilPlayer%d.png", i ) ) );
			}
		}
		
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
