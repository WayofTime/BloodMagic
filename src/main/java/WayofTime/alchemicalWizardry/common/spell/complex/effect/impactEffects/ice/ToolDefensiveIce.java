package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.SummonToolEffect;

public class ToolDefensiveIce extends SummonToolEffect
{
    public ToolDefensiveIce(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public int onSummonTool(ItemStack toolStack, World world, Entity entity)
    {
        int horizRadius = this.powerUpgrades * 2 + 2;
        int vertRadius = this.powerUpgrades * 3 + 2;
        List<Entity> entityList = SpellHelper.getEntitiesInRange(world, entity.posX, entity.posY, entity.posZ, horizRadius, vertRadius);

        for (Entity ent : entityList)
        {
            if (ent instanceof EntityLivingBase && !ent.equals(entity))
            {
                ((EntityLivingBase) ent).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, this.potencyUpgrades * 2));
            }
        }

        BlockPos pos = entity.getPosition();

        for (int x = -horizRadius; x <= horizRadius; x++)
        {
            for (int y = -vertRadius; y <= vertRadius; y++)
            {
                for (int z = -horizRadius; z <= horizRadius; z++)
                {
                	BlockPos newPos = pos.add(x, y, z);
                    SpellHelper.freezeWaterBlock(world, newPos);
                    if (world.isSideSolid(newPos, EnumFacing.UP) && world.isAirBlock(newPos.offsetUp()))
                    {
                        world.setBlockState(newPos.offsetUp(), Blocks.snow_layer.getDefaultState());
                    }
                }
            }
        }

        return 0;
    }
}
