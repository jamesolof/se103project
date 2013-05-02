import javax.swing.JFrame;

public class GameMain 
{

	public static void main( String args[] )
	{
		GameMenuFrame frame = new GameMenuFrame();
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize( 800, 630 );
		frame.setVisible( true );
		frame.setResizable( false );
	}
}
