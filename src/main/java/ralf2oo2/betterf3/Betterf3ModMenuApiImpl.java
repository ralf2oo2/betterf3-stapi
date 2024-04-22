package ralf2oo2.betterf3;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import ralf2oo2.betterf3.config.ModConfigFile;
import ralf2oo2.betterf3.config.gui.ModConfigScreen;

import java.util.function.Function;

public class Betterf3ModMenuApiImpl implements ModMenuApi {
    @Override
    public String getModId() {
        return Betterf3.MODID.toString();
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return parent -> new ModConfigScreen();
    }
}
