package com.wayoftime.bloodmagic.core.living;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;

@JsonAdapter(LivingUpgrade.Deserializer.class)
public class LivingUpgrade {

    public static final LivingUpgrade DUMMY = new LivingUpgrade(new ResourceLocation("dummy"), levels -> levels.add(new Level(0, 0)));

    private final ResourceLocation key;
    private final Set<ResourceLocation> incompatible;
    private final TreeMap<Integer, Integer> experienceToLevel;
    private final Map<Integer, Integer> levelToCost;
    private final Map<String, Bonus> bonuses;
    private boolean isNegative;
    private String unlocalizedName = null;
    private IAttributeProvider attributeProvider;
    private IArmorProvider armorProvider;

    public LivingUpgrade(ResourceLocation key, Consumer<List<Level>> experienceMapper) {
        this.key = key;
        this.incompatible = Sets.newHashSet();
        this.experienceToLevel = Maps.newTreeMap();
        this.levelToCost = Maps.newHashMap();
        this.bonuses = Maps.newHashMap();

        List<Level> levels = Lists.newArrayList();
        experienceMapper.accept(levels);

        for (int i = 0; i < levels.size(); i++) {
            Level level = levels.get(i);
            experienceToLevel.put(level.experienceNeeded, i + 1);
            levelToCost.put(i + 1, level.upgradeCost);
        }
    }

    public LivingUpgrade withBonusSet(String id, Consumer<List<Number>> modifiers) {
        List<Number> values = NonNullList.create();
        modifiers.accept(values);
        if (values.size() != levelToCost.size())
            throw new RuntimeException("Bonus size and level size must be the same.");

        bonuses.put(id, new Bonus(id, values));
        return this;
    }

    @Nonnull
    public Number getBonusValue(String id, int level) {
        return bonuses.getOrDefault(id, Bonus.DEFAULT).modifiers.get(level - 1);
    }

    public LivingUpgrade withAttributeProvider(IAttributeProvider attributeProvider) {
        this.attributeProvider = attributeProvider;
        return this;
    }

    @Nullable
    public IAttributeProvider getAttributeProvider() {
        return attributeProvider;
    }

    public LivingUpgrade withArmorProvider(IArmorProvider armorProvider) {
        this.armorProvider = armorProvider;
        return this;
    }

    @Nullable
    public IArmorProvider getArmorProvider() {
        return armorProvider;
    }

    public String getUnlocalizedName() {
        return unlocalizedName == null ? unlocalizedName = "upgrade." + key.getNamespace() + ":" + key.getPath() + ".name" : unlocalizedName;
    }

    public boolean isNegative() {
        return isNegative;
    }

    public boolean isCompatible(ResourceLocation otherUpgrade) {
        return !incompatible.contains(otherUpgrade);
    }

    public LivingUpgrade addIncompatibility(ResourceLocation... otherKeys) {
        Collections.addAll(incompatible, otherKeys);
        return this;
    }

    public int getLevel(int experience) {
        Map.Entry<Integer, Integer> floor = experienceToLevel.floorEntry(experience);
        return floor == null ? 0 : floor.getValue();
    }

    public int getNextRequirement(int experience) {
        Integer ret = experienceToLevel.ceilingKey(experience + 1);
        return ret == null ? 0 : ret;
    }

    public int getLevelCost(int level) {
        return levelToCost.getOrDefault(level, 0);
    }

    public ResourceLocation getKey() {
        return key;
    }

    public LivingUpgrade asDowngrade() {
        this.isNegative = true;
        return this;
    }

    @Override
    public String toString() {
        return key.toString();
    }

    public interface IAttributeProvider {
        void handleAttributes(LivingStats stats, Multimap<String, AttributeModifier> attributeMap, int level);
    }

    public interface IArmorProvider {
        double getProtection(EntityPlayer player, DamageSource source, int level);
    }

    public static class Level {
        @SerializedName("xp")
        private final int experienceNeeded;
        @SerializedName("cost")
        private final int upgradeCost;

        public Level(int experienceNeeded, int upgradeCost) {
            this.experienceNeeded = experienceNeeded;
            this.upgradeCost = upgradeCost;
        }
    }

    public static class Bonus {

        private static final Bonus DEFAULT = new Bonus("null", Collections.emptyList());

        private final String id;
        private final List<Number> modifiers;

        public Bonus(String id, List<Number> modifiers) {
            this.id = id;
            this.modifiers = modifiers;
        }
    }

    public static class Deserializer implements JsonDeserializer<LivingUpgrade> {
        @Override
        public LivingUpgrade deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = element.getAsJsonObject();
            ResourceLocation id = new ResourceLocation(json.getAsJsonPrimitive("id").getAsString());
            List<LivingUpgrade.Level> levels = context.deserialize(json.getAsJsonArray("levels"), new TypeToken<List<Level>>(){}.getType());
            boolean negative = json.has("negative") && json.getAsJsonPrimitive("negative").getAsBoolean();

            LivingUpgrade upgrade = new LivingUpgrade(id, upgradeLevels -> upgradeLevels.addAll(levels));
            if (negative)
                upgrade.asDowngrade();

            if (json.has("incompatibilities")) {
                String[] incompatibilities = context.deserialize(json.getAsJsonArray("incompatibilities"), String[].class);
                for (String incompat : incompatibilities)
                    upgrade.addIncompatibility(new ResourceLocation(incompat));
            }

            if (json.has("bonuses")) {
                Map<String, Number[]> bonuses = context.deserialize(json.getAsJsonObject("bonuses"), new TypeToken<Map<String, Number[]>>(){}.getType());
                bonuses.forEach((k, v) -> upgrade.withBonusSet(k, numbers -> Collections.addAll(numbers, v)));
            }

            return upgrade;
        }
    }
}
