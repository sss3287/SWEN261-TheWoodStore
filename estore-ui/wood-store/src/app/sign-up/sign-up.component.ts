import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import {User} from '../user';
import {Router} from '@angular/router';
import { SharingService } from '../sharing-service';

/**
 * A new user can make an account if their username is not 
 * already in use and then be logged in and brought to the homepage
 */
@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['../user-login/user-login.component.css', './sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  isValidSignUp:boolean = true;
  newUser: User | undefined;
  users:User[] = [];
  name: string = "";
  constructor(
    private userService: UserService,
    private router: Router,
    private sharingService: SharingService
  ) { }

  ngOnInit(): void {
    this.getUsers();
  }

  /**
   * Gets all user accounts
   */
  getUsers():void{
    this.userService.getUsers().subscribe(users =>this.users = users);
  }

  /**
   * directs user to the homepage
   */
  navigateToBrowse(){
    this.router.navigateByUrl("/browse");
  }

  /**
   * directs user to the login page
   */
   navigateToLogin(){
    this.router.navigateByUrl("/login");
  }

  /**
   * Adds the new user account to backend
   * @param username 
   * @returns http status
   */
  add(username: string): void {    
    username = username.trim();    
    if (!username) { return; } 
    let isLoggedIn = true;   
    let cart = new Array;
    this.newUser = {username, isLoggedIn, cart} as User;
    this.sharingService.setData(this.newUser);
    this.userService.addUser(this.newUser)
      .subscribe(user => {
        this.users.push(user);
      });
  }

  /**
   * If the username is already taken, a new account can not be created with that name
   * 
   * If the name is free and not an empty string, a new account is created and user is
   * logged in and brought to the homepage
   */
  createUser(){
    this.name = ((document.getElementById("uname") as HTMLInputElement).value);
    let userExists:Boolean = false;
    for(let user of this.users){
      if (user.username === this.name){
        userExists = true;
        this.isValidSignUp = false;
        break;
      }
    }
    if (userExists != true && this.name != ""){
      this.isValidSignUp = true;
        this.add(this.name);
        this.navigateToBrowse();
    }
  }
}
