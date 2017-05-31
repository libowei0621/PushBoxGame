
public class GameMap {
	
	private int playerX = 0;
	private int playerY = 0;
	private byte map[][];
	private int level;
	
	// For undo operation
	// Need current player position and map status
	public GameMap(int playerX, int playerY, byte [][] map) 
	{
		this.playerX = playerX;
		this.playerY = playerY;
		
		int maxRow = map.length;
		int maxCol = map[0].length;
		byte [][] temp = new byte[maxRow][maxCol];
		
		for(int i = 0; i < maxRow; i++)
		{
			for(int j = 0; j < maxCol; j++)
			{
				temp[i][j] = map[i][j];
			}
		}
		
		this.map = temp;
	}
	
	// For save operation
	// Need current player position, map status, and level
	public GameMap(int playerX, int playerY, byte [][] map, int level)
    {
        this(playerX, playerY, map);
        this.level = level;
    }
	
	public int getPlayerX()
    {
        return playerX;
    }
	
	public int getPlayerY()
    {
        return playerY;
    }
	
	public byte[][] getMap()
    {
        return map;
    }
	
	public int getLevel()
    {
        return level;
    }
}
