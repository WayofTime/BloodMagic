package WayofTime.alchemicalWizardry.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import WayofTime.alchemicalWizardry.api.event.TeleposeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import vazkii.botania.api.internal.IManaBurst;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.BloodMagicConfiguration;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.items.armour.BoundArmour;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;
import WayofTime.alchemicalWizardry.common.omega.OmegaParadigm;
import WayofTime.alchemicalWizardry.common.omega.OmegaRegistry;
import WayofTime.alchemicalWizardry.common.omega.ReagentRegenConfiguration;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class AlchemicalWizardryEventHooks
{
	public static Map<String, Boolean> playerFlightBuff = new HashMap();
	public static Map<String, Boolean> playerBoostStepHeight = new HashMap();
	public static List<String> playersWith1Step = new ArrayList();

	public static Map<Integer, List<CoordAndRange>> respawnMap = new HashMap();
	public static Map<Integer, List<CoordAndRange>> forceSpawnMap = new HashMap();


	@SubscribeEvent
	public void onAnvilUpdateEvent(AnvilUpdateEvent event)
	{
		if(event.isCancelable() && event.left != null && event.right != null && (event.left.getItem() instanceof BoundArmour || event.right.getItem() instanceof BoundArmour))
		{
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onLivingHurtEvent(LivingHurtEvent event)
	{
		if(!event.isCanceled() && event.entityLiving instanceof EntityPlayer && !event.entityLiving.worldObj.isRemote)
		{
			EntityPlayer player = (EntityPlayer)event.entityLiving;

			float prevHp = APISpellHelper.getCurrentAdditionalHP((EntityPlayer)event.entityLiving);
			if(prevHp > 0)
			{
				float recalculatedAmount = ArmorProperties.ApplyArmor(player, player.inventory.armorInventory, event.source, event.ammount);
				if (recalculatedAmount <= 0) return;
				
				float ratio = recalculatedAmount / event.ammount;
				
	            float f1 = recalculatedAmount;
	            recalculatedAmount = Math.max(recalculatedAmount - player.getAbsorptionAmount(), 0.0F);
	            player.setAbsorptionAmount(player.getAbsorptionAmount() - (f1 - recalculatedAmount));
	            
	            if(prevHp > recalculatedAmount)
	            {
		            float hp = (prevHp - recalculatedAmount);
		            
		            if(hp > 0)
					{
						event.ammount = 0;
					}else
					{
						event.ammount += hp / ratio;
						Reagent reagent = APISpellHelper.getPlayerReagentType(player);
						OmegaParadigm paradigm = OmegaRegistry.getParadigmForReagent(reagent);
						if(paradigm != null)
						{
							ItemStack chestStack = player.inventory.armorInventory[2];

							if(chestStack != null && chestStack.getItem() instanceof OmegaArmour)
							{
								if(((OmegaArmour)chestStack.getItem()).paradigm == paradigm)
								{
									paradigm.onHPBarDepleted(player, chestStack);
								}
							}
						}
					}
		            
		            APISpellHelper.setCurrentAdditionalHP((EntityPlayer)event.entityLiving, Math.max(0, hp));

					System.out.println("HP: " + hp);
					APISpellHelper.setCurrentAdditionalHP(player, Math.max(0, hp));
					NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getAddedHPPacket(Math.max(0, hp), APISpellHelper.getCurrentAdditionalMaxHP(player)), (EntityPlayerMP)player);
					
	            }				
			}
		}
	}
	
	@SubscribeEvent
	public void omegaUpdateReagentAndHpEvent(LivingUpdateEvent event)
	{
		if(event.entityLiving instanceof EntityPlayer && !event.entityLiving.worldObj.isRemote)
		{			
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			Reagent reagent = APISpellHelper.getPlayerReagentType(player);
			float reagentAmount = APISpellHelper.getPlayerCurrentReagentAmount(player);
			
			boolean hasReagentChanged = false;
			
			if(reagentAmount > 0 && OmegaRegistry.hasParadigm(reagent))
			{
				OmegaParadigm parad = OmegaRegistry.getParadigmForReagent(reagent);
				ReagentRegenConfiguration config = parad.getRegenConfig(player);
				
				if(parad.isPlayerWearingFullSet(player))
				{
					if(event.entityLiving.worldObj.getWorldTime() % config.tickRate == 0)
					{
						boolean hasHealthChanged = false;
						int maxHealth = parad.getMaxAdditionalHealth();
						
						float health = APISpellHelper.getCurrentAdditionalHP(player);
						
						if(health > maxHealth)
						{
							health = maxHealth;
							hasHealthChanged = true;
						}else if(health < maxHealth)
						{
							float addedAmount = Math.min(Math.min((reagentAmount / config.costPerPoint), config.healPerTick), maxHealth - health);
							float drain = addedAmount * config.costPerPoint;
							
							reagentAmount -= drain;
							hasReagentChanged = true;
							
							health += addedAmount;
							
							hasHealthChanged = true;
						}
						
						if(player instanceof EntityPlayerMP)
						{
							if(hasHealthChanged)
							{
								APISpellHelper.setCurrentAdditionalHP(player, health);
								NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getAddedHPPacket(health, maxHealth), (EntityPlayerMP)player);
							}
						}
					}
				}
				//Consumes the amount
				float costPerTick = parad.getCostPerTickOfUse(player);
				if(parad.doDrainReagent(player))
				{
					if(reagentAmount > costPerTick)
					{
						hasReagentChanged = true;
						reagentAmount = Math.max(0, reagentAmount - costPerTick);
					}else
					{
						hasReagentChanged = true;
						reagentAmount = 0;
					}
				}
				
				
				hasReagentChanged = true;
			}
	
			if(reagentAmount <= 0)
			{
				ItemStack[] armourInventory = player.inventory.armorInventory;
				for(ItemStack stack : armourInventory)
				{
					if(stack != null && stack.getItem() instanceof OmegaArmour)
					{
						((OmegaArmour)stack.getItem()).revertArmour(player, stack);
					}
				}
			}
			
			if(player instanceof EntityPlayerMP)
			{
				if(hasReagentChanged)
				{
					APISpellHelper.setPlayerCurrentReagentAmount(player, reagentAmount);
					NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getReagentBarPacket(reagent, reagentAmount, APISpellHelper.getPlayerMaxReagentAmount(player)), (EntityPlayerMP)player);
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onPlayerDamageEvent(LivingAttackEvent event)
	{
		if (event.source.isProjectile())
		{
			if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionProjProt) && event.isCancelable())
			{
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onLivingSpawnEvent(CheckSpawn event)
	{
		if (!(event.entityLiving instanceof EntityMob))
		{
			return;
		}

		String respawnRitual = "AW028SpawnWard";

		Integer dimension = new Integer(event.world.provider.dimensionId);
		if (respawnMap.containsKey(dimension))
		{
			List<CoordAndRange> list = respawnMap.get(dimension);

			if (list != null)
			{
				for (CoordAndRange coords : list)
				{
					TileEntity tile = event.world.getTileEntity(coords.xCoord, coords.yCoord, coords.zCoord);

					if (tile instanceof TEMasterStone && ((TEMasterStone) tile).isRunning && ((TEMasterStone) tile).getCurrentRitual().equals(respawnRitual))
					{
						if (event.x > coords.xCoord - coords.horizRadius && event.x < coords.xCoord + coords.horizRadius && event.z > coords.zCoord - coords.horizRadius && event.z < coords.zCoord + coords.horizRadius && event.y > coords.yCoord - coords.vertRadius && event.y < coords.yCoord + coords.vertRadius)
						{
							switch (event.getResult())
							{
								case ALLOW:
									event.setResult(Result.DEFAULT);
									break;
								case DEFAULT:
									event.setResult(Result.DENY);
									break;
								case DENY:
									break;
								default:
									break;
							}
							break;
						}
					} else
					{
						list.remove(coords);
					}
				}
			}
		}

		if (event.entityLiving instanceof EntityCreeper)
		{
			GameRegistry d;
			return;
		}

		String forceSpawnRitual = "AW029VeilOfEvil";

		if (forceSpawnMap.containsKey(dimension))
		{
			List<CoordAndRange> list = forceSpawnMap.get(dimension);

			if (list != null)
			{
				for (CoordAndRange coords : list)
				{
					TileEntity tile = event.world.getTileEntity(coords.xCoord, coords.yCoord, coords.zCoord);

					if (tile instanceof TEMasterStone && ((TEMasterStone) tile).isRunning && ((TEMasterStone) tile).getCurrentRitual().equals(forceSpawnRitual))
					{
						if (event.x > coords.xCoord - coords.horizRadius && event.x < coords.xCoord + coords.horizRadius && event.z > coords.zCoord - coords.horizRadius && event.z < coords.zCoord + coords.horizRadius && event.y > coords.yCoord - coords.vertRadius && event.y < coords.yCoord + coords.vertRadius)
						{
							switch (event.getResult())
							{
								case ALLOW:
									break;
								case DEFAULT:
									event.setResult(Result.ALLOW);
									break;
								case DENY:
									event.setResult(Result.DEFAULT);
									break;
								default:
									break;
							}
							break;
						}
					} else
					{
						list.remove(coords);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerRespawnEvent(PlayerRespawnEvent event)
	{
		if (AlchemicalWizardry.respawnWithDebuff)
		{
			event.player.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionSoulFray.id, 20 * 60 * 5, 0));
		}
	}

	@SubscribeEvent
	public void onLivingJumpEvent(LivingJumpEvent event)
	{
//		event.entityLiving.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(new AttributeModifier(new UUID(493295, 1), "HealthModifier", 2, 0));
		//event.entityLiving.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier(new UUID(493295, 1), "HealthModifier", 2, 0));

		if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionBoost))
		{
			int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionBoost).getAmplifier();
			event.entityLiving.motionY += (0.1f) * (2 + i);
		}

		if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionHeavyHeart))
		{
			event.entityLiving.motionY = 0;
		}
	}

	@SubscribeEvent
	public void onEndermanTeleportEvent(EnderTeleportEvent event)
	{
		if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionPlanarBinding) && event.isCancelable())
		{
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onEntityDamaged(LivingAttackEvent event)
	{
		EntityLivingBase entityAttacked = event.entityLiving;

		if (entityAttacked.isPotionActive(AlchemicalWizardry.customPotionReciprocation))
		{
			Entity entityAttacking = event.source.getSourceOfDamage();

			if (entityAttacking != null && entityAttacking instanceof EntityLivingBase)
			{
				int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionReciprocation).getAmplifier();
				float damageRecieve = event.ammount / 2 * (i + 1);
				((EntityLivingBase) entityAttacking).attackEntityFrom(DamageSource.generic, damageRecieve);
			}
		}

		if (entityAttacked.isPotionActive(AlchemicalWizardry.customPotionFlameCloak))
		{
			int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFlameCloak).getAmplifier();

			Entity entityAttacking = event.source.getSourceOfDamage();

			if (entityAttacking != null && entityAttacking instanceof EntityLivingBase && !entityAttacking.isImmuneToFire() && !((EntityLivingBase) entityAttacking).isPotionActive(Potion.fireResistance))
			{
				entityAttacking.attackEntityFrom(DamageSource.inFire, 2 * i + 2);
				entityAttacking.setFire(3);
			}
		}
	}

		
//	@SubscribeEvent
//	public void onFOVUpdate(FOVUpdateEvent event)
//	{
//		event.setResult(Result.DENY);
//	}
	//    @SubscribeEvent
	//    public void onPlayerTickEnd(PlayerTickEvent event)
	//    {
	//    	if(event.type.equals(Type.PLAYER) && event.phase.equals(TickEvent.Phase.END))
	//    	{
	//    		ObfuscationReflectionHelper.setPrivateValue(PlayerCapabilities.class, event.player.capabilities, Float.valueOf(0.1f), new String[]{"walkSpeed", "g", "field_75097_g"});
	//    	}
	//    }

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event)
	{
		EntityLivingBase entityLiving = event.entityLiving;
		double x = entityLiving.posX;
		double y = entityLiving.posY;
		double z = entityLiving.posZ;

		Vec3 blockVector = SpellHelper.getEntityBlockVector(entityLiving);
		int xPos = (int) (blockVector.xCoord);
		int yPos = (int) (blockVector.yCoord);
		int zPos = (int) (blockVector.zCoord);

		if (entityLiving instanceof EntityPlayer)
		{
			if(!entityLiving.worldObj.isRemote && entityLiving.worldObj.getTotalWorldTime() % 20 == 0)
			{				
				if(entityLiving instanceof EntityPlayerMP)
				{
					String ownerName = SoulNetworkHandler.getUsername((EntityPlayer)entityLiving);
					NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getLPPacket(SoulNetworkHandler.getCurrentEssence(ownerName), SoulNetworkHandler.getMaximumForOrbTier(SoulNetworkHandler.getCurrentMaxOrb(ownerName))), (EntityPlayerMP)entityLiving);
				}
			}
			ObfuscationReflectionHelper.setPrivateValue(PlayerCapabilities.class, ((EntityPlayer) event.entityLiving).capabilities, Float.valueOf(0.1f), new String[]{"walkSpeed", "g", "field_75097_g"});
		}

		if (entityLiving instanceof EntityPlayer && entityLiving.worldObj.isRemote)
		{
			EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
			boolean highStepListed = playersWith1Step.contains(entityPlayer.getDisplayName());
			boolean hasHighStep = entityPlayer.isPotionActive(AlchemicalWizardry.customPotionBoost);

			if (hasHighStep && !highStepListed)
			{
				playersWith1Step.add(SpellHelper.getUsername(entityPlayer));
			}

			if (!hasHighStep && highStepListed)
			{
				playersWith1Step.remove(SpellHelper.getUsername(entityPlayer));
				entityPlayer.stepHeight = 0.5F;
			}
		}

		if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionFeatherFall))
		{
			event.entityLiving.fallDistance = 0;
		}

		if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionDrowning) && ! event.entityLiving.isPotionActive(Potion.waterBreathing))
		{
			int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionDrowning).getAmplifier();

			if (event.entityLiving.worldObj.getWorldTime() % ((int) (20 / (i + 1))) == 0)
			{
				event.entityLiving.attackEntityFrom(DamageSource.drown, 2);
				event.entityLiving.hurtResistantTime = Math.min(event.entityLiving.hurtResistantTime, 20 / (i + 1));
			}
		}

//		event.entityLiving.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeAllModifiers();
		
		if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionBoost))
		{
			int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionBoost).getAmplifier();
			EntityLivingBase entity = event.entityLiving;
			//if(!entity.isSneaking())
			{
				float percentIncrease = (i + 1) * 0.05f;
				
//				AttributeModifier speedModifier = new AttributeModifier(new UUID(213241, 3), "Potion Boost", percentIncrease, 0);
//				
//				event.entityLiving.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(speedModifier);
				
				if (event.entityLiving instanceof EntityPlayer)
				{
					EntityPlayer entityPlayer = (EntityPlayer) event.entityLiving;
					entityPlayer.stepHeight = 1.0f;

					if ((entityPlayer.onGround || entityPlayer.capabilities.isFlying) && entityPlayer.moveForward > 0F)
						entityPlayer.moveFlying(0F, 1F, entityPlayer.capabilities.isFlying ? (percentIncrease / 2.0f) : percentIncrease);
				}
			}
		}

		if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionProjProt))
		{
			int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionProjProt).getAmplifier();
			EntityLivingBase entity = event.entityLiving;
			int posX = (int) Math.round(entity.posX - 0.5f);
			int posY = (int) Math.round(entity.posY);
			int posZ = (int) Math.round(entity.posZ - 0.5f);
			int d0 = (int) ((i + 1) * 2.5);
			AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(posX - 0.5, posY - 0.5, posZ - 0.5, posX + 0.5, posY + 0.5, posZ + 0.5).expand(d0, d0, d0);
			List list = event.entityLiving.worldObj.getEntitiesWithinAABB(Entity.class, axisalignedbb);
			Iterator iterator = list.iterator();
			EntityLivingBase livingEntity;

			while (iterator.hasNext())
			{
				Entity projectile = (Entity) iterator.next();

				if (projectile == null)
				{
					continue;
				}

				if (!(projectile instanceof IProjectile) || (AlchemicalWizardry.isBotaniaLoaded && isManaBurst(projectile)))
				{
					continue;
				}

				Entity throwingEntity = null;

				if (projectile instanceof EntityArrow)
				{
					throwingEntity = ((EntityArrow) projectile).shootingEntity;
				} else if (projectile instanceof EnergyBlastProjectile)
				{
					throwingEntity = ((EnergyBlastProjectile) projectile).shootingEntity;
				} else if (projectile instanceof EntityThrowable)
				{
					throwingEntity = ((EntityThrowable) projectile).getThrower();
				}

				if (throwingEntity != null && throwingEntity.equals(entity))
				{
					continue;
				}

				double delX = projectile.posX - entity.posX;
				double delY = projectile.posY - entity.posY;
				double delZ = projectile.posZ - entity.posZ;

				if (throwingEntity != null)
				{
					delX = -projectile.posX + throwingEntity.posX;
					delY = -projectile.posY + (throwingEntity.posY + throwingEntity.getEyeHeight());
					delZ = -projectile.posZ + throwingEntity.posZ;
				}

				double curVel = Math.sqrt(delX * delX + delY * delY + delZ * delZ);

				delX /= curVel;
				delY /= curVel;
				delZ /= curVel;
				double newVel = Math.sqrt(projectile.motionX * projectile.motionX + projectile.motionY * projectile.motionY + projectile.motionZ * projectile.motionZ);
				projectile.motionX = newVel * delX;
				projectile.motionY = newVel * delY;
				projectile.motionZ = newVel * delZ;
				//TODO make this not affect player's projectiles
			}
		}

		if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionFlight))
		{
			if (event.entityLiving instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) event.entityLiving;
				String ownerName = SpellHelper.getUsername(entityPlayer);
				playerFlightBuff.put(ownerName, true);
				entityPlayer.capabilities.allowFlying = true;
			}
		} else
		{
			if (event.entityLiving instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) event.entityLiving;
				String ownerName = SpellHelper.getUsername(entityPlayer);

				if (!playerFlightBuff.containsKey(ownerName))
				{
					playerFlightBuff.put(ownerName, false);
				}

				if (playerFlightBuff.get(ownerName))
				{
					playerFlightBuff.put(ownerName, false);

					if (!entityPlayer.capabilities.isCreativeMode)
					{
						entityPlayer.capabilities.allowFlying = false;
						entityPlayer.capabilities.isFlying = false;
						entityPlayer.sendPlayerAbilities();
					}
				}
			}
		}

		if (entityLiving.isPotionActive(AlchemicalWizardry.customPotionFlameCloak))
		{
			entityLiving.worldObj.spawnParticle("flame", x + SpellHelper.gaussian(1), y - 1.3 + SpellHelper.gaussian(0.3), z + SpellHelper.gaussian(1), 0, 0.06d, 0);

			int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFlameCloak).getAmplifier();
			double range = i * 0.5;

			List<Entity> entities = SpellHelper.getEntitiesInRange(entityLiving.worldObj, x, y, z, range, range);
			if (entities != null)
			{
				for (Entity entity : entities)
				{
					if (!entity.equals(entityLiving) && !entity.isImmuneToFire() && !(entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(Potion.fireResistance)))
					{
						entity.setFire(3);
					}
				}
			}
		}

		if (entityLiving.isPotionActive(AlchemicalWizardry.customPotionIceCloak))
		{
			if (entityLiving.worldObj.getWorldTime() % 2 == 0)
				entityLiving.worldObj.spawnParticle("reddust", x + SpellHelper.gaussian(1), y - 1.3 + SpellHelper.gaussian(0.3), z + SpellHelper.gaussian(1), 0x74, 0xbb, 0xfb);

			int r = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionIceCloak).getAmplifier();
			int horizRange = r + 1;
			int vertRange = 1;

			if (!entityLiving.worldObj.isRemote)
			{
				for (int i = -horizRange; i <= horizRange; i++)
				{
					for (int k = -horizRange; k <= horizRange; k++)
					{
						for (int j = -vertRange - 1; j <= vertRange - 1; j++)
						{
							SpellHelper.freezeWaterBlock(entityLiving.worldObj, xPos + i, yPos + j, zPos + k);
						}
					}
				}
			}
		}

		if (entityLiving.isPotionActive(AlchemicalWizardry.customPotionHeavyHeart))
		{
			entityLiving.worldObj.spawnParticle("flame", x + SpellHelper.gaussian(1), y - 1.3 + SpellHelper.gaussian(0.3), z + SpellHelper.gaussian(1), 0, 0.06d, 0);

			int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionHeavyHeart).getAmplifier();
			double decrease = 0.025 * (i + 1);

			if (entityLiving.motionY > -0.9)
			{
				entityLiving.motionY -= decrease;
			}

			if(entityLiving instanceof EntityPlayer)
			{
				SpellHelper.setPlayerSpeedFromServer((EntityPlayer)entityLiving, entityLiving.motionX, entityLiving.motionY - decrease, entityLiving.motionZ);
			}
		}

		if (entityLiving.isPotionActive(AlchemicalWizardry.customPotionFireFuse))
		{
			entityLiving.worldObj.spawnParticle("flame", x + SpellHelper.gaussian(1), y - 1.3 + SpellHelper.gaussian(0.3), z + SpellHelper.gaussian(1), 0, 0.06d, 0);

			int r = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFireFuse).getAmplifier();
			int radius = r + 1;

			if (entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFireFuse).getDuration() <= 2)
			{
				entityLiving.worldObj.createExplosion(null, x, y, z, radius, false);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onTelepose(TeleposeEvent event) {
		for (int i = 0; i < AlchemicalWizardry.teleposerBlacklist.length; i++) {
			String[] blockData = AlchemicalWizardry.teleposerBlacklist[i].split(":");

			if (blockData.length == 3) {

				Block block = GameRegistry.findBlock(blockData[0], blockData[1]);
				int meta;

				// Check if it's an int, if so, parse it. If not, set to 0 to avoid crashing.
				if (blockData[2].matches("-?\\d+"))
					meta = Integer.parseInt(blockData[2]);
				else
					meta = 0;

				if (block != null)
					if ((event.initialBlock == block || event.finalBlock == block) && (event.initialMetadata == meta || event.finalMetadata == meta))
						event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals("AWWayofTime")) {
			BloodMagicConfiguration.syncConfig();
			AlchemicalWizardry.logger.info("Refreshing configuration file.");
		}
	}

	@Optional.Method(modid = "Botania")
	private boolean isManaBurst(Entity projectile)
	{
		return projectile instanceof IManaBurst;
	}
}
