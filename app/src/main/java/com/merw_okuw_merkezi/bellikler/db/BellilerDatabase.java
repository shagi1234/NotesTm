package com.merw_okuw_merkezi.bellikler.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.merw_okuw_merkezi.bellikler.model.BaseEntity;
import com.merw_okuw_merkezi.bellikler.model.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class, BaseEntity.class}, version = 1, exportSchema = false)

public abstract class BellilerDatabase extends RoomDatabase {
    private static BellilerDatabase bellilerDatabase;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract DaoNote daoNote();

    public synchronized static BellilerDatabase getBellilerDatabase(Context context) {
        if (bellilerDatabase == null) {
            bellilerDatabase = Room.databaseBuilder(context, BellilerDatabase.class, "Notes")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return bellilerDatabase;
    }

    private static final Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDbAsyncTask(bellilerDatabase).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, List<Note>> {
        private final DaoNote countryDao;

        @SuppressWarnings("deprecation")
        private PopulateDbAsyncTask(BellilerDatabase db) {
            countryDao = db.daoNote();
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            return countryDao.getAllNotes();
        }
    }
}
