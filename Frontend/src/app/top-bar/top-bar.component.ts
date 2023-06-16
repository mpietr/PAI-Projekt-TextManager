import { ChangeDetectorRef, Component } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})


export class TopBarComponent {

  constructor(public authenticationService: AuthenticationService) {
  }


  logout(): void {
    this.authenticationService.logout()
  }
}
