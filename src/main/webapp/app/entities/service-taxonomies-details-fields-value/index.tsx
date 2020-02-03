import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ServiceTaxonomiesDetailsFieldsValue from './service-taxonomies-details-fields-value';
import ServiceTaxonomiesDetailsFieldsValueDetail from './service-taxonomies-details-fields-value-detail';
import ServiceTaxonomiesDetailsFieldsValueUpdate from './service-taxonomies-details-fields-value-update';
import ServiceTaxonomiesDetailsFieldsValueDeleteDialog from './service-taxonomies-details-fields-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ServiceTaxonomiesDetailsFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ServiceTaxonomiesDetailsFieldsValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ServiceTaxonomiesDetailsFieldsValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={ServiceTaxonomiesDetailsFieldsValue} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ServiceTaxonomiesDetailsFieldsValueDeleteDialog} />
  </>
);

export default Routes;
