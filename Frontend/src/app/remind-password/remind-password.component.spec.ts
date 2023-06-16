import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemindPasswordComponent } from './remind-password.component';

describe('RemindPasswordComponent', () => {
  let component: RemindPasswordComponent;
  let fixture: ComponentFixture<RemindPasswordComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RemindPasswordComponent]
    });
    fixture = TestBed.createComponent(RemindPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
