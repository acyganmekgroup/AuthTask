package wrappers;

import enums.Kind;

public class WaiterItemsWrapper {
    private String name;
    private String shortDesc;
    private Kind kind;

    public WaiterItemsWrapper() {}

    public WaiterItemsWrapper(String name, String shortDesc, Kind kind) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.kind = kind;
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

    @Override
    public String toString() {
        return "WaiterItemsWrapper{" +
                "name='" + name + '\'' +
                ", shortDesc='" + shortDesc + '\'' +
                ", kind=" + kind +
                '}';
    }
}
