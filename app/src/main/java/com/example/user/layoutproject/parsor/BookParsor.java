package com.example.user.layoutproject.parsor;

import android.util.Log;

import com.example.user.layoutproject.model.Book;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookParsor {
    static String[] targetStrings = {"書名：", "原文名稱：", "語言：", "ISBN：", "頁數：", "出版社：", "作者：", "譯者：", "出版日期：", "類別："};
    static public Book singlBookParsor(String data, String targetUrl){
        Book book = new Book();

        Document doc = Jsoup.parse(data);
        String title = doc.title();
        Elements metaElems = doc.select("meta");

        for (Element metaElem : metaElems) {
            String content = metaElem.attr("content");
            String property = metaElem.attr("property");

            if("og:title".equals(property)){
                Log.i("tag3", "title : " + content);
                book.setTitle(content);
            } else if("og:image".equals(property)){
                Log.i("tag3", "image : " + content);
                book.setImageUrl(content);
            } else if("og:url".equals(property)){
                Log.i("tag3", "url : " + content);
                book.setWebpageUrl(content);
            }  else if("og:description".equals(property)){
                descriptionStringParser(content, book);
            }
        }
        Log.i("tag3", "Finish");
        return book;
    }

    private static void descriptionStringParser(String description, Book book){

        List<String> fragment = new ArrayList<>();

        int beginIndex = 0;
        int endIndex = 0;
        Log.i("tag3", "description : " + description);
        for (String str: targetStrings) {
            Integer index = description.indexOf(str);
            endIndex = index;
            if(endIndex > 0){
                fragment.add(description.substring(beginIndex, endIndex - 1));
            }
            beginIndex = endIndex;
        }
        fragment.add(description.substring(beginIndex, description.length()));

        for (String str : fragment) {
           if (str.contains("原文名稱：")){
               book.setSubTitle(str.replace("原文名稱：", ""));
           } else if (str.contains("語言：")){
               book.setLanguage(str.replace("語言：", ""));
           } else if (str.contains("ISBN：")){
               book.setIsbn(str.replace("ISBN：", ""));
           } else if (str.contains("頁數：")){
               book.setPages(str.replace("頁數：", ""));
           } else if (str.contains("出版社：")){
               book.setPublisher(str.replace("出版社：", ""));
           } else if (str.contains("作者：")){
               book.setAuthor(str.replace("作者：", ""));
           } else if (str.contains("譯者：")){
               book.setTranslator(str.replace("譯者：", ""));
           } else if (str.contains("類別：")){
               book.setType(str.replace("類別：", ""));
           } else if (str.contains("出版日期：")){
               try{
                   SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                   Date date = sdf.parse(str.replace("出版日期：", ""));
                   book.setPublishDate(date);
               }catch (Exception e){
                   Date date = new Date();
                   book.setPublishDate(date);
               }
           }
        }
    }
}
