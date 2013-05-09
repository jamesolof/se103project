import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class OilGame extends Game
{
	private Random random = new Random();
	
	private BufferedImage background = null;
	
	private int bucketScore = 0;
	private BufferedImage[] bucketImages = new BufferedImage[ 3 ];
	
	private int currentPlayerColumn = 2;
	private BufferedImage playerThrowingRight = null;
	private BufferedImage playerThrowingLeft = null;
	
	
	private int currentCatcherColumn = 0;
	private boolean movingRight = true;
	private int catcherFrames = 0;
	private int catcherFramesBeforeUpdate = 75;
	
	private int[] oilXPoints = { 250, 400, 550 };
	private int[] oilYPoints = { 75, 150, 225, 300 }; 
	private int currentOilColumn = -1;
	private int currentOilRow = 0;
	private int oilFrames = 0;
	private int oilFramesBeforeUpdate = 100;
	private BufferedImage oilStartImage = null;
	private BufferedImage oilFallImage = null;
	
	private int score = 0;
	private int misses = 0;
	
	private int maxMisses = 3;
	
	private OilPlayer oilPlayer;
	
	public OilGame( ImageObserver io )
	{
		super( io );
		
		oilPlayer = new OilPlayer( 0, 0, io );
		oilPlayer.update( currentPlayerColumn );
		
		loadImages();
	}
	
	private void loadImages()
	{
		try
		{
			background = ImageIO.read( new File( "GamePics/Oil/oilBackground.png" ) );
			
			for( int i = 0; i < bucketImages.length; i++ )
			{
				bucketImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/Misc/oilBucket%d.png", i ) ) );
			}
			
			oilStartImage = ImageIO.read( new File( "GamePics/Oil/Misc/oilDropStart.png" ) );
			oilFallImage = ImageIO.read( new File( "GamePics/Oil/Misc/oilDrop.png" ) );
			playerThrowingLeft = ImageIO.read( new File( "GamePics/Oil/Misc/oilPlayerThrowing1.png" ) );
			playerThrowingRight = ImageIO.read( new File( "GamePics/Oil/Misc/oilPlayerThrowing2.png" ) );
		}
		
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
	
	private void updateOil()
	{
		oilFrames++;
		
		if( currentOilColumn == -1 )
		{
			currentOilColumn = random.nextInt( 3 );
			oilFrames = 0;
			currentOilRow = 0;
		}
		
		if( oilFrames >= oilFramesBeforeUpdate )
		{
			currentOilRow++;
			oilFrames = 0;
		}
		
		if( currentOilRow == 4 )
		{
			if( currentOilColumn == currentPlayerColumn - 1 && bucketScore <= 2 )
			{
				bucketScore++;
				score++;
				currentOilColumn = -1;
			}
			else
			{
				misses++;
				currentOilColumn = -1;
			}
			
			adjustOilSpeed();
			
			System.out.println( "Score: " + score + "\tMisses: " + misses + "\nBucket Score: " + bucketScore + "\nOil Fb4U: " + oilFramesBeforeUpdate );
		}
	}
	
	public void adjustOilSpeed()
	{
		if( score >= 15 && score < 30 )
			oilFramesBeforeUpdate = 95;
		else if( score >= 30 && score < 50 )
			oilFramesBeforeUpdate = 90;
		else if( score >= 50 && score < 70 )
			oilFramesBeforeUpdate = 80;
		else if( score >= 70 && score < 90 )
			oilFramesBeforeUpdate = 70;
		else if( score >= 90 && score < 115 )
			oilFramesBeforeUpdate = 60;
		else if( score >= 115 && score < 140 )
			oilFramesBeforeUpdate = 50;
		else if( score >= 140 && score < 165 )
			oilFramesBeforeUpdate = 40;
		else if( score >= 165 )
			oilFramesBeforeUpdate = 30;
	}
	
	private void updateAI()
	{
		catcherFrames++;
		
		if( catcherFrames >= catcherFramesBeforeUpdate )
		{
			if( currentCatcherColumn == 0 )
			{
				movingRight = true;
			}
			else if( currentCatcherColumn == 3 )
			{
				movingRight = false;
			}
			
			if( movingRight == true )
			{
				currentCatcherColumn++;
			}
			else
			{
				currentCatcherColumn--;
			}
			
			catcherFrames = 0;
		}
	}
	
	@Override
	public String getGameName() 
	{
		return "oil";
	}

	@Override
	public void update()
	{
		updateOil();
		updateAI();
	}

	@Override
	public void draw(Graphics g)
	{
		g.drawImage( background, 0, 0, getImageObserver() );
		
		oilPlayer.draw( g );
		
		if( currentOilColumn >= 0 )
		{
			g.fillOval( oilXPoints[currentOilColumn], oilYPoints[currentOilRow], 25, 25 );
		}
		
		//g.drawImage( )
	}

	@Override
	public void processKeyEvents( String key )
	{
		if( key.equals( "A" ) || key.equals( "Left" ) )
		{
			if( currentPlayerColumn > 1 )
			{
				currentPlayerColumn--;
			}
		}
		else if( key.equals( "D" ) || key.equals( "Right" ) )
		{
			if( currentPlayerColumn < 3 )
			{
				currentPlayerColumn++;
			}
		}
		else if( key.equals( "Space" ) )
		{
			if( currentPlayerColumn == 1 )
			{
				currentPlayerColumn = 0;
				
				if( currentCatcherColumn == 0 )
				{
					score = score + bucketScore;
					bucketScore = 0;
				}
				else
				{
					bucketScore = 0;
				}
			}
			else if( currentPlayerColumn == 3 )
			{
				currentPlayerColumn = 4;
				
				if( currentCatcherColumn == 3 )
				{
					score = score + bucketScore;
					bucketScore = 0;
				}
				else
				{
					bucketScore = 0;
				}
			}
		}
		
		oilPlayer.update( currentPlayerColumn );
		//oilPlayer.setCurrentImage( currentPlayerColumn );
	}
}
