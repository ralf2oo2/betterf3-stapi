package ralf2oo2.betterf3;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import ralf2oo2.betterf3.config.ModConfigFile;
import ralf2oo2.betterf3.modules.*;
import ralf2oo2.betterf3.utils.PositionEnum;
import ralf2oo2.betterf3.utils.Utils;

public class Betterf3 {
    @Entrypoint.Namespace
    public static Namespace MODID = Null.get();
    public Betterf3() {
        new MinecraftModule().init();
        new FpsModule().init();
        new CoordsModule().init();
        new LocationModule().init();
        new EntityModule().init();

        BaseModule.modules.add(EmptyModule.INSTANCE);

        new SystemModule().init(PositionEnum.RIGHT);
        BaseModule.modulesRight.add(EmptyModule.INSTANCE);
        new TargetModule().init(PositionEnum.RIGHT);

        ModConfigFile.load();

        // Loads processor info when launching to avoid a stutter when opening the debug hud for the first time in a session
        Utils.getProcessorInfo();
    }
}
