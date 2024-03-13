package ralf2oo2.betterf3;

import net.glasslauncher.mods.api.gcapi.api.GConfig;
import ralf2oo2.betterf3.config.GeneralConfig;
import ralf2oo2.betterf3.config.ModConfigFile;
import ralf2oo2.betterf3.modules.CoordsModule;
import ralf2oo2.betterf3.modules.MinecraftModule;
import ralf2oo2.betterf3.modules.SystemModule;
import ralf2oo2.betterf3.utils.PositionEnum;

public class Betterf3 {
    public Betterf3() {
        new MinecraftModule().init();
        new CoordsModule().init();

        new SystemModule().init(PositionEnum.RIGHT);

        ModConfigFile.load();
    }
}
