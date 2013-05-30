
public class HighScoreRecord 
{
	private int score;
	private String name;
	
	public HighScoreRecord( int s, String n )
	{
		setScore( score );
		setName( name );
	}
	
	public void setScore( int s )
	{
		score = s;
	}
	
	public void setName( String n )
	{
		name = n;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return String.format( "%d\n%s", getScore(), getName() );
	}
}
