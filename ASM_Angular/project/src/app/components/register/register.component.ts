import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="auth-container">
      <h2>Register</h2>
      <form (ngSubmit)="onSubmit()" class="registration-form">
        <div class="form-row">
          <div class="form-group">
            <label for="firstName">First Name:</label>
            <input 
              type="text" 
              id="firstName" 
              [(ngModel)]="formData.firstName" 
              name="firstName" 
              required>
          </div>
          <div class="form-group">
            <label for="lastName">Last Name:</label>
            <input 
              type="text" 
              id="lastName" 
              [(ngModel)]="formData.lastName" 
              name="lastName" 
              required>
          </div>
        </div>

        <div class="form-group">
          <label for="username">Username:</label>
          <input 
            type="text" 
            id="username" 
            [(ngModel)]="formData.username" 
            name="username" 
            required>
        </div>

        <div class="form-group">
          <label for="email">Email:</label>
          <input 
            type="email" 
            id="email" 
            [(ngModel)]="formData.email" 
            name="email" 
            required>
        </div>

        <div class="form-group">
          <label for="password">Password:</label>
          <input 
            type="password" 
            id="password" 
            [(ngModel)]="formData.password" 
            name="password" 
            required>
        </div>

        <button type="submit" class="submit-btn">Register</button>
      </form>
      <p class="auth-link">Already have an account? <a routerLink="/login">Login</a></p>
    </div>
  `,
  styles: [`
    .auth-container {
      max-width: 600px;
      margin: 50px auto;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
      background: white;
    }
    h2 {
      text-align: center;
      color: #2c3e50;
      margin-bottom: 30px;
    }
    .registration-form {
      display: flex;
      flex-direction: column;
      gap: 15px;
    }
    .form-row {
      display: flex;
      gap: 15px;
    }
    .form-group {
      flex: 1;
    }
    label {
      display: block;
      margin-bottom: 5px;
      color: #34495e;
      font-weight: 500;
    }
    input {
      width: 100%;
      padding: 10px;
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
export class RegisterComponent {
  formData = {
    username: '',
    password: '',
    email: '',
    firstName: '',
    lastName: '',
  };

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit() {
    if (this.isFormValid()) {
      const formDataWithDate = { ...this.formData};
      this.authService.register(formDataWithDate).subscribe({
        next: () => {
          this.router.navigate(['/login']);
          alert('Registration successful! Please login.');
        },
        error: (error) => {
          console.error('Registration failed:', error);
          alert('Registration failed. Please try again.');
        }
      });
    }
  }

  private isFormValid(): boolean {
    return Object.values(this.formData).every(value => value.trim() !== '');
  }
}