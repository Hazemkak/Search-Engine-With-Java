import React,{Component} from 'react';
import SearchWordService from '../api/SearchWordService.jsx';

class SearchBox extends Component{
    constructor(){
        super();
    }

    render=()=>{
        return(
            <div className="container">
                <h1 className="searchTitle"><i className="fab fa-searchengin"></i>Go-El8alaba</h1>
                <div className="input-group mb-3 p-3">
                    <input id="type" type="text" className="form-control" placeholder="Search Something"></input>
                    <div className="input-group-append">
                        <button className="btn btn-outline-success" onClick={this.searchClickedHandler} >Search</button>
                    </div>
                </div>
            </div>
        );
    }
    //port 4200
    searchClickedHandler=()=>{
        //console.log("I'm clicked");
        let content=document.querySelector("#type").value;
        console.log(content);
        SearchWordService.getSearchedWord(content)
        .then(
            response=>{
                // console.log(response['data'].length);
                response['data'].map(
                    tuple=>{
                        console.log(tuple.webUrl);
                    }
                )
            }
        )
        .catch(
            err=>{
                console.log(err.response);
            }
        )
    }

}

export {SearchBox};