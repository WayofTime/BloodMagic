package WayofTime.alchemicalWizardry.common.block;

public class BlockEfficiencyRune extends BlockBloodRune
{
    public BlockEfficiencyRune()
    {
        super();
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public int getRuneEffect(int metaData)
    {
        return 2;
    }
}
