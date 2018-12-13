import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VisibilityType} from '../../model/visibilityType';
import {ResourceService} from '../../services/resource.service';
import {BehaviorSubject} from 'rxjs';


@Component({
  selector: 'app-creation',
  templateUrl: './creation.component.html',
  styleUrls: ['./creation.component.scss']
})
export class CreationComponent implements OnInit {

  @Input()
  createNew: boolean;
  @Output()
    createNewChange = new EventEmitter<boolean>();

  geneticResourceForm: FormGroup;
  visibilityTypes = Object.keys(VisibilityType);
  selectedType: VisibilityType;

  constructor(private formBuilder: FormBuilder, private resourceService: ResourceService) {
    this.createForm();
  }

  ngOnInit() {
  }

  onSubmit() {
    console.log(this.geneticResourceForm.getRawValue());
    this.resourceService.create(this.geneticResourceForm.getRawValue()).subscribe(result => {
      console.log(result);
      this.createNew = !this.createNew;
      this.createNewChange.emit(this.createNew);
    }, error => {
      console.log(error);
    });
  }

  createForm() {
    this.geneticResourceForm = this.formBuilder.group({
      identifier: ['', Validators.required],
      description: ['', Validators.required],
      source: ['', Validators.required],
      origin: ['', Validators.required],
      hashSequence: [''],
      visibilityType: ['', Validators.required]
    });
  }
}
