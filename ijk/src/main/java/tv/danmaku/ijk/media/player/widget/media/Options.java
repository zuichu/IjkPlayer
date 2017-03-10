package tv.danmaku.ijk.media.player.widget.media;

import java.util.ArrayList;

/**
 * Created by office on 2016/10/10.
 */
public class Options {

    private ArrayList<Option> optionsList;

    public Options() {
        optionsList = new ArrayList<Option>();
    }

    public void setOption(int type, String name, long value) {
        Option option = new Option();
        option.setType(type);
        option.setName(name);
        option.setValue(value);
        optionsList.add(option);
    }

    public ArrayList<Option> getOptions() {
        return optionsList;
    }

}
