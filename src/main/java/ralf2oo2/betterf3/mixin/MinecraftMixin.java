package ralf2oo2.betterf3.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ralf2oo2.betterf3.config.GeneralOptions;
import ralf2oo2.betterf3.utils.IOnCloseHandler;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public Screen currentScreen;

    @Inject(at = @At(value = "HEAD"), method = "method_2111", cancellable = true)
    private void betterf3_disableLagometer(CallbackInfo ci) {
        if(GeneralOptions.disableLagometer){
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "setScreen", cancellable = true)
    private void betterf3_onCloseHandler(Screen screen, CallbackInfo ci) {
        if(currentScreen instanceof IOnCloseHandler){
            ((IOnCloseHandler)currentScreen).onClose();
        }
    }
}
