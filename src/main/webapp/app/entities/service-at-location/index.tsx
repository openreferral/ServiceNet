import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ServiceAtLocation from './service-at-location';
import ServiceAtLocationDetail from './service-at-location-detail';
import ServiceAtLocationUpdate from './service-at-location-update';
import ServiceAtLocationDeleteDialog from './service-at-location-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ServiceAtLocationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ServiceAtLocationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ServiceAtLocationDetail} />
      <ErrorBoundaryRoute path={match.url} component={ServiceAtLocation} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ServiceAtLocationDeleteDialog} />
  </>
);

export default Routes;
