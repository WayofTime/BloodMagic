package WayofTime.alchemicalWizardry.common.achievements;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.items.ItemRitualDiviner;
import WayofTime.alchemicalWizardry.common.items.armour.BoundArmour;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.stats.Achievement;

import java.util.ArrayList;
import java.util.List;

public class AchievementsRegistry
{
    public final static List<Item> craftinglist = new ArrayList<Item>();
    public final static List<Item> pickupList = new ArrayList<Item>();
    public final static List<Block> blockCraftingList = new ArrayList<Block>();
//    public final static List<Block> blockPickupList = new ArrayList<Block>();

    public static void init()
    {
        addItemsToCraftingList();
        addBlocksToCraftingList();
        addItemsToPickupList();
//        addBlocksToPickupList();
    }

    public static void addItemsToCraftingList()
    {
        craftinglist.add(ModItems.sacrificialDagger);
        craftinglist.add(ModItems.itemBloodPack);
        craftinglist.add(ModItems.waterSigil);
        craftinglist.add(ModItems.airSigil);
        craftinglist.add(ModItems.sigilOfHolding);
        craftinglist.add(ModItems.itemRitualDiviner);
        craftinglist.add(ModItems.sigilOfTheBridge);
        craftinglist.add(ModItems.itemSigilOfSupression);
        craftinglist.add(ModItems.energyBazooka);
    }

    public static void addBlocksToCraftingList()
    {
        blockCraftingList.add(ModBlocks.bloodRune);
        blockCraftingList.add(ModBlocks.blockWritingTable);
        blockCraftingList.add(ModBlocks.blockTeleposer);
    }

    public static void addItemsToPickupList()
    {
        pickupList.add(ModItems.weakBloodOrb);
        pickupList.add(ModItems.apprenticeBloodOrb);
        pickupList.add(ModItems.daggerOfSacrifice);
        pickupList.add(ModItems.magicianBloodOrb);
        pickupList.add(ModItems.energySword);
        pickupList.add(ModItems.boundHelmet);
        pickupList.add(ModItems.boundPlate);
        pickupList.add(ModItems.boundLeggings);
        pickupList.add(ModItems.boundBoots);
        pickupList.add(ModItems.itemComplexSpellCrystal);
        pickupList.add(ModItems.masterBloodOrb);
        pickupList.add(ModItems.archmageBloodOrb);
        pickupList.add(ModItems.transcendentBloodOrb);
    }

    public static void addBlocksToPickupList()
    {

    }

    public static Achievement getAchievementForItem(Item item)
    {
        if (item == ModItems.sacrificialDagger) return ModAchievements.firstPrick;
        if (item == ModItems.weakBloodOrb) return ModAchievements.weakOrb;
        if (item == ModItems.itemBloodPack) return ModAchievements.bloodLettersPack;
        if (item == ModItems.waterSigil) return ModAchievements.waterSigil;
        if (item == ModItems.apprenticeBloodOrb) return ModAchievements.apprenticeOrb;
        if (item == ModItems.airSigil) return ModAchievements.airSigil;
        if (item == ModItems.daggerOfSacrifice) return ModAchievements.daggerOfSacrifice;
        if (item == ModItems.magicianBloodOrb) return ModAchievements.magicianOrb;
        if (item == ModItems.sigilOfHolding) return ModAchievements.sigilHolding;
        if (item == ModItems.energySword) return ModAchievements.boundBlade;
        if (item instanceof BoundArmour) return ModAchievements.boundArmor;
        if (item == ModItems.itemComplexSpellCrystal) return ModAchievements.complexSpells;
        if (item instanceof ItemRitualDiviner) return ModAchievements.ritualDiviner;
        if (item == ModItems.masterBloodOrb) return ModAchievements.masterOrb;
        if (item == ModItems.sigilOfTheBridge) return ModAchievements.phantomBridgeSigil;
        if (item == ModItems.itemSigilOfSupression) return ModAchievements.suppressionSigil;
        if (item == ModItems.archmageBloodOrb) return ModAchievements.archmageOrb;
        if (item == ModItems.energyBazooka) return ModAchievements.energyBazooka;
        if (item == ModItems.transcendentBloodOrb) return ModAchievements.transcendentOrb;

        return null;
    }
    
    public static Achievement getAchievementForBlock(Block block)
    {
        if (block == ModBlocks.bloodRune) return ModAchievements.blankRunes;
        if (block == ModBlocks.blockWritingTable) return ModAchievements.brewingPotions;
        if (block == ModBlocks.blockTeleposer) return ModAchievements.teleposer;

        return null;
    }
}
