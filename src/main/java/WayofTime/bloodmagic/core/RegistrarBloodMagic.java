package WayofTime.bloodmagic.core;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.orb.BloodOrb;
import WayofTime.bloodmagic.core.registry.OrbRegistry;
import WayofTime.bloodmagic.entity.mob.*;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.entity.projectile.EntityMeteor;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import WayofTime.bloodmagic.entity.projectile.EntitySoulSnare;
import WayofTime.bloodmagic.potion.PotionBloodMagic;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
@GameRegistry.ObjectHolder(BloodMagic.MODID)
public class RegistrarBloodMagic
{

    private static final BloodOrb ORB_DEF = new BloodOrb("", 0, 0, 0);
    @GameRegistry.ObjectHolder("weak")
    public static final BloodOrb ORB_WEAK = ORB_DEF;
    @GameRegistry.ObjectHolder("apprentice")
    public static final BloodOrb ORB_APPRENTICE = ORB_DEF;
    @GameRegistry.ObjectHolder("magician")
    public static final BloodOrb ORB_MAGICIAN = ORB_DEF;
    @GameRegistry.ObjectHolder("master")
    public static final BloodOrb ORB_MASTER = ORB_DEF;
    @GameRegistry.ObjectHolder("archmage")
    public static final BloodOrb ORB_ARCHMAGE = ORB_DEF;
    @GameRegistry.ObjectHolder("transcendent")
    public static final BloodOrb ORB_TRANSCENDENT = ORB_DEF;

    public static final Potion BOOST = MobEffects.HASTE;
    public static final Potion WHIRLWIND = MobEffects.HASTE;
    public static final Potion PLANAR_BINDING = MobEffects.HASTE;
    public static final Potion SOUL_SNARE = MobEffects.HASTE;
    public static final Potion SOUL_FRAY = MobEffects.HASTE;
    public static final Potion FIRE_FUSE = MobEffects.HASTE;
    public static final Potion CONSTRICT = MobEffects.HASTE;
    public static final Potion PLANT_LEECH = MobEffects.HASTE;
    public static final Potion DEAFNESS = MobEffects.HASTE;
    public static final Potion BOUNCE = MobEffects.HASTE;
    public static final Potion CLING = MobEffects.HASTE;
    public static final Potion SACRIFICIAL_LAMB = MobEffects.HASTE;

    public static IForgeRegistry<BloodOrb> BLOOD_ORBS = null;

    @SubscribeEvent
    public static void registerBloodOrbs(RegistryEvent.Register<BloodOrb> event)
    {
        ResourceLocation orb = RegistrarBloodMagicItems.BLOOD_ORB.getRegistryName();
        event.getRegistry().registerAll(
                new BloodOrb("weak", 1, 5000, 2).withModel(new ModelResourceLocation(orb, "type=weak")).setRegistryName("weak"),
                new BloodOrb("apprentice", 2, 25000, 5).withModel(new ModelResourceLocation(orb, "type=apprentice")).setRegistryName("apprentice"),
                new BloodOrb("magician", 3, 150000, 15).withModel(new ModelResourceLocation(orb, "type=magician")).setRegistryName("magician"),
                new BloodOrb("master", 4, 1000000, 25).withModel(new ModelResourceLocation(orb, "type=master")).setRegistryName("master"),
                new BloodOrb("archmage", 5, 10000000, 50).withModel(new ModelResourceLocation(orb, "type=archmage")).setRegistryName("archmage"),
                new BloodOrb("transcendent", 6, 30000000, 50).withModel(new ModelResourceLocation(orb, "type=transcendent")).setRegistryName("transcendent")
                );
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event)
    {
        event.getRegistry().registerAll(
                new PotionBloodMagic("Boost", false, 0xFFFFFF, 0, 0).setRegistryName("boost"),
                new PotionBloodMagic("Planar Binding", false, 0, 2, 0).setRegistryName("planar_binding"),
                new PotionBloodMagic("Soul Snare", false, 0xFFFFFF, 3, 0).setRegistryName("soul_snare"),
                new PotionBloodMagic("Soul Fray", true, 0xFFFFFF, 4, 0).setRegistryName("soul_fray"),
                new PotionBloodMagic("Fire Fuse", true, 0xFF3333, 5, 0).setRegistryName("fire_fuse"),
                new PotionBloodMagic("Constriction", true, 0x000000, 6, 0).setRegistryName("constrict"),
                new PotionBloodMagic("Plant Leech", true, 0x000000, 7, 0).setRegistryName("plant_leech"),
                new PotionBloodMagic("Deaf", true, 0x000000, 0, 1).setRegistryName("deafness"),
                new PotionBloodMagic("Bounce", false, 0x000000, 1, 1).setRegistryName("bounce"),
                new PotionBloodMagic("Cling", false, 0x000000, 2, 1).setRegistryName("cling"),
                new PotionBloodMagic("S. Lamb", false, 0x000000, 3, 1).setRegistryName("sacrificial_lamb")
                );
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
    {
        int entities = 0;

        event.getRegistry().registerAll(
                EntityEntryBuilder.create().id("blood_light", ++entities).entity(EntityBloodLight.class).name("blood_light").tracker(16 * 4, 3, true).build(),
                EntityEntryBuilder.create().id("soul_snare", ++entities).entity(EntitySoulSnare.class).name("soul_snare").tracker(16 * 4, 3, true).build(),
                EntityEntryBuilder.create().id("soul_arrow", ++entities).entity(EntitySentientArrow.class).name("sentient_arrow").tracker(16 * 4, 3, true).build(),
                EntityEntryBuilder.create().id("meteor", ++entities).entity(EntityMeteor.class).name("meteor").tracker(16 * 4, 3, true).build(),
                EntityEntryBuilder.create().id("sentient_specter", ++entities).entity(EntitySentientSpecter.class).name("sentient_specter").tracker(16 * 4, 3, true).build(),
                EntityEntryBuilder.create().id("mimic", ++entities).entity(EntityMimic.class).name("mimic").tracker(16 * 4, 3, true).build(),
                EntityEntryBuilder.create().id("corrupted_zombie", ++entities).entity(EntityCorruptedZombie.class).name("corrupted_zombie").tracker(16 * 4, 3, true).build(),
                EntityEntryBuilder.create().id("corrupted_sheep", ++entities).entity(EntityCorruptedSheep.class).name("corrupted_sheep").tracker(16 * 4, 3, true).build(),
                EntityEntryBuilder.create().id("corrupted_chicken", ++entities).entity(EntityCorruptedChicken.class).name("corrupted_chicken").tracker(16 * 4, 3, true).build(),
                EntityEntryBuilder.create().id("corrupted_spider", ++entities).entity(EntityCorruptedSpider.class).name("corrupted_spider").tracker(16 * 4, 3, true).build()
                );
    }

    @SubscribeEvent
    public static void onRegistryCreation(RegistryEvent.NewRegistry event)
    {
        BLOOD_ORBS = new RegistryBuilder<BloodOrb>()
                .setName(new ResourceLocation(BloodMagic.MODID, "blood_orb"))
                .setIDRange(0, Short.MAX_VALUE)
                .setType(BloodOrb.class)
                .addCallback((IForgeRegistry.AddCallback<BloodOrb>) (owner, stage, id, obj, oldObj) -> OrbRegistry.tierMap.put(obj.getTier(), OrbRegistry.getOrbStack(obj)))
                .create();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        for (BloodOrb orb : BLOOD_ORBS)
        {
            ModelResourceLocation modelLocation = orb.getModelLocation();
            if (modelLocation == null)
                modelLocation = new ModelResourceLocation(orb.getRegistryName(), "inventory");

            ModelLoader.registerItemVariants(RegistrarBloodMagicItems.BLOOD_ORB, modelLocation);
        }

        ModelLoader.setCustomMeshDefinition(RegistrarBloodMagicItems.BLOOD_ORB, stack ->
        {
            if (!stack.hasTagCompound())
                return new ModelResourceLocation(ORB_WEAK.getRegistryName(), "inventory");

            BloodOrb orb = BLOOD_ORBS.getValue(new ResourceLocation(stack.getTagCompound().getString("orb")));
            if (orb == null || orb.getModelLocation() == null)
                return new ModelResourceLocation(ORB_WEAK.getRegistryName(), "inventory");

            return orb.getModelLocation();
        });
    }
}
