package WayofTime.alchemicalWizardry.client;

import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.CommonProxy;
import WayofTime.alchemicalWizardry.common.EntityAirElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityBileDemon;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityBoulderFist;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityEarthElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFallenAngel;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFireElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityHolyElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityIceDemon;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityLowerGuardian;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityShade;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityShadeElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntitySmallEarthGolem;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityWaterElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityWingedFireDemon;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityEnergyBazookaMainProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityMeteor;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderConduit;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderPedestal;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderPlinth;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderWritingTable;
import WayofTime.alchemicalWizardry.common.renderer.block.TEAltarRenderer;
import WayofTime.alchemicalWizardry.common.renderer.block.itemRender.TEAltarItemRenderer;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderBileDemon;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderBoulderFist;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderElemental;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderFallenAngel;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderIceDemon;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderLowerGuardian;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderShade;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderSmallEarthGolem;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderWingedFireDemon;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelBileDemon;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelBoulderFist;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelElemental;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelFallenAngel;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelIceDemon;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelLowerGuardian;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelShade;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelSmallEarthGolem;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelWingedFireDemon;
import WayofTime.alchemicalWizardry.common.renderer.projectile.RenderEnergyBazookaMainProjectile;
import WayofTime.alchemicalWizardry.common.renderer.projectile.RenderEnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.renderer.projectile.RenderMeteor;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEConduit;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPedestal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	public static int renderPass;
	public static int altarRenderType;

	@Override
	public void registerRenderers()
	{
		//altarRenderType = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerEntityRenderingHandler(EnergyBlastProjectile.class, new RenderEnergyBlastProjectile());
		RenderingRegistry.registerEntityRenderingHandler(EntityEnergyBazookaMainProjectile.class, new RenderEnergyBazookaMainProjectile());
		RenderingRegistry.registerEntityRenderingHandler(EntityMeteor.class, new RenderMeteor());
		//EntityRegistry.registerGlobalEntityID(EntityFallenAngel.class, "AlchemicalWizardry.FallenAngel", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityFallenAngel.class, new RenderFallenAngel(new ModelFallenAngel(), 0.5F));
		//EntityRegistry.registerGlobalEntityID(EntityLowerGuardian.class, "AlchemicalWizardry.LowerGuardian", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityLowerGuardian.class, new RenderLowerGuardian(new ModelLowerGuardian(), 0.5F));
		//EntityRegistry.registerGlobalEntityID(EntityBileDemon.class, "AlchemicalWizardry.BileDemon", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityBileDemon.class, new RenderBileDemon(new ModelBileDemon(), 1.5F));
		//EntityRegistry.registerGlobalEntityID(EntityWingedFireDemon.class, "AlchemicalWizardry.WingedFireDemon", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityWingedFireDemon.class, new RenderWingedFireDemon(new ModelWingedFireDemon(), 1.0F));
		//EntityRegistry.registerGlobalEntityID(EntitySmallEarthGolem.class, "AlchemicalWizardry.SmallEarthGolem", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntitySmallEarthGolem.class, new RenderSmallEarthGolem(new ModelSmallEarthGolem(), 0.5F));
		//EntityRegistry.registerGlobalEntityID(EntityIceDemon.class, "AlchemicalWizardry.IceDemon", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityIceDemon.class, new RenderIceDemon(new ModelIceDemon(), 0.5F));
		// EntityRegistry.registerGlobalEntityID(EntityBoulderFist.class, "AlchemicalWizardry.BoulderFist", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityBoulderFist.class, new RenderBoulderFist(new ModelBoulderFist(), 0.5F));
		//EntityRegistry.registerGlobalEntityID(EntityShade.class, "AlchemicalWizardry.Shade", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityShade.class, new RenderShade(new ModelShade(), 0.5F));
		//EntityRegistry.registerGlobalEntityID(EntityAirElemental.class, "AlchemicalWizardry.AirElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityAirElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
		//EntityRegistry.registerGlobalEntityID(EntityWaterElemental.class, "AlchemicalWizardry.WaterElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
		//EntityRegistry.registerGlobalEntityID(EntityEarthElemental.class, "AlchemicalWizardry.EarthElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityEarthElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
		//EntityRegistry.registerGlobalEntityID(EntityFireElemental.class, "AlchemicalWizardry.FireElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityFireElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
		//EntityRegistry.registerGlobalEntityID(EntityShadeElemental.class, "AlchemicalWizardry.ShadeElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityShadeElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
		//EntityRegistry.registerGlobalEntityID(EntityHolyElemental.class, "AlchemicalWizardry.HolyElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
		RenderingRegistry.registerEntityRenderingHandler(EntityHolyElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
		ClientRegistry.bindTileEntitySpecialRenderer(TEPedestal.class, new RenderPedestal());
		ClientRegistry.bindTileEntitySpecialRenderer(TEPlinth.class, new RenderPlinth());
		ClientRegistry.bindTileEntitySpecialRenderer(TEWritingTable.class, new RenderWritingTable());
		ClientRegistry.bindTileEntitySpecialRenderer(TEConduit.class, new RenderConduit());
		//RenderingRegistry.registerEntityRenderingHandler(FireProjectile.class, new RenderFireProjectile());
		//RenderingRegistry.registerBlockHandler(new AltarRenderer());
	}

	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public void InitRendering()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TEAltar.class, new TEAltarRenderer());
		MinecraftForgeClient.registerItemRenderer(AlchemicalWizardry.blockAltar.blockID, new TEAltarItemRenderer());
		//MinecraftForgeClient.registerItemRenderer(AlchemicalWizardry.blockWritingTable.blockID, new TEWritingTableItemRenderer());
	}
}
