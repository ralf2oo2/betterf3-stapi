package ralf2oo2.betterf3test.event.init;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import ralf2oo2.betterf3.registry.ModuleRegisterEvent;
import ralf2oo2.betterf3.utils.ModulePosition;
import ralf2oo2.betterf3test.modules.TestModule;

public class ModuleListener {

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerModules(ModuleRegisterEvent event){
        event.register(NAMESPACE.id("test_module"), TestModule::new, ModulePosition.LEFT, "test");
    }
}
