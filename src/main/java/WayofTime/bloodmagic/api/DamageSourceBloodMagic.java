package WayofTime.bloodmagic.api;

import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

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
