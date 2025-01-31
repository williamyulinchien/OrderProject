import { Address } from './Address';
import { Order } from './Order';

export interface User {
    userId?: number; 
    firstName: string;
    lastName: string;
    username: string;
    password: string;
    phoneNumber: string;
    email: string;
    dateOfBirth: string;
    address?: Address;
    orders?: Order[];
  }