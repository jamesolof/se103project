import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Player 
{
	private int xPos;
	private int yPos;
	private ImageObserver imageObserver;
	private ArrayList<Image> images = new ArrayList<Image>();
	private Image currentImage;
	
	public Player( int x, int y, ImageObserver io, String typeOfGame )
	{
		setXPos( x );
		setYPos( y );
		setImageObserver( io );
		loadImages( typeOfGame );
	}
	
	public void setXPos( int x )
	{
		xPos = x;
	}
	
	public void setYPos( int y )
	{
		yPos = y;
	}
	
	public void setImageObserver( ImageObserver io )
	{
		imageObserver = io;
	}
	
	public void setCurrentImage( int imageIndex )
	{
		currentImage = images.get( imageIndex );
	}
	
	public void loadImages( String typeOfGame )
	{
		switch( typeOfGame )
		{
		case "oil":
			//load oil player images into 'images' arrayList
			break;
			
		case "cement":
			//load cement factory player images into 'images' arrayList
			break;
			
			//rest for other games
		}
	}
	
	public void draw( Graphics g )
	{
		//g.drawImage( currentImage, xPos, yPos, imageObserver );
		
		g.fillOval(xPos, yPos, 50, 50); 
	}
}
