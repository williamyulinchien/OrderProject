import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../model/Order';
import { OrderItem } from '../model/OrderItem';
@Injectable({
  providedIn: 'root'
})
export class OrderService {
private apiUrl = "http://localhost:8080/api";

constructor(private http: HttpClient) {}

getAllOrders(){
  const url =`${this.apiUrl}/orders`;
  return this.http.get<Order[]>(url)
}
createOrder(userId: number, orderItems: OrderItem[]): Observable<Order> {
  const url = `${this.apiUrl}/users/${userId}/orders`;
  return this.http.post<Order>(url, orderItems);
}
getUserOrders(userId: number): Observable<Order[]> {
  const url = `${this.apiUrl}/users/${userId}/orders`;
  return this.http.get<Order[]>(url);
}
getOrderById(userId: number, orderId: number): Observable<Order> {
  const url = `${this.apiUrl}/users/${userId}/orders/${orderId}`;
  return this.http.get<Order>(url);
}
updateOrder(userId: number, orderId: number, order: Order): Observable<Order> {
  const url = `${this.apiUrl}/users/${userId}/orders/${orderId}`;
  return this.http.put<Order>(url, order,{ responseType: 'text' as 'json'});
}
deleteOrder(userId: number, orderId: number): Observable<void> {
  const url = `${this.apiUrl}/users/${userId}/orders/${orderId}`;
  return this.http.delete<void>(url, { responseType: 'text' as 'json'});
}


}
