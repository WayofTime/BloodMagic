//package WayofTime.alchemicalWizardry.common.rituals;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import net.minecraft.block.Block;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.util.BlockPos;
//import net.minecraft.util.MathHelper;
//import net.minecraft.world.World;
//import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
//import WayofTime.alchemicalWizardry.api.rituals.LocalRitualStorage;
//import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
//import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
//import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
//import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGrunt;
//import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.IHoardDemon;
//
//public class RitualEffectAlphaPact extends RitualEffect
//{
//	Random rand = new Random();
//	
//    @Override
//    public void performEffect(IMasterRitualStone ritualStone)
//    {
//        String owner = ritualStone.getOwner();
//
//        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
//        World world = ritualStone.getWorldObj();
//        BlockPos pos = ritualStone.getPosition();
//
//        if (world.getWorldTime() % 20 != 0)
//        {
//            return;
//        }
//                
//        LocalRitualStorage stor = ritualStone.getLocalStorage();
//        if(stor instanceof LocalStorageAlphaPact)
//        {
//        	LocalStorageAlphaPact storage = (LocalStorageAlphaPact)stor;
//        	
//        	Object[] demonList = storage.hoardList.toArray();
//        	
//    		for(Object demon : demonList)
//        	{
//        		if(demon instanceof EntityLivingBase)
//        		{
//        			if(!((EntityLivingBase) demon).isEntityAlive())
//        			{
//        				System.out.println(storage.hoardList.remove(demon));
//        			}
//        		}
//        	}
//        	
//            System.out.println("Hi!");
//
//        	int summons = 0;
//        	
//        	int horizontalRange = 25;
//        	int verticalRange = 20;
//        	
//        	if(storage.hoardList.isEmpty())
//        	{
//        		IHoardDemon demon = this.getRandomDemonForStage(world, x, y, z, horizontalRange, verticalRange);
//        		if(demon instanceof EntityLivingBase)
//        		{
//        			world.spawnEntityInWorld((EntityLivingBase)demon);
//        			storage.thrallDemon(demon);
//        		}
//        	}else
//        	{
//        	}
//        }
//    }
//    
//    public IHoardDemon getRandomDemonForStage(World world, int x, int y, int z, int horizontalRange, int verticalRange)
//    {
//    	EntityLivingBase entityLiving = new EntityMinorDemonGrunt(world);
//    	
//    	boolean isGood = false;
//    	for(int n=0; n<100; n++)
//    	{
//    		double newX = x + (rand.nextInt(horizontalRange) - horizontalRange) + 0.5;
//            double newY = y + (double) (rand.nextInt((int) verticalRange));
//            double newZ = z + (rand.nextInt(horizontalRange) - horizontalRange) + 0.5;
//            
//            entityLiving.posX = newX;
//            entityLiving.posY = newY;
//            entityLiving.posZ = newZ;
//            
//            int i = MathHelper.floor_double(entityLiving.posX);
//            int j = MathHelper.floor_double(entityLiving.posY);
//            int k = MathHelper.floor_double(entityLiving.posZ);
//            Block l;
//
//            if (entityLiving.worldObj.blockExists(i, j, k))
//            {
//            	boolean flag1 = false;
//
//                while (!flag1 && j > 0)
//                {
//                    l = entityLiving.worldObj.getBlock(i, j - 1, k);
//
//                    if (l != null && l.getMaterial().blocksMovement())
//                    {
//                        flag1 = true;
//                    } else
//                    {
//                        --entityLiving.posY;
//                        --j;
//                    }
//                }
//            }
//            
//            if(entityLiving.worldObj.getCollidingBoundingBoxes(entityLiving, entityLiving.boundingBox).isEmpty() && !entityLiving.worldObj.isAnyLiquid(entityLiving.boundingBox))
//            {
//            	entityLiving.setPositionAndUpdate(newX, newY, newZ);
//            	isGood = true;
//            }
//    	}
//    	
//    	if(isGood = false)
//    	{
//    		return null;
//    	}
//
//    	return (IHoardDemon)entityLiving;
//    }
//    
//    public int spawnMoreDemons(LocalStorageAlphaPact storage)
//    {
//    	return 5;
//    }
//
//    @Override
//    public int getCostPerRefresh()
//    {
//        return 1;
//    }
//    
//    public LocalRitualStorage getNewLocalStorage()
//    {
//    	return new LocalStorageAlphaPact();
//    }
//
//    @Override
//    public List<RitualComponent> getRitualComponentList()
//    {
//        ArrayList<RitualComponent> omegaRitual = new ArrayList();
//        
//        this.addCornerRunes(omegaRitual, 1, 0, RitualComponent.BLANK);
//        this.addOffsetRunes(omegaRitual, 2, 1, 0, RitualComponent.FIRE);
//        this.addParallelRunes(omegaRitual, 4, 0, RitualComponent.WATER);
//        this.addParallelRunes(omegaRitual, 5, 0, RitualComponent.EARTH);
//        this.addCornerRunes(omegaRitual, 4, 0, RitualComponent.AIR);
//        this.addOffsetRunes(omegaRitual, 3, 4, 0, RitualComponent.AIR);
//        this.addParallelRunes(omegaRitual, 5, 1, RitualComponent.WATER);
//        this.addParallelRunes(omegaRitual, 5, 2, RitualComponent.EARTH);
//        this.addParallelRunes(omegaRitual, 4, 3, RitualComponent.WATER);
//        this.addParallelRunes(omegaRitual, 4, 4, RitualComponent.WATER);
//        this.addParallelRunes(omegaRitual, 3, 5, RitualComponent.BLANK);
//        this.addParallelRunes(omegaRitual, 2, 5, RitualComponent.FIRE);
//        this.addParallelRunes(omegaRitual, 1, 5, RitualComponent.DUSK);
//        this.addOffsetRunes(omegaRitual, 5, 3, 1, RitualComponent.WATER);
//        this.addOffsetRunes(omegaRitual, 6, 3, 1, RitualComponent.DUSK);
//        this.addOffsetRunes(omegaRitual, 6, 4, 1, RitualComponent.FIRE);
//        this.addOffsetRunes(omegaRitual, 6, 5, 1, RitualComponent.BLANK);
//        this.addCornerRunes(omegaRitual, 4, 2, RitualComponent.FIRE);
//        this.addCornerRunes(omegaRitual, 4, 3, RitualComponent.AIR);
//        this.addCornerRunes(omegaRitual, 4, 4, RitualComponent.AIR);
//        this.addOffsetRunes(omegaRitual, 4, 3, 2, RitualComponent.BLANK);
//        this.addCornerRunes(omegaRitual, 3, 5, RitualComponent.EARTH);
//        this.addOffsetRunes(omegaRitual, 2, 3, 5, RitualComponent.AIR);
//
//        return omegaRitual;
//    }
//}
