package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.registry.LivingArmourDowngradeRecipeRegistry;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.recipe.LivingArmourDowngradeRecipe;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RitualRegister("armour_downgrade")
public class RitualLivingArmourDowngrade extends Ritual {
    public static final String DOWNGRADE_RANGE = "containmentRange";
    private int internalTimer = 0;

    public RitualLivingArmourDowngrade() {
        super("ritualDowngrade", 2, 10000, "ritual." + BloodMagic.MODID + ".downgradeRitual");
        addBlockRange(DOWNGRADE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-3, 0, -3), 7));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        BlockPos masterPos = masterRitualStone.getBlockPos();

        AreaDescriptor downgradeRange = masterRitualStone.getBlockRange(DOWNGRADE_RANGE);

        boolean isActivatorPresent = false;
        for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, downgradeRange.getAABB(masterRitualStone.getBlockPos()))) {
            if (player.getGameProfile().getId().equals(masterRitualStone.getOwner())) {
                ItemStack keyStack = getStackFromItemFrame(world, masterPos, masterRitualStone.getDirection());
                if (keyStack.isEmpty()) {
                    return;
                }

                List<ITextComponent> textList = LivingArmourDowngradeRecipeRegistry.getDialogForProcessTick(keyStack, internalTimer);
                if (textList != null) {
                    ChatUtil.sendChat(player, textList.toArray(new ITextComponent[textList.size()]));
                }

                internalTimer++;

                if (player.isSneaking()) {
                    double distance2 = masterPos.offset(EnumFacing.UP).distanceSqToCenter(player.posX, player.posY, player.posZ);
                    if (distance2 > 1) {
                        return;
                    }

                    BlockPos chestPos = masterPos.offset(masterRitualStone.getDirection(), 2).offset(EnumFacing.UP);
                    TileEntity tile = world.getTileEntity(chestPos);
                    if (tile == null) {
                        return;
                    }
                    IItemHandler inv = Utils.getInventory(tile, null);
                    if (inv != null) {
                        List<ItemStack> recipeList = new ArrayList<>();
                        for (int i = 0; i < inv.getSlots(); i++) {
                            ItemStack invStack = inv.getStackInSlot(i);
                            if (!invStack.isEmpty()) {
                                recipeList.add(invStack);
                            }
                        }

                        LivingArmourDowngradeRecipe recipe = LivingArmourDowngradeRecipeRegistry.getMatchingRecipe(keyStack, recipeList, world, masterPos);
                        if (recipe != null) {
                            LivingArmourUpgrade upgrade = recipe.getRecipeOutput();
                            if (LivingArmour.hasFullSet(player)) {
                                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                                if (armour != null) {
                                    if (armour.canApplyUpgrade(player, upgrade)) {
                                        if (armour.upgradeArmour(player, upgrade)) {
                                            ItemLivingArmour.setLivingArmour(chestStack, armour);

                                            recipe.consumeInventory(inv);

                                            EntityLightningBolt lightning = new EntityLightningBolt(world, chestPos.getX(), chestPos.getY(), chestPos.getZ(), true);
                                            world.spawnEntity(lightning);

                                            masterRitualStone.setActive(false);
                                        }
                                    } else {
                                        //TODO: You are not able to receive my blessing...
                                        //TODO: Need to add a timer that will stop it from working. 
                                    }
                                }
                            }
                        }
                    }
                }

                return;
            }
        }

        if (!isActivatorPresent) {
            internalTimer = 0;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        this.internalTimer = tag.getInteger("internalTimer");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("internalTimer", internalTimer);
    }

    public ItemStack getStackFromItemFrame(World world, BlockPos masterPos, EnumFacing direction) {
        BlockPos offsetPos = new BlockPos(0, 3, 0);
        offsetPos = offsetPos.offset(direction, 2);

        AxisAlignedBB bb = new AxisAlignedBB(masterPos.add(offsetPos));
        List<EntityItemFrame> frames = world.getEntitiesWithinAABB(EntityItemFrame.class, bb);
        for (EntityItemFrame frame : frames) {
            if (!frame.getDisplayedItem().isEmpty()) {
                return frame.getDisplayedItem();
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return 0;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addRune(components, 0, 0, -1, EnumRuneType.AIR);
        addRune(components, 0, 0, -2, EnumRuneType.DUSK);
        addRune(components, 0, 1, -3, EnumRuneType.DUSK);
        addRune(components, 0, 2, -3, EnumRuneType.BLANK);
        addRune(components, 0, 3, -3, EnumRuneType.BLANK);
        addRune(components, 0, 1, -4, EnumRuneType.FIRE);

        for (int i = 1; i <= 3; i++)
            addRune(components, 0, 0, i, EnumRuneType.AIR);

        for (int sgn = -1; sgn <= 1; sgn += 2) {
            addRune(components, sgn, 0, 4, EnumRuneType.AIR);
            addRune(components, sgn * 2, 0, 2, EnumRuneType.AIR);
            addRune(components, sgn * 3, 0, 2, EnumRuneType.AIR);
            addRune(components, sgn * 3, 0, 3, EnumRuneType.AIR);
            addRune(components, sgn, 0, 0, EnumRuneType.EARTH);
            addRune(components, sgn, 0, 1, EnumRuneType.EARTH);
            addRune(components, sgn * 2, 0, -1, EnumRuneType.FIRE);
            addRune(components, sgn * 2, 0, -2, EnumRuneType.FIRE);
            addRune(components, sgn * 3, 0, -2, EnumRuneType.FIRE);
            addRune(components, sgn * 3, 0, -3, EnumRuneType.FIRE);
            addRune(components, sgn * 3, 0, -4, EnumRuneType.FIRE);
            addRune(components, sgn, 1, -1, EnumRuneType.AIR);
            addRune(components, sgn, 1, -2, EnumRuneType.AIR);
            addRune(components, sgn, 1, -4, EnumRuneType.FIRE);
            addRune(components, sgn * 2, 1, -4, EnumRuneType.FIRE);
            addRune(components, sgn, 0, -3, EnumRuneType.EARTH);
            addRune(components, sgn, 0, -4, EnumRuneType.EARTH);
            addRune(components, sgn, 0, -5, EnumRuneType.EARTH);
            addRune(components, sgn, 1, -5, EnumRuneType.EARTH);
            addRune(components, sgn, 2, -5, EnumRuneType.EARTH);
            addRune(components, sgn, 3, -5, EnumRuneType.EARTH);
            addRune(components, sgn, 3, -4, EnumRuneType.EARTH);
        }
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualLivingArmourDowngrade();
    }
}
