import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Pattern;

public class GameSaver
{   
    private static String dirPath = "save";
    private static String savePath = dirPath + "\\save.data";
    
    static
    {
        // create save folder
        try
        {
            Files.createDirectory(Paths.get(dirPath));
        }
        catch (FileAlreadyExistsException e)
        {
            // folder existed, do nothing
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }      
    }
    
    public static void gameSave(GameMap game)
    {
        Path path = Paths.get(savePath);
        byte[] output = mapToString(game).getBytes();
        
        try
        {
            Files.write(path, output, StandardOpenOption.CREATE); 
        } 
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static GameMap gameLoad()
    {
        Path path = Paths.get(savePath);
        String [] results = null;
        String saveData = "";
        int playerX = 0;
        int playerY = 0;
        int level = 0;
        byte[][] map = null;
        
        if(Files.exists(path, LinkOption.NOFOLLOW_LINKS))
        {
            try
            {
                saveData = Files.readAllLines(path).get(0);
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            // no save file
            return null;
        }
        
        results = saveData.split(Pattern.quote("|"));
        
        // get level
        level = Integer.parseInt(results[0]);
        
        // get player's position
        playerX = Integer.parseInt(results[1]);
        playerY = Integer.parseInt(results[2]);
        
        // get map
        map = stringToMap(results);
        
        return new GameMap(playerX, playerY, map, level);
    }
    
    private static String mapToString(GameMap map)
    {
        StringBuffer buffer = new StringBuffer();
        
        // level
        buffer.append(map.getLevel());
        buffer.append("|");
        
        // player position
        buffer.append(map.getPlayerX());
        buffer.append("|");
        buffer.append(map.getPlayerY());
        buffer.append("|");
        
        // map
        byte [][] tempMap = map.getMap();
        for(int i = 0; i < tempMap.length; i++)
        {
            for(int j = 0; j < tempMap[i].length; j++)
            {
                buffer.append(tempMap[i][j]);
            }
            buffer.append("|");
        }
        
        // End
        buffer.append("END");
        return buffer.toString();
    }
    
    private static byte[][] stringToMap(String[] s)
    {
        // number of rows
        int rowNumber = s.length - 4;
        
        // number of cols
        int colNumber = s[3].length();
        
        // create map
        byte [][] result = new byte[rowNumber][colNumber];
        
        for(int i = 0; i < rowNumber; i++)
        {
            for(int j = 0; j < colNumber; j++)
            {
                String temp = "" + s[i+3].charAt(j);
                result[i][j] = Byte.parseByte(temp);
            }
        }
        
        return result;
    }
}
