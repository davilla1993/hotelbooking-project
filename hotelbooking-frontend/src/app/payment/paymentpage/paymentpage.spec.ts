import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Paymentpage } from './paymentpage';

describe('Paymentpage', () => {
  let component: Paymentpage;
  let fixture: ComponentFixture<Paymentpage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Paymentpage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Paymentpage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
