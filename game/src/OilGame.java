import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OilGame extends Game
{
	private BufferedImage background = null;
	
	private BufferedImage[] scoreNumbers = new BufferedImage[ 10 ];
	private int hundredsIndex = 0;
	private int tensIndex = 0;
	private int onesIndex = 0;
	
	private BufferedImage[] girlImages = new BufferedImage[ 5 ];
	private boolean gAngry = false;
	private int gImage = 0;
	private int gCycle = 0;
	private int gFrames = 0;
	private int girlXPoint1 = 586;
	private int girlXPoint2 = 610;
	private int girlYPoint1 = 514;
	private int girlYPoint2 = 484;
	
	private BufferedImage[] manImages = new BufferedImage[ 5 ];
	private boolean mAngry = false;
	private int mImage = 0;
	private int mCycle = 0;
	private int mFrames = 0;
	private int manXPoint1 = 130;
	private int manXPoint2 = 90;
	private int manYPoint = 511;

	private int mgFramesBeforeUpdate = 40;
	
	private BufferedImage[] officerImages = new BufferedImage[ 2 ];
	
	private BufferedImage[] bucketImages = new BufferedImage[ 3 ];
	private int bucketScore = -1;
	private int[] bucketXPoints = { 157, 338, 528 };
	private int bucketYPoint = 208;
	
	private BufferedImage[] oilMissesImages = new BufferedImage[ 3 ];
	private int oilMisses = -1;
	private int oilMissXPoint = 670;
	private int oilMissYPoint = 55;
	
	private BufferedImage[] throwMissesImages = new BufferedImage[ 3 ];
	private int throwMisses = -1;
	private int throwMissXPoint = 616;
	private int throwMissYPoint = 315;
	
	private BufferedImage[] throwLeftImages = new BufferedImage[ 2 ];
	private int[] throwLeftXPoints = { 183, 160 };
	private int[] throwLeftYPoints = { 418, 442 };
	
	private BufferedImage[] throwRightImages = new BufferedImage[ 2 ];
	private int[] throwRightXPoints = { 590, 580 };
	private int[] throwRightYPoints = { 418, 442 };
	
	private int throwIndex = -1;
	private int throwFrames = 0;
	private int throwFramesBeforeUpdate = 18;
	
	private OilPlayer player;
	private OilAI catcher;
	private OilDrop oil;
	
	private int maxMisses = 2;
	private int playerColumn = 2;
	private int score = 0;
	
	private boolean leftCatch = false;
	private boolean rightCatch = false;
	private boolean wasThrownLeft = false;
	
	public OilGame( ImageObserver io )
	{
		super( io );
		
		player = new OilPlayer( 0, 0, io );
		player.update( playerColumn );
		
		catcher = new OilAI( 0, 0, io );
		
		oil = new OilDrop( 0, 0, io );
		
		loadImages();
	}
	
	private void loadImages()
	{
		try
		{
			background = ImageIO.read( new File( "GamePics/Oil/oilBackground.png" ) );
			
			for( int i = 0; i < scoreNumbers.length; i++ )
			{
				scoreNumbers[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/Misc/number%d.png", i ) ) );
				
				if( i < 5 )
				{
					manImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/Misc/oilMan%d.png", i ) ) );
					girlImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/Misc/oilGirl%d.png", i ) ) );
				}
				
				if( i < 3 )
				{
					bucketImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/Misc/oilBucket%d.png", i ) ) );
					oilMissesImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/Misc/oilMiss%d.png", i ) ) );
					throwMissesImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/Misc/throwMiss%d.png", i ) ) );
				}
				
				if( i < 2 )
				{
					officerImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/Misc/gameOverOfficer%d.png", i ) ) );
					throwLeftImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/Misc/throwLeft%d.png", i ) ) );
					throwRightImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Oil/Misc/throwRight%d.png", i ) ) );
				}
			}
		}
		
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public String getGameName() 
	{
		return "oil";
	}

	public void setScoreImage()
	{
		if( score >= 100 )
		{
			hundredsIndex = score / 100;
			tensIndex = score % 100 / 10;
			onesIndex = score % 100 % 10;
		}
		else if( score < 100 && score >= 10 )
		{
			tensIndex = score / 10;
			onesIndex = score % 10;
		}
		else
		{
			onesIndex = score;
		}
	}
	
	public void animateMan()
	{
		mFrames++;
		
		if( mFrames == mgFramesBeforeUpdate )
		{
			if( mImage < 3 )
				mImage++;
			else if( mImage == 3 )
				mImage = 4;
			else
			{
				mCycle++;
				mImage = 3;
			}
				
			mFrames = 0;
		}
		
		if( mCycle == 6 )
		{
			mCycle = 0;
			mFrames = 0; 
			mImage = 0;
			mAngry = false;
		}
	}
	
	public void animateGirl()
	{
		gFrames++;
		
		if( gFrames == mgFramesBeforeUpdate )
		{
			if( gImage < 3 )
				gImage++;
			else if( gImage == 3 )
				gImage = 4;
			else
			{
				gCycle++;
				gImage = 3;
			}
				
			gFrames = 0;
		}
		
		if( gCycle == 6 )
		{
			gCycle = 0;
			gFrames = 0; 
			gImage = 0;
			gAngry = false;
		}
	}
	
	public void animateThrow()
	{
		throwFrames++;
		
		if( throwFrames == throwFramesBeforeUpdate )
		{
			if( leftCatch || rightCatch )
			{	
				throwIndex++;
				throwFrames = 0;
			}
			else
			{
				throwIndex = -1;
				throwFrames = 0;
			}
		}
		
		if( throwIndex > 1 )
		{
			leftCatch = false;
			rightCatch = false;
			throwIndex = -1;
		}
	}
	
	@Override
	public void update()
	{
		if( oilMisses == maxMisses || throwMisses == maxMisses )
		{
			setGameOver( true );
		}
		
		int[] scoreUpdates = oil.update( score, bucketScore, oilMisses, playerColumn );
		
		score = scoreUpdates[ 0 ];
		bucketScore = scoreUpdates[ 1 ];
		oilMisses = scoreUpdates[ 2 ];
		
		if( mAngry )
			animateMan();
		
		if( gAngry )
			animateGirl();
		
		if( throwIndex >= 0 )
			animateThrow();
		
		setScoreImage();
		
		catcher.update();
	}

	@Override
	public void draw(Graphics g)
	{
		g.drawImage( background, 0, 0, getImageObserver() );
		
		player.draw( g );
		
		if( score >= 100 )
		{
			g.drawImage( scoreNumbers[ hundredsIndex ], 35, 66, getImageObserver() );
			g.drawImage( scoreNumbers[ tensIndex ], 75, 66, getImageObserver() );
			g.drawImage( scoreNumbers[ onesIndex ], 115, 66, getImageObserver() );
		}
		else if( score < 100 && score >= 10 )
		{
			g.drawImage( scoreNumbers[ tensIndex ], 75, 66, getImageObserver() );
			g.drawImage( scoreNumbers[ onesIndex ], 115, 66, getImageObserver() );
		}
		else
			g.drawImage( scoreNumbers[ onesIndex ], 115, 66, getImageObserver() );
		
		if( throwIndex >= 0 && wasThrownLeft )
			g.drawImage( throwLeftImages[ throwIndex ], throwLeftXPoints[ throwIndex ], throwLeftYPoints[ throwIndex ], getImageObserver() );
		else if( throwIndex >= 0 && !wasThrownLeft )
			g.drawImage( throwRightImages[ throwIndex ], throwRightXPoints[ throwIndex ], throwRightYPoints[ throwIndex ], getImageObserver() );
			
		if( oilMisses >= 0 )
			g.drawImage( oilMissesImages[ oilMisses ], oilMissXPoint, oilMissYPoint, getImageObserver() );
		
		if( throwMisses >= 0 )
			g.drawImage( throwMissesImages[ throwMisses ], throwMissXPoint, throwMissYPoint, getImageObserver() );
		
		if( bucketScore >= 0 )
			g.drawImage( bucketImages[ bucketScore ], bucketXPoints[ playerColumn - 1 ], bucketYPoint, getImageObserver() );
		
		if( gImage < 3 )
			g.drawImage( girlImages[ gImage ], girlXPoint1, girlYPoint1, getImageObserver() );
		else
			g.drawImage( girlImages[ gImage ], girlXPoint2, girlYPoint2, getImageObserver() );
		
		if( mImage < 3 )
			g.drawImage( manImages[ mImage ], manXPoint1, manYPoint, getImageObserver() );
		else
			g.drawImage( manImages[ mImage ], manXPoint2, manYPoint, getImageObserver() );
	
		catcher.draw( g );
		oil.draw( g );
	}

	@Override
	public void processKeyEvents( String key )
	{		
		if( key.equals( "A" ) || key.equals( "Left" ) )
		{
			if( playerColumn > 1 )
			{
				playerColumn--;
			}
		}
		else if( key.equals( "D" ) || key.equals( "Right" ) )
		{
			if( playerColumn < 3 )
			{
				playerColumn++;
			}
		}
		else if( key.equals( "Space" ) )
		{
			if( playerColumn == 1 )
			{
				playerColumn = 0;
			
				if( bucketScore >= 0 )
				{
					throwIndex = 0;
					wasThrownLeft = true;
				}
				
				if( catcher.getColumn() == 0 )
				{
					leftCatch = true;
					score = score + bucketScore;
					bucketScore = -1;
				}
				else if( bucketScore >= 0 )
				{
					if( throwMisses <= 1 )
						throwMisses++;
					
					mAngry = true;
					bucketScore = -1;
				}
			}
			else if( playerColumn == 3 )
			{
				playerColumn = 4;
			
				if( bucketScore >= 0 )
				{
					throwIndex = 0;
					wasThrownLeft = false;
				}
				
				if( catcher.getColumn() == 3 )
				{
					rightCatch = true;
					score = score + bucketScore;
					bucketScore = -1;
				}
				else if( bucketScore >= 0 )
				{
					if( throwMisses <= 1 )
						throwMisses++;
					
					gAngry = true;
					
					bucketScore = -1;
				}
			}
		}		
	
		player.update( playerColumn );
	}
}
