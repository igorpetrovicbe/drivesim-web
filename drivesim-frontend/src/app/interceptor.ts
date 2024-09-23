import { Injectable } from "@angular/core"
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse  } from "@angular/common/http"
import { Observable } from "rxjs"
import { Router } from '@angular/router';
import {tap} from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constructor(
        private router: Router
    ) {}
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        
        const token = localStorage.getItem("AccessToken")
        if(token){
            const clonedRequest = req.clone({
                headers: req.headers.set("Authorization","Bearer " + token)
            })
            return next.handle(clonedRequest)
        }
        else{
          return next.handle(req);
            /*return next.handle(req).pipe( tap(() => {},
            (err: any) => {
            if (err instanceof HttpErrorResponse) {
              if (err.status == 403 || err.status == 401) {
                this.router.navigate(['/login']);
              }
              return;
            }
          }));*/
        }
    }


}