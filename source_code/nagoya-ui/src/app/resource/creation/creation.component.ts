import {Component, OnInit} from '@angular/core';
import {GeneticResource} from '../../model/geneticResource';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';


@Component({
  selector: 'app-creation',
  templateUrl: './creation.component.html',
  styleUrls: ['./creation.component.scss']
})
export class CreationComponent implements OnInit {

  geneticResourceForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.createForm();
  }

  ngOnInit() {
  }

  onSubmit() {
    console.log(this.geneticResourceForm.getRawValue());
  }

  createForm() {
    this.geneticResourceForm = this.formBuilder.group({
      denotation: ['', Validators.required],
      description: ['', Validators.required],
      source: ['', Validators.required],
      origin: ['', Validators.required],
      geneSequence: [''],
    });
  }
}
