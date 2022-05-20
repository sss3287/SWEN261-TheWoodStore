import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { ProductsComponent } from './products/products.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { HomePageComponent } from './home-page/home-page.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { UserLoginComponent } from './user-login/user-login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { AdminProductDetailComponent } from './admin-product-detail/admin-product-detail.component';

const routes: Routes = [
  { path: 'detail/:id', component: ProductDetailComponent },
  { path: 'products', component: ProductsComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: UserLoginComponent },
  { path: 'browse', component: HomePageComponent },
  { path: 'shoppingcart', component: ShoppingCartComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'admin', component: AdminPageComponent },
  { path: 'admin-detail/:id', component: AdminProductDetailComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}