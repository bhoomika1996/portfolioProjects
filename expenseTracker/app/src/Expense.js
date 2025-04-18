import React, { Component } from 'react';
import AppNav from './AppNav';
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";
import './App.css';
import { Button, Container, FormGroup, Form, Table } from 'reactstrap';
import {Link} from 'react-router-dom';
import moment from 'moment';

// {
//     "id": 1,
//     "expensedate": "2024-03-22T10:30:00Z",
//     "description": "Vietnam trip",
//     "location": "Vietnam", 
//     "category": {
//         "id": 1,
//         "name": "Travel"
//     } 
// }
class Expense extends Component {

    emptyItem = {
        id: null,       //auto-increment
        expensedate: new Date(),
        description: '',
        location: '',
        category:  {id:'',name:''}
    }

    constructor(props){
        super(props)

        this.state = { 
            date: new Date(),
            isLoading: false,
            categories: [],
            expenses: [],
            item: this.emptyItem 
         }
         this.handleSubmit = this.handleSubmit.bind( this);
        this.handleChange = this.handleChange.bind( this);
        this.handleDateChange = this.handleDateChange.bind( this);
    }

    async handleSubmit(event) {
        event.preventDefault();
    
        let newId = this.state.expenses.length > 0 
            ? Math.max(...this.state.expenses.map(exp => exp.id)) + 1 
            : 1; // Auto-increment the ID
    
        const newExpense = { ...this.state.item, id: newId };
    
        await fetch(`/api/expenses`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newExpense)
        });
    
        // Append new expense to the list
        this.setState((prevState) => ({
            expenses: [...prevState.expenses, newExpense], // Append new expense
            item: { ...this.emptyItem, id: null } // Reset form
        }));
    }
    

    handleChange(event){
        
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        if (name === "category") {
            // Find the selected category object and assign it to item
            const selectedCategory = this.state.categories.find(cat => cat.id.toString() === value);
            item.category = selectedCategory || { id: value, name: '' };
        } else {
            item[name] = value;
        }
        this.setState({item});
        console.log(this.state);
    }

    handleDateChange(date){
        let item = {...this.state.item};
        item.expensedate = date;
        this.setState({item});
        console.log(this.state);

    }

    async remove(id){
        await fetch(`/api/expenses/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(()=>{
            let updatedExpenses = [...this.state.expenses].filter( i => i.id!== id);
            this.setState({expenses: updatedExpenses});
        });

    }

    async componentDidMount(){
        const response = await fetch('/api/categories');
        const body = await response.json();
        this.setState({categories :body , isLoading:false});

        const responseExp = await fetch('/api/expenses');
        const bodyExp = await responseExp.json();
        this.setState({expenses :bodyExp , isLoading:false});
    }
    render() { 
        const title = <h3>Add Expense</h3>
        const {categories} = this.state;
        const {expenses,isLoading} = this.state;

        if(isLoading)
            return(<div>Loading....</div> )

            let optionsList  =
            categories.map( (category) =>
                <option value={category.id} key={category.id}>
                            {category.name} 
                </option>
            )
        let rows =
            expenses.map(expense=>
                <tr key={expense.id}>
                    <td>{expense.description}</td>
                    <td>{expense.location}</td>
                    <td>{moment(expense.expensedate).format("YYYY/MM/DD")}</td>
                    <td>{expense.category.name}</td>
                    <td><Button size="sm" color="danger" onClick={()=> this.remove(expense.id) } />Delete</td>
                </tr>

            )
        
        return (
            <div>
                <AppNav/>
                <Container>
                    {title}
                    <Form onSubmit={(e) => this.handleSubmit(e)} >
                        <FormGroup>
                            <label for="description">Description</label>
                            <input type='text' name='description' id='description' onChange={this.handleChange} autoComplete='name'/>
                        </FormGroup>
                        
                        <FormGroup>
                            <label for="category">Category</label>
                            <select name="category" value={this.state.item.category.id} onChange={this.handleChange}>
                                {this.state.categories.map((category) => (
                                    <option key={category.id} value={category.id}>
                                        {category.name}
                                    </option>
                                ))}
                            </select>
                        </FormGroup>

                        <FormGroup>
                            <label for="expenseDate">ExpenseDate</label>
                            <DatePicker selected={this.state.item.expensedate } onChange={this.handleDateChange}/>
                        </FormGroup>

                        <div className='row'>
                        <FormGroup className='col-md-4 mb3'>
                            <label for="location">Location</label>
                            <input type='text' name='location' id='location' onChange={this.handleChange}/>
                        </FormGroup>
                        </div>
                        
                        <FormGroup>
                            <Button type='submit' color='primary'>Save</Button>{' '}
                            <Button color='secondary' tag={Link} to='/'>Cancel</Button>
                        </FormGroup>
                    </Form>
                </Container>

                {' '}

                <Container>
                     <h3>Expense List</h3>
                    <Table className='mt-4 '>
                        <thead>
                            <tr>
                                <th width = "30%">Description</th>
                                <th width = "10%">Location</th>
                                <th>Date</th>
                                <th>Category</th>
                                <th width = "10%">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {rows}
                        </tbody>
                    </Table>
                </Container>
            </div>
            
        );
    }
}
 
export default Expense;