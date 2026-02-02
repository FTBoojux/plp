package GlobalEnums;

public enum StringEnums {
    WILDCARD("*"),
    EMPTY(""),
    TRUE("true"),
    FALSE("false");
    private String string;

    StringEnums(String s) {
        this.string = s;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
