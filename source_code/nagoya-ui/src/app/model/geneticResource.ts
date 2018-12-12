export class GeneticResource  {
  identifier: string;
  description: string;
  source: string;
  origin: string;
  hashSequence?: string;
  files?: File[];
  visibilityType: VisibilityType;
}
enum VisibilityType {
  PRIVATE,
  PUBLIC,
  GROUP
}
