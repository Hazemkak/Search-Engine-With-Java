package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.*;

import java.util.*;

@Service
public class InvertedIndex {

    private class Tuple {

        private String webName;
        private int count;
        private float tf;
        private float itf;
        private String Result ="  ";

        public void incrementCount() {
            count++;

        }


        public void addDiscription(int Wordindex, String Word, int totalCount, String totalParagraph) throws java.lang.ArrayIndexOutOfBoundsException
        {
            try {
                totalCount = totalParagraph.length();

                if (totalCount < 100)
                    return;


                else
                {
                    String [] Res = totalParagraph.split("\\W+");
                    totalCount = Res.length ;

                    if (( totalCount - Wordindex )> 50 ) {

                        //this.Result+= Word ;
                        for (int i = Wordindex-1 ; i < (Wordindex + 10); i++)


                            this.Result +=  " "+Res[i] ;
                    }
                }
            }

            catch ( java.lang.ArrayIndexOutOfBoundsException  e )
            {
                System.out.println(" Wordindex :" + Wordindex );
                System.out.println(" Word: " + Word );
                System.out.println(" TotalCount :" + totalCount );
                System.out.println(" totalParagraph :" + totalParagraph );


            }



        }







        public void incrementTF_ITF (int totalCount )
        {
            tf = (float) ((float) this.count / (float) totalCount) ;
            itf = (float) (totalCount / this.count) ;


        }

        public Tuple(String webName, int count) {
            this.webName = webName;
            this.count = count;
        }
    }


    List<String> usellessword = Arrays.asList("able",
            " ","0","1","2","3","4", "5", "6", "7","8","9","10","11","12","13","14","15","16","17","18","19","20",
            "",
            "an",
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "i",
            "j",
            "h",
            "i",
            "g",
            "h",
            "k",
            "l",
            "m",
            "n",
            "o",
            "q",
            "r",
            "s",
            "t",
            "w",
            "x",
            "u",
            "U",
            "y",
            "z",
            "A",
            "B",
            "C",
            "D",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "S",
            "L",
            "P",
            "Q",
            "R",
            "T",
            "W",
            "X",
            "Y",
            "Z",
            "be",
            "about",
            "above",
            "abroad",
            "according",
            "accordingly",
            "across",
            "actually",
            "adj",
            "after",
            "afterwards",
            "again",
            "against",
            "ago", "ahead", "ain’t", "all", "allow", "allows", "almost", "alone", "along", "alongside", "already",
            "also",
            "although",
            "always",
            "am",
            "amid",
            "amidst",
            "among",
            "amongst",
            "an",
            "and",
            "another",
            "any", "anybody",
            "anyhow",
            "anyone",
            "anything",
            "anyway",
            "anyways",
            "anywhere",
            "apart",
            "appear",
            "appreciate",
            "appropriate",
            "are",
            "aren’t",
            "around",
            "as",
            "as",
            "aside",
            "ask",
            "asking",
            "associated",
            "at",
            "available",
            "away",
            "awfully");

    Map<String, List<Tuple>> invertedindex = new HashMap<String, List<Tuple>>();


    public void index(String pharagraphs, String webNam) {

        int numWords = 0;

        int totallength = pharagraphs.length();
        for (String words : pharagraphs.split("\\W+")) {
            String word = words.toLowerCase();




            int length = word.length() ;

            if (length > 5) {

                if (Character.compare(word.charAt(length - 1), 's') == 0) // we have found an s in the end of string so we need to remove it
                    word = word.substring(0, length - 1);


                else if ((Character.compare(word.charAt(length - 1), 'g') == 0) &&
                        (Character.compare(word.charAt(length - 2), 'n') == 0) &&
                        (Character.compare(word.charAt(length - 3), 'i') == 0)) // we have found an ing in the end of string so we need to remove it

                    word = word.substring(0, length - 3);

                else if ((Character.compare(word.charAt(length - 1), 'd') == 0) && (Character.compare(word.charAt(length - 2), 'e') == 0))

                    word = word.substring(0, length - 2);
            }


            if (usellessword.contains(word))
            { numWords++;
                continue;
            }

            List<Tuple> idx = invertedindex.get(word);
            if (idx == null) {  // check if it found in the tuples

                idx = new LinkedList<Tuple>();
                invertedindex.put(word, idx);
                Tuple tup =new Tuple(webNam, 1);
                idx.add(tup);
                tup.incrementTF_ITF(totallength);

                totallength = pharagraphs.length();
                tup.addDiscription(numWords,word,totallength, pharagraphs );


            } else      // in case of that the word exist
            {
                List<Tuple> allTuples = invertedindex.get(word);
                boolean found = false;

                for (Tuple tup : allTuples) {
                    if (tup.webName.equals(webNam)) { // the word was found in the already saved website
                        tup.incrementCount();
                        tup.incrementTF_ITF(totallength);
                        found = true;


                    }

                }

                if (!found)  // the word was found in a new site
                {
                    Tuple t =new Tuple(webNam, 1);
                    allTuples.add(t);
                    t.incrementTF_ITF(totallength);
                    totallength = pharagraphs.length();
                    t.addDiscription(numWords,word,totallength, pharagraphs );
                }

            }

            numWords++;





        }




        System.out.println("indexed " + webNam + " " + numWords + " words");
    }

    public void search(List<String> words) {


        for (String _word : words) {

            int i = 0;
            ArrayList<String> answerString = new ArrayList<String>();
            ArrayList<Integer> answerCount = new ArrayList<Integer>();

            String word = _word.toLowerCase();
            List<Tuple> idx = invertedindex.get(word);
            if (idx != null) {

                for (Tuple t : idx) {
                    answerString.add(i, t.webName);
                    answerCount.add(i, t.count);
                    i++;
                }
            }
            System.out.println(word);
            int CountWebsite = 0;
            for (int j = 0; j < i; j++) {
                System.out.println("Website name " + answerString.get(j));
                System.out.println("Number of Occurence " + answerCount.get(j));
                CountWebsite++;
            }


            System.out.println(" no of sites  " + CountWebsite);

        }


    }


    public boolean startIndexing(HashMap<Elements, String> crawlResult) {


        if (crawlResult == null) {
            return false;
        }


        Iterator i = crawlResult.entrySet().iterator();
        while (i.hasNext())  // each string in the crawler result) // loop over all sites which returned from crawler
        {
            Map.Entry pair = (Map.Entry) i.next();

            System.out.println("Here is the key " + (String) pair.getValue());

            Elements res = (Elements) pair.getKey();


            if (res != null) {
                String paragraph ="" ;
                for (Element r : res) // loop over all paragraphs  for the website
                {
                    paragraph +=  r.text() ;
                }


//                try {
//                    Document htmlDocument = Jsoup.connect((String) pair.getValue() ).get();
//                    paragraph+=" "+htmlDocument.title();
//
//                      Elements Ancher = htmlDocument.getElementsByTag("a");
//
//                 for(Element M : Ancher)  // to get the ancher tags as text
//                     paragraph+= M.text()  ;
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


                // URL
                this.index(paragraph, (String) pair.getValue());
                System.out.println(paragraph);
            }
            i.remove();

        }
        return true;


    }


    public void saveinDatabase() {

        try {
            java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "Mashy1234");
            Statement statement = connection.createStatement();
            ResultSet resultset = statement.executeQuery("select * from words");


            int count = 0;


            Iterator i = invertedindex.entrySet().iterator();

            while (i.hasNext()) {
                Map.Entry pair = (Map.Entry) i.next();

                String Word = pair.getKey().toString();
                List<Tuple> idx = invertedindex.get(Word);


                if ( Word.length() > 400    )
                    continue;

                String query = "insert into words (Word ,Website,Count) values(?,?,?)";

                PreparedStatement pstmt = connection.prepareStatement(query);
                if (idx != null) {
                    for (Tuple t : idx) {
                        String web = t.webName;
                        String  parag = t.Result ;
                        float tf = t.tf ;
                        float itf = t.itf ;

                        if (web.length() > 400 )
                            continue;

                        statement.execute("INSERT INTO words (Word ,Website,tf , itf ,paragraph) VALUES('"+ Word+"','"+ t.webName + "',"+tf+","+itf + ",'" +parag +"');" ) ; //insert url in db

//
//                        pstmt.setString(1, pair.getKey().toString());
//                        pstmt.setString(2, t.webName);
//                        pstmt.setInt(3, t.count);
//                        pstmt.executeUpdate();
                    }

                }

                count++;
                i.remove();
            }

            //System.out.println(count);


            connection.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }



}