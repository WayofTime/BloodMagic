package WayofTime.bloodmagic.api;

import WayofTime.bloodmagic.api.util.helper.LogHelper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class BloodMagicAPI
{
    public static final String ORB = "ItemBloodOrb";
    public static final String SCRIBE = "ItemInscriptionTool";

    @Getter
    private static final List<BlockStack> teleposerBlacklist = new ArrayList<BlockStack>();

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
     * Used to obtain Items from BloodMagic. Use the constants above for common
     * items in case internal names change.
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
     * Used to add a {@link BlockStack} to the Teleposer blacklist that cannot
     * be changed via Configuration files.
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
}
