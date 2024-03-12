package ralf2oo2.betterf3.modules;

import net.minecraft.client.Minecraft;
import ralf2oo2.betterf3.utils.DebugLine;
import ralf2oo2.betterf3.utils.Text;
import ralf2oo2.betterf3.utils.Utils;

import java.util.Arrays;

public class CoordsModule extends BaseModule{
    public int colorX;
    public int colorY;
    public int colorZ;

    public int defaultColorX = 0xFF5555;
    public int defaultColorY = 0x55FF55;
    public int defaultColorZ = 0x55FFFF;

    public CoordsModule(){
        this.defaultNameColor = 0xFF5555;

        this.nameColor = defaultNameColor;
        this.colorX = defaultColorX;
        this.colorY = defaultColorY;
        this.colorZ = defaultColorZ;

        lines.add(new DebugLine("player_coords", "format-betterf3-coords", true));
        lines.add(new DebugLine("block_coords", "format.betterf3.coords", true));
        lines.add(new DebugLine("chunk_relative_coords", "format.betterf3.coords", true));
        lines.add(new DebugLine("chunk_coords", "format.betterf3.coords", true));
        lines.get(2).inReducedDebug = true;
    }

    @Override
    public void update(Minecraft minecraft) {
        Text xyz = Utils.getStyledText("X", colorX).append(Utils.getStyledText("Y", colorY).getSections()).append(Utils.getStyledText("Z", colorZ).getSections());

        String playerX = String.format("%.3f", minecraft.player.x);
        String playerY = String.format("%.5f", minecraft.player.y);
        String playerZ = String.format("%.3f", minecraft.player.z);

        // Player coords
        lines.get(0).setValue(Arrays.asList(xyz, Utils.getStyledText(playerX, colorX), Utils.getStyledText(playerY, colorY), Utils.getStyledText(playerZ, colorZ)));

        lines.get(1).setValue(Arrays.asList(Utils.getStyledText(playerX, colorX), Utils.getStyledText(playerY, colorY), Utils.getStyledText(playerZ, colorZ)));
    }
}
