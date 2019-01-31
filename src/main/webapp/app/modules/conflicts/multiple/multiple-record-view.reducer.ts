import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  FETCH_BASE_ORGANIZATION: 'uploadPage/FETCH_BASE_ORGANIZATION',
  FETCH_PARTNER_ORGANIZATION: 'uploadPage/FETCH_PARTNER_ORGANIZATION',
  FETCH_MATCHES: 'uploadPage/FETCH_MATCHES'
};

const initialState = {
  errorMessage: null,
  baseRecord: null,
  partnerRecord: null,
  matches: []
};

export type MultipleRecordViewState = Readonly<typeof initialState>;

// Reducer
export default (state: MultipleRecordViewState = initialState, action): MultipleRecordViewState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BASE_ORGANIZATION):
    case REQUEST(ACTION_TYPES.FETCH_PARTNER_ORGANIZATION):
    case REQUEST(ACTION_TYPES.FETCH_MATCHES):
      return {
        ...state
      };
    case FAILURE(ACTION_TYPES.FETCH_BASE_ORGANIZATION):
    case FAILURE(ACTION_TYPES.FETCH_PARTNER_ORGANIZATION):
    case FAILURE(ACTION_TYPES.FETCH_MATCHES):
      return {
        ...state,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BASE_ORGANIZATION):
      return {
        ...state,
        baseRecord: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARTNER_ORGANIZATION):
      return {
        ...state,
        partnerRecord: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MATCHES):
      return {
        ...state,
        matches: action.payload.data
      };
    default:
      return state;
  }
};

// Actions
const url = 'api/';
const activityUrl = url + 'activities/';
const matchesUrl = url + 'organization-matches/organization/';

export const getBaseRecord = orgId => ({
  type: ACTION_TYPES.FETCH_BASE_ORGANIZATION,
  payload: axios.get(`${activityUrl + orgId}`)
});

export const getPartnerRecord = orgId => ({
  type: ACTION_TYPES.FETCH_PARTNER_ORGANIZATION,
  payload: axios.get(`${activityUrl + orgId}`)
});

export const getMatches = orgId => ({
  type: ACTION_TYPES.FETCH_MATCHES,
  payload: axios.get(`${matchesUrl + orgId}`)
});
