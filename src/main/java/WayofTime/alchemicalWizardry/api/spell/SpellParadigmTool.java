package WayofTime.alchemicalWizardry.api.spell;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.api.items.ItemSpellMultiTool;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;

public class SpellParadigmTool extends SpellParadigm
{
    private List<ILeftClickEffect> leftClickEffectList;
    private List<IRightClickEffect> rightClickEffectList;
    private List<IToolUpdateEffect> toolUpdateEffectList;
    private List<IOnSummonTool> toolSummonEffectList;
    private List<IOnBanishTool> toolBanishEffectList;
    private List<IOnBreakBlock> breakBlockEffectList;
    private List<IItemManipulator> itemManipulatorEffectList;
    private List<IDigAreaEffect> digAreaEffectList;
    private List<ISpecialDamageEffect> specialDamageEffectList;

    private float maxDamage;
    private HashMap<String, Integer> harvestLevel;
    private HashMap<String, Float> digSpeed;
    private HashMap<String, Float> maxDamageHash;
    private HashMap<String, Float> critChanceHash;
    private HashMap<String, Integer> durationHash;

    private HashMap<String, String> toolInfoString;

    private int fortuneLevel;
    private boolean silkTouch;

    private int duration;
    
    public static Item customTool;

    public SpellParadigmTool()
    {
        this.leftClickEffectList = new LinkedList();
        this.rightClickEffectList = new LinkedList();
        this.toolUpdateEffectList = new LinkedList();
        this.toolSummonEffectList = new LinkedList();
        this.toolBanishEffectList = new LinkedList();
        this.breakBlockEffectList = new LinkedList();
        this.itemManipulatorEffectList = new LinkedList();
        this.digAreaEffectList = new LinkedList();
        this.specialDamageEffectList = new LinkedList();
        this.durationHash = new HashMap();

        this.toolInfoString = new HashMap();
        this.critChanceHash = new HashMap();

        this.maxDamage = 5;

        this.harvestLevel = new HashMap();
        this.harvestLevel.put("pickaxe", -1);
        this.harvestLevel.put("shovel", -1);
        this.harvestLevel.put("axe", -1);

        this.digSpeed = new HashMap();
        this.digSpeed.put("pickaxe", 1.0f);
        this.digSpeed.put("shovel", 1.0f);
        this.digSpeed.put("axe", 1.0f);

        this.maxDamageHash = new HashMap();
        this.maxDamageHash.put("default", 5.0f);

        this.fortuneLevel = 0;
        this.silkTouch = false;
        this.duration = 0;
        this.durationHash.put("default", 2400);
    }

    @Override
    public void enhanceParadigm(SpellEnhancement enh)
    {

    }

    @Override
    public void castSpell(World world, EntityPlayer entityPlayer, ItemStack crystal)
    {
        if (entityPlayer.worldObj.isRemote)
        {
            return;
        }

        int cost = this.getTotalCost();
        
        if(!SoulNetworkHandler.syphonAndDamageFromNetwork(crystal, entityPlayer, cost))
        {
        	return;
        }

        ItemStack toolStack = this.prepareTool(crystal, world);

        entityPlayer.setCurrentItemOrArmor(0, toolStack);

        this.onSummonTool(toolStack, world, entityPlayer);
    }

    /**
     * @param crystalStack
     * @return stack containing the new multitool
     */
    public ItemStack prepareTool(ItemStack crystalStack, World world)
    {
        ItemStack toolStack = new ItemStack(customTool, 1);

        ItemSpellMultiTool itemTool = (ItemSpellMultiTool) customTool;

        itemTool.setItemAttack(toolStack, this.composeMaxDamageFromHash());

        Set<Entry<String, Integer>> harvestLevelSet = this.harvestLevel.entrySet();

        for (Entry<String, Integer> testMap : harvestLevelSet)
        {
            String tool = testMap.getKey();
            int level = testMap.getValue();

            itemTool.setHarvestLevel(toolStack, tool, level);
        }

        Set<Entry<String, Float>> digSpeedSet = this.digSpeed.entrySet();

        for (Entry<String, Float> testMap : digSpeedSet)
        {
            String tool = testMap.getKey();
            float speed = testMap.getValue();

            itemTool.setDigSpeed(toolStack, tool, speed);
        }

        itemTool.setFortuneLevel(toolStack, getFortuneLevel());
        itemTool.setSilkTouch(toolStack, this.getSilkTouch());

        if (this.getSilkTouch())
        {
            this.addToolString("SilkTouch", "Silk Touch" + " " + APISpellHelper.getNumeralForInt(1));
        }

        if (this.getFortuneLevel() > 0)
        {
            this.addToolString("Fortune", "Fortune" + " " + APISpellHelper.getNumeralForInt(this.getFortuneLevel()));
        }

        itemTool.setCritChance(toolStack, this.getCritChance() / 100f);

        List<String> toolStringList = new LinkedList();

        for (String str : this.toolInfoString.values())
        {
            toolStringList.add(str);
        }

        itemTool.setToolListString(toolStack, toolStringList);

        for (Integer integ : this.durationHash.values())
        {
            this.duration += integ;
        }

        itemTool.setDuration(toolStack, world, this.duration);
        itemTool.loadParadigmIntoStack(toolStack, this.bufferedEffectList);

        SoulNetworkHandler.checkAndSetItemOwner(toolStack, SoulNetworkHandler.getOwnerName(crystalStack));

        itemTool.setContainedCrystal(toolStack, crystalStack);

        return toolStack;
    }

    @Override
    public int getDefaultCost()
    {
        return 100;
    }

    public static SpellParadigmTool getParadigmForEffectArray(List<SpellEffect> effectList)
    {
        SpellParadigmTool parad = new SpellParadigmTool();

        for (SpellEffect eff : effectList)
        {
            parad.addBufferedEffect(eff);
        }

        parad.applyAllSpellEffects();

        return parad;
    }

    public void addLeftClickEffect(ILeftClickEffect eff)
    {
        if (eff != null)
        {
            this.leftClickEffectList.add(eff);
        }
    }

    public void addRightClickEffect(IRightClickEffect eff)
    {
        if (eff != null)
        {
            this.rightClickEffectList.add(eff);
        }
    }

    public void addUpdateEffect(IToolUpdateEffect eff)
    {
        if (eff != null)
        {
            this.toolUpdateEffectList.add(eff);
        }
    }

    public void addToolSummonEffect(IOnSummonTool eff)
    {
        if (eff != null)
        {
            this.toolSummonEffectList.add(eff);
        }
    }

    public void addToolBanishEffect(IOnBanishTool eff)
    {
        if (eff != null)
        {
            this.toolBanishEffectList.add(eff);
        }
    }

    public void addBlockBreakEffect(IOnBreakBlock eff)
    {
        if (eff != null)
        {
            this.breakBlockEffectList.add(eff);
        }
    }

    public void addItemManipulatorEffect(IItemManipulator eff)
    {
        if (eff != null)
        {
            this.itemManipulatorEffectList.add(eff);
        }
    }

    public void addDigAreaEffect(IDigAreaEffect eff)
    {
        if (eff != null)
        {
            this.digAreaEffectList.add(eff);
        }
    }

    public void addSpecialDamageEffect(ISpecialDamageEffect eff)
    {
        if (eff != null)
        {
            this.specialDamageEffectList.add(eff);
        }
    }

    public int onLeftClickEntity(ItemStack stack, EntityLivingBase attacked, EntityLivingBase weilder)
    {
        int total = 0;
        for (ILeftClickEffect effect : this.leftClickEffectList)
        {
            total += effect.onLeftClickEntity(stack, attacked, weilder);
        }

        return total;
    }

    public int onRightClickBlock(ItemStack toolStack, EntityLivingBase weilder, World world, MovingObjectPosition mop)
    {
        int total = 0;
        for (IRightClickEffect effect : this.rightClickEffectList)
        {
            total += effect.onRightClickBlock(toolStack, weilder, world, mop);
        }

        return total;
    }

    public int onRightClickAir(ItemStack toolStack, World world, EntityPlayer player)
    {
        int total = 0;
        for (IRightClickEffect effect : this.rightClickEffectList)
        {
            total += effect.onRightClickAir(toolStack, player);
        }

        return total;
    }

    public int onUpdate(ItemStack toolStack, World world, Entity par3Entity, int invSlot, boolean inHand)
    {
        int total = 0;
        for (IToolUpdateEffect effect : this.toolUpdateEffectList)
        {
            total += effect.onUpdate(toolStack, world, par3Entity, invSlot, inHand);
        }

        return total;
    }

    public int onSummonTool(ItemStack toolStack, World world, Entity entity)
    {
        int total = 0;
        for (IOnSummonTool effect : this.toolSummonEffectList)
        {
            total += effect.onSummonTool(toolStack, world, entity);
        }

        return total;
    }

    public int onBanishTool(ItemStack toolStack, World world, Entity entity, int invSlot, boolean inHand)
    {
        int total = 0;
        for (IOnBanishTool effect : this.toolBanishEffectList)
        {
            total += effect.onBanishTool(toolStack, world, entity, invSlot, inHand);
        }

        return total;
    }

    public int onBreakBlock(ItemStack container, World world, EntityPlayer player, Block block, int meta, int x, int y, int z, ForgeDirection sideBroken)
    {
        int total = 0;
        for (IOnBreakBlock effect : this.breakBlockEffectList)
        {
            total += effect.onBlockBroken(container, world, player, block, meta, x, y, z, sideBroken);
        }

        return total;
    }

    public List<ItemStack> handleItemList(ItemStack toolStack, List<ItemStack> items)
    {
        List<ItemStack> heldList = items;

        for (IItemManipulator eff : this.itemManipulatorEffectList)
        {
            List<ItemStack> newHeldList = eff.handleItemsOnBlockBroken(toolStack, heldList);
            heldList = newHeldList;
        }

        return heldList;
    }

    public int digSurroundingArea(ItemStack container, World world, EntityPlayer player, MovingObjectPosition blockPos, String usedToolClass, float blockHardness, int harvestLvl, ItemSpellMultiTool itemTool)
    {
        int cost = 0;

        for (IDigAreaEffect effect : this.digAreaEffectList)
        {
            cost += effect.digSurroundingArea(container, world, player, blockPos, usedToolClass, blockHardness, harvestLvl, itemTool);
        }

        return cost;
    }

    public int getFortuneLevel()
    {
        return this.fortuneLevel;
    }

    public void setFortuneLevel(int fortuneLevel)
    {
        this.fortuneLevel = fortuneLevel;
    }

    public boolean getSilkTouch()
    {
        return this.silkTouch;
    }

    public void setSilkTouch(boolean silkTouch)
    {
        this.silkTouch = silkTouch;
    }

    public int getDuration()
    {
        return this.duration;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    public void setDigSpeed(String toolClass, float digSpeed)
    {
        this.digSpeed.put(toolClass, digSpeed);
    }

    public void setHarvestLevel(String toolClass, int hlvl)
    {
        this.harvestLevel.put(toolClass, hlvl);
    }

    public float composeMaxDamageFromHash()
    {
        float damage = 0.0f;

        for (float f : this.maxDamageHash.values())
        {
            damage += f;
        }

        return damage;
    }

    public void addDamageToHash(String key, float dmg)
    {
        this.maxDamageHash.put(key, dmg);
    }

    public void addToolString(String key, String str)
    {
        if (str != null && key != null)
        {
            this.toolInfoString.put(key, str);
        }
    }

    public void addCritChance(String key, float chance)
    {
        //Chance is in percentage chance i.e. chance = 1.0 means 1.0%
        this.critChanceHash.put(key, chance);
    }

    public void addDuration(String key, int dur)
    {
        this.durationHash.put(key, dur);
    }

    public float getCritChance()
    {
        float chance = 0.0f;

        for (float fl : this.critChanceHash.values())
        {
            chance += fl;
        }

        return chance;
    }

    public float getAddedDamageForEntity(Entity entity)
    {
        HashMap<String, Float> hash = new HashMap();

        for (ISpecialDamageEffect effect : this.specialDamageEffectList)
        {
            hash.put(effect.getKey(), effect.getDamageForEntity(entity));
        }

        float addedDmg = 0.0f;

        for (float fl : hash.values())
        {
            addedDmg += fl;
        }

        return addedDmg;
    }
}