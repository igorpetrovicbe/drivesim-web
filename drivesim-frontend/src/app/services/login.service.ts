import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private _url: string = "api/auth/login";
  httpOptions = {
    headers: new HttpHeaders().set('Content-Type', 'application/json')
  };

  constructor(private http: HttpClient) { }

  login(user: any): Observable<any> {
    return this.http.post<any>(this._url, JSON.stringify(user), this.httpOptions);
  }
}