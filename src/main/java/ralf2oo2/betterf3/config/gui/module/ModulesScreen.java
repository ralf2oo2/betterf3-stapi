package ralf2oo2.betterf3.config.gui.module;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import ralf2oo2.betterf3.config.ModConfigFile;
import ralf2oo2.betterf3.config.gui.widgets.ModuleListWidget;
import ralf2oo2.betterf3.modules.BaseModule;
import ralf2oo2.betterf3.utils.IOnCloseHandler;
import ralf2oo2.betterf3.utils.PositionEnum;

public class ModulesScreen extends Screen implements IOnCloseHandler {
    Screen parent;
    ModuleListWidget modulesListWidget;
    private boolean initialized = false;
    public PositionEnum side;

    public ModulesScreen(Screen parent, PositionEnum side){
        this.parent = parent;
        this.side = side;
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (!button.active) {
            return;
        }
        // Delete button
        if(button.id == 0){
            if(this.modulesListWidget.selectedModuleIndex != -1){
                this.modulesListWidget.removeModule(this.modulesListWidget.selectedModuleIndex);
                this.modulesListWidget.selectedModuleIndex = -1;
            }
        }
        // Edit button
        if(button.id == 1){
            minecraft.setScreen(new EditModuleConfigScreen(this, this.modulesListWidget.moduleEntries.get(modulesListWidget.selectedModuleIndex).module));
        }
        // Add button
        if(button.id == 2){
            minecraft.setScreen(new AddModuleScreen(this));
        }
        // Done button
        if(button.id == 3){
            minecraft.setScreen(parent);
        }
    }

    private void setActiveState(boolean active){
        ((ButtonWidget)this.buttons.get(0)).active = active;
        ((ButtonWidget)this.buttons.get(1)).active = active;
    }

    public void registerButtons(){
        ButtonWidget deleteButton = new ButtonWidget(0, this.width / 2 - 154, this.height - 50, 100, 20, TranslationStorage.getInstance().get("config.betterf3.modules.delete_button"));
        deleteButton.active = false;
        this.buttons.add(deleteButton);

        ButtonWidget editButton = new ButtonWidget(1, this.width / 2 - 50, this.height - 50, 100, 20, TranslationStorage.getInstance().get("config.betterf3.modules.edit_button"));
        editButton.active = false;
        this.buttons.add(editButton);

        ButtonWidget addButton = new ButtonWidget(2, this.width / 2 + 4 + 50, this.height - 50, 100, 20, TranslationStorage.getInstance().get("config.betterf3.modules.add_button"));
        this.buttons.add(addButton);

        ButtonWidget doneButton = new ButtonWidget(3, this.width / 2 - 154, this.height - 30 + 4, 300 + 8, 20, TranslationStorage.getInstance().get("config.betterf3.modules.done_button"));
        this.buttons.add(doneButton);
    }

    @Override
    public void init() {
        super.init();
        registerButtons();

        this.initialized = true;
        this.modulesListWidget = new ModuleListWidget(this, minecraft, this.width, this.height, 32, this.height - 64, 36);

        if(this.side == PositionEnum.LEFT){
            this.modulesListWidget.setModules(BaseModule.modules);
        } else if (this.side == PositionEnum.RIGHT){
            this.modulesListWidget.setModules(BaseModule.modulesRight);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.modulesListWidget.render(mouseX, mouseY, delta);
        if(this.modulesListWidget.selectedModuleIndex != -1){
            this.setActiveState(true);
        } else {
            this.setActiveState(false);
        }
        super.render(mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.side == PositionEnum.LEFT) {
            BaseModule.modules.clear();
            for (ModuleListWidget.ModuleEntry entry : this.modulesListWidget.moduleEntries) {
                BaseModule.modules.add(entry.module);
            }
        } else if (this.side == PositionEnum.RIGHT) {
            BaseModule.modulesRight.clear();
            for (ModuleListWidget.ModuleEntry entry : this.modulesListWidget.moduleEntries) {
                BaseModule.modulesRight.add(entry.module);
            }
        }
        ModConfigFile.saveRunnable.run();
    }
}
