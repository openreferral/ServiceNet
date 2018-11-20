import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PostalAddress from './postal-address';
import PostalAddressDetail from './postal-address-detail';
import PostalAddressUpdate from './postal-address-update';
import PostalAddressDeleteDialog from './postal-address-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PostalAddressUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PostalAddressUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PostalAddressDetail} />
      <ErrorBoundaryRoute path={match.url} component={PostalAddress} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PostalAddressDeleteDialog} />
  </>
);

export default Routes;
