package pl.piotrskiba.angularowo.models;

public class Rank {

    private final String name;
    private final boolean staff;
    private final String colorCode;
    private final String chatColorCode;

    public Rank(String name, boolean staff, String colorCode, String chatColorCode){
        this.name = name;
        this.staff = staff;
        this.colorCode = colorCode;
        this.chatColorCode = chatColorCode;
    }

    public String getName() {
        return name;
    }

    public boolean isStaff() {
        return staff;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getChatColorCode() {
        return chatColorCode;
    }
}
