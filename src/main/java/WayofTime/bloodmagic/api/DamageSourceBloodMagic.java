package WayofTime.bloodmagic.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class DamageSourceBloodMagic extends DamageSource
{
    public DamageSourceBloodMagic()
    {
        super("bloodMagic");

        setDamageBypassesArmor();
        setDamageIsAbsolute();
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase livingBase)
    {
        return new ChatComponentText(TextHelper.localizeEffect("chat.BloodMagic.damageSource", livingBase.getName()));
    }
}
