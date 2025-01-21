import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginRequest, RegisterRequest, AuthResponse, Profile } from '../models/auth.model';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080';
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private jwtHelper = new JwtHelperService();
  private profileSubject = new BehaviorSubject<Profile | null>(null);

  constructor(private http: HttpClient, private router: Router) {
    this.loadProfileFromToken(); // Tự động lấy profile khi app khởi động
  }

  //  Lấy thông tin user từ JWT
  private loadProfileFromToken() {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken: any = this.jwtHelper.decodeToken(token);
      if (decodedToken) {
        const profile: Profile = {
          firstName: decodedToken.given_name,
          lastName: decodedToken.family_name,
          username: decodedToken.preferred_username,
          email: decodedToken.email,
        };
        console.log(profile);
        this.profileSubject.next(profile);
        this.isAuthenticatedSubject.next(true);
      }
    }
  }

  // hàm lòa
  private loadProfileFromAPI() {
    this.http.get<{ data: Profile }>(`${this.baseUrl}/my-profile`).subscribe({
      next: (response) => {
        this.profileSubject.next(response.data);
        this.isAuthenticatedSubject.next(true);
      },
      error: (err) => {
        console.error('Lỗi khi lấy thông tin người dùng:', err);
        this.isAuthenticatedSubject.next(false);
        this.profileSubject.next(null);
      },
    });
  }

  // 📌 Trả về Observable profile để các component có thể subscribe
  getProfile(): Observable<Profile | null> {
    return this.profileSubject.asObservable();
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, request)
      .pipe(
        tap(response => {
          localStorage.setItem('token', response.data.access_token);
          document.cookie = `token=${response.data.access_token}; path=/`;
          this.loadProfileFromToken(); // Cập nhật profile sau khi đăng nhập
          this.isAuthenticatedSubject.next(true);
        })
      );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, request);
  }

  logout() {
    localStorage.removeItem('token');
    document.cookie = 'token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    this.isAuthenticatedSubject.next(false);
    this.profileSubject.next(null);
    this.router.navigate(['/login']);
  }
}
