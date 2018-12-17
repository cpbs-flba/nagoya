import {VisibilityType} from './visibilityType';
import {ResourceFile} from './resourceFile';

export class GeneticResource  {
  identifier: string;
  description: string;
  source: string;
  origin: string;
  hashSequence?: string;
  files?: ResourceFile[];
  visibilityType: VisibilityType;
}

