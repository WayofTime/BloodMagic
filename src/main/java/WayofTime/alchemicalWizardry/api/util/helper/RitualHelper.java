package WayofTime.alchemicalWizardry.api.util.helper;

import WayofTime.alchemicalWizardry.api.event.RitualEvent;
import WayofTime.alchemicalWizardry.api.registry.RitualRegistry;
import WayofTime.alchemicalWizardry.api.ritual.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.ritual.Ritual;
import WayofTime.alchemicalWizardry.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.alchemicalWizardry.api.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.Event;
import sun.misc.Launcher;

import java.io.File;
import java.net.URL;

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

    public static boolean activate(IMasterRitualStone masterRitualStone, Ritual ritual, EntityPlayer player) {
        String owner = masterRitualStone.getOwner();

        RitualEvent.RitualActivatedEvent event = new RitualEvent.RitualActivatedEvent(masterRitualStone, owner, ritual, player, player.getHeldItem(), player.getHeldItem().getItemDamage());

        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
            return false;

        return RitualRegistry.ritualEnabled(ritual) && ritual.startRitual(masterRitualStone, player);
    }

    public static void perform(IMasterRitualStone masterRitualStone, Ritual ritual) {
        String owner = masterRitualStone.getOwner();

        RitualEvent.RitualRunEvent event = new RitualEvent.RitualRunEvent(masterRitualStone, owner, ritual);

        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
            return;

        if (RitualRegistry.ritualEnabled(ritual))
            ritual.performEffect(masterRitualStone);
    }

    public static void stop(IMasterRitualStone masterRitualStone, Ritual ritual, Ritual.BreakType breakType) {
        String owner = masterRitualStone.getOwner();

        RitualEvent.RitualStopEvent event = new RitualEvent.RitualStopEvent(masterRitualStone, owner, ritual, breakType);
        MinecraftForge.EVENT_BUS.post(event);

        ritual.onRitualBroken(masterRitualStone, breakType);
    }

    /**
     * Adds your Ritual to the {@link RitualRegistry#enabledRituals} Map.
     * This is used to determine whether your effect is enabled or not.
     *
     * The config option will be created as {@code B:ClassName=true} with a comment of
     * {@code Enables the ClassName ritual}.
     *
     * Should be safe to modify at any point.
     *
     * @param config      - Your mod's Forge {@link Configuration} object.
     * @param packageName - The package your Rituals are located in.
     * @param category    - The config category to write to.
     */
    public static void checkRituals(Configuration config, String packageName, String category) {
        String name = packageName;
        if (!name.startsWith("/"))
            name = "/" + name;

        name = name.replace('.', '/');
        URL url = Launcher.class.getResource(name);
        File directory = new File(url.getFile());

        if (directory.exists()) {
            String[] files = directory.list();

            for (String file : files) {
                if (file.endsWith(".class")) {
                    String className = file.substring(0, file.length() - 6);

                    try {
                        Object o = Class.forName(packageName + "." + className).newInstance();

                        if (o instanceof Ritual)
                            RitualRegistry.enabledRituals.put((Ritual) o, config.get(category, className, true).getBoolean());

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

    public static class Imperfect {

        public static boolean activate(IImperfectRitualStone imperfectRitualStone, ImperfectRitual imperfectRitual, EntityPlayer player) {
            String owner = imperfectRitualStone.getOwner();

            RitualEvent.ImperfectRitualActivatedEvent event = new RitualEvent.ImperfectRitualActivatedEvent(imperfectRitualStone, owner, imperfectRitual);

            if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
                return false;


            return imperfectRitual.onActivate(imperfectRitualStone, player);
        }
    }
}
