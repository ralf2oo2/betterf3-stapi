package ralf2oo2.betterf3.config.gui.module;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ralf2oo2.betterf3.config.gui.IConfigLineParentHandler;
import ralf2oo2.betterf3.config.gui.widgets.ConfigLineWidget;

import java.util.ArrayList;
import java.util.List;

public class EditModuleScreen extends Screen implements IConfigLineParentHandler {
    private List<ConfigLineWidget> configLines;
    private final Screen parent;
    public int listMarginTop = 50;
    public int listMarginBottom = 30;
    private float scrollAmount = 0f;
    private int focusedId = -1;
    public EditModuleScreen(Screen parent){
        this.parent = parent;
    }

    @Override
    protected void keyPressed(char character, int keyCode) {
        if(focusedId != -1){
            configLines.get(focusedId).keyPressed(character, keyCode);
        }
    }

    private void registerConfigLines(){
        ConfigLineWidget testConfigLine = new ConfigLineWidget(0, this, minecraft, 1,"Title", "This is a tooltip");
        testConfigLine.setDefaultValue(0xFF0000);
        testConfigLine.setValue(0xFFFF00);
        ConfigLineWidget testConfigLine2 = new ConfigLineWidget(1, this, minecraft, 0,"Title", "This is a tooltip");
        testConfigLine2.setDefaultValue(true);
        testConfigLine2.setValue(false);
        ConfigLineWidget testConfigLine3 = new ConfigLineWidget(2, this, minecraft, 1,"Title", "This is a tooltip");
        ConfigLineWidget testConfigLine4 = new ConfigLineWidget(3, this, minecraft, 1,"Title", "This is a tooltip");
        ConfigLineWidget testConfigLine5 = new ConfigLineWidget(4, this, minecraft, 1,"Title", "This is a tooltip");
        ConfigLineWidget testConfigLine6 = new ConfigLineWidget(5, this, minecraft, 1,"Title", "This is a tooltip");
        configLines.add(testConfigLine);
        configLines.add(testConfigLine2);
        configLines.add(testConfigLine3);
        configLines.add(testConfigLine4);
        configLines.add(testConfigLine5);
        configLines.add(testConfigLine6);

    }

    public void setFocusedId(int id){
        if(id == focusedId) return;
        if(focusedId != -1){
            configLines.get(focusedId).processValue();
        }
        focusedId = id;
    }

    @Override
    public int getFocusedId() {
        return focusedId;
    }

    private void handleMouseScrolling(){
        this.scrollAmount += -Mouse.getDWheel() * 0.1;
        if(this.scrollAmount < 0){
            this.scrollAmount = 0;
        }
    }

    @Override
    public void init() {
        super.init();
        configLines = new ArrayList<>();
        registerConfigLines();
    }

    public void renderDarkBackground(int vOFfset){
        Tessellator var16 = Tessellator.INSTANCE;
        GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var17 = 32.0F;
        var16.startQuads();
        var16.color(2105376);
        var16.vertex((double)0, (double)this.height - listMarginBottom, 0.0, (double)((float)0 / var17), (double)((float)(this.height - listMarginBottom + (int)this.scrollAmount) / var17));
        var16.vertex((double)this.width, (double)this.height - listMarginBottom, 0.0, (double)((float)this.width / var17), (double)((float)(this.height  - listMarginBottom + (int)this.scrollAmount) / var17));
        var16.vertex((double)this.width, (double)listMarginTop, 0.0, (double)((float)this.width / var17), (double)((float)(this.listMarginTop + (int)this.scrollAmount) / var17));
        var16.vertex((double)0, (double)listMarginBottom, 0.0, (double)((float)0 / var17), (double)((float)(this.listMarginBottom + (int)this.scrollAmount) / var17));
        var16.draw();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        handleMouseScrolling();
        renderBackgroundTexture(0);
        super.render(mouseX, mouseY, delta);

        float xScale = (float) minecraft.displayWidth / this.width;
        float yScale = (float) minecraft.displayHeight / this.height;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(0, (int)(listMarginBottom * yScale), (int)(minecraft.displayWidth), minecraft.displayHeight - (int)((listMarginTop + listMarginBottom) * yScale));
        renderDarkBackground((int)scrollAmount);
        for(int i = 0; i <  configLines.size(); i++){
            configLines.get(i).render(0, i * (20 + 4) - (int)scrollAmount + listMarginTop, mouseX, mouseY, delta);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
