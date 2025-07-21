import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-paymentsuccess',
  imports: [],
  templateUrl: './paymentsuccess.html',
  styleUrl: './paymentsuccess.css'
})
export class Paymentsuccess implements OnInit{

  bookingReference: string = '';

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.bookingReference = this.route.snapshot.paramMap.get('bookingReference') || '';
  }

}
