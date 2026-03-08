package ralf2oo2.betterf3.registry;

import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ralf2oo2.betterf3.modules.BaseModuleFactory;
import ralf2oo2.betterf3.utils.ModulePosition;

import java.util.Locale;

public class ModuleRegisterEvent extends Event {
    public ModuleRegistry registry;

    public ModuleRegisterEvent(){
        registry = ModuleRegistry.getInstance();
    }

    public void register(Identifier identifier, BaseModuleFactory factory, @Nullable ModulePosition defaultPosition, String translationName){
        String translationKey = "text." + identifier.namespace + ".module." + translationName;
        registry.registerModule(identifier, factory, defaultPosition, translationKey);
    }
}
