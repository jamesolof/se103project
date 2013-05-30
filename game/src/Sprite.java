import java.awt.Graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Sprite 
{
	private int xPos;
	private int yPos;
	
	//image observer is 'GamePanel'
	private ImageObserver imageObserver;
	
	//add method in each concrete class to determine the current image for rendering,
	//call method 'setCurrentImage( Image i )' inside that method for setting the image
	private BufferedImage currentImage;
	
	public Sprite( int x, int y, ImageObserver io )
	{
		setXPos( x );
		setYPos( y );
		
		imageObserver = io;
	}
	
	public void setXPos( int x )
	{
		xPos = x;
	}
	
	public void setYPos( int y )
	{
		yPos = y;
	}
	
	public void setCurrentImage( BufferedImage i )
	{
		currentImage = i;
	}
	
	public void draw( Graphics g )
	{
		g.drawImage( currentImage, xPos, yPos, imageObserver );
	}
}
