package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ImperfectRitualStone extends Block
{
    public ImperfectRitualStone()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("imperfectRitualStone");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:ImperfectRitualStone");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOff, float yOff, float zOff)    {
{        
        if (SpellHelper.isFakePlayer(world, player))
        {
            return false;
        }
        else
        {
            Block block = world.getBlock(x, y + 1, z);

            if (block == Blocks.water)
            {
                if (!player.capabilities.isCreativeMode && !world.isRemote)
                {
                    EnergyItems.drainPlayerNetwork(player, 5000);
                }

                if (!world.isRemote)
                {
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 2, z));
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
                    EnergyItems.drainPlayerNetwork(player, 5000);
                }

                EntityZombie zomb = new EntityZombie(world);
                zomb.setPosition(x + 0.5, y + 2, z + 0.5);
                zomb.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2000));
                zomb.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 20000, 7));
                zomb.addPotionEffect(new PotionEffect(Potion.resistance.id, 20000, 3));

                if (!world.isRemote)
                {
                    world.spawnEntityInWorld(zomb);
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 2, z));
                }

                return true;
            } else if (block == Blocks.lapis_block)
            {
                if (!player.capabilities.isCreativeMode && !world.isRemote)
                {
                    EnergyItems.drainPlayerNetwork(player, 5000);
                }

                if (!world.isRemote)
                {
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 2, z));
                    world.setWorldTime((world.getWorldTime() / 24000) * 24000 + 13800);
                }
            } else if (block == Blocks.bedrock)
            {
                if (!player.capabilities.isCreativeMode && !world.isRemote)
                {
                    EnergyItems.drainPlayerNetwork(player, 5000);
                }

                if (!world.isRemote)
                {
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 2, z));
                }

                player.addPotionEffect(new PotionEffect(Potion.resistance.id, 60 * 20, 1));
            }
        }
        return false;
    }
}
