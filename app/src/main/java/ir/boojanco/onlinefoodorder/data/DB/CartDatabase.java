package ir.boojanco.onlinefoodorder.data.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Cart.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {
    public abstract  CartDAO cartDAO;
    private static CartDatabase instance;

    public static CartDatabase getInstance(Context context){
        if(instance == null)
            instance = Room.databaseBuilder(context, CartDatabase.class,"BOOJAN_OnlineFoodOrderDB")
                    .allowMainThreadQueries()
                    .build();
            return instance;

    }

}
