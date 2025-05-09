import React, { Component } from 'react';
import {Navbar, Nav,NavItem,NavbarBrand, NavLink} from 'reactstrap';

class AppNav extends Component {
    state = {  } 
    render() { 
        return (
            <div>
              <Navbar color="dark" dark  expand="md">
                <NavbarBrand href="/">Expense tracker Application</NavbarBrand>
                  <Nav className="ml-auto" navbar>
                    <NavItem>
                      <NavLink href=" /">Home</NavLink>
                    </NavItem>
                    <NavItem>
                      <NavLink href="/categories">
                        Categories
                      </NavLink>
                      <NavLink href="/expenses">
                        Expenses
                      </NavLink>
                    </NavItem>
                    
                  </Nav>
              </Navbar>
            </div>
          );
    }
}
 
export default AppNav;