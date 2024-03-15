package ralf2oo2.betterf3.config.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import ralf2oo2.betterf3.config.gui.module.AddModuleScreen;
import ralf2oo2.betterf3.config.gui.module.EditModuleScreen;

import java.util.ArrayList;
import java.util.List;

public class ConfigListWidget extends EntryListWidget {
    private final Minecraft minecraft;
    private final EditModuleScreen editModuleScreen;
    private List<ConfigLineWidget> configLines;
    private int mouseX;
    private int mouseY;
    public ConfigListWidget(EditModuleScreen editModuleScreen, Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraft, width, height, top, bottom, itemHeight);
        this.minecraft = minecraft;
        this.editModuleScreen = editModuleScreen;
        configLines = new ArrayList<>();
    }

    @Override
    public void render(int mouseX, int mouseY, float f) {
        super.render(mouseX, mouseY, f);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    protected int getEntriesHeight() {
        return 20 * getEntryCount();
    }

    @Override
    protected int getEntryCount() {
        return (int)configLines.stream().count();
    }

    @Override
    protected void entryClicked(int index, boolean doubleClick) {

    }

    @Override
    protected boolean isSelectedEntry(int index) {
        return false;
    }

    @Override
    protected void renderBackground() {

    }

    @Override
    protected void renderEntry(int index, int x, int y, int l, Tessellator tesselator) {
        //configLines.get(index).render(0, y, mouseX, mouseY);
    }
}
