import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiServerUrl = 'http://localhost:8080';

  LOGIN = this.apiServerUrl + "/authenticate";

  constructor(private http:HttpClient) { }

  // Provide username and password for authentication, and once authentication is successful,
  //store JWT token in session
  login(loginForm:  {username: string, password: string}) {
    return this.http.post<any>(this.LOGIN, loginForm)
      .pipe(
        map(userData => {
          sessionStorage.setItem("username", loginForm.username);
          let tokenStr = "Bearer " + userData.token;
          sessionStorage.setItem("token", tokenStr);
          return userData;
        })
      );
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem("username");
    return !(user === null);
  }

  logOut() {
    sessionStorage.removeItem("username");
  }
}
