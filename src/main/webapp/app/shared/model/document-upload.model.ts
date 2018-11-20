import { Moment } from 'moment';

export interface IDocumentUpload {
  id?: number;
  dateUploaded?: Moment;
  documentId?: string;
}

export const defaultValue: Readonly<IDocumentUpload> = {};
