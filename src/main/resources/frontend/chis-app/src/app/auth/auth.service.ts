import { Injectable } from '@angular/core';
import {FormGroup} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiServerUrl = '';

  LOGIN = this.apiServerUrl + "/login";

  constructor(private http:HttpClient) { }

  public login(loginForm: FormGroup): Observable<any> {
    return this.http.post(this.LOGIN, loginForm);
  }
}
