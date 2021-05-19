import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup = new FormGroup({
    'username': new FormControl(''),
    'password': new FormControl('')
  });

  passwordIsVisible: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  submitForm() {

  }

}
