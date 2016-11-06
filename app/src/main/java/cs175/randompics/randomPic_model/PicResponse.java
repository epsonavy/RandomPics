package cs175.randompics.randomPic_model;

/**
 * Created by Pei Liu on 16/10/2.
 */
public class PicResponse {

    private String id;
    private PicModel urls;

    public String getThumb() {
        return urls.thumb;
    }
}
