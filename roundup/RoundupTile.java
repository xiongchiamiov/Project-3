package roundup;

import java.awt.Toolkit;
import javax.swing.ImageIcon;

import gridgame.Renderable;
import gridgame.RenderDescriptor;

class RoundupTile extends ImageIcon implements Renderable
{
    public static enum Robot { none, red, green, dead };
    public static enum RobotDirection { up, right, down, left };

    private Robot robot = Robot.none;
    private RobotDirection robotDirection = RobotDirection.up;
    private boolean isDot = false;
    protected boolean isCenterTile = false;
    
    public RenderDescriptor getRenderDescriptor()
    {
        RenderDescriptor renderDescriptor = new RenderDescriptor();
        
        // TODO
        renderDescriptor.isImage = true;
        if (this.robot == Robot.none)
        {
            if (this.isDot)
            {
                renderDescriptor.text = "dot";
            }
            else
            {
                renderDescriptor.text = "empty";
            }
        }
        
        return renderDescriptor;
    }

    public String toString()
    {
        return this.getRenderDescriptor().text;
    }
}

