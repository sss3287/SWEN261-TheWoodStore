import { Component, OnInit } from '@angular/core';
import { ShoppingCart } from '../shopping-cart';
import { ShoppingCartService } from '../shopping-cart.service';
import { Product } from '../product';
import { User } from '../user';
import { UserService } from '../user.service';
import { SharingService } from '../sharing-service';
import { ProductService } from '../product.service';
import { ProductSharingService } from '../product-sharing-service';

/**
 * shopping-cart.component gets the current user and their assigned cart
 */

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: [ './shopping-cart.component.css']
})

export class ShoppingCartComponent implements OnInit {

  shoppingCart: ShoppingCart | undefined;
  currUser: User | undefined;
  products: Product[] | undefined;
  inventory: Product[] = [];
  firstRemoval: boolean = true;
  isComplete:boolean = false;
  total: number | undefined;

  constructor(
    private shoppingCartService: ShoppingCartService,
    private userService: UserService,
    private sharingService: SharingService,
    private productService: ProductService,
    private productSharingService: ProductSharingService) { }

  ngOnInit(): void {
    this.getProducts();
    this.currUser = this.sharingService.getData();
    this.products = this.currUser?.cart;
    this.getUser();
    this.getCart();        
    if(this.shoppingCart != undefined){      
      this.products = Array.from(this.shoppingCart.shoppingCart.keys());
    }
    this.getTotal();
  }

  /**
   * Gets products in inventory
   */
   getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.inventory = products.slice(0, 50));
  }

  /**
   * Gets the current user
   */
  getUser(): void {
    this.currUser = this.userService.getCurrentUser();
  }

  /**
   * Gets the customer's cart when they are logged in
   */
  getCart(): void {
    if(this.currUser != undefined){
      this.shoppingCartService.getCart(this.currUser.id).subscribe(shoppingCart => this.shoppingCart = shoppingCart);                  
    }
  }

  /**
   * gets the current quantity of product from inventory
   * @param inventory 
   * @param product 
   */
   updateCartWithInventory (inventory: Product[], product: Product): number {
    let name = product.name;
    let quantity = 0;
    inventory.forEach(element => {
      if (element.name === name) {
        quantity = element.quantity + 1;
      }
    });
    return quantity;
  }


  /**
   * Removes selected product from the user's cart
   * 
   * @param product 
   */
  removeFromCart(product: Product): void {
    if (this.currUser != undefined) {
      let quantity = this.updateCartWithInventory(this.inventory, product);
      this.productSharingService.setData([quantity, this.firstRemoval]);
      if (this.firstRemoval) {
        this.firstRemoval = false;
      }
    }
    this.shoppingCartService.removeFromCart(product);
    this.getTotal();
  }

  /**
   * sets user status to logged out and brings them to the login page
   */
  cartLogOut () {
    this.userService.logOut();
  }

  /**
   * clears user's cart
   */
  completeOrder(): void {
    this.currUser = this.sharingService.getData();
    if (this.currUser != undefined) {
      let cart = this.currUser.cart;
      let length = cart.length;
      for (var i = 0; i < length; i++) {
        cart.pop();
      }
      this.currUser.cart = cart;
      this.userService.updateUser(this.currUser).subscribe(currentUser => this.currUser = currentUser);
      this.sharingService.setData(this.currUser);
      this.isComplete = true;
    }
  }

  getTotal() {
    this.currUser = this.sharingService.getData();
    let total = 0;
    if (this.currUser != undefined) {
      this.products = this.currUser?.cart;
      this.products.forEach(product => {
        total = total + product.price;
      });
    }
    this.total = total;
  }

}