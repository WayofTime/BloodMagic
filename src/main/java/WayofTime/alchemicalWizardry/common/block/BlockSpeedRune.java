package WayofTime.alchemicalWizardry.common.block;

public class BlockSpeedRune extends BlockBloodRune
{
    public BlockSpeedRune()
    {
        super();
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public int getRuneEffect(int metaData)
    {
        return 1;
    }
}
