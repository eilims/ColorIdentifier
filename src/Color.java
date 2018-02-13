public enum Color {
    RED("Red", new ColorRatioNode(0xFFFF0000)),
    GREEN("Green", new ColorRatioNode(0xFF00FF00)),
    BLUE("Blue", new ColorRatioNode(0xFF0000FF)),
    MAGENTA("Magenta", new ColorRatioNode(0xFFFF00FF)),
    YELLOW("Yellow", new ColorRatioNode(0xFFFFFF00)),
    CYAN("Cyan", new ColorRatioNode(0xFF00FFFF));

    private String name;
    private ColorRatioNode colorRatioNode;

    Color(String name, ColorRatioNode colorRatioNode) {
        this.name = name;
        this.colorRatioNode = colorRatioNode;
    }

    public ColorRatioNode getColorRatioNode() {
        return colorRatioNode;
    }

    public String getString(){
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }

}
