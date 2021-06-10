package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class SearchResource {

    @Autowired
    private GetSearchFromDB objDB;

    @GetMapping(path="/searched/{word}")
    public List<SearchData> SearchInDB(@PathVariable String word){
        return objDB.getAll(word);
    }
    @GetMapping(path="/suggestion/{word}")
    public String[] SearchInDBSuggest(@PathVariable String word){
        return objDB.getSuggestions(word);
    }
}
