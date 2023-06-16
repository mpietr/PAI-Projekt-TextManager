import { Component, OnInit } from '@angular/core';
import { TextResource } from '../clients/textresource';
import { Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { TextClient } from '../clients/text.client';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {
  public textResource!: Observable<TextResource>;
  public editForm!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private textClient: TextClient,
    private router: Router) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      let code = params.get('code');
      if (code !== null) {
        this.textResource = this.textClient.getTextByCode(code);
        this.textResource.subscribe(resource => {
          this.editForm.patchValue({
            name: resource.name,
            tags: resource.tags,
            content: resource.text
          });
        });
      }
    });
  
    this.editForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(64)]),
      tags: new FormControl('', Validators.pattern(/^[a-zA-Z]+(?:\s+[a-zA-Z]+)*$/)),
      content: new FormControl('', Validators.required),
    });
    
  }

  public onSubmitSave() {
    if(confirm("Are you sure you want to save changes?")) {
      this.textResource.subscribe (resource => {
        this.textClient.editText(
          this.editForm.get('name')!.value,
          this.editForm.get('content')!.value,
          this.editForm.get('tags')!.value,
          resource.code
        ).subscribe((text) => {
          console.log(text.code);
          this.router.navigate(['/dashboard']);
        });
      });
    }
  }

  public onSubmitDelete() {
    if(confirm("Are you sure you want to delete resource?")) {
      this.textResource.subscribe (resource => {
        this.textClient.deleteText(resource.code).subscribe(() =>
          {this.router.navigate(['/dashboard']);}
        )
      });
    }
    
  }
}
