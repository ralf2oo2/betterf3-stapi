package ralf2oo2.betterf3.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	private boolean originalDebugHudValue;
	@Shadow private Minecraft minecraft;
	@Debug(export = true)
	@Inject(at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 3), method = "render", remap = false)
	private void betterf3_beforeRenderDebug(CallbackInfo ci) {
		originalDebugHudValue = minecraft.options.debugHud;
		minecraft.options.debugHud = false;
	}
	@Debug(export = true)
	@Inject(at = @At(value = "TAIL"), method = "render")
	private void betterf3_afterRender(CallbackInfo ci) {
		minecraft.options.debugHud = originalDebugHudValue;
	}
}
