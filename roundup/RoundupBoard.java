package roundup;

import gridgame.GridBoard;
import gridgame.HallOfFame;
import gridgame.HallOfFameEntry;
import gridgame.Renderable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JOptionPane;

public class RoundupBoard extends GridBoard<RoundupTile>
{
    // Override GridBoard's parent attribute so we don't have to do casts.
    private RoundupGame parent;

    private static int kBoardWidth = 7;
    private static int kBoardHeight = 7;

    public void setParent(RoundupGame parent)
    {
        this.parent = parent;
    }
    
    protected void resetBoard()
    {
        this.grid = new RoundupTile[this.kBoardHeight][this.kBoardWidth];

        // Fill the board with normal (non-bomb) pieces.
        for (int line = 0; line < this.kBoardHeight; line++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                this.grid[line][column] = new RoundupTile();
            }
        }
        
        // TODO: Mark edges as black wall tiles.
        // TODO: Mark center square as pink.
        // TODO: Place robots.
    }

    protected void clickTile(final int row, final int column)
    {
        // Basic sanity check.
        if (row < 0 || row >= this.kBoardHeight || column < 0 || column >= this.kBoardWidth)
        {
            throw new IllegalArgumentException("Tile must be on the board.");
        }
        
        RoundupTile tile = (RoundupTile)this.grid[row][column];
        // TODO: Select robot.
    }
}

