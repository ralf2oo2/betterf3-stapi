package ralf2oo2.betterf3;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import ralf2oo2.betterf3.config.ModConfigFile;
import ralf2oo2.betterf3.modules.*;
import ralf2oo2.betterf3.utils.PositionEnum;

public class Betterf3 {
    @Entrypoint.Namespace
    public static final Namespace MODID = Null.get();
    public Betterf3() {
        new MinecraftModule().init();
        new FpsModule().init();
        new CoordsModule().init();
        BaseModule.modules.add(EmptyModule.INSTANCE);

        new SystemModule().init(PositionEnum.RIGHT);
        BaseModule.modulesRight.add(EmptyModule.INSTANCE);

        ModConfigFile.load();
    }
}
