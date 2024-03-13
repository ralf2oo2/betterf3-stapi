package ralf2oo2.betterf3.modules;

import net.minecraft.client.Minecraft;
import ralf2oo2.betterf3.utils.DebugLine;

public class EmptyModule extends BaseModule{
    public static EmptyModule INSTANCE = new EmptyModule(false);
    public EmptyModule(boolean invisible) {
        super(invisible);
        lines.add(new DebugLine("", "", false));

        lines.get(0).inReducedDebug = true;
    }

    @Override
    public void update(Minecraft minecraft) {
        lines.get(0).setValue("");
    }
}
