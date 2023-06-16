import { TextClient } from './../clients/text.client';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';
import { TextResource } from '../clients/textresource';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit{
  public text: Observable<TextResource[]> = this.textClient.getAllTexts()
  public filterForm!: FormGroup;
  

  constructor(
    private textClient: TextClient,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit() {
    this.filterForm = new FormGroup({
      name: new FormControl('', Validators.nullValidator),
      tags: new FormControl('', Validators.pattern(/^[a-zA-Z]+(?:\s+[a-zA-Z]+)*$/)),
      content: new FormControl('', Validators.nullValidator),
    });
  }

  logout(): void {
    this.authenticationService.logout();
  }

  filter(): void {

      this.text = this.textClient.getFilteredTexts(
        this.filterForm.get('name')!.value,
        this.filterForm.get('content')!.value,
        this.filterForm.get('tags')!.value
      );
  }

  deleteFilters(): void {
    this.filterForm.get('name')!.setValue('');
    this.filterForm.get('content')!.setValue('');
    this.filterForm.get('tags')!.setValue('');
    this.filter();
  }

}
