package mahjongg;

import gridgame.GridBoard;
import gridgame.Renderable;

import java.util.Collections;
import java.util.LinkedList;

public class MahjonggBoard extends GridBoard<MahjonggTile>
{
    private static final int kBoardWidth = 12;
    private static final int kBoardHeight = 8;
    private static final int[] blankEdgeMahjonggTiles = { 0, 2, 1, 0, 0, 1, 2, 0 };
    private int tileCount = 0;
    
    protected void resetBoard()
    {
        if (this.grid == null)
        {
            this.grid = new MahjonggTile[this.kBoardHeight][this.kBoardWidth];
        }
        else
        {
            clearBoard();
        }
        this.tileCount = 0;
        
        // Make a list of all the tiles, in nice predictable order.
        // Needs to be a LinkedList specifically, since we're calling .pop().
        LinkedList<MahjonggTile> tiles = new LinkedList<MahjonggTile>();
        MahjonggTile.Suit[] suits = { MahjonggTile.Suit.Bamboo, MahjonggTile.Suit.Characters, MahjonggTile.Suit.Dots };
        for (int rank = 1; rank < 8; rank++)
        {
            for (MahjonggTile.Suit suit : suits)
            {
                // There are four of each tile.
                for (int i = 0; i < 4; i++)
                {
                    tiles.add(new MahjonggTile(suit, rank));
                }
            }
        }
        
        // Shuffle a bitch.
        int gameNumber = this.parent.getGame();
        java.util.Random generator = new java.util.Random(gameNumber);
        java.util.Collections.shuffle(tiles, generator);
        
        // Put our shuffled tiles onto the board.
        for (int line = 0; line < this.kBoardHeight; line++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                if (column < this.blankEdgeMahjonggTiles[line]
                 || column > this.kBoardWidth - this.blankEdgeMahjonggTiles[line] - 1
                 || tiles.isEmpty())
                {
                    this.grid[line][column] = null;
                }
                else
                {
                    this.grid[line][column] = tiles.pop();
                    this.tileCount++;
                }
            }
        }
        
        //this.secondsElapsed = 0;
        //updateStatusBar();
        //setTitle("Mahjongg - board " + this.gameNumber);
    }
    
    protected void clearBoard()
    {
        for (int row = 0; row < this.kBoardHeight; row++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                this.grid[row][column] = null;
            }
        }
    }

    protected void cheat()
    {
        this.clearBoard();
        this.grid[(this.kBoardHeight / 2) - 1][(this.kBoardWidth / 2) - 1] = new MahjonggTile(MahjonggTile.Suit.Bamboo, 1);
        this.grid[(this.kBoardHeight / 2) - 1][(this.kBoardWidth / 2)] = new MahjonggTile(MahjonggTile.Suit.Bamboo, 1);
        this.tileCount = 2;
    }

    protected MahjonggTile findOpenPair()
    {
        java.util.List<MahjonggTile> edgeTiles = edgeTiles();
        Collections.<MahjonggTile>sort(edgeTiles);

        MahjonggTile previous = null;
        for (MahjonggTile current : edgeTiles)
        {
            if (current.equals(previous))
            {
                return current;
            }

            previous = current;
        }

        return null;
    }

    protected java.util.List<MahjonggTile> edgeTiles()
    {
        // This is a lazy and terribly inefficient way to do this.
        java.util.List<MahjonggTile> edgeTiles = new LinkedList<MahjonggTile>();
        for (int row = 0; row < this.kBoardHeight; row++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                if (isEdgeTile(row, column) && this.grid[row][column] != null)
                {
                    edgeTiles.add((MahjonggTile)this.grid[row][column]);
                }
            }
        }
        
        return edgeTiles;
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
            if (this.grid[row][i] != null)
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
            if (this.grid[row][i] != null)
            {
                return false;
            }
            
            i++;
        }
    }
}

