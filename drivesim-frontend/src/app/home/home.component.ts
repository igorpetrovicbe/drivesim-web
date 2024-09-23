import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Role } from '../role';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public role = "" as string |null;

  constructor(private router: Router) {}

  ngOnInit() {
    this.role = localStorage.getItem('Role');
    this.navigateToHomePage();
  }

  navigateToHomePage() {
    switch (this.role) {
      case Role.Admin:
        this.router.navigate(['/admin-homepage']);
        break;
      case Role.User:
        this.router.navigate(['/user-homepage']);
        break;
      case Role.Staff:
        this.router.navigate(['/staff-homepage']);
        break;
      default:
        this.router.navigate(['/user-homepage']); // Default home route
        break;
    }
  }
}