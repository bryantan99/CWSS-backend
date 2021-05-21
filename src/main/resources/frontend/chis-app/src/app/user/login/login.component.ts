import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {CustomValidationService} from "../../shared/services/custom-validation.service";
import {AuthService} from "../../auth/auth.service";
import {finalize} from "rxjs/operators";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  submitted: boolean = false;

  passwordIsVisible: boolean = false;

  constructor(private fb: FormBuilder,
              private customValidator: CustomValidationService,
              private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  submitForm() {
    this.submitted = true;
    this.authService.login(this.loginForm)
      .pipe(finalize(() => {
        this.submitted = false;
      }))
      .subscribe(resp => {
      console.log(resp);
    }, error => {
      console.log("Encountered problem when logging in.", error);
    });
  }

}
