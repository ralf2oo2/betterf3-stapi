package ralf2oo2.betterf3.modules;

import net.minecraft.client.Minecraft;
import ralf2oo2.betterf3.utils.DebugLine;
import ralf2oo2.betterf3.utils.Text;
import ralf2oo2.betterf3.utils.Utils;

import java.util.Arrays;

public class CoordsModule extends BaseModule{
    public Integer colorX;
    public Integer colorY;
    public Integer colorZ;

    public Integer defaultColorX = 0xFF5555;
    public Integer defaultColorY = 0x55FF55;
    public Integer defaultColorZ = 0x55FFFF;

    public CoordsModule(){
        this.defaultNameColor = 0xFF5555;

        this.nameColor = defaultNameColor;
        this.colorX = defaultColorX;
        this.colorY = defaultColorY;
        this.colorZ = defaultColorZ;

        lines.add(new DebugLine("player_coords", "format-betterf3-coords", true));
        lines.add(new DebugLine("block_coords", "format-betterf3-coords", true));
        lines.add(new DebugLine("chunk_relative_coords", "format-betterf3-coords", true));
        lines.add(new DebugLine("chunk_coords", "format-betterf3-coords", true));
        lines.get(2).inReducedDebug = true;
    }

    @Override
    public void update(Minecraft minecraft) {
        Text xyz = Utils.getStyledText("X", colorX).append(Utils.getStyledText("Y", colorY).getSections()).append(Utils.getStyledText("Z", colorZ).getSections());

        String playerX = String.format("%.3f", minecraft.player.x);
        String playerY = String.format("%.5f", minecraft.player.y - minecraft.player.eyeHeight);
        String playerZ = String.format("%.3f", minecraft.player.z);

        String blockX = Integer.toString((int)Math.floor(minecraft.player.x));
        String blockY = Integer.toString((int)Math.floor(minecraft.player.y - minecraft.player.eyeHeight));
        String blockZ = Integer.toString((int)Math.floor(minecraft.player.z));

        String chunkRelativeX = Integer.toString((int)Math.floor(minecraft.player.x) & 15);
        String chunkRelativeY = Integer.toString((int)Math.floor(minecraft.player.y - minecraft.player.eyeHeight) & 15);
        String chunkRelativeZ = Integer.toString((int)Math.floor(minecraft.player.z) & 15);

        String chunkX = Integer.toString((int)Math.floor(minecraft.player.x) >> 4);
        String chunkY = Integer.toString((int)Math.floor(minecraft.player.y - minecraft.player.eyeHeight) >> 4);
        String chunkZ = Integer.toString((int)Math.floor(minecraft.player.z) >> 4);

        // Player coords
        lines.get(0).setValue(Arrays.asList(xyz, Utils.getStyledText(playerX, colorX), Utils.getStyledText(playerY, colorY), Utils.getStyledText(playerZ, colorZ)));
        // Block coords
        lines.get(1).setValue(Arrays.asList(Utils.getStyledText(blockX, colorX), Utils.getStyledText(blockY, colorY), Utils.getStyledText(blockZ, colorZ)));
        // Chunk Relative coords
        lines.get(2).setValue(Arrays.asList(Utils.getStyledText(chunkRelativeX, colorX), Utils.getStyledText(chunkRelativeY, colorY), Utils.getStyledText(chunkRelativeZ, colorZ)));
        // Chunk coords
        lines.get(3).setValue(Arrays.asList(Utils.getStyledText(chunkX, colorX), Utils.getStyledText(chunkY, colorY), Utils.getStyledText(chunkZ, colorZ)));
    }
}
