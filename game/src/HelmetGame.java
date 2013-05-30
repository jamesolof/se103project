import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;
import java.util.ArrayList;

public class HelmetGame extends Game
{
	private BufferedImage background = null;
	private BufferedImage openDoor = null;
	
	private boolean open = false;
	private Random random = new Random();
	
	private BufferedImage[] playerImages = new BufferedImage[ 7 ];

	private BufferedImage[] toolImages = new BufferedImage[5];
	
	private BufferedImage[] scoreNumbers = new BufferedImage[ 10 ];
	private int hundredsIndex = 0;
	private int tensIndex = 0;
	private int onesIndex = 0;
	
	private BufferedImage[] livesImages = new BufferedImage[ 3 ];
	private int misses = -1;
	private int livesMissXPoint = 330;
	private int livesMissYPoint = -170;
	
	private HelmetPlayer player;
	private int playerFrames = 0;
	private int playerFramesBefore = 10;
	
	private int maxMisses = 2;
	private int playerColumn = 0;
	private int score = 0;
	private int scoreFrames = 0;
	private int scoreFramesBefore = 300;
	
	private int doorFrames = 0;
	private int doorFramesBefore = 0;
	
	private int drawFrames = 0;
	private double drawFramesBefore = 100;
	
	private double speedFramesBefore = 100;
	
	private int toolIndex = 0;
	private int lastToolIndex = 0;
	
	ArrayList<HelmetTools> tools = new ArrayList<HelmetTools>(); 
	
	public HelmetGame( ImageObserver io )
	{
		super( io );
		
		loadImages();
		
		player = new HelmetPlayer( 0, 0, io, playerImages );
		player.update( playerColumn );
		
		doorFramesBefore = (2 + random.nextInt(9)) * 60;
	}
	
	private void loadImages()
	{
		try
		{
			background = ImageIO.read( new File( "GamePics/Helmet/Background.png" ) );
			openDoor = ImageIO.read(new File("GamePics/Helmet/Misc/DoorOpen.png"));
			
			for( int i = 0; i < scoreNumbers.length; i++ )
			{
				scoreNumbers[ i ] = ImageIO.read( new File( String.format( "GamePics/Icons/ScoreNumbers/number%d.png", i ) ) );
			
				if( i < playerImages.length )
					playerImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Helmet/Player/Player%d.png", i ) ) );
				
				if( i < toolImages.length )
					toolImages[i] = ImageIO.read( new File( String.format( "GamePics/Helmet/Tools/Tool%d.png", i ) ) );
				
				if( i < livesImages.length )
				{
					livesImages[ i ] = ImageIO.read( new File( String.format( "GamePics/Helmet/Misc/Life%d.png", i ) ) );
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
		return "helmet";
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
	public void update()
	{
		doorFrames++;
		
		if(doorFrames == doorFramesBefore)
		{
			doorFrames = 0;
			doorFramesBefore = (2 + random.nextInt(9)) * 60;
			open = !open;
		}
		
		if(playerColumn > 0)
			scoreFrames++;
		
		if(scoreFrames == scoreFramesBefore)
		{
			score++;
			scoreFrames = 0;
		}
		
		if(playerColumn == 6)
		{
			playerColumn = 0;
			player.update(playerColumn);
			score = score + 5;
		}
		
		drawFrames++;
		
		if( drawFrames == drawFramesBefore )
		{
			adjustSpeed();
			
			drawFrames = 0;
			
			toolIndex = random.nextInt(5);
			
			while( toolIndex == lastToolIndex )
				toolIndex = random.nextInt( 5 );
			
			tools.add( new HelmetTools(0, 0, toolIndex, getImageObserver(), toolImages, speedFramesBefore ) );
			
			lastToolIndex = toolIndex;
		}
		
		for(int i = 0; i < tools.size(); i++)
		{
			misses = tools.get(i).update( score, misses, playerColumn);
			
			if( misses == maxMisses )
			{
				setGameOver( true );
			}
		}
		
		playerFrames++;
		
		setScoreImage();
	}
	
	public void adjustSpeed()
	{
		speedFramesBefore = Math.round( speedFramesBefore - (score*.07) );
		
		drawFramesBefore = Math.round( drawFramesBefore - (score*.07) );
		
		if( speedFramesBefore < 20)
			speedFramesBefore = 20;
		
		if( drawFramesBefore < 17 )
			drawFramesBefore = 17;
	}
	
	@Override
	public void draw(Graphics g)
	{
		g.drawImage( background, 0, 0, getImageObserver() );
		
		if(open == true)
			g.drawImage(openDoor, 475, 225, getImageObserver());
		
		player.draw( g );
		
		if( score >= 100 )
		{
			g.drawImage( scoreNumbers[ hundredsIndex ], 35, 30, getImageObserver() );
			g.drawImage( scoreNumbers[ tensIndex ], 75, 30, getImageObserver() );
			g.drawImage( scoreNumbers[ onesIndex ], 115, 30, getImageObserver() );
		}
		else if( score < 100 && score >= 10 )
		{
			g.drawImage( scoreNumbers[ tensIndex ], 75, 30, getImageObserver() );
			g.drawImage( scoreNumbers[ onesIndex ], 115, 30, getImageObserver() );
		}
		else
			g.drawImage( scoreNumbers[ onesIndex ], 115, 30, getImageObserver() );
		
		if( misses >= 0 )
			g.drawImage( livesImages[ misses ], livesMissXPoint, livesMissYPoint, getImageObserver() );
		
		for(int i = 0; i < tools.size(); i++)
		{
			if(tools.get(i).getRow() > 4)
			{
				tools.remove(i);
				i--;
			}
			else
			{
				tools.get(i).draw(g);
			}
		}
	}
	
	@Override
	public void processKeyEvents( String key )
	{	
		if(playerFrames >= playerFramesBefore)
		{
			if( key.equals( "A" ) || key.equals( "Left" ) )
			{
				if( playerColumn > 0 && playerColumn != 6 )
				{
					playerColumn--;
				}
			}
			else if( key.equals( "D" ) || key.equals( "Right" ) )
			{
				if( playerColumn == 5 && open == true)
				{
					playerColumn++;
				}
				else if (playerColumn < 5)
					playerColumn++;
			}
			playerFrames = 0;
			
			for( int i = 0; i < tools.size(); i++ )
			{
				if( tools.get( i ).getIndex() == playerColumn - 1 && tools.get( i ).getRow() == 4 )
				{
					misses++;
					
					if( misses > 2 )
						misses = 2;
				}
			}
		}
		
		player.update( playerColumn );
	}
}
