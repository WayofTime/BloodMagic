package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockImperfectRitualStone extends Block
{
    public BlockImperfectRitualStone()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	if(SpellHelper.isFakePlayer(player))
    	{
    		return false;
    	}
        {
            Block block = world.getBlockState(blockPos.add(0, 1, 0)).getBlock();

            if (block == Blocks.water)
            {
                if (!player.capabilities.isCreativeMode && !world.isRemote)
                {
                    SoulNetworkHandler.hurtPlayer(player, 5000);
                }

                if (!world.isRemote)
                {
                    world.addWeatherEffect(new EntityLightningBolt(world, blockPos.getX(), blockPos.getY() + 2, blockPos.getZ()));
                }

                world.getWorldInfo().setRaining(true);

                if (world.isRemote)
                {
                    world.setRainStrength(1.0F);
                    world.setThunderStrength(1.0f);
                }

                world.getWorldInfo().setThunderTime(0);
                world.getWorldInfo().setThundering(true);
                return true;
            } else if (block == Blocks.coal_block)
            {
                if (!player.capabilities.isCreativeMode && !world.isRemote)
                {
                    SoulNetworkHandler.hurtPlayer(player, 5000);
                }

                EntityZombie zomb = new EntityZombie(world);
                zomb.setPosition(blockPos.getX() + 0.5, blockPos.getY() + 2, blockPos.getZ() + 0.5);
                zomb.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2000));
                zomb.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 20000, 7));
                zomb.addPotionEffect(new PotionEffect(Potion.resistance.id, 20000, 3));

                if (!world.isRemote)
                {
                    world.spawnEntityInWorld(zomb);
                    world.addWeatherEffect(new EntityLightningBolt(world, blockPos.getX(), blockPos.getY() + 2, blockPos.getZ()));
                }

                return true;
            } else if (block == Blocks.lapis_block)
            {
                if (!player.capabilities.isCreativeMode && !world.isRemote)
                {
                    SoulNetworkHandler.hurtPlayer(player, 5000);
                }

                if (!world.isRemote)
                {
                    world.addWeatherEffect(new EntityLightningBolt(world, blockPos.getX(), blockPos.getY() + 2, blockPos.getZ()));
                    world.setWorldTime((world.getWorldTime() / 24000) * 24000 + 13800);
                }
            } else if (block == Blocks.bedrock)
            {
                if (!player.capabilities.isCreativeMode && !world.isRemote)
                {
                    SoulNetworkHandler.hurtPlayer(player, 5000);
                }

                if (!world.isRemote)
                {
                    world.addWeatherEffect(new EntityLightningBolt(world, blockPos.getX(), blockPos.getY() + 2, blockPos.getZ()));
                }

                player.addPotionEffect(new PotionEffect(Potion.resistance.id, 60 * 20, 1));
            }
        }
        return false;
    }
}
