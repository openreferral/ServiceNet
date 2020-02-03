import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ContactDetailsFieldsValue from './contact-details-fields-value';
import ContactDetailsFieldsValueDetail from './contact-details-fields-value-detail';
import ContactDetailsFieldsValueUpdate from './contact-details-fields-value-update';
import ContactDetailsFieldsValueDeleteDialog from './contact-details-fields-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ContactDetailsFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ContactDetailsFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ContactDetailsFieldsValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={ContactDetailsFieldsValue} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ContactDetailsFieldsValueDeleteDialog} />
  </>
);

export default Routes;
