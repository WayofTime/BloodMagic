package WayofTime.alchemicalWizardry.common.block;

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
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ImperfectRitualStone extends Block
{
    public ImperfectRitualStone()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("imperfectRitualStone");
        // TODO Auto-generated constructor stub
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:ImperfectRitualStone");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOff, float yOff, float zOff)
    {
        //ItemStack ist = player.getItemInUse();
        //if (!world.isRemote)
        {
            Block block = world.getBlock(x, y + 1, z);

            if (block == Blocks.water)
            {
                if (!player.capabilities.isCreativeMode && world.isRemote)
                {
                	
                    //PacketDispatcher.sendPacketToServer(PacketHandler.getPacket(SpellHelper.getUsername(player) , -5000, 0));
                }

                if (!world.isRemote)
                {
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 2, z));
//                    if (!player.capabilities.isCreativeMode)
//                    {
//                        PacketDispatcher.sendPacketToServer(PacketHandler.getPacket(player.getEntityName(), -5000, 0));
//                    }
                }

                world.getWorldInfo().setRaining(true);

                if (world.isRemote)
                {
                    world.setRainStrength(1.0F);
                }

                world.setThunderStrength(1.0f);
                world.getWorldInfo().setThunderTime(0);
                world.getWorldInfo().setThundering(true);
                return true;
            } else if (block == Blocks.coal_block)
            {
                if (!player.capabilities.isCreativeMode && world.isRemote)
                {
                	EnergyItems.drainPlayerNetwork(player, 5000);
                }

                //EntityFallenAngel zomb = new EntityFallenAngel(world);
                EntityZombie zomb = new EntityZombie(world);
                zomb.setPosition(x + 0.5, y + 2, z + 0.5);
                //			zomb.setCurrentItemOrArmor(4, new ItemStack(Item.helmetIron.itemID,1,0));
                //			zomb.setCurrentItemOrArmor(3, new ItemStack(Item.plateIron.itemID,1,0));
                //			zomb.setCurrentItemOrArmor(2, new ItemStack(Item.legsIron.itemID,1,0));
                //			zomb.setCurrentItemOrArmor(1, new ItemStack(Item.bootsIron.itemID,1,0));
                //zomb.setCurrentItemOrArmor(0, new ItemStack(AlchemicalWizardry.energySword.itemID,1,0));
                zomb.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2000));
                zomb.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 20000, 7));
                zomb.addPotionEffect(new PotionEffect(Potion.resistance.id, 20000, 3));

                if (!world.isRemote)
                {
                    world.spawnEntityInWorld(zomb);
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 2, z));
//                    if (!player.capabilities.isCreativeMode)
//                    {
//                        PacketDispatcher.sendPacketToServer(PacketHandler.getPacket(player.getEntityName(), -5000, 0));
//                    }
                }

                return true;
            } else if (block== Blocks.lapis_block)
            {
                if (!player.capabilities.isCreativeMode && world.isRemote)
                {
                	EnergyItems.drainPlayerNetwork(player, 5000);
                }

                if (!world.isRemote)
                {
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 2, z));
                    world.setWorldTime((world.getWorldTime() / 24000) * 24000 + 13800);
//                    if (!player.capabilities.isCreativeMode)
//                    {
//                        PacketDispatcher.sendPacketToServer(PacketHandler.getPacket(player.getEntityName(), -5000, 0));
//                    }
                }
            } else if (block == Blocks.bedrock)
            {
                if (!player.capabilities.isCreativeMode && world.isRemote)
                {
                    EnergyItems.drainPlayerNetwork(player, 5000);
                }

                if (!world.isRemote)
                {
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 2, z));
                    //world.setWorldTime((world.getWorldTime()/24000)*24000+13800);
//                    if (!player.capabilities.isCreativeMode)
//                    {
//                        PacketDispatcher.sendPacketToServer(PacketHandler.getPacket(player.getEntityName(), -5000, 0));
//                    }
                }

                player.addPotionEffect(new PotionEffect(Potion.resistance.id, 60 * 20, 1));
            }
        }
        return false;
    }
}
