import React from 'react';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import PrivateRoute from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

import Settings from './settings/settings';
import Password from './password/password';
import Sessions from './sessions/sessions';

const Routes = ({ match, isAdmin }) => (
  <div>
    <ErrorBoundaryRoute path={`${match.url}/settings`} component={Settings} />
    <ErrorBoundaryRoute path={`${match.url}/password`} component={Password} />
    <PrivateRoute path={`${match.url}/sessions`} isAdmin={isAdmin} component={Sessions} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
  </div>
);

export default Routes;
