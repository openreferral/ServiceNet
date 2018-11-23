export interface IPaymentAccepted {
  id?: number;
  payment?: string;
  srvcName?: string;
  srvcId?: number;
}

export const defaultValue: Readonly<IPaymentAccepted> = {};
