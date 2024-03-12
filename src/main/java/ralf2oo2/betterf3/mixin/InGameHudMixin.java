package ralf2oo2.betterf3.mixin;
import joptsimple.internal.Strings;
import net.minecraft.class_564;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ralf2oo2.betterf3.utils.Utils;

import java.util.Arrays;
import java.util.List;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	private boolean originalDebugHudValue;
	@Shadow private Minecraft minecraft;

	@Inject(at = @At(value = "HEAD"), method = "render")
	private void betterf3_render(CallbackInfo ci) {

	}

	@Debug(export = true)
	@Inject(at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 2), method = "render", remap = false)
	private void betterf3_beforeRenderDebug(CallbackInfo ci) {
		originalDebugHudValue = minecraft.options.debugHud;
		minecraft.options.debugHud = false;
		if(Utils.showDebug){
			betterf3_renderAnimation();
			betterf3_renderLeftText();
			betterf3_renderRightText();
		}
	}
	@Debug(export = true)
	@Inject(at = @At(value = "TAIL"), method = "render")
	private void betterf3_afterRender(CallbackInfo ci) {
		minecraft.options.debugHud = originalDebugHudValue;
	}

	private void betterf3_renderLeftText(){
		List<String> list = Arrays.asList("Test string", "test string 2");
		for(int i = 0; i < list.size(); i++){
			if(!Strings.isNullOrEmpty(list.get(i))){
				int height = 9;
				int width = minecraft.textRenderer.getWidth(list.get(i));
				int y = 2 + height * i;
				int xPosLeft = 2;

				//Replace with generaloptions.enableanimations
				if(true){
					xPosLeft -= Utils.xPos;
				}

				minecraft.textRenderer.drawWithShadow(list.get(i), xPosLeft, y, 0xFFFFFF);
			}
		}
	}
	private void betterf3_renderRightText(){
		List<String> list = Arrays.asList("Test string right", "test string 2");
		for(int i = 0; i < list.size(); i++){
			if(!Strings.isNullOrEmpty(list.get(i))){
				int height = 9;
				int width = minecraft.textRenderer.getWidth(list.get(i));
				class_564 scaledResolution = new class_564(this.minecraft.options, this.minecraft.displayWidth, this.minecraft.displayHeight);
				int windowWidth = scaledResolution.method_1857() - 2 - width;
				//Replace with generaloptions.enableanimations
				if(true){
					windowWidth += Utils.xPos;
				}
				int y = 2 + height * i;

				minecraft.textRenderer.drawWithShadow(list.get(i), windowWidth, y, 0xFFFFFF);
			}
		}
	}
	private void betterf3_renderAnimation(){
		long time = System.currentTimeMillis();

		if(time - Utils.lastAnimationUpdate >= 10 && (Utils.xPos != 0 || Utils.closingAnimation)){
			System.out.println(time);
			int i = ((Utils.START_X_POS/2 + Utils.xPos) / 10) -9;

			if(Utils.xPos != 0 && !Utils.closingAnimation){
				// Change 1 to animationspeed when config gets added
				Utils.xPos /= 1;
				Utils.xPos -= i;
			}

			if(i == 0){
				i = 1;
			}

			if(Utils.closingAnimation){
				Utils.xPos += i;
				// Change 1 to animationspeed when config gets added
				Utils.xPos *= 1;
			}

			if(Utils.xPos >= 300){
				Utils.closingAnimation = false;
				if(!originalDebugHudValue){
					Utils.showDebug = false;
				}
			}
			Utils.lastAnimationUpdate = time;
		}
	}
}
