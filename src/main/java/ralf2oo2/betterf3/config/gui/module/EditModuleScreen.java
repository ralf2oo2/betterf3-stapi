package ralf2oo2.betterf3.config.gui.module;

import net.minecraft.client.gui.screen.Screen;
import org.checkerframework.checker.units.qual.C;
import ralf2oo2.betterf3.config.gui.widgets.ConfigLineWidget;

import java.util.ArrayList;
import java.util.List;

public class EditModuleScreen extends Screen {
    private List<ConfigLineWidget> configLines;
    private final Screen parent;
    public EditModuleScreen(Screen parent){
        this.parent = parent;
    }

    private void registerConfigLines(){
        ConfigLineWidget testConfigLine = new ConfigLineWidget(this, minecraft, "Title", "This is a tooltip");
        configLines.add(testConfigLine);
    }

    @Override
    public void init() {
        super.init();
        configLines = new ArrayList<>();
        registerConfigLines();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(0);
        super.render(mouseX, mouseY, delta);
        for(int i = 0; i <  configLines.size(); i++){
            configLines.get(i).render(0, 0, mouseX, mouseY, delta);
        }
    }
}
