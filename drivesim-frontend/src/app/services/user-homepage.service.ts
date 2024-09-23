import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { UserHomepageInfo } from '../user-homepage-info';

@Injectable({
  providedIn: 'root'
})
export class UserHomepageService {
  httpOptions = {
    headers: new HttpHeaders().set('Content-Type', 'application/json'),
};

  constructor(private http: HttpClient) {}

  generate(generationRequest: any): Observable<string> {
    return this.http.post<string>('api/video/generate', generationRequest, {
      headers: { 'Content-Type': 'application/json' }, // Specify content type as JSON
    });
  }

  getUserData(): Observable<UserHomepageInfo> {
    return this.http.get<UserHomepageInfo>('api/user/profile', this.httpOptions);
  }

  getResult(): Observable<any> {
    return this.http.get('api/video/get_result', {
      headers: this.httpOptions.headers,
      responseType: 'text' // Expect a text response, which can also be JSON formatted
    }).pipe(
      map(response => {
        try {
          // Try to parse the response as JSON
          return JSON.parse(response);
        } catch (e) {
          // If parsing fails, return it as plain text
          return response;
        }
      })
    );
  }
}
