package ralf2oo2.betterf3.config;

import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.GeneratedConfig;

import java.lang.reflect.Field;
public class GeneralConfig {
    @ConfigName("Disable Mod")
    public Boolean disableMod = false;

    @ConfigName("Disable Lagometer")
    public Boolean disableLagometer = true;

    @ConfigName("Space Every Module")
    public Boolean spaceEveryModule = false;

    @ConfigName("Shadow Text")
    public Boolean shadowText = true;

    @ConfigName("Enable Animations")
    public Boolean enableAnimations = true;

    @ConfigName("Animation Speed")
    public Float animationSpeed = 1f;
    //public double fontScale = 1;

    @ConfigName("Background Color")
    public Integer backgroundColor = 0x6F505050;
}
