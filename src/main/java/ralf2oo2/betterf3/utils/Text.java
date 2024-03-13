package ralf2oo2.betterf3.utils;

import joptsimple.internal.Strings;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.Arrays;
import java.util.stream.Stream;

public class Text {
    private TextSection[] textSections;

    public Text(TextSection... textSections) {
        this.textSections = textSections;
    }

    public Text(String... strings){
        if(strings == null) return;
        TextSection[] sections = new TextSection[strings.length];
        for(int i = 0; i < strings.length; i++){
            sections[i] = new TextSection(strings[i], 0xFFFFFF);
        }
    }

    @Override
    public String toString() {
        String string = "";
        for(int i = 0; i < textSections.length; i++){
            if(!Strings.isNullOrEmpty(textSections[i].text)){
                string += textSections[i].text;
            }
        }
        return string;
    }

    public int getSectionCount(){
        return textSections.length;
    }

    public TextSection getSection(int sectionIndex){
        return textSections[sectionIndex];
    }

    public TextSection[] getSections(){
        return textSections;
    }

    public static Text fromColorCodedString(String string){
        String[] sections = string.split("§");
        String[] validSections = Arrays.stream(sections).filter((x) -> x.length() > 1).toArray(String[]::new);
        TextSection[] textSections = new TextSection[validSections.length];
        for(int i = 0; i < textSections.length; i++){
            String colorCode = validSections[i].substring(0,1);
            String text = validSections[i].substring(1,validSections[i].length());
            textSections[i] = new TextSection(text, Utils.colors.get(colorCode));
        }
        return new Text(textSections);
    }

    public Text append(TextSection... textSections){
        if(textSections == null) return this;
        return new Text(
                Stream.concat(Arrays.stream(this.textSections), Arrays.stream(textSections)).toArray(TextSection[]::new)
        );
    }

    public static Text format(String format, int formatColor, Text... texts){
        String formatString = TranslationStorage.getInstance().get(format);
        String formatStrings[] = formatString.split("%s");
        if(formatStrings.length <= 1){
            return new Text(new TextSection(formatString, formatColor));
        }
        Text finalText = new Text(new TextSection(formatStrings[0], formatColor));
        if(texts[0] != null){
            finalText = finalText.append(texts[0].getSections());
        }
        for(int i = 1; i < formatStrings.length; i++){
            if(i > texts.length - 1){
                finalText = finalText.append(new TextSection("%s", formatColor));
                continue;
            }
            finalText = finalText.append(new TextSection(formatStrings[i], formatColor));
            if(texts[i] != null){
                finalText = finalText.append(texts[i].getSections());
            }
        }
        return finalText;
    }
}
