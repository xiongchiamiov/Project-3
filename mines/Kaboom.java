import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.text.*;

/**  Skeleton for Kaboom. 
 *   GUI has a menu bar, a status area, and a 2d playing area.
 *   The GUI will display the game and handle user interaction. 
 * @author J. Dalbey
 * @version 9/24/2011
*/
public class Kaboom extends JFrame implements ActionListener
{
    /* Main components of the GUI */
    // DO NOT CHANGE ANY OF THE GUI COMPONENT DECLARATIONS IN THIS SECTION
    private String[] columns = {"", "", "", "", "", "", "", "", "", "", };
    private JTable table;
    private JMenuBar menuBar;
    private JMenu mnuGame;
    private JMenuItem[] mnuItems;
    private JLabel lblStatus = new JLabel();
    private ImageIcon background;
    
    /* The game board */
    private Renderable[][] myBoard; 
    private static final int kBoardWidth = 10;
    private static final int kBoardHeight = 10;
    private static final int kMaxBombs = 9;
    private static final int kMaxGameNumber = 5000;
    private static int gameNumber = 0;
    private int moves = 0;
    private int flagsPlaced = 0;
    private int numBombs = 0;
    private int secondsElapsed = 0;
    private boolean justCheated = false;
    
    /* Square dimensions in pixels */
    private static final int kTileWidth = 65;
    private static final int kTileHeight = 43;
    
    
    /** Create a GUI.
     * Will use the System Look and Feel when possible.
     */
    public Kaboom()
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
        this.gameNumber = (new java.util.Random()).nextInt(this.kMaxGameNumber);
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
            
            public TableCellRenderer getCellRenderer(int row, int column)
            {
                return new PieceRenderer(GridImages.createInstance(""));
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

        mnuItems[2] = new JMenuItem("Select Game");
        mnuItems[2].setAccelerator(KeyStroke.getKeyStroke('G', ActionEvent.ALT_MASK));
        mnuItems[2].addActionListener(this);
        mnuGame.add(mnuItems[2]);

        mnuItems[3] = new JMenuItem("Scores");
        mnuItems[3].setAccelerator(KeyStroke.getKeyStroke('S', ActionEvent.ALT_MASK));
        mnuItems[3].addActionListener(this);
        mnuGame.add(mnuItems[3]);

        mnuItems[4] = new JMenuItem("Cheat");
        mnuItems[4].setAccelerator(KeyStroke.getKeyStroke('C', ActionEvent.ALT_MASK));
        mnuItems[4].addActionListener(this);
        mnuGame.add(mnuItems[4]);
        
        mnuItems[5] = new JMenuItem("Quit");
        mnuItems[5].setAccelerator(KeyStroke.getKeyStroke('Q', ActionEvent.ALT_MASK));
        mnuItems[5].addActionListener(this);
        mnuGame.add(mnuItems[5]);

        setJMenuBar(menuBar);   // tell the frame which menu bar to use
    }
    
    /* Listener to respond to mouse clicks on the table */
    private MouseAdapter myMouseListener = new MouseAdapter()
    {
        public void mouseReleased(MouseEvent ev)
        {
            // call methods to handle player's click
            // Left-click?
            if (ev.getButton() == MouseEvent.BUTTON1)
            {
                int col = table.getSelectedColumn();
                int row = table.getSelectedRow();
                clickTile(row, col);
            }
            // Right-click?
            else if (ev.getButton() == MouseEvent.BUTTON3)
            {
                int col = table.columnAtPoint(ev.getPoint());
                int row = table.rowAtPoint(ev.getPoint());
                rightClickTile(row, col);
            }
            repaint();
        }
    };
    
    protected void loadImages()
    {
        // load background image
        background = new ImageIcon( 
                    Toolkit.getDefaultToolkit().getImage(
                        this.getClass().getResource("PieceImages/bkgd.jpg")));
        // Load tile images here
    }
    
    protected void newGame()
    {
        this.gameNumber++;
        if (this.gameNumber > this.kMaxGameNumber)
        {
            this.gameNumber = 1;
        }
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
        
        // Fill the board with normal (non-bomb) pieces.
        for (int line = 0; line < this.kBoardHeight; line++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                this.myBoard[line][column] = new Tile();
            }
        }
        
        // Figure out (deterministically) where we want the bombs.
        // Storing them in a Set allows us to avoid duplicates.  Unfortunately,
        // between Java's lack of type inference, the necessary type-specifying
        // required by a statically-typed language using a system like
        // generics, and the fact that you can't genericize primitives like
        // int, the logic here gets muddled a bit by syntax.
        Set<Pair<Integer, Integer>> bombSet = new TreeSet<Pair<Integer, Integer>>();
        java.util.Random generator = new java.util.Random(this.gameNumber);
        for (int bombNumber = 0; bombNumber < this.kMaxBombs; bombNumber++)
        {
            int row = generator.nextInt(this.kBoardHeight);
            int column = generator.nextInt(this.kBoardWidth);
            bombSet.add(new Pair<Integer, Integer>(row, column));
        }
        this.numBombs = bombSet.size();
        for (Pair<Integer, Integer> bombCoordinates : bombSet)
        {
            Tile tile = (Tile)this.myBoard[bombCoordinates.first][bombCoordinates.second];
            tile.isBomb = true;
        }

        this.secondsElapsed = 0;
        this.moves = 0;
        this.flagsPlaced = 0;
        updateStatusBar();
        setTitle("Mines - board " + this.gameNumber);
    }
    
    /**
     * Calculate how many bombs are adjacent to a spot.
     *
     * Don't call this on a spot that has a bomb.
     */
    protected int calculateSurroundingBombs(int row, int column)
    {
        if (((Tile)this.myBoard[row][column]).isBomb)
        {
            throw new IllegalArgumentException("You should never be calculating nearby bombs for a bomb spot!");
        }
        
        int nearbyBombs = 0;
        // We don't care about including the actual tile in this list because
        // the above assertion guarantees it's not a bomb.
        int[] offsets = {-1, 0, 1};
        for (int offsetRow : offsets)
        {
            for (int offsetColumn : offsets)
            {
                // Easier than doing bounds-checking.
                try {
                    Tile adjacentTile = (Tile)this.myBoard[row+offsetRow][column+offsetColumn];
                    if (adjacentTile.isBomb)
                    {
                        nearbyBombs++;
                    }
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    // Do nothing, because we don't care about tiles that are
                    // off the board.
                }
            }
        }
        
        return nearbyBombs;
    }
    
    protected void startTimer()
    {
        // Every 1 second.
        Timer timer = new Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Kaboom.this.secondsElapsed++;
                Kaboom.this.updateStatusBar();
            }
        });
        
        Kaboom.this.updateStatusBar();
        timer.start();
    }
    
    protected void updateStatusBar()
    {
       // I don't really want to do printf in Java.
       String optionalSpace = " ";
       if (this.flagsPlaced > 9)
       {
          optionalSpace = "";
       }
       
       this.lblStatus.setText("Moves: " + this.moves + "   "
                            + "Flags:  " + optionalSpace + this.flagsPlaced + "/" + this.numBombs + " "
                            + this.secondsElapsed / 60 + ":" + String.format("%02d", this.secondsElapsed % 60));
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
        else if ("Select Game".equals(e.getActionCommand()))
        {
            String gameNumber = (String)JOptionPane.showInputDialog(this, "Enter desired game number (1 - 5000):", "Select Game", JOptionPane.QUESTION_MESSAGE, null, null, "");
            if (gameNumber != null)
            {
                int parsedGameNumber = Integer.parseInt(gameNumber);
                if (parsedGameNumber > 0 && parsedGameNumber <= this.kMaxGameNumber)
                {
                    this.gameNumber = parsedGameNumber;
                    this.restartGame();
                }
            }
        }
        else if ("Scores".equals(e.getActionCommand()))
        {
            try {
                HighScores highScores = HighScores.createInstance(".");
                JOptionPane.showMessageDialog(this, highScores.getHighScores(true), "High Scores", JOptionPane.PLAIN_MESSAGE);
            }
            catch (java.io.IOException exception)
            {
                System.err.println(exception);
            }
        }
        else if ("Cheat".equals(e.getActionCommand()))
        {
            this.revealBoard();
            this.justCheated = true;
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
        if (row < 0 || row >= this.kBoardHeight || column < 0 || column >= this.kBoardWidth)
        {
            throw new IllegalArgumentException("Tile must be on the board.");
        }
        
        Tile tile = (Tile)this.myBoard[row][column];
        if (tile.status == Piece.hidden || tile.status == Piece.flagged || this.justCheated)
        {
            this.moves++;
            this.justCheated = false;
            this.updateStatusBar();
            if (tile.isBomb)
            {
                this.revealBoard();
                tile.status = Piece.exploded;
                // TODO: This should be shown *after* the pieces are rendered revealed.
                JOptionPane.showMessageDialog(this, "You lost.");
            }
            else
            {
                this.revealEmptyCells(row, column);
                if (this.isBoardWon())
                {
                    String time = this.secondsElapsed / 60 + ":" + String.format("%02d", this.secondsElapsed % 60);
                    int choice = JOptionPane.showConfirmDialog(this, "Game "+this.gameNumber+" Cleared!\nSave your time of "+time+"?", "Win Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    // Magic number for "Yes"
                    if (choice == 0)
                    {
                        String name = (String)JOptionPane.showInputDialog(this, "Your score of "+time+" will be entered into the Hall of Fame.  Enter your name:", "Hall of Fame Entry", JOptionPane.QUESTION_MESSAGE, null, null, "");
                        HighScores highScores = HighScores.createInstance(".");
                        try
                        {
                            highScores.saveScore(time, name);
                        }
                        catch (java.io.IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        // Already-clicked pieces are still supposed to increment the move
        // counter.  It's easier to do that here, rather than earlier, because
        // we'd have to make more logic exceptions.
        else if (!tile.isBomb)
        {
            this.moves++;
            this.updateStatusBar();
        }
    }
    
    protected void revealEmptyCells(int row, int column)
    {
        Tile tile = (Tile)this.myBoard[row][column];
        tile.numSurroundingBombs = this.calculateSurroundingBombs(row, column);

        // We don't want to auto-reveal bombs.
        if (tile.isBomb)
        {
            return;
        }
        // Tiles next to bombs should be counted as empty for the sake of
        // calculations, but we want to stop recursing at them; they form a
        // barrier to contiguous "truly empty" spaces.
        if (tile.numSurroundingBombs != 0)
        {
            tile.status = Piece.empty;
            return;
        }

        tile.status = Piece.empty; // [2]
        
        // We don't care about including the actual tile in this list because
        // the condition at [1] will always be false for it due to [2].
        int[] offsets = {-1, 0, 1};
        for (int offsetRow : offsets)
        {
            for (int offsetColumn : offsets)
            {
                // Easier than doing bounds-checking.
                try {
                    Tile adjacentTile = (Tile)this.myBoard[row+offsetRow][column+offsetColumn];
                    // Only recurse to tiles that haven't been revealed already.
                    // Otherwise, we'd get infinite recursion back and forth.
                    if (adjacentTile.status == Piece.hidden
                     || adjacentTile.status == Piece.flagged) // [1]
                    {
                        this.revealEmptyCells(row+offsetRow, column+offsetColumn);
                    }
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    // Do nothing, because we don't care about tiles that are
                    // off the board.
                }
            }
        }
    }
    
    protected void revealBoard()
    {
        for (int row = 0; row < this.kBoardHeight; row++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                Tile tile = (Tile)this.myBoard[row][column];
                if (tile.isBomb)
                {
                    tile.status = Piece.bomb;
                }
                else
                {
                    tile.status = Piece.empty;
                    tile.numSurroundingBombs = this.calculateSurroundingBombs(row, column);
                }
            }
        }
    }
    
    /**
     * The board is won if all non-bomb pieces have been revealed.
     */
    protected boolean isBoardWon()
    {
        for (int row = 0; row < this.kBoardHeight; row++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                Tile tile = (Tile)this.myBoard[row][column];
                if (!tile.isBomb && (tile.status == Piece.hidden || tile.status == Piece.flagged))
                {
                    return false;
                }
            }
        }
        
        return true;
    }

    protected void rightClickTile(final int row, final int column)
    {
        // Basic sanity check.
        if (row < 0 || row >= this.kBoardHeight || column < 0 || column >= this.kBoardWidth)
        {
            throw new IllegalArgumentException("Tile must be on the board.");
        }
        
        Tile tile = (Tile)this.myBoard[row][column];
        if (tile.status == Piece.hidden)
        {
            tile.status = Piece.flagged;
            this.flagsPlaced++;
        }
        else if (tile.status == Piece.flagged)
        {
            tile.status = Piece.hidden;
            this.flagsPlaced--;
        }
        this.updateStatusBar();
    }
    
    // Local main to launch the GUI
    public static void main(String[] args)
    {
        // Create the GUI 
        Kaboom frame = new Kaboom();
        
        frame.layoutGUI();   // do the layout of widgets
        
        // Make the GUI visible and available for user interaction
        frame.pack();
        frame.setVisible(true);
    }
}  // end class

class Tile extends ImageIcon implements Renderable
{
    public boolean isBomb = false;
    public int numSurroundingBombs = 0;
    public Piece status = Piece.hidden;
    
    public RenderDescriptor getRenderDescriptor()
    {
        RenderDescriptor renderDescriptor = new RenderDescriptor();
        if (this.status == Piece.empty && numSurroundingBombs == 0)
        {
            renderDescriptor.text = "";
        }
        else if (numSurroundingBombs == 0)
        {
            renderDescriptor.isImage = true;
            renderDescriptor.text = this.status.toString();
        }
        else
        {
            renderDescriptor.isImage = false;
            renderDescriptor.isInverse = true; // White so we can see it.
            renderDescriptor.isStrong = true; // Be bold, be brave.
            renderDescriptor.text = Integer.toString(this.numSurroundingBombs);
        }
        return renderDescriptor;
    }

    public String toString()
    {
        return this.getRenderDescriptor().text;
    }
}

/** This is a silly little class, created because Java doesn't have 2-tuples
 * (or n-tuples of any sort, for that matter).
 */
class Pair<E1 extends Comparable<?>, E2 extends Comparable<?>> implements Comparable<Pair<E1, E2>> {
    public E1 first;
    public E2 second;
    
    public Pair(E1 first, E2 second)
    {
        this.first = first;
        this.second = second;
    }
    
    public boolean equals(Object other)
    {
        if (other instanceof Pair)
        {
            Pair otherPair = (Pair)other;
            return this.first.equals(otherPair.first) && this.second.equals(otherPair.second);
        }
        
        return false;
    }
    
    public int compareTo(Pair<E1, E2> other)
    {
        if (this.equals(other))
        {
            return 0;
        }
        
        if (this.first.equals(other.first))
        {
            return ((Comparable)this.second).compareTo(other.second);
        }
        else
        {
            return ((Comparable)this.first).compareTo(other.first);
        }
    }
}

