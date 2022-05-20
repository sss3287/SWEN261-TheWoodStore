import { Component, OnInit } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { UserService } from '../user.service';
// import { Options } from '@angular-slider/ngx-slider';
import { Observable, Subject, of } from 'rxjs';
import {
   debounceTime, distinctUntilChanged, switchMap, isEmpty
 } from 'rxjs/operators';

/**
 * Home-page allows users to browse the sites products
 */

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  products: Product[] = [];
  products$!: Observable<Product[]>;
  private searchPrices = new Subject<number>();
  check: boolean = true;

  value: number = 100;
  /*options: Options = {
      floor: 0,
      ceil: 250
  };*/

  constructor(private productService: ProductService,
    private userService: UserService
    ) { }

  ngOnInit(): void {
    this.getProducts();

    this.products$ = this.searchPrices.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((price: number) => this.productService.searchProducts(null, price )),
    );
  }

  // Push a search term into the observable stream.
  search(price: number): void {
    this.searchPrices.next(price);
    this.check = false;
  }


  /**
   * Gets products in inventory
   */
  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products.slice(0, 50));

     // this.products$.subscribe(() => this.products);

      // copy the products[] into products$
    
  }


  /**
   * Logs user out and directs them to the login page
   */
  homeLogOut() {
    this.userService.logOut();
  }

}
