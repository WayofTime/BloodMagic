package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.items.ItemSpellMultiTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.DigAreaEffect;

public class ToolEnvironmentalEarth extends DigAreaEffect
{
    public ToolEnvironmentalEarth(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public int digSurroundingArea(ItemStack container, World world, EntityPlayer player, MovingObjectPosition blockPos, String usedToolClass, float blockHardness, int harvestLvl, ItemSpellMultiTool itemTool)
    {
        if (!blockPos.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK))
        {
            return 0;
        }

        BlockPos pos = blockPos.func_178782_a();
        EnumFacing sidehit = blockPos.field_178784_b;

        int radius = 2;
        int depth = 5;

        depth--;

        int posX = radius;
        int negX = radius;
        int posY = radius;
        int negY = radius;
        int posZ = radius;
        int negZ = radius;

        switch (sidehit)
        {
            case UP:
                posY = 0;
                negY = depth;
                break;
            case DOWN:
                negY = 0;
                posY = depth;
                break;
            case SOUTH:
                posZ = 0;
                negZ = depth;
                break;
            case NORTH:
                negZ = 0;
                posZ = depth;
                break;
            case WEST:
                negX = 0;
                posX = depth;
                break;
            case EAST:
                posX = 0;
                negX = depth;
                break;

            default:
        }

        for (int i = -negX; i <= posX; i++)
        {
            for (int j = -negY; j <= posY; j++)
            {
                for (int k = -negZ; k <= posZ; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                    itemTool.onBlockStartBreak(container, newPos, player);
                }
            }
        }

        return 0;
    }
}
