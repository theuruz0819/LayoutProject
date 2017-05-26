package com.example.user.layoutproject.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.layoutproject.R;
import com.example.user.layoutproject.RealmController;
import com.example.user.layoutproject.model.Book;
import com.example.user.layoutproject.parsor.BookParsor;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookParsorFragment extends Fragment {
    // data
    private String targetUrl;
    private RequestQueue mQueue;
    // interface
    private Button loadBtn;
    private Button frowardBtn;
    private Button backBtn;
    private EditText targetUrlEdt;
    private Button singleParsorBtn;
    private Button listParsorBtn;
    private WebView webView;
    private BookParserResultDialogFragment.BookParsorResDialogListener mListener;
    private Realm realm;

    public BookParsorFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_parsor, container, false);
        loadBtn = (Button) view.findViewById(R.id.book_parsor_load_btn);
        frowardBtn = (Button) view.findViewById(R.id.book_parsor_forward_brtn);
        backBtn = (Button) view.findViewById(R.id.book_parsor_back_btn);
        targetUrlEdt = (EditText) view.findViewById(R.id.book_parsor_target_edt);
        singleParsorBtn = (Button) view.findViewById(R.id.book_parsor_single_parsor_btn);
        listParsorBtn = (Button) view.findViewById(R.id.book_parsor_list_parsor_btn);
        webView = (WebView) view.findViewById(R.id.book_parsor_web_view);

        String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        webView.getSettings().setUserAgentString(newUA);
        mQueue = Volley.newRequestQueue(getContext());

        mListener = new BookParserResultDialogFragment.BookParsorResDialogListener() {
            @Override
            public void saveBook(Book book) {
                if(book!= null){
                    try {
                        realm.beginTransaction();
                        realm.copyToRealm(book);
                        realm.commitTransaction();
                    } catch (Exception ex){
                        Snackbar snackbar = Snackbar
                                .make(getView(), ex.getMessage(), Snackbar.LENGTH_LONG);
                        realm.cancelTransaction();
                        snackbar.show();
                    }
                }
            }
        };

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(targetUrlEdt.getText().toString());
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goBack();
            }
        });

        singleParsorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest getRequest = new StringRequest(webView.getUrl(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String data) {
                                try {
                                    Book book = BookParsor.singlBookParsor(data, webView.getUrl());
                                    DialogFragment fragment = new BookParserResultDialogFragment().newInstance(book, mListener);
                                    fragment.show(getActivity().getSupportFragmentManager(), "book_parser_results");
                                }catch (Exception e){
                                    Snackbar snackbar = Snackbar
                                            .make(getView(), e.getMessage(), Snackbar.LENGTH_LONG);
                                }
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

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
}
