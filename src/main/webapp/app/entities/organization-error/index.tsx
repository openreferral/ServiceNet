import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OrganizationError from './organization-error';
import OrganizationErrorDetail from './organization-error-detail';
import OrganizationErrorUpdate from './organization-error-update';
import OrganizationErrorDeleteDialog from './organization-error-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrganizationErrorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrganizationErrorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrganizationErrorDetail} />
      <ErrorBoundaryRoute path={match.url} component={OrganizationError} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OrganizationErrorDeleteDialog} />
  </>
);

export default Routes;
