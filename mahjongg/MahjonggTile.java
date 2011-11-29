package mahjongg;

import java.awt.Toolkit;
import javax.swing.ImageIcon;

import gridgame.Renderable;
import gridgame.RenderDescriptor;

class MahjonggTile extends ImageIcon implements Comparable<MahjonggTile>, Renderable
{
    public enum Suit {Bamboo, Dots, Characters};
    
    private Suit suit;
    private int rank;
    
    public MahjonggTile(Suit suit, int rank)
    {
        //super(Toolkit.getDefaultToolkit().getImage(Mahjongg.class.getResource("img/" + suit.name().substring(0, 1) + rank + ".JPG")));
        if (rank < 1 || rank > 7)
        {
            throw new IllegalArgumentException("Rank must be between 1 and 7");
        }
        this.suit = suit;
        this.rank = rank;
    }
    
    public boolean equals(Object other)
    {
        if (other instanceof MahjonggTile)
        {
            MahjonggTile otherTile = (MahjonggTile)other;
            if (this.suit == otherTile.suit && this.rank == otherTile.rank)
            {
                return true;
            }
        }
        
        return false;
    }

    public int compareTo(MahjonggTile other)
    {
        if (this.suit == other.suit)
        {
            return new Integer(this.rank).compareTo(other.rank);
        }
        
        return new Integer(this.suit.ordinal()).compareTo(other.suit.ordinal());
    }
    
    public RenderDescriptor getRenderDescriptor()
    {
        RenderDescriptor renderDescriptor = new RenderDescriptor();
        renderDescriptor.isImage = true;
        renderDescriptor.text = suit.name().substring(0, 1) + rank;
        return renderDescriptor;
    }
    
    public String toString()
    {
        return this.suit.name() + " " + this.rank;
    }
}

