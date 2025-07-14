import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { Profile } from './profile/profile';
import { GuardService } from './service/guard.service';
import { Home } from './home/home';
import { Editprofile } from './editprofile/editprofile';

export const routes: Routes = [

  {path: 'home', component: Home},
  {path: 'login', component: Login},
  {path: 'register', component: Register},

  {path: 'profile', component: Profile, canActivate: [GuardService]},
  {path: 'edit-profile', component: Editprofile, canActivate: [GuardService]}

];
