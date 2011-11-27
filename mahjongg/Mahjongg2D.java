import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.LinkedList;
import java.text.*;

/**  Skeleton for Mahjongg2D. 
 *   GUI has a menu bar, a status area, and a 2d playing area.
 *   The GUI will display the game and handle user interaction. 
 * @author J. Dalbey
 * @version 9/24/2011
*/
public class Mahjongg2D extends JFrame implements ActionListener
{
    /* Main components of the GUI */
    // DO NOT CHANGE ANY OF THE GUI COMPONENT DECLARATIONS IN THIS SECTION
    private String[] columns = {"", "", "", "", "", "", "", "", "", "", "", "", };
    private JTable table;
    private JMenuBar menuBar;
    private JMenu mnuGame;
    private JMenuItem[] mnuItems;    
    private JLabel lblStatus = new JLabel();
    private ImageIcon background;
    
    /* The game board */
    private Object[][] myBoard; 
    private static final int kBoardWidth = 12;
    private static final int kBoardHeight = 8;
    private static int gameNumber = 0;
    private static final int[] blankEdgeTiles = { 0, 2, 1, 0, 0, 1, 2, 0 };
    private int tileCount;
    private int firstTileRow = -1;
    private int firstTileColumn = -1;
    private int secondsElapsed = 0;
    
    /* Square dimensions in pixels */
    private static final int kTileWidth = 58;
    private static final int kTileHeight = 78;
    
    
    /** Create a GUI.
     * Will use the System Look and Feel when possible.
     */
    public Mahjongg2D()
    {
        super();
        try
        {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }
    }
    
    /** Place all the Swing widgets in the frame of this GUI.
     * @post the GUI is visible.  
     */
    public void layoutGUI()
    {
        loadImages();
        newGame();
        startTimer();
        table = new JTable(this.myBoard, this.columns)
        {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer( renderer, row, column);
                // We want renderer component to be
                // transparent so background image is visible
                if ( c instanceof JComponent)
                    ((JComponent)c).setOpaque(false);
                return c;
            }
            
            // Override paint so as to show the table background
            public void paint( Graphics g )
            {
                // paint an image in the table background
                if (background != null)
                {
                    g.drawImage( background.getImage(), 0, 0, null, null );
                }
                // Now let the paint do its usual work
                super.paint(g);
            }
            
            // Make the table cells not editable
            public boolean isCellEditable(int row,int column)
            {  
                return false;  
            }              
            
            public Class getColumnClass(int c)
            {
                return Tile.class;
            }
        }
        ; // end table def
        
        TableColumn column = null;
        // Does the board exist?
        if (this.myBoard != null)
        {
            // Set the dimensions for each column in the board to match the image */
            for (int index = 0; index < kBoardWidth; index++)
            {
                column = table.getColumnModel().getColumn(index);
                column.setMaxWidth(kTileWidth);
                column.setMinWidth(kTileWidth);
            }
        }
        
        // Define the layout manager that will control order of components
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        // Create the menu options
        layoutMenus();
        
        // Create a panel for the status information
        JPanel statusPane = new JPanel();
        statusPane.add(this.lblStatus);
        this.lblStatus.setName("Status");
        statusPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(statusPane);
        
        // Define the characteristics of the table that shows the game board        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);
        table.setRowHeight(kTileHeight);
        table.setOpaque(false);
        table.setShowGrid(false);
        table.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(table);
        
        // Define the mouse listener that will handle player's clicks.
        table.addMouseListener(myMouseListener);
        
        // And handle window closing events
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        }
        );
    } // end layout
    
    private void layoutMenus()
    {
        // Add a menubar
        menuBar = new javax.swing.JMenuBar();
        mnuGame = new JMenu("Game");     
        menuBar.add(mnuGame);
        mnuItems = new JMenuItem[9];  // allocate space for 9 menu items      
        
        // Create the Restart menu item
        mnuItems[0] = new JMenuItem("Restart");
        mnuItems[0].setAccelerator(KeyStroke.getKeyStroke('R', ActionEvent.ALT_MASK));
        mnuItems[0].addActionListener(this);
        mnuGame.add(mnuItems[0]);

        mnuItems[1] = new JMenuItem("New Game");
        mnuItems[1].setAccelerator(KeyStroke.getKeyStroke('N', ActionEvent.ALT_MASK));
        mnuItems[1].addActionListener(this);
        mnuGame.add(mnuItems[1]);

        mnuItems[2] = new JMenuItem("Hint");
        mnuItems[2].setAccelerator(KeyStroke.getKeyStroke('H', ActionEvent.ALT_MASK));
        mnuItems[2].addActionListener(this);
        mnuGame.add(mnuItems[2]);

        mnuItems[3] = new JMenuItem("Cheat");
        mnuItems[3].setAccelerator(KeyStroke.getKeyStroke('C', ActionEvent.ALT_MASK));
        mnuItems[3].addActionListener(this);
        mnuGame.add(mnuItems[3]);
        
        mnuItems[4] = new JMenuItem("Quit");
        mnuItems[4].setAccelerator(KeyStroke.getKeyStroke('Q', ActionEvent.ALT_MASK));
        mnuItems[4].addActionListener(this);
        mnuGame.add(mnuItems[4]);

        setJMenuBar(menuBar);   // tell the frame which menu bar to use
    }
    
    /* Listener to respond to mouse clicks on the table */
    private MouseAdapter myMouseListener = new MouseAdapter()
    {
        public void mouseReleased(MouseEvent ev)
        {
            int col = table.getSelectedColumn();
            int row = table.getSelectedRow();
            // call methods to handle player's click
            clickTile(row, col);
            repaint();
        }
    };
    
    protected void loadImages()
    {
        // load background image
        background = new ImageIcon( 
                    Toolkit.getDefaultToolkit().getImage(
                        this.getClass().getResource("img/" + "bkgd.jpg")));
        // Load tile images here
    }
    
    protected void newGame()
    {
        this.gameNumber++;
        restartGame();
    }

    protected void restartGame()
    {
        if (this.myBoard == null)
        {
            this.myBoard = new Tile[this.kBoardHeight][this.kBoardWidth];
        }
        else
        {
            clearBoard();
        }
        this.tileCount = 0;
        
        // Make a list of all the tiles, in nice predictable order.
        // Needs to be a LinkedList specifically, since we're calling .pop().
        LinkedList<Tile> tiles = new LinkedList<Tile>();
        Tile.Suit[] suits = { Tile.Suit.Bamboo, Tile.Suit.Characters, Tile.Suit.Dots };
        for (int rank = 1; rank < 8; rank++)
        {
            for (Tile.Suit suit : suits)
            {
                // There are four of each tile.
                for (int i = 0; i < 4; i++)
                {
                    tiles.add(new Tile(suit, rank));
                }
            }
        }
        
        // Shuffle a bitch.
        java.util.Random generator = new java.util.Random(this.gameNumber);
        java.util.Collections.shuffle(tiles, generator);
        
        // Put our shuffled tiles onto the board.
        for (int line = 0; line < this.kBoardHeight; line++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                if (column < this.blankEdgeTiles[line]
                 || column > this.kBoardWidth - this.blankEdgeTiles[line] - 1
                 || tiles.isEmpty())
                {
                    this.myBoard[line][column] = null;
                }
                else
                {
                    this.myBoard[line][column] = tiles.pop();
                    this.tileCount++;
                }
            }
        }

        this.secondsElapsed = 0;
        updateStatusBar();
        setTitle("Mahjongg - board " + this.gameNumber);
    }
    
    protected void startTimer()
    {
        // Every 1 second.
        Timer timer = new Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Mahjongg2D.this.secondsElapsed++;
                Mahjongg2D.this.updateStatusBar();
            }
        });
        
        Mahjongg2D.this.updateStatusBar();
        timer.start();
    }
    
    protected void updateStatusBar()
    {
       this.lblStatus.setText("Tiles Left: " + this.tileCount + "  "
                            + "Time: " + this.secondsElapsed / 60 + ":" + String.format("%02d", this.secondsElapsed % 60));
    }
    
    /** Handle button clicks
     * @param e The result of what was clicked
     */
    public void actionPerformed(ActionEvent e) 
    {
        // Does the user want to restart the current game?
        if ("Restart".equals(e.getActionCommand()))
        {
            restartGame();
        }
        else if ("New Game".equals(e.getActionCommand()))
        {
            newGame();
        }
        else if ("Hint".equals(e.getActionCommand()))
        {
            Tile hintTile = findOpenPair();
            if (hintTile == null)
            {
                JOptionPane.showMessageDialog(this, "No moves available.");
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Hint: " + hintTile.toString());
            }
        }
        else if ("Cheat".equals(e.getActionCommand()))
        {
            clearBoard();
            this.myBoard[(this.kBoardHeight / 2) - 1][(this.kBoardWidth / 2) - 1] = new Tile(Tile.Suit.Bamboo, 1);
            this.myBoard[(this.kBoardHeight / 2) - 1][(this.kBoardWidth / 2)] = new Tile(Tile.Suit.Bamboo, 1);
            this.tileCount = 2;
            updateStatusBar();
        }
        else if ("Quit".equals(e.getActionCommand()))
        {
            System.exit(0);
        }
        repaint();
    }

    protected void clearBoard()
    {
        for (int row = 0; row < this.kBoardHeight; row++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                this.myBoard[row][column] = null;
            }
        }
    }
    
    protected void clickTile(final int row, final int column)
    {
        // Basic sanity check.
        if (row < 0 || row > this.kBoardHeight || column < 0 || column > this.kBoardWidth)
        {
            throw new IllegalArgumentException("Tile must be on the board.");
        }
        
        // Do we already have a first tile in the pair we want to compare?
        if (this.firstTileRow > -1 && this.firstTileColumn > -1)
        {
            Tile tile1 = (Tile)this.myBoard[this.firstTileRow][this.firstTileColumn];
            Tile tile2 = (Tile)this.myBoard[row][column];
            
            if (!(this.firstTileRow == row && this.firstTileColumn == column)
             && tile1.equals(tile2) && isEdgeTile(this.firstTileRow, this.firstTileColumn) && isEdgeTile(row, column))
            {
                this.myBoard[this.firstTileRow][this.firstTileColumn] = this.myBoard[row][column] = null;
                this.tileCount -= 2;
                this.updateStatusBar();

                if (this.tileCount == 0)
                {
                    JOptionPane.showMessageDialog(this, "You win!");
                }
                
                // Reset our state variables.
                this.firstTileRow = -1;
                this.firstTileColumn = -1;
            }
            // Nope, those tiles aren't a valid match.
            else
            {
                // Let the second-clicked tile become our new selected "first" tile.
                this.firstTileRow = row;
                this.firstTileColumn = column;
            }
        }
        else
        {
            this.firstTileRow = row;
            this.firstTileColumn = column;
        }
    }
    
    protected boolean isEdgeTile(final int row, final int column)
    {
        // Basic sanity check.
        if (row < 0 || row > this.kBoardHeight || column < 0 || column > this.kBoardWidth)
        {
            throw new IllegalArgumentException("Tile must be on the board.");
        }
        
        // Style guidelines prevent use of the 'break' statement.  So, poor man's break.
        boolean kontinue = true;
        // Is this a left-side tile?
        int i = 0;
        while (kontinue && i < this.kBoardWidth)
        {
            if (this.myBoard[row][i] != null)
            {
                if (i == column)
                {
                    return true;
                }
                // Nope, we reached a non-null tile that isn't the one we want.
                else
                {
                    kontinue = false;
                }
            }
            
            i++;
        }
        
        // Is this a right-side tile?
        i = column + 1;
        while (true)
        {
            // We reached the end of the row.  Congratulations!
            if (i == this.kBoardWidth)
            {
                return true;
            }
            
            // Nope, there's a tile to the right of the one we're examining.
            if (this.myBoard[row][i] != null)
            {
                return false;
            }
            
            i++;
        }
    }

    protected Tile findOpenPair()
    {
        java.util.List<Tile> edgeTiles = edgeTiles();
        Collections.<Tile>sort(edgeTiles);

        Tile previous = null;
        for (Tile current : edgeTiles)
        {
            if (current.equals(previous))
            {
                return current;
            }

            previous = current;
        }

        return null;
    }

    protected java.util.List<Tile> edgeTiles()
    {
        // This is a lazy and terribly inefficient way to do this.
        java.util.List<Tile> edgeTiles = new LinkedList<Tile>();
        for (int row = 0; row < this.kBoardHeight; row++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                if (isEdgeTile(row, column) && this.myBoard[row][column] != null)
                {
                    edgeTiles.add((Tile)this.myBoard[row][column]);
                }
            }
        }
        
        return edgeTiles;
    }
    
    // Local main to launch the GUI
    public static void main(String[] args)
    {
        // Create the GUI 
        Mahjongg2D frame = new Mahjongg2D();
        
        frame.layoutGUI();   // do the layout of widgets
        
        // Make the GUI visible and available for user interaction
        frame.pack();
        frame.setVisible(true);
    }
}  // end class

class Tile extends ImageIcon implements Comparable<Tile>
{
    public enum Suit {Bamboo, Dots, Characters};
    
    private Suit suit;
    private int rank;
    
    public Tile(Suit suit, int rank)
    {
        super(Toolkit.getDefaultToolkit().getImage(Mahjongg2D.class.getResource("img/" + suit.name().substring(0, 1) + rank + ".JPG")));
        if (rank < 1 || rank > 7)
        {
            throw new IllegalArgumentException("Rank must be between 1 and 7");
        }
        this.suit = suit;
        this.rank = rank;
    }
    
    public boolean equals(Object other)
    {
        if (other instanceof Tile)
        {
            Tile otherTile = (Tile)other;
            if (this.suit == otherTile.suit && this.rank == otherTile.rank)
            {
                return true;
            }
        }
        
        return false;
    }

    public int compareTo(Tile other)
    {
        if (this.suit == other.suit)
        {
            return new Integer(this.rank).compareTo(other.rank);
        }
        
        return new Integer(this.suit.ordinal()).compareTo(other.suit.ordinal());
    }

    public String toString()
    {
        return this.suit.name() + " " + this.rank;
    }
}

