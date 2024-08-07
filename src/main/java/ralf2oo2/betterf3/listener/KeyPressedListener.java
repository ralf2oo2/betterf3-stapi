package ralf2oo2.betterf3.listener;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import org.lwjgl.input.Keyboard;
import ralf2oo2.betterf3.config.gui.ModConfigScreen;
import ralf2oo2.betterf3.utils.Text;
import ralf2oo2.betterf3.utils.Utils;

import java.util.Arrays;

public class KeyPressedListener {
    boolean prevDebugHudState = false;
    @EventListener
    public void keyPressed(KeyStateChangedEvent event) {
        Minecraft minecraft = Utils.getMc();
        if(Keyboard.getEventKey() == Keyboard.KEY_F3 && prevDebugHudState != minecraft.options.debugHud){
            prevDebugHudState = minecraft.options.debugHud;
            if(minecraft.options.debugHud){
                Utils.showDebug = true;
            }

            if(minecraft.options.debugHud){
                Utils.closingAnimation = false;
                Utils.xPos = Utils.START_X_POS;
            } else{
                Utils.closingAnimation = true;
            }
        }
    }
}
