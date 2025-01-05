package sk.tuke.zadanie_zajko;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Player{

    public Player(){

    }
    public Player(String playerName, int playerScore){
        setPlayerName(playerName);
        setPlayerScore(playerScore);
    }

    @PrimaryKey(autoGenerate = true)
    private int Id;

    @ColumnInfo(name = "player_name")
    private String PlayerName;

    @ColumnInfo(name = "player_score")
    private int PlayerScore;



    public void setId(int id) {
        Id = id;
    }

    public void setPlayerName(String playerName) {
        PlayerName = playerName;
    }

    public void setPlayerScore(int playerScore) {
        PlayerScore = playerScore;
    }

    public int getId() {
        return Id;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public int getPlayerScore() {
        return PlayerScore;
    }
}
