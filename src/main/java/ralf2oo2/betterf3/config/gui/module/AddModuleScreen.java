package ralf2oo2.betterf3.config.gui.module;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import ralf2oo2.betterf3.config.gui.widgets.DropdownWidget;
import ralf2oo2.betterf3.utils.IOnCloseHandler;

public class AddModuleScreen extends Screen {
    private final ModulesScreen parent;
    DropdownWidget dropdownWidget;

    public AddModuleScreen(ModulesScreen parent){
        this.parent = parent;
    }
    @Override
    public void init() {
        super.init();
        registerButtons();
        dropdownWidget = new DropdownWidget(this, minecraft, 200, 200, this.width / 2 - 100, 10);
    }

    private void registerButtons(){
        ButtonWidget saveButton = new ButtonWidget(0, this.width / 2 - 100, this.height - 100, 200, 20, TranslationStorage.getInstance().get("config-betterf3-modules-add_button"));
        this.buttons.add(saveButton);
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if(button.id == 0){
            try{
                parent.modulesListWidget.addModule(dropdownWidget.getSelectedModule().getClass().newInstance());
            } catch (InstantiationException | IllegalAccessException e){
                parent.modulesListWidget.addModule(dropdownWidget.getSelectedModule());
            }
            ((IOnCloseHandler)parent).onClose();
            minecraft.setScreen(parent);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        super.render(mouseX, mouseY, delta);
        dropdownWidget.render(mouseX, mouseY, delta);
    }
}
