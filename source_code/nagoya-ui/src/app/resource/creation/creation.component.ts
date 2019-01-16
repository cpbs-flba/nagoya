import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VisibilityType} from '../../model/visibilityType';
import {ResourceService} from '../../services/resource.service';
import {BehaviorSubject, Observable} from 'rxjs';
import {GeneticResource} from '../../model/geneticResource';
import {ResourceFile} from '../../model/resourceFile';
import {TranslateService} from '@ngx-translate/core';
import {Taxonomy} from '../../model/taxonomy';
import {Router} from '@angular/router';


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
  @Output()
  resourceSubmitted = new EventEmitter<boolean>();
  geneticResourceForm: FormGroup;
  visibilityTypes = Object.keys(VisibilityType);
  attachments: ResourceFile[] = [];
  taxonomyLevels: [Taxonomy[]] = [[]];
  selectedTaxonomy: Taxonomy;
  @ViewChild('file') fileInput;


  constructor(private formBuilder: FormBuilder,
              private resourceService: ResourceService,
              public translate: TranslateService, private router: Router) {
    this.createForm();
  }

  ngOnInit() {
    this.getTaxonomy();
  }


  getTaxonomy(taxonomy?: Taxonomy) {
    this.selectedTaxonomy = taxonomy;
    this.resourceService.getTaxonomy(taxonomy).subscribe(response => {

      //parent got newly selected
      if (!taxonomy) {

        //reset and start again
        this.taxonomyLevels = [[]];
        this.taxonomyLevels[0] = response;
      } else {

        //get depth of taxonomy
        const taxonomyLevel = this.getTaxonomyLevel(taxonomy);

        //if tree changes, delete everything after change
        if (taxonomyLevel < this.taxonomyLevels.length) {
          this.taxonomyLevels[taxonomyLevel] = response;
          this.taxonomyLevels.splice(taxonomyLevel);

          // else push add end of list
        } else {
          this.taxonomyLevels.push(response);
        }
      }
    });


  }

  getTaxonomyLevel(taxonomy: Taxonomy): number {
    return taxonomy ? 1 + this.getTaxonomyLevel(taxonomy.parent) : 0;
  }


  onSubmit() {
    this.resourceService.create(this.extractResource()).subscribe(result => {
      this.createNew = !this.createNew;
      this.createNewChange.emit(this.createNew);
      this.resourceSubmitted.emit(true);
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

  addAttachment(event: any) {
    const reader = new FileReader();
    const resourceFile = new ResourceFile();
    if (event.target.files && event.target.files.length) {
      const [file] = event.target.files;
      resourceFile.name = file.name;
      resourceFile.type = file.type;
      reader.readAsDataURL(file);

      reader.onload = () => {
        resourceFile.content = reader.result.toString().split(',')[1];
        this.attachments.push(resourceFile);
      };
    }
  }

  removeAttachment(resourceFile) {
    const index = this.attachments.indexOf(resourceFile, 0);
    if (index > -1) {
      this.attachments.splice(index, 1);
    }
  }

  extractResource(): GeneticResource {
    const resource = new GeneticResource();
    resource.identifier = this.geneticResourceForm.controls.identifier.value;
    resource.description = this.geneticResourceForm.controls.description.value;
    resource.origin = this.geneticResourceForm.controls.origin.value;
    resource.source = this.geneticResourceForm.controls.source.value;
    resource.hashSequence = this.geneticResourceForm.controls.hashSequence.value;
    resource.visibilityType = this.geneticResourceForm.controls.visibilityType.value;
    resource.files = this.attachments;
    resource.taxonomy = this.selectedTaxonomy;
    return resource;
  }
}
