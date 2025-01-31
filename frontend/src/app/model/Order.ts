import { OrderItem } from "./OrderItem";
import { User } from "./User";


export interface Order {
  orderId: number;
  user: User;
  userId:number;
  orderItems: OrderItem[];
  totalPrice: number;
  status: string;
  createdAt: Date;
  updatedAt: Date;
}
