package ralf2oo2.betterf3.config.gui.module;

import net.minecraft.client.gui.screen.Screen;
import ralf2oo2.betterf3.modules.BaseModule;
import ralf2oo2.betterf3.utils.PositionEnum;

public class ModulesScreen extends Screen {
    Screen parent;
    ModuleListWidget modulesListWidget;
    private boolean initialized = false;
    public PositionEnum side;

    public ModulesScreen(Screen parent, PositionEnum side){
        this.parent = parent;
        this.side = side;
    }

    @Override
    public void init() {
        super.init();

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
    }
}
