package ralf2oo2.betterf3.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import ralf2oo2.betterf3.modules.BaseModule;
import ralf2oo2.betterf3.modules.CoordsModule;
import ralf2oo2.betterf3.modules.EmptyModule;
import ralf2oo2.betterf3.modules.FpsModule;
import ralf2oo2.betterf3.utils.DebugLine;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ModConfigFile {
    public static Runnable saveRunnable = () -> {
        FileConfig config = FileConfig.builder(Paths.get("config/betterf3/modules.json")).concurrent().autosave().build();

        List<Config> configsLeft = new ArrayList<>();

        for (BaseModule module : BaseModule.modules) {

            Config moduleConfig = saveModule(module);

            configsLeft.add(moduleConfig);
        }

        List<Config> configsRight = new ArrayList<>();

        for (BaseModule module : BaseModule.modulesRight) {

            Config moduleConfig = saveModule(module);

            configsRight.add(moduleConfig);
        }

        config.set("modules_left", configsLeft);
        config.set("modules_right", configsRight);
    };

    public static void load(){
        File file = new File("config/betterf3/modules.json");

        if (!file.exists()) {
            return;
        }

        FileConfig config = FileConfig.builder(file).concurrent().autosave().build();

        config.load();

        List<BaseModule> modulesLeft = new ArrayList<>();
        List<BaseModule> modulesRight = new ArrayList<>();

        List<Config> modulesLeftConfig = config.getOrElse("modules_left", () -> null);

        if (modulesLeftConfig != null) {

            for (Config moduleConfig : modulesLeftConfig) {
                String moduleName = moduleConfig.getOrElse("name", null);

                if (moduleName == null) {
                    continue;
                }

                BaseModule baseModule = ModConfigFile.loadModule(moduleConfig);

                modulesLeft.add(baseModule);
            }
        }

        if (!modulesLeft.isEmpty()) {
            BaseModule.modules = modulesLeft;
        }

        List<Config> modulesRightConfig = config.getOrElse("modules_right", () -> null);

        if (modulesRightConfig != null) {
            for (Config moduleConfig : modulesRightConfig) {

                String moduleName = moduleConfig.getOrElse("name", () -> null);

                if (moduleName == null) {
                    continue;
                }

                BaseModule baseModule = ModConfigFile.loadModule(moduleConfig);

                modulesRight.add(baseModule);
            }
        }

        if (!modulesRight.isEmpty()) {
            BaseModule.modulesRight = modulesRight;
        }

        config.close();
    }

    private static BaseModule loadModule(Config moduleConfig){
        String moduleName = moduleConfig.getOrElse("name", null);

        BaseModule baseModule;
        try {
            baseModule = BaseModule.getModuleById(moduleName).getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException | NullPointerException e) {
            baseModule = EmptyModule.INSTANCE;
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

        if (baseModule instanceof CoordsModule) {

            CoordsModule coordsModule = (CoordsModule) baseModule;

            coordsModule.colorX = moduleConfig.getOrElse("color_x", coordsModule.defaultColorX);
            coordsModule.colorY = moduleConfig.getOrElse("color_y", coordsModule.defaultColorY);
            coordsModule.colorZ = moduleConfig.getOrElse("color_z", coordsModule.defaultColorZ);
        }

        if (baseModule instanceof FpsModule) {

            FpsModule fpsModule = (FpsModule) baseModule;

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

        moduleConfig.set("name", module.id);

        if (module.nameColor != null) {
            moduleConfig.set("name_color", module.nameColor);
        }
        if (module.valueColor != null) {
            moduleConfig.set("value_color", module.valueColor);
        }

        if (module instanceof CoordsModule) {
            CoordsModule coordsModule = (CoordsModule) module;
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

        if (module instanceof FpsModule) {
            FpsModule fpsModule = (FpsModule) module;
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
