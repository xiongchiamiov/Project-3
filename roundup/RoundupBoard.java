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

    private int selectedRobotRow = 0;
    private int selectedRobotColumn = 0;

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
        
        this.selectedRobotRow = row;
        this.selectedRobotColumn = column;
    }

    protected void go(final RoundupTile.RobotDirection direction)
    {
        // Don't do anything if we don't have a robot selected.
        if (this.selectedRobotRow == 0 || this.selectedRobotColumn == 0)
        {
            return;
        }
        
        this.clearDots();
        // Temporarily pull robot off the board for the sake of our recursive algorithm.
        RoundupTile.Robot movingRobot = this.grid[this.selectedRobotRow][this.selectedRobotColumn].robot;
        this.grid[this.selectedRobotRow][this.selectedRobotColumn].robot = RoundupTile.Robot.none;
        this.moveRobot(this.selectedRobotRow, this.selectedRobotColumn, direction, movingRobot);
        
        this.parent.moves++;
        // Require the user to select a robot each time.
        this.selectedRobotRow = 0;
        this.selectedRobotColumn = 0;
    }
    
    private RoundupTile.Robot moveRobot(int row, int column, RoundupTile.RobotDirection direction, RoundupTile.Robot robot)
    {
        // Stop moving if we've hit a border.
        if (row == 0 || column == 0
         || row == this.kBoardHeight-1 || column == this.kBoardWidth-1)
        {
            this.grid[row][column].robot = RoundupTile.Robot.dead;
            this.grid[row][column].robotDirection = direction;
            this.parent.gameLost = true;
            
            return RoundupTile.Robot.none;
        }
        
        // Stop moving if we hit another robot.
        if (this.grid[row][column].robot != RoundupTile.Robot.none)
        {
            return robot;
        }
        
        // Try moving to the next tile.
        this.grid[row][column].robot = this.moveRobot(row + direction.row,
                                                      column + direction.column,
                                                      direction,
                                                      robot);
        this.grid[row][column].robotDirection = direction;
        
        // Do we have a robot here?
        if (this.grid[row][column].robot == RoundupTile.Robot.none)
        {
            this.grid[row][column].isDot = true;
        }
        
        // Tell the previous tile they don't have a robot.
        return RoundupTile.Robot.none;
    }
    
    private void clearDots()
    {
        for (int line = 0; line < this.kBoardHeight; line++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                this.grid[line][column].isDot = false;
            }
        }
    }
}

