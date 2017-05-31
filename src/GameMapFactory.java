
public class GameMapFactory {
	
	// Wall
	public static final byte WALL = 1;
	
	// Box
	public static final byte BOX = 2;
	
	// Box on the destination
	public static final byte BOXONEND = 3;
	
	// The box's destination
	public static final byte END = 4;
	
	// Player face to South
	public static final byte MANDOWN = 5;
	
	// Player face to West
	public static final byte MANLEFT = 6;
	
	// Player face to East
	public static final byte MANRIGHT = 7;
	
	// Player face to North
	public static final byte MANUP = 8;
	
	// The road
	public static final byte ROAD = 9;
	
	// Player face to South on destination
	public static final byte MANDOWNONEND = 10;
	
	// Player face to West on destination
	public static final byte MANLEFTTOEND = 11;
	
	// Player face to East on destination
	public static final byte MANRIGHTTOEND = 12;
	
	// Player face to North on destination
	public static final byte MANUPONEND = 13;
	
	// All levels maps
	// First dimension represents level
	private static byte map[][][] =
		{
			{
    			{ 0, 0, 1, 1, 1, 0, 0, 0 },
    			{ 0, 0, 1, 4, 1, 0, 0, 0 },
    			{ 0, 0, 1, 9, 1, 1, 1, 1 },
    			{ 1, 1, 1, 2, 9, 2, 4, 1 },
    			{ 1, 4, 9, 2, 5, 1, 1, 1 },
    			{ 1, 1, 1, 1, 2, 1, 0, 0 },
    			{ 0, 0, 0, 1, 4, 1, 0, 0 },
    			{ 0, 0, 0, 1, 1, 1, 0, 0 }
			},
			{
			    { 1, 1, 1, 1, 1, 1, 1, 0 },
			    { 1, 9, 9, 5, 9, 9, 1, 1 },
			    { 1, 9, 2, 9, 2, 9, 9, 1 },
			    { 1, 9, 9, 2, 9, 2, 9, 1 },
			    { 1, 1, 1, 2, 1, 1, 1, 1 },
			    { 1, 9, 4, 9, 4, 4, 1, 0 },
			    { 1, 9, 9, 9, 9, 9, 1, 0 },
			    { 1, 9, 9, 1, 9, 4, 1, 0 },
			    { 1, 9, 9, 4, 9, 9, 1, 0 },
			    { 1, 1, 1, 1, 1, 1, 1, 0 }
			},
			{
			    { 0, 0, 1, 1, 1, 1, 0, 0 },
			    { 0, 0, 1, 4, 4, 1, 0, 0 },
			    { 0, 1, 1, 9, 4, 1, 1, 0 },
			    { 0, 1, 9, 9, 2, 4, 1, 0 },
			    { 1, 1, 9, 2, 9, 9, 1, 1 },
			    { 1, 9, 9, 1, 2, 2, 9, 1 },
			    { 1, 9, 9, 5, 9, 9, 9, 1 },
			    { 1, 1, 1, 1, 1, 1, 1, 1 }
			}
		};
	
	// Total levels
	public static int gameLevels = map.length;
	
	public static byte[][] getMap(int level)
	{
		byte[][] temp;
		
		if(level >= 0 && level < gameLevels)
		{
			temp = map[level];
		}
		else
		{
			temp = map[0];
		}
		
		int maxRow = temp.length;
		int maxCol = temp[0].length;
		byte [][] result = new byte[maxRow][maxCol];
		
		for(int i = 0; i < maxRow; i++)
		{
			for(int j = 0; j < maxCol; j++)
			{
				result[i][j] = temp[i][j];
			}
		}
		
		return result;
	}
}
