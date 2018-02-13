package WayofTime.bloodmagic.proxy;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.apibutnotreally.Constants;
import WayofTime.bloodmagic.apibutnotreally.soul.DemonWillHolder;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.client.Sprite;
import WayofTime.bloodmagic.client.helper.ShaderHelper;
import WayofTime.bloodmagic.client.hud.HUDElementCornerTile;
import WayofTime.bloodmagic.client.hud.HUDElementDemonWillAura;
import WayofTime.bloodmagic.client.hud.HUDElementHolding;
import WayofTime.bloodmagic.client.key.KeyBindings;
import WayofTime.bloodmagic.client.render.LayerBloodElytra;
import WayofTime.bloodmagic.client.render.block.*;
import WayofTime.bloodmagic.client.render.entity.*;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.entity.mob.*;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.entity.projectile.EntityMeteor;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import WayofTime.bloodmagic.entity.projectile.EntitySoulSnare;
import WayofTime.bloodmagic.tile.*;
import WayofTime.bloodmagic.tile.routing.TileRoutingNode;
import WayofTime.bloodmagic.util.helper.NumeralHelper;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ClientProxy extends CommonProxy {
    public static DemonWillHolder currentAura = new DemonWillHolder();

    @Override
    public void preInit() {
        super.preInit();

        OBJLoader.INSTANCE.addDomain(BloodMagic.MODID);

        ClientRegistry.bindTileEntitySpecialRenderer(TileInversionPillar.class, new AnimationTESR<TileInversionPillar>() {
            @Override
            public void handleEvents(TileInversionPillar chest, float time, Iterable<Event> pastEvents) {
                chest.handleEvents(time, pastEvents);
            }
        });

        ClientRegistry.bindTileEntitySpecialRenderer(TileAlchemyArray.class, new RenderAlchemyArray());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new RenderAltar());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRoutingNode.class, new RenderItemRoutingNode());
        ClientRegistry.bindTileEntitySpecialRenderer(TileDemonCrucible.class, new RenderDemonCrucible());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMimic.class, new RenderMimic());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBloodTank.class, new RenderBloodTank());

        // Initialize key-binds during startup so they load correctly
        for (KeyBindings key : KeyBindings.values())
            key.getKey();
    }

    @Override
    public void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntitySoulSnare.class, new SoulSnareRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntitySentientArrow.class, new SentientArrowRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityBloodLight.class, new BloodLightRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityMeteor.class, new MeteorRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntitySentientSpecter.class, new SentientSpecterRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityMimic.class, new MimicRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityCorruptedZombie.class, new CorruptedZombieRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityCorruptedSheep.class, new CorruptedSheepRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityCorruptedChicken.class, new CorruptedChickenRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityCorruptedSpider.class, new CorruptedSpiderRenderFactory());

        ShaderHelper.init();
    }

    @Override
    public void init() {
        super.init();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            try {
                if (stack.hasTagCompound() && stack.getTagCompound().hasKey(Constants.NBT.COLOR))
                    if (tintIndex == 1)
                        return Color.decode(stack.getTagCompound().getString(Constants.NBT.COLOR)).getRGB();
            } catch (NumberFormatException e) {
                return -1;
            }
            return -1;
        }, RegistrarBloodMagicItems.SIGIL_HOLDING);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            if (tintIndex != 0 && tintIndex != 2)
                return -1;

            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("empty"))
                return -1;

            return PotionUtils.getPotionColorFromEffectList(PotionUtils.getEffectsFromStack(stack));
        }, RegistrarBloodMagicItems.POTION_FLASK);

        addElytraLayer();
    }

    @Override
    public void postInit() {
        new HUDElementHolding();
        new HUDElementDemonWillAura();
        new HUDElementCornerTile.BloodAltar(true) { // Divination Sigil
            @Override
            protected void addInformation(List<Pair<Sprite, Function<TileAltar, String>>> information) {
                information.add(Pair.of(new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 46, 16, 16), altar -> NumeralHelper.toRoman(altar.getTier().toInt())));
                information.add(Pair.of(new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 16, 46, 16, 16), altar -> String.format("%d/%d", altar.getCurrentBlood(), altar.getCapacity())));
            }
        };
        new HUDElementCornerTile.BloodAltar(false) { // Seer Sigil
            @Override
            protected void addInformation(List<Pair<Sprite, Function<TileAltar, String>>> information) {
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 46, 16, 16),
                        altar -> NumeralHelper.toRoman(altar.getTier().toInt())
                ));
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 16, 46, 16, 16),
                        altar -> String.format("%d/%d", altar.getCurrentBlood(), altar.getCapacity())
                ));
                information.add(Pair.of( // Craft Progress
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 32, 46, 16, 16),
                        altar -> {
                            if (!altar.isActive())
                                return "Inactive"; // FIXME localize
                            int progress = altar.getProgress();
                            int totalLiquidRequired = altar.getLiquidRequired() * altar.getStackInSlot(0).getCount();
                            return String.format("%d/%d", progress, totalLiquidRequired);
                        }
                ));
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 48, 46, 16, 16),
                        altar -> String.valueOf((int) (altar.getConsumptionRate() * (altar.getConsumptionMultiplier() + 1)))
                ));
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 64, 46, 16, 16),
                        altar -> String.valueOf(altar.getTotalCharge())
                ));
            }
        };
    }

    @Override
    public void tryHandleBlockModel(Block block, String name) {
        if (block instanceof IVariantProvider) {
            IVariantProvider variantProvider = (IVariantProvider) block;
            for (Pair<Integer, String> variant : variantProvider.getVariants())
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), variant.getLeft(), new ModelResourceLocation(new ResourceLocation(BloodMagic.MODID, name), variant.getRight()));
        }
    }

    @Override
    public void tryHandleItemModel(Item item, String name) {
        if (item instanceof IMeshProvider) {
            IMeshProvider meshProvider = (IMeshProvider) item;
            ModelLoader.setCustomMeshDefinition(item, meshProvider.getMeshDefinition());
            ResourceLocation resourceLocation = meshProvider.getCustomLocation();
            if (resourceLocation == null)
                resourceLocation = new ResourceLocation(BloodMagic.MODID, "item/" + name);
            for (String variant : meshProvider.getVariants())
                ModelLoader.registerItemVariants(item, new ModelResourceLocation(resourceLocation, variant));
        } else if (item instanceof IVariantProvider) {
            IVariantProvider variantProvider = (IVariantProvider) item;
            for (Pair<Integer, String> variant : variantProvider.getVariants())
                ModelLoader.setCustomModelResourceLocation(item, variant.getLeft(), new ModelResourceLocation(new ResourceLocation(BloodMagic.MODID, "item/" + name), variant.getRight()));
        }
    }

    private void addElytraLayer() {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        try {
            Map<String, RenderPlayer> skinMap = ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, renderManager, "skinMap", "field_178636_l");
            skinMap.get("default").addLayer(new LayerBloodElytra(skinMap.get("default")));
            skinMap.get("slim").addLayer(new LayerBloodElytra(skinMap.get("slim")));
            BloodMagic.instance.logger.info("Elytra layer added");
        } catch (Exception e) {
            BloodMagic.instance.logger.error("Failed to set custom Elytra Layer for Elytra Living Armour Upgrade.");
            BloodMagic.instance.logger.error(e.getLocalizedMessage());
        }
    }

    @Override
    public IAnimationStateMachine load(ResourceLocation location, ImmutableMap<String, ITimeValue> parameters) {
        return ModelLoaderRegistry.loadASM(location, parameters);
    }
}
