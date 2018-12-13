import {VisibilityType} from './visibilityType';

export class GeneticResource  {
  identifier: string;
  description: string;
  source: string;
  origin: string;
  hashSequence?: string;
  files?: File[];
  visibilityType: VisibilityType;
}

