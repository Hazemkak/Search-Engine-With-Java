import React, { Component } from 'react';
import {BrowserRouter as Router,Route, Switch,Link} from 'react-router-dom';
import {SearchBox} from './SearchBox';
import {ResultPage} from './ResultPage';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'font-awesome/css/font-awesome.min.css';
import './SearchHome.css';

class SearchHome extends Component{
    constructor(){
        super();
    }

    render=()=>{
        return(
            <Router>
                    <>
                        <Header></Header>
                        <Switch>
                            <Route path="/" exact component={ResultPage}></Route>
                        </Switch>
                    </>
            </Router>
        );
    }
}

class Header extends Component{
    constructor(){
        super();
    }

    render=()=>{
        return(
            <nav className="navbar navbar-expand-md  navbar-dark bg-dark mb-4">
            <div className="container-fluid-nav">
                <div>
                    <a className="navbar-brand navbarText" href="#"><h3 className="navbarText">Go-El8alaba 🔎</h3></a>
                </div>
            </div>
            </nav>
        );
    }
}



export {SearchHome};