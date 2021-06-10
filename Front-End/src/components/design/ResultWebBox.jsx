import React,{Component} from 'react';

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

export default ResultWebBox;