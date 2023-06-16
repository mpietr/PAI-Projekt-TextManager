import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.css']
})
export class NewPasswordComponent {

  public passwordForm!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private authenticationService: AuthenticationService) {
      this.passwordForm = new FormGroup({
        password: new FormControl('', [Validators.required, Validators.minLength(7)])
      });
    }

  public onSubmit() {
    this.route.paramMap
    .subscribe(params => {
      let code = params.get('code')
      if (code !== null) {
        console.log(code);
        let password = this.passwordForm.get('password')!.value
        console.log(password);
        this.authenticationService.reset(code, password);
      };
    })
  }

  
}
