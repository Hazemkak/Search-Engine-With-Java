import React,{Component} from 'react';
import SearchWordService from '../api/SearchWordService.jsx';
import './ResultPage.css';
import WaitPage from './WaitPage.jsx';

/* Intializing Components */
const SpeechRecognition=window.SpeechRecognition || window.webkitSpeechRecognition;
const mic=new SpeechRecognition();

mic.continous=true;
mic.interimResults=true;
mic.lang='en-US';

class ResultPage extends Component{

    constructor(){
        super();
        this.state={
            noNext:0,
            start:0,
            end:0,
            pageNumber:1,
            resultTuples:[],
            islistening:false,
            inputText:"",
            recordOn:"⚫",
            suggestionsArr:[],
            wait:false
        }
    }


    render=()=>{
        return(
            <>
                <div>
                    <div className="row">
                        <div className="col-8 searchResult">
                            <div className="input-group mb-3 ">
                                <input list="browsers" value={this.state.inputText} onChange={this.changeText} type="text" className="form-control" placeholder="Search Something" id="word" required></input>
                                <datalist id="browsers">
                                    {
                                        this.state.suggestionsArr.map(
                                            suggestTuple=>
                                            <option value={suggestTuple}/>
                                        )
                                    }
                                </datalist>
                                <div className="input-group-append">
                                    <button onClick={this.searchHandler} className="btn btn-outline-success" type="button">Search</button>
                                    <button onClick={this.handleListen} className="btn btn-outline-success" type="button">{<span>{this.state.recordOn}</span>}🎙</button>
                                </div>
                            </div>
                            <div>

                            </div>
                        </div>
                        <div className="col-4"></div>
                    </div>
                    <hr className="hrResult"></hr>
                    <br></br>
                    {this.state.wait && <div className="centerDiv"><WaitPage></WaitPage></div>}
                    {
                        this.state.resultTuples.slice(this.state.start,this.state.end).map(
                            tuple=>
                            <ResultWebBox url={tuple.webUrl} title={tuple.title} words={tuple.paragraph}></ResultWebBox>
                            
                        )
                    }
                    <br></br>
                    <br></br>
                    <div className="row">
                        <div className="col-sm-4">
                            { this.state.start!==0 && <button className="btn btn-success btn-lg" onClick={this.getPreviousPage}>PREVIOUS</button>}
                        </div>
                        <div className="col-sm-4">
                            {this.state.resultTuples.length!==0 && <span className="text-muted">PAGE {this.state.pageNumber} / {Math.ceil(this.state.resultTuples.length/10)}</span>}
                        </div>
                        <div className="col-sm-4">
                            {this.state.noNext===1 && <button className="btn btn-success btn-lg" onClick={this.getNextPage}>NEXT</button>}
                        </div>
                    </div>
                    <br></br>
                </div>
            </>
            
        )
    }

    changeText=(event)=>{
        this.setState({
            inputText:event.target.value
        })
        let wordContentSuggest=document.querySelector("#word").value;
        SearchWordService.getSuggestionWord(wordContentSuggest)
        .then(
            response=>{
                console.log("recieved the suggestions");
                //console.log(response['data']);
                let maxNumber=10;
                /*validate that the end of looping will be valid*/
                if(response['data'].length!=0){
                    this.setState({suggestionsArr:response['data']});
                }
            }
        )
        .catch(
            err=>{
                console.log(err.response);
            }
        )
    }

    

    handleListen=()=>{
        this.state.islistening=!this.state.islistening;
        
        
        if(this.state.islistening){
            this.setState({recordOn:"🔴"});
            mic.start();
            mic.onend=()=>{
                console.log("continue");
                mic.start();
            }
        }else{
            this.setState({recordOn:"⚫"});
            mic.stop();
            mic.onend=()=>{
                console.log("stopped mic");
            }
        }
        mic.onstart=()=>{
          console.log("mic is on");
        }
    
        mic.onresult=event=>{
          const transcript=Array.from(event.results)
          .map(result=>result[0])
          .map(result=>result.transcript)
          .join('');
          this.setState({inputText:transcript});
          console.log(transcript);
          mic.onerror=event=>{
            console.log(event.error);
          }
        }
    }

    getPreviousPage=()=>{

        this.setState({
            end:this.state.start,
            start:this.state.start-10,
            noNext:1,
            pageNumber:this.state.pageNumber-1
        });
    }

    getNextPage=()=>{
        console.log(this.state.end);
        console.log(this.state.resultTuples.length);
        let number=this.state.end;
        number+=10;
        if(number>this.state.resultTuples.length){
            number=this.state.resultTuples.length;
            this.setState({
                start:this.state.end,
                end:number,
                noNext:0,
                pageNumber:this.state.pageNumber+1
            });
        }else{
            this.setState({
                start:this.state.end,
                end:this.state.end+10,
                noNext:1,
                pageNumber:this.state.pageNumber+1
            });
        }
        
    }

    searchHandler=()=>{
        
        let wordContent=document.querySelector("#word").value;
        console.log(wordContent);
        if(!wordContent){//if there is no entry
            alert("Please Enter An Input First");
            return;
        }else{
            this.setState({wait:true});
            SearchWordService.getSearchedWord(wordContent)
            .then(
                response=>{
                    console.log("Displaying Results now.... "+response['data'].length);

                    let maxNumber=10;
                    /*validate that the end of looping will be valid*/
                    if(response['data'].length==0){
                        alert("No Results Found");
                        this.setState({wait:false});
                        return;
                    }
                    else if(maxNumber>response['data'].length){
                        maxNumber=response['data'].length;
                        this.setState({
                            resultTuples:response['data'],
                            start:0,
                            end:maxNumber,
                            noNext:0,
                            wait:false
                        });
                        
                    }else{
                        this.setState({
                            resultTuples:response['data'],
                            start:0,
                            end:maxNumber,
                            noNext:1,
                            wait:false
                        });
                    }
                    // response['data']=[];
                }
            )
            .catch(
                err=>{
                    console.log(err.response);
                }
            )
        }
        
    }
}

class ResultWebBox extends Component{
    constructor(props){
        super(props);
        //console.log(this.props);
    }

    render=()=>{
        return(
            <>
                <div className="card-body webResult">
                    <cite className="text-muted">{this.props.url}</cite>
                    <a href={this.props.url}>
                        <h4 className="card-title">
                            {this.props.title}
                        </h4>
                    </a>
                    <hr></hr>
                    <small>{this.props.words}</small>
                </div>
            </>
        )
    }
}

export {ResultPage};