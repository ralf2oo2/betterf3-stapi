package ralf2oo2.betterf3.config.gui.module;

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

import java.util.ArrayList;
import java.util.List;

public class EditModuleScreen extends Screen implements IConfigLineParentHandler {
    private List<ConfigLineWidget> configLines;
    private final Screen parent;
    private final BaseModule module;
    public int listMarginTop = 50;
    public int listMarginBottom = 30;
    private float scrollAmount = 0f;
    private int focusedId = -1;
    public EditModuleScreen(Screen parent, BaseModule module){
        this.parent = parent;
        this.module = module;
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
                minecraft.setScreen(parent);
            }
        }
    }

    private void registerConfigLines(){
        ConfigLineWidget moduleEnabled = new ConfigLineWidget(0, this, minecraft, 0, TranslationStorage.getInstance().get("config-betterf3-module-enable"),
                newValue -> {
                    module.enabled = (boolean)newValue;
                    module.setEnabled((boolean)newValue);
                });
        moduleEnabled.setDefaultValue(true);
        moduleEnabled.setValue(module.enabled);
        configLines.add(moduleEnabled);
        if(module instanceof CoordsModule){
            CoordsModule coordsModule = (CoordsModule) module;

            if (coordsModule.colorX != 0 && coordsModule.defaultColorX != 0) {
                ConfigLineWidget colorX = new ConfigLineWidget(1, this, minecraft, 1, TranslationStorage.getInstance().get("config-betterf3-color-x"),
                        newValue -> {
                            coordsModule.colorX = (int)newValue;
                        });
                colorX.setDefaultValue(coordsModule.defaultColorX);
                colorX.setValue(coordsModule.colorX);
                configLines.add(colorX);
            }
            if (coordsModule.colorY != 0 && coordsModule.defaultColorY != 0) {
                ConfigLineWidget colorY = new ConfigLineWidget(2, this, minecraft, 1, TranslationStorage.getInstance().get("config-betterf3-color-y"),
                        newValue -> {
                            coordsModule.colorY = (int)newValue;
                        });
                colorY.setDefaultValue(coordsModule.defaultColorY);
                colorY.setValue(coordsModule.colorY);
                configLines.add(colorY);
            }
            if (coordsModule.colorZ != 0 && coordsModule.defaultColorZ != 0) {
                ConfigLineWidget colorZ = new ConfigLineWidget(3, this, minecraft, 1, TranslationStorage.getInstance().get("config-betterf3-color-z"),
                        newValue -> {
                            coordsModule.colorZ = (int)newValue;
                        });
                colorZ.setDefaultValue(coordsModule.defaultColorZ);
                colorZ.setValue(coordsModule.colorZ);
                configLines.add(colorZ);
            }
        } else if(module instanceof FpsModule) {
            FpsModule fpsModule = (FpsModule)module;
            if (fpsModule.colorHigh != 0 && fpsModule.defaultColorHigh != 0) {
                ConfigLineWidget colorHigh = new ConfigLineWidget(1, this, minecraft, 1, TranslationStorage.getInstance().get("config-betterf3-color-fps-high"),
                        newValue -> {
                            fpsModule.colorHigh = (int)newValue;
                        });
                colorHigh.setDefaultValue(fpsModule.defaultColorHigh);
                colorHigh.setValue(fpsModule.colorHigh);
                configLines.add(colorHigh);
            }
            if (fpsModule.colorMed != 0 && fpsModule.defaultColorMed != 0) {
                ConfigLineWidget colorMed = new ConfigLineWidget(2, this, minecraft, 1, TranslationStorage.getInstance().get("config-betterf3-color-fps-medium"),
                        newValue -> {
                            fpsModule.colorMed = (int)newValue;
                        });
                colorMed.setDefaultValue(fpsModule.defaultColorMed);
                colorMed.setValue(fpsModule.colorMed);
                configLines.add(colorMed);
            }
            if (fpsModule.colorLow != 0 && fpsModule.defaultColorLow != 0) {
                ConfigLineWidget colorLow = new ConfigLineWidget(3, this, minecraft, 1, TranslationStorage.getInstance().get("config-betterf3-color-fps-low"),
                        newValue -> {
                            fpsModule.colorLow = (int)newValue;
                        });
                colorLow.setDefaultValue(fpsModule.defaultColorLow);
                colorLow.setValue(fpsModule.colorLow);
                configLines.add(colorLow);
            }
        }

        if (module.nameColor != -1 && module.defaultNameColor != -1) {
            ConfigLineWidget nameColor = new ConfigLineWidget(4, this, minecraft, 1, TranslationStorage.getInstance().get("config-betterf3-color-name"),
                    newValue -> {
                        module.nameColor = (int)newValue;
                    });
            nameColor.setDefaultValue(module.defaultNameColor);
            nameColor.setValue(module.nameColor);
            configLines.add(nameColor);
        }

        if (module.valueColor != -1 && module.defaultValueColor != -1) {
            ConfigLineWidget valueColor = new ConfigLineWidget(5, this, minecraft, 1, TranslationStorage.getInstance().get("config-betterf3-color-value"),
                    newValue -> {
                        module.valueColor = (int)newValue;
                    });
            valueColor.setDefaultValue(module.defaultValueColor);
            valueColor.setValue(module.valueColor);
            configLines.add(valueColor);
        }
    }


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
            configLines.get(i).render(0, i * (20 + 4) - (int)scrollAmount + listMarginTop, mouseX, mouseY, delta);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
