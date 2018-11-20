import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Organization from './organization';
import OrganizationDetail from './organization-detail';
import OrganizationUpdate from './organization-update';
import OrganizationDeleteDialog from './organization-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrganizationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrganizationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrganizationDetail} />
      <ErrorBoundaryRoute path={match.url} component={Organization} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OrganizationDeleteDialog} />
  </>
);

export default Routes;
