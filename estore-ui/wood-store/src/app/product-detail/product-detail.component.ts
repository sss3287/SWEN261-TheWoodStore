import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { WoodTypesEnum, WoodType2LabelMapping } from '../WoodTypes.enum';
import { VarnishTypesEnum, VarnishType2LabelMapping } from '../VarnishTypes.enum';
import { ShoppingCartService } from '../shopping-cart.service';
import { UserService } from '../user.service';

/**
 * Product-detail has the customer choose their desired wood and varnish type
 * Customers have the option to add an engraved message
 * Customers may add the customized product to their cart
 */

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: [ './product-detail.component.css' ]
})

export class ProductDetailComponent implements OnInit {
  product!: Product;
  public WoodType2LabelMapping = WoodType2LabelMapping;
  public woodTypes = Object.values(WoodTypesEnum);
  public VarnishType2LabelMapping = VarnishType2LabelMapping;
  public varnishTypes = Object.values(VarnishTypesEnum);

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location,
    private shoppingCartService: ShoppingCartService,
    private userService: UserService,

  ) {}

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
   * Updates the product with the selected wood type
   * 
   * @param woodType 
   * @returns 
   */
  setWoodType(woodType: string): void {
    woodType = woodType.trim();
    if (!woodType) { return; }
    this.productService.updateProduct({ woodType } as Product)
      .subscribe(product =>
        this.product = product);
  }

  /**
   * Updates the product with the selected varnish type
   * 
   * @param varnishType 
   * @returns 
   */
  setVarnishType(varnishType: string): void {
    varnishType = varnishType.trim();
    if (!varnishType) { return; }
    this.productService.updateProduct({ varnishType } as Product)
    .subscribe(product =>
      this.product = product);
  }

  /**
   * Adds the product to cart only if a wood type and varnish type have been selected
   * sends an alert if item has been added or if customer is missing a product requirement
   * 
   * @param product 
   */
  addToCart(product: Product) {
    if (product.quantity == 0) {
      window.alert("This product is currently out of stock");
    }
    else if (product.woodType == null && product.varnishType == null) {
      window.alert("Please select a Wood Type and Varnish Type")
    }
    else if (product.woodType == null) {
      window.alert("Please select a Wood Type")
    }
    else if (product.varnishType == null) {
      window.alert("Please select a Varnish Type")
    }
    else {
      this.shoppingCartService.addToCartFr(product);
    }
  }

  detailLogOut(): void {
    this.userService.logOut();
  }

  /**
   * returns user to home-page
   */
  goBack(): void {
    this.location.back();
  }

}