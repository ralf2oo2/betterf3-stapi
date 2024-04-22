package ralf2oo2.betterf3.config.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ralf2oo2.betterf3.config.gui.IConfigLineParentHandler;
import ralf2oo2.betterf3.config.gui.widgets.ConfigLineWidget;
import ralf2oo2.betterf3.modules.BaseModule;
import ralf2oo2.betterf3.modules.CoordsModule;
import ralf2oo2.betterf3.modules.FpsModule;
import ralf2oo2.betterf3.utils.DebugLine;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen implements IConfigLineParentHandler {
    protected List<ConfigLineWidget> configLines;
    private final Screen parent;
    public int listMarginTop = 50;
    public int listMarginBottom = 30;
    private float scrollAmount = 0f;
    private int focusedId = -1;
    public ConfigScreen(Screen parent){
        this.parent = parent;
    }

    @Override
    protected void keyPressed(char character, int keyCode) {
        if(focusedId != -1){
            ConfigLineWidget line = configLines.stream().filter(cfgline -> cfgline.id == focusedId).findFirst().orElse(null);
            if(line != null){
                configLines.get(configLines.indexOf(line)).keyPressed(character, keyCode);
            }
        }
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if(button.id == 0){
            minecraft.setScreen(parent);
        }
        if(button.id == 1){
            setFocusedId(-1);
            if(getInputErrors() == 0){
                for(ConfigLineWidget line : configLines){
                    line.saveChanges();
                }
                minecraft.setScreen(parent);
            }
        }
    }

    protected void registerConfigLines(){}


    //TODO: make buttons use lang file
    private void registerButtons(){
        ButtonWidget buttonCancel = new ButtonWidget(0, (this.width / 2) - 205, this.height - 25, 200, 20, "Cancel");
        this.buttons.add(buttonCancel);

        ButtonWidget buttonSave = new ButtonWidget(1, (this.width / 2) + 5, this.height - 25, 200, 20, "Save");
        this.buttons.add(buttonSave);
    }

    private int getInputErrors(){
        int inputErrors = 0;
        for(ConfigLineWidget configLine : configLines){
            if(configLine.inputError){
                inputErrors++;
            }
        }
        return inputErrors;
    }

    public void setFocusedId(int id){
        if(id == focusedId) return;
        if(focusedId != -1){
            ConfigLineWidget line = configLines.stream().filter(cfgline -> cfgline.id == focusedId).findFirst().orElse(null);
            if(line != null){
                configLines.get(configLines.indexOf(line)).processValue();
            }
        }
        focusedId = id;
    }

    @Override
    public int getFocusedId() {
        return focusedId;
    }

    private void handleMouseScrolling(){
        if(configLines.stream().count() * (20 + 4) < height - listMarginTop - listMarginBottom) return;
        if(configLines.stream().count() * (20 + 4) - (int)scrollAmount + listMarginTop < height - listMarginBottom){
            scrollAmount = configLines.stream().count() * (20 + 4) - (height - listMarginBottom - listMarginTop);
            return;
        }
        this.scrollAmount += -Mouse.getDWheel() * 0.1;
        if(this.scrollAmount < 0){
            this.scrollAmount = 0;
        }
    }

    @Override
    public void init() {
        super.init();
        configLines = new ArrayList<>();
        this.registerConfigLines();
        this.registerButtons();
        this.scrollAmount = 0;
    }

    public void renderDarkBackground(){
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
        renderDarkBackground();
        for(int i = 0; i <  configLines.size(); i++){
            configLines.get(i).render(0, i * (20 + 4) - (int)scrollAmount + listMarginTop, mouseX, mouseY, mouseY > listMarginTop && mouseY < height - listMarginBottom, delta);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
