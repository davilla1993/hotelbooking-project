import { Component } from '@angular/core';
import { ApiService } from '../service/api.service';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule, RouterOutlet, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  constructor(private router: Router, private apiService: ApiService) {}

  formData: any = {

    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    password: ''
  }

  error: any = null;

  handleSubmit(){
    if (
      !this.formData.email ||
      !this.formData.firstName ||
      !this.formData.lastName ||
      !this.formData.phoneNumber ||
      !this.formData.password
    ) {
      this.showError('Please all fields are required');
      return
    }

    this.apiService.registerUser(this.formData).subscribe({
      next:(res: any) => {
        this.router.navigate(['/login'])
      },
      error: (err: any) => {
        this.showError(err?.error?.message || err.message || 'Unable to Register a user: ' + err)
      }
    });
  }

  showError(msg: string){
    this.error = msg;
    setTimeout(()=> {
      this.error = null
    }, 4000);
  }
}
