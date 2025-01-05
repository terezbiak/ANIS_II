package sk.tuke.zadanie_zajko;

import android.content.Context;

import androidx.room.Room;

import java.lang.ref.WeakReference;

public class DbTools {
    private static PlayerDatabase _db;
    public DbTools(WeakReference<Context> refContext){
        getDbContext(refContext);
    }
    public static PlayerDatabase getDbContext(WeakReference<Context> refContext){
        if(_db!=null)
            return _db;
        return Room.databaseBuilder(refContext.get(),PlayerDatabase.class,"ma_to_serie").build();
    }
}
