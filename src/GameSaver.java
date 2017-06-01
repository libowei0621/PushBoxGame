import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
}
