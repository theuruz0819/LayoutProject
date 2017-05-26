package com.example.user.layoutproject.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.user.layoutproject.model.TrackTarget;
import com.example.user.layoutproject.parsor.TraceTragetParsor;

import java.util.ArrayList;
import java.util.List;

public class ReflashTargetTask extends AsyncTask<String, Void, Void> {
    public void setTargetUrl(List<String> targetUrl) {
        this.targetUrl = targetUrl;
    }

    // interface setting
    public interface ReflashFinishListener {
        public void reflash(List<TrackTarget> targets);
    }
    private ReflashFinishListener mListener;

    public void setReflashFinishListener(ReflashFinishListener listener){
        this.mListener = listener;
    }
    // data
    private List<TrackTarget> targets;
    private List<String> targetUrl;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        targets = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(String... params) {
        try{
            for (String url:targetUrl ) {
                if (url.contains("www.dm5.com")){
                    targets.add(TraceTragetParsor.dm5Parsor(url));
                }else if(url.contains("comic.ck101.com")){
                    targets.add(TraceTragetParsor.ck101Parsor(url));
                }

            }
        }catch (Exception e){
            Log.e(this.getClass().getName(),e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mListener.reflash(targets);
    }
}
