package wayoftime.bloodmagic.common.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;


public class BloodMagicFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, BloodMagic.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, BloodMagic.MODID);
    public static final RegistryObject<FluidType> LIFE_ESSENCE_FLUID_TYPE = FLUID_TYPES.register("life_essence_fluid_type", () -> new FluidType(FluidType.Properties.create().descriptionId("fluid.bloodmagic.life_essence_fluid").fallDistanceModifier(0F).canExtinguish(false).canConvertToSource(false).supportsBoating(false).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH).canHydrate(false).viscosity(1000)) {
        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                public static final ResourceLocation FLUID_STILL = new ResourceLocation("bloodmagic:block/lifeessencestill");
                public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("bloodmagic:block/lifeessenceflowing");

                @Override
                public ResourceLocation getStillTexture() {
                    return FLUID_STILL;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return FLUID_FLOWING;
                }
            });
        }
    });
    public static final RegistryObject<FluidType> DOUBT_FLUID_TYPE = FLUID_TYPES.register("doubt_fluid_type", () -> new FluidType(FluidType.Properties.create().descriptionId("fluid.bloodmagic.doubt_fluid").fallDistanceModifier(0F).canExtinguish(false).canConvertToSource(false).supportsBoating(false).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH).canHydrate(false).viscosity(1000)) {
        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                public static final ResourceLocation DOUBT_STILL_RESOURCE = new ResourceLocation("bloodmagic:block/liquid_doubt_still");
                public static final ResourceLocation DOUBT_FLOWING_RESOURCE = new ResourceLocation("bloodmagic:block/liquid_doubt_flowing");

                @Override
                public ResourceLocation getStillTexture() {
                    return DOUBT_STILL_RESOURCE;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return DOUBT_FLOWING_RESOURCE;
                }
            });
        }
    });

    private static ForgeFlowingFluid.Properties makeLifeEssenceProperties() {
        return new ForgeFlowingFluid.Properties(LIFE_ESSENCE_FLUID_TYPE, LIFE_ESSENCE_FLUID, LIFE_ESSENCE_FLUID_FLOWING).block(LIFE_ESSENCE_BLOCK).bucket(BloodMagicItems.LIFE_ESSENCE_BUCKET);
    }

    private static ForgeFlowingFluid.Properties makeDoubtProperties() {
        return new ForgeFlowingFluid.Properties(DOUBT_FLUID_TYPE, DOUBT_FLUID, DOUBT_FLUID_FLOWING).block(DOUBT_BLOCK).bucket(BloodMagicItems.DOUBT_BUCKET);
    }
    public static final RegistryObject<FlowingFluid> LIFE_ESSENCE_FLUID = FLUIDS.register("life_essence_fluid", () -> new ForgeFlowingFluid.Source(makeLifeEssenceProperties()));
    public static final RegistryObject<Fluid> LIFE_ESSENCE_FLUID_FLOWING = FLUIDS.register("life_essence_fluid_flowing", () -> new ForgeFlowingFluid.Flowing(makeLifeEssenceProperties()));

    public static final RegistryObject<FlowingFluid> DOUBT_FLUID = FLUIDS.register("doubt_fluid", () -> new ForgeFlowingFluid.Source(makeDoubtProperties()));
    public static final RegistryObject<Fluid> DOUBT_FLUID_FLOWING = FLUIDS.register("doubt_fluid_flowing", () -> new ForgeFlowingFluid.Flowing(makeDoubtProperties()));

    public static final RegistryObject<LiquidBlock> LIFE_ESSENCE_BLOCK = BloodMagicBlocks.BLOCKS.register("life_essence_block", () -> new LiquidBlock(LIFE_ESSENCE_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final RegistryObject<LiquidBlock> DOUBT_BLOCK = BloodMagicBlocks.BLOCKS.register("doubt_block", () -> new LiquidBlock(DOUBT_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER)));
}
