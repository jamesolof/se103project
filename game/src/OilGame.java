import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.Random;


public class OilGame extends Game
{
	private Random random = new Random();
	
	private int bucketScore = 0;
	
	private int[] playerXPoints = { 150, 250, 400, 550, 650 };
	private int playerYPoint = 400;
	private int currentPlayerColumn = 2;
	
	private int[] catcherXPoints = { 150, 350, 550, 650 };
	private int catcherYPoint = 550;
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
	
	private int score = 0;
	private int misses = 0;
	
	private int maxMisses = 3;
	
	private Player oilPlayer;
	
	public OilGame( ImageObserver io )
	{
		super( io );
		
		oilPlayer = new Player( playerXPoints[currentPlayerColumn], playerYPoint, io, getGameName() );
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
			
			System.out.println( "Score: " + score + "\nBucket Score: " + bucketScore + "\nMisses: " + misses );
		}
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
		oilPlayer.draw( g );
		
		if( currentOilColumn >= 0 )
		{
			g.fillOval( oilXPoints[currentOilColumn], oilYPoints[currentOilRow], 25, 25 );
		}
		
		g.fillOval( catcherXPoints[ currentCatcherColumn ], catcherYPoint, 50, 50 );
		//g.drawImage( )
	}

	@Override
	public void processKeyEvents( String key )
	{
		switch( key )
		{
		case "A":
		case "Left":
			if( currentPlayerColumn > 1 )
			{
				currentPlayerColumn--;
			}
			break;
			
		case "D":
		case "Right":
			if( currentPlayerColumn < 3 )
			{
				currentPlayerColumn++;
			}
			break;
			
		case "Space":
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
			break;
		}
		oilPlayer.setXPos( playerXPoints[ currentPlayerColumn ] );
		oilPlayer.setYPos( playerYPoint );
		//oilPlayer.setCurrentImage( currentPlayerColumn );
	}
}
