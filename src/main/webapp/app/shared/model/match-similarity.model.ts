export interface IMatchSimilarity {
  id?: number;
  similarity?: number;
  resourceClass?: string;
  fieldName?: string;
  organizationMatchId?: number;
}

export const defaultValue: Readonly<IMatchSimilarity> = {};
