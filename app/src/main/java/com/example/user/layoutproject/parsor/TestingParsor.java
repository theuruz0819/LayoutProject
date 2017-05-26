package com.example.user.layoutproject.parsor;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TestingParsor {
    static public void testingParsor(String targetUrl) throws IOException {
        // testing input
        List<String> productUrlList = new ArrayList<>();

        targetUrl = "http://www.dlsite.com/books/fsr/=/language/jp/sex_category%5B0%5D/male/keyword/%E6%B5%9C%E9%A2%A8/per_page/30/from/fs.header";
        URL url = new URL(targetUrl);
        Document doc = Jsoup.connect(targetUrl).get();
        String title = doc.title();
        Elements metaElems = doc.select("div.work_thumb").parents();
        for (Element metaElem : metaElems) {
            String productUrl = metaElem.select("a").first().attr("href");
            if(productUrl.contains("product_id")){
                Log.i("tag3", metaElem.select("a").first().attr("href"));
                Log.i("tag3", metaElem.select("a").first().select("img").first().attr("title"));
                if (!productUrlList.contains(productUrl)){
                    productUrlList.add(productUrl);
                }
            }
        }
        Log.i("tag3", "Finish");
    }
}
