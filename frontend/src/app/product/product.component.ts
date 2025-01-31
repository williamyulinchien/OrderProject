// src/app/components/product-list/product-list.component.ts
import { Component, OnInit } from '@angular/core';
import { ProductService } from 'src/app/services/product.service';
import { Product } from '../model/Product';
import { HttpErrorResponse } from '@angular/common/http';
import { Form, NgForm } from '@angular/forms';

@Component({
  selector: 'app-product-list',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class ProductComponent implements OnInit {
  products: Product[] = [];
  product: Product =  {
    productId:0,
    productName: '',
    price: 0,
    description: '',
    quantity: 0,
    productImageUrl: '',
  }
 

  constructor(private productService: ProductService) {
  }
  isEditMode = false;
  

  ngOnInit(): void {
    this.getProducts();
    
  }
  onEdit(product: Product): void {
    this.isEditMode = true;
    this.product = { ...product }; 
  }
  
  getProducts() {
    this.productService.getProducts().subscribe((products) => {
      this.products = products;
      console.log(this.products)
    });
  }

  deleteProduct(productId?: number) {
    if (productId === undefined) {
      console.error('Invalid ID: ID is undefined.');
      alert('Failed to delete user. Invalid ID.');
      return;
    }
    const confirmDelete = confirm('Are you sure you want to delete this product?');
    if (confirmDelete) {
    this.productService.deleteProduct(productId).subscribe(() => {
      this.getProducts();
    });}(error: HttpErrorResponse) => {
            console.error('An error occurred:', error.message);
            alert('Failed to delete product. Please try again later.');
          }
  }
  onSubmit(form: NgForm) {
      if (form.valid) {
        console.log(this.product.productId)
        if (this.product.productId) {
        this.productService.updateProduct(this.product).subscribe(
          (response) => {
            console.log('Product updated successfully', response);
            this.getProducts();
            form.resetForm();
          },
          (error) => {
            console.error('Error updating user', error);
          }
        );
      } else {

        delete this.product.productId;
        this.productService.createProduct(this.product).subscribe(
          (response) => {
            console.log('Product created successfully', response);
            this.getProducts();
            form.resetForm();
          },
          (error) => {
            console.error('Error creating product', error);
          }
        );
      }
    }

  }}


