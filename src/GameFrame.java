import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameFrame extends JFrame implements MouseListener, KeyListener
{

    private static final long serialVersionUID = -5727453900716058849L;
    
    // current level
    private int level = 0;
    
    // player's position
    private int playerRow = 7;
    private int playerCol = 7;
    
    // position to print first picture
    private int leftX = 0;
    private int leftY = 0;
    
    // map's row and col
    private int mapRow = 0;
    private int mapCol = 0;
    
    // screen's size
    private int width = 0;
    private int height = 0;
    
    // pics we need
    private Image pic[] = null;
    
    private byte[][] map = null;
    
    // for the undo operation
    private boolean acceptUndo = true;
    private Stack<GameMap> list = new Stack<GameMap>();
    
    public GameFrame()
    {
        // TODO Auto-generated constructor stub
        super("PushBoxGame");
        setSize(600, 600);
        setResizable(false);
        setLocation(300, 20);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
        setLayout(null);
        setVisible(true);
        
        //get pics
        getPic();
        
        width = getWidth();
        height = getHeight();
        initMap();
        addKeyListener(this);
        addMouseListener(this);
        
        
        // add restart and next level buttoms
        JButton nextLevel = new JButton("Next Level");
        JButton restart = new JButton("Restart");
        add(nextLevel);
        add(restart);
        restart.setBounds((600/2 - 100)/2, height - 100, 100, 50);
        nextLevel.setBounds((600/2 - 100)/2 + width/2, height - 100, 100, 50);
        
        restart.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                initMap();
                update(getGraphics());
            }
        });
        
        nextLevel.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                level++;
                initMap();
                update(getGraphics());
                
            }
        });
        
        // paint out buttons
        super.paint(getGraphics());
    }
    
    // init map
    private void initMap()
    {
        map = GameMapFactory.getMap(level);
        list.clear();
        getMapSizeAndPosition();
        getPlayerPosition();
        
        // reset undo flag
        acceptUndo = true;
        
        // get focus
        setFocusable(true);
        requestFocus();
    }
    
    // get player's position on map
    private void getPlayerPosition()
    {
        for(int i = 0; i < mapRow; i++)
        {
            for(int j = 0; j < mapCol; j++)
            {
                if(map[i][j] == GameMapFactory.MANDOWN || map[i][j] == GameMapFactory.MANUP ||
                   map[i][j] == GameMapFactory.MANRIGHT || map[i][j] == GameMapFactory.MANLEFT ||
                   map[i][j] == GameMapFactory.MANDOWNONEND || map[i][j] == GameMapFactory.MANUPONEND ||
                   map[i][j] == GameMapFactory.MANRIGHTTOEND || map[i][j] == GameMapFactory.MANLEFTTOEND)
                {
                    playerRow = i;
                    playerCol = j;
                    break;
                }
            }
        }
    }
    
    // get map size and position
    private void getMapSizeAndPosition()
    {
        mapRow = map.length;
        mapCol = map[0].length;
        leftX = (width - mapRow * 30) / 2;
        leftY = (height - mapCol * 30) / 2;
    }
    
    // load all pictures
    private void getPic()
    {
        this.pic = new Image[14];
        for(int i = 0; i <= 13; i++)
        {
            pic[i] = Toolkit.getDefaultToolkit().getImage("images\\pic" + i + ".JPG");
        }
    }
    
    public int getPlayerCol()
    {
        return playerCol;
    }
    
    public int getPlayerRow()
    {
        return playerRow;
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public byte[][] getMap()
    {
        return map;
    }
    
    // move up
    private void moveUp()
    {
        // move to the wall
        if(map[playerRow - 1][playerCol] == GameMapFactory.WALL)
        {
            return;
        }
        
        if(map[playerRow - 1][playerCol] == GameMapFactory.BOX || map[playerRow - 1][playerCol] == GameMapFactory.BOXONEND)
        {
            if(map[playerRow - 2][playerCol] == GameMapFactory.ROAD || map[playerRow - 2][playerCol] == GameMapFactory.END)
            {
                // Save current map;
                GameMap currentMap = new GameMap(playerRow, playerCol, map);
                list.push(currentMap);
                
                // get type that after the move
                byte boxTemp = map[playerRow - 2][playerCol] == GameMapFactory.END ? GameMapFactory.BOXONEND : GameMapFactory.BOX;
                byte playerTemp = map[playerRow - 1][playerCol] == GameMapFactory.BOX ? GameMapFactory.MANUP : GameMapFactory.MANUPONEND;
                
                // change the map
                map[playerRow - 2][playerCol] = boxTemp;
                map[playerRow - 1][playerCol] = playerTemp;
                map[playerRow][playerCol] = roadOrEnd(map[playerRow][playerCol]);
                
                // change player's position
                playerRow--;
            }
        }
        else
        {
            if(map[playerRow - 1][playerCol] == GameMapFactory.ROAD || map[playerRow - 1][playerCol] == GameMapFactory.END)
            {
                // Save current map;
                GameMap currentMap = new GameMap(playerRow, playerCol, map);
                list.push(currentMap);
                
                // get type that after the move
                byte playerTemp = map[playerRow - 1][playerCol] == GameMapFactory.END ? GameMapFactory.MANUPONEND : GameMapFactory.MANUP;
                
                // change the map
                map[playerRow - 1][playerCol] = playerTemp;
                map[playerRow][playerCol] = roadOrEnd(map[playerRow][playerCol]);
                
                playerRow--;
            }
        }
    }
    
    private void moveDown()
    {
        // move to the wall
        if(map[playerRow + 1][playerCol] == GameMapFactory.WALL)
        {
            return;
        }
        
        if(map[playerRow + 1][playerCol] == GameMapFactory.BOX || map[playerRow + 1][playerCol] == GameMapFactory.BOXONEND)
        {
            if(map[playerRow + 2][playerCol] == GameMapFactory.ROAD || map[playerRow + 2][playerCol] == GameMapFactory.END)
            {
                // Save current map;
                GameMap currentMap = new GameMap(playerRow, playerCol, map);
                list.push(currentMap);
                
                // get type that after the move
                byte boxTemp = map[playerRow + 2][playerCol] == GameMapFactory.END ? GameMapFactory.BOXONEND : GameMapFactory.BOX;
                byte playerTemp = map[playerRow + 1][playerCol] == GameMapFactory.BOX ? GameMapFactory.MANDOWN : GameMapFactory.MANDOWNONEND;
                
                // change the map
                map[playerRow + 2][playerCol] = boxTemp;
                map[playerRow + 1][playerCol] = playerTemp;
                map[playerRow][playerCol] = roadOrEnd(map[playerRow][playerCol]);
                
                // change player's position
                playerRow++;
            }
        }
        else
        {
            if(map[playerRow + 1][playerCol] == GameMapFactory.ROAD || map[playerRow + 1][playerCol] == GameMapFactory.END)
            {
                // Save current map;
                GameMap currentMap = new GameMap(playerRow, playerCol, map);
                list.push(currentMap);
                
                // get type that after the move
                byte playerTemp = map[playerRow + 1][playerCol] == GameMapFactory.END ? GameMapFactory.MANDOWNONEND : GameMapFactory.MANDOWN;
                
                // change the map
                map[playerRow + 1][playerCol] = playerTemp;
                map[playerRow][playerCol] = roadOrEnd(map[playerRow][playerCol]);
                
                playerRow++;
            }
        }
    }
    
    private void moveLeft()
    {
        // move to the wall
        if(map[playerRow][playerCol - 1] == GameMapFactory.WALL)
        {
            return;
        }
        
        if(map[playerRow][playerCol - 1] == GameMapFactory.BOX || map[playerRow][playerCol - 1] == GameMapFactory.BOXONEND)
        {
            if(map[playerRow][playerCol - 2] == GameMapFactory.ROAD || map[playerRow][playerCol - 2] == GameMapFactory.END)
            {
                // Save current map;
                GameMap currentMap = new GameMap(playerRow, playerCol, map);
                list.push(currentMap);
                
                // get type that after the move
                byte boxTemp = map[playerRow][playerCol - 2] == GameMapFactory.END ? GameMapFactory.BOXONEND : GameMapFactory.BOX;
                byte playerTemp = map[playerRow][playerCol - 1] == GameMapFactory.BOX ? GameMapFactory.MANLEFT : GameMapFactory.MANLEFTTOEND;
                
                // change the map
                map[playerRow][playerCol - 2] = boxTemp;
                map[playerRow][playerCol - 1] = playerTemp;
                map[playerRow][playerCol] = roadOrEnd(map[playerRow][playerCol]);
                
                // change player's position
                playerCol--;
            }
        }
        else
        {
            if(map[playerRow][playerCol - 1] == GameMapFactory.ROAD || map[playerRow][playerCol - 1] == GameMapFactory.END)
            {
                // Save current map;
                GameMap currentMap = new GameMap(playerRow, playerCol, map);
                list.push(currentMap);
                
                // get type that after the move
                byte playerTemp = map[playerRow][playerCol - 1] == GameMapFactory.END ? GameMapFactory.MANLEFTTOEND : GameMapFactory.MANLEFT;
                
                // change the map
                map[playerRow][playerCol - 1] = playerTemp;
                map[playerRow][playerCol] = roadOrEnd(map[playerRow][playerCol]);
                
                playerCol--;
            }
        }
    }
    
    private void moveRight()
    {
        // move to the wall
        if(map[playerRow][playerCol + 1] == GameMapFactory.WALL)
        {
            return;
        }
        
        if(map[playerRow][playerCol + 1] == GameMapFactory.BOX || map[playerRow][playerCol + 1] == GameMapFactory.BOXONEND)
        {
            if(map[playerRow][playerCol + 2] == GameMapFactory.ROAD || map[playerRow][playerCol + 2] == GameMapFactory.END)
            {
                // Save current map;
                GameMap currentMap = new GameMap(playerRow, playerCol, map);
                list.push(currentMap);
                
                // get type that after the move
                byte boxTemp = map[playerRow][playerCol + 2] == GameMapFactory.END ? GameMapFactory.BOXONEND : GameMapFactory.BOX;
                byte playerTemp = map[playerRow][playerCol + 1] == GameMapFactory.BOX ? GameMapFactory.MANRIGHT : GameMapFactory.MANRIGHTTOEND;
                
                // change the map
                map[playerRow][playerCol + 2] = boxTemp;
                map[playerRow][playerCol + 1] = playerTemp;
                map[playerRow][playerCol] = roadOrEnd(map[playerRow][playerCol]);
                
                // change player's position
                playerCol++;
            }
        }
        else
        {
            if(map[playerRow][playerCol + 1] == GameMapFactory.ROAD || map[playerRow][playerCol + 1] == GameMapFactory.END)
            {
                // Save current map;
                GameMap currentMap = new GameMap(playerRow, playerCol, map);
                list.push(currentMap);
                
                // get type that after the move
                byte playerTemp = map[playerRow][playerCol + 1] == GameMapFactory.END ? GameMapFactory.MANRIGHTTOEND : GameMapFactory.MANRIGHT;
                
                // change the map
                map[playerRow][playerCol + 1] = playerTemp;
                map[playerRow][playerCol] = roadOrEnd(map[playerRow][playerCol]);
                
                playerCol++;
            }
        }
    }
    
    // when player move, return the position type should to be
    private byte roadOrEnd(byte player)
    {
        byte result = GameMapFactory.ROAD;
        
        if(player == GameMapFactory.MANUPONEND || player == GameMapFactory.MANDOWNONEND ||
           player == GameMapFactory.MANRIGHTTOEND || player == GameMapFactory.MANLEFTTOEND)
        {
            result = GameMapFactory.END;
        }
        
        return result;
    }
    
    // game is end or not
    private boolean isFinished()
    {
        for(int i = 0; i < mapRow; i++)
        {
            for(int j = 0; j < mapCol; j++)
            {
                if(map[i][j] == GameMapFactory.END || map[i][j] == GameMapFactory.MANDOWNONEND ||
                   map[i][j] == GameMapFactory.MANLEFTTOEND || map[i][j] == GameMapFactory.MANRIGHTTOEND||
                   map[i][j] == GameMapFactory.MANUPONEND)
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private void displayToast(String str)
    {
        JOptionPane.showMessageDialog(null, str, "Notice", JOptionPane.ERROR_MESSAGE);
    }
    
    private void undo()
    {
        if(acceptUndo)
        {
            if(!list.isEmpty())
            {
                GameMap preMap = list.pop();
                this.map = preMap.getMap();
                this.playerRow = preMap.getPlayerX();
                this.playerCol = preMap.getPlayerY();
                repaint();
            }
            else
            {
                displayToast("Cannot undo");
            }
        }
        else
        {
            displayToast("Cannot undo");
        }
    }
    
    @Override
    public void paint(Graphics g)
    {
        for(int i = 0; i < mapRow; i++)
        {
            for(int j = 0; j < mapCol; j++)
            {
                if(map[i][j] != 0)
                {
                    g.drawImage(pic[map[i][j]], leftX + j * 30, leftY + i * 30, this);
                }
            }
        }
    }
    
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        // TODO Auto-generated method stub
        if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            moveUp();
        }
        
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            moveDown();
        }
        
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            moveLeft();
        }
        
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            moveRight();
        }
        
        repaint();
        
        if(isFinished())
        {
            // when game finish, lose focus, and reject all undo operation
            acceptUndo = false;
            setFocusable(false);
            JOptionPane.showMessageDialog(null, "Finished");
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // TODO Auto-generated method stub
        if(e.getButton() == MouseEvent.BUTTON3)
        {
            undo();
        }
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

    public static void main(String[] args)
    {
        new GameFrame();
    }
}
