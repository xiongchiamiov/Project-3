package roundup;

import java.awt.Toolkit;
import javax.swing.ImageIcon;

import gridgame.Renderable;
import gridgame.RenderDescriptor;

class RoundupTile extends ImageIcon implements Renderable
{
    public static enum Robot { none, red, green, dead };
    public static enum RobotDirection { up, right, down, left };

    protected Robot robot = Robot.none;
    protected RobotDirection robotDirection = RobotDirection.up;
    protected boolean isDot = false;
    protected boolean isCenterTile = false;
    protected boolean isEdgeTile = false;
    
    public RenderDescriptor getRenderDescriptor()
    {
        RenderDescriptor renderDescriptor = new RenderDescriptor();
        
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
        else
        {
            renderDescriptor.text = this.robot.toString() + this.robotDirection.toString();
        }
        
        return renderDescriptor;
    }

    public String toString()
    {
        return this.getRenderDescriptor().text;
    }
}

