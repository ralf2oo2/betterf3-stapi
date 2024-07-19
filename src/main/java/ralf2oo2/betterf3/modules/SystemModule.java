package ralf2oo2.betterf3.modules;

import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;
import ralf2oo2.betterf3.utils.DebugLine;
import ralf2oo2.betterf3.utils.Text;
import ralf2oo2.betterf3.utils.TextSection;
import ralf2oo2.betterf3.utils.Utils;

import javax.annotation.processing.Processor;
import java.awt.*;

public class SystemModule extends BaseModule{

    public SystemModule(){
        this.defaultNameColor = 0xFFAA00;
        this.defaultValueColor = 0x55FFFF;

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("java_version"));
        lines.add(new DebugLine("memory_usage"));
        lines.add(new DebugLine("allocated_memory"));
        lines.add(new DebugLine("cpu"));
        lines.add(new DebugLine("display"));
        lines.add(new DebugLine("gpu"));
        lines.add(new DebugLine("opengl_version"));
        lines.add(new DebugLine("gpu_driver"));

        for (DebugLine line : lines) {
            line.inReducedDebug = true;
        }
    }
    @Override
    public void update(Minecraft minecraft) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        String javaVersion = String.format("%s %dbit", System.getProperty("java.version"), Utils.checkIs64Bit() ? 64 : 32);
        String memoryUsage = String.format("% 2d%% %03d/%03d MB", usedMemory * 100 / maxMemory, usedMemory / 1024 / 1024, maxMemory / 1024 / 1024);
        String allocatedMemory = String.format("% 2d%% %03dMB", totalMemory * 100 / maxMemory, totalMemory / 1024 / 1024);

        String displayInfo = String.format("%d x %d (%s)", minecraft.displayWidth, minecraft.displayHeight, GL11.glGetString(7936));

        String[] versionSplit = GL11.glGetString(7938).split(" ");

        String openGlVersion = versionSplit[0];
        String gpuDriverVersion = String.join(" ", ArrayUtils.remove(versionSplit, 0));

        String cpuInfo = "todo: fix this";


        lines.get(0).setValue(javaVersion);
        lines.get(1).setValue(new Text( new TextSection(memoryUsage, Utils.getPercentColor((int) (usedMemory * 100 / maxMemory)))));
        lines.get(2).setValue(allocatedMemory);
        lines.get(3).setValue(cpuInfo);
        lines.get(4).setValue(displayInfo);
        lines.get(5).setValue(GL11.glGetString(7937));
        lines.get(6).setValue(openGlVersion);
        lines.get(7).setValue(gpuDriverVersion);

    }
}
