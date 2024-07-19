package ralf2oo2.betterf3.modules;

import net.minecraft.client.Minecraft;
import ralf2oo2.betterf3.utils.DebugLine;
import ralf2oo2.betterf3.utils.Text;
import ralf2oo2.betterf3.utils.TextSection;
import ralf2oo2.betterf3.utils.Utils;

import java.util.Arrays;
import java.util.Collections;

public class FpsModule extends BaseModule{
    public Integer colorHigh;
    public Integer colorMed;
    public Integer colorLow;

    public Integer defaultColorHigh = 0x55FF55;
    public Integer defaultColorMed = 0xFFFF55;
    public Integer defaultColorLow = 0xFF5555;

    public FpsModule(){
        lines.add(new DebugLine("fps", "format.betterf3.fps", true));
        lines.get(0).inReducedDebug = true;

        colorHigh = defaultColorHigh;
        colorMed = defaultColorMed;
        colorLow = defaultColorLow;
    }
    @Override
    public void update(Minecraft minecraft) {
        String fpsString = minecraft.debugText.split(" ")[0];
        int currentFps = Integer.parseInt(fpsString);

        int color = 0xFFFFFF;

        switch(Utils.getFpsColor(currentFps)){
            case 0:
                color = colorHigh;
                break;
            case 1:
                color = colorMed;
                break;
            case 2:
                color = colorLow;
                break;
        }
        // Kind of a hack, should find a better solution
        nameColor = color;
        lines.get(0).setValue(Arrays.asList(new Text(new TextSection(Integer.toString(currentFps), color))));
    }
}
