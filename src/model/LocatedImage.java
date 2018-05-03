package model;

import javafx.scene.image.Image;
/**
 * used to keep and get access to image URL
 */
public class LocatedImage extends Image {
    private final String url;

    /**
     * sets this images URL
     * 
     * @param url setting image URL
     */
    public LocatedImage(String url) {
        super(url);
        this.url = url;
    }

    /**
     * return the URL as String
     * 
     * @return String URL
     */
    public String getURL() {
        return url;
    }
}
