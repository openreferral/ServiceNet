import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_SYSTEM_ACCOUNTS: 'uploadPage/FETCH_SYSTEM_ACCOUNTS'
};

const initialState = {
  errorMessage: null,
  systemAccounts: [] as any[]
};

export type UploadPageState = Readonly<typeof initialState>;

// Reducer
export default (state: UploadPageState = initialState, action): UploadPageState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SYSTEM_ACCOUNTS):
      return {
        ...state
      };
    case FAILURE(ACTION_TYPES.FETCH_SYSTEM_ACCOUNTS):
      return {
        ...state,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SYSTEM_ACCOUNTS):
      return {
        ...state,
        systemAccounts: action.payload.data
      };
    default:
      return state;
  }
};

// Actions
const apiUrl = SERVICENET_API_URL + '/';

export const getSystemAccounts = () => ({
  type: ACTION_TYPES.FETCH_SYSTEM_ACCOUNTS,
  payload: axios.get(`${apiUrl}system-accounts`)
});
