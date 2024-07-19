package ralf2oo2.betterf3.mixin;

import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ralf2oo2.betterf3.utils.ITextRenderer;
import ralf2oo2.betterf3.utils.Text;
import ralf2oo2.betterf3.utils.TextSection;

@Mixin(TextRenderer.class)
public abstract class TextRendererMixin implements ITextRenderer {

    @Shadow public abstract void draw(String text, int x, int y, int color);

    @Shadow public abstract int getWidth(String text);

    @Shadow public abstract void drawWithShadow(String text, int x, int y, int color);

    public void drawMultiColorString(Text text, int x, int y, boolean shadow){
        if (text != null) {
            int sectionOffset = 0;
            for(int i = 0; i < text.getSectionCount(); i++){
                TextSection currentSection = text.getSection(i);
                if(shadow){
                    drawWithShadow(currentSection.text, x + sectionOffset, y, currentSection.color);
                } else {
                    draw(currentSection.text, x + sectionOffset, y, currentSection.color);
                }
                sectionOffset += getWidth(currentSection.text);
            }
        }
    }
}
