import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserRegister } from '../register/user-register';
import { UserProfileService } from '../services/user-profile.service';

enum Role {
    USER,
    ADMINISTRATOR,
    STAFF,
}

@Component({
    selector: 'app-user-profile',
    templateUrl: './user-profile.component.html',
    styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
    role: Role = Role.USER;

    user: UserRegister = new UserRegister();

    infoChanged: boolean = false;
    errorMessage: string = '';
    responseMessage: string = '';

    constructor(private router: Router, private _userProfileService: UserProfileService) {}

    ngOnInit(): void {
        this._userProfileService.getUserData().subscribe((data) => {
            this.user = data;
        });
    }

    changeInfo() {
        this._userProfileService.changeUserData(this.user).subscribe(
            (response) => {
                this.user = response;
                this.responseMessage = 'Info changed successfully';
                console.log(response);
            },
            (error) => {
                this.errorMessage = error.error;
            }
        );
    }

    contentIsValid() {
        return true;
    }
}
