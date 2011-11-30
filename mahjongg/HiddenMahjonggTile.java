package mahjongg;

import gridgame.RenderDescriptor;

public class HiddenMahjonggTile extends MahjonggTile
{
    public HiddenMahjonggTile()
    {
        // This doesn't really matter, but the compiler needs it.
        super(MahjonggTile.Suit.Null, 1);
    }
    
    public RenderDescriptor getRenderDescriptor()
    {
        RenderDescriptor renderDescriptor = new RenderDescriptor();
        renderDescriptor.text = "";
        return renderDescriptor;
    }
}

