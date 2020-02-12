package com.sobralapps.android.shop_bazarsmg.Data.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.sobralapps.android.shop_bazarsmg.Data.Daos.AnuncioDao;
import com.sobralapps.android.shop_bazarsmg.Data.DataBase;
import com.sobralapps.android.shop_bazarsmg.Data.Entities.AnuncioEntity;

import java.util.List;

public class AnuncioRepository {

    private AnuncioDao anuncioDao;
    private LiveData<List<AnuncioEntity>> allAnuncios;

    public AnuncioRepository(Application application){

        DataBase db = DataBase.getInstance(application);
        anuncioDao = db.getAnuncioDao();
        allAnuncios = anuncioDao.getAllAnuncios();
    }


    public void insert(AnuncioEntity anuncio){
        new InsertAsyncTask(anuncioDao).execute(anuncio);
    }

    public void update(AnuncioEntity anuncio){
        new UpdateAsyncTask(anuncioDao).execute(anuncio);
    }

    public void delete(AnuncioEntity anuncio){
        new DeleteAsyncTask(anuncioDao).execute(anuncio);
    }

    public LiveData<List<AnuncioEntity>> getAllAnuncios() {
        return allAnuncios;
    }

    public LiveData<AnuncioEntity> getAnuncioById(int id){
        return anuncioDao.getAnuncioById(id);
    }


    private static class InsertAsyncTask extends AsyncTask<AnuncioEntity, Void, Void> {

        private AnuncioDao AsyncDao;

        private InsertAsyncTask(AnuncioDao anuncioDao){
            AsyncDao = anuncioDao;
        }


        @Override
        protected Void doInBackground(AnuncioEntity... anuncios) {
            AsyncDao.insert(anuncios[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<AnuncioEntity, Void, Void>{

        private AnuncioDao AsyncDao;

        private UpdateAsyncTask(AnuncioDao anuncioDao){
            AsyncDao = anuncioDao;
        }


        @Override
        protected Void doInBackground(AnuncioEntity... anuncios) {
            AsyncDao.update(anuncios[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<AnuncioEntity, Void, Void>{

        private AnuncioDao AsyncDao;

        private DeleteAsyncTask(AnuncioDao anuncioDao){
            AsyncDao = anuncioDao;
        }


        @Override
        protected Void doInBackground(AnuncioEntity... anuncios) {
            AsyncDao.delete(anuncios[0]);
            return null;
        }
    }
}
