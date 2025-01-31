import { Product } from "./Product";

export interface OrderItem {
    id: number;
    product: Product;
    productName:string;
    quantity: number;
    price: number;
    subtotal: number;
    orderItemId:number;
  }