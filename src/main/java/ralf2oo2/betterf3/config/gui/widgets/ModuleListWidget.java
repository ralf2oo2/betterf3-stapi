package ralf2oo2.betterf3.config.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ralf2oo2.betterf3.config.gui.module.ModulesScreen;
import ralf2oo2.betterf3.modules.BaseModule;
import ralf2oo2.betterf3.modules.CoordsModule;
import ralf2oo2.betterf3.modules.FpsModule;
import ralf2oo2.betterf3.utils.ITextRenderer;
import ralf2oo2.betterf3.utils.Text;
import ralf2oo2.betterf3.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ModuleListWidget extends EntryListWidget {
    ModulesScreen screen;
    Minecraft minecraft;
    public int selectedModuleIndex = -1;
    int mouseX = 0;
    int mouseY = 0;
    boolean hasClickedUp = false;
    boolean hasClickedDown = false;
    public List<ModuleEntry> moduleEntries = new ArrayList();

    public ModuleListWidget(ModulesScreen screen, Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraft, width, height, top, bottom, itemHeight);
        this.screen = screen;
        this.minecraft = minecraft;
    }

    public void setModules(List<BaseModule> modules){
        this.moduleEntries.clear();

        for(BaseModule module : modules) {
            addModule(module);
        }
    }

    public void addModule(BaseModule module){
        ModuleEntry entry = new ModuleEntry(this, this.screen, module);
        this.moduleEntries.add(entry);
    }

    public void removeModule(int index){
        ModuleEntry entry = this.moduleEntries.get(index);
        this.moduleEntries.remove(entry);
    }

    private void swapEntries(int i, int j){
        ModuleEntry temp = moduleEntries.get(i);
        moduleEntries.set(i, moduleEntries.get(j));
        moduleEntries.set(j, temp);

        selectedModuleIndex = j;
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

    public void upClicked(int index){
        System.out.println(index);
        swapEntries(index, index - 1);
    }

    public void downClicked(int index){
        System.out.println(index);
        swapEntries(index, index + 1);
    }

    @Override
    protected boolean isSelectedEntry(int index) {
        return index == selectedModuleIndex;
    }

    @Override
    protected void renderBackground() {

    }

    @Override
    public void render(int mouseX, int mouseY, float f) {
        super.render(mouseX, mouseY, f);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    protected void renderEntry(int index, int x, int y, int l, Tessellator tesselator) {
        moduleEntries.get(index).render(index, x, y, l, tesselator);
    }

    public class ModuleEntry{
        private final ModuleListWidget list;
        private final ModulesScreen screen;
        private final Minecraft minecraft;
        public final BaseModule module;

        protected ModuleEntry(ModuleListWidget list, ModulesScreen screen, BaseModule module) {
            this.list = list;
            this.screen = screen;
            this.module = module;
            this.minecraft = Utils.getMc();
        }

        public void render(int index, int x, int y, int l, Tessellator tesselator){
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
            } else if(this.module instanceof FpsModule){
                FpsModule fpsModule = (FpsModule) this.module;
                exampleText = Utils.getStyledText("60 fps ", fpsModule.colorHigh)
                        .append(Utils.getStyledText("40 fps ", fpsModule.colorMed).getSections())
                        .append(Utils.getStyledText("10 fps", fpsModule.colorLow).getSections());
            } else if (this.module.nameColor != null && this.module.valueColor != null){
                exampleText = Utils.getStyledText("Name: ", this.module.nameColor)
                        .append(Utils.getStyledText("Value", this.module.valueColor).getSections());
            }
            ((ITextRenderer)minecraft.textRenderer).drawMultiColorString(exampleText, x + 40 + 3, y + 13, false);

            DrawContext drawContext = new DrawContext();
            //TODO: figure out check for if statement
            if(index == selectedModuleIndex){
                minecraft.textureManager.bindTexture(minecraft.textureManager.getTextureId("/assets/betterf3/gui/gui.png"));
                GL11.glColor3f(1f, 1f, 1f);
                int v = mouseX - x;
                int w = mouseY - y;
                if(index > 0){
                    //TODO: change to mouse pos check
                    if(v < 14 && v > 2 && w < 11 && w > 4){
                        if(Mouse.isButtonDown(0)){
                            if(!hasClickedUp){
                                hasClickedUp = true;
                                upClicked(index);
                                System.out.println("Clicked");
                            }
                        } else {
                            hasClickedUp = false;
                        }
                        drawContext.drawTexture(x, y, 96, 32, 32, 32);
                    } else {
                        drawContext.drawTexture(x, y, 96, 0, 32, 32);
                    }
                }
                if(index < moduleEntries.size() - 1){
                    if(v < 14 && v > 2 && w > 20 && w < 27){
                        if(Mouse.isButtonDown(0)){
                            if(!hasClickedDown){
                                hasClickedDown = true;
                                downClicked(index);
                                System.out.println("Clicked");
                            }
                        } else {
                            hasClickedDown = false;
                        }
                        drawContext.drawTexture(x, y, 64, 32, 32, 32);
                    } else {
                        drawContext.drawTexture(x, y, 64, 0, 32, 32);
                    }
                }
            }
        }
    }
}
