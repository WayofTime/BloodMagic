package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.ItemSpellMultiTool;
import WayofTime.alchemicalWizardry.api.spell.IDigAreaEffect;

public class DigAreaEffect implements IDigAreaEffect
{
    protected int powerUpgrades;
    protected int potencyUpgrades;
    protected int costUpgrades;

    public DigAreaEffect(int power, int potency, int cost)
    {
        this.powerUpgrades = power;
        this.potencyUpgrades = potency;
        this.costUpgrades = cost;
    }

    @Override
    public int digSurroundingArea(ItemStack container, World world, EntityPlayer player, MovingObjectPosition blockPos, String usedToolClass, float blockHardness, int harvestLvl, ItemSpellMultiTool itemTool)
    {
        if (!blockPos.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK))
        {
            return 0;
        }

        int x = blockPos.func_178782_a().getX(); //BlockPos
        int y = blockPos.func_178782_a().getY();
        int z = blockPos.func_178782_a().getZ();
        EnumFacing sidehit = blockPos.field_178784_b;

        for (int xPos = x - 1; xPos <= x + 1; xPos++)
        {
            for (int yPos = y - 1; yPos <= y + 1; yPos++)
            {
                for (int zPos = z - 1; zPos <= z + 1; zPos++)
                {
                	BlockPos newPos = new BlockPos(xPos, yPos, zPos);
                    ModItems.customTool.onBlockStartBreak(container, newPos, player);
                }
            }
        }

        return 0;
    }
    
    public float getHardnessDifference()
    {
        return 1.5f;
    }
}
