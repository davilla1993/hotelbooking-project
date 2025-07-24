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
import { Adminhome } from './admin/adminhome/adminhome';
import { Managerooms } from './admin/managerooms/managerooms';
import { Addroom } from './admin/addroom/addroom';
import { Editroom } from './admin/editroom/editroom';
import { Managebookings } from './admin/managebookings/managebookings';
import { Updatebooking } from './admin/updatebooking/updatebooking';

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

  //ADMIN ROUTES OR PAGES
  {path: 'admin', component: Adminhome, canActivate: [GuardService], data: {requiresAdmin: true}},
  {path: 'admin/manage-rooms', component: Managerooms, canActivate: [GuardService], data: {requiresAdmin: true}},
  {path: 'admin/add-room', component: Addroom, canActivate: [GuardService], data: {requiresAdmin: true}},
  {path: 'admin/edit-room/:id', component: Editroom, canActivate: [GuardService], data: {requiresAdmin: true}},

  {path: 'admin/manage-bookings', component: Managebookings, canActivate: [GuardService], data: {requiresAdmin: true}},
  {path: 'admin/edit-booking/:bookingCode', component: Updatebooking, canActivate: [GuardService], data: {requiresAdmin: true}},

  {path: '**', redirectTo: 'home'}

];
