import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../model/User';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
apiUrl:string = "http://localhost:8080/api/users"
constructor(private http:HttpClient)
{ }
loadUser(){
  return this.http.get(this.apiUrl)
}

createUser(user: User): Observable<User> {
  const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };
  return this.http.post<User>(this.apiUrl,user,httpOptions);
}
deleteUser(userId: number): Observable<any> {
  return this.http.delete<void>(`${this.apiUrl}/${userId}`, { responseType: 'text' as 'json' });
}

updateUser(user: User): Observable<User> {
  const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };
  return this.http.put<User>(`${this.apiUrl}/${user.userId}`, user, httpOptions);
}

}
