import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PostalAddressFieldsValue from './postal-address-fields-value';
import PostalAddressFieldsValueDetail from './postal-address-fields-value-detail';
import PostalAddressFieldsValueUpdate from './postal-address-fields-value-update';
import PostalAddressFieldsValueDeleteDialog from './postal-address-fields-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PostalAddressFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PostalAddressFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PostalAddressFieldsValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={PostalAddressFieldsValue} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PostalAddressFieldsValueDeleteDialog} />
  </>
);

export default Routes;
