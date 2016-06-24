package minium.co.launcher2.model;

/**
 * Created by Shahab on 6/10/2016.
 */
public class OptionsListItem {

    int pos;
    String iconName;
    String text;

    public OptionsListItem(int pos, String iconName, String text) {
        this.pos = pos;
        this.iconName = iconName;
        this.text = text;
    }

    public int getPosition() {
        return pos;
    }

    public String getIconName() {
        return iconName;
    }

    public String getText() {
        return text;
    }
}
