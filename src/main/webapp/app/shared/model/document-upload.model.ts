import { Moment } from 'moment';

export interface IDocumentUpload {
  id?: number;
  dateUploaded?: Moment;
  originalDocumentId?: string;
  parsedDocumentId?: string;
  uploaderLogin?: string;
  uploaderId?: number;
}

export const defaultValue: Readonly<IDocumentUpload> = {};
