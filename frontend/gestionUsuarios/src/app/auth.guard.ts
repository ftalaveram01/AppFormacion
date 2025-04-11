import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    if (token) {
      const payload = this.decodeToken(token);
      const isExpired = this.isTokenExpired(payload.exp);

      if (!isExpired) {
        return true;
      }
    }

    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }

  private decodeToken(token: string): any {
    const payloadBase64 = token.split('.')[1];
    const payloadJson = atob(payloadBase64);
    return JSON.parse(payloadJson);
  }

  private isTokenExpired(expiration: number): boolean {
    const currentTime = Math.floor(Date.now() / 1000);
    return expiration < currentTime;
  }
}
