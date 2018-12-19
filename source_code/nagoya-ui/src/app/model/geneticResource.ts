import {VisibilityType} from './visibilityType';
import {ResourceFile} from './resourceFile';
import {Taxonomy} from './taxonomy';

export class GeneticResource  {
  identifier: string;
  description: string;
  source: string;
  origin: string;
  hashSequence?: string;
  files?: ResourceFile[];
  visibilityType: VisibilityType;
  taxonomy?: Taxonomy;
}

