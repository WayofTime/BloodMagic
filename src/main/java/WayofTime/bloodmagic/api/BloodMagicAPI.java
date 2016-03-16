package WayofTime.bloodmagic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import WayofTime.bloodmagic.api.util.helper.LogHelper;

/**
 * The primary API class. Includes helper methods and blacklists.
 * 
 * Some API methods can be used via IMC instead. The supported methods are:
 * 
 * <ul>
 * <li>{@link #addToTeleposerBlacklist(BlockStack)}</li>
 * <li>{@link #blacklistFromGreenGrove(Block)}</li>
 * <li>{@link #setEntitySacrificeValue(Class, int)}</li>
 * </ul>
 */
public class BloodMagicAPI
{
    @Getter
    private static final List<BlockStack> teleposerBlacklist = new ArrayList<BlockStack>();
    @Getter
    private static final Map<String, Integer> entitySacrificeValues = new HashMap<String, Integer>();
    @Getter
    private static final ArrayList<Block> greenGroveBlacklist = new ArrayList<Block>();

    @Getter
    @Setter
    private static boolean loggingEnabled;

    @Getter
    private static LogHelper logger = new LogHelper("BloodMagic|API");

    @Getter
    private static DamageSource damageSource = new DamageSourceBloodMagic();

    @Getter
    @Setter
    private static Fluid lifeEssence;

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
        return GameRegistry.findItem(Constants.Mod.MODID, name);
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
        return GameRegistry.findBlock(Constants.Mod.MODID, name);
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
     * {@code FMLInterModComs.sendMessage("BloodMagic", "teleposerBlacklist", ItemStack)}
     * Example:
     * {@code FMLInterModComs.sendMessage("BloodMagic", "teleposerBlacklist", new ItemStack(Blocks.bedrock))}
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
     * Used to set the sacrifice value of an Entity. The value provided is how
     * much LP will be gained when the entity is sacrificed at a Blood Altar.
     * 
     * Setting a sacrificeValue of 0 will effectively blacklist the entity.
     * 
     * The default value for any unset Entity is 500 LP per sacrifice.
     * 
     * IMC:
     * {@code FMLInterModComs.sendMessage("BloodMagic", "sacrificeValue", "ClassName;Value")}
     * Example:
     * {@code FMLInterModComs.sendMessage("BloodMagic", "sacrificeValue", "EntityVillager;2000")}
     * 
     * @param entityClass
     *        - The class of the entity to blacklist.
     * @param sacrificeValue
     *        - The Amount of LP to provide per each entity sacrificed.
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
     *        - The Amount of LP to provide per each entity sacrificed.
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
     * {@code FMLInterModComs.sendMessage("BloodMagic", "greenGroveBlacklist", "domain:name")}
     * Example:
     * {@code FMLInterModComs.sendMessage("BloodMagic", "greenGroveBlacklist", "minecraft:wheat")}
     * 
     * @param block
     *        - Block to blacklist
     */
    public static void blacklistFromGreenGrove(Block block)
    {
        if (!greenGroveBlacklist.contains(block))
            greenGroveBlacklist.add(block);
    }
}
