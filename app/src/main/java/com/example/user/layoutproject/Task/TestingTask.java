package com.example.user.layoutproject.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.user.layoutproject.parsor.TestingParsor;

public class TestingTask extends AsyncTask<String, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        try{
            TestingParsor.testingParsor();
        }catch (Exception e){
            Log.e(this.getClass().getName(),e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
