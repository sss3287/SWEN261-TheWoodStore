import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {FormsModule}        from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserLoginComponent } from './user-login/user-login.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { ProductsComponent } from './products/products.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { HomePageComponent } from './home-page/home-page.component';
import { HttpClientModule } from '@angular/common/http';
import { SignUpComponent } from './sign-up/sign-up.component';
import { MessagesComponent } from './messages/messages.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { ProductSearchComponent } from './product-search/product-search.component';
import { AdminProductDetailComponent } from './admin-product-detail/admin-product-detail.component';
import { SharingService } from './sharing-service';
import { ProductSharingService } from './product-sharing-service';
// import { NgxSliderModule } from "@angular-slider/ngx-slider"; 


@NgModule({
  declarations: [
    AppComponent,
    UserLoginComponent,
    ProductsComponent,
    ProductDetailComponent,
    ShoppingCartComponent,
    HomePageComponent,
    SignUpComponent,
    MessagesComponent,
    AdminPageComponent,
    ProductSearchComponent,
    AdminProductDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
     // NgxSliderModule  
  ],
  providers: [SharingService,
  ProductSharingService],
  bootstrap: [AppComponent]
})
export class AppModule { }
