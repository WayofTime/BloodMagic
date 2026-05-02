package wayoftime.bloodmagic.compat.modopedia;

import net.favouriteless.modopedia.api.registries.client.TemplateRegistry;
import net.favouriteless.modopedia.api.registries.client.TextFormatterRegistry;
import wayoftime.bloodmagic.compat.modopedia.text.ExtraFormatter;
import wayoftime.bloodmagic.compat.modopedia.text.LinkFormatter;

public class BookCompat {

    public static void init() {
        registerTemplates();
        registerTextProcessors();
    }

    public static void registerTemplates() {
        TemplateRegistry registry = TemplateRegistry.get();
    }

    public static void registerTextProcessors() {
        TextFormatterRegistry registry = TextFormatterRegistry.get();
        registry.register(new ExtraFormatter());
        registry.register(new LinkFormatter("bmcat:", "category"));
        registry.register(new LinkFormatter("bmentry:", "entry"));
    }
}
