package pl.piotrskiba.angularowo.models;

public class MojangPlayer {

    private final String id;
    private final String name;

    public MojangPlayer(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
