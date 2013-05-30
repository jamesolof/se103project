import java.util.Random;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FlagmanGame extends Game 
{
	private Random random = new Random();
	
	private BufferedImage background = null;
	
	private BufferedImage timeBlock = null;
	
	private BufferedImage[] scoreNumbers = new BufferedImage[10];
		
	private BufferedImage[] playerImages = new BufferedImage[6];
			
	private BufferedImage[] signalImages = new BufferedImage[4];
	
	private FlagmanPlayer player;
	private int playerFramesBeforeUpdate = 26;
	private int playerFrames = playerFramesBeforeUpdate;
	
	private boolean raisedFlag = false;
	
	private int blocksRemaining = 5;
	private int blockXPoint = 650;
	private int[] blockYPoints = { 440, 345, 250, 155, 60 };
	
	private int signalFlag = 0;
	private int lastSignalFlag = -1;
	private int signalXPoint = 60;
	private int[] signalYPoints = { 65, 180, 295, 410 };
	
	private int hundredsIndex;
	private int tensIndex;
	private int onesIndex;
	
	private int flag = 5;
	private int timerFrames = 0;
	private int timerFramesBeforeUpdate = 37;
	private int lives = 3;
	private int score = 0;
	
	public FlagmanGame( ImageObserver io ) 
	{
		super(io);
		
		loadImages();
		
		player = new FlagmanPlayer( 0, 0, io, playerImages );

		signalFlag = random.nextInt( 4 );
	}

	private void loadImages()
	{
		try
		{
			background = ImageIO.read( new File( "GamePics/Flagman/background.png" ) );
			
			timeBlock = ImageIO.read( new File( "GamePics/Flagman/Misc/timeBlock.png" ) );
			
			for( int i = 0; i < scoreNumbers.length; i++ )
			{
				scoreNumbers[i] = ImageIO.read( new File( String.format( "GamePics/Icons/ScoreNumbers/number%d.png", i ) ) );
				
				if( i < playerImages.length )
					playerImages[i] = ImageIO.read( new File( String.format( "GamePics/Flagman/Player/player%d.png", i ) ) );
				
				if( i < signalImages.length )
					signalImages[i] = ImageIO.read( new File( String.format( "GamePics/Flagman/Misc/signal%d.png", i ) ) );
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
		return "flagman";
	}

	@Override
	public void update()
	{
			timerFrames++;
			playerFrames++;
			
			if( playerFrames >= playerFramesBeforeUpdate )
			{
				flag = 5;
				player.update( flag );
				raisedFlag = false;
			}
			
			if( timerFrames == timerFramesBeforeUpdate )
			{
				timerFrames = 0;
				blocksRemaining--;
				
				if( blocksRemaining == 0 )
				{
					lives--;
					lastSignalFlag = signalFlag;
					
					signalFlag = random.nextInt( 4 );
					
					while( signalFlag == lastSignalFlag )
						signalFlag = random.nextInt( 4 );
					
					blocksRemaining = 5;
					timerFrames = 0;
				}
			}
			
			if( raisedFlag == true )
			{
				if( flag == signalFlag )
					score++;
				else
					lives--;
				
				lastSignalFlag = signalFlag;
				
				signalFlag = random.nextInt( 4 );
				
				while( signalFlag == lastSignalFlag )
					signalFlag = random.nextInt( 4 );
				
				blocksRemaining = 5;
				timerFrames = 0;
				raisedFlag = false;
			}
			
			if( lives <= 0 )
			{
				setScore( score );
				setGameOver( true );
				lives = 0;
			}
			
			setScoreImage();
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
	
	@Override
	public void draw(Graphics g) 
	{
		g.drawImage( background, 0, 0, getImageObserver() );
		
		player.draw( g );
		
		g.drawImage( signalImages[ signalFlag ], signalXPoint, signalYPoints[ signalFlag ], getImageObserver() );
		
		for( int i = 0; i < blocksRemaining; i++ )
		{
			g.drawImage( timeBlock, blockXPoint, blockYPoints[i], getImageObserver() );
		}
		
		g.setFont( new Font( "Sansserif", Font.BOLD, 35 ) );
		g.setColor( Color.BLACK );
		
		g.drawString( "Lives:     | Score:", 180, 95 );
		
		g.drawImage( scoreNumbers[ lives ], 300, 66, getImageObserver() );
		
		if( score >= 100 )
		{
			g.drawImage( scoreNumbers[ hundredsIndex ], 520, 66, getImageObserver() );
			g.drawImage( scoreNumbers[ tensIndex ], 560, 66, getImageObserver() );
			g.drawImage( scoreNumbers[ onesIndex ], 600, 66, getImageObserver() );
		}
		else if( score < 100 && score >= 10 )
		{
			g.drawImage( scoreNumbers[ tensIndex ], 520, 66, getImageObserver() );
			g.drawImage( scoreNumbers[ onesIndex ], 560, 66, getImageObserver() );
		}
		else
			g.drawImage( scoreNumbers[ onesIndex ], 520, 66, getImageObserver() );
	}
	
	@Override
	public void processKeyEvents( String key )
	{
		if( playerFrames >= playerFramesBeforeUpdate )
		{
			if( key.equals( "1" ) )
			{
				flag = 0;
				raisedFlag = true;
				playerFrames = 0;
			}
			else if( key.equals( "2" ) )
			{
				flag = 1;
				raisedFlag = true;
				playerFrames = 0;
			}
			else if( key.equals( "3" ) )
			{
				flag = 2;
				raisedFlag = true;
				playerFrames = 0;
			}
			else if( key.equals( "4" ) )
			{
				flag = 3;
				raisedFlag = true;
				playerFrames = 0;
			}
			
			if( score % 10 == 0 && timerFramesBeforeUpdate > 15 )
			{
				timerFramesBeforeUpdate--;
			}
			
		}
		
		player.update( flag );
	}	
}

