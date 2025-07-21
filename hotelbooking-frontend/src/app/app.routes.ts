import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { Profile } from './profile/profile';
import { GuardService } from './service/guard.service';
import { Home } from './home/home';
import { Editprofile } from './editprofile/editprofile';
import { Rooms } from './rooms/rooms';
import { Roomdetails } from './roomdetails/roomdetails';
import { Findbooking } from './findbooking/findbooking';
import { Paymentpage } from './payment/paymentpage/paymentpage';
import { Paymentsuccess } from './payment/paymentsuccess/paymentsuccess';
import { Paymentfailure } from './payment/paymentfailure/paymentfailure';

export const routes: Routes = [

  {path: 'home', component: Home},
  {path: 'login', component: Login},
  {path: 'register', component: Register},

  {path: 'rooms', component: Rooms},
  {path: 'room-details/:id', component: Roomdetails, canActivate: [GuardService]},
  {path: 'find-booking', component: Findbooking},

  {path: 'profile', component: Profile, canActivate: [GuardService]},
  {path: 'edit-profile', component: Editprofile, canActivate: [GuardService]},

  //PAYMENTS ROUTES
  {path: 'payment/:bookingReference/:amount', component: Paymentpage, canActivate: [GuardService]},
  {path: 'payment-success/:bookingReference', component: Paymentsuccess, canActivate: [GuardService]},
  {path: 'payment-failure/:bookingReference', component: Paymentfailure, canActivate: [GuardService]},


  {path: '**', redirectTo: 'home'}

];
