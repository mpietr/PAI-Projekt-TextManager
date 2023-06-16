import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-remind-password',
  templateUrl: './remind-password.component.html',
  styleUrls: ['./remind-password.component.css']
})
export class RemindPasswordComponent implements OnInit {

  public remindForm!: FormGroup;

  constructor(private authenticationService: AuthenticationService) {}

  ngOnInit() {
    this.remindForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
    });
  }

  public onSubmit() {
    this.authenticationService.remind(
      this.remindForm.get('email')!.value,
    );
  }

}
