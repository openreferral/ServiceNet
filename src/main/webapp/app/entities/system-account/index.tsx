import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SystemAccount from './system-account';
import SystemAccountDetail from './system-account-detail';
import SystemAccountUpdate from './system-account-update';
import SystemAccountDeleteDialog from './system-account-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SystemAccountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SystemAccountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SystemAccountDetail} />
      <ErrorBoundaryRoute path={match.url} component={SystemAccount} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SystemAccountDeleteDialog} />
  </>
);

export default Routes;
