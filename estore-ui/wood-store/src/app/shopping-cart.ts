import { Product } from "./product";
/**
 * representation of the Shopping Cart
 * (this is no longer used)
 */
export interface ShoppingCart{
    shoppingCart: Map<Product, number>;
    isOrderComplete: boolean;
    totalCost: number;
}
