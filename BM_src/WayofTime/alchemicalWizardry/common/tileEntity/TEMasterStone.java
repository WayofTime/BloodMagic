package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.rituals.Rituals;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TEMasterStone extends TileEntity
{
    private int currentRitual;
    private boolean isActive;
    private String owner;
    private int cooldown;
    private int var1;
    private int direction;

    public TEMasterStone()
    {
        currentRitual = 0;
        isActive = false;
        owner = "";
        cooldown = 0;
        var1 = 0;
        direction = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        currentRitual = par1NBTTagCompound.getInteger("currentRitual");
        isActive = par1NBTTagCompound.getBoolean("isActive");
        owner = par1NBTTagCompound.getString("owner");
        cooldown = par1NBTTagCompound.getInteger("cooldown");
        var1 = par1NBTTagCompound.getInteger("var1");
        direction = par1NBTTagCompound.getInteger("direction");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("currentRitual", currentRitual);
        par1NBTTagCompound.setBoolean("isActive", isActive);
        par1NBTTagCompound.setString("owner", owner);
        par1NBTTagCompound.setInteger("cooldown", cooldown);
        par1NBTTagCompound.setInteger("var1", var1);
        par1NBTTagCompound.setInteger("direction", direction);
    }

    public void activateRitual(World world, int crystalLevel)
    {
        int testRitual = Rituals.checkValidRitual(world, xCoord, yCoord, zCoord);

        if (testRitual == 0)
        {
            return;
        }

        boolean testLevel = Rituals.canCrystalActivate(testRitual, crystalLevel);

        if (!testLevel)
        {
            return;
        }

        if (world.isRemote)
        {
            return;
        }

        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;

        if (currentEssence < Rituals.getCostForActivation(testRitual))
        {
            //TODO Bad stuff
            return;
        }

        if (!world.isRemote)
        {
            data.currentEssence = currentEssence - Rituals.getCostForActivation(testRitual);
            data.markDirty();

            for (int i = 0; i < 12; i++)
            {
                PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(xCoord, yCoord, zCoord, (short) 1));
            }
        }

        cooldown = Rituals.getInitialCooldown(testRitual);
        var1 = 0;
        currentRitual = testRitual;
        isActive = true;
        direction = Rituals.getDirectionOfRitual(world, xCoord, yCoord, zCoord, testRitual);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    @Override
    public void updateEntity()
    {
        if (!isActive)
        {
            return;
        }

        int worldTime = (int) (worldObj.getWorldTime() % 24000);

        if (worldObj.isRemote)
        {
            return;
        }

        if (worldTime % 100 == 0)
        {
            boolean testRunes = Rituals.checkDirectionOfRitualValid(worldObj, xCoord, yCoord, zCoord, currentRitual, direction);
            PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(xCoord, yCoord, zCoord, (short) 1));

            if (!testRunes)
            {
                isActive = false;
                currentRitual = 0;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                //PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(xCoord, yCoord, zCoord, (short)3));
                return;
            }
        }

        if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0)
        {
            return;
        }

        performRitual(worldObj, xCoord, yCoord, zCoord, currentRitual);
    }

    public void performRitual(World world, int x, int y, int z, int ritualID)
    {
        Rituals.performEffect(this, ritualID);
        /*
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork)worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;

        switch (ritualID)
        {
            case 1:
                if (world.isAirBlock(x, y + 1, z))
                {
                    if (currentEssence < Rituals.getCostPerRefresh(ritualID))
                    {
                        EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

                        if (entityOwner == null)
                        {
                            return;
                        }

                        entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                    }
                    else
                    {
                        for (int i = 0; i < 10; i++)
                        {
                            PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(xCoord, yCoord, zCoord, (short)3));
                        }

                        world.setBlock(x, y + 1, z, Block.waterMoving.blockID, 0, 3);
                        data.currentEssence = currentEssence - Rituals.getCostPerRefresh(ritualID);
                        data.markDirty();
                    }
                }

                break;

            case 2:
                if (world.isAirBlock(x, y + 1, z))
                {
                    if (currentEssence < Rituals.getCostPerRefresh(ritualID))
                    {
                        EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

                        if (entityOwner == null)
                        {
                            return;
                        }

                        entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                    }
                    else
                    {
                        for (int i = 0; i < 10; i++)
                        {
                            PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(xCoord, yCoord, zCoord, (short)3));
                        }

                        world.setBlock(x, y + 1, z, Block.lavaMoving.blockID, 0, 3);
                        data.currentEssence = currentEssence - Rituals.getCostPerRefresh(ritualID);
                        data.markDirty();
                    }
                }

                break;

            case 3:
            	if (currentEssence < Rituals.getCostPerRefresh(ritualID))
                {
                    EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

                    if (entityOwner == null)
                    {
                        return;
                    }

                    entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                }else
                {
                	if (world.getWorldTime() % 20 != 0)
                    {
                        return;
                    }
                	boolean flag = false;
                	for(int i=-1; i<=1; i++)
                	{
                		for(int j=-1;j<=1;j++)
                		{
                			int id = world.getBlockId(x+i, y + 2, z+j);
                            Block block = Block.blocksList[id];

                            if (block instanceof IPlantable)
                            {
                                {
                                    PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(xCoord+i, yCoord + 2, zCoord+j, (short)3));
                                    block.updateTick(world, x+i, y + 2, z+j, world.rand);
                                    flag = true;
                                }
                            }
                		}
                	}
                	if(flag)
                	{
                		data.currentEssence = currentEssence - Rituals.getCostPerRefresh(ritualID);
                        data.markDirty();
                	}
                }

                break;

            case 4:
                if (currentEssence < Rituals.getCostPerRefresh(ritualID))
                {
                    EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

                    if (entityOwner == null)
                    {
                        return;
                    }

                    entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                }
                else
                {
                    int d0 = 0;
                    AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand(d0, d0, d0);
                    axisalignedbb.maxY = Math.min((double)this.worldObj.getHeight(), (double)(this.yCoord + 1+d0));
                    List list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
                    Iterator iterator = list.iterator();
                    EntityLivingBase entityplayer;

                    boolean flag = false;

                    while (iterator.hasNext())
                    {
                        entityplayer = (EntityLivingBase)iterator.next();

                        if (!(entityplayer.getEntityName().equals(owner)))
                        {
                            double xDif = entityplayer.posX - xCoord;
                            double yDif = entityplayer.posY - (yCoord + 1);
                            double zDif = entityplayer.posZ - zCoord;
                            entityplayer.motionX=0.1*xDif;
                            entityplayer.motionY=0.1*yDif;
                            entityplayer.motionZ=0.1*zDif;
                            entityplayer.fallDistance = 0;
                            if(!(entityplayer instanceof EntityPlayer))
                            {
                            	flag=true;
                            }

                            //entityplayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                        }
                    }

                    if (worldObj.getWorldTime() % 2 == 0 && flag)
                    {
                        data.currentEssence = currentEssence - Rituals.getCostPerRefresh(ritualID);
                        data.markDirty();
                    }
                }

                break;

            case 5:
                if (currentEssence < Rituals.getCostPerRefresh(ritualID))
                {
                    EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

                    if (entityOwner == null)
                    {
                        return;
                    }

                    entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                }
                else
                {
                    int d0 = 5;
                    AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand(d0, d0, d0);
                    List list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
                    Iterator iterator = list.iterator();
                    EntityLivingBase livingEntity;
                    boolean flag = false;
                    while (iterator.hasNext())
                    {
                        livingEntity = (EntityLivingBase)iterator.next();

                        if (livingEntity instanceof EntityPlayer)
                        {
                            continue;
                        }

                        if (!(livingEntity.getEntityName().equals(owner)))
                        {
                            double xDif = livingEntity.posX - (xCoord + 0.5);
                            double yDif = livingEntity.posY - (yCoord + 3);
                            double zDif = livingEntity.posZ - (zCoord + 0.5);
                            livingEntity.motionX=-0.05*xDif;
                            livingEntity.motionY=-0.05*yDif;
                            livingEntity.motionZ=-0.05*zDif;
                            flag=true;
                            //livingEntity.setVelocity(-0.05 * xDif, -0.05 * yDif, -0.05 * zDif);

                            if (world.rand.nextInt(10) == 0)
                            {
                                PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(livingEntity.posX, livingEntity.posY, livingEntity.posZ, (short)1));
                            }

                            livingEntity.fallDistance = 0;
                            //entityplayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                        }
                    }

                    if (worldObj.getWorldTime() % 2 == 0 && flag)
                    {
                        data.currentEssence = currentEssence - Rituals.getCostPerRefresh(ritualID);
                        data.markDirty();
                    }
                }

                break;

            case 6:
                if (currentEssence < Rituals.getCostPerRefresh(ritualID))
                {
                    EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

                    if (entityOwner == null)
                    {
                        return;
                    }

                    entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                }
                else
                {
                    if (var1 == 0)
                    {
                        int d0 = 0;
                        AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)this.xCoord, (double)this.yCoord + 1, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 2), (double)(this.zCoord + 1)).expand(d0, d0, d0);
                        List list = this.worldObj.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
                        Iterator iterator = list.iterator();
                        EntityItem item;

                        while (iterator.hasNext())
                        {
                            item = (EntityItem)iterator.next();
        //		                double xDif = item.posX - (xCoord+0.5);
        //		                double yDif = item.posY - (yCoord+1);
        //		                double zDif = item.posZ - (zCoord+0.5);
                            ItemStack itemStack = item.getEntityItem();

                            if (itemStack == null)
                            {
                                continue;
                            }

                            if (itemStack.itemID == AlchemicalWizardry.apprenticeBloodOrb.itemID)
                            {
                                var1 = AlchemicalWizardry.energyBlaster.itemID;
                                world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                                cooldown--;
                                item.setDead();
                                return;
                            }
                            else if (itemStack.itemID == Item.swordDiamond.itemID)
                            {
                                var1 = AlchemicalWizardry.energySword.itemID;
                                world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                                cooldown--;
                                item.setDead();
                                return;
                            }
                            else if (itemStack.itemID == Item.pickaxeDiamond.itemID)
                            {
                                var1 = AlchemicalWizardry.boundPickaxe.itemID;
                                world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                                cooldown--;
                                item.setDead();
                                return;
                            }
                            else if (itemStack.itemID == Item.axeDiamond.itemID)
                            {
                                var1 = AlchemicalWizardry.boundAxe.itemID;
                                world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                                cooldown--;
                                item.setDead();
                                return;
                            }
                            else if (itemStack.itemID == Item.shovelDiamond.itemID)
                            {
                                var1 = AlchemicalWizardry.boundShovel.itemID;
                                world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                                cooldown--;
                                item.setDead();
                                return;
                            }

                            if (world.rand.nextInt(10) == 0)
                            {
                                PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(item.posX, item.posY, item.posZ, (short)1));
                            }
                        }

                        data.currentEssence = currentEssence - Rituals.getCostPerRefresh(ritualID);
                        data.markDirty();
                    }
                    else
                    {
                        cooldown--;

                        if (world.rand.nextInt(20) == 0)
                        {
                            int lightningPoint = world.rand.nextInt(8);

                            switch (lightningPoint)
                            {
                                case 0:
                                    world.addWeatherEffect(new EntityLightningBolt(world, x + 4, y + 3, z + 0));
                                    break;

                                case 1:
                                    world.addWeatherEffect(new EntityLightningBolt(world, x - 4, y + 3, z + 0));
                                    break;

                                case 2:
                                    world.addWeatherEffect(new EntityLightningBolt(world, x + 0, y + 3, z + 4));
                                    break;

                                case 3:
                                    world.addWeatherEffect(new EntityLightningBolt(world, x + 0, y + 3, z - 4));
                                    break;

                                case 4:
                                    world.addWeatherEffect(new EntityLightningBolt(world, x + 3, y + 3, z + 3));
                                    break;

                                case 5:
                                    world.addWeatherEffect(new EntityLightningBolt(world, x - 3, y + 3, z + 3));
                                    break;

                                case 6:
                                    world.addWeatherEffect(new EntityLightningBolt(world, x + 3, y + 3, z - 3));
                                    break;

                                case 7:
                                    world.addWeatherEffect(new EntityLightningBolt(world, x - 3, y + 3, z - 3));
                                    break;
                            }
                        }

                        if (cooldown <= 0)
                        {
                            EntityItem newItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1, zCoord + 0.5, new ItemStack(var1, 1, 0));
                            worldObj.spawnEntityInWorld(newItem);
                            isActive = false;
                        }
                    }
                }

                break;

            case 7:
            	if (currentEssence < Rituals.getCostPerRefresh(ritualID))
                {
                    EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

                    if (entityOwner == null)
                    {
                        return;
                    }

                    entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                }
                else
                {
                    int d0 = 0;
                    AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)this.xCoord, (double)this.yCoord + 1, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 2), (double)(this.zCoord + 1)).expand(d0, d0, d0);
                    List list = this.worldObj.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
                    Iterator iterator = list.iterator();
                    EntityItem item;

                    while (iterator.hasNext())
                    {
                        item = (EntityItem)iterator.next();
        //		                double xDif = item.posX - (xCoord+0.5);
        //		                double yDif = item.posY - (yCoord+1);
        //		                double zDif = item.posZ - (zCoord+0.5);
                        ItemStack itemStack = item.getEntityItem();

                        if (itemStack == null)
                        {
                            continue;
                        }

                        if (itemStack.itemID == AlchemicalWizardry.boundHelmet.itemID)
                        {
                            var1 = 5;
                        }
                        else if (itemStack.itemID == AlchemicalWizardry.boundPlate.itemID)
                        {
                            var1 = 8;
                        }else if (itemStack.itemID == AlchemicalWizardry.boundLeggings.itemID)
                        {
                            var1 = 7;
                        }else if (itemStack.itemID == AlchemicalWizardry.boundBoots.itemID)
                        {
                            var1 = 4;
                        }else if(itemStack.itemID == AlchemicalWizardry.sigilOfHolding.itemID)
                        {
                        	var1 = -1;
                        }

                        if(var1>0)
                        {
                        	item.setDead();

                        	world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z - 5));
                            world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z + 5));
                            world.addWeatherEffect(new EntityLightningBolt(world, x - 5, y + 1, z));
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 5, y + 1, z));

                        	NBTTagCompound itemTag = itemStack.stackTagCompound;

                        	ItemStack[] inv = ((BoundArmour)itemStack.getItem()).getInternalInventory(itemStack);
                        	if(inv!=null)
                        	{
                        		for(ItemStack internalItem : inv)
                        		{
                        			if(internalItem!=null)
                        			{
                        				EntityItem newItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1, zCoord + 0.5, internalItem.copy());
                                        worldObj.spawnEntityInWorld(newItem);
                        			}
                        		}
                        	}
                        	EntityItem newItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1, zCoord + 0.5, new ItemStack(Block.blocksList[AlchemicalWizardry.bloodSocket.blockID],var1));
                            worldObj.spawnEntityInWorld(newItem);

                            isActive=false;

                        	break;
                        }else if(var1 ==-1)
                        {
                        	item.setDead();

                        	world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z - 5));
                            world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z + 5));
                            world.addWeatherEffect(new EntityLightningBolt(world, x - 5, y + 1, z));
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 5, y + 1, z));

                        	NBTTagCompound itemTag = itemStack.stackTagCompound;

                        	ItemStack[] inv = ((SigilOfHolding)itemStack.getItem()).getInternalInventory(itemStack);
                        	if(inv!=null)
                        	{
                        		for(ItemStack internalItem : inv)
                        		{
                        			if(internalItem!=null)
                        			{
                        				EntityItem newItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1, zCoord + 0.5, internalItem.copy());
                                        worldObj.spawnEntityInWorld(newItem);
                        			}
                        		}
                        	}
                        	EntityItem newItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1, zCoord + 0.5, new ItemStack(AlchemicalWizardry.sigilOfHolding.itemID,1,0));
                            worldObj.spawnEntityInWorld(newItem);

                            isActive=false;

                        	break;
                        }

                        if (world.rand.nextInt(10) == 0)
                        {
                            PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(item.posX, item.posY, item.posZ, (short)1));
                        }
                    }

                    data.currentEssence = currentEssence - Rituals.getCostPerRefresh(ritualID);
                    data.markDirty();
                }

                break;

            case 8:
            	if (currentEssence < Rituals.getCostPerRefresh(ritualID))
                {
                    EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

                    if (entityOwner == null)
                    {
                        return;
                    }

                    entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                }
                else
                {
                    int d0 = 0;
                    AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)this.xCoord, (double)this.yCoord+1, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 2), (double)(this.zCoord + 1)).expand(d0, d0, d0);
                    axisalignedbb.maxY = Math.min((double)this.worldObj.getHeight(), (double)(this.yCoord + 2+d0));
                    List list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
                    Iterator iterator = list.iterator();
                    EntityLivingBase entityplayer;

                    boolean flag = false;

                    while (iterator.hasNext())
                    {
                        entityplayer = (EntityLivingBase)iterator.next();

                        if(entityplayer instanceof EntityPlayer)
                        {
                            PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(entityplayer.motionX, 1, entityplayer.motionZ), (Player)entityplayer);
                            entityplayer.motionY=1;
                            entityplayer.fallDistance = 0;
                            flag=true;
                        }else
                        //if (!(entityplayer.getEntityName().equals(owner)))
                        {
        //                            double xDif = entityplayer.posX - xCoord;
        //                            double yDif = entityplayer.posY - (yCoord + 1);
        //                            double zDif = entityplayer.posZ - zCoord;
                            //entityplayer.motionX=0.1*xDif;
                            entityplayer.motionY=1;
                            //entityplayer.motionZ=0.1*zDif;
                            entityplayer.fallDistance = 0;
                            flag=true;
                            //entityplayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                        }
                    }

                    if (worldObj.getWorldTime() % 2 == 0 && flag)
                    {
                        data.currentEssence = currentEssence - Rituals.getCostPerRefresh(ritualID);
                        data.markDirty();
                    }
                }

                break;

            default:
                return;
        }
        */
    }

    public String getOwner()
    {
        return owner;
    }

    public void setCooldown(int newCooldown)
    {
        this.cooldown = newCooldown;
    }

    public int getCooldown()
    {
        return this.cooldown;
    }

    public void setVar1(int newVar1)
    {
        this.var1 = newVar1;
    }

    public int getVar1()
    {
        return this.var1;
    }

    public void setActive(boolean active)
    {
        this.isActive = active;
    }

    public int getDirection()
    {
        return this.direction;
    }
}