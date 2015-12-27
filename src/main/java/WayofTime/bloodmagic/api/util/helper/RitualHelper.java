package WayofTime.bloodmagic.api.util.helper;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.block.BlockRitualStone;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class RitualHelper {

    public static boolean canCrystalActivate(Ritual ritual, int crystalLevel) {
        return ritual.getCrystalLevel() <= crystalLevel && RitualRegistry.ritualEnabled(ritual);
    }

    public static String getNextRitualKey(String currentKey) {
        int currentIndex = RitualRegistry.getIds().indexOf(currentKey);
        int nextIndex = RitualRegistry.getRituals().listIterator(currentIndex).nextIndex();

        return RitualRegistry.getIds().get(nextIndex);
    }

    public static String getPrevRitualKey(String currentKey) {
        int currentIndex = RitualRegistry.getIds().indexOf(currentKey);
        int previousIndex = RitualRegistry.getIds().listIterator(currentIndex).previousIndex();

        return RitualRegistry.getIds().get(previousIndex);
    }

    public static boolean checkValidRitual(World world, BlockPos pos, String ritualId, EnumFacing direction) {
        ArrayList<RitualComponent> components = RitualRegistry.getRitualForId(ritualId).getComponents();

        if (components == null)
            return false;

        for (RitualComponent component : components) {
            IBlockState worldState = world.getBlockState(pos.add(component.getOffset()));
            if (worldState.getBlock() instanceof BlockRitualStone) {
                EnumRuneType worldType = EnumRuneType.values()[worldState.getBlock().getMetaFromState(worldState)];

                if (component.getRuneType() != worldType)
                    return false;
            }
        }

        return true;
    }

    public static void checkImperfectRituals(Configuration config, String packageName, String category) {
        checkRituals(config, packageName, category, ImperfectRitual.class, ImperfectRitualRegistry.enabledRituals);
    }

    public static void checkRituals(Configuration config, String packageName, String category) {
        checkRituals(config, packageName, category, Ritual.class, RitualRegistry.enabledRituals);
    }

    /**
     * Adds your Ritual to the {@link RitualRegistry#enabledRituals} Map.
     * This is used to determine whether your effect is enabled or not.
     *
     * The config option will be created as {@code B:ClassName=true} with a comment of
     * {@code Enables the ClassName ritual}.
     *
     * Use {@link #}
     *
     * Should be safe to modify at any point.
     *
     * @param config      - Your mod's Forge {@link Configuration} object.
     * @param packageName - The package your Rituals are located in.
     * @param category    - The config category to write to.
     */
    @SuppressWarnings("unchecked")
    private static void checkRituals(Configuration config, String packageName, String category, Class ritualClass, Map enabledMap) {
        String name = packageName;
        if (!name.startsWith("/"))
            name = "/" + name;

        name = name.replace('.', '/');
        URL url = BloodMagic.class.getResource(name);
        File directory = new File(url.getFile());

        if (directory.exists()) {
            String[] files = directory.list();

            for (String file : files) {
                if (file.endsWith(".class")) {
                    String className = file.substring(0, file.length() - 6);

                    try {
                        Object o = Class.forName(packageName + "." + className).newInstance();

                        if (ritualClass.isInstance(o))
                            enabledMap.put(ritualClass.cast(o), config.get(category, className, true).getBoolean());

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
