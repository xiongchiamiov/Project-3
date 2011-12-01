package mines;

import java.awt.Toolkit;
import javax.swing.ImageIcon;

import gridgame.Renderable;
import gridgame.RenderDescriptor;

class MinesTile extends ImageIcon implements Renderable
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

