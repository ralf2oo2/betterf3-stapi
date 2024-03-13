package ralf2oo2.betterf3.config;

import net.glasslauncher.mods.api.gcapi.api.GConfig;

public class Betterf3Config {
    @GConfig(value = "config", visibleName = "General Config")
    public static final GeneralConfig generalConfig = new GeneralConfig();
}
