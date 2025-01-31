import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderItem } from '../model/OrderItem';
@Injectable({
  providedIn: 'root',
})
export class OrderItemService {
  private apiUrl:string = "http://localhost:8080/api/users"

  constructor(private http: HttpClient) {}

  addOrderItem(userId: number, orderId: number, orderItem:any): Observable<OrderItem> {
     const httpOptions = {
          headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
        };
    const url = `${this.apiUrl}/${userId}/orders/${orderId}/item`;
    return this.http.post<OrderItem>(url, orderItem,{ responseType: 'text' as 'json'});
  }


  updateOrderItem(userId: number, orderId: number, itemId: number, orderItem: OrderItem): Observable<OrderItem> {
    const url = `${this.apiUrl}/${userId}/orders/${orderId}/item/${itemId}`;
    return this.http.put<OrderItem>(url, orderItem,{ responseType: 'text' as 'json'});
  }


  deleteOrderItem(userId: number, orderId: number, itemId: number): Observable<void> {
    const url = `${this.apiUrl}/${userId}/orders/${orderId}/item/${itemId}`;
    return this.http.delete<void>(url,{ responseType: 'text' as 'json'});
  }
}

