import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_JOBS: 'schedulerAdministration/FETCH_JOBS',
  TRIGGER_JOB: 'schedulerAdministration/TRIGGER_JOB',
  PAUSE_JOB: 'schedulerAdministration/PAUSE_JOB'
};

const initialState = {
  errorMessage: null,
  jobs: [] as any[]
};

export type SchedulerState = Readonly<typeof initialState>;

// Reducer
export default (state: SchedulerState = initialState, action): SchedulerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_JOBS):
    case REQUEST(ACTION_TYPES.TRIGGER_JOB):
    case REQUEST(ACTION_TYPES.PAUSE_JOB):
      return {
        ...state
      };
    case FAILURE(ACTION_TYPES.FETCH_JOBS):
    case FAILURE(ACTION_TYPES.TRIGGER_JOB):
    case FAILURE(ACTION_TYPES.PAUSE_JOB):
      return {
        ...state,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_JOBS):
      return {
        ...state,
        jobs: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.TRIGGER_JOB):
    case SUCCESS(ACTION_TYPES.PAUSE_JOB):
      return {
        ...state
      };
    default:
      return state;
  }
};

// Actions
const baseUrl = SERVICENET_API_URL + '/jobs';
const pauseUrl = `${baseUrl}/pause`;

export const getJobs = () => ({
  type: ACTION_TYPES.FETCH_JOBS,
  payload: axios.get(baseUrl)
});

export const triggerJob = job => ({
  type: ACTION_TYPES.TRIGGER_JOB,
  payload: axios.post(baseUrl, job)
});

export const pauseJob = job => ({
  type: ACTION_TYPES.PAUSE_JOB,
  payload: axios.post(pauseUrl, job)
});
