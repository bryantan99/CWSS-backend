import { Component, OnInit } from '@angular/core';
import {AppService} from "../../app.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html'
})
export class HomepageComponent implements OnInit {

  nzSelectedIndex = 0;

  constructor(private app: AppService, private http: HttpClient) {
    http.get('account/get-account').subscribe(data => {
      console.log(data);
    })
  }

  ngOnInit(): void {
  }

  authenticated() {
    return this.app.authenticated;
  }
}
