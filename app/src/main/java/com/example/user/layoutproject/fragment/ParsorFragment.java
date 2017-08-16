package com.example.user.layoutproject.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.layoutproject.R;
import com.example.user.layoutproject.RealmController;
import com.example.user.layoutproject.Task.DownloadImageTask;
import com.example.user.layoutproject.model.TrackTarget;
import com.example.user.layoutproject.parsor.TraceTragetParsor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class ParsorFragment extends Fragment {

    private RequestQueue mQueue;

    private String targetUrl;
    private Button startBtn;
    private Button saveBtn;
    private EditText targetEdt;
    private TextView titleText;
    private TextView lastUpdatedView;
    private TextView description;
    private ImageView imageView;

    private Realm realm;
    private TrackTarget target;
    private TraceTragetParsor parsor;
    public ParsorFragment() {
        // Required empty public constructor
    }

    public void setTargetUrl(String url){
        this.targetUrl = url;
    }

    public static ParsorFragment newInstance() {
        ParsorFragment fragment = new ParsorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = RealmController.with(this).getRealm();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parsor, container, false);

        titleText = (TextView) view.findViewById(R.id.parsor_title_textview);
        lastUpdatedView = (TextView) view.findViewById(R.id.parsor_last_update_date);
        description = (TextView) view.findViewById(R.id.parsor_description);
        imageView = (ImageView) view.findViewById(R.id.parsor_image);
        saveBtn = (Button) view.findViewById(R.id.parsor_save_btn);
        startBtn = (Button) view.findViewById(R.id.parsor_start_btn);
        targetEdt = (EditText) view.findViewById(R.id.parsor_target_url);
        targetEdt.setText(targetUrl);

        mQueue = Volley.newRequestQueue(getContext());
        parsor = new TraceTragetParsor();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(target!= null){
                    try {
                        realm.beginTransaction();
                        realm.copyToRealm(target);
                        realm.commitTransaction();
                    } catch (Exception ex){
                        Snackbar snackbar = Snackbar
                                .make(getView(), ex.getMessage(), Snackbar.LENGTH_LONG);
                        realm.cancelTransaction();
                        snackbar.show();
                    }
                }
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest getRequest = new StringRequest(targetUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String data) {
                                try {
                                    if (targetUrl.contains("www.dm5.com")){
                                        target = parsor.dm5Parsor(data, targetUrl);
                                    }else if(targetUrl.contains("comic.ck101.com")){
                                        target = parsor.ck101Parsor(data, targetUrl);
                                    }
                                }catch (Exception e){
                                    Snackbar snackbar = Snackbar
                                            .make(getView(), e.getMessage(), Snackbar.LENGTH_LONG);
                                }

                                // dm5 parsor完成前只有CK101回傳目標並且設竟數值
                                titleText.setText(target.getTitle());
                                description.setText(target.getDescription());
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                String dateString = df.format(target.getLastUpdateDate());
                                lastUpdatedView.setText(dateString);
                                new DownloadImageTask(imageView).execute(target.getImageUrl());

                                Log.i("tag3", "Finish");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        }){
                    @Override
                    public Map<String, String> getHeaders(){
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("User-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
                        return headers;
                    }
                };
                mQueue.add(getRequest);
            }
        });

        return view;
    }
}
