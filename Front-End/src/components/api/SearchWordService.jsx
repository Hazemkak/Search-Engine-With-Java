import axios from "axios";

class SearchWordService{
    getSearchedWord(word){
        return axios.get(`http://localhost:4000/searched/${word}`);
    }
    getSuggestionWord(word){
        return axios.get(`http://localhost:4000/suggestion/${word}`);
    }
}

export default new SearchWordService;