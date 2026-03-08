package ralf2oo2.betterf3test.modules;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.util.Identifier;
import ralf2oo2.betterf3.modules.BaseModule;
import ralf2oo2.betterf3.utils.DebugLine;
import ralf2oo2.betterf3test.event.init.ModuleListener;

public class TestModule extends BaseModule {
    public TestModule(Identifier id) {
        super(id);
        this.defaultNameColor = 0xFC03BD;
        this.defaultValueColor = 0x03FC42;
        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        this.lines.add(new DebugLine("test1").withNamespace(ModuleListener.NAMESPACE));
    }

    @Override
    public void update(Minecraft minecraft) {
        lines.get(0).setValue(String.valueOf(minecraft.world.random.nextFloat()));
    }
}
