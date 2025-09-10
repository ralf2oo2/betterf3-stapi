package ralf2oo2.betterf3;

import net.danygames2014.modmenu.api.ConfigScreenFactory;
import net.danygames2014.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import ralf2oo2.betterf3.config.gui.ModConfigScreen;

import java.util.function.Function;

public class Betterf3ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> new ModConfigScreen();
    }
}
