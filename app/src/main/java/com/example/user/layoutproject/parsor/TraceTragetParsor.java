package com.example.user.layoutproject.parsor;

import android.util.Log;

import com.example.user.layoutproject.model.TrackTarget;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TraceTragetParsor {

    static public TrackTarget ck101Parsor(String data, String targetUrl){
        TrackTarget target;
        target = new TrackTarget();
        Document doc = Jsoup.parse(data);
        String title = doc.title();
        Elements metaElems = doc.select("meta");
        for (Element metaElem : metaElems) {
            String content = metaElem.attr("content");
            String property = metaElem.attr("property");
            Log.i("tag3", property + " : " + content);

            target.setTargetUrl(targetUrl);
            if(property.equals("og:image")){
                target.setImageUrl(content);
            } else if (property.equals("og:title")){
                target.setTitle(content);
            } else if (property.equals("book:release_date")){
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(content);
                    target.setLastUpdateDate(date);
                }catch (Exception e){
                    Date date = new Date();
                    target.setLastUpdateDate(date);
                }
            } else if (property.equals("og:description")){
                target.setDescription(content);
            }

        }
        Log.i("tag3", "Finish");
        return target;
    }

    static public TrackTarget ck101Parsor(String targetUrl) throws IOException {
        TrackTarget target;
        target = new TrackTarget();
        URL url = new URL(targetUrl);
        Document doc = Jsoup.connect(targetUrl).get();
        String title = doc.title();
        Elements metaElems = doc.select("meta");
        for (Element metaElem : metaElems) {
            String content = metaElem.attr("content");
            String property = metaElem.attr("property");
            Log.i("tag3", property + " : " + content);

            target.setTargetUrl(targetUrl);
            if(property.equals("og:image")){
                target.setImageUrl(content);
            } else if (property.equals("og:title")){
                target.setTitle(content);
            } else if (property.equals("book:release_date")){
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(content);
                    target.setLastUpdateDate(date);
                }catch (Exception e){
                    Date date = new Date();
                    target.setLastUpdateDate(date);
                }
            } else if (property.equals("og:description")){
                target.setDescription(content);
            }
        }
        Log.i("tag3", "Finish");
        return target;
    }

    static public TrackTarget dm5Parsor(String targetUrl) throws Exception{
        TrackTarget target;
        target = new TrackTarget();
        URL url = new URL(targetUrl);
        Document doc = Jsoup.connect(targetUrl).get();
        String title = doc.title();
        Log.i("tag3", "title : " + title);
        target.setTargetUrl(targetUrl);
        Elements img = doc.select("img");
        for (Element elem : img) {
            String src = elem.attr("src");
            String alt = elem.attr("alt");
            if (title.contains(alt) && !alt.isEmpty()){
                Log.i("tag3", "Title : " + alt);
                Log.i("tag3", "Image : " + src);
                target.setTitle(alt);
                target.setImageUrl(src);
            }
        }

        String selector = "p:contains(" + target.getTitle() + ")";
        Elements des = doc.select(selector);
        for (Element elem : des) {
            if (elem.ownText().contains("漫画") && !elem.ownText().contains("法律法规")){
                String description = elem.ownText().substring(elem.ownText().indexOf("漫画") + 4);
                Log.i("tag3", description);
                target.setDescription(description);
                break;
            }
        }

        Elements timeElement = doc.getElementsByClass("innr92_l");
        for (Element elem : timeElement) {
            if (elem.ownText().contains("更新时间")){
                String dateString = elem.ownText().substring(5, 15);
                Log.i("tag3", dateString);
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(dateString);
                    target.setLastUpdateDate(date);
                }catch (Exception e){
                    Date date = new Date();
                    target.setLastUpdateDate(date);
                }
            }
        }
        Log.i("tag3", "Finish");
        return target;
    }

    static public TrackTarget dm5Parsor(String data, String targetUrl) {
        TrackTarget target;
        target = new TrackTarget();
        Document doc = Jsoup.parse(data);
        String title = doc.title();
        Log.i("tag3", "title : " + title);
        target.setTargetUrl(targetUrl);
        Elements img = doc.select("img");
        for (Element elem : img) {
            String src = elem.attr("src");
            String alt = elem.attr("alt");
            if (title.contains(alt) && !alt.isEmpty()){
                Log.i("tag3", "Title : " + alt);
                Log.i("tag3", "Image : " + src);
                target.setTitle(alt);
                target.setImageUrl(src);
            }
        }

        String selector = "p:contains(" + target.getTitle() + ")";
        Elements des = doc.select(selector);
        for (Element elem : des) {
            if (elem.ownText().contains("漫画") && !elem.ownText().contains("法律法规")){
                String description = elem.ownText().substring(elem.ownText().indexOf("漫画") + 4);
                Log.i("tag3", description);
                target.setDescription(description);
                break;
            }
        }

        Elements timeElement = doc.getElementsByClass("innr92_l");
        for (Element elem : timeElement) {
            if (elem.ownText().contains("更新时间")){
                String dateString = elem.ownText().substring(5, 15);
                Log.i("tag3", dateString);
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(dateString);
                    target.setLastUpdateDate(date);
                }catch (Exception e){
                    Date date = new Date();
                    target.setLastUpdateDate(date);
                }
            }
        }
        Log.i("tag3", "Finish");
        return target;
    }
}
