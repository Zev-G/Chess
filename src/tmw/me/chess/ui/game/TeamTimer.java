package tmw.me.chess.ui.game;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import tmw.me.chess.virtual.Team;

public class TeamTimer extends BorderPane {

    private int time;
    private final Team team;

    private final Label timeLabel = new Label();

    public TeamTimer(Team team, int initialTime) {
        this.team = team;
        this.time = initialTime;
        timeLabel.setText(Integer.toString(initialTime));
        getStyleClass().addAll("timer", "timer-" + team.toString().toLowerCase());
    }

    public double getTime() {
        return time;
    }

    public void changeTimeBy(int change) {
        time += change;
        timeLabel.setText(Integer.toString(time));
    }

    public Team getTeam() {
        return team;
    }
}
