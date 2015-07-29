package WayofTime.alchemicalWizardry.common.block;

public class BlockRuneOfSelfSacrifice extends BlockBloodRune
{
    public BlockRuneOfSelfSacrifice()
    {
        super();
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public int getRuneEffect(int metaData)
    {
        return 4;
    }
}
