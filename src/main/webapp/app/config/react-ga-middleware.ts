import ReactGA from 'react-ga';

import { REQUEST } from './../shared/reducers/action-type.util';
import { ACTION_TYPES as AUTH_ACTIONS } from './../shared/reducers/authentication';

const ACTIONS_CATEGORY = 'UserActions';

const TRACKED_ACTIONS = {
  [REQUEST(AUTH_ACTIONS.LOGIN)]: 'Log In',
  [REQUEST(AUTH_ACTIONS.LOGOUT)]: 'Log Out'
};

export default () => next => action => {
  const trackedAction = TRACKED_ACTIONS[action.type];

  if (trackedAction) {
    ReactGA.event({ category: ACTIONS_CATEGORY, action: trackedAction });
  }

  return next(action);
};
