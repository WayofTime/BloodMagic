package WayofTime.bloodmagic.compat.thaumcraft.research;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;

public class SanguineResearchItem extends ResearchItem
{
    public SanguineResearchItem(String key, String category)
    {
        super(key, category);
    }

    public SanguineResearchItem(String key, String category, AspectList tags, int col, int row, int complex, Object... icon)
    {
        super(key, category, tags, col, row, complex, icon);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getName()
    {
        return StatCollector.translateToLocal("bloodmagic.research_name." + this.key);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getText()
    {
        return StatCollector.translateToLocal("bloodmagic.research_text." + this.key);
    }
}
