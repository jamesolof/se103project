import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FlagmanPlayer extends Sprite
{
	private BufferedImage[] images = new BufferedImage[6];
	
	private int[] xPoints = { 169, 307, 220, 307, 169, 307 };
	private int[] yPoints = { 179, 172, 200, 200, 179, 200 };
	
	public FlagmanPlayer( int x, int y, ImageObserver io, BufferedImage[] pImages )
	{
		super( x, y, io);
		
		images = pImages;
	}
	
	public void update( int flag )
	{
		setXPos( xPoints[ flag ] );
		setYPos( yPoints[ flag ] );
		setCurrentImage( images[ flag ] );
	}
}
