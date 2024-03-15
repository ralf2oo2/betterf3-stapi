package ralf2oo2.betterf3.config.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.CharacterUtils;
import net.modificationstation.stationapi.api.util.math.ColorHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ralf2oo2.betterf3.config.gui.IConfigLineParentHandler;
import ralf2oo2.betterf3.mixin.DrawContextAccessor;

import java.util.function.Consumer;

public class ConfigLineWidget {
    public final int id;
    private final Screen parent;
    private final Minecraft minecraft;
    private final int inputType;
    private final String title;
    private final String tooltip;
    private final Consumer<Object> saveConsumer;
    private int margin = 50;
    public boolean inputError = false;
    private boolean inputClicked = false;
    private Object value;
    private Object defaultValue;
    private String textBoxValue = "";
    private int textBoxMaxLength = 15;

    public ConfigLineWidget(int id, Screen parent, Minecraft minecraft, int inputType, String title, Consumer<Object> saveConsumer){
        this.id = id;
        this.parent = parent;
        this.minecraft = minecraft;
        this.inputType = inputType;
        this.title = title;
        this.saveConsumer = saveConsumer;
        this.tooltip = "remove this";
        switch (inputType){
            case 1:
                textBoxMaxLength = 6;
                break;
        }
    }

    public void processValue(){
        switch(inputType){
            case 0:
                break;
            case 1:
                inputError = false;
                try{
                    value = Integer.parseInt(textBoxValue, 16);
                } catch (Exception e){
                    inputError = true;
                }
                break;
        }
    }

    private void refreshInputs(){
        switch(inputType){
            case 1:
                textBoxValue = String.format("%02x", (int)value);
                break;
            default:
                break;
        }
    }

    private void resetValue(){
        this.value = this.defaultValue;
        refreshInputs();

    }
    public void render(int x, int y, int mouseX, int mouseY, float delta){
        DrawContext drawContext = new DrawContext();
        int color = 0xFFFFFF;
        if(inputError){
            color = 0xAA0000;
        }
        if(isInLine(x, y, mouseX, mouseY, parent.width)){
            //((DrawContextAccessor)drawContext).invokeFill(x, y, parent.width, 20, 0xFFFFFFFF);
            renderTooltip(mouseX, mouseY);
        }
        drawContext.drawTextWithShadow(minecraft.textRenderer, title, x + margin, y + 6, color);
        this.renderResetButton(y, mouseX, mouseY);
        this.renderInput(y, mouseX, mouseY);

    }

    private void renderTooltip(int mouseX, int mouseY){
        DrawContext drawContext = new DrawContext();
        int textWidth = minecraft.textRenderer.getWidth(tooltip);
        //((DrawContextAccessor)drawContext).invokeFill(mouseX, mouseY - 10, textWidth + 10, 10, Integer.MIN_VALUE);
        drawContext.drawTextWithShadow(minecraft.textRenderer, tooltip, mouseX + 5, mouseY - 8, 0xFFFFFF);
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

    private void renderResetButton(int y, int mouseX, int mouseY){
        DrawContext drawContext = new DrawContext();

        int width = 50;
        int height = 20;
        int buttonState = 1;
        if(value != null && defaultValue != null && value == defaultValue){
            buttonState = 0;
        }

        if(isInLine(parent.width - margin - width, y, mouseX, mouseY, width) && buttonState == 1){
            buttonState = 2;
            if(Mouse.isButtonDown(0)){
                resetValue();
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

    private void renderInput(int y, int mouseX, int mouseY){
        switch(inputType){
            case 0:
                renderBooleanInput(y, mouseX, mouseY);
                break;
            case 1:
            case 2:
                renderTextInput(y, mouseX, mouseY);
                break;
        }
    }
    private void renderBooleanInput(int y, int mouseX, int mouseY){
        int width = 104;
        int height = 20;

        int buttonState = 1;

        int x = parent.width - margin - 50 - 2 - width;

        boolean isInBox = isInLine(x, y - 2, mouseX, mouseY, width);
        if(isInBox){
            buttonState = 2;
        }

        if(Mouse.isButtonDown(0) && isInBox){
            if(!inputClicked){
                inputClicked = true;
                ((IConfigLineParentHandler)parent).setFocusedId(this.id);
                this.value = !(boolean)value;
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
    private void renderTextInput(int y, int mouseX, int mouseY){
        DrawContext drawContext = new DrawContext();
        y = y + 2;
        int width = 100;
        int height = 16;

        int x = parent.width - margin - 50 - 4 - width;
        boolean isInBox = isInLine(x, y - 2, mouseX, mouseY, width);

        if(Mouse.isButtonDown(0) && isInBox){
            ((IConfigLineParentHandler)parent).setFocusedId(this.id);
        }

        ((DrawContextAccessor)drawContext).invokeFill(x- 1, y - 1, x + width + 1, y + height + 1, -6250336);
        ((DrawContextAccessor)drawContext).invokeFill(x, y, x + width, y + height, -16777216);

        drawContext.drawTextWithShadow(minecraft.textRenderer, (inputType == 1 ? "#" : "") + textBoxValue + (((IConfigLineParentHandler)parent).getFocusedId() == this.id ? "_" : ""), x + 4, y + (height - 8) / 2, (inputError ? 0xAA0000 : 14737632));

        if(inputType == 1){
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
            GL11.glColor3f(red, green, blue);
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
}
