import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TextClient } from '../clients/text.client';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {
  public createForm!: FormGroup;

  constructor(
    private textClient: TextClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.createForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(64)]),
      tags: new FormControl('', Validators.pattern(/^[a-zA-Z]+(?:\s+[a-zA-Z]+)*$/)),
      content: new FormControl('', Validators.required),
    });
  }

  public onSubmit() {
    this.textClient.addText(
      this.createForm.get('name')!.value,
      this.createForm.get('content')!.value,
      this.createForm.get('tags')!.value
    ).subscribe((text) => {
      this.router.navigate(['/dashboard']);
    });
  }
}
