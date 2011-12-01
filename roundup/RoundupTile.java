package roundup;

import java.awt.Toolkit;
import javax.swing.ImageIcon;

import gridgame.Renderable;
import gridgame.RenderDescriptor;

class RoundupTile extends ImageIcon implements Renderable
{
    public RenderDescriptor getRenderDescriptor()
    {
        RenderDescriptor renderDescriptor = new RenderDescriptor();
        // TODO
        return renderDescriptor;
    }

    public String toString()
    {
        return this.getRenderDescriptor().text;
    }
}

