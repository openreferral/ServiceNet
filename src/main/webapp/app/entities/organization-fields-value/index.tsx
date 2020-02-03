import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OrganizationFieldsValue from './organization-fields-value';
import OrganizationFieldsValueDetail from './organization-fields-value-detail';
import OrganizationFieldsValueUpdate from './organization-fields-value-update';
import OrganizationFieldsValueDeleteDialog from './organization-fields-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrganizationFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrganizationFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrganizationFieldsValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={OrganizationFieldsValue} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OrganizationFieldsValueDeleteDialog} />
  </>
);

export default Routes;
