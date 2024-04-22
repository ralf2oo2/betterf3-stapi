package ralf2oo2.betterf3.config.gui.module;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.TranslationStorage;
import ralf2oo2.betterf3.config.gui.ConfigScreen;
import ralf2oo2.betterf3.config.gui.widgets.ConfigLineWidget;
import ralf2oo2.betterf3.modules.BaseModule;
import ralf2oo2.betterf3.modules.CoordsModule;
import ralf2oo2.betterf3.modules.FpsModule;
import ralf2oo2.betterf3.utils.DebugLine;
import ralf2oo2.betterf3.utils.InputTypeEnum;

public class EditModuleConfigScreen extends ConfigScreen {
    private final BaseModule module;
    public EditModuleConfigScreen(Screen parent, BaseModule module) {
        super(parent);
        this.module = module;
    }

    @Override
    protected void registerConfigLines() {
        ConfigLineWidget moduleEnabled = new ConfigLineWidget(0, this, minecraft, InputTypeEnum.BOOLEAN, TranslationStorage.getInstance().get("config-betterf3-module-enable"),
                newValue -> {
                    module.enabled = (boolean)newValue;
                    module.setEnabled((boolean)newValue);
                });
        moduleEnabled.setDefaultValue(true);
        moduleEnabled.setValue(module.enabled);
        configLines.add(moduleEnabled);
        if(module instanceof CoordsModule){
            CoordsModule coordsModule = (CoordsModule) module;

            if (coordsModule.colorX != null && coordsModule.defaultColorX != null) {
                ConfigLineWidget colorX = new ConfigLineWidget(1, this, minecraft, InputTypeEnum.RGB, TranslationStorage.getInstance().get("config-betterf3-color-x"),
                        newValue -> {
                            coordsModule.colorX = (int)newValue;
                        });
                colorX.setDefaultValue(coordsModule.defaultColorX);
                colorX.setValue(coordsModule.colorX);
                configLines.add(colorX);
            }
            if (coordsModule.colorY != null && coordsModule.defaultColorY != null) {
                ConfigLineWidget colorY = new ConfigLineWidget(2, this, minecraft, InputTypeEnum.RGB, TranslationStorage.getInstance().get("config-betterf3-color-y"),
                        newValue -> {
                            coordsModule.colorY = (int)newValue;
                        });
                colorY.setDefaultValue(coordsModule.defaultColorY);
                colorY.setValue(coordsModule.colorY);
                configLines.add(colorY);
            }
            if (coordsModule.colorZ != null && coordsModule.defaultColorZ != null) {
                ConfigLineWidget colorZ = new ConfigLineWidget(3, this, minecraft, InputTypeEnum.RGB, TranslationStorage.getInstance().get("config-betterf3-color-z"),
                        newValue -> {
                            coordsModule.colorZ = (int)newValue;
                        });
                colorZ.setDefaultValue(coordsModule.defaultColorZ);
                colorZ.setValue(coordsModule.colorZ);
                configLines.add(colorZ);
            }
        } else if(module instanceof FpsModule) {
            FpsModule fpsModule = (FpsModule)module;
            if (fpsModule.colorHigh != null && fpsModule.defaultColorHigh != null) {
                ConfigLineWidget colorHigh = new ConfigLineWidget(1, this, minecraft, InputTypeEnum.RGB, TranslationStorage.getInstance().get("config-betterf3-color-fps-high"),
                        newValue -> {
                            fpsModule.colorHigh = (int)newValue;
                        });
                colorHigh.setDefaultValue(fpsModule.defaultColorHigh);
                colorHigh.setValue(fpsModule.colorHigh);
                configLines.add(colorHigh);
            }
            if (fpsModule.colorMed != null && fpsModule.defaultColorMed != null) {
                ConfigLineWidget colorMed = new ConfigLineWidget(2, this, minecraft, InputTypeEnum.RGB, TranslationStorage.getInstance().get("config-betterf3-color-fps-medium"),
                        newValue -> {
                            fpsModule.colorMed = (int)newValue;
                        });
                colorMed.setDefaultValue(fpsModule.defaultColorMed);
                colorMed.setValue(fpsModule.colorMed);
                configLines.add(colorMed);
            }
            if (fpsModule.colorLow != null && fpsModule.defaultColorLow != null) {
                ConfigLineWidget colorLow = new ConfigLineWidget(3, this, minecraft, InputTypeEnum.RGB, TranslationStorage.getInstance().get("config-betterf3-color-fps-low"),
                        newValue -> {
                            fpsModule.colorLow = (int)newValue;
                        });
                colorLow.setDefaultValue(fpsModule.defaultColorLow);
                colorLow.setValue(fpsModule.colorLow);
                configLines.add(colorLow);
            }
        }

        if (module.nameColor != null && module.defaultNameColor != null) {
            ConfigLineWidget nameColor = new ConfigLineWidget(4, this, minecraft, InputTypeEnum.RGB, TranslationStorage.getInstance().get("config-betterf3-color-name"),
                    newValue -> {
                        module.nameColor = (int)newValue;
                    });
            nameColor.setDefaultValue(module.defaultNameColor);
            nameColor.setValue(module.nameColor);
            configLines.add(nameColor);
        }

        if (module.valueColor != null && module.defaultValueColor != null) {
            ConfigLineWidget valueColor = new ConfigLineWidget(5, this, minecraft, InputTypeEnum.RGB, TranslationStorage.getInstance().get("config-betterf3-color-value"),
                    newValue -> {
                        module.valueColor = (int)newValue;
                    });
            valueColor.setDefaultValue(module.defaultValueColor);
            valueColor.setValue(module.valueColor);
            configLines.add(valueColor);
        }

        if(module.getLines().size() > 1){
            int nextId = 6;
            for (DebugLine line : module.getLines()){
                if(line.getId().equals("")){
                    continue;
                }
                String name = TranslationStorage.getInstance().get("text-betterf3-line-" + line.getId());

                if(name.equals("")){
                    name = line.getId().replace("_", "");
                }

                ConfigLineWidget enabled = new ConfigLineWidget(nextId, this, minecraft, InputTypeEnum.BOOLEAN, name,
                        newValue -> {
                            line.enabled = (boolean)newValue;
                        });
                enabled.setDefaultValue(true);
                enabled.setValue(line.enabled);

                configLines.add(enabled);
                nextId++;
            }
        }
    }
}
