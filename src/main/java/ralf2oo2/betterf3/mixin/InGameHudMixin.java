package ralf2oo2.betterf3.mixin;
import joptsimple.internal.Strings;
import net.minecraft.class_564;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ralf2oo2.betterf3.config.GeneralOptions;
import ralf2oo2.betterf3.modules.BaseModule;
import ralf2oo2.betterf3.utils.ITextRenderer;
import ralf2oo2.betterf3.utils.Text;
import ralf2oo2.betterf3.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawContext {
	private boolean originalDebugHudValue;
	@Shadow private Minecraft minecraft;

	public List<Text> getNewLeftText(){
		List<Text> list = new ArrayList<>();

		for (BaseModule module : BaseModule.modules) {
			if (!module.enabled) {
				continue;
			}
			module.update(minecraft);

			// TODO: Change this to check for reduced debug
			list.addAll(module.getLinesFormatted(false));

			if (GeneralOptions.spaceEveryModule) {
				list.add(new Text(""));
			}
		}

		return list;
	}

	public List<Text> getNewRightText(){
		List<Text> list = new ArrayList<>();

		for (BaseModule module : BaseModule.modulesRight) {
			if (!module.enabled) {
				continue;
			}
			module.update(minecraft);

			// TODO: Change this to check for reduced debug
			list.addAll(module.getLinesFormatted(false));

			if (GeneralOptions.spaceEveryModule) {
				list.add(new Text(""));
			}
		}

		return list;
	}

	@Debug(export = true)
	@Inject(at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 2), method = "render", remap = false)
	private void betterf3_beforeRenderDebug(CallbackInfo ci) {
		if(GeneralOptions.disableMod) {
			return;
		}
		originalDebugHudValue = minecraft.options.debugHud;
		minecraft.options.debugHud = false;
		betterf3_renderAnimation();
		if(Utils.showDebug){
			betterf3_renderLeftText();
			betterf3_renderRightText();
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "render")
	private void betterf3_afterRender(CallbackInfo ci) {
		if(GeneralOptions.disableMod) return;
		minecraft.options.debugHud = originalDebugHudValue;
	}

	private void betterf3_renderLeftText(){
		List<Text> list = getNewLeftText();
		for(int i = 0; i < list.size(); i++){
			if(!Strings.isNullOrEmpty(list.get(i).toString())){
				int height = 9;
				int width = minecraft.textRenderer.getWidth(list.get(i).toString());
				int y = 2 + height * i;
				int xPosLeft = 2;

				if(GeneralOptions.enableAnimations){
					xPosLeft -= Utils.xPos;
				}
				if(GeneralOptions.showBackground){
					super.fill(0, y - 1, (width + xPosLeft) + 2, y + height - 1, GeneralOptions.backgroundColor);
				}

				if(GeneralOptions.shadowText){
					((ITextRenderer)minecraft.textRenderer).drawMultiColorString(list.get(i), xPosLeft, y, true);
				} else {
					((ITextRenderer)minecraft.textRenderer).drawMultiColorString(list.get(i), xPosLeft, y, false);
				}
			}
		}
	}
	private void betterf3_renderRightText(){
		List<Text> list = getNewRightText();
		for(int i = 0; i < list.size(); i++){
			if(!Strings.isNullOrEmpty(list.get(i).toString())){
				int height = 9;
				int width = minecraft.textRenderer.getWidth(list.get(i).toString());
				class_564 scaledResolution = new class_564(this.minecraft.options, this.minecraft.displayWidth, this.minecraft.displayHeight);
				int windowWidth = scaledResolution.method_1857() - 2 - width;
				if(GeneralOptions.enableAnimations){
					windowWidth += Utils.xPos;
				}
				int y = 2 + height * i;

				if(GeneralOptions.showBackground){
					super.fill(windowWidth - 3, y - 1, scaledResolution.method_1857(), y + height - 1, GeneralOptions.backgroundColor);
				}

				if(GeneralOptions.shadowText){
					((ITextRenderer)minecraft.textRenderer).drawMultiColorString(list.get(i), windowWidth, y, true);
				} else {
					((ITextRenderer)minecraft.textRenderer).drawMultiColorString(list.get(i), windowWidth, y, false);
				}
			}
		}
	}
	private void betterf3_renderAnimation(){
		long time = System.currentTimeMillis();

		if(time - Utils.lastAnimationUpdate >= 10 && (Utils.xPos != 0 || Utils.closingAnimation)){
			int i = ((Utils.START_X_POS/2 + Utils.xPos) / 10) -9;

			if(Utils.xPos != 0 && !Utils.closingAnimation){
				Utils.xPos /= GeneralOptions.animationSpeed;
				Utils.xPos -= i;
			}

			if(i == 0){
				i = 1;
			}

			if(Utils.closingAnimation){
				if(!GeneralOptions.enableAnimations) Utils.showDebug = false;
				Utils.xPos += i;
				Utils.xPos *= GeneralOptions.animationSpeed;

				if(Utils.xPos >= 300){
					Utils.closingAnimation = false;
					Utils.showDebug = false;
				}
			}
			Utils.lastAnimationUpdate = time;
		}
	}
}
