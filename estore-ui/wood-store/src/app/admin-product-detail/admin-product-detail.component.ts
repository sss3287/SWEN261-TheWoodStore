import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { UserService } from '../user.service';

/**
 * Product detail page for admin
 * Product price and quantity can be updated
 */

@Component({
  selector: 'app-admin-product-detail',
  templateUrl: './admin-product-detail.component.html',
  styleUrls: ['./admin-product-detail.component.css']
})
export class AdminProductDetailComponent implements OnInit {
  product!: Product;
  
  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location,
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getProduct();
  }

  /**
   * Gets a specific product
   */
     getProduct(): void {
      const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
      this.productService.getProduct(id)
        .subscribe(product => this.product = product);
    }
  
  /**
   * Updates a new product to inventory
   */
   save(): void {
    if (this.product) {
      this.productService.updateProduct(this.product)
        .subscribe(product => this.product = product);
    }
   }

   /**
    * set admin status to logged out and brings them to login page
    */
   adminDetailLogOut(): void {
    this.userService.logOut();
  }

  /**
   * brings admin back to their browsing page
   */
  adminDetailBrowse(): void {
    this.router.navigateByUrl("/admin");
  }


}
