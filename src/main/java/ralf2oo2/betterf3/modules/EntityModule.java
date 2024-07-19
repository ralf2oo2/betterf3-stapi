package ralf2oo2.betterf3.modules;

import net.minecraft.client.Minecraft;
import ralf2oo2.betterf3.utils.DebugLine;
import ralf2oo2.betterf3.utils.Utils;

import java.util.Arrays;

public class EntityModule extends BaseModule{

    Integer totalColor = 0xFFAA00;

    public EntityModule(){
        this.defaultNameColor = 0xFF5555;
        this.defaultValueColor = 0xFFFF55;

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("entities", "format.betterf3.total", true));
    }
    @Override
    public void update(Minecraft minecraft) {
        String entityString = minecraft.method_2142();

        String part = entityString.split("\\.")[0];
        String renderedEntites = part.split("/")[0];
        String totalEntities = part.split("/")[1];
        renderedEntites = renderedEntites.substring(3, renderedEntites.length());

        lines.get(0).setValue(Arrays.asList(Utils.getStyledText("rendered", valueColor), Utils.getStyledText("total", totalColor), Utils.getStyledText(renderedEntites, valueColor), Utils.getStyledText(totalEntities, valueColor)));
    }
}
