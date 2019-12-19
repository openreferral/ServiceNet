import { ITaxonomy } from 'app/shared/model/taxonomy.model';

export interface ITaxonomyGroup {
  id?: number;
  taxonomies?: ITaxonomy[];
}

export const defaultValue: Readonly<ITaxonomyGroup> = {};
