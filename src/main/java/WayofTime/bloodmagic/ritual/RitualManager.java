package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.util.BMLog;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RitualManager {
    public static final DamageSource RITUAL_DAMAGE = new DamageSource("ritual_damage").setDamageBypassesArmor();

    private final Map<String, Ritual> rituals;
    private final Map<Ritual, String> ritualsReverse;
    private final List<Ritual> sortedRituals;
    private final Map<String, ImperfectRitual> imperfectRituals;
    private final Map<ImperfectRitual, String> imperfectRitualsReverse;
    private final Configuration config;


    public RitualManager(Configuration config) {
        this.rituals = Maps.newTreeMap();
        this.ritualsReverse = Maps.newHashMap();
        this.sortedRituals = Lists.newArrayList();
        this.imperfectRituals = Maps.newTreeMap();
        this.imperfectRitualsReverse = Maps.newHashMap();
        this.config = config;
    }

    public void discover(ASMDataTable dataTable) {
        Set<ASMDataTable.ASMData> data = dataTable.getAll(RitualRegister.class.getName());
        for (ASMDataTable.ASMData found : data) {
            try {
                Class<?> discoveredClass = Class.forName(found.getClassName());
                if (!Ritual.class.isAssignableFrom(discoveredClass))
                    throw new BadRitualException("Annotated class " + found.getClassName() + " does not inherit from " + Ritual.class.getName());

                Class<? extends Ritual> ritualClass = discoveredClass.asSubclass(Ritual.class);
                RitualRegister ritualRegister = ritualClass.getAnnotation(RitualRegister.class);
                String id = ritualRegister.value();
                Ritual ritual = ritualRegister.factory().newInstance().apply(ritualClass);
                if (ritual == null) {
                    BMLog.DEFAULT.error("Error creating ritual instance for {}.", id);
                    continue;
                }

                rituals.put(id, ritual);
                ritualsReverse.put(ritual, id);
                BMLog.DEBUG.info("Registered ritual {}", id);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Set<ASMDataTable.ASMData> imperfectData = dataTable.getAll(RitualRegister.Imperfect.class.getName());
        for (ASMDataTable.ASMData found : imperfectData) {
            try {
                Class<?> discoveredClass = Class.forName(found.getClassName());
                if (!ImperfectRitual.class.isAssignableFrom(discoveredClass))
                    throw new BadRitualException("Annotated class " + found.getClassName() + " does not inherit from " + ImperfectRitual.class.getName());

                Class<? extends ImperfectRitual> ritualClass = discoveredClass.asSubclass(ImperfectRitual.class);
                RitualRegister.Imperfect ritualRegister = ritualClass.getAnnotation(RitualRegister.Imperfect.class);
                String id = ritualRegister.value();
                ImperfectRitual ritual = ritualRegister.factory().newInstance().apply(ritualClass);
                if (ritual == null) {
                    BMLog.DEFAULT.error("Error creating imperfect ritual instance for {}.", id);
                    continue;
                }

                imperfectRituals.put(id, ritual);
                imperfectRitualsReverse.put(ritual, id);
                BMLog.DEBUG.info("Registered imperfect ritual {}", id);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        syncConfig();

        // Sort rituals
        sortedRituals.addAll(rituals.values());
        // Oh dear this is probably so slow
        sortedRituals.sort((o1, o2) -> {
            Set<RitualComponent> components = Sets.newHashSet();
            o1.gatherComponents(components::add);
            int initialSize = components.size();
            components.clear();
            o2.gatherComponents(components::add);
            return Integer.compare(initialSize, components.size());
        });
    }

    public Ritual getRitual(String id) {
        return rituals.get(id);
    }

    public String getId(Ritual ritual) {
        return ritualsReverse.get(ritual);
    }

    public ImperfectRitual getImperfectRitual(IBlockState state) {
        for (ImperfectRitual ritual : imperfectRituals.values())
            if (ritual.getBlockRequirement().test(state))
                return ritual;

        return null;
    }

    public String getId(ImperfectRitual ritual) {
        return imperfectRitualsReverse.get(ritual);
    }

    public Collection<Ritual> getRituals() {
        return rituals.values();
    }

    public Collection<ImperfectRitual> getImperfectRituals() {
        return imperfectRituals.values();
    }

    public List<Ritual> getSortedRituals() {
        return sortedRituals;
    }

    public void syncConfig() {
        config.addCustomCategoryComment("rituals", "Toggles for all rituals");
        rituals.forEach((k, v) -> config.getBoolean(k, "rituals", true, "Enable the " + k + " ritual."));
        imperfectRituals.forEach((k, v) -> config.getBoolean(k, "rituals.imperfect", true, "Enable the " + k + " imperfect ritual."));
        config.save();
    }

    public boolean enabled(String id, boolean imperfect) {
        return id != null && config.getBoolean(id, "rituals" + (imperfect ? ".imperfect" : ""), true, "");
    }

    public Configuration getConfig() {
        return config;
    }

    public static class BadRitualException extends RuntimeException {
        public BadRitualException(String message) {
            super(message);
        }
    }
}
