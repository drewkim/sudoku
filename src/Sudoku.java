import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Sudoku extends JFrame implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	static int MAX_WIDTH = 1440;
	static int MAX_HEIGHT = 900;
	final int DELAY_IN_MILLISEC = 50;
	public static Cell[][] grid;
	final static int CELL_WIDTH = Cell.WIDTH;
	final static int OFFSET = 22;
	static int rows;
	static int cols;
	private static Scanner input;

	public static void main(String args[])
	{
		input = new Scanner(System.in);
		System.out.println("Enter dimensions of the region:");
		System.out.println("Enter rows:");
		rows = input.nextInt();
		System.out.println("Enter columns:");
		cols = input.nextInt();

		grid = new Cell[rows * cols][cols * rows];
		initializeGrid();

		final Sudoku sd = new Sudoku();
		sd.addKeyListener(sd);
		sd.addMouseListener(sd);
		sd.setSize(rows * cols * CELL_WIDTH + 1, rows * cols * CELL_WIDTH + OFFSET + 1);
		sd.setVisible(true);
	}

	public Sudoku()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Timer clock= new Timer(DELAY_IN_MILLISEC, this);
		clock.start();
	}

	private static void initializeGrid()
	{
		for(int row = 0; row < rows*cols; row++)
		{
			for(int col = 0; col < rows*cols; col++)
			{
				grid[row][col] = new Cell(col*CELL_WIDTH, OFFSET + row*CELL_WIDTH);
			}
		}
	}

	public void actionPerformed(ActionEvent e) 
	{
	}

	public void paint(Graphics g)
	{
		clearScreen(g);
		clearCollisions();
		checkIfCollision(grid);
		checkIfChunkCollision(grid);
		paintGrid(g);
		drawBoldLines(g);
		System.out.println(isFull(grid));
	}

	public void clearScreen(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
	}

	private static void paintGrid(Graphics g)
	{
		g.setColor(Color.BLACK);
		for(int row = 0; row < rows*cols; row++)
		{
			for(int col = 0; col < rows*cols; col++)
			{
				Cell cell = grid[row][col];
				g.drawRect(cell.getX(), cell.getY(), CELL_WIDTH, CELL_WIDTH);
				if(cell.getIfEdit())
				{
					g.setColor(new Color(1.0f, 1.0f, 0.0f, 0.5f));
					g.fillRect(cell.getX() + 1, cell.getY() + 1, CELL_WIDTH - 1, CELL_WIDTH - 1);
					g.setColor(Color.BLACK);
				}
				if(cell.getIfCollision())
				{
					g.setColor(Color.RED);
					if(cell.getIfEdit())
						g.setColor(new Color(1.0f, 1.0f, 0.0f, 1.0f));
					g.fillRect(cell.getX() + 1, cell.getY() + 1, CELL_WIDTH - 1, CELL_WIDTH - 1);
					g.setColor(Color.BLACK);
				}
				g.setFont(new Font("Serif", Font.BOLD, 32));
				g.drawString(cell.getNumber(), cell.getX() + 20, cell.getY() + 35);
			}
		}
	}

	private static void drawBoldLines(Graphics g)
	{
		g.setColor(Color.BLACK);
		for(int i = 0; i < cols*rows; i += rows)
		{
			g.drawLine(i * CELL_WIDTH + 1, OFFSET + 1, i * CELL_WIDTH + 1, OFFSET + rows * cols * CELL_WIDTH + 1);
		}
		for(int j = 0; j < cols*rows; j += cols)
		{
			g.drawLine(1, OFFSET + j * CELL_WIDTH + 1, cols * rows* CELL_WIDTH + 1, OFFSET + j * CELL_WIDTH + 1);
		}
	}

	private static void clearCollisions()
	{
		for(int i = 0; i < rows*cols; i++)
		{
			for(int j = 0; j < rows*cols; j++)
			{
				grid[i][j].setCollision(false);
			}
		}
	}

	private static int checkIfChunkCollision(Cell[][] grid1)
	{
		int collisions = 0;
		for(int i = 0; i < rows * cols; i+=cols)
		{
			for(int j = 0; j < rows * cols; j+=rows)
			{
				for(int x = i; x < i + cols; x++)
				{
					for(int y = j; y < j + rows; y++)
					{
						Cell cell1 = grid1[x][y];
						for(int a = i; a < i + cols; a++)
						{
							for(int b = j; b < j + rows; b++)
							{
								if(a != x || b != y)
								{
									Cell cell2 = grid1[a][b];
									if(cell1.getNumber().equals(cell2.getNumber()) && !cell1.getNumber().equals("") && !cell2.getNumber().equals(""))
									{
										cell1.setCollision(true);
										cell2.setCollision(true);
										collisions++;
									}
								}
							}
						}
					}
				}
			}
		}
		return collisions;
	}

	private static int checkIfCollision(Cell[][] grid1)
	{
		int collisions = 0;
		for(int i = 0; i < rows*cols; i++)
		{
			for(int j = 0; j < rows*cols; j++)
			{
				Cell cell1 = grid1[i][j];
				for(int x = 0; x < rows*cols; x++)
				{
					for(int y = 0; y < rows*cols; y++)
					{
						if(x != i || y != j)
						{
							Cell cell2 = grid1[x][y];
							if(cell1.getNumber().equals(cell2.getNumber()) && !cell1.getNumber().equals(""))
							{
								if(cell1.getX() == cell2.getX() || cell1.getY() == cell2.getY())
								{
									cell1.setCollision(true);
									cell2.setCollision(true);
									collisions++;
								}
							}
						}
					}
				}
			}
		}
		return collisions;
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
		String number = "" + e.getKeyChar();
		for(int row = 0; row < rows*cols; row++)
		{
			for(int col = 0; col < rows*cols; col++)
			{
				Cell cell = grid[row][col];
				if(cell.getIfEdit() && Integer.parseInt(number) <= rows*cols && Integer.parseInt(number) > 0)
				{
					cell.setNumber(number);
					cell.setIfEdit(false);
				}
				else if(cell.getIfEdit() && Integer.parseInt(number) == 0)
				{
					cell.setNumber("");
					cell.setIfEdit(false);
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		int x = e.getX();
		int y = e.getY();

		if(inGrid(x,y))
		{
			Cell cell = grid[(y-OFFSET)/CELL_WIDTH][x/CELL_WIDTH];
			if(cell.getIfEdit() == false)
			{
				for(int row = 0; row < rows*cols; row++)
				{
					for(int col = 0; col < rows*cols; col++)
					{
						if(grid[row][col].getIfEdit())
							grid[row][col].setIfEdit(false);
					}
				}
				cell.setIfEdit(true);
			}
			else
			{
				cell.setIfEdit(false);
			}
		}
		repaint();
	}

	private static boolean inGrid(int xIn, int yIn)
	{
		if(xIn >= 0 && xIn <= rows * cols * CELL_WIDTH && yIn >= OFFSET && yIn <= rows * cols * CELL_WIDTH + OFFSET)
			return true;
		else
			return false;
	}
	
	private static void solve()
	{
		
	}
	
	//TODO
	private static int getFitness(Cell[][] grid1)
	{
		return 0;
	}
	
	private static boolean isFull(Cell[][] grid1)
	{
		boolean full = true;
		for(int i = 0; i < grid1.length; i++)
		{
			for(int j = 0; j < grid1[0].length; j++)
			{
				if(grid1[i][j].getNumber().equals(""))
					full = false;
			}
		}
		return full;
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		// TODO Auto-generated method stub	
	}
}
