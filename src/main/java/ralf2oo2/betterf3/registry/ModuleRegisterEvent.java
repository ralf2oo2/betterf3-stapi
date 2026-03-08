package ralf2oo2.betterf3.registry;

import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ralf2oo2.betterf3.modules.BaseModuleFactory;
import ralf2oo2.betterf3.utils.ModulePosition;
import ralf2oo2.betterf3.utils.Utils;

public class ModuleRegisterEvent extends Event {
    public ModuleRegistry registry;

    public ModuleRegisterEvent(){
        registry = ModuleRegistry.getInstance();
    }

    /**
     * Registers a new module into the system registry with its associated factory,
     * default positioning, and a generated translation key.
     *
     * @param identifier      The unique {@link Identifier} for the module
     * @param factory         The {@link BaseModuleFactory} used to instantiate the module.
     * @param defaultPosition The initial {@link ModulePosition} for the module;
     * if the module shouldn't show up by default, you can pass {@code null}.
     * @param translationName The name string used to construct the module's
     * localized translation key.
     */
    public void register(Identifier identifier, BaseModuleFactory factory, @Nullable ModulePosition defaultPosition, String translationName){
        String translationKey = Utils.getTextTranslationKey(identifier.namespace, "module", translationName);
        registry.registerModule(identifier, factory, defaultPosition, translationKey);
    }
}
