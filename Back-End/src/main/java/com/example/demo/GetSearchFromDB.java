package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@Service
public class GetSearchFromDB {
    private static List<SearchData> searchList;
    private static String[] suggestions;

    public List<SearchData> getAll(String word){
        return RetrieveSearchedWord(word);
    }


    public String[] getSuggestions(String currWord){
        suggestions=new String[5];
        int counterSuggestions=0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "0110084949");
            Statement statement = connection.createStatement();

            currWord=currWord.replace( "'", "" );//removing ' to not ruin the query

            int length = currWord.length() ;


            if ( Character.compare(currWord.charAt(length-1), 's') == 0) // we have found an s in the end of string so we need to remove it
                currWord = currWord.substring(0,length-1) ;



            else if ( (Character.compare(currWord.charAt(length-1), 'g') == 0) &&
                    ( Character.compare(currWord.charAt(length-2), 'n') == 0) &&
                    ( Character.compare(currWord.charAt(length-3), 'i') == 0) ) // we have found an ing in the end of string so we need to remove it

                currWord = currWord.substring(0,length-3) ;

            else if ((Character.compare(currWord.charAt(length-1), 'd') ==0)  && (Character.compare(currWord.charAt(length-2), 'e')==0  ))

                currWord = currWord.substring(0,length-2) ;


            ResultSet resultset = statement.executeQuery("select * from suggestions where suggest like " + "'%" + currWord + "%' ;");
            while (resultset.next() && counterSuggestions<5) {//adding the first 5 suggestions
                suggestions[counterSuggestions]=resultset.getString("suggest");
                counterSuggestions++;
            }
            connection.close();
        }
        catch(Exception e ) {
            e.printStackTrace();
        }

        return suggestions;
    }

    public  List<SearchData> RetrieveSearchedWord(String target ) {
        searchList=new ArrayList<SearchData>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "0110084949");
            Statement statement = connection.createStatement();
            //target=target.replace( "'", "" );//removing ' to not ruin the query

            ResultSet resultset1 = statement.executeQuery("select * from suggestions where SUGGEST ='" + target + "'");
            int querySize=0;
            while(resultset1.next()) {
                querySize++;
                break;
            }
            if(querySize==0){//if word wasn't added in suggestions before add it
                statement.execute("insert into suggestions (suggest) values('"+target+"');");
            }



            int length = target.length() ;


            if ( Character.compare(target.charAt(length-1), 's') == 0) // we have found an s in the end of string so we need to remove it
                target = target.substring(0,length-1) ;



            else if ( (Character.compare(target.charAt(length-1), 'g') == 0) &&
                    ( Character.compare(target.charAt(length-2), 'n') == 0) &&
                    ( Character.compare(target.charAt(length-3), 'i') == 0) ) // we have found an ing in the end of string so we need to remove it

                target = target.substring(0,length-3) ;

            else if ((Character.compare(target.charAt(length-1), 'd') ==0)  && (Character.compare(target.charAt(length-2), 'e')==0  ))

                target = target.substring(0,length-2) ;


            String[] possibilties = new String[4];
            possibilties[0] = target;
            possibilties[1] = target + "ed";
            possibilties[2] = target + "ing";
            possibilties[3] = target + "s";

            int i = 0;
            while (i < 4) {
                ResultSet resultset = statement.executeQuery("select * from words where Word =" + "'" + possibilties[i] + "'");
                while (resultset.next()) {


                    String WebsiteURL = resultset.getString("Website");//getting the website url
                    org.jsoup.Connection connection1 = Jsoup.connect(WebsiteURL).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1");
                    org.jsoup.nodes.Document htmlDocument = connection1.get();
                    String WebsiteTitle=htmlDocument.title();
                    String WebsiteParagraph=resultset.getString("paragraph");
                    //Elements paragraphs=htmlDocument.getElementsByTag("p");

                    /*Adding the tuple to the array of results*/
                    searchList.add(new SearchData(WebsiteTitle,WebsiteURL,WebsiteParagraph));
                }
                i++;
            }
            connection.close();

        }
        catch(Exception e )
        {
            e.printStackTrace();

        }

        return searchList;

    }
}
