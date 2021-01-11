package Setup;

//Single word (1 of 25)
public class Word {
    private final String name;
    private final Team team;
    private boolean visible;

    public Word(String name, Team team) {
        this.name = name;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return name;
    }
}
