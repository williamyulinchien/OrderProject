import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { UserComponent } from './user/user.component';
import { ProductComponent } from './product/product.component';
import { OrderItemComponent } from './orderItem/orderItem.component';
import { OrderComponent } from './order/order.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: 'user',
        component: UserComponent,
      },
      {
        path: 'product',
        component: ProductComponent,
      },
      {
        path: 'orderItem',
        component: OrderItemComponent,
      },
      {
        path: 'order',
        component: OrderComponent,
      },
    ],
  },
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
