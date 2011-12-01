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
    private class RobotCoordinate
    {
        public int row;
        public int column;
        public boolean isRed;
        
        public RobotCoordinate(int row, int column)
        {
            this(row, column, false);
        }
        
        public RobotCoordinate(int row, int column, boolean isRed)
        {
            this.row = row;
            this.column = column;
            this.isRed = isRed;
        }
    }
    
    private RobotCoordinate[][] boards = {
        {},
        { // 1
            new RobotCoordinate(1, 1),
            new RobotCoordinate(1, 5),
            new RobotCoordinate(3, 2, true),
            new RobotCoordinate(3, 4),
            new RobotCoordinate(5, 1),
            new RobotCoordinate(5, 5),
        },
        { // 2
            new RobotCoordinate(2, 2, true),
            new RobotCoordinate(1, 4),
            new RobotCoordinate(3, 1),
            new RobotCoordinate(4, 2),
            new RobotCoordinate(4, 4),
            new RobotCoordinate(5, 5),
        },
        { // 3
            new RobotCoordinate(1, 1),
            new RobotCoordinate(2, 1),
            new RobotCoordinate(3, 1, true),
            new RobotCoordinate(5, 1),
            new RobotCoordinate(1, 5),
            new RobotCoordinate(4, 5),
        },
        { // 4
            new RobotCoordinate(1, 1),
            new RobotCoordinate(2, 2, true),
            new RobotCoordinate(3, 1),
            new RobotCoordinate(3, 5),
            new RobotCoordinate(5, 1),
            new RobotCoordinate(5, 4),
        },
        { // 5
            new RobotCoordinate(1, 1),
            new RobotCoordinate(2, 2),
            new RobotCoordinate(2, 5),
            new RobotCoordinate(3, 1),
            new RobotCoordinate(4, 1, true),
            new RobotCoordinate(4, 4),
        },
        { // 6
            new RobotCoordinate(2, 1),
            new RobotCoordinate(2, 2),
            new RobotCoordinate(1, 3, true),
            new RobotCoordinate(3, 3),
            new RobotCoordinate(4, 2),
            new RobotCoordinate(4, 3),
        },
        { // 7
            new RobotCoordinate(1, 1),
            new RobotCoordinate(1, 4, true),
            new RobotCoordinate(3, 1),
            new RobotCoordinate(3, 3),
            new RobotCoordinate(3, 4),
            new RobotCoordinate(4, 4),
        },
        { // 8
            new RobotCoordinate(1, 1),
            new RobotCoordinate(1, 3, true),
            new RobotCoordinate(1, 5),
            new RobotCoordinate(2, 1),
            new RobotCoordinate(4, 5),
            new RobotCoordinate(5, 1),
        },
        { // 9
            new RobotCoordinate(1, 1),
            new RobotCoordinate(1, 5),
            new RobotCoordinate(2, 1),
            new RobotCoordinate(3, 3),
            new RobotCoordinate(4, 1, true),
            new RobotCoordinate(4, 4),
        },
        { // 10
            new RobotCoordinate(1, 1),
            new RobotCoordinate(1, 3),
            new RobotCoordinate(2, 1),
            new RobotCoordinate(2, 5, true),
            new RobotCoordinate(3, 1),
            new RobotCoordinate(5, 4),
        },
        { // 11
            new RobotCoordinate(1, 3),
            new RobotCoordinate(1, 5),
            new RobotCoordinate(2, 5, true),
            new RobotCoordinate(3, 1),
            new RobotCoordinate(4, 4),
            new RobotCoordinate(5, 2),
        },
        { // 12
            new RobotCoordinate(1, 1),
            new RobotCoordinate(1, 5),
            new RobotCoordinate(2, 1),
            new RobotCoordinate(2, 3, true),
            new RobotCoordinate(4, 5),
            new RobotCoordinate(5, 1),
        },
        { // 13
            new RobotCoordinate(1, 1),
            new RobotCoordinate(1, 3),
            new RobotCoordinate(1, 5),
            new RobotCoordinate(3, 1),
            new RobotCoordinate(5, 1),
            new RobotCoordinate(5, 5, true),
        },
        { // 14
            new RobotCoordinate(1, 1, true),
            new RobotCoordinate(1, 5),
            new RobotCoordinate(4, 1),
            new RobotCoordinate(4, 4),
            new RobotCoordinate(5, 3),
            new RobotCoordinate(5, 4),
        },
        { // 15
            new RobotCoordinate(1, 3),
            new RobotCoordinate(1, 5),
            new RobotCoordinate(2, 1, true),
            new RobotCoordinate(2, 5),
            new RobotCoordinate(5, 2),
            new RobotCoordinate(5, 5),
        },
        { // 16
            new RobotCoordinate(1, 1, true),
            new RobotCoordinate(2, 5),
            new RobotCoordinate(4, 1),
            new RobotCoordinate(5, 1),
            new RobotCoordinate(5, 4),
            new RobotCoordinate(5, 5),
        },
        { // 17
            new RobotCoordinate(1, 4),
            new RobotCoordinate(2, 1, true),
            new RobotCoordinate(3, 4),
            new RobotCoordinate(4, 1),
            new RobotCoordinate(4, 5),
            new RobotCoordinate(5, 2),
        },
        { // 18
            new RobotCoordinate(1, 1),
            new RobotCoordinate(1, 5),
            new RobotCoordinate(2, 1),
            new RobotCoordinate(4, 3, true),
            new RobotCoordinate(4, 5),
            new RobotCoordinate(5, 1),
        },
    };
    
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

        // Fill the board with unoccupied pieces.
        for (int line = 0; line < this.kBoardHeight; line++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                this.grid[line][column] = new RoundupTile();
                if (line == 0 || column == 0
                 || line == this.kBoardHeight-1 || column == this.kBoardWidth-1)
                {
                    this.grid[line][column].isEdgeTile = true;
                }
            }
        }
        this.grid[this.kBoardHeight/2][this.kBoardWidth/2].isCenterTile = true;
        
        this.placeRobots();
    }
    
    private void placeRobots()
    {
        for (RobotCoordinate robotCoordinate : this.boards[this.parent.getGame()])
        {
            if (robotCoordinate.isRed)
            {
                this.grid[robotCoordinate.row][robotCoordinate.column].robot = RoundupTile.Robot.red;
            }
            else
            {
                this.grid[robotCoordinate.row][robotCoordinate.column].robot = RoundupTile.Robot.green;
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
        
        RoundupTile tile = (RoundupTile)this.grid[row][column];
        // TODO: Select robot.
    }
}

