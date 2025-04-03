import React, { Component } from 'react';
import {Route, Routes, BrowserRouter as Router, useNavigate} from 'react-router-dom';
import Home from './Home';
import Category from './Category';
import Expense from './Expense';

const ExpenseWithNavigation = (props) => {
    const navigate = useNavigate();
    return <Expense {...props} navigate={navigate} />;
};
class App extends Component {
    state = {  } 
    render() {  
        return (
            <Router>
                <Routes>
                    <Route path='/' exact={true} element = {<Home/>}/>
                    <Route path='/categories' exact={true} element = {<Category/>}/>
                    <Route path='/expenses' exact={true} element={<ExpenseWithNavigation/>}/>

                </Routes>
            </Router>
        );
    }
}
 
export default App;