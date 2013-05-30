import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class HelmetTools extends Sprite
{
	private int index;
	
	int[] xPoints = {-75, 15, 105,195, 285};
	int[] yPoints = { -150, -69, 12, 93, 175, 1000 };
	
	private BufferedImage[] toolImages = new BufferedImage[5];
	
	private int row = 0;
	private int frames = 0;
	private double framesBeforeUpdate;
	
	public HelmetTools(int x, int y, int toolIndex, ImageObserver io, BufferedImage[] tImages, double fBU ) 
	{
		super( x, y, io );
		
		framesBeforeUpdate = fBU;
		
		index = toolIndex;
		
		toolImages = tImages;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public int update(int score, int misses, int playerColumn )
	{		
		frames++;
		
		if( frames == framesBeforeUpdate )
		{
			frames = 0;
			
			row++;
			
			if(row == 4)
			{
				if( ( playerColumn - 1 ) == index)
					misses++;
				
				if(misses > 2)
					misses = 2;
			}
		}
		
		setXPos(xPoints[index]);
		setYPos(yPoints[row]);
		setCurrentImage(toolImages[index]);
		
		return misses;
	}
}
