package com.sobralapps.android.shop_bazarsmg.Data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.sobralapps.android.shop_bazarsmg.Data.Daos.AnuncioDao;
import com.sobralapps.android.shop_bazarsmg.Data.Entities.AnuncioEntity;

@Database(entities = AnuncioEntity.class, version = 12)
public abstract class DataBase extends RoomDatabase {

    private static DataBase instance;

    public abstract AnuncioDao getAnuncioDao();


    public static synchronized DataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DataBase.class, "database")
                    .fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

}
