import { IUser } from 'app/shared/model/user.model';

export interface IActivity {
  id?: number;
  userLogin?: string;
  userId?: number;
  reviewers?: IUser[];
  metadataId?: number;
  organizationId?: number;
}

export const defaultValue: Readonly<IActivity> = {};
