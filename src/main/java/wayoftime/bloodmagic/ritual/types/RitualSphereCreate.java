package wayoftime.bloodmagic.ritual.types;

import java.util.function.Consumer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("sphere")
public class RitualSphereCreate extends Ritual
{
	public static final String SPHEROID_RANGE = "spheroidRange";

	private boolean cached = false;
	private BlockPos currentPos; // Offset

	public RitualSphereCreate()
	{
		super("ritualSphere", 0, 20000, "ritual." + BloodMagic.MODID + ".sphereRitual");
		addBlockRange(SPHEROID_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-16, -35, -16), new BlockPos(17, -2, 17)));

		setMaximumVolumeAndDistanceOfRange(SPHEROID_RANGE, 0, 32, 70);
	}

	public void readFromNBT(CompoundTag tag)
	{
		super.readFromNBT(tag);
		currentPos = new BlockPos(tag.getInt(Constants.NBT.X_COORD), tag.getInt(Constants.NBT.Y_COORD), tag.getInt(Constants.NBT.Z_COORD));
	}

	public void writeToNBT(CompoundTag tag)
	{
		super.writeToNBT(tag);
		if (currentPos != null)
		{
			tag.putInt(Constants.NBT.X_COORD, currentPos.getX());
			tag.putInt(Constants.NBT.Y_COORD, currentPos.getY());
			tag.putInt(Constants.NBT.Z_COORD, currentPos.getZ());
		}
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		BlockPos masterPos = masterRitualStone.getMasterBlockPos();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		AreaDescriptor sphereRange = masterRitualStone.getBlockRange(SPHEROID_RANGE);
		AABB sphereBB = sphereRange.getAABB(masterPos);
		int minX = (int) (masterPos.getX() - sphereBB.minX);
		int maxX = (int) (sphereBB.maxX - masterPos.getX()) - 1;
		int minY = (int) (masterPos.getY() - sphereBB.minY);
		int maxY = (int) (sphereBB.maxY - masterPos.getY()) - 1;
		int minZ = (int) (masterPos.getZ() - sphereBB.minZ);
		int maxZ = (int) (sphereBB.maxZ - masterPos.getZ()) - 1;

		double sphereCenterX = (maxX + -minX) / 2; // These are... potentially negative.
		double sphereCenterY = (maxY + -minY) / 2;
		double sphereCenterZ = (maxZ + -minZ) / 2;

		int yTeleportOffset = (int) (-sphereCenterY * 2);

		double xR = (maxX + minX) / 2.0;
		double yR = (maxY + minY) / 2.0;
		double zR = (maxZ + minZ) / 2.0;

		int j = -minY;
		int i = -minX;
		int k = -minZ;

		if (currentPos != null)
		{
			j = currentPos.getY();
			i = currentPos.getX();
			k = currentPos.getZ();
		}
		int checks = 0;
		int maxChecks = 100;

		while (j <= maxY)
		{
			while (i <= maxX)
			{
				while (k <= maxZ)
				{
					checks++;
					if (checks >= maxChecks)
					{
//						System.out.println("Reached max checks: " + this.currentPos);
						this.currentPos = new BlockPos(i, j, k);
//						System.out.println(this.currentPos);
						return;
					}

//							if (checkIfEllipsoidShell(xR, yR, zR, i - sphereCenterX, j - sphereCenterY, k - sphereCenterZ))
					if (checkIfEllipsoid(xR, yR, zR, i - sphereCenterX, j - sphereCenterY, k - sphereCenterZ))
					{
						BlockPos newPos = masterPos.offset(i, j, k);

						if (world.isEmptyBlock(newPos))
						{
							// Don't swap the location, since this is empty
							k++;
							continue;
						}

						BlockPos swapPos = newPos.offset(0, yTeleportOffset, 0);
//
						Utils.swapLocations(world, newPos, world, swapPos);

						if (world.isEmptyBlock(newPos))
						{
							// TODO: If we have Will, fill in the space with something related to the type.
						}
//
////								BlockState placeState = Block.getBlockFromItem(itemHandler.getStackInSlot(blockSlot).getItem()).getDefaultState();
//								BlockState placeState = Blocks.STONE.getDefaultState();
//								world.setBlockState(newPos, placeState);

						masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
						k++;
						this.currentPos = new BlockPos(i, j, k);
//								System.out.println(this.currentPos);
						return;
					}
					k++;
				}
				i++;
				k = -minZ;
			}
			j++;
			i = -minX;
			this.currentPos = new BlockPos(i, j, k);
			return;
		}

		masterRitualStone.setActive(false);

	}

	public boolean checkIfEllipsoidShell(double xR, double yR, double zR, double xOff, double yOff, double zOff)
	{
		// Checking shell in the x-direction
		if (!checkIfEllipsoid(xR, yR, zR, xOff, yOff, zOff))
		{
			return false;
		}

		return !((checkIfEllipsoid(xR, yR, zR, xOff + 1, yOff, zOff) && checkIfEllipsoid(xR, yR, zR, xOff - 1, yOff, zOff)) && (checkIfEllipsoid(xR, yR, zR, xOff, yOff + 1, zOff) && checkIfEllipsoid(xR, yR, zR, xOff, yOff - 1, zOff)) && (checkIfEllipsoid(xR, yR, zR, xOff, yOff, zOff + 1) && checkIfEllipsoid(xR, yR, zR, xOff, yOff, zOff - 1)));
	}

	public boolean checkIfEllipsoid(double xR, double yR, double zR, double xOff, double yOff, double zOff)
	{
		float possOffset = 0.5f;
		return xOff * xOff / ((xR + possOffset) * (xR + possOffset)) + yOff * yOff / ((yR + possOffset) * (yR + possOffset)) + zOff * zOff / ((zR + possOffset) * (zR + possOffset)) <= 1;
	}

	@Override
	public int getRefreshCost()
	{
		return 10;
	}

	@Override
	public int getRefreshTime()
	{
		return 1;
	}

//    @Override
//    public void readFromNBT(NBTTagCompound tag)
//    {
//        super.readFromNBT(tag);
//        tag
//    }

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addRune(components, 1, 0, 1, EnumRuneType.EARTH);
		addRune(components, 1, 0, -1, EnumRuneType.EARTH);
		addRune(components, -1, 0, 1, EnumRuneType.EARTH);
		addRune(components, -1, 0, -1, EnumRuneType.EARTH);
		addRune(components, 2, 1, 0, EnumRuneType.EARTH);
		addRune(components, 0, 1, 2, EnumRuneType.EARTH);
		addRune(components, -2, 1, 0, EnumRuneType.EARTH);
		addRune(components, 0, 1, -2, EnumRuneType.EARTH);
		addRune(components, 2, 1, 2, EnumRuneType.AIR);
		addRune(components, 2, 1, -2, EnumRuneType.AIR);
		addRune(components, -2, 1, 2, EnumRuneType.AIR);
		addRune(components, -2, 1, -2, EnumRuneType.AIR);
		addRune(components, 2, 2, 0, EnumRuneType.FIRE);
		addRune(components, 0, 2, 2, EnumRuneType.FIRE);
		addRune(components, -2, 2, 0, EnumRuneType.FIRE);
		addRune(components, 0, 2, -2, EnumRuneType.DUSK);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualSphereCreate();
	}
}
