import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../services/login.service';
import { UserLogin } from './user-login';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
})
export class LoginComponent {
    email: string = '';
    password: string = '';

    token = '';

    constructor(private router: Router, private _loginService: LoginService) {}

    handleError() {
        this.email = '';
        this.password = '';
        alert('Failed to login please try again'); //temp
    }

    login() {
        let user = new UserLogin();
        user.email = this.email;
        user.password = this.password;

        this._loginService.login(user).subscribe(
            (data) => {
                localStorage.setItem('AccessToken', data.accessToken);
                localStorage.setItem('Role', data.roles[0]);
            },
            (error) => this.handleError(),
            () => this.router.navigate([''])
        );
    }
    verifyPassword() {
        if (this.password == '') return false;
        return true;
    }

    verifyEmail() {
        if (this.email == '') return false;
        return true;
    }

    contentIsValid() {
        if (!this.verifyPassword()) return false;
        if (!this.verifyEmail()) return false;
        return true;
    }
}
