import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OilGame extends Game
{
	//background image
	private BufferedImage background = null;
	
	//number images
	private BufferedImage[] scoreNumbers = new BufferedImage[ 10 ];
	
	//stores the score by numbers 0-9 for each place, used for rendering correct score
	private int hundredsIndex = 0;
	private int tensIndex = 0;
	private int onesIndex = 0;
	
	//images of pedestrian girl in lower right corner
	private BufferedImage[] girlImages = new BufferedImage[ 5 ];
	private boolean gAngry = false; //certain images of the girl are displayed when true
	private int gImage = 0; //index of the girls current image
	private int gCycle = 0; //times the girl's angry frames have been displayed after oil is thrown on her
	private int gFrames = 0; //counter to determine when image is changed
	private int girlXPoint1 = 586;
	private int girlXPoint2 = 610;
	private int girlYPoint1 = 514;
	private int girlYPoint2 = 484;
	
	//same as above, but for the man in lower left corner
	private BufferedImage[] manImages = new BufferedImage[ 5 ];
	private boolean mAngry = false;
	private int mImage = 0;
	private int mCycle = 0;
	private int mFrames = 0;
	private int manXPoint1 = 130;
	private int manXPoint2 = 90;
	private int manYPoint = 511;

	//when gFrames or mFrames matches this, gImage or mImage (image index) is changed
	private int mgFramesBeforeUpdate = 40;
	
	//images of the police officer displayed at game over screen
	private BufferedImage[] officerImages = new BufferedImage[ 2 ];
	
	//images of oil inside player's bucket
	private BufferedImage[] bucketImages = new BufferedImage[ 3 ];
	private int bucketScore = -1; //index of bucket image to display, changed when oil is caught or thrown
	private int[] bucketXPoints = { 157, 338, 528 };
	private int bucketYPoint = 208;
	
	//images of miss counter for oil that isn't caught
	private BufferedImage[] oilMissesImages = new BufferedImage[ 3 ];
	private int oilMisses = -1; //index of oil miss counter, changed when oil isn't caught
	private int oilMissXPoint = 670;
	private int oilMissYPoint = 55;
	
	//images of miss counter for throws that land on pedestrians
	private BufferedImage[] throwMissesImages = new BufferedImage[ 3 ];
	private int throwMisses = -1; //index of throw miss counter, changed when oil isn't thrown at right time
	private int throwMissXPoint = 616;
	private int throwMissYPoint = 315;
	
	//images of oil being thrown to the left by player
	private BufferedImage[] throwLeftImages = new BufferedImage[ 2 ];
	private int[] throwLeftXPoints = { 183, 160 };
	private int[] throwLeftYPoints = { 418, 442 };
	
	//same as above, but thrown to the right
	private BufferedImage[] throwRightImages = new BufferedImage[ 2 ];
	private int[] throwRightXPoints = { 590, 580 };
	private int[] throwRightYPoints = { 418, 442 };
	
	//index of which thrown oil image to display, changed when oil is thrown (is image index for left AND right throws) 
	private int throwImage = -1;
	private int throwFrames = 0; //counter for when throw image is changed
	private int throwFramesBeforeUpdate = 18; //when throwFrames matches this, throwImage is incremented
	
	private int maxMisses = 2; //when oilMisses or throwMisses matches this, the game is over
	private int playerColumn = 2;
	private int score = 0;
	
	// 'caught' and 'wasThrownLeft' checked when displaying certain images
	private boolean caught = false;
	private boolean wasThrownLeft = false;
	
	private OilPlayer player; //see class OilPlayer
	private OilAI catcher; //see class OilAI
	private OilDrop oil; //see class OilDrop
	
	public OilGame( ImageObserver io )
	{
		super( io ); //call to superclass Game's constructor
		
		player = new OilPlayer( 0, 0, io ); //create an object of OilPlayer
		player.update( playerColumn ); //update player to 
		
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
			if( caught )
			{	
				throwImage++;
				throwFrames = 0;
			}
			else
			{
				throwImage = -1;
				throwFrames = 0;
			}
		}
		
		if( throwImage > 1 )
		{
			caught = false;
			throwImage = -1;
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
		
		setScoreImage();
		
		if( mAngry )
			animateMan();
		
		if( gAngry )
			animateGirl();
		
		if( throwImage >= 0 )
			animateThrow();
		
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
		
		if( throwImage >= 0 && wasThrownLeft )
			g.drawImage( throwLeftImages[ throwImage ], throwLeftXPoints[ throwImage ], throwLeftYPoints[ throwImage ], getImageObserver() );
		else if( throwImage >= 0 && !wasThrownLeft )
			g.drawImage( throwRightImages[ throwImage ], throwRightXPoints[ throwImage ], throwRightYPoints[ throwImage ], getImageObserver() );
			
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
					throwImage = 0;
					wasThrownLeft = true;
				}
				
				if( catcher.getColumn() == 0 )
				{
					caught = true;
					score = score + bucketScore + 1;
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
					throwImage = 0;
					wasThrownLeft = false;
				}
				
				if( catcher.getColumn() == 3 )
				{
					caught = true;
					score = score + bucketScore + 1;
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
