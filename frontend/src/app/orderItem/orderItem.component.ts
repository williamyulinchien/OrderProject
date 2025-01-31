import { Component, OnInit } from '@angular/core';
import { ProductService } from '../services/product.service';
import { Product } from '../model/Product';
import { User } from '../model/User';
import { Order } from '../model/Order';
import { UserService } from '../services/user.service';
import { OrderService } from '../services/order.service';
import { OrderItemService } from '../services/orderItem.service';
import { NgForm } from '@angular/forms';
import { OrderItem } from '../model/OrderItem';

@Component({
  selector: 'app-orderItem',
  templateUrl: './orderItem.component.html',
  styleUrls: ['./orderItem.component.css']
})
export class OrderItemComponent implements OnInit {
  orderList: Order[] =[];
  userList: User[]=[];
  products: Product[] = [];
  selectedUserId: number | null = null;
  selectedOrderId: number | null = null;
  filteredOrders: Order[] = [];
  selectedProduct: any = null;
  selectedQuantity: number = 0;
  constructor(private productService: ProductService,private userService:UserService,private orderService:OrderService,private orderItemService:OrderItemService) {
    this.getProducts()
    this.getUsers()
    this.loadOrders()

   }

  ngOnInit() {
  }
  loadOrders(): void {
    this.orderService.getAllOrders().subscribe(
      (orders) => {
        this.orderList = orders;
        
        console.log(this.orderList)
      },
      (error) => {
        console.error('Error fetching orders:', error);
      }
    );
  }
  getProducts() {
    this.productService.getProducts().subscribe((products) => {
      this.products = products;
      console.log(this.products)
    });
  }
  getUsers(){
    this.userService.loadUser().subscribe((users:any)=>{
      this.userList = users;
      console.log(this.userList)
    })
  }
  onUserChange(){
    if (this.selectedUserId !== null) {
      console.log("this.selectedUserId",this.selectedUserId)
      console.log('orderList',this.orderList)
      
      this.filteredOrders = this.orderList.filter(order => order.userId ===Number(this.selectedUserId));
      console.log("this.filteredOrders",this.filteredOrders)
    } else {
      this.filteredOrders = [];
    }
    this.selectedOrderId = null; 
  }
  openOrderItemModal(product: any) {
    this.selectedProduct = product;
    console.log(this.selectedProduct)

  }
  onSubmit(form: NgForm): void {
    console.log(form.value);
    console.log(this.selectedProduct.productId)
    const theOrderItem = {
      productId: this.selectedProduct.productId,
      quantity: form.value.quantity
    };
    this.orderItemService.addOrderItem(form.value.userId,form.value.orderId,theOrderItem ).subscribe(
      (response) => {
        console.log('item added successfully', response);
        form.resetForm();
      },
      (error) => {
        console.error('item added error', error);
      }
    )

    }





}


