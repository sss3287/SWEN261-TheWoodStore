import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import {User} from '../user';
import {Router} from '@angular/router';
import { SharingService } from '../sharing-service';

/**
 * Allows users to login if they already have an account and routes 
 * to the other parts of the site
 */

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {
  currentUser: User | undefined;
  userArray:User[] = [];
  username: string = "";
  isValidLogin: boolean = true;
  constructor(
    private userService: UserService,
    private router : Router,
    private sharingService: SharingService
  ) { }

  ngOnInit(): void {
    this.getUsers();
  }

  /**
   * Gets all user accounts
   */
  getUsers():void{
    this.userService.getUsers().subscribe(userArray=>this.userArray = userArray);
  }

  /**
   * finds the user's account from their username
   * @param uname username
   * @returns user's account if found, else undefined
   */
  getUserByUserName(uname:string){
    for(let x = 0; x < this.userArray.length; x++){
      if(this.userArray[x].username === uname){
          return (this.userArray[x]);
      }
  }
  return undefined;
}

/**
 * navigate to home-page
 */
navigateToBrowse(){
this.router.navigateByUrl("/browse");
}

/**
 * navigate to admin's home-page view
 */
navigateToAdmin(){
this.router.navigateByUrl("/admin");
}

/**
 * navigate to sign-up page
 */
navigateToSignUp(){
  this.router.navigateByUrl("/signup");
}


/**
 * Logs in user if they have an account
 * Sends user to home-page
 * Sends user to admin home-page if "admin" username is used
 */
  loginSelected(){
    this.username=((document.getElementById("uname") as HTMLInputElement).value);
    this.currentUser=this.getUserByUserName(this.username);
    if(this.currentUser != undefined){
      this.currentUser.isLoggedIn=true;
      this.userService.updateUser(this.currentUser).subscribe(currentUser => this.currentUser = currentUser);
      this.userService.setCurrentUser(this.currentUser);
      this.isValidLogin = true;
      if(this.username != "admin"){
        this.navigateToBrowse();
        this.sharingService.setData(this.currentUser);
      }else{
        this.navigateToAdmin();
      }
    }else{
      this.isValidLogin = false;
    }
  }

}