import { Component, OnInit } from '@angular/core';
import { Event, RouterEvent, Router } from '@angular/router';
import { filter } from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['../shared-style.css', './navbar.component.css']
})
export class NavbarComponent implements OnInit {
  
  public role: any;

  constructor(private router: Router) {
    router.events.pipe(
      filter((e: Event): e is RouterEvent => e instanceof RouterEvent)
    ).subscribe((e: RouterEvent) => {
      this.setRole();
    });
  }
  ngOnInit(): void {
    this.setRole();
  }

  setRole(): void {
    this.role = localStorage.getItem('Role');
    console.log(this.role);

    if(this.role == "ROLE_USER"){
      this.role = "user";
    }
    else if (this.role == "ROLE_ADMINISTRATOR"){
      this.role = "admin";
    }
    else if (this.role == "ROLE_STAFF"){
      this.role = "staff";
    }
    else {
      this.role = " ";
    }
  }

  signout(){
    localStorage.clear();
  }

}
