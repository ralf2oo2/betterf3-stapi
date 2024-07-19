package ralf2oo2.betterf3.config.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.TranslationStorage;
import ralf2oo2.betterf3.config.GeneralOptions;
import ralf2oo2.betterf3.config.ModConfigFile;
import ralf2oo2.betterf3.config.gui.widgets.ConfigLineWidget;
import ralf2oo2.betterf3.utils.InputTypeEnum;

public class GeneralConfigScreen extends ConfigScreen{
    public GeneralConfigScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected void registerConfigLines() {
        ConfigLineWidget disableMod = new ConfigLineWidget(0, this, minecraft, InputTypeEnum.BOOLEAN, TranslationStorage.getInstance().get("config-betterf3-disable"),
                newValue -> {
                    GeneralOptions.disableMod = (boolean)newValue;
                });
        disableMod.setDefaultValue(false);
        disableMod.setValue(GeneralOptions.disableMod);
        configLines.add(disableMod);

        ConfigLineWidget spaceModules = new ConfigLineWidget(1, this, minecraft, InputTypeEnum.BOOLEAN, TranslationStorage.getInstance().get("config-betterf3-space_modules"),
                newValue -> {
                    GeneralOptions.spaceEveryModule = (boolean)newValue;
                });
        spaceModules.setDefaultValue(false);
        spaceModules.setValue(GeneralOptions.spaceEveryModule);
        configLines.add(spaceModules);

        ConfigLineWidget shadowText = new ConfigLineWidget(2, this, minecraft, InputTypeEnum.BOOLEAN, TranslationStorage.getInstance().get("config-betterf3-shadow_text"),
                newValue -> {
                    GeneralOptions.shadowText = (boolean)newValue;
                });
        shadowText.setDefaultValue(true);
        shadowText.setValue(GeneralOptions.shadowText);
        configLines.add(shadowText);

        ConfigLineWidget animations = new ConfigLineWidget(3, this, minecraft, InputTypeEnum.BOOLEAN, TranslationStorage.getInstance().get("config-betterf3-animations"),
                newValue -> {
                    GeneralOptions.enableAnimations = (boolean)newValue;
                });
        animations.setDefaultValue(true);
        animations.setValue(GeneralOptions.enableAnimations);
        configLines.add(animations);

        ConfigLineWidget animationSpeed = new ConfigLineWidget(4, this, minecraft, InputTypeEnum.DOUBLE, TranslationStorage.getInstance().get("config-betterf3-animationSpeed"),
                newValue -> {
                    GeneralOptions.animationSpeed = (double)newValue;
                });
        animationSpeed.setDefaultValue(1.0d);
        animationSpeed.setValue(GeneralOptions.animationSpeed);
        animationSpeed.setMinimumValue(1.0d);
        animationSpeed.setMaximumValue(3.0d);
        configLines.add(animationSpeed);

        ConfigLineWidget enableBackground = new ConfigLineWidget(5, this, minecraft, InputTypeEnum.BOOLEAN, TranslationStorage.getInstance().get("config-betterf3-show_background"),
                newValue -> {
                    GeneralOptions.showBackground = (boolean)newValue;
                });
        enableBackground.setDefaultValue(true);
        enableBackground.setValue(GeneralOptions.showBackground);
        configLines.add(enableBackground);

        ConfigLineWidget backgroundColor = new ConfigLineWidget(6, this, minecraft, InputTypeEnum.RGBA, TranslationStorage.getInstance().get("config-betterf3-color-background"),
                newValue -> {
                    GeneralOptions.backgroundColor = (int)newValue;
                });
        backgroundColor.setDefaultValue(0x6F505050);
        backgroundColor.setValue(GeneralOptions.backgroundColor);
        configLines.add(backgroundColor);

        ConfigLineWidget disableLagometer = new ConfigLineWidget(7, this, minecraft, InputTypeEnum.BOOLEAN, TranslationStorage.getInstance().get("config.betterf3.disable_lagometer"),
                newValue -> {
                    GeneralOptions.disableLagometer = (boolean)newValue;
                });
        disableLagometer.setDefaultValue(true);
        disableLagometer.setValue(GeneralOptions.disableLagometer);
        configLines.add(disableLagometer);
    }

    @Override
    protected void save() {
        super.save();
        ModConfigFile.saveRunnable.run();
    }
}
