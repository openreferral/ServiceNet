import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PhysicalAddress from './physical-address';
import PhysicalAddressDetail from './physical-address-detail';
import PhysicalAddressUpdate from './physical-address-update';
import PhysicalAddressDeleteDialog from './physical-address-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PhysicalAddressUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PhysicalAddressUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PhysicalAddressDetail} />
      <ErrorBoundaryRoute path={match.url} component={PhysicalAddress} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PhysicalAddressDeleteDialog} />
  </>
);

export default Routes;
