import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserRegister } from './user-register';
import { RegisterService } from '../services/register.service';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
    email: string = 'horar70418@djpich.com';
    password: string = 'hori123';
    confirmPassword: string = 'hori123';
    firstName: string = 'Hori';
    lastName: string = 'Horic';

    registrationCompleted: boolean = false;
    responseMessage: string = '';

    constructor(private router: Router, private _registerService: RegisterService) {}

    ngOnInit(): void {}

    register() {
        //TODO: validacija unosa

        let user = new UserRegister();
        user.email = this.email;
        user.password = this.password;
        user.firstName = this.firstName;
        user.lastName = this.lastName;

        this._registerService.register(user).subscribe(
            (response) => {
                this.registrationCompleted = true;
                console.log(response);
            },
            (error) => {
                this.responseMessage = error.error;
            }
            /*
      success => setTimeout(() => {
        
        //this.router.navigate(['']);
      }, 800)*/
        );
    }

    contentIsValid() {
        return true;
    }
}
