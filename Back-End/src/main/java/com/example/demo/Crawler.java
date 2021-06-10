package com.example.demo;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.sql.ResultSet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList ;
import java.util.List ;


class CrawlerHelper implements Runnable{




    InvertedIndex newIndex;

    public void run(){
        HashMap<Elements, String> crawlReturned=startCrawling(this.URL);
        newIndex=new InvertedIndex();
        System.out.println("--------------------------------FROM CRAWL TITLES "+this.Titles+" --------------------------------------------");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newIndex.startIndexing(crawlReturned);//Calling the indexer after finishing crawling
        newIndex.saveinDatabase();
    }

    private List<String> Links = new LinkedList<String>();
    private Document htmlDocument;
    private String URL;
    private int interrupt;
    private String Titles;

    public CrawlerHelper(String URL,int interrupt_detector) {
        this.Titles="";
        this.URL = URL;
        this.interrupt=interrupt_detector;
    }

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    public HashMap<Elements, String> Crawl(String URL) {
        try {
            HashMap <Elements,String > webSiteTexts =  new HashMap<Elements , String>()  ;
            int i = 0 ;
            Connection connection = Jsoup.connect(URL).userAgent(USER_AGENT);
            org.jsoup.nodes.Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;


            Elements linksOnPage = htmlDocument.select("a[href]");


            Elements webSitefirst = this.Extacttext(URL);
            webSiteTexts.put(webSitefirst , URL ) ;  // put the first elemt in the map

            webSitefirst=this.htmlDocument.select("h1");
            if(webSitefirst.hasText())
                webSiteTexts.put(webSitefirst , URL ) ;  // put the headers1

            webSitefirst=this.htmlDocument.select("h2");
            if(webSitefirst.hasText())
                webSiteTexts.put(webSitefirst , URL ) ;  // put the headers2

            webSitefirst=this.htmlDocument.select("h3");
            if(webSitefirst.hasText())
                webSiteTexts.put(webSitefirst , URL ) ;  // put the headers3

            webSitefirst=this.htmlDocument.select("title");
            if(webSitefirst.hasText())
                webSiteTexts.put(webSitefirst , URL ) ;  // put the titles



            System.out.println("found  no of links = " + linksOnPage.size());  // for the first link



            boolean check = true;
            int visitedUrls = 1 ;  // for trial

            int Count = 0  ;

            for (Element link : linksOnPage) {
                if ( visitedUrls > 100 )
                    break;  // for trial



                System.out.println("link " + link.absUrl("href"));  // for the links inside the first link
                String currentURL=link.absUrl("href");
                java.sql.Connection connectionDB = null;
                try {
                    /*connecting to database*/
                    connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "0110084949");
                    Statement statement = connectionDB.createStatement();
                    ResultSet resultset = statement.executeQuery("SELECT * FROM crawled WHERE URL =" + "'" + currentURL+ "'");
                    if(!resultset.next()){//not visited before

                        boolean executedSuccessfully =statement.execute("INSERT INTO crawled (URL,ThreadNum,interrupt) VALUES('"+currentURL+"','"+Thread.currentThread().getName()+"','"+this.interrupt+"');");//insert url in db
                        System.out.println("not visited before");

                        connection = Jsoup.connect(currentURL).userAgent(USER_AGENT);
                        this.htmlDocument = connection.get();

                        webSitefirst=this.htmlDocument.select("h1");
                        if(webSitefirst.hasText())
                            webSiteTexts.put(webSitefirst , URL ) ;  // put the headers1

                        webSitefirst=this.htmlDocument.select("h2");
                        if(webSitefirst.hasText())
                            webSiteTexts.put(webSitefirst , URL ) ;  // put the headers2

                        webSitefirst=this.htmlDocument.select("h3");
                        if(webSitefirst.hasText())
                            webSiteTexts.put(webSitefirst , URL ) ;  // put the headers3

                        webSitefirst=this.htmlDocument.select("title");
                        if(webSitefirst.hasText())
                            webSiteTexts.put(webSitefirst , URL ) ;  // put the titles


                        /*Crawl the website wasn't visited before*/
                        Elements webSiteText = this.Extacttext(link.absUrl("href"));

                        webSiteTexts.put(webSiteText , link.absUrl("href")) ;
                        visitedUrls++;  //increase the visited urls
                    }
                    connectionDB.close();//closing connection of sql
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            return webSiteTexts ;

        } catch (IOException e) {
            System.out.println("error occure ");
        }
        return  null ;
    }


    public Elements Extacttext(String HTML) {
        try {
            this.htmlDocument = Jsoup.connect(HTML).get();
            Elements paragraphs = this.htmlDocument.select("p");
            for (Element p : paragraphs)
                System.out.println(p.text());

            return paragraphs;

        } catch (IOException e) {
            System.out.println("cannot get paragraphs");
        }
        return null;
    }

    public List<String> getLinks() {
        return this.Links;
    }


    public HashMap<Elements ,String > startCrawling ( String entryHTTP ) {

        try {

//            CrawlerHelper myCrawler = new CrawlerHelper(this.URL,this.interrupt);
            /*--------------Interrupt Checking-------------*/
            java.sql.Connection connectionDB1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "0110084949");
            Statement statement = connectionDB1.createStatement();
            //System.out.println("-------interrupt"+this.interrupt+"----------------------Thread "+Thread.currentThread().getName()+"-----------------------");
            ResultSet resultset = statement.executeQuery("SELECT * FROM crawled WHERE Threadnum='"+Thread.currentThread().getName()+"' and interrupt='"+this.interrupt+"' ORDER BY ID DESC");
            String newURL="-1";

            while(resultset.next()){
                System.out.println("----------------------------------------------INTURRPET DETECTED--------------------------------------------------------------");
                System.out.println("----------------------------------------------Curr Web:  "+entryHTTP+"--------------------------------------------------------------");
                newURL=resultset.getString("URL");
                System.out.println("----------------------------------------------Changed to Web:  "+newURL+"--------------------------------------------------------------");
                break;
            }
            connectionDB1.close();
            HashMap<Elements, String> crawlResult;
            if(newURL!="-1"){
                crawlResult= this.Crawl(newURL);
            }else{
                crawlResult = this.Crawl(entryHTTP);
            }

            if (crawlResult != null)
                return crawlResult; // this will return to us all the work
                // of the crawler ready to be indexed

            else throw new Exception();
        } catch (Exception e) {

            System.out.println(" check the crawling a problem occur there ");
            return null;
        }
    }

}