package models;

import enums.Kind;

public class Items {
    private float priceNet;
    private String image;
    private String name;
    private String shortDesc;
    private Kind kind;

    public Items(float priceNet, String image, String name, String shortDesc, Kind kind) {
        this.priceNet = priceNet;
        this.image = image;
        this.name = name;
        this.shortDesc = shortDesc;
        this.kind = kind;
    }

    public float getPriceNet() {
        return priceNet;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public Kind getKind() {
        return kind;
    }
}
