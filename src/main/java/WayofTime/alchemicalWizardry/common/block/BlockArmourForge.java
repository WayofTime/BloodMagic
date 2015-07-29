package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.ArmourComponent;
import WayofTime.alchemicalWizardry.common.items.armour.BoundArmour;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockArmourForge extends Block
{
    public static List<ArmourComponent> helmetList = new ArrayList<ArmourComponent>();
    public static List<ArmourComponent> plateList = new ArrayList<ArmourComponent>();
    public static List<ArmourComponent> leggingsList = new ArrayList<ArmourComponent>();
    public static List<ArmourComponent> bootsList = new ArrayList<ArmourComponent>();

    public BlockArmourForge()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return false;
        }

        int armourType = getArmourType(world, blockPos);

        if (armourType == -1)
        {
            return false;
        }

        int direction = getDirectionForArmourType(world, blockPos, armourType);

        if (!isParadigmValid(armourType, direction, world, blockPos))
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

        if (armourPiece.getTagCompound() == null)
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
                    tileEntity = world.getTileEntity(blockPos.add(xOff, 0, -zOff));
                    break;

                case 2:
                    tileEntity = world.getTileEntity(blockPos.add(zOff, 0, xOff));
                    break;

                case 3:
                    tileEntity = world.getTileEntity(blockPos.add(-xOff, 0, zOff));
                    break;

                case 4:
                    tileEntity = world.getTileEntity(blockPos.add(-zOff, 0, -xOff));
                    break;

                case 5:
                    tileEntity = world.getTileEntity(blockPos.add(xOff, zOff, 0));
                    break;

                case 6:
                    tileEntity = world.getTileEntity(blockPos.add(0, zOff, xOff));
                    break;

                default:
                    tileEntity = null;
            }

            if (tileEntity instanceof TESocket)
            {
                ItemStack itemStack = ((TESocket) tileEntity).getStackInSlot(0);
                BlockPos tilePos = tileEntity.getPos();
                ((TESocket) tileEntity).setInventorySlotContents(0, null);
                world.setBlockToAir(tilePos);

                for (int i = 0; i < 8; i++)
                {
                    SpellHelper.sendIndexedParticleToAllAround(world, tilePos, 20, world.provider.getDimensionId(), 1, tilePos);
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
            world.addWeatherEffect(new EntityLightningBolt(world, blockPos.getX() + xOff, blockPos.getY() + 5, blockPos.getZ() + zOff));
            world.spawnEntityInWorld(new EntityItem(world, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), armourPiece));
        }

        return true;
    }

    //0 for plate, 1 for leggings, 2 for helmet, 3 for boots
    public int getArmourType(World world, BlockPos blockPos)
    {
        for (int i = 0; i <= 3; i++)
        {
            if (getDirectionForArmourType(world, blockPos, i) != -1)
            {
                return i;
            }
        }

        return -1;
    }

    public int getDirectionForArmourType(World world, BlockPos blockPos, int armourType)
    {
        for (int i = 1; i <= 6; i++)
        {
            if (isParadigmValid(armourType, i, world, blockPos))
            {
                return i;
            }
        }

        return -1;
    }

    public boolean isParadigmValid(int armourType, int direction, World world, BlockPos blockPos)
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
                    if (!(world.getTileEntity(blockPos.add(xOff, 0, -zOff)) instanceof TESocket))
                    {
                        return false;
                    }

                    break;

                case 2:
                    if (!(world.getTileEntity(blockPos.add(zOff, 0, xOff)) instanceof TESocket))
                    {
                        return false;
                    }

                    break;

                case 3:
                    if (!(world.getTileEntity(blockPos.add(-xOff, 0, zOff)) instanceof TESocket))
                    {
                        return false;
                    }

                    break;

                case 4:
                    if (!(world.getTileEntity(blockPos.add(-zOff, 0, -xOff)) instanceof TESocket))
                    {
                        return false;
                    }

                    break;

                case 5:
                    if (!(world.getTileEntity(blockPos.add(xOff, zOff, 0)) instanceof TESocket))
                    {
                        return false;
                    }

                    break;

                case 6:
                    if (!(world.getTileEntity(blockPos.add(0, zOff, xOff)) instanceof TESocket))
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
