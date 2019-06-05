import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Beds from './beds';
import BedsDetail from './beds-detail';
import BedsUpdate from './beds-update';
import BedsDeleteDialog from './beds-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BedsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BedsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BedsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Beds} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={BedsDeleteDialog} />
  </>
);

export default Routes;
