package ralf2oo2.betterf3.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ralf2oo2.betterf3.utils.Utils;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public GameOptions options;
    boolean prevDebugHudState = false;
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;forceResourceReload()V", shift = At.Shift.BY, by = 3), method = "tick")
    private void betterf3_afterTick(CallbackInfo ci) {
        options.debugHud = !options.debugHud;
        System.out.println("test");
//        if(options.debugHud != prevDebugHudState){
//            prevDebugHudState = options.debugHud;
//            if(options.debugHud){
//                Utils.showDebug = true;
//            }
//            System.out.println("toggled debughud");
//
//            // Replace with enableanimations
//            if(true){
//                if(options.debugHud){
//                    Utils.closingAnimation = true;
//                } else{
//                    Utils.closingAnimation = false;
//                    Utils.xPos = Utils.START_X_POS;
//                }
//            }
//        }
    }
}
