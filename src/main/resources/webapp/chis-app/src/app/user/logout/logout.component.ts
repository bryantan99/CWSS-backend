import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html'
})
export class LogoutComponent implements OnInit {

  constructor(private authService: AuthService,
              private router:Router) {}

  ngOnInit(): void {
    this.authService.logOut();
    this.router.navigate(['login'])
  }

}