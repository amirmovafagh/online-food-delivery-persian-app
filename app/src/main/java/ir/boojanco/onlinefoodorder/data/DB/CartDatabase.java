package ir.boojanco.onlinefoodorder.data.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {CartItem.class}, version = 1, exportSchema = false)
public abstract class CartDatabase extends RoomDatabase {

    private static CartDatabase instance;

    public static CartDatabase getInstance(Context context){
        if(instance == null)
            instance = Room.databaseBuilder(context, CartDatabase.class,"BOOJAN_OnlineFoodOrderDB")
                    .build();
            return instance;

    }

}
