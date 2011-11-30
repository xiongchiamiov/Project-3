package mahjongg;

import gridgame.GridBoard;
import gridgame.Renderable;

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
}

