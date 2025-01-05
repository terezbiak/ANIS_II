package sk.tuke.zadanie_zajko;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {
    @Query("SELECT * FROM Player")
    List<Player> getAll();
    @Query("SELECT * FROM Player WHERE player_name = :playerName LIMIT 1")
    Player findByName(String playerName);

    @Query("SELECT * FROM Player WHERE Id LIKE :Id")
    Player getById(int Id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Player player);
    @Insert
    void insertPlayers(Player ... players);

    @Delete
    void deletePlayer(Player player);

    @Query("SELECT * FROM Player")
    List<Player> getAllPlayers();

    @Query("SELECT * FROM Player ORDER BY player_score DESC")
    List<Player> getAllPlayersSortedByScore();

    @Update
    void update(Player player);
}
