import { Moment } from 'moment';

export interface IDocumentUpload {
  id?: number;
  dateUploaded?: Moment;
  documentId?: string;
  uploaderLogin?: string;
  uploaderId?: number;
}

export const defaultValue: Readonly<IDocumentUpload> = {};
