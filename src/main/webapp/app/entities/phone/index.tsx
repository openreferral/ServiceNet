import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Phone from './phone';
import PhoneDetail from './phone-detail';
import PhoneUpdate from './phone-update';
import PhoneDeleteDialog from './phone-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PhoneUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PhoneUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PhoneDetail} />
      <ErrorBoundaryRoute path={match.url} component={Phone} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PhoneDeleteDialog} />
  </>
);

export default Routes;
