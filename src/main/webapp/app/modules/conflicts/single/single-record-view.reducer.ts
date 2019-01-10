import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  FETCH_ACTIVITY_DETAILS: 'uploadPage/FETCH_ACTIVITY_DETAILS'
};

const initialState = {
  errorMessage: null,
  activityDetails: null
};

export type SingleRecordViewState = Readonly<typeof initialState>;

// Reducer
export default (state: SingleRecordViewState = initialState, action): SingleRecordViewState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ACTIVITY_DETAILS):
      return {
        ...state
      };
    case FAILURE(ACTION_TYPES.FETCH_ACTIVITY_DETAILS):
      return {
        ...state,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVITY_DETAILS):
      return {
        ...state,
        activityDetails: action.payload.data
      };
    default:
      return state;
  }
};

// Actions
const apiUrl = 'api/';

export const getActivityDetails = orgId => ({
  type: ACTION_TYPES.FETCH_ACTIVITY_DETAILS,
  payload: axios.get(`${apiUrl}/activities/${orgId}`)
});
