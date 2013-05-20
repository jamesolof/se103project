import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class OilDrop extends Sprite 
{
	private Random random = new Random();
	
	private BufferedImage startImage = null;
	private BufferedImage fallImage = null;

	private int column;
	private int lastColumn;
	private int row = 0;
	private int frames = 0;
	private double framesBeforeUpdate = 90;

	public OilDrop(int x, int y, ImageObserver io) 
	{
		super( x, y, io );
		
		column = random.nextInt( 3 );
		loadImages();
	}

	public int getColumn()
	{
		return column;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int[] update( int score, int bucketScore, int misses, int playerColumn )
	{
		int[] xPointsStart = { 162, 341, 528 };
		int[] xPointsFall = { 172, 351, 538 };
		int[] yPoints = { 43, 90, 130, 172 };
		
		frames++;
		
		if( frames == framesBeforeUpdate )
		{
			frames = 0;
			
			row++;	
			
			if( row == 4 )
			{
				if( column == playerColumn - 1 && bucketScore <= 1 )
				{
					bucketScore++;
					score++;
					adjustSpeed( score );
				}
				else
				{
					if( misses <= 1 )
						misses++;
				}
				
				lastColumn = column;
				
				column = random.nextInt( 3 );
				
				while( column == lastColumn )
					column = random.nextInt( 3 );
				
				row = 0;
			}
			
			if( row == 0 )
			{
				setCurrentImage( startImage );
				setXPos( xPointsStart[ column ] );
			}
			else
			{
				setCurrentImage( fallImage );
				setXPos( xPointsFall[ column ] );
			}
				
			setYPos( yPoints[ row ] );
		}
		
		int[] updates = { score, bucketScore, misses };
		
		return updates;
	}

	
	public void adjustSpeed( int score )
	{
		framesBeforeUpdate = Math.round( framesBeforeUpdate - (score*.02) );
		
		if( framesBeforeUpdate < 30 )
			framesBeforeUpdate = 30;
	}
	
	@Override
	public void loadImages() 
	{
		try 
		{
			startImage = ImageIO.read( new File( "GamePics/Oil/Misc/oilDropStart.png" ) );
			fallImage = ImageIO.read( new File( "GamePics/Oil/Misc/oilDrop.png" ) );
		} 
		
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
