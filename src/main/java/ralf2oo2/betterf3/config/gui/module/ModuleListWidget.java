package ralf2oo2.betterf3.config.gui.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import ralf2oo2.betterf3.modules.BaseModule;
import ralf2oo2.betterf3.modules.CoordsModule;
import ralf2oo2.betterf3.utils.ITextRenderer;
import ralf2oo2.betterf3.utils.Text;
import ralf2oo2.betterf3.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ModuleListWidget extends EntryListWidget {
    ModulesScreen screen;
    int selectedModuleIndex = -1;
    List<ModuleEntry> moduleEntries = new ArrayList();

    public ModuleListWidget(ModulesScreen screen, Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraft, width, height, top, bottom, itemHeight);
        this.screen = screen;
    }

    public void setModules(List<BaseModule> modules){
        this.moduleEntries.clear();

        for(BaseModule module : modules) {
            addModule(module);
        }
    }

    public void addModule(BaseModule module){
        ModuleEntry entry = new ModuleEntry(this.screen, module);
        this.moduleEntries.add(entry);
    }

    public void removeModule(int index){
        ModuleEntry entry = this.moduleEntries.get(index);
        this.moduleEntries.remove(entry);
    }

    @Override
    protected int getEntryCount() {
        return (int)moduleEntries.stream().count();
    }

    @Override
    protected int getEntriesHeight() {
        return getEntryCount() * 36;
    }

    @Override
    protected void entryClicked(int index, boolean doubleClick) {
        selectedModuleIndex = index;
    }

    @Override
    protected boolean isSelectedEntry(int index) {
        return index == selectedModuleIndex;
    }

    @Override
    protected void renderBackground() {

    }

    @Override
    protected void renderEntry(int index, int x, int y, int l, Tessellator tesselator) {
        moduleEntries.get(index).render(x, y, l, tesselator);
    }

    public class ModuleEntry{
        private final ModulesScreen screen;
        private final Minecraft minecraft;
        public final BaseModule module;

        protected ModuleEntry(ModulesScreen screen, BaseModule module) {
            this.screen = screen;
            this.module = module;
            this.minecraft = Utils.getMc();
        }

        public void render(int x, int y, int l, Tessellator tesselator){
            DrawContext drawContext = new DrawContext();
            minecraft.textRenderer.draw(this.module.toString(), x + 32 + 3, y + 1, 0xffffff);
            Text exampleText = new Text("");

            //TODO: add check for fps module
            if (this.module instanceof CoordsModule){
                CoordsModule coordsModule = (CoordsModule) this.module;
                exampleText = Utils.getStyledText("X", coordsModule.colorX)
                        .append(Utils.getStyledText("Y", coordsModule.colorY).getSections())
                        .append(Utils.getStyledText("Z", coordsModule.colorZ).getSections())
                        .append(Utils.getStyledText(": ", coordsModule.nameColor).getSections())
                        .append(Utils.getStyledText("100 ", coordsModule.colorX).getSections())
                        .append(Utils.getStyledText("200 ", coordsModule.colorY).getSections())
                        .append(Utils.getStyledText("300", coordsModule.colorZ).getSections());
            } else if (this.module.nameColor != 0 && this.module.valueColor != 0){
                exampleText = Utils.getStyledText("Name: ", this.module.nameColor)
                        .append(Utils.getStyledText("Value", this.module.valueColor).getSections());
            }
            ((ITextRenderer)minecraft.textRenderer).drawMultiColorString(exampleText, x + 40 + 3, y + 13, false);

            //TODO: figure out check for if statement
            if(true){
                minecraft.textureManager.bindTexture(minecraft.textureManager.getTextureId("/assets/betterf3/gui/arrows.png"));
                if(selectedModuleIndex > 0){
                    //TODO: change to mouse pos check
                    if(true){
                        drawContext.drawTexture(x, y, 96, 32, 32, 32);
                    } else {
                        drawContext.drawTexture(x, y, 96, 0, 32, 32);
                    }
                }
            }
        }
    }
}
