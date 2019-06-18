import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LocationExclusion from './location-exclusion';
import LocationExclusionDetail from './location-exclusion-detail';
import LocationExclusionUpdate from './location-exclusion-update';
import LocationExclusionDeleteDialog from './location-exclusion-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LocationExclusionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LocationExclusionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LocationExclusionDetail} />
      <ErrorBoundaryRoute path={match.url} component={LocationExclusion} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={LocationExclusionDeleteDialog} />
  </>
);

export default Routes;
