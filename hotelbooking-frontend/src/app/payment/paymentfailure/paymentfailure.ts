import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-paymentfailure',
  imports: [],
  templateUrl: './paymentfailure.html',
  styleUrl: './paymentfailure.css'
})
export class Paymentfailure {
  
  bookingReference: string = '';

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.bookingReference = this.route.snapshot.paramMap.get('bookingReference') || '';
  }
}
