import java.awt.Graphics;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public abstract class Player 
{
	private int xPos;
	private int yPos;
	
	//image observer is 'GamePanel'
	private ImageObserver imageObserver;
	
	//load images into this
	private ArrayList<Image> images = new ArrayList<Image>();
	
	//add method in each concrete class to determine the current image for rendering,
	//call method 'setCurrentImage( Image i )' inside that method for setting the image
	private Image currentImage;
	
	public Player( int x, int y, ImageObserver io )
	{
		setXPos( x );
		setYPos( y );
		loadImages();
		
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
	
	public void setCurrentImage( Image i )
	{
		currentImage = i;
	}
	
	public abstract void loadImages();
	
	public void draw( Graphics g )
	{
		//g.drawImage( currentImage, xPos, yPos, imageObserver );
		
		g.fillOval(xPos, yPos, 50, 50); 
	}
}
