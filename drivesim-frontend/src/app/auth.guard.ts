import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable()
export class AuthGuard implements CanActivate{

    constructor(
        private router: Router,
        private jwtHelper: JwtHelperService
    ) {}
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        const { roles } = route.data;
        const token = this.getToken()
        const role = this.getRole();
        const decodedToken = this.jwtHelper.decodeToken(token);
        if(!roles)
            return true;
        if(token && !this.jwtHelper.isTokenExpired(token) && roles.includes(role)){
            return true;
        }
        this.router.navigate(['login']);
        return false
    }

    getToken(): string {
        const token = localStorage.getItem("AccessToken")
        if(token){return token}
        else{return ""}
    }

    getRole(): string {
        const role = localStorage.getItem("Role")
        if(role){return role}
        else{return ""}
    }
}