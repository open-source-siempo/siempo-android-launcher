package minium.co.launcher2.events;

/**
 * Created by Shahab on 4/29/2016.
 */
public class ActionItemUpdateEvent {

    private String txt;

    public ActionItemUpdateEvent(String txt) {
        this.txt = txt;
    }

    public String getText() {
        return txt;
    }
}