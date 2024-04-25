package ralf2oo2.betterf3.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.util.Util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    // Animation stuff
    public static final int START_X_POS = 200;
    public static int xPos = START_X_POS;
    public static long lastAnimationUpdate = 0;
    public static boolean closingAnimation = false;
    public static boolean showDebug = false;

    public static Map<String, Integer> colors = new HashMap<String, Integer>(){{
      put("0", 0x000000);
      put("1", 0x0000AA);
      put("2", 0x00AA00);
      put("3", 0x00AAAA);
      put("4", 0xAA0000);
      put("5", 0xAA00AA);
      put("6", 0xFFAA00);
      put("7", 0xAAAAAA);
      put("8", 0x555555);
      put("9", 0x5555FF);
      put("a", 0x55FF55);
      put("b", 0x55FFFF);
      put("c", 0xFF5555);
      put("d", 0xFF55FF);
      put("e", 0xFFFF55);
      put("f", 0xFFFFFF);
    }};

    // Other
    public static Minecraft getMc(){
        return Minecraft.class.cast(FabricLoader.getInstance().getGameInstance());
    }

    public static Text getStyledText(String string, Integer color){
        if(string == null){
            string = "";
        }
        if(color == null){
            color = 0xFFFFFF;
        }
        return new Text(new TextSection(string, color));
    }

    public static Integer getPercentColor(int percent) {
        if (percent >= 90) {
            return 0xFF5555;
        } else if (percent >= 60) {
            return 0xFFFF55;
        } else {
            return 0x55FF55;
        }
    }

    public static Integer getFpsColor(int currentFps){
        if(currentFps >= 60){
            return 0;
        } else if (currentFps >= 20){
            return 1;
        } else {
            return 2;
        }
    }

    public static Text formattedFromString(final String string, final int nameColor, final int valueColor) {
        if(string == null){
            return new Text("");
        }
        final String[] split = string.split(":");

        if(string.contains(":")){
            final Text name = Utils.getStyledText(split[0], nameColor);
            final Text value = Utils.getStyledText(String.join(":", Arrays.asList(split).subList(1, split.length)), valueColor);

            return name.append(new Text(":").getSections()).append(value.getSections());
        } else {
            return new Text(string);
        }
    }

    public static boolean checkIs64Bit() {
        String[] strings = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
        String[] var1 = strings;
        int var2 = strings.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String string = var1[var3];
            String string2 = System.getProperty(string);
            if (string2 != null && string2.contains("64")) {
                return true;
            }
        }

        return false;
    }

    public static Text propertyToString(Map.Entry<Property<?>, Comparable<?>> propEntry, int keyColor, int valueColor){
        Property<?> key = propEntry.getKey();
        Comparable<?> value = propEntry.getValue();

        Text newValue = new Text(new TextSection(getValueAsString(key, value), valueColor));
        if(Boolean.TRUE.equals(value)){
            newValue.getSection(0).color = 0x55FF55;
        } else if (Boolean.FALSE.equals(value)){
            newValue.getSection(0).color = 0xFF5555;
        }
        newValue = new Text(new TextSection(key.getName() + ": ", keyColor)).append(newValue.getSections());
        return newValue;
    }

    public static <T extends Comparable<T>> String getValueAsString(Property<T> property, Object value) {
        return property.name((T)value);
    }
}
