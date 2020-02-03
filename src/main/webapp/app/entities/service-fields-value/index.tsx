import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ServiceFieldsValue from './service-fields-value';
import ServiceFieldsValueDetail from './service-fields-value-detail';
import ServiceFieldsValueUpdate from './service-fields-value-update';
import ServiceFieldsValueDeleteDialog from './service-fields-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ServiceFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ServiceFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ServiceFieldsValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={ServiceFieldsValue} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ServiceFieldsValueDeleteDialog} />
  </>
);

export default Routes;
