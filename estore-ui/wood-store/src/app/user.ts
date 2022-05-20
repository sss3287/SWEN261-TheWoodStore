import { Product } from "./product";

/**
 * front-end representation of user
 * matches back-end representation with exception to admin:
 * back-end admin's cart is null
 */
export interface User {
    id: number;
    username: string;
    isLoggedIn: boolean;
    cart: Product[];
  }