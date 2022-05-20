import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { Product } from './product';
import { ShoppingCart } from './shopping-cart';     
import { MessageService } from './message.service';
import { SharingService } from './sharing-service';
import { User } from './user';
import { UserService } from './user.service';
import { ProductService } from './product.service';
import { Router } from '@angular/router';
import { ProductSharingService } from './product-sharing-service';

/**
 * Methods for cart functionality, including add and remove product
 */

@Injectable({ providedIn: 'root' })
export class ShoppingCartService {

  private shoppingCartUrl = 'http://localhost:8080/shoppingCart'; 

  currentUser: User | undefined;
  cart: Product[] | undefined;
  currQuantity: number = 0;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService,
    private sharingService: SharingService,
    private userService: UserService,
    private productService: ProductService,
    private router: Router,
    private productSharingService: ProductSharingService
) { }

  /**
   * directs user to their cart
   */
 navigateToCart(){
  this.router.navigateByUrl("/shoppingcart");
}

  /** GET cart from the server */
  getCart(id : number): Observable<ShoppingCart> {

    const url = `${this.shoppingCartUrl}/${id}`;

    return this.http.get<ShoppingCart>(url)
      .pipe(
        tap(_ => this.log('fetched shopping cart')),
        catchError(this.handleError<ShoppingCart>('getShoppingCartERROR'))
      );
  }

  /**
   * decreases product quantity in inventory by 1
   * helper for addToCartFr()
   * 
   * @param productUpdate 
   */
  updateQuantityAdd (productUpdate: Product) {
    let id = productUpdate.id;
    let name = productUpdate.name;
    let price = productUpdate.price;
    let quantity = productUpdate.quantity - 1;
    productUpdate = {name, price, quantity, id} as Product;
    this.productService.updateProduct(productUpdate).subscribe(product => productUpdate = product);
  }

  /**
   * Adds product to user's cart and directs them to their cart
   * 
   * @param product 
   */
  addToCartFr(product: Product) {
    this.currentUser = this.sharingService.getData();
    if (this.currentUser != undefined) {
      this.cart = this.currentUser.cart;
      this.cart.push(product);
      this.userService.updateUser(this.currentUser).subscribe(currentUser => this.currentUser = currentUser);
      this.sharingService.setData(this.currentUser);
      this.updateQuantityAdd(product);
      this.navigateToCart();
    }
  }

  /**
   * returns size of cart
   * @param cart 
   * @returns 
   */
  getCartLength(cart: Product[]): number {
    return cart.length;
  }

  /**
   * Removes the product from the user's cart
   * 
   * @param product
   */
   removeFromCart(product: Product) {
     this.currentUser = this.sharingService.getData();
     let firstRemoval = this.productSharingService.getData()[1];
     if (firstRemoval) {
      this.currQuantity = this.productSharingService.getData()[0];
     }
     else {
       this.currQuantity++;
     }
     if (this.currentUser != undefined) {
       this.cart = this.currentUser.cart;
      length = this.getCartLength(this.cart);
      this.updateCartQuantity(this.cart, product, this.currQuantity);
      var i: number;
      let foundType = false
      let foundProduct = false;
      //last instance of product type's quantity = updated quantity
      //unless the product is the last of it's type in the cart
      for (i = length - 1; i >= 0; i--) {
        if (this.cart[i].name === product.name) {
          if (!foundType) {
            this.currQuantity = this.cart[i].quantity;
            foundType = true;
          }
        }
      }
      //removes product from cart and updates product and user
      for (i = length - 1; i>= 0; i--) {
        if (product == this.cart[i] && !foundProduct) {
          this.cart.splice(i, 1);
          this.userService.updateUser(this.currentUser).subscribe(currentUser => this.currentUser = currentUser);
          this.sharingService.setData(this.currentUser);
          this.updateQuantityRemove(product, this.currQuantity);
          foundProduct = true;
        }
      }
    }
  }

  /**
   * updates product with +1 quantity
   * removeFromCart() helper
   * @param productUpdate 
   * @param quantity 
   */
  updateQuantityRemove(productUpdate: Product, quantity: number) {
    let id = productUpdate.id;
    let name = productUpdate.name;
    let price = productUpdate.price;
    productUpdate = {name, price, quantity, id} as Product;
    this.productService.updateProduct(productUpdate).subscribe(product => productUpdate = product);
  }

  /**
   * increases the quantity of each product of the same name by 1
   * @param cart 
   * @param product 
   */
  updateCartQuantity(cart: Product[], product: Product, quantity: number) {
    let length = cart.length;
    for (var i = length - 1; i >= 0; i--){
      if (product.name === cart[i].name) {
        cart[i].quantity = quantity;
        quantity++;
      }
    }
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a ProductService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`ShoppingCartService: ${message}`);
  }
}

