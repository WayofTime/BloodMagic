package wayoftime.bloodmagic.client.model.sigil;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class SigilLoader implements IGeometryLoader<UnbakedSigilModel> {

    @Override
    public UnbakedSigilModel read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
        return new UnbakedSigilModel();
    }
}
