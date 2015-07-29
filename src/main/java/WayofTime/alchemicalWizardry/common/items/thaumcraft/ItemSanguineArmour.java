//package WayofTime.alchemicalWizardry.common.items.thaumcraft;
//
//import WayofTime.alchemicalWizardry.AlchemicalWizardry;
//import WayofTime.alchemicalWizardry.ModItems;
//import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//import net.minecraft.client.renderer.texture.IIconRegister;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemArmor;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.IIcon;
//import net.minecraft.util.StatCollector;
//import net.minecraft.world.World;
//import thaumcraft.api.IGoggles;
//import thaumcraft.api.IRepairable;
//import thaumcraft.api.IRunicArmor;
//import thaumcraft.api.IVisDiscountGear;
//import thaumcraft.api.aspects.Aspect;
//import thaumcraft.api.nodes.IRevealer;
//
//import java.util.List;
//
//public class ItemSanguineArmour extends ItemArmor implements ArmourUpgrade, IGoggles, IVisDiscountGear, IRevealer, IRunicArmor, IRepairable
//{
//    @SideOnly(Side.CLIENT)
//    private IIcon helmetIcon;
//    @SideOnly(Side.CLIENT)
//    private IIcon plateIcon;
//    @SideOnly(Side.CLIENT)
//    private IIcon leggingsIcon;
//    @SideOnly(Side.CLIENT)
//    private IIcon bootsIcon;
//
//    public ItemSanguineArmour(int armorType)
//    {
//        super(AlchemicalWizardry.sanguineArmourArmourMaterial, 0, armorType);
//        setMaxDamage(1000);
//        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void registerIcons(IIconRegister iconRegister)
//    {
//        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
//        this.helmetIcon = iconRegister.registerIcon("AlchemicalWizardry:SanguineHelmet");
//        this.plateIcon = iconRegister.registerIcon("AlchemicalWizardry:SanguinePlate");
//        this.leggingsIcon = iconRegister.registerIcon("AlchemicalWizardry:SanguineLeggings");
//        this.bootsIcon = iconRegister.registerIcon("AlchemicalWizardry:SanguineBoots");
//    }
//
//    @SideOnly(Side.CLIENT)
//    public IIcon getIconFromDamage(int par1)
//    {
//        if (this.equals(ModItems.sanguineHelmet))
//        {
//            return this.helmetIcon;
//        }
//
//        if (this.equals(ModItems.sanguineRobe))
//        {
//            return this.plateIcon;
//        }
//
//        if (this.equals(ModItems.sanguinePants))
//        {
//            return this.leggingsIcon;
//        }
//
//        if (this.equals(ModItems.sanguineBoots))
//        {
//            return this.bootsIcon;
//        }
//
//        return this.itemIcon;
//    }
//
//    @Override
//    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
//    {
//        if (this == ModItems.sanguineHelmet)
//        {
//            return "alchemicalwizardry:models/armor/sanguineArmour_layer_1.png";
//        }
//
//        if (this == ModItems.sanguineRobe || this == ModItems.sanguineBoots)
//        {
//            return "alchemicalwizardry:models/armor/sanguineArmour_layer_1.png";
//        }
//
//        if (this == ModItems.sanguinePants)
//        {
//            return "alchemicalwizardry:models/armor/sanguineArmour_layer_2.png";
//        } else
//        {
//            return null;
//        }
//    }
//
//    @Override
//    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
//    {
//        int discount = 0;
//
//        switch (this.armorType)
//        {
//            case 0:
//                discount = 6;
//                break;
//            case 1:
//                discount = 3;
//                break;
//            case 2:
//                discount = 3;
//                break;
//            case 3:
//                discount = 2;
//                break;
//        }
//
//        switch (this.armorType)
//        {
//            case 0:
//                par3List.add(StatCollector.translateToLocal("tooltip.sanguinearmor.desc1"));
//                break;
//            case 1:
//
//            case 2:
//
//            case 3:
//                par3List.add(StatCollector.translateToLocal("tooltip.sanguinearmor.desc2"));
//        }
//
//        par3List.add(StatCollector.translateToLocal("tooltip.sanguinearmor.visdisc") + " " + discount + "%");
//    }
//
//    @Override
//    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
//    {
//        return;
//    }
//
//    @Override
//    public boolean isUpgrade()
//    {
//        return true;
//    }
//
//    @Override
//    public int getEnergyForTenSeconds()
//    {
//        return 0;
//    }
//
//    @Override
//    public boolean showNodes(ItemStack itemstack, EntityLivingBase player)
//    {
//        return true;
//    }
//
//    @Override
//    public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect)
//    {
//        switch (this.armorType)
//        {
//            case 0:
//                return 7;
//            case 1:
//                return 3;
//            case 2:
//                return 2;
//            case 3:
//                return 2;
//        }
//        return 0;
//    }
//
//    @Override
//    public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player)
//    {
//        return true;
//    }
//
//    @Override
//    public int getRunicCharge(ItemStack itemstack)
//    {
//        return 0;
//    }
//}