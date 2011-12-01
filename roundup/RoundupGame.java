package roundup;

import gridgame.GridBoard;
import gridgame.GridGame;
import gridgame.GridStatus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.List;
import java.util.Arrays;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class RoundupGame extends GridGame
{
    protected RoundupBoard gridBoard;
    protected GridStatus gridStatus;

    private static final int kMaxGameNumber = 18;

    protected int moves = 0;
    protected int secondsElapsed = 0;
    private boolean gameLost = false;
    
    public GridBoard getBoardToView()
    {
        return this.gridBoard;
    }
    
    public GridStatus getStatusToView()
    {
        return this.gridStatus;
    }
    
    public void init()
    {
        this.setGame(1);
        this.gridBoard = new RoundupBoard();
        this.gridBoard.setParent(this);
        this.gridBoard.resetBoard();

        this.gridStatus = new RoundupStatus();
        this.startTimer();
    }
    
    public void makeMove(int row, int col)
    {
        this.gridBoard.clickTile(row, col);
        this.updateStatusBar();
        setChanged();
        notifyObservers();
    }

    public void restart()
    {
        this.gridBoard.resetBoard();
        this.updateStatusBar();
        setChanged();
        notifyObservers(this.getGame());
    }

    private void selectGame()
    {
        String gameNumber = (String)JOptionPane.showInputDialog(null, "Enter desired game number (1 - 18):", "Select Game", JOptionPane.QUESTION_MESSAGE, null, null, "");
        if (gameNumber != null)
        {
            int parsedGameNumber = Integer.parseInt(gameNumber);
            if (parsedGameNumber > 0 && parsedGameNumber <= this.kMaxGameNumber)
            {
                this.setGame(parsedGameNumber);
                this.secondsElapsed = 0;
                this.restart();
            }
        }
    }
    
    protected void startTimer()
    {
        // Every 1 second.
        Timer timer = new Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                RoundupGame.this.secondsElapsed++;
                RoundupGame.this.updateStatusBar();
            }
        });
        
        this.updateStatusBar();
        timer.start();
    }
    
    protected void updateStatusBar()
    {
        String time = this.secondsElapsed / 60 + ":" + String.format("%02d", this.secondsElapsed % 60);
        if (this.gameLost)
        {
            this.gridStatus.setLabelText("LOSE " + time);
        }
        else
        {
            this.gridStatus.setLabelText("Moves: " + this.moves + "  " + time);
        }
    }
    
    public List<Action> getMenuActions()
    {
        return Arrays.asList(new Action[]{
            new RestartAction("Restart", KeyStroke.getKeyStroke('R', InputEvent.ALT_MASK)),
            new NewGameAction("New Game", KeyStroke.getKeyStroke('N', InputEvent.ALT_MASK)),
            new SelectGameAction("Select Game", KeyStroke.getKeyStroke('G', InputEvent.ALT_MASK)),
            new HallOfFameAction("Hall of Fame", KeyStroke.getKeyStroke('H', InputEvent.ALT_MASK)),
            new AboutAction("About", KeyStroke.getKeyStroke('A', InputEvent.ALT_MASK)),
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
            RoundupGame.this.restart();
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
            RoundupGame.this.incrementGame();
            RoundupGame.this.secondsElapsed = 0;
            RoundupGame.this.restart();
        }
    }

    class SelectGameAction extends AbstractAction
    {
        public SelectGameAction(String label, KeyStroke accelKey)
        {
            super(label);
            putValue(ACCELERATOR_KEY, accelKey);
        }

        public void actionPerformed(ActionEvent e)
        {
            RoundupGame.this.selectGame();
            RoundupGame.this.setChanged();
            RoundupGame.this.notifyObservers();
        }
    }

    class HallOfFameAction extends AbstractAction
    {
        public HallOfFameAction(String label, KeyStroke accelKey)
        {
            super(label);
            putValue(ACCELERATOR_KEY, accelKey);
        }

        public void actionPerformed(ActionEvent e)
        {
            RoundupGame.this.showHighScores();
            RoundupGame.this.setChanged();
            RoundupGame.this.notifyObservers();
        }
    }

    class AboutAction extends AbstractAction
    {
        public AboutAction(String label, KeyStroke accelKey)
        {
            super(label);
            putValue(ACCELERATOR_KEY, accelKey);
        }

        public void actionPerformed(ActionEvent e)
        {
            // TODO: Show about dialog.
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

