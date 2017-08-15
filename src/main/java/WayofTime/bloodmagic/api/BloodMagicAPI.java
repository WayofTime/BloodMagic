package WayofTime.bloodmagic.api;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.altar.EnumAltarComponent;
import WayofTime.bloodmagic.api.util.helper.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The primary API class. Includes helper methods and blacklists.
 * 
 * Some API methods can be used via IMC instead. The supported methods are:
 * 
 * <ul>
 * <li>{@link #addToTeleposerBlacklist(BlockStack)}</li>
 * <li>{@link #blacklistFromGreenGrove(Block)}</li>
 * <li>{@link #setEntitySacrificeValue(Class, int)}</li>
 * <li>{@link #addAltarComponent(IBlockState, EnumAltarComponent)}</li>
 * </ul>
 */
public class BloodMagicAPI
{
    public static final List<BlockStack> teleposerBlacklist = new ArrayList<BlockStack>();
    public static final List<BlockStack> transpositionBlacklist = new ArrayList<BlockStack>();
    public static final Map<String, Integer> entitySacrificeValues = new HashMap<String, Integer>();
    public static final ArrayList<Block> greenGroveBlacklist = new ArrayList<Block>();
    public static final Map<IBlockState, EnumAltarComponent> altarComponents = new HashMap<IBlockState, EnumAltarComponent>();

    public static boolean loggingEnabled;

    public static LogHelper logger = new LogHelper("BloodMagic|API");

    public static DamageSource damageSource = new DamageSourceBloodMagic();

    public static Fluid lifeEssence;
    public static ItemStack lifeEssenceBucket;

    public static ItemStack getLifeEssenceBucket()
    {
        if (lifeEssenceBucket != null)
            return lifeEssenceBucket;

        lifeEssenceBucket = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, lifeEssence);
        return lifeEssenceBucket;
    }

    /**
     * Used to obtain Items from BloodMagic. Use
     * {@link WayofTime.bloodmagic.api.Constants.BloodMagicItem} to get the
     * registered name.
     * 
     * @param name
     *        - The registered name of the item. Usually the same as the class
     *        name.
     * @return - The requested Item
     */
    public static Item getItem(String name)
    {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(BloodMagic.MODID, name));
    }

    /**
     * @see #getItem(String)
     * 
     * @param bloodMagicItem
     *        - The {@link WayofTime.bloodmagic.api.Constants.BloodMagicItem} to
     *        get.
     * @return - The requested Item
     */
    public static Item getItem(Constants.BloodMagicItem bloodMagicItem)
    {
        return getItem(bloodMagicItem.getRegName());
    }

    /**
     * Used to obtain Blocks from BloodMagic. Use
     * {@link WayofTime.bloodmagic.api.Constants.BloodMagicBlock} to get the
     * registered name.
     * 
     * @param name
     *        - The registered name of the block. Usually the same as the class
     *        name.
     * @return - The requested Block
     */
    public static Block getBlock(String name)
    {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(BloodMagic.MODID, name));
    }

    /**
     * @see #getBlock(String)
     * 
     * @param bloodMagicBlock
     *        - The {@link WayofTime.bloodmagic.api.Constants.BloodMagicBlock}
     *        to get.
     * @return - The requested Block
     */
    public static Block getBlock(Constants.BloodMagicBlock bloodMagicBlock)
    {
        return getBlock(bloodMagicBlock.getRegName());
    }

    /**
     * Used to add a {@link BlockStack} to the Teleposer blacklist that cannot
     * be changed via Configuration files.
     * 
     * IMC:
     * {@code FMLInterModComs.sendMessage("bloodmagic", "teleposerBlacklist", ItemStack)}
     * Example:
     * {@code FMLInterModComs.sendMessage("bloodmagic", "teleposerBlacklist", new ItemStack(Blocks.bedrock))}
     * 
     * @param blockStack
     *        - The BlockStack to blacklist.
     */
    public static void addToTeleposerBlacklist(BlockStack blockStack)
    {
        if (!teleposerBlacklist.contains(blockStack))
            teleposerBlacklist.add(blockStack);
    }

    /**
     * @see #addToTeleposerBlacklist(BlockStack)
     * 
     * @param block
     *        - The block to blacklist
     * @param meta
     *        - The meta of the block to blacklist
     */
    public static void addToTeleposerBlacklist(Block block, int meta)
    {
        addToTeleposerBlacklist(new BlockStack(block, meta));
    }

    /**
     * @see #addToTeleposerBlacklist(BlockStack)
     * 
     * @param block
     *        - The block to blacklist
     */
    public static void addToTeleposerBlacklist(Block block)
    {
        addToTeleposerBlacklist(block, 0);
    }

    /**
     * Used to add a {@link BlockStack} to the Transposition blacklist that
     * cannot be changed via Configuration files.
     * 
     * IMC:
     * {@code FMLInterModComs.sendMessage("bloodmagic", "transpositionBlacklist", ItemStack)}
     * Example:
     * {@code FMLInterModComs.sendMessage("bloodmagic", "transpositionBlacklist", new ItemStack(Blocks.bedrock))}
     * 
     * @param blockStack
     *        - The BlockStack to blacklist.
     */
    public static void addToTranspositionBlacklist(BlockStack blockStack)
    {
        if (!transpositionBlacklist.contains(blockStack))
            transpositionBlacklist.add(blockStack);
    }

    /**
     * @see #addToTranspositionBlacklist(BlockStack)
     * 
     * @param block
     *        - The block to blacklist
     * @param meta
     *        - The meta of the block to blacklist
     */
    public static void addToTranspositionBlacklist(Block block, int meta)
    {
        addToTranspositionBlacklist(new BlockStack(block, meta));
    }

    /**
     * @see #addToTranspositionBlacklist(BlockStack)
     * 
     * @param block
     *        - The block to blacklist
     */
    public static void addToTranspositionBlacklist(Block block)
    {
        addToTranspositionBlacklist(block, 0);
    }

    /**
     * Used to set the sacrifice value of an Entity. The value provided is how
     * much LP will be gained when the entity is sacrificed at a Blood Altar.
     * 
     * Setting a sacrificeValue of 0 will effectively blacklist the entity.
     * 
     * The default value for any unset Entity is 500 LP per sacrifice.
     * 
     * IMC:
     * {@code FMLInterModComs.sendMessage("bloodmagic", "sacrificeValue", "ClassName;Value")}
     * Example:
     * {@code FMLInterModComs.sendMessage("bloodmagic", "sacrificeValue", "EntityVillager;2000")}
     * 
     * @param entityClass
     *        - The class of the entity to blacklist.
     * @param sacrificeValue
     *        - The Amount of LP to provide per each HP of the entity
     *        sacrificed.
     */
    public static void setEntitySacrificeValue(Class<? extends EntityLivingBase> entityClass, int sacrificeValue)
    {
        if (!entitySacrificeValues.containsKey(entityClass.getSimpleName()))
            entitySacrificeValues.put(entityClass.getSimpleName(), sacrificeValue);
    }

    /**
     * @see #setEntitySacrificeValue(Class, int)
     * 
     * @param entityClassName
     *        - The name of the class of the entity to blacklist.
     * @param sacrificeValue
     *        - The Amount of LP to provide per each HP of the entity
     *        sacrificed.
     */
    public static void setEntitySacrificeValue(String entityClassName, int sacrificeValue)
    {
        if (!entitySacrificeValues.containsKey(entityClassName))
            entitySacrificeValues.put(entityClassName, sacrificeValue);
    }

    /**
     * Blacklists a block from the Green Grove Ritual and Sigil.
     * 
     * IMC:
     * {@code FMLInterModComs.sendMessage("bloodmagic", "greenGroveBlacklist", "domain:name")}
     * Example:
     * {@code FMLInterModComs.sendMessage("bloodmagic", "greenGroveBlacklist", "minecraft:wheat")}
     * 
     * @param block
     *        - Block to blacklist
     */
    public static void blacklistFromGreenGrove(Block block)
    {
        if (!greenGroveBlacklist.contains(block))
            greenGroveBlacklist.add(block);
    }

    /**
     * Marks an IBlockState as a specific {@link EnumAltarComponent} without needing to implement
     * {@link WayofTime.bloodmagic.api.altar.IAltarComponent} on the block.
     *
     * IMC:
     * {@code FMLInterModComs.sendMessage("bloodmagic", "altarComponent", "domain:name:meta:component")}
     * Example:
     * {@code FMLInterModComs.sendMessage("bloodmagic", "altarComponent", "minecraft:glowstone:0:GLOWSTONE")}
     *
     * @param state
     *        - The IBlockState for this component
     * @param altarComponent
     *        - The EnumAltarComponent for this state
     */
    public static void addAltarComponent(IBlockState state, EnumAltarComponent altarComponent)
    {
        if (!altarComponents.containsKey(state))
            altarComponents.put(state, altarComponent);
    }
}
