package ralf2oo2.betterf3.config.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.CharacterUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ralf2oo2.betterf3.config.gui.IConfigLineParentHandler;
import ralf2oo2.betterf3.mixin.DrawContextAccessor;
import ralf2oo2.betterf3.utils.InputTypeEnum;

import java.util.function.Consumer;

public class ConfigLineWidget {
    public final int id;
    private final Screen parent;
    private final Minecraft minecraft;
    private final InputTypeEnum inputType;
    private final String title;
    private final Consumer<Object> saveConsumer;
    private int margin = 50;
    public boolean inputError = false;
    private boolean inputClicked = false;
    private Object value;
    private Object defaultValue;
    private String textBoxValue = "";
    private int textBoxMaxLength = 15;
    private double minValue = 0d;
    private double maxValue = 100d;

    public ConfigLineWidget(int id, Screen parent, Minecraft minecraft, InputTypeEnum inputType, String title, Consumer<Object> saveConsumer){
        this.id = id;
        this.parent = parent;
        this.minecraft = minecraft;
        this.inputType = inputType;
        this.title = title;
        this.saveConsumer = saveConsumer;
        switch (inputType){
            case RGB:
                textBoxMaxLength = 6;
                break;
            case RGBA:
                textBoxMaxLength = 8;
        }
    }

    public void processValue(){
        switch(inputType){
            case BOOLEAN:
                break;
            case RGB:
            case RGBA:
                inputError = false;
                try{
                    value = Integer.parseInt(textBoxValue, 16);
                } catch (Exception e){
                    inputError = true;
                }
                break;
            case DOUBLE:
                inputError = false;
                try {
                    value = Double.parseDouble(textBoxValue);
                    if((double)value < minValue){
                        value = minValue;
                        refreshInputs();
                    }
                    if((double)value > maxValue){
                        value = maxValue;
                        refreshInputs();
                    }
                } catch (Exception e){
                    inputError = true;
                }
                break;
        }
    }

    private void refreshInputs(){
        switch(inputType){
            case RGB:
            case RGBA:
                textBoxValue = String.format("%02x", (int)value);
                break;
            case DOUBLE:
                textBoxValue = String.valueOf((double)value);
                break;
            default:
                break;
        }
    }

    private void resetValue(){
        this.value = this.defaultValue;
        refreshInputs();

    }
    public void render(int x, int y, int mouseX, int mouseY, boolean canClickControls, float delta){
        DrawContext drawContext = new DrawContext();
        int color = 0xFFFFFF;
        if(inputError){
            color = 0xAA0000;
        }
        if(isInLine(x, y, mouseX, mouseY, parent.width)){
            //((DrawContextAccessor)drawContext).invokeFill(x, y, parent.width, 20, 0xFFFFFFFF);
        }
        drawContext.drawTextWithShadow(minecraft.textRenderer, title, x + margin, y + 6, color);
        this.renderResetButton(y, mouseX, mouseY, canClickControls);
        this.renderInput(y, mouseX, mouseY, canClickControls);

    }

    private int getButtonStateTextColor(int buttonState){
        int color = 0xFFFFFF;
        switch (buttonState){
            case 0:
                color = -6250336;
                break;
            case 1:
                color = 14737632;
                break;
            case 2:
                color = 16777120;
                break;
        }
        return color;
    }

    private void renderResetButton(int y, int mouseX, int mouseY, boolean canClickControls){
        DrawContext drawContext = new DrawContext();

        int width = 50;
        int height = 20;
        int buttonState = 1;
        if(value != null && defaultValue != null && value.equals(defaultValue)){
            buttonState = 0;
        }

        if(isInLine(parent.width - margin - width, y, mouseX, mouseY, width) && buttonState == 1 && canClickControls){
            buttonState = 2;
            if(Mouse.isButtonDown(0)){
                resetValue();
                this.minecraft.soundManager.method_2009("random.click", 1.0F, 1.0F);
            }
        }

        GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("/gui/gui.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawContext.drawTexture(parent.width - margin - width, y, 0, 46 + buttonState * 20, width / 2, height);
        drawContext.drawTexture((parent.width - margin - width) + width / 2, y, 200 - width / 2, 46 + buttonState * 20, width / 2, height);

        drawContext.drawCenteredTextWithShadow(minecraft.textRenderer, "Reset", parent.width - margin - width / 2, y + 6, getButtonStateTextColor(buttonState));
    }

    public void keyPressed(char character, int keyCode) {
        if (((IConfigLineParentHandler)parent).getFocusedId() == this.id) {
            if (character == '\t') {
                this.parent.handleTab();
            }

            if (character == 22) {
                String var3 = Screen.getClipboard();
                if (var3 == null) {
                    var3 = "";
                }

                int var4 = 32 - this.textBoxValue.length();
                if (var4 > var3.length()) {
                    var4 = var3.length();
                }

                if (var4 > 0) {
                    this.textBoxValue = this.textBoxValue + var3.substring(0, var4);
                }
            }

            if (keyCode == 14 && this.textBoxValue.length() > 0) {
                this.textBoxValue = this.textBoxValue.substring(0, this.textBoxValue.length() - 1);
            }

            if (CharacterUtils.VALID_CHARACTERS.indexOf(character) >= 0 && (this.textBoxValue.length() < this.textBoxMaxLength || this.textBoxMaxLength == 0)) {
                this.textBoxValue = this.textBoxValue + character;
            }

        }
        processValue();
    }

    public void saveChanges(){
        if(!inputError){
            saveConsumer.accept(value);
        }
    }

    private void renderInput(int y, int mouseX, int mouseY, boolean canClickControls){
        switch(inputType){
            case BOOLEAN:
                renderBooleanInput(y, mouseX, mouseY, canClickControls);
                break;
            case RGB:
            case RGBA:
            case DOUBLE:
                renderTextInput(y, mouseX, mouseY, canClickControls);
                break;
        }
    }
    private void renderBooleanInput(int y, int mouseX, int mouseY, boolean canClickControls){
        int width = 104;
        int height = 20;

        int buttonState = 1;

        int x = parent.width - margin - 50 - 2 - width;

        boolean isInBox = isInLine(x, y - 2, mouseX, mouseY, width) && canClickControls;
        if(isInBox){
            buttonState = 2;
        }

        if(Mouse.isButtonDown(0) && isInBox){
            if(!inputClicked){
                inputClicked = true;
                ((IConfigLineParentHandler)parent).setFocusedId(this.id);
                this.value = !(boolean)value;
                this.minecraft.soundManager.method_2009("random.click", 1.0F, 1.0F);
            }
        } else {
            inputClicked = false;
        }

        DrawContext drawContext = new DrawContext();
        GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("/gui/gui.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawContext.drawTexture(x, y, 0, 46 + buttonState * 20, width / 2, height);
        drawContext.drawTexture(x + width / 2, y, 200 - width / 2, 46 + buttonState * 20, width / 2, height);

        drawContext.drawCenteredTextWithShadow(minecraft.textRenderer, (boolean)value ? "Yes" : "No", x + width / 2, y + 6, (boolean)value ? 0x55FF55 : 0xFF5555);
    }
    private void renderTextInput(int y, int mouseX, int mouseY, boolean canClickControls){
        DrawContext drawContext = new DrawContext();
        y = y + 2;
        int width = 100;
        int height = 16;

        int x = parent.width - margin - 50 - 4 - width;
        boolean isInBox = isInLine(x, y - 2, mouseX, mouseY, width);

        if(Mouse.isButtonDown(0) && isInBox && canClickControls){
            ((IConfigLineParentHandler)parent).setFocusedId(this.id);
        }

        ((DrawContextAccessor)drawContext).invokeFill(x- 1, y - 1, x + width + 1, y + height + 1, -6250336);
        ((DrawContextAccessor)drawContext).invokeFill(x, y, x + width, y + height, -16777216);

        drawContext.drawTextWithShadow(minecraft.textRenderer, (inputType == InputTypeEnum.RGB || inputType == InputTypeEnum.RGBA ? "#" : "") + textBoxValue + (((IConfigLineParentHandler)parent).getFocusedId() == this.id ? "_" : ""), x + 4, y + (height - 8) / 2, (inputError ? 0xAA0000 : 14737632));

        if(inputType == InputTypeEnum.RGB || inputType == InputTypeEnum.RGBA){
            minecraft.textureManager.bindTexture(minecraft.textureManager.getTextureId("/assets/betterf3/gui/gui.png"));
            GL11.glColor3f(1f, 1f, 1f);
            drawContext.drawTexture(x - 24, y - 2, 0, 0, 20, 20);
            int color = 0xFFFFFF;
            if(this.value != null){
                color = (int)value;
            }
            float red = (float)(color >> 16 & 255) / 255.0F;
            float green = (float)(color >> 8 & 255) / 255.0F;
            float blue = (float)(color & 255) / 255.0F;
            float alpha = (float)(color >> 24 & 255) / 255.0F;
            GL11.glColor4f(0, 0, 0, 0);
            drawContext.drawTexture(x - 24, y - 2, 20, 0, 20, 20);
            GL11.glColor4f(red, green, blue, alpha);
            drawContext.drawTexture(x - 24, y - 2, 20, 0, 20, 20);
        }
    }

    private boolean isInLine(int x, int y, int mouseX, int mouseY, int width){
        return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 20);
    }

    public void setValue(Object value){
        this.value = value;
        refreshInputs();
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setMinimumValue(double minValue){
        this.minValue = minValue;
    }

    public void setMaximumValue(double maxValue){
        this.maxValue = maxValue;
    }
}
