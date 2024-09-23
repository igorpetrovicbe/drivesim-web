import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { UserHomepageComponent } from './user-homepage/user-homepage.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { AuthGuard } from './auth.guard';
import { Role } from './role';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  { path: "register", component: RegisterComponent },
  { path: "login", component: LoginComponent},
  { path: "", component: HomeComponent, canActivate: [AuthGuard]},
  { path: "user-homepage", component: UserHomepageComponent, canActivate: [AuthGuard]},
  { path: 'user-profile', component: UserProfileComponent, canActivate: [AuthGuard], data: { roles: [Role.User] } },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
