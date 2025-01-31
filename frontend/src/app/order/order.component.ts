import { Component, OnInit } from '@angular/core';
import { Order } from '../model/Order';
import { OrderService } from '../services/order.service';
import { HttpErrorResponse } from '@angular/common/http';
import { OrderItemService } from '../services/orderItem.service';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { User } from '../model/User';
import { UserService } from '../services/user.service';
import { Product } from '../model/Product';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {
  orderList: Order[] =[];
  products:Product[]=[];
  orderForm: FormGroup;
  userList:User[]=[]

  constructor(private orderService: OrderService,private orderItemService:OrderItemService,private fb: FormBuilder, private dialog: MatDialog,private userService:UserService,private productService:ProductService) { 
    this.getUsers()
    this.loadOrders();
    this.getProducts()
    this.orderForm = this.fb.group({
      userId: [null, Validators.required],
      orderItems: this.fb.array([]),
    });

    
  }

  ngOnInit(): void {
  }
  getUsers(){
    this.userService.loadUser().subscribe((users:any)=>{
      this.userList = users;
      console.log(this.userList)
    })
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
  onDeleteOrder(userId:number,orderId?: number) {
      if (orderId === undefined) {
        console.error('Invalid ID: ID is undefined.');
        alert('Failed to delete user. Invalid ID.');
        return;
      }
      const confirmDelete = confirm('Are you sure you want to delete this order?');
      if (confirmDelete) {
      this.orderService.deleteOrder(userId,orderId).subscribe(() => {
        this.loadOrders();
      });}(error: HttpErrorResponse) => {
              console.error('An error occurred:', error.message);
              alert('Failed to delete product. Please try again later.');
            }
    }
    updateSubtotal(orderIndex: number, itemIndex: number): void {
      const item = this.orderList[orderIndex].orderItems[itemIndex];
      item.subtotal = item.quantity * item.price;
      this.updateTotalPrice(orderIndex);
    }
    updateTotalPrice(orderIndex: number): void {
      const order = this.orderList[orderIndex];
      order.totalPrice = order.orderItems.reduce((sum, item) => sum + item.subtotal, 0);
    }
    onUpdateOrderItem(orderId:number,userId:number,orderItemId:number,orderIndex: number, itemIndex: number): void {
      const updatedItem = this.orderList[orderIndex].orderItems[itemIndex];
      this.orderItemService.updateOrderItem(orderId,userId,orderItemId,updatedItem).subscribe(
        (response) => {
          console.log('OrderItem update successfully', response);
      
        },
        (error) => {
          console.error('OrderItem update failed', error);
     
        }
      );
    }
    onDeleteOrderItem(userId: number, orderId: number, orderItemId:number){
      const confirmDelete = confirm('Are you sure you want to delete this orderItem?');
      if (confirmDelete) {
        this.orderItemService.deleteOrderItem(userId,orderId,orderItemId).subscribe((response) => {
          console.log('OrderItem delete successfully', response);
      
        });
    }(error: HttpErrorResponse) => {
            console.error('An error occurred:', error.message);
            alert('Failed to delete product. Please try again later.');
          }
        }


    createOrderItem(): FormGroup {
          return this.fb.group({
            userId: [null, Validators.required],
            productId: [null, Validators.required],
            quantity: [1, [Validators.required, Validators.min(1)]]
          });
        }

        
  get orderItems(): FormArray {
          return this.orderForm.get('orderItems') as FormArray;
        }
      
      
  addOrderItem(): void {
    const orderItemGroup = this.fb.group({
      productId: [null, Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]]
    });
    this.orderItems.push(orderItemGroup);
  }

  removeOrderItem(index: number): void {
    this.orderItems.removeAt(index);
  }

  onSubmit(): void {
    if (this.orderForm.invalid) {
      return;
    } 
    const productId = Number(this.orderForm.get('productId')?.value);
    const quantity = Number(this.orderForm.get('quantity')?.value);
    const orderData = {...this.orderForm.value};
    const userId = Number(this.orderForm.get('userId')?.value);
    orderData.productId = Number(orderData.productId);
    orderData.quantity = Number(orderData.quantity);
    console.log(orderData)
        this.orderService.createOrder(userId,orderData).subscribe(
          (response) => {
            console.log('Order created successfully', response);
            this.orderForm.reset();
            this.orderItems.clear();
            this.addOrderItem();
          },
      (error) => {
        console.error('Error creating order:', error);
      }
    );
  }



}
