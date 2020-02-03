import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LocationFieldsValue from './location-fields-value';
import LocationFieldsValueDetail from './location-fields-value-detail';
import LocationFieldsValueUpdate from './location-fields-value-update';
import LocationFieldsValueDeleteDialog from './location-fields-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LocationFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LocationFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LocationFieldsValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={LocationFieldsValue} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={LocationFieldsValueDeleteDialog} />
  </>
);

export default Routes;
