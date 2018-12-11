export class GeneticResource  {
  denotation: string;
  description: string;
  source: string;
  origin: string;
  geneSequence?: string;
  // images?: File[];
  // documents?: File[];
  // access: Access;
}
enum Access {
  Private,
  Public,
  Group
}
