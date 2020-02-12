package com.sobralapps.android.shop_bazarsmg.Data.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobralapps.android.shop_bazarsmg.Data.Entities.AnuncioEntity;
import com.sobralapps.android.shop_bazarsmg.Data.Repositories.AnuncioRepository;

import java.util.List;

public class AnuncioOperationsViewModel extends AndroidViewModel {

    private AnuncioRepository repository;
    private LiveData<List<AnuncioEntity>> anuncios;


    public AnuncioOperationsViewModel(@NonNull Application application) {
        super(application);

        repository = new AnuncioRepository(application);
        anuncios = repository.getAllAnuncios();
    }

    public void insert(AnuncioEntity anuncio){
        repository.insert(anuncio);
    }
    public void update(AnuncioEntity anuncio){
        repository.update(anuncio);
    }
    public void delete(AnuncioEntity anuncio) {
        repository.delete(anuncio);
    }
    public LiveData<List<AnuncioEntity>> getAllAnuncios(){
        return anuncios;
    }
    public LiveData<AnuncioEntity> getAnuncioById(int id){
        return repository.getAnuncioById(id);
    }
}
