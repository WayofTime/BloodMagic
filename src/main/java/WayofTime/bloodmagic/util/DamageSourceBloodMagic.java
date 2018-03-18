package WayofTime.bloodmagic.util;

import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class DamageSourceBloodMagic extends DamageSource {

    public static final DamageSourceBloodMagic INSTANCE = new DamageSourceBloodMagic();

    public DamageSourceBloodMagic() {
        super("bloodMagic");

        setDamageBypassesArmor();
        setDamageIsAbsolute();
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase livingBase) {
        return new TextComponentString(TextHelper.localizeEffect("chat.bloodmagic.damageSource", livingBase.getName()));
    }
}
