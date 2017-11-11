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

    ObjectContent() {
    };

    ObjectContent(String title, int duration, String category) {
        this.title = title;
        this.duration = duration;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Title:" + title + "\nDuration: " + duration + "\nCategory: " + category;
    }

}
