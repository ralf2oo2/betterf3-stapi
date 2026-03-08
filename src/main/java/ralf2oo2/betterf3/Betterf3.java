package ralf2oo2.betterf3;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;
import ralf2oo2.betterf3.config.ModConfigFile;
import ralf2oo2.betterf3.event.init.ModuleListener;
import ralf2oo2.betterf3.registry.ModuleRegisterEvent;
import ralf2oo2.betterf3.registry.ModuleRegistry;
import ralf2oo2.betterf3.utils.LegacyIdMap;
import ralf2oo2.betterf3.utils.ModulePosition;
import ralf2oo2.betterf3.utils.Utils;

public class Betterf3 {
    public static final F3State f3State = new F3State();

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER;

    @Environment(EnvType.CLIENT)
    @EventListener(phase = InitEvent.PRE_INIT_PHASE)
    public void preInitListener(InitEvent event) {
        FabricLoader.getInstance().getEntrypointContainers("betterf3:event_bus_client", Object.class).forEach(EntrypointManager::setup);
    }

    @EventListener
    public void initListener(InitEvent event){
        StationAPI.EVENT_BUS.post(new ModuleRegisterEvent());

        setupDefaultModuleState(f3State);

        LegacyIdMap.setup();

        ModConfigFile.load();

        Utils.getProcessorInfo();
    }

    public void setupDefaultModuleState(F3State state){
        state.clear();

        state.leftModules.add(ModuleRegistry.getInstance().createInstance(ModuleListener.minecraftModule));
        state.leftModules.add(ModuleRegistry.getInstance().createInstance(ModuleListener.fpsModule));
        state.leftModules.add(ModuleRegistry.getInstance().createInstance(ModuleListener.locationModule));
        state.leftModules.add(ModuleRegistry.getInstance().createInstance(ModuleListener.entityModule));

        state.rightModules.add(ModuleRegistry.getInstance().createInstance(ModuleListener.systemModule));
        state.leftModules.add(ModuleRegistry.getInstance().createInstance(ModuleListener.emptyModule));
        state.rightModules.add(ModuleRegistry.getInstance().createInstance(ModuleListener.targetModule));

        for(Identifier module : ModuleRegistry.getInstance().ID_TO_FACTORY.keySet()){
            ModulePosition position = ModuleRegistry.getInstance().ID_TO_DEFAULT_POSITION.get(module);
            if(position == ModulePosition.LEFT){
                state.leftModules.add(ModuleRegistry.getInstance().createInstance(module));
            }
            if(position == ModulePosition.RIGHT){
                state.rightModules.add(ModuleRegistry.getInstance().createInstance(module));
            }
        }
    }
}
