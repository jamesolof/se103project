import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class FireGame extends Game
{
	private BufferedImage background = null;
	
	private BufferedImage[] manImages = new BufferedImage[25];
	
	private BufferedImage[] playerImages = new BufferedImage[3];
	
	private BufferedImage[] missImages = new BufferedImage[3];
	
	private BufferedImage[] smokeImages = new BufferedImage[4];
	
	private BufferedImage[] bellImages = new BufferedImage[2];
	
	private Random random = new Random();
	
	private int modInt = 7;
	
	private int frames = 0;
	
	private BufferedImage[] scoreNumbers = new BufferedImage[ 10 ];
	
	private int hundredsIndex = 0;
	private int tensIndex = 0;
	private int onesIndex = 0;

	private int misses= -1;
	private int maxMisses = 2; 
	private int playerColumn = 1;
	private int score = 0;
	
	private int smokeFrame = 0;
	private int[] smokeXPoint = {100,120,140,240};
	private int[] smokeYPoint = {80,50,30,-10};
	
	private int bellFrame = 0;
	private int[] bellXPoint = {712,650};
	private int[] bellYPoint = {100,100};
	
	private FirePlayer player;
	
	private ArrayList<FireMan> men = new ArrayList<FireMan>();
	
	private int addFrames = 0;
	private double addFramesBeforeUpdate = 600;
	
	public FireGame( ImageObserver io )
	{
		super( io ); 
		
		loadImages();
		
		player = new FirePlayer( 0, 0, io, playerImages );
		player.update( playerColumn );
	}
	
	private void loadImages()
	{
		try
		{
			background = ImageIO.read( new File( "GamePics/Fire/FireBackground.png" ) );
			
			for( int i = 0; i < manImages.length; i++ )
			{
				manImages[i] = ImageIO.read( new File( String.format("GamePics/Fire/AI/man%s.png", i ) ) );
				
				if( i < scoreNumbers.length )
					scoreNumbers[ i ] = ImageIO.read( new File( String.format( "GamePics/Icons/ScoreNumbers/number%d.png", i ) ) );
					
				if( i < missImages.length )
				{
					missImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Fire/Misc/fireMiss%d.png", i ) ) );
					playerImages[i] = ImageIO.read( new File( String.format( "GamePics/Fire/Player/player%d.png", i ) ) );
				}
				if(i < smokeImages.length)
				{
					smokeImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Fire/Misc/smoke%d.png", i ) ) );
				}
				if(i < bellImages.length)
				{
					bellImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Fire/Misc/bell%d.png", i ) ) );
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
		return "fire";
	}
	
	public void setScoreImage()
	{
		if( score >= 100 )
		{
			hundredsIndex = score / 100;
			tensIndex = score % 100 / 10;
			onesIndex = score % 100 % 10;
			
			if( score > 135 )
				modInt = 3;
		}
		else if( score < 100 && score >= 10 )
		{
			tensIndex = score / 10;
			onesIndex = score % 10;
			
			if( score == 45 )
				modInt = 5;
			else if( score == 90 )
				modInt = 4;
		}
		else
		{
			onesIndex = score;
		}
	}
	
	private void spawnFireMan()
	{
		if( random.nextInt( 21 ) % modInt == 0 )
		{
			men.add( new FireMan( 0, 0, getImageObserver(), manImages ) );
		}
	}
	
	private void animateSmoke(){
		if (smokeFrame <= 2){
			smokeFrame++;
		}
		else{
			smokeFrame = 0;
		}
		if (bellFrame == 0){
			bellFrame++;
		}
		else{
			bellFrame--;
		}
	}
	
	@Override
	public void update()
	{
		addFrames++;
		
		
		if( addFrames >= addFramesBeforeUpdate || men.size() == 0 )
		{
			addFrames = 0;
			men.add( new FireMan( 0, 0, getImageObserver(), manImages ) );
		}
		
		for( int i = 0; i < men.size(); i++ )
		{
			int[] updates = men.get( i ).update( score, misses, playerColumn );
			
			score = updates[ 0 ];
			misses = updates[ 1 ];
			
			if( updates[ 2 ] == 1 )
				spawnFireMan();
						
			if( men.get( i ).getIndex() == 25 )
			{
				men.remove( i );
				i--;
				
				if( addFramesBeforeUpdate > 300 )
				{
					addFramesBeforeUpdate = Math.round( 600 - ( score*2 ) );
				}
			}
		}	
		
		if( misses >= maxMisses )
		{
			misses = 2;
			setScore( score );
			setGameOver( true );
		}
		
		setScoreImage();
		frames++;
		if (frames == 60){
			animateSmoke();
			frames = 0;
		}
	}

	@Override
	public void draw( Graphics g ) 
	{
		g.drawImage( background, 0, 0, getImageObserver() );
		
		player.draw( g );
		
		g.drawImage( smokeImages[ smokeFrame ], smokeXPoint[smokeFrame], smokeYPoint[smokeFrame], getImageObserver() );
		
		g.drawImage( bellImages[ bellFrame ], bellXPoint[bellFrame], bellYPoint[bellFrame], getImageObserver() );

		
		for( int i = 0; i < men.size(); i++ )
		{
			men.get( i ).draw( g );
		}
		
		if( score >= 100 )
		{
			g.drawImage( scoreNumbers[ hundredsIndex ], 510, 39, getImageObserver() );
			g.drawImage( scoreNumbers[ tensIndex ], 550, 39, getImageObserver() );
			g.drawImage( scoreNumbers[ onesIndex ], 590, 39, getImageObserver() );
		}
		else if( score < 100 && score >= 10 )
		{
			g.drawImage( scoreNumbers[ tensIndex ], 550, 39, getImageObserver() );
			g.drawImage( scoreNumbers[ onesIndex ], 590, 39, getImageObserver() );
		}
		else
			g.drawImage( scoreNumbers[ onesIndex ], 590, 39, getImageObserver() );
		
		if( misses >= 0 )
		{
			g.drawImage( missImages[ misses ], 665, 30, getImageObserver() );
		}
		

	}

	@Override
	public void processKeyEvents( String key ) 
	{
		if( key.equals( "A" ) || key.equals( "Left" ) )
		{
			if( playerColumn > 0 )
				playerColumn--;
		}
		else if( key.equals( "D" ) || key.equals( "Right" ) )
		{
			if( playerColumn < 2 )
				playerColumn++;
		}
		
		player.update( playerColumn );
	}
}
