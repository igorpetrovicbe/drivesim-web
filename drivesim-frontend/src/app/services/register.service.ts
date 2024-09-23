import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserRegister } from '../register/user-register';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class RegisterService {
    private _url: string = 'api/user/register';
    httpOptions = {
        headers: new HttpHeaders().set('Content-Type', 'application/json'),
    };

    constructor(private http: HttpClient) {}
    /*
  register(user: UserRegister): Observable<string> {
    return this.http.post<string>(this._url, JSON.stringify(user), this.httpOptions);
  }
  */

    register(user: UserRegister): Observable<string> {
        return this.http.post(this._url, JSON.stringify(user), {
            ...this.httpOptions,
            responseType: 'text',
        });
    }
}
