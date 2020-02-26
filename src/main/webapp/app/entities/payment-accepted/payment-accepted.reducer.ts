import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPaymentAccepted, defaultValue } from 'app/shared/model/payment-accepted.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_PAYMENTACCEPTED_LIST: 'paymentAccepted/FETCH_PAYMENTACCEPTED_LIST',
  FETCH_PAYMENTACCEPTED: 'paymentAccepted/FETCH_PAYMENTACCEPTED',
  CREATE_PAYMENTACCEPTED: 'paymentAccepted/CREATE_PAYMENTACCEPTED',
  UPDATE_PAYMENTACCEPTED: 'paymentAccepted/UPDATE_PAYMENTACCEPTED',
  DELETE_PAYMENTACCEPTED: 'paymentAccepted/DELETE_PAYMENTACCEPTED',
  RESET: 'paymentAccepted/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPaymentAccepted>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PaymentAcceptedState = Readonly<typeof initialState>;

// Reducer

export default (state: PaymentAcceptedState = initialState, action): PaymentAcceptedState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTACCEPTED_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTACCEPTED):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PAYMENTACCEPTED):
    case REQUEST(ACTION_TYPES.UPDATE_PAYMENTACCEPTED):
    case REQUEST(ACTION_TYPES.DELETE_PAYMENTACCEPTED):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTACCEPTED_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTACCEPTED):
    case FAILURE(ACTION_TYPES.CREATE_PAYMENTACCEPTED):
    case FAILURE(ACTION_TYPES.UPDATE_PAYMENTACCEPTED):
    case FAILURE(ACTION_TYPES.DELETE_PAYMENTACCEPTED):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTACCEPTED_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTACCEPTED):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PAYMENTACCEPTED):
    case SUCCESS(ACTION_TYPES.UPDATE_PAYMENTACCEPTED):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PAYMENTACCEPTED):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = SERVICENET_API_URL + '/payment-accepteds';

// Actions

export const getEntities: ICrudGetAllAction<IPaymentAccepted> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PAYMENTACCEPTED_LIST,
  payload: axios.get<IPaymentAccepted>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPaymentAccepted> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENTACCEPTED,
    payload: axios.get<IPaymentAccepted>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPaymentAccepted> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PAYMENTACCEPTED,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPaymentAccepted> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAYMENTACCEPTED,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPaymentAccepted> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PAYMENTACCEPTED,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
