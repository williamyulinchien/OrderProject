import { Component, OnInit } from '@angular/core';
import { User } from '../model/User';
import { UserService } from '../services/user.service';
import { FormBuilder, FormGroup, Validators ,FormsModule, NgForm} from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  userList:User[]=[];
  user:User =  {
    userId:0,
    firstName: '',
    lastName: '',
    username: '',
    password: '',
    phoneNumber: '',
    email: '',
    dateOfBirth: '',
    address:{
      street: '',
      city: '',
      state: '',
      postalCode: '',
      country: ''
    }
  };
  constructor(private userService:UserService) {
    this.getAllUsers();
   }
  isEditMode = false;
  onEdit(selectedUser: User): void {
    this.isEditMode = true;
    this.user = { ...selectedUser };
    if (Array.isArray(this.user.address)) {
      this.user.address = this.user.address[0] || {
        street: '',
        city: '',
        state: '',
        postalCode: '',
        country: ''
      };
    }
  }
  ngOnInit(): void {
    this.getAllUsers()
    
  }
  getAllUsers() {
    this.userService.loadUser().subscribe(
      (result: any) => {
        this.userList = result.map((user: any) => {
          if (Array.isArray(user.addresses) && user.addresses.length > 0) {
            user.address = { ...user.addresses[0] }; 
          } else {
            user.address = {
              street: '',
              city: '',
              state: '',
              postalCode: '',
              country: ''
            };
          }

          delete user.addresses;
          return user;
        });
      },
      (error) => {
        console.error("Error fetching user data:", error);
      }
    );
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      if (this.user.userId) {

      this.userService.updateUser(this.user).subscribe(
        (response) => {
          console.log('User updated successfully', response);
          this.getAllUsers();
          form.resetForm();
        },
        (error) => {
          console.error('Error updating user', error);
        }
      );
    } else {
      console.log('content:',this.user)
      const userPayload = {
        ...this.user,
        addresses: [this.user.address]
      };
      this.userService.createUser(userPayload).subscribe(
        (response) => {
          console.log('User created successfully', response);
          this.getAllUsers();
          form.resetForm();
        },
        (error) => {
          console.error('Error creating user', error);
        }
      );
    }
  }
  }


  onDelete(id?:number){
    console.log(id)
    
    if (id === undefined) {
      console.error('Invalid ID: ID is undefined.');
      alert('Failed to delete user. Invalid ID.');
      return;
    }

  const confirmDelete = confirm('Are you sure you want to delete this user?');
  if (confirmDelete) {
    this.userService.deleteUser(id).subscribe(
      (result: any) => {
          alert("User deleted successfully");
          console.log(result)
          this.getAllUsers();
      },
      (error: HttpErrorResponse) => {
        console.error('An error occurred:', error.message);
        alert('Failed to delete customer. Please try again later.');
      }
    );
}}
  }

