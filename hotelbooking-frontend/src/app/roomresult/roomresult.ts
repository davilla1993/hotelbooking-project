import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../service/api.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-roomresult',
  imports: [CommonModule],
  templateUrl: './roomresult.html',
  styleUrl: './roomresult.css'
})
export class Roomresult {

  @Input() roomSearchResults: any[] = []; // Input property for room results
  isAdmin: boolean;

  constructor(private router: Router, private apiService: ApiService) {
    // Get the current user's admin status
    this.isAdmin = this.apiService.isAdmin();
  }

  // Method to navigate to the edit room page (for admins)
  navigateToEditRoom(roomId: string) {
    this.router.navigate([`/admin/edit-room/${roomId}`]);
  }

  // Method to navigate to the room details page (for users)
  navigateToRoomDetails(roomId: string) {
    this.router.navigate([`/room-details/${roomId}`]);
  }

}
