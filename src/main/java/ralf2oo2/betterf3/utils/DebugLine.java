package ralf2oo2.betterf3.utils;

import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.mixin.lang.TranslationStorageAccessor;

import java.util.ArrayList;
import java.util.List;

public class DebugLine {
    private Object value;
    private String format;
    private final String id;

    public boolean active = true;
    public boolean enabled = true;
    public boolean isCustom = false;
    public boolean inReducedDebug = false;

    public DebugLine(String id) {
        this.id = id;
        this.format = "format-betterf3-default_format";
        this.value = "";
    }

    public DebugLine(String id, String formatString, boolean isCustom) {
        this.id = id;
        this.value = "";
        this.format = formatString;
        this.isCustom = isCustom;
    }

    public Text toText(int nameColor, int valueColor, int formatColor){
        String name = this.getName();

        Text nameStyled = Utils.getStyledText(name, nameColor);
        Text valueStyled;

        if (this.value instanceof Text) {
            valueStyled = (Text) this.value;
        } else {
            valueStyled = Utils.getStyledText((String)this.value, valueColor);
        }

        if(this.value.toString().equals("")){
            this.active = false;
        }

        return Text.format(this.format, formatColor, nameStyled, valueStyled);
    }

    public Text toTextCustom(int nameColor, int formatColor) {

        String name = this.getName();

        if (value instanceof List) {
            // format properly if value is a List (bad)
            List<Object> values = new ArrayList<>();
            List<?> value = (List<?>) this.value;


            if (!name.equals("")) {
                values.add(Utils.getStyledText(name, nameColor));
            }

            values.addAll(value);
            return Text.format(this.format, formatColor, values.toArray(new Text[values.size()]));
        } else {
            return Text.format(this.format, formatColor, new Text(new TextSection(name, nameColor)), new Text((String)value));
        }
    }

    public void setValue(Object value){
        this.active = true;
        this.value = value;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getName() {
        if (id.isEmpty()) {
            this.format = "%s%s";
            return "";
        }
        TranslationStorage translationStorage = TranslationStorage.getInstance();
        return translationStorage.get("text-betterf3-line-" + id);
    }

    public String getId() {
        return id;
    }
}
