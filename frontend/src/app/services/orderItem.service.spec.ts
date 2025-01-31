/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { OrderItemService } from './orderItem.service';

describe('Service: OrderItem', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrderItemService]
    });
  });

  it('should ...', inject([OrderItemService], (service: OrderItemService) => {
    expect(service).toBeTruthy();
  }));
});
