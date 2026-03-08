package ralf2oo2.betterf3.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ralf2oo2.betterf3.Betterf3;
import ralf2oo2.betterf3.modules.BaseModule;
import ralf2oo2.betterf3.modules.CoordsModule;
import ralf2oo2.betterf3.modules.FpsModule;
import ralf2oo2.betterf3.registry.ModuleRegistry;
import ralf2oo2.betterf3.utils.DebugLine;
import ralf2oo2.betterf3.utils.LegacyIdMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModConfigFile {
    public static Runnable saveRunnable = () -> {
        FileConfig config = FileConfig.builder(FabricLoader.getInstance().getConfigDir().resolve("betterf3.json")).concurrent().autosave().build();

        Config general = Config.inMemory();
        general.set("disable_mod", GeneralOptions.disableMod);
        general.set("space_modules", GeneralOptions.spaceEveryModule);
        general.set("shadow_text", GeneralOptions.shadowText);
        general.set("animations", GeneralOptions.enableAnimations);
        general.set("animationSpeed", GeneralOptions.animationSpeed);
        general.set("background_color", GeneralOptions.backgroundColor);
        general.set("disable_lagometer", GeneralOptions.disableLagometer);
        general.set("show_background", GeneralOptions.showBackground);

        List<Config> configsLeft = new ArrayList<>();

        for (BaseModule module : Betterf3.f3State.leftModules) {

            Config moduleConfig = saveModule(module);

            configsLeft.add(moduleConfig);
        }

        List<Config> configsRight = new ArrayList<>();

        for (BaseModule module : Betterf3.f3State.rightModules) {

            Config moduleConfig = saveModule(module);

            configsRight.add(moduleConfig);
        }

        config.set("modules_left", configsLeft);
        config.set("modules_right", configsRight);

        config.set("general", general);
    };

    public static void load(){
        File file = FabricLoader.getInstance().getConfigDir().resolve("betterf3.json").toFile();

        if (!file.exists()) {
            return;
        }

        FileConfig config = FileConfig.builder(file).concurrent().autosave().build();

        config.load();

        List<BaseModule> leftModules = new ArrayList<>();
        List<BaseModule> rightModules = new ArrayList<>();

        List<Config> modulesLeftConfig = config.getOrElse("modules_left", () -> null);

        if (modulesLeftConfig != null) {

            for (Config moduleConfig : modulesLeftConfig) {
                String moduleName = moduleConfig.getOrElse("name", () -> null);
                String idString = moduleConfig.getOrElse("id", () -> null);

                if (moduleName == null && idString == null) {
                    continue;
                }

                BaseModule baseModule = ModConfigFile.loadModule(moduleConfig);
                if(baseModule == null){
                    continue;
                }

                leftModules.add(baseModule);
            }
        }

        if (!leftModules.isEmpty()) {
            Betterf3.f3State.leftModules = leftModules;
        }

        List<Config> modulesRightConfig = config.getOrElse("modules_right", () -> null);

        if (modulesRightConfig != null) {
            for (Config moduleConfig : modulesRightConfig) {

                String moduleName = moduleConfig.getOrElse("name", () -> null);
                String idString = moduleConfig.getOrElse("id", () -> null);

                if (moduleName == null && idString == null) {
                    continue;
                }

                BaseModule baseModule = ModConfigFile.loadModule(moduleConfig);
                if(baseModule == null){
                    continue;
                }

                rightModules.add(baseModule);
            }
        }

        if (!rightModules.isEmpty()) {
            Betterf3.f3State.rightModules = rightModules;
        }

        Config general = config.getOrElse("general", () -> null);

        if (general != null) {
            GeneralOptions.disableMod = general.getOrElse("disable_mod", false);
            GeneralOptions.spaceEveryModule = general.getOrElse("space_modules", false);
            GeneralOptions.shadowText = general.getOrElse("shadow_text", true);
            GeneralOptions.enableAnimations = general.getOrElse("animations", true);
            GeneralOptions.animationSpeed = general.getOrElse("animationSpeed", 1.0);
            GeneralOptions.backgroundColor = general.getOrElse("background_color", 0x6F505050);
            GeneralOptions.disableLagometer = general.getOrElse("disable_lagometer", true);
            GeneralOptions.showBackground = general.getOrElse("show_background", true);
        }

        config.close();
    }

    @Nullable
    private static BaseModule loadModule(Config moduleConfig){
        String moduleName = moduleConfig.getOrElse("name", () -> null);
        String idString = moduleConfig.getOrElse("id", () -> null);
        Identifier id = null;
        if(moduleName != null){
            if(!LegacyIdMap.LEGACY_ID_TO_ID.containsKey(moduleName)){
                Betterf3.LOGGER.warn("No module for legacy name '{}', skipping...", moduleName);
                return null;
            }
            Betterf3.LOGGER.info("Loading legacy module '{}'", moduleName);
            id = LegacyIdMap.LEGACY_ID_TO_ID.get(moduleName);
        } else {
            try{
                id = Identifier.tryParse(idString);
            } catch (Exception e){
                Betterf3.LOGGER.warn("Failed parsing identifier '{}', skipping...", idString);
            }
        }


        BaseModule baseModule = ModuleRegistry.getInstance().createInstance(id);

        if(baseModule == null){
            Betterf3.LOGGER.warn("No module found with name '{}', skipping...", idString);
            return null;
        }

        Config lines = moduleConfig.getOrElse("lines", () -> null);

        if (lines != null) {
            for (Config.Entry e : lines.entrySet()) {
                DebugLine line = baseModule.getLine(e.getKey());

                if (line != null) {
                    line.enabled = e.getValue();
                }

            }
        }

        if (baseModule.defaultNameColor != null) {
            baseModule.nameColor = moduleConfig.getOrElse("name_color", baseModule.defaultNameColor);
        }
        if (baseModule.defaultValueColor != null) {
            baseModule.valueColor = moduleConfig.getOrElse("value_color", baseModule.defaultNameColor);
        }

        if (baseModule instanceof CoordsModule coordsModule) {

            coordsModule.colorX = moduleConfig.getOrElse("color_x", coordsModule.defaultColorX);
            coordsModule.colorY = moduleConfig.getOrElse("color_y", coordsModule.defaultColorY);
            coordsModule.colorZ = moduleConfig.getOrElse("color_z", coordsModule.defaultColorZ);
        }

        if (baseModule instanceof FpsModule fpsModule) {

            fpsModule.colorHigh = moduleConfig.getOrElse("color_high", fpsModule.defaultColorHigh);
            fpsModule.colorMed = moduleConfig.getOrElse("color_med", fpsModule.defaultColorMed);
            fpsModule.colorLow = moduleConfig.getOrElse("color_low", fpsModule.defaultColorLow);
        }

        baseModule.enabled = moduleConfig.getOrElse("enabled", true);
        return baseModule;
    }

    private static Config saveModule(BaseModule module){
        Config moduleConfig = Config.inMemory();
        Config lines = Config.inMemory();

        for (DebugLine line : module.getLines()) {

            String lineId = line.getId();

            lines.set(lineId, line.enabled);
        }

        moduleConfig.set("id", module.id.toString());

        if (module.nameColor != null) {
            moduleConfig.set("name_color", module.nameColor);
        }
        if (module.valueColor != null) {
            moduleConfig.set("value_color", module.valueColor);
        }

        if (module instanceof CoordsModule coordsModule) {
            if (coordsModule.colorX != null) {
                moduleConfig.set("color_x", coordsModule.colorX);
            }
            if (coordsModule.colorY != null) {
                moduleConfig.set("color_y", coordsModule.colorY);
            }
            if (coordsModule.colorZ != null) {
                moduleConfig.set("color_z", coordsModule.colorZ);
            }
        }

        if (module instanceof FpsModule fpsModule) {
            if (fpsModule.colorHigh != null) {
                moduleConfig.set("color_high", fpsModule.colorHigh);
            }
            if (fpsModule.colorMed != null) {
                moduleConfig.set("color_med", fpsModule.colorMed);
            }
            if (fpsModule.colorLow != null) {
                moduleConfig.set("color_low", fpsModule.colorLow);
            }
        }

        moduleConfig.set("enabled", module.enabled);
        moduleConfig.set("lines", lines);

        return moduleConfig;
    }
}
