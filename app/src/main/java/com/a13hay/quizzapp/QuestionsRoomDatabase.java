package com.a13hay.quizzapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Questions.class},version = 2)
public abstract class QuestionsRoomDatabase extends RoomDatabase {


    private static QuestionsRoomDatabase INSTANCE;


    public abstract QuestionsDao wordDao();



    public static synchronized QuestionsRoomDatabase getInstance(final Context context){

        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    QuestionsRoomDatabase.class,"questions_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(RoomDBCallback)
                    .build();
        }

        return INSTANCE;
    }

    private static Callback RoomDBCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDbAsyncTask(INSTANCE).execute();

        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {


        private QuestionsDao wordDao;


        private PopulateDbAsyncTask(QuestionsRoomDatabase db)
        {
            wordDao = db.wordDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {


            wordDao.insert(new Questions(" API 21 is for what ?","Lollipop","Nought","Oreo","Android","1"));
            wordDao.insert(new Questions(" PC full is ? ","Lollipop","Personal Computer","Oreo","Android","2"));
            wordDao.insert(new Questions(" Firefox is what ?","Virus","Nought","Browser","Android","3"));
            wordDao.insert(new Questions(" API 25 is for what ?","Lollipop","Nought","Oreo","Android","2"));

            wordDao.insert(new Questions("Which of the following is a chat engine?","Google Bol","Yahoo Talk","Rediif Messenger","None of these","4"));
            wordDao.insert(new Questions("Which of the following is an input device?","Plotter","Printer","Monitor","Scanner","4"));
            wordDao.insert(new Questions("HTML is used to create -","Operating System","High Level Program","Web-Server","Web Page","4"));

            wordDao.insert(new Questions("Which is the fastest memory in computer","RAM","ROM","Cache","Hard Drive","3"));
            wordDao.insert(new Questions("What is the name for a webpage address? ","Directory","Protocol","URL","Domain","3"));
            wordDao.insert(new Questions("Which of the following is not an input device?","Microphone","Keyboard","Mozilla firefox","Mouse","3"));



            return null;
        }
    }


}