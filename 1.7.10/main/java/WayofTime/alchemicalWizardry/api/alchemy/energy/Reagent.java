package WayofTime.alchemicalWizardry.api.alchemy.energy;

public class Reagent
{
    public final String name;

    public static final int REAGENT_SIZE = 1000;

    private int colourRed = 0;
    private int colourGreen = 0;
    private int colourBlue = 0;
    private int colourIntensity = 255;

    public Reagent(String name)
    {
        this.name = name;
    }

    public void setColour(int red, int green, int blue, int intensity)
    {
        this.colourRed = red;
        this.colourGreen = green;
        this.colourBlue = blue;
        this.colourIntensity = intensity;
    }

    public int getColourRed()
    {
        return colourRed;
    }

    public int getColourGreen()
    {
        return colourGreen;
    }

    public int getColourBlue()
    {
        return colourBlue;
    }

    public int getColourIntensity()
    {
        return colourIntensity;
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof Reagent ? this == o && name.equals(((Reagent) o).name) : false;
    }
}
