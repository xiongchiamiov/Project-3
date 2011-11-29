package mahjongg;

import gridgame.GridBoard;
import gridgame.GridGame;
import gridgame.GridStatus;

public class MahjonggGame extends GridGame
{
    protected GridBoard gridBoard;
    protected GridStatus gridStatus;
    
    public GridBoard getBoardToView()
    {
        // TODO: Probably supposed to be a copy.
        return this.gridBoard;
    }
    
    public GridStatus getStatusToView()
    {
        // TODO: Probably supposed to be a copy.
        return this.gridStatus;
    }
    
    public void init()
    {
        // TODO
        this.gridBoard = new MahjonggBoard<MahjonggTile>();
        this.gridBoard.setParent(this);

        this.gridStatus = new MahjonggStatus();
    }
    
    public void makeMove(int row, int col)
    {
        // TODO
    }
    
    public void restart()
    {
        // TODO
    }
}

