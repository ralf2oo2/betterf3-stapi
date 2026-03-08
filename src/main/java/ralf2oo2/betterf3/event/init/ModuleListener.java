package ralf2oo2.betterf3.event.init;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import ralf2oo2.betterf3.modules.*;
import ralf2oo2.betterf3.registry.ModuleRegisterEvent;

public class ModuleListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    public static Identifier minecraftModule;
    public static Identifier fpsModule;
    public static Identifier coordsModule;
    public static Identifier locationModule;
    public static Identifier entityModule;
    public static Identifier systemModule;
    public static Identifier targetModule;
    public static Identifier emptyModule;

    @EventListener
    public void registerModules(ModuleRegisterEvent event){
        minecraftModule = NAMESPACE.id("minecraft_module");
        fpsModule = NAMESPACE.id("fps_module");
        coordsModule = NAMESPACE.id("coords_module");
        locationModule = NAMESPACE.id("location_module");
        entityModule = NAMESPACE.id("entity_module");
        systemModule = NAMESPACE.id("system_module");
        targetModule = NAMESPACE.id("target_module");
        emptyModule = NAMESPACE.id("empty_module");

        event.register(minecraftModule, MinecraftModule::new, null, "minecraft");
        event.register(fpsModule, FpsModule::new, null, "fps");
        event.register(coordsModule, CoordsModule::new, null, "coords");
        event.register(locationModule, LocationModule::new, null, "location");
        event.register(entityModule, EntityModule::new, null, "entity");
        event.register(systemModule, SystemModule::new, null, "system");
        event.register(targetModule, TargetModule::new, null, "target");
        event.register(emptyModule, EmptyModule::new, null, "empty");
    }
}
