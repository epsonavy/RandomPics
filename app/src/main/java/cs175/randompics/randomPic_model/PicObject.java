package cs175.randompics.randomPic_model;

/**
 * Created by Pei Liu on 16/10/2.
 */
public class PicObject {
    private String name;
    private int state;

    public PicObject(String name, int state) {
        super();
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
