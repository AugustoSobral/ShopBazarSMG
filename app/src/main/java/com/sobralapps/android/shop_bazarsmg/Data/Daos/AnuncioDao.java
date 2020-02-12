package com.sobralapps.android.shop_bazarsmg.Data.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sobralapps.android.shop_bazarsmg.Data.Entities.AnuncioEntity;

import java.util.List;

@Dao
public interface AnuncioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AnuncioEntity anuncio);

    @Update
    void update(AnuncioEntity anuncio);

    @Delete
    void delete(AnuncioEntity anuncio);

    @Query("SELECT * FROM anuncio_table")
    LiveData<List<AnuncioEntity>> getAllAnuncios();

    @Query("SELECT * FROM anuncio_table WHERE id = :id")
    LiveData<AnuncioEntity> getAnuncioById(int id);


}
