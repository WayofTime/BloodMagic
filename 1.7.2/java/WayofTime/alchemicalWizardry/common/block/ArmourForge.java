package WayofTime.alchemicalWizardry.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.ArmourComponent;
import WayofTime.alchemicalWizardry.common.items.BoundArmour;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ArmourForge extends Block
{
    public static List<ArmourComponent> helmetList = new ArrayList();
    public static List<ArmourComponent> plateList = new ArrayList();
    public static List<ArmourComponent> leggingsList = new ArrayList();
    public static List<ArmourComponent> bootsList = new ArrayList();

    public ArmourForge()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("armourForge");
        //setUnlocalizedName("armourForge");
        // TODO Auto-generated constructor stub
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:SoulForge");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
    {
        if (world.isRemote)
        {
            return false;
        }

        int armourType = getArmourType(world, x, y, z);

        if (armourType == -1)
        {
            return false;
        }

        int direction = getDirectionForArmourType(world, x, y, z, armourType);

        if (!isParadigmValid(armourType, direction, world, x, y, z))
        {
            return false;
        }

        List<ArmourComponent> list = null;
        ItemStack armourPiece = null;

        switch (armourType)
        {
            case 0:
                list = plateList;
                armourPiece = new ItemStack(ModItems.boundPlate, 1, 0);
                break;

            case 1:
                list = leggingsList;
                armourPiece = new ItemStack(ModItems.boundLeggings, 1, 0);
                break;

            case 2:
                list = helmetList;
                armourPiece = new ItemStack(ModItems.boundHelmet, 1, 0);
                break;

            case 3:
                list = bootsList;
                armourPiece = new ItemStack(ModItems.boundBoots, 1, 0);
                break;
        }

        if (list == null)
        {
            return false;
        }

        if (armourPiece == null)
        {
            return false;
        }

        if (armourPiece.stackTagCompound == null)
        {
            armourPiece.setTagCompound(new NBTTagCompound());
        }

        for (ArmourComponent ac : list)
        {
            int xOff = ac.getXOff();
            int zOff = ac.getZOff();
            TileEntity tileEntity;

            switch (direction)
            {
                case 1:
                    tileEntity = world.getTileEntity(x + xOff, y, z - zOff);
                    break;

                case 2:
                    tileEntity = world.getTileEntity(x + zOff, y, z + xOff);
                    break;

                case 3:
                    tileEntity = world.getTileEntity(x - xOff, y, z + zOff);
                    break;

                case 4:
                    tileEntity = world.getTileEntity(x - zOff, y, z - xOff);
                    break;

                case 5:
                    tileEntity = world.getTileEntity(x + xOff, y + zOff, z);
                    break;

                case 6:
                    tileEntity = world.getTileEntity(x, y + zOff, z + xOff);
                    break;

                default:
                    tileEntity = null;
            }

            if (tileEntity instanceof TESocket)
            {
                ItemStack itemStack = ((TESocket) tileEntity).getStackInSlot(0);
                int xCoord = tileEntity.xCoord;
                int yCoord = tileEntity.yCoord;
                int zCoord = tileEntity.zCoord;
                ((TESocket) tileEntity).setInventorySlotContents(0, null);
                world.setBlockToAir(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

                for (int i = 0; i < 8; i++)
                {
                    //PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, world.provider.dimensionId, TEAltar.getParticlePacket(xCoord, yCoord, zCoord, (short) 1));
                    SpellHelper.sendIndexedParticleToAllAround(world, xCoord, yCoord, zCoord, 20, world.provider.dimensionId, 1, xCoord, yCoord, zCoord);
                }

                if (itemStack != null)
                {
                    Item item = itemStack.getItem();

                    if (item instanceof ArmourUpgrade)
                    {
                        ((BoundArmour) armourPiece.getItem()).hasAddedToInventory(armourPiece, itemStack.copy());
                        ((TESocket) tileEntity).setInventorySlotContents(0, null);
                    }
                }
            }
        }

        if (armourPiece != null)
        {
            int xOff = (world.rand.nextInt(11) - 5);
            int zOff = (int) (Math.sqrt(25 - xOff * xOff) * (world.rand.nextInt(2) - 0.5) * 2);
            world.addWeatherEffect(new EntityLightningBolt(world, x + xOff, y + 5, z + zOff));
            world.spawnEntityInWorld(new EntityItem(world, x, y + 1, z, armourPiece));
        }

        return true;
    }

    //0 for plate, 1 for leggings, 2 for helmet, 3 for boots
    public int getArmourType(World world, int x, int y, int z)
    {
        for (int i = 0; i <= 3; i++)
        {
            if (getDirectionForArmourType(world, x, y, z, i) != -1)
            {
                return i;
            }
        }

        return -1;
    }

    public int getDirectionForArmourType(World world, int x, int y, int z, int armourType)
    {
        for (int i = 1; i <= 6; i++)
        {
            if (isParadigmValid(armourType, i, world, x, y, z))
            {
                return i;
            }
        }

        return -1;
    }

    public boolean isParadigmValid(int armourType, int direction, World world, int x, int y, int z)
    {
        List<ArmourComponent> list = null;

        switch (armourType)
        {
            case 0:
                list = plateList;
                break;

            case 1:
                list = leggingsList;
                break;

            case 2:
                list = helmetList;
                break;

            case 3:
                list = bootsList;
                break;
        }

        if (list == null)
        {
            return false;
        }

        for (ArmourComponent ac : list)
        {
            int xOff = ac.getXOff();
            int zOff = ac.getZOff();

            switch (direction)
            {
                case 1:
                    if (!(world.getTileEntity(x + xOff, y, z - zOff) instanceof TESocket))
                    {
                        return false;
                    }

                    break;

                case 2:
                    if (!(world.getTileEntity(x + zOff, y, z + xOff) instanceof TESocket))
                    {
                        return false;
                    }

                    break;

                case 3:
                    if (!(world.getTileEntity(x - xOff, y, z + zOff) instanceof TESocket))
                    {
                        return false;
                    }

                    break;

                case 4:
                    if (!(world.getTileEntity(x - zOff, y, z - xOff) instanceof TESocket))
                    {
                        return false;
                    }

                    break;

                case 5:
                    if (!(world.getTileEntity(x + xOff, y + zOff, z) instanceof TESocket))
                    {
                        return false;
                    }

                    break;

                case 6:
                    if (!(world.getTileEntity(x, y + zOff, z + xOff) instanceof TESocket))
                    {
                        return false;
                    }

                    break;

                default:
                    return false;
            }
        }

        return true;
    }

    public static void initializeRecipes()
    {
        helmetList.add(new ArmourComponent(-1, 1));
        helmetList.add(new ArmourComponent(0, 1));
        helmetList.add(new ArmourComponent(1, 1));
        helmetList.add(new ArmourComponent(-1, 0));
        helmetList.add(new ArmourComponent(1, 0));
        bootsList.add(new ArmourComponent(-1, 1));
        bootsList.add(new ArmourComponent(1, 1));
        bootsList.add(new ArmourComponent(-1, 0));
        bootsList.add(new ArmourComponent(1, 0));
        plateList.add(new ArmourComponent(-1, 0));
        plateList.add(new ArmourComponent(1, 0));
        plateList.add(new ArmourComponent(-1, -1));
        plateList.add(new ArmourComponent(0, -1));
        plateList.add(new ArmourComponent(1, -1));
        plateList.add(new ArmourComponent(-1, -2));
        plateList.add(new ArmourComponent(0, -2));
        plateList.add(new ArmourComponent(1, -2));
        leggingsList.add(new ArmourComponent(-1, 1));
        leggingsList.add(new ArmourComponent(0, 1));
        leggingsList.add(new ArmourComponent(1, 1));
        leggingsList.add(new ArmourComponent(-1, 0));
        leggingsList.add(new ArmourComponent(1, 0));
        leggingsList.add(new ArmourComponent(-1, -1));
        leggingsList.add(new ArmourComponent(1, -1));
    }
}
