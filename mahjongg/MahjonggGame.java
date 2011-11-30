package mahjongg;

import gridgame.GridBoard;
import gridgame.GridGame;
import gridgame.GridStatus;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.List;
import java.util.Arrays;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;

public class MahjonggGame extends GridGame
{
    protected MahjonggBoard gridBoard;
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
        this.setGame(1);
        this.gridBoard = new MahjonggBoard();
        this.gridBoard.setParent(this);
        this.gridBoard.resetBoard();

        this.gridStatus = new MahjonggStatus();
    }
    
    public void makeMove(int row, int col)
    {
        // TODO
    }
    
    public void restart()
    {
        // TODO
        this.gridBoard.resetBoard();
        //this.secondsElapsed = 0;
        //updateStatusBar();
        setChanged();
        notifyObservers(this.getGame());
    }
    
    public List<Action> getMenuActions()
    {
        return Arrays.asList(new Action[]{
            new RestartAction("Restart", KeyStroke.getKeyStroke('R', InputEvent.ALT_MASK)),
            new NewGameAction("New Game", KeyStroke.getKeyStroke('N', InputEvent.ALT_MASK)),
            new HintAction("Hint", KeyStroke.getKeyStroke('H', InputEvent.ALT_MASK)),
            new CheatAction("Cheat", KeyStroke.getKeyStroke('C', InputEvent.ALT_MASK)),
            new QuitAction("Quit", KeyStroke.getKeyStroke('Q', InputEvent.ALT_MASK)),
        });
    }

    class RestartAction extends AbstractAction
    {
        public RestartAction(String label, KeyStroke accelKey)
        {
            super(label);
            putValue(ACCELERATOR_KEY, accelKey);
        }

        public void actionPerformed(ActionEvent e)
        {
            MahjonggGame.this.restart();
        }
    }

    class NewGameAction extends AbstractAction
    {
        public NewGameAction(String label, KeyStroke accelKey)
        {
            super(label);
            putValue(ACCELERATOR_KEY, accelKey);
        }

        public void actionPerformed(ActionEvent e)
        {
            MahjonggGame.this.incrementGame();
            MahjonggGame.this.restart();
        }
    }

    class HintAction extends AbstractAction
    {
        public HintAction(String label, KeyStroke accelKey)
        {
            super(label);
            putValue(ACCELERATOR_KEY, accelKey);
        }

        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Hint");
        }
    }

    class CheatAction extends AbstractAction
    {
        public CheatAction(String label, KeyStroke accelKey)
        {
            super(label);
            putValue(ACCELERATOR_KEY, accelKey);
        }

        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Cheat");
        }
    }

    class QuitAction extends AbstractAction
    {
        public QuitAction(String label, KeyStroke accelKey)
        {
            super(label);
            putValue(ACCELERATOR_KEY, accelKey);
        }

        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }
}

