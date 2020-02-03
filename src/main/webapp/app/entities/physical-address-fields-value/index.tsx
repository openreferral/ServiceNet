import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PhysicalAddressFieldsValue from './physical-address-fields-value';
import PhysicalAddressFieldsValueDetail from './physical-address-fields-value-detail';
import PhysicalAddressFieldsValueUpdate from './physical-address-fields-value-update';
import PhysicalAddressFieldsValueDeleteDialog from './physical-address-fields-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PhysicalAddressFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PhysicalAddressFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PhysicalAddressFieldsValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={PhysicalAddressFieldsValue} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PhysicalAddressFieldsValueDeleteDialog} />
  </>
);

export default Routes;
