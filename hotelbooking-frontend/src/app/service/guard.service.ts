import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class GuardService implements CanActivate{

  constructor(private apiService:ApiService, private router:Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    const requiresAdmin = route.data['requiresAdmin'] || false;

    if (requiresAdmin) {
      if(this.apiService.isAdmin()){
        return true; //allow acceess for admin if the user is an admin
      }else{
        this.router.navigate(['/login'], {
          queryParams: {returnUrl: state.url}
        });
        return false; //deny access to the route if the user is not an admin
      }

    }else{

      if (this.apiService.isAuthenticated()) {
        return true; //allow access
      }else{
        this.router.navigate(['/login'], {
          queryParams: {returnUrl: state.url}
        });
        return false; //deny access
      }
    }
  }

}
