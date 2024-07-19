package ralf2oo2.betterf3.config.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ralf2oo2.betterf3.modules.BaseModule;

import java.util.List;

public class DropdownWidget{
    private final Minecraft minecraft;
    private final Screen parent;
    private final int width;
    private final int height;
    private final int x;
    private final int y;
    private List<BaseModule> modules;
    private boolean clickedOpen = false;
    private int selectedIndex = 0;
    private float scrollAmount = 0f;
    public boolean isOpen = false;

    public DropdownWidget(Screen parent, Minecraft minecraft, int width, int height, int x, int y){
        this.parent = parent;
        this.minecraft = minecraft;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        this.modules = BaseModule.allModules;
    }

    public int getItemCount(){
        return (int) modules.stream().count();
    }

    public BaseModule getSelectedModule(){
        return modules.get(selectedIndex);
    }

    private void handleMouseScrolling(){
        this.scrollAmount += -Mouse.getDWheel() * 0.1;
        if(((getItemCount() + 1) * 20) < this.height - 20){
            this.scrollAmount = 0;
            return;
        }
        if(this.scrollAmount < 0){
            this.scrollAmount = 0;
        }
        if(this.scrollAmount > ((getItemCount() + 1) * 20) - this.height){
            this.scrollAmount = ((getItemCount() + 1) * 20) - this.height;
        }
    }

    public void render(int mouseX, int mouseY, float f){
        if(this.isOpen){
            handleMouseScrolling();
        }
        DrawContext drawContext = new DrawContext();
        TextRenderer textRenderer = minecraft.textRenderer;
        GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("/gui/gui.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        boolean isInBox = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + 20;

        drawContext.drawTexture(this.x, this.y, 0, 46 + (isInBox ? 2 : 1) * 20, this.width / 2, 20);
        drawContext.drawTexture(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + (isInBox ? 2 : 1) * 20, this.width / 2, 20);
        drawContext.drawCenteredTextWithShadow(minecraft.textRenderer, modules.get(selectedIndex).toString(), this.x + this.width / 2, this.y + 5, 0xFFFFFF);

        if(Mouse.isButtonDown(0) && isInBox){
            if(!clickedOpen){
                clickedOpen = true;
                if(!this.isOpen){
                    isOpen = true;
                    this.scrollAmount = 0;
                } else {
                    isOpen = false;
                }
                this.minecraft.soundManager.playSound("random.click", 1.0F, 1.0F);
            }
        } else {
            clickedOpen = false;
        }

        if(isOpen){
            float xScale = (float) minecraft.displayWidth / parent.width;
            float yScale = (float) minecraft.displayHeight / parent.height;
            GL11.glScissor((int)(x * xScale), (int)(minecraft.displayHeight - ((this.height + this.y) * yScale)), (int)(this.width * xScale), (int)((this.height - 20) * yScale));
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            for(int i = 1; i <= getItemCount(); i++){
                if(i * 20 - scrollAmount > 0 && (i * 20 - scrollAmount) < this.height){
                    GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("/gui/gui.png"));
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                    boolean isOnItem = mouseX >= this.x && mouseY >= (this.y + (i * 20) - (int)Math.floor(scrollAmount)) && mouseX < this.x + this.width && mouseY < (this.y + (i * 20) - (int)Math.floor(scrollAmount)) + (20) && !isInBox;

                    drawContext.drawTexture(this.x, (this.y + (i * 20) - (int)Math.floor(scrollAmount)), 0, (46 + (isOnItem ? 2 : 0) * 20), this.width / 2, 20);
                    drawContext.drawTexture(this.x + this.width / 2, (this.y + (i * 20) - (int)Math.floor(scrollAmount)), 200 - this.width / 2, (46 + (isOnItem ? 2 : 0) * 20), this.width / 2, 20);
                    if(true){
                        drawContext.drawCenteredTextWithShadow(minecraft.textRenderer, modules.get(i - 1).toString(), this.x + this.width / 2, (this.y + (i * 20) - (int)Math.floor(scrollAmount)) + 5, 0xFFFFFF);
                    }

                    if(Mouse.isButtonDown(0) && isOnItem){
                        selectedIndex = i - 1;
                        isOpen = false;
                        this.minecraft.soundManager.playSound("random.click", 1.0F, 1.0F);
                    }
                }
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
    }
}
