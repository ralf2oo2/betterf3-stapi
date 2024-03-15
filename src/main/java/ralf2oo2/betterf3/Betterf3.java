package ralf2oo2.betterf3;

import net.glasslauncher.mods.api.gcapi.api.GConfig;
import ralf2oo2.betterf3.config.GeneralConfig;
import ralf2oo2.betterf3.config.ModConfigFile;
import ralf2oo2.betterf3.modules.*;
import ralf2oo2.betterf3.utils.PositionEnum;

public class Betterf3 {
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
