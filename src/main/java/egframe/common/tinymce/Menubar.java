package egframe.common.tinymce;

public enum Menubar {
    //@formatter:off
    FILE("file"),
    EDIT("edit"),
    VIEW("view"),
    MATH("math"),
    INSERT("insert"),
    FORMAT("format"),
    FORMULA("formula"),
    TOOLS("tools"),
    TABLE("table"),
    HELP("help");
    //@formatter:on

    public final String menubarLabel;

    private Menubar(String menubarLabel) {
        this.menubarLabel = menubarLabel;
    }

}
