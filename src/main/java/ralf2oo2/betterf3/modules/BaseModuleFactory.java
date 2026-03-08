package ralf2oo2.betterf3.modules;

import net.modificationstation.stationapi.api.util.Identifier;

public interface BaseModuleFactory {
    BaseModule create(Identifier id);
}
