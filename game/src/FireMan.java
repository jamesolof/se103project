import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class FireMan extends Sprite
{								
	private int[] xPoints = { 45, 78, 88, 100, 128, 80, 140, 160, 174, 194, 235, 260, 282, 330, 280, 360, 390, 430, 465, 485, 514, 465, 565, 600, 645 };
	
	private int[] yPoints = { 105, 160, 250, 314, 400, 485, 314, 250, 165, 110, 160, 250, 320, 400, 475, 320, 250, 160, 250, 320, 400, 485, 320, 270, 314 };
	
	private int index = 0;
	private int frames = 0;
	private int framesBeforeUpdate = 30;
	private boolean dead = false;
	
	private BufferedImage[] manImages = new BufferedImage[25];
	
	public FireMan( int x, int y, ImageObserver io, BufferedImage[] mImages )
	{
		super( x, y, io );
		
		manImages = mImages;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public int[] update( int score, int misses, int playerColumn )
	{
		//if another jumper should try to be spawned then spawnOne will be returned as 1, else 0 
		int spawnOne = 0;
		
		frames++;
		
		if( frames >= framesBeforeUpdate )
		{
			index++;
			frames = 0;
			
			if( dead )
			{
				index = 25;
				dead = false;
			}
			else
			{
				if( index == 5 ) 
				{
					if( playerColumn == 0 )
					{
						score++;
						index++;
					}
					else
					{
						misses++;
						dead = true;
					}
				}
				else if( index == 14 )
				{
					if( playerColumn == 1 )
					{
						score++;
						index++;
						spawnOne = 1;
					}
					else
					{
						misses++;
						dead = true;
					}
				}
				else if( index == 21 )
				{
					if( playerColumn == 2 )
					{
						score++;
						index++;
					}
					else
					{
						misses++;
						dead = true;
					}
				}
			}
		}
		
		if( index <= 24 )
		{
			setXPos( xPoints[ index ] );
			setYPos( yPoints[ index ] );
			setCurrentImage( manImages[ index ] );
		}
		
		int[] updates = { score, misses, spawnOne };
		
		return updates;
	}
}
