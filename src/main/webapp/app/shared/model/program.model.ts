import { IService } from 'app/shared/model//service.model';

export interface IProgram {
  id?: number;
  name?: string;
  alternateName?: string;
  organizationName?: string;
  organizationId?: number;
  services?: IService[];
}

export const defaultValue: Readonly<IProgram> = {};
