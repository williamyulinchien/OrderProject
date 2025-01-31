import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule} from '@angular/common/http'
import {FormsModule, ReactiveFormsModule} from'@angular/forms';
import { HomeComponent } from './home/home.component';
import { UserComponent } from './user/user.component';
import { CommonModule } from '@angular/common';
import { ProductComponent } from './product/product.component';
import { OrderItemComponent } from './orderItem/orderItem.component';
import { OrderComponent } from './order/order.component';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [	
    AppComponent,
    HomeComponent,
    UserComponent,
    ProductComponent,
      OrderItemComponent,
      OrderComponent
   ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,CommonModule,ReactiveFormsModule,MatDialogModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
