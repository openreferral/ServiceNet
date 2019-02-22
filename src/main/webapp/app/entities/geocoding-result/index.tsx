import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GeocodingResult from './geocoding-result';
import GeocodingResultDetail from './geocoding-result-detail';
import GeocodingResultUpdate from './geocoding-result-update';
import GeocodingResultDeleteDialog from './geocoding-result-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GeocodingResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GeocodingResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GeocodingResultDetail} />
      <ErrorBoundaryRoute path={match.url} component={GeocodingResult} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GeocodingResultDeleteDialog} />
  </>
);

export default Routes;
