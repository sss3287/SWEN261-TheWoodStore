import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { UserService } from '../user.service';

/**
 * The admin page allows the admin to view, add, and delete products in inventory
 */

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {

  products: Product[] = [];

  constructor(
    private productService: ProductService,
    private userService: UserService,
    private router: Router
    ) { }

  ngOnInit(): void {
    this.getProducts();
  }

  /**
   * Gets products from inventory
   */
  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products.slice(0, 50));
  }

  /**
   * Adds product to inventory if the name does not already exist in inventory
   * initially sets a quantity of 1 and price of 0
   * @param name 
   * @returns 
   */
  add(name: string): void {
    let valid = true;
    name = name.trim();
    if (name.length == 0) { return; }
    this.products.forEach(product => {
      if (name.toLowerCase() === product.name.toLowerCase()) {
        valid = false;
        window.alert("Product already exists");
      }
    });
    if (valid) {
      this.productService.addProduct(({ name } as Product))
      .subscribe(product => {
        this.products.push(product);
      });
    }
  }

  /**
   * Deletes a product type from inventory
   * 
   * @param product 
   */
  delete(product: Product): void {
    this.products = this.products.filter(h => h !== product);
    this.productService.deleteProduct(product.id).subscribe();
  }

  /**
   * Logs admin out and directs them to the login page
   */
  adminLogOut(): void {
    this.userService.logOut();
  }

  /**
   * brings admin back to their browsing page
   */
   adminDetailBrowse(): void {
    this.router.navigateByUrl("/admin");
  }

  
}
