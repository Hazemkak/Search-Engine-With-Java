package com.example.demo;

public class SearchData{
    private String Title;
    private String WebUrl;
    private String Paragraph;

    public SearchData(String title, String website, String paragraph) {

        this.Title = title;
        this.WebUrl = website;
        this.Paragraph = paragraph;
    }

    public String getTitle() {
        return this.Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getWebUrl() {
        return this.WebUrl;
    }

    public void setWebUrl(String webUrl) {
        this.WebUrl = webUrl;
    }

    public String getParagraph() {
        return this.Paragraph;
    }

    public void setParagraph(String paragraph) {
        this.Paragraph = paragraph;
    }
}