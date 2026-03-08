package ralf2oo2.betterf3.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ralf2oo2.betterf3.modules.BaseModule;
import ralf2oo2.betterf3.modules.BaseModuleFactory;
import ralf2oo2.betterf3.utils.ModulePosition;

public class ModuleRegistry {
    private static final ModuleRegistry INSTANCE = new ModuleRegistry();

    public Object2ObjectOpenHashMap<Identifier, BaseModuleFactory> ID_TO_FACTORY = new Object2ObjectOpenHashMap<>();
    public Object2ObjectOpenHashMap<BaseModuleFactory, Identifier> FACTORY_TO_ID = new Object2ObjectOpenHashMap<>();
    public Object2ObjectOpenHashMap<Identifier, ModulePosition> ID_TO_DEFAULT_POSITION = new Object2ObjectOpenHashMap<>();
    public Object2ObjectOpenHashMap<Identifier, String> ID_TO_TRANSLATION_KEY = new Object2ObjectOpenHashMap<>();

    public static ModuleRegistry getInstance() {
        return INSTANCE;
    }

    public void registerModule(Identifier identifier, BaseModuleFactory factory, @Nullable ModulePosition defaultPosition, String translationKey){
        if(!ID_TO_FACTORY.containsKey(identifier)){
            ID_TO_FACTORY.put(identifier, factory);
            FACTORY_TO_ID.put(factory, identifier);
            ID_TO_DEFAULT_POSITION.put(identifier, defaultPosition);
            ID_TO_TRANSLATION_KEY.put(identifier, translationKey);
        }
    }

    @Nullable
    public BaseModule createInstance(Identifier identifier){
        if(!ID_TO_FACTORY.containsKey(identifier)){
            return null;
        }
        return ID_TO_FACTORY.get(identifier).create(identifier);
    }
}
