import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="auth-container">
      <div class="login-header">
        <h2>Welcome Back</h2>
        <p>Please login to your account</p>
      </div>
      <form (ngSubmit)="onSubmit()" class="login-form">
        <div class="form-group">
          <label for="username">Username:</label>
          <input 
            type="text" 
            id="username" 
            [(ngModel)]="username" 
            name="username" 
            required>
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input 
            type="password" 
            id="password" 
            [(ngModel)]="password" 
            name="password" 
            required>
        </div>
        <button type="submit" class="submit-btn">Login</button>
      </form>
      <p class="auth-link">Don't have an account? <a routerLink="/register">Register</a></p>
    </div>
  `,
  styles: [`
    .auth-container {
      max-width: 400px;
      margin: 50px auto;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
      background: white;
    }
    .login-header {
      text-align: center;
      margin-bottom: 30px;
    }
    .login-header h2 {
      color: #2c3e50;
      margin-bottom: 10px;
    }
    .login-header p {
      color: #7f8c8d;
      margin: 0;
    }
    .login-form {
      display: flex;
      flex-direction: column;
      gap: 20px;
    }
    .form-group {
      display: flex;
      flex-direction: column;
      gap: 5px;
    }
    label {
      color: #34495e;
      font-weight: 500;
    }
    input {
      padding: 12px;
      border: 1px solid #ddd;
      border-radius: 5px;
      font-size: 14px;
      transition: border-color 0.3s;
    }
    input:focus {
      border-color: #3498db;
      outline: none;
    }
    .submit-btn {
      background-color: #3498db;
      color: white;
      padding: 12px;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      font-size: 16px;
      transition: background-color 0.3s;
    }
    .submit-btn:hover {
      background-color: #2980b9;
    }
    .auth-link {
      text-align: center;
      margin-top: 20px;
      color: #7f8c8d;
    }
    .auth-link a {
      color: #3498db;
      text-decoration: none;
    }
    .auth-link a:hover {
      text-decoration: underline;
    }
  `]
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit() {
    if (this.username && this.password) {
      this.authService.login({
        username: this.username,
        password: this.password
      }).subscribe({
        next: () => {
          this.router.navigate(['/home']);
        },
        error: (error) => {
          console.error('Login failed:', error);
          alert('Login failed. Please try again.');
        }
      });
    }
  }
}