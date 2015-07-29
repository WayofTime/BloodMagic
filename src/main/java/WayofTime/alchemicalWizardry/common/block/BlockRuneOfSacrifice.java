package WayofTime.alchemicalWizardry.common.block;

public class BlockRuneOfSacrifice extends BlockBloodRune
{
    public BlockRuneOfSacrifice()
    {
        super();
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public int getRuneEffect(int metaData)
    {
        return 3;
    }
}
