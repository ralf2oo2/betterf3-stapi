package ralf2oo2.betterf3;

import ralf2oo2.betterf3.modules.CoordsModule;
import ralf2oo2.betterf3.modules.MinecraftModule;
import ralf2oo2.betterf3.modules.SystemModule;
import ralf2oo2.betterf3.utils.PositionEnum;

public class Betterf3 {
    public Betterf3() {
        new MinecraftModule().init();
        new CoordsModule().init();

        new SystemModule().init(PositionEnum.RIGHT);
    }
}
