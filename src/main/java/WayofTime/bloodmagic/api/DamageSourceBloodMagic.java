package WayofTime.bloodmagic.api;

import net.minecraft.util.DamageSource;

public class DamageSourceBloodMagic extends DamageSource {

    public DamageSourceBloodMagic() {
        super("bloodMagic");

        setDamageBypassesArmor();
    }
}
