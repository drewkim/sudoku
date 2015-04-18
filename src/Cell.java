
public class Cell 
{
	private String number;
	private int xCoord;
	private int yCoord;
	private boolean inEdit;
	public static final int WIDTH = 50;
	private boolean colliding;
	
	public Cell(int xIn, int yIn)
	{
		xCoord = xIn;
		yCoord = yIn;
		inEdit = false;
		number = "";
		colliding = false;
	}
	
	public void setNumber(String numberIn)
	{
		number = numberIn;
	}
	
	public String getNumber()
	{
		return number;
	}
	
	public int getX()
	{
		return xCoord;
	}
	
	public int getY()
	{
		return yCoord;
	}
	
	public void setIfEdit(boolean x)
	{
		inEdit = x;
	}
	
	public boolean getIfEdit()
	{
		return inEdit;
	}
	
	public void setCollision(boolean col)
	{
		colliding = col;
	}
	
	public boolean getIfCollision()
	{
		return colliding;
	}
}
