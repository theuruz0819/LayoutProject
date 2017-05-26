package com.example.user.layoutproject;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.example.user.layoutproject.model.TrackTarget;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    public RealmResults<TrackTarget> getTargets() {
        return realm.where(TrackTarget.class).findAll();
    }

    public List<TrackTarget> getTargetList(){
        List<TrackTarget> targetList = realm.copyFromRealm(realm.where(TrackTarget.class).findAllSorted("lastUpdateDate", Sort.DESCENDING));
        return targetList;
    }

    public List<String> getAllTracingUrl(){
        List<TrackTarget> targetList = realm.copyFromRealm(realm.where(TrackTarget.class).findAll());
        List<String> urls = new ArrayList<>();
        for (TrackTarget target : targetList) {
            urls.add(target.getTargetUrl());
        }
        return  urls;
    }

    public void RemoveTarget(String targetUrl){
        final RealmQuery<TrackTarget> result = realm.where(TrackTarget.class).equalTo("targetUrl", targetUrl);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.findAll().deleteAllFromRealm();
            }
        });
    }
}

