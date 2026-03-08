package ralf2oo2.betterf3.modules;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.util.Identifier;
import ralf2oo2.betterf3.utils.DebugLine;

public class MinecraftModule extends BaseModule{

    public MinecraftModule(Identifier id){
        super(id);
        defaultNameColor = 0xA0522D;
        defaultValueColor = 0x00AA00;

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("minecraft", "format.betterf3.default_no_colon", false));
        lines.get(0).inReducedDebug = true;
    }
    @Override
    public void update(Minecraft minecraft) {
        lines.get(0).setValue("Beta 1.7.3");
    }
}
