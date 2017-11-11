package Client;

import java.io.Serializable;

/**
 * Created by arnau on 11/11/2017.
 */
public class ObjectContent implements Serializable {


    private static final long serialVersionUID = 1L;
    private String title;
    private int duration;
    private String category;

    public ObjectContent() {
    };

    public ObjectContent(String title, int duration, String category) {
        this.title = title;
        this.duration = duration;
        this.category = category;
    }

    protected long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Title:" + title + "\nDuration: " + duration + "\nCategory: " + category;
    }

}
