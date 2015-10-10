package com.cricketcraft.chisel.api.rendering;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.cricketcraft.chisel.api.carving.CarvableHelper;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.cricketcraft.ctmlib.CTM;
import com.cricketcraft.ctmlib.ISubmapManager;
import com.cricketcraft.ctmlib.RenderBlocksCTM;
import com.cricketcraft.ctmlib.RenderBlocksColumn;
import com.cricketcraft.ctmlib.TextureSubmap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Handles all default {@link ISubmapManager} behavior
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public enum TextureType {

	// @formatter:off
	TOPSIDE("top", "side") {
		@Override
		protected Object registerIcons(ICarvingVariation variation, String modName, String texturePath, IIconRegister register) {
			return new IIcon[]{
					register.registerIcon(modName + ":" + texturePath + "-side"),
					register.registerIcon(modName + ":" + texturePath + "-top")
			};
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, int side, int meta) {
			IIcon[] icons = (IIcon[]) cachedObject;
			return side > 1 ? icons[0] : icons[1];
		}
	},
	TOPBOTSIDE("top", "bottom", "side"){
		@Override
		protected Object registerIcons(ICarvingVariation variation, String modName, String texturePath, IIconRegister register) {
			return new IIcon[]{
					register.registerIcon(modName + ":" + texturePath + "-side"),
					register.registerIcon(modName + ":" + texturePath + "-bottom"),
					register.registerIcon(modName + ":" + texturePath + "-top")
			};
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, int side, int meta) {
			IIcon[] icons = (IIcon[]) cachedObject;
			return side > 1 ? icons[0] : icons[side + 1];
		}
	},
	CTMV("ctmv", "top"){
				
		@Override
		protected Object registerIcons(ICarvingVariation variation, String modName, String texturePath, IIconRegister register) {
			return Pair.of(new TextureSubmap(register.registerIcon(modName + ":" + texturePath + "-ctmv"), 2, 2), register.registerIcon(modName + ":" + texturePath + "-top"));
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, int side, int meta) {
			Pair<TextureSubmap, IIcon> data = (Pair<TextureSubmap, IIcon>) cachedObject;
			return side < 2 ? data.getRight() : data.getLeft().getSubIcon(0, 0);
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, IBlockAccess world, int x, int y, int z, int side, int meta) {
			Pair<TextureSubmap, IIcon> data = (Pair<TextureSubmap, IIcon>) cachedObject;
			if (side < 2)
				return data.getRight();

			Block block = world.getBlock(x, y, z);
			boolean topConnected = ctm.isConnected(world, x, y + 1, z, side, block, meta);
			boolean botConnected = ctm.isConnected(world, x, y - 1, z, side, block, meta);

			TextureSubmap map = data.getLeft();
			if (topConnected && botConnected)
				return map.getSubIcon(0, 1);
			if (topConnected && !botConnected)
				return map.getSubIcon(1, 1);
			if (!topConnected && botConnected)
				return map.getSubIcon(1, 0);
			return map.getSubIcon(0, 0);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		protected RenderBlocks createRenderContext(RenderBlocks rendererOld, IBlockAccess world, Object cachedObject) {
			RenderBlocksColumn ret = theRenderBlocksColumn;
			Pair<TextureSubmap, IIcon> data = (Pair<TextureSubmap, IIcon>) cachedObject;
			
			ret.blockAccess = world;
			ret.renderMaxX = 1.0;
			ret.renderMaxY = 1.0;
			ret.renderMaxZ = 1.0;

			ret.submap = data.getLeft();
			ret.iconTop = data.getRight();
			return ret;
		}
	},
	CTMH("ctmh", "top"){
		@Override
		protected Object registerIcons(ICarvingVariation variation, String modName, String texturePath, IIconRegister register) {
			return Pair.of(new TextureSubmap(register.registerIcon(modName + ":" + texturePath + "-ctmh"), 2, 2), register.registerIcon(modName + ":" + texturePath + "-top"));
		}		
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, int side, int meta) {
			return CTMV.getIcon(variation, cachedObject, side, meta);
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, IBlockAccess world, int x, int y, int z, int side, int meta) {
			Pair<TextureSubmap, IIcon> data = (Pair<TextureSubmap, IIcon>) cachedObject;
			if (side < 2)
				return data.getRight();

			Block block = ctm.getBlockOrFacade(world, x, y, z, side);

			boolean p;
			boolean n;
			boolean reverse = side == 3 || side == 4;

			if (side < 4) {
				p = ctm.isConnected(world, x - 1, y, z, side, block, meta);
				n = ctm.isConnected(world, x + 1, y, z, side, block, meta);
			} else {
				p = ctm.isConnected(world, x, y, z - 1, side, block, meta);
				n = ctm.isConnected(world, x, y, z + 1, side, block, meta);
			}
			
			TextureSubmap map = data.getLeft();
			if (p && n)
				return map.getSubIcon(1, 0);
			else if (p)
				return map.getSubIcon(reverse ? 1 : 0, 1);
			else if (n)
				return map.getSubIcon(reverse ? 0 : 1, 1);
			return map.getSubIcon(0, 0);
		}
	},
	V9("v9"){
		@Override
		protected Object registerIcons(ICarvingVariation variation, String modName, String texturePath, IIconRegister register) {
			return new TextureSubmap(register.registerIcon(modName + ":" + texturePath + "-v9"), 3, 3);
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, int side, int meta) {
			return ((TextureSubmap)cachedObject).getSubIcon(1, 1);
		}

		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, IBlockAccess world, int x, int y, int z, int side, int meta) {           
			return getVIcon(this, (TextureSubmap) cachedObject, x, y, z, side);
		}
	},
	V4("v4"){
		@Override
		protected Object registerIcons(ICarvingVariation variation, String modName, String texturePath, IIconRegister register) {
			return new TextureSubmap(register.registerIcon(modName + ":" + texturePath + "-v4"), 2, 2);
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, int side, int meta) {
			return ((TextureSubmap)cachedObject).getSubIcon(0, 0);
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, IBlockAccess world, int x, int y, int z, int side, int meta) {
			return getVIcon(this, (TextureSubmap) cachedObject, x, y, z, side);
		}
	},
	CTMX("", "ctm"){
		@Override
		protected Object registerIcons(ICarvingVariation variation, String modName, String texturePath, IIconRegister register) {
			IIcon baseIcon = register.registerIcon(modName + ":" + texturePath);
			return Triple.of(
					baseIcon,
					new TextureSubmap(register.registerIcon(modName + ":" + texturePath + "-ctm"), 4, 4),
					new TextureSubmap(baseIcon, 2, 2)
			);
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, int side, int meta) {
			return ((Triple<IIcon, ?, ?>)cachedObject).getLeft();
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		protected RenderBlocks createRenderContext(RenderBlocks rendererOld, IBlockAccess world, Object cachedObject) {
			RenderBlocksCTM ret = theRenderBlocksCTM;
			Triple<?, TextureSubmap, TextureSubmap> data = (Triple<?, TextureSubmap, TextureSubmap>) cachedObject;
			ret.blockAccess = world;

			ret.submap = data.getMiddle();
			ret.submapSmall = data.getRight();

			ret.rendererOld = rendererOld;
			return ret;
		}
	},
	R16("r16"){
		@Override
		protected Object registerIcons(ICarvingVariation variation, String modName, String texturePath, IIconRegister register) {
			return new TextureSubmap(register.registerIcon(modName + ":" + texturePath + "-r16"), 4, 4);
		}		
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, int side, int meta) {
			return V9.getIcon(variation, cachedObject, side, meta);
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, IBlockAccess world, int x, int y, int z, int side, int meta) {
			return getRIcon(this, (TextureSubmap) cachedObject, x, y, z, side);
		}
	},
	R9("r9"){
		@Override
		protected Object registerIcons(ICarvingVariation variation, String modName, String texturePath, IIconRegister register) {
			return new TextureSubmap(register.registerIcon(modName + ":" + texturePath + "-r9"), 3, 3);
		}	
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, int side, int meta) {
			return V9.getIcon(variation, cachedObject, side, meta);
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, IBlockAccess world, int x, int y, int z, int side, int meta) {
			return getRIcon(this, (TextureSubmap) cachedObject, x, y, z, side);
		}
	},
	R4("r4"){
		
		@Override
		protected Object registerIcons(ICarvingVariation variation, String modName, String texturePath, IIconRegister register) {
			return new TextureSubmap(register.registerIcon(modName + ":" + texturePath + "-r4"), 2, 2);
		}	
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, int side, int meta) {
			return V4.getIcon(variation, cachedObject, side, meta);
		}
		
		@Override
		protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, IBlockAccess world, int x, int y, int z, int side, int meta) {
			return getRIcon(this, (TextureSubmap) cachedObject, x, y, z, side);
		}
	},
	NORMAL,
	CUSTOM;
	
	/* Some util stuff for shared code between v* and r* */
	
	public static IIcon getVIcon(TextureType type, TextureSubmap map, int x, int y, int z, int side) {
		int variationSize = (type == TextureType.V9) ? 3 : 2;

		int xModulus = x % variationSize;
		int zModulus = z % variationSize;
		//This ensures that blocks placed near 0,0 or it's axis' do not misbehave
		int textureX = (xModulus < 0) ? (xModulus + variationSize) : xModulus;
		int textureZ = (zModulus < 0) ? (zModulus + variationSize) : zModulus;
		//Always invert the y index
		int textureY = (variationSize - (y % variationSize) - 1);

		if (side == 2 || side == 5) {
			//For WEST, SOUTH reverse the indexes for both X and Z
			textureX = (variationSize - textureX - 1);
			textureZ = (variationSize - textureZ - 1);
		} /*else if (side == 0) {
        //For DOWN, reverse the indexes for only Z
        textureZ = (variationSize - textureZ - 1);
    	}*/

		int index;
		if (side == 0 || side == 1) {
			// DOWN || UP
			index = textureX + textureZ * variationSize;
		} else if (side == 2 || side == 3) {
			// NORTH || SOUTH
			index = textureX + textureY * variationSize;
		} else {
			// WEST || EAST
			index = textureZ + textureY * variationSize;
		}

		return map.getSubIcon(index % variationSize, index / variationSize);
	}
	
	public static IIcon getRIcon(TextureType type, TextureSubmap map, int x, int y, int z, int side) {
		rand.setSeed(getCoordinateRandom(x, y, z));
		rand.nextBoolean();
		int size = type == TextureType.R4 ? 2 : type == TextureType.R9 ? 3 : 4;
		return map.getSubIcon(rand.nextInt(size), rand.nextInt(size));
	}
	
	private static long getCoordinateRandom(int x, int y, int z) {
		// MC 1.8 code...
		long l = (x * 3129871) ^ z * 116129781L ^ y;
		l = l * l * 42317861L + l * 11L;
		return l;
    }
	
	/* End of that mess */
	
	private static final TextureType[] VALUES;
	private static final CTM ctm = CTM.getInstance();
	private static final Random rand = new Random();
	@SideOnly(Side.CLIENT)
	private static RenderBlocksCTM theRenderBlocksCTM;
	@SideOnly(Side.CLIENT)
	private static RenderBlocksColumn theRenderBlocksColumn;

	private String[] suffixes;
	static {
		VALUES = ArrayUtils.subarray(values(), 0, values().length - 1);
	}

	private TextureType(String... suffixes) {
		this.suffixes = suffixes.length == 0 ? new String[] { "" } : suffixes;
	}
	
	private static void initStatics() {
		if (theRenderBlocksCTM == null) {
			theRenderBlocksCTM = new RenderBlocksCTM();
			theRenderBlocksColumn = new RenderBlocksColumn();
		}
	}
	
	public ISubmapManager createManagerFor(ICarvingVariation variation, String texturePath) {
		return new SubmapManagerDefault(this, variation, texturePath);
	}
	
	public ISubmapManager createManagerFor(ICarvingVariation variation, Block block, int meta) {
		return new SubmapManagerExistingIcon(this, variation, block, meta);
	}
	
	@SideOnly(Side.CLIENT)
	protected Object registerIcons(ICarvingVariation variation, String modName, String texturePath, IIconRegister register) {
		return register.registerIcon(modName + ":" + texturePath);
	}
	
	protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, int side, int meta) {
		return (IIcon) cachedObject;
	}
	
	protected IIcon getIcon(ICarvingVariation variation, Object cachedObject, IBlockAccess world, int x, int y, int z, int side, int meta) {
		return getIcon(variation, cachedObject, side, meta);
	}

	@SideOnly(Side.CLIENT)
	protected RenderBlocks createRenderContext(RenderBlocks rendererOld, IBlockAccess world, Object cachedObject) {
		return rendererOld;
	}
	
	public static TextureType getTypeFor(CarvableHelper inst, String modid, String path) {
		if (path == null) {
			return CUSTOM;
		}
		for (TextureType t : VALUES) {
			boolean matches = true;
			for (String s : t.suffixes) {
				if (!exists(modid, path, s.isEmpty() ? s : "-" + s)) {
					matches = false;
				}
			}
			if (matches) {
				return t;
			}
		}
		return CUSTOM;
	}
	
	// This is ugly, but faster than class.getResource
	private static boolean exists(String modid, String path, String postfix) {
		ResourceLocation rl = new ResourceLocation(modid, "textures/blocks/" + path + postfix + ".png");
		try {
			Minecraft.getMinecraft().getResourceManager().getAllResources(rl);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public abstract static class AbstractSubmapManager implements ISubmapManager {
		protected final TextureType type;
		protected ICarvingVariation variation;
		protected Object cachedObject;
		
		private AbstractSubmapManager(TextureType type, ICarvingVariation variation) {
			this.type = type;
			this.variation = variation;
		}
		
		@Override
		public IIcon getIcon(int side, int meta) {
			return type.getIcon(variation, cachedObject, side, meta);
		}

		@Override
		public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
			return type.getIcon(variation, cachedObject, world, x, y, z, side, world.getBlockMetadata(x, y, z));
		}
		
		@Override
		public RenderBlocks createRenderContext(RenderBlocks rendererOld, Block block, IBlockAccess world) {
			initStatics();
			RenderBlocks rb = type.createRenderContext(rendererOld, world, cachedObject);
			rb.setRenderBoundsFromBlock(block);
			return rb;
		}
		
		@Override
		public void preRenderSide(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		}
		
		@Override
		public void postRenderSide(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {			
		}
		
		public Object getCachedObject() {
			return cachedObject;
		}
	}

	@SideOnly(Side.CLIENT)
	private static class SubmapManagerDefault extends AbstractSubmapManager {

		private String texturePath;
		
		private SubmapManagerDefault(TextureType type, ICarvingVariation variation, String texturePath) {
			super(type, variation);
			this.texturePath = texturePath;
		}

		@Override
		public void registerIcons(String modName, Block block, IIconRegister register) {
			cachedObject = type.registerIcons(variation, modName, texturePath, register);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static class SubmapManagerExistingIcon extends AbstractSubmapManager {
		
		private Block block;
		private int meta;
		
		private SubmapManagerExistingIcon(TextureType type, ICarvingVariation variation, Block block, int meta) {
			super(type, variation);
			this.block = block;
			this.meta = meta;
		}
		
		@Override
		public IIcon getIcon(int side, int meta) {
			return block.getIcon(side, this.meta);
		}
		
		@Override
		public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
			return getIcon(side, world.getBlockMetadata(x, y, z));
		}

		@Override
		public void registerIcons(String modName, Block block, IIconRegister register) {
		}
	}
}