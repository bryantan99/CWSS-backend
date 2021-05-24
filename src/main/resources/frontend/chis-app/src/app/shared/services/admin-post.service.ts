import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AdminPostService {

  private apiServerUrl = 'http://localhost:8080';

  GET_ADMIN_POST = this.apiServerUrl + "/post/get-admin-posts";
  ADD_ADMIN_POST = this.apiServerUrl + "/post/add-admin-post";
  DELETE_ADMIN_POST = this.apiServerUrl + "/post/delete-admin-post";

  constructor(private http:HttpClient) { }

  addAdminPost(newPostForm: any):Observable<any> {
    return this.http.post(this.ADD_ADMIN_POST, newPostForm);
  }

  getAdminPost():Observable<any> {
    return this.http.get(this.GET_ADMIN_POST);
  }

  deleteAdminPost(postId: number):Observable<any> {
    const url = this.DELETE_ADMIN_POST + "/" + postId;
    return this.http.delete(url);
  }
}
