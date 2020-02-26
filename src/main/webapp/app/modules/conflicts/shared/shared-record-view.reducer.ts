import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_BASE_ORGANIZATION: 'recordView/FETCH_BASE_ORGANIZATION',
  FETCH_PARTNER_ORGANIZATION: 'recordView/FETCH_PARTNER_ORGANIZATION',
  FETCH_PARTNER_ORGANIZATIONS: 'recordView/FETCH_PARTNER_ORGANIZATIONS',
  FETCH_MATCHES: 'recordView/FETCH_MATCHES'
};

const initialState = {
  errorMessage: null,
  baseRecord: null,
  partnerRecord: null,
  matches: [],
  dismissedMatches: [],
  hiddenMatches: [],
  partnerRecords: []
};

export type SharedRecordViewState = Readonly<typeof initialState>;

// Reducer
export default (state: SharedRecordViewState = initialState, action): SharedRecordViewState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BASE_ORGANIZATION):
    case REQUEST(ACTION_TYPES.FETCH_PARTNER_ORGANIZATION):
    case REQUEST(ACTION_TYPES.FETCH_PARTNER_ORGANIZATIONS):
    case REQUEST(ACTION_TYPES.FETCH_MATCHES):
      return {
        ...state
      };
    case FAILURE(ACTION_TYPES.FETCH_BASE_ORGANIZATION):
    case FAILURE(ACTION_TYPES.FETCH_PARTNER_ORGANIZATION):
    case FAILURE(ACTION_TYPES.FETCH_PARTNER_ORGANIZATIONS):
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
    case SUCCESS(ACTION_TYPES.FETCH_PARTNER_ORGANIZATIONS):
      return {
        ...state,
        partnerRecords: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MATCHES):
      const matches = [];
      const dismissedMatches = [];
      const hiddenMatches = [];

      action.payload.data.forEach(item => {
        if (item.hidden) {
          hiddenMatches.push(item);
        } else if (item.dismissed) {
          dismissedMatches.push(item);
        } else {
          matches.push(item);
        }
      });

      return {
        ...state,
        matches,
        dismissedMatches,
        hiddenMatches
      };
    default:
      return state;
  }
};

// Actions
const url = SERVICENET_API_URL + '/';
const activityUrl = url + 'activities/';
const partnerActivityUrl = url + 'partner-activities/';
const matchesUrl = url + 'organization-matches/organization/';
const hiddenMatchesUrl = url + 'organization-matches/hidden';

export const getBaseRecord = orgId => ({
  type: ACTION_TYPES.FETCH_BASE_ORGANIZATION,
  payload: axios.get(`${activityUrl + orgId}`)
});

export const getPartnerRecord = orgId => ({
  type: ACTION_TYPES.FETCH_PARTNER_ORGANIZATION,
  payload: axios.get(`${activityUrl + orgId}`)
});

export const getPartnerRecords = orgId => ({
  type: ACTION_TYPES.FETCH_PARTNER_ORGANIZATIONS,
  payload: axios.get(`${partnerActivityUrl + orgId}`)
});

export const getMatches = orgId => ({
  type: ACTION_TYPES.FETCH_MATCHES,
  payload: axios.get(`${matchesUrl + orgId}`)
});

export const getHiddenMatches = () => ({
  type: ACTION_TYPES.FETCH_MATCHES,
  payload: axios.get(hiddenMatchesUrl)
});

export const getNotHiddenMatchesByOrg = orgId => ({
  type: ACTION_TYPES.FETCH_MATCHES,
  payload: axios.get(matchesUrl + orgId + '/notHidden')
});
