import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FieldExclusion from './field-exclusion';
import FieldExclusionDetail from './field-exclusion-detail';
import FieldExclusionUpdate from './field-exclusion-update';
import FieldExclusionDeleteDialog from './field-exclusion-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FieldExclusionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FieldExclusionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FieldExclusionDetail} />
      <ErrorBoundaryRoute path={match.url} component={FieldExclusion} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FieldExclusionDeleteDialog} />
  </>
);

export default Routes;
