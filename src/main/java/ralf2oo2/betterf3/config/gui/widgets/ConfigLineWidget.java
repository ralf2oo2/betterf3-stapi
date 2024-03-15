package ralf2oo2.betterf3.config.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import ralf2oo2.betterf3.mixin.DrawContextAccessor;

public class ConfigLineWidget {
    private final Screen parent;
    private final Minecraft minecraft;
    private final String title;
    private final String tooltip;
    private Object value;
    private Object defaultValue;

    public ConfigLineWidget(Screen parent, Minecraft minecraft, String title, String tooltip){
        this.parent = parent;
        this.minecraft = minecraft;
        this.title = title;
        this.tooltip = tooltip;
    }
    public void render(int x, int y, int mouseX, int mouseY, float delta){
        DrawContext drawContext = new DrawContext();
        int color = 0xFFFFFF;
        if(isInLine(x, y, mouseX, mouseY, parent.width)){
            ((DrawContextAccessor)drawContext).invokeFill(x, y, parent.width, 20, Integer.MIN_VALUE);
            renderTooltip(mouseX, mouseY);
        }
        drawContext.drawTextWithShadow(minecraft.textRenderer, title, x + 10, y + 7, color);
    }

    private void renderTooltip(int mouseX, int mouseY){
        DrawContext drawContext = new DrawContext();
        int textWidth = minecraft.textRenderer.getWidth(tooltip);
        ((DrawContextAccessor)drawContext).invokeFill(mouseX, mouseY - 10, textWidth + 10, 10, Integer.MIN_VALUE);
        drawContext.drawTextWithShadow(minecraft.textRenderer, tooltip, mouseX + 5, mouseY - 8, 0xFFFFFF);
    }

    private boolean isInLine(int x, int y, int mouseX, int mouseY, int width){
        return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 20);
    }

    public void setValue(Object value){
        this.value = value;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
