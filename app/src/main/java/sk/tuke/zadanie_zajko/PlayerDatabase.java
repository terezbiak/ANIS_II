package sk.tuke.zadanie_zajko;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Player.class}, version = 3)
public abstract class PlayerDatabase extends RoomDatabase {
    public abstract PlayerDao playerDao();
}
