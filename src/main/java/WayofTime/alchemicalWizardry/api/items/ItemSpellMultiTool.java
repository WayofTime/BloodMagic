package WayofTime.alchemicalWizardry.api.items;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.api.spell.SpellEffect;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;

public class ItemSpellMultiTool extends Item
{
    private static final String harvestLevelSuffix = "harvestLvl";
    private static final String digLevelSuffix = "digLvl";
    private static final String tagName = "BloodMagicTool";
    private Random rand = new Random();

    public ItemSpellMultiTool()
    {
        super();
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setFull3D();
    }

    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundTool");
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        float damage = this.getCustomItemAttack(par1ItemStack);

        SpellParadigmTool parad = this.loadParadigmFromStack(par1ItemStack);

        if (parad != null)
        {
            parad.onLeftClickEntity(par1ItemStack, par2EntityLivingBase, par3EntityLivingBase);
        }

        damage += parad.getAddedDamageForEntity(par2EntityLivingBase);

        if (rand.nextFloat() < this.getCritChance(par1ItemStack))
        {
            damage *= 1.75f;
        }

        if (par3EntityLivingBase instanceof EntityPlayer)
        {
            par2EntityLivingBase.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) par3EntityLivingBase), damage);
        } else
        {
            par2EntityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(par3EntityLivingBase), damage);
        }

        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        SpellParadigmTool parad = this.loadParadigmFromStack(stack);

        if (parad != null && entity instanceof EntityLivingBase)
        {
            parad.onLeftClickEntity(stack, (EntityLivingBase) entity, player);
        }

        return false;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player)
    {
        if (player.worldObj.isRemote)
        {
            return false;
        }

        if (!stack.hasTagCompound())
            return false;

        World world = player.worldObj;
        Block block = player.worldObj.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (block == null || block == Blocks.air)
            return false;
        int hlvl = -1;
        float blockHardness = block.getBlockHardness(world, x, y, z);

        MovingObjectPosition mop = APISpellHelper.raytraceFromEntity(world, player, true, 5.0D);

        Block localBlock = world.getBlock(x, y, z);
        int localMeta = world.getBlockMetadata(x, y, z);
        String toolClass = block.getHarvestTool(meta);
        if (toolClass != null && this.getHarvestLevel(stack, toolClass) != -1)
            hlvl = block.getHarvestLevel(meta);
        int toolLevel = this.getHarvestLevel(stack, toolClass);

        float localHardness = localBlock == null ? Float.MAX_VALUE : localBlock.getBlockHardness(world, x, y, z);

        if (hlvl <= toolLevel && localHardness - 1.5 <= blockHardness)
        {
            boolean cancelHarvest = false;

            if (!cancelHarvest)
            {
                if (localBlock != null && !(localHardness < 0))
                {
                    boolean isEffective = false;

                    String localToolClass = this.getToolClassForMaterial(localBlock.getMaterial());

                    if (localToolClass != null && this.getHarvestLevel(stack, toolClass) >= localBlock.getHarvestLevel(localMeta))
                    {
                        isEffective = true;
                    }


                    if (localBlock.getMaterial().isToolNotRequired())
                    {
                        isEffective = true;
                    }

                    if (!player.capabilities.isCreativeMode)
                    {
                        if (isEffective)
                        {
                            if (localBlock.removedByPlayer(world, player, x, y, z))
                            {
                                localBlock.onBlockDestroyedByPlayer(world, x, y, z, localMeta);
                            }
                            localBlock.onBlockHarvested(world, x, y, z, localMeta, player);
                            if (blockHardness > 0f)
                                onBlockDestroyed(stack, world, localBlock, x, y, z, player);

                            List<ItemStack> items = APISpellHelper.getItemsFromBlock(world, localBlock, x, y, z, localMeta, this.getSilkTouch(stack), this.getFortuneLevel(stack));

                            SpellParadigmTool parad = this.loadParadigmFromStack(stack);
                            List<ItemStack> newItems = parad.handleItemList(stack, items);

                            if (!world.isRemote)
                            {
                                APISpellHelper.spawnItemListInWorld(newItems, world, x + 0.5f, y + 0.5f, z + 0.5f);
                            }

                            world.func_147479_m(x, y, z);

                            int cost = 0;

                            cost += parad.digSurroundingArea(stack, world, player, mop, localToolClass, localHardness, toolLevel, this);

                            cost += parad.onBreakBlock(stack, world, player, localBlock, localMeta, x, y, z, ForgeDirection.getOrientation(mop.sideHit));

                            if (cost > 0)
                            {
                            	SoulNetworkHandler.syphonAndDamageFromNetwork(stack, player, cost);
                            }
                        } else
                        {
                            world.setBlockToAir(x, y, z);
                            world.func_147479_m(x, y, z);
                        }

                    } else
                    {
                        world.setBlockToAir(x, y, z);
                        world.func_147479_m(x, y, z);
                    }
                }
            }
        }

        if (!world.isRemote)
            world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
        return true;

    }

    public Material[] getMaterialsForToolclass(String toolClass)
    {
        if ("pickaxe".equals(toolClass))
        {
            return new Material[]{Material.rock, Material.iron, Material.ice, Material.glass, Material.piston, Material.anvil, Material.circuits};
        } else if ("shovel".equals(toolClass))
        {
            return new Material[]{Material.grass, Material.ground, Material.sand, Material.snow, Material.craftedSnow, Material.clay};
        } else if ("axe".equals(toolClass))
        {
            return new Material[]{Material.wood, Material.vine, Material.circuits, Material.cactus};
        }
        return new Material[0];
    }

    public String getToolClassForMaterial(Material mat)
    {
        String testString = "pickaxe";

        Material[] matList = this.getMaterialsForToolclass(testString);
        for (int i = 0; i < matList.length; i++)
        {
            if (matList[i] == mat)
            {
                return testString;
            }
        }

        testString = "shovel";
        matList = this.getMaterialsForToolclass(testString);
        for (int i = 0; i < matList.length; i++)
        {
            if (matList[i] == mat)
            {
                return testString;
            }
        }

        testString = "axe";
        matList = this.getMaterialsForToolclass(testString);
        for (int i = 0; i < matList.length; i++)
        {
            if (matList[i] == mat)
            {
                return testString;
            }
        }

        return null;
    }

    public Set<String> getToolClasses(ItemStack stack)
    {
        Set<String> set = new HashSet();

        if (this.getHarvestLevel(stack, "pickaxe") > -1)
        {
            set.add("pickaxe");
        }

        if (this.getHarvestLevel(stack, "axe") > -1)
        {
            set.add("axe");
        }

        if (this.getHarvestLevel(stack, "shovel") > -1)
        {
            set.add("shovel");
        }

        return set;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta)
    {
        String toolClass = block.getHarvestTool(meta);

        if (toolClass == null || toolClass.equals(""))
        {
            return 1.0f;
        }
        {
            if (stack.hasTagCompound())
            {
                NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

                return tag.getFloat(digLevelSuffix + toolClass);
            } else
            {
                stack.setTagCompound(new NBTTagCompound());
            }
        }

        return 1.0f;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            if (tag.hasKey(harvestLevelSuffix + toolClass))
            {
                return tag.getInteger(harvestLevelSuffix + toolClass);
            } else
            {
                return -1;
            }
        } else
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        return -1;
    }

    @Override
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack)
    {

        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return false;
    }

    @Override
    public void onUpdate(ItemStack toolStack, World world, Entity par3Entity, int par4, boolean par5)
    {
        if (world.isRemote)
        {
            return;
        }

        SpellParadigmTool parad = this.loadParadigmFromStack(toolStack);
        int cost = parad.onUpdate(toolStack, world, par3Entity, par4, par5);

        if (par3Entity instanceof EntityPlayer && cost > 0)
            SoulNetworkHandler.syphonAndDamageFromNetwork(toolStack, (EntityPlayer) par3Entity, cost);

        int duration = Math.max(this.getDuration(toolStack, world), 0);

        if (duration <= 0 && par3Entity instanceof EntityPlayer)
        {
            int banishCost = parad.onBanishTool(toolStack, world, par3Entity, par4, par5);
            SoulNetworkHandler.syphonAndDamageFromNetwork(toolStack, (EntityPlayer) par3Entity, banishCost);
            ((EntityPlayer) par3Entity).inventory.mainInventory[par4] = this.getContainedCrystal(toolStack);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par3EntityPlayer.isSneaking())
        {
            par3EntityPlayer.setCurrentItemOrArmor(0, this.getContainedCrystal(par1ItemStack));
            return par1ItemStack;
        }

        SpellParadigmTool parad = this.loadParadigmFromStack(par1ItemStack);

        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);

        int cost = 0;

        if (mop != null && mop.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK))
        {
            cost = parad.onRightClickBlock(par1ItemStack, par3EntityPlayer, par2World, mop);
        } else
        {
            cost = parad.onRightClickAir(par1ItemStack, par2World, par3EntityPlayer);
        }

        if (cost > 0)
        {
        	SoulNetworkHandler.syphonAndDamageFromNetwork(par1ItemStack, par3EntityPlayer, cost);
        }

        return par1ItemStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("A mace filled with ancient alchemy");

        if (!(par1ItemStack.getTagCompound() == null))
        {
            if (!par1ItemStack.getTagCompound().getString("ownerName").equals(""))
            {
                par3List.add("Current owner: " + par1ItemStack.getTagCompound().getString("ownerName"));
            }

            for (String str : this.getToolListString(par1ItemStack))
            {
                par3List.add(str);
            }

            par3List.add("");
            float damage = this.getCustomItemAttack(par1ItemStack);
            par3List.add("\u00A79+" + ((int) (damage * 10)) / 10.0f + " " + "Attack Damage");
            float critChance = ((int) (this.getCritChance(par1ItemStack) * 1000)) / 10.0f;
            par3List.add("\u00A79+" + critChance + "% " + "Crit Chance");
        }
    }

    //--------------Custom methods--------------//

    public void setHarvestLevel(ItemStack stack, String toolClass, int harvestLevel)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            tag.setInteger(harvestLevelSuffix + toolClass, Math.max(-1, harvestLevel));

            stack.getTagCompound().setTag(tagName, tag);
        } else
        {
            stack.setTagCompound(new NBTTagCompound());

            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            tag.setInteger(harvestLevelSuffix + toolClass, Math.max(-1, harvestLevel));

            stack.getTagCompound().setTag(tagName, tag);
        }
    }

    public void setDigSpeed(ItemStack stack, String toolClass, float digSpeed)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            tag.setFloat(digLevelSuffix + toolClass, digSpeed);

            stack.getTagCompound().setTag(tagName, tag);
        } else
        {
            stack.setTagCompound(new NBTTagCompound());

            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            tag.setFloat(digLevelSuffix + toolClass, digSpeed);

            stack.getTagCompound().setTag(tagName, tag);
        }
    }

    public float getDigSpeed(ItemStack stack, String toolClass)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            return tag.getFloat(digLevelSuffix + toolClass);
        } else
        {
            stack.setTagCompound(new NBTTagCompound());

            return 0.0f;
        }
    }

    public void setItemAttack(ItemStack stack, float damage)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            tag.setFloat("itemAttack", Math.max(damage, 0.0f));

            stack.getTagCompound().setTag(tagName, tag);
        } else
        {
            stack.setTagCompound(new NBTTagCompound());

            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            tag.setFloat("itemAttack", Math.max(damage, 0.0f));

            stack.getTagCompound().setTag(tagName, tag);
        }
    }

    public float getCustomItemAttack(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            return tag.getFloat("itemAttack");
        } else
        {
            stack.setTagCompound(new NBTTagCompound());

            return 0.0f;
        }
    }

    public ItemStack getContainedCrystal(ItemStack container)
    {
        if (container.hasTagCompound())
        {
            NBTTagCompound tag = container.getTagCompound().getCompoundTag(tagName).getCompoundTag("heldItem");

            return ItemStack.loadItemStackFromNBT(tag);
        } else
        {
            container.setTagCompound(new NBTTagCompound());

            return null;
        }
    }

    public void setContainedCrystal(ItemStack container, ItemStack crystal)
    {
        if (container.hasTagCompound())
        {
            NBTTagCompound compTag = container.getTagCompound().getCompoundTag(tagName);
            NBTTagCompound tag = compTag.getCompoundTag("heldItem");

            crystal.writeToNBT(tag);

            compTag.setTag("heldItem", tag);
            container.getTagCompound().setTag(tagName, compTag);
        } else
        {
            container.setTagCompound(new NBTTagCompound());

            NBTTagCompound compTag = container.getTagCompound().getCompoundTag(tagName);
            NBTTagCompound tag = compTag.getCompoundTag("heldItem");

            crystal.writeToNBT(tag);

            compTag.setTag("heldItem", tag);
            container.getTagCompound().setTag(tagName, compTag);
        }
    }

    public void setDuration(ItemStack container, World world, int duration)
    {
        if (world.isRemote)
        {
            return;
        } else
        {
            World overWorld = DimensionManager.getWorld(0);
            long worldtime = overWorld.getTotalWorldTime();

            if (container.hasTagCompound())
            {
                NBTTagCompound tag = container.getTagCompound().getCompoundTag(tagName);

                tag.setLong("duration", Math.max(duration + worldtime, worldtime));

                container.getTagCompound().setTag(tagName, tag);
            } else
            {
                container.setTagCompound(new NBTTagCompound());

                NBTTagCompound tag = container.getTagCompound().getCompoundTag(tagName);

                tag.setLong("duration", Math.max(duration + worldtime, worldtime));

                container.getTagCompound().setTag(tagName, tag);
            }
        }
    }

    public int getDuration(ItemStack container, World world)
    {
        if (world.isRemote)
        {
            return 0;
        } else
        {
            World overWorld = DimensionManager.getWorld(0);
            long worldtime = overWorld.getTotalWorldTime();

            if (container.hasTagCompound())
            {
                NBTTagCompound tag = container.getTagCompound().getCompoundTag(tagName);

                return (int) (tag.getLong("duration") - worldtime);
            } else
            {
                container.setTagCompound(new NBTTagCompound());

                return 0;
            }
        }
    }

    public void loadParadigmIntoStack(ItemStack container, List<SpellEffect> list)
    {
        if (!container.hasTagCompound())
        {
            container.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tagiest = container.getTagCompound().getCompoundTag(tagName);

        NBTTagList effectList = new NBTTagList();

        for (SpellEffect eff : list)
        {
            effectList.appendTag(eff.getTag());
        }

        tagiest.setTag("Effects", effectList);

        container.getTagCompound().setTag(tagName, tagiest);
    }

    public SpellParadigmTool loadParadigmFromStack(ItemStack container)
    {
        if (!container.hasTagCompound())
        {
            container.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tagiest = container.getTagCompound().getCompoundTag(tagName);

        NBTTagList tagList = tagiest.getTagList("Effects", Constants.NBT.TAG_COMPOUND);

        List<SpellEffect> spellEffectList = new LinkedList();
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);

            SpellEffect eff = SpellEffect.getEffectFromTag(tag);
            if (eff != null)
            {
                spellEffectList.add(eff);
            }
        }

        return SpellParadigmTool.getParadigmForEffectArray(spellEffectList);
    }

    public void setSilkTouch(ItemStack stack, boolean silkTouch)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            tag.setBoolean("silkTouch", silkTouch);

            stack.getTagCompound().setTag(tagName, tag);
        } else
        {
            stack.setTagCompound(new NBTTagCompound());

            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            tag.setBoolean("silkTouch", silkTouch);

            stack.getTagCompound().setTag(tagName, tag);
        }
    }

    public boolean getSilkTouch(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            return tag.getBoolean("silkTouch");
        } else
        {
            stack.setTagCompound(new NBTTagCompound());

            return false;
        }
    }

    public void setFortuneLevel(ItemStack stack, int fortune)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            tag.setInteger("fortuneLevel", Math.max(fortune, 0));

            stack.getTagCompound().setTag(tagName, tag);
        } else
        {
            stack.setTagCompound(new NBTTagCompound());

            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            tag.setInteger("fortuneLevel", Math.max(fortune, 0));

            stack.getTagCompound().setTag(tagName, tag);
        }
    }

    public int getFortuneLevel(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(tagName);

            return tag.getInteger("fortuneLevel");
        } else
        {
            stack.setTagCompound(new NBTTagCompound());

            return 0;
        }
    }

    public List<String> getToolListString(ItemStack container)
    {
        if (!container.hasTagCompound())
        {
            container.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tagiest = container.getTagCompound().getCompoundTag(tagName);

        NBTTagList tagList = tagiest.getTagList("ToolTips", Constants.NBT.TAG_COMPOUND);

        List<String> toolTipList = new LinkedList();
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);

            String str = tag.getString("tip");
            if (str != null)
            {
                toolTipList.add(str);
            }
        }

        return toolTipList;
    }

    public void setToolListString(ItemStack container, List<String> toolTipString)
    {
        if (!container.hasTagCompound())
        {
            container.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tagiest = container.getTagCompound().getCompoundTag(tagName);

        NBTTagList stringList = new NBTTagList();

        for (String str : toolTipString)
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("tip", str);

            stringList.appendTag(tag);
        }

        tagiest.setTag("ToolTips", stringList);

        container.getTagCompound().setTag(tagName, tagiest);
    }

    public void setCritChance(ItemStack container, float chance)
    {
        if (container.hasTagCompound())
        {
            NBTTagCompound tag = container.getTagCompound().getCompoundTag(tagName);

            tag.setFloat("critChance", Math.max(chance, 0));

            container.getTagCompound().setTag(tagName, tag);
        } else
        {
            container.setTagCompound(new NBTTagCompound());

            NBTTagCompound tag = container.getTagCompound().getCompoundTag(tagName);

            tag.setFloat("critChance", Math.max(chance, 0));

            container.getTagCompound().setTag(tagName, tag);
        }
    }

    public float getCritChance(ItemStack container)
    {
        if (container.hasTagCompound())
        {
            NBTTagCompound tag = container.getTagCompound().getCompoundTag(tagName);

            return tag.getFloat("critChance");
        } else
        {
            container.setTagCompound(new NBTTagCompound());

            return 0;
        }
    }
}