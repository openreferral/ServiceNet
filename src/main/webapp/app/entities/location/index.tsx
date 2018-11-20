import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Location from './location';
import LocationDetail from './location-detail';
import LocationUpdate from './location-update';
import LocationDeleteDialog from './location-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LocationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LocationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LocationDetail} />
      <ErrorBoundaryRoute path={match.url} component={Location} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={LocationDeleteDialog} />
  </>
);

export default Routes;
