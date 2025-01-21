import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { Profile } from '../../models/auth.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  template: `
  <div class="home-container">
    <header class="main-header">
      <div class="header-content">
        <h1>Welcome, {{profile?.firstName}} {{profile?.lastName}}</h1>
        <button (click)="logout()" class="logout-btn">Logout</button>
      </div>
    </header>
    
    <main class="main-content">
      <div class="profile-card">
        <h2>Profile Information</h2>
        <div class="profile-info">
          <div class="info-group">
            <label>Username:</label>
            <span>{{profile?.username}}</span>
          </div>
          <div class="info-group">
            <label>Email:</label>
            <span>{{profile?.email}}</span>
          </div>
        </div>
      </div>
    </main>
  </div>
`,
  styles: [`
    .home-container {
      min-height: 100vh;
      background-color: #f5f6fa;
    }
    .main-header {
      background-color: #2c3e50;
      padding: 20px;
      color: white;
      box-shadow: 0 2px 5px rgba(0,0,0,0.1);
    }
    .header-content {
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .header-content h1 {
      margin: 0;
      font-size: 24px;
    }
    .logout-btn {
      padding: 8px 16px;
      background-color: #e74c3c;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      transition: background-color 0.3s;
    }
    .logout-btn:hover {
      background-color: #c0392b;
    }
    .main-content {
      max-width: 1200px;
      margin: 30px auto;
      padding: 0 20px;
    }
    .profile-card {
      background: white;
      border-radius: 10px;
      padding: 30px;
      box-shadow: 0 0 20px rgba(0,0,0,0.1);
    }
    .profile-card h2 {
      color: #2c3e50;
      margin-top: 0;
      margin-bottom: 20px;
      padding-bottom: 10px;
      border-bottom: 2px solid #f1f2f6;
    }
    .profile-info {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
    }
    .info-group {
      display: flex;
      flex-direction: column;
      gap: 5px;
    }
    .info-group label {
      color: #7f8c8d;
      font-size: 14px;
    }
    .info-group span {
      color: #2c3e50;
      font-size: 16px;
      font-weight: 500;
    }
  `]
})
export class HomeComponent implements OnInit {
  profile?: Profile | null;

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.getProfile().subscribe({
      next: (profile) => {
        this.profile = profile;
      },
      error: (err) => {
        console.error('Lỗi khi lấy thông tin profile', err);
      }
    });
  }

  logout() {
    this.authService.logout();
  }
}
