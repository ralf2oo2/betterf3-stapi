package ralf2oo2.betterf3.utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.modificationstation.stationapi.api.util.Identifier;
import ralf2oo2.betterf3.event.init.ModuleListener;

public class LegacyIdMap {
    public static final Object2ObjectOpenHashMap<String, Identifier> LEGACY_ID_TO_ID = new Object2ObjectOpenHashMap<>();

    public static void setup(){
        LEGACY_ID_TO_ID.put("minecraft", ModuleListener.minecraftModule);
        LEGACY_ID_TO_ID.put("fps", ModuleListener.fpsModule);
        LEGACY_ID_TO_ID.put("coords", ModuleListener.coordsModule);
        LEGACY_ID_TO_ID.put("location", ModuleListener.locationModule);
        LEGACY_ID_TO_ID.put("entity", ModuleListener.entityModule);
        LEGACY_ID_TO_ID.put("system", ModuleListener.systemModule);
        LEGACY_ID_TO_ID.put("target", ModuleListener.targetModule);
        LEGACY_ID_TO_ID.put("empty", ModuleListener.emptyModule);
    }
}
