package ralf2oo2.betterf3.config.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import ralf2oo2.betterf3.config.gui.module.ModulesScreen;
import ralf2oo2.betterf3.utils.PositionEnum;

public class ModConfigScreen extends Screen {

    @Override
    public void init() {
        registerButtons();
    }

    public void registerButtons(){
        ButtonWidget leftModules = new ButtonWidget(0, this.width / 2 - 130, this.height/4, 120, 20, TranslationStorage.getInstance().get("config.bettef3.order_left_button"));
        this.buttons.add(leftModules);

        ButtonWidget rightModules = new ButtonWidget(1, this.width / 2 + 10, this.height/4, 120, 20, TranslationStorage.getInstance().get("config.bettef3.order_right_button"));
        this.buttons.add(rightModules);

        ButtonWidget generalSettings = new ButtonWidget(3, this.width / 2 - 130, this.height/4 - 24, 260, 20, TranslationStorage.getInstance().get("config.bettef3.general_settings"));
        this.buttons.add(generalSettings);

        ButtonWidget done = new ButtonWidget(2, this.width / 2 - 130, this.height - 50, 260, 20, TranslationStorage.getInstance().get("config.betterf3.modules.done_button"));
        this.buttons.add(done);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.drawCenteredTextWithShadow(this.textRenderer, TranslationStorage.getInstance().get("config.betterf3.title.config"), this.width / 2, 20, 16777215);
        super.render(mouseX, mouseY, delta);
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if(button.id == 0){
            minecraft.setScreen(new ModulesScreen(this, PositionEnum.LEFT));
        }
        if(button.id == 1){
            minecraft.setScreen(new ModulesScreen(this, PositionEnum.RIGHT));
        }
        if(button.id == 3){
            minecraft.setScreen(new GeneralConfigScreen(this));
        }
        if(button.id == 2){
            minecraft.setScreen(null);
        }
    }
}
