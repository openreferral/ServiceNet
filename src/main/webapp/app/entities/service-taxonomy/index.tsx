import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ServiceTaxonomy from './service-taxonomy';
import ServiceTaxonomyDetail from './service-taxonomy-detail';
import ServiceTaxonomyUpdate from './service-taxonomy-update';
import ServiceTaxonomyDeleteDialog from './service-taxonomy-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ServiceTaxonomyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ServiceTaxonomyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ServiceTaxonomyDetail} />
      <ErrorBoundaryRoute path={match.url} component={ServiceTaxonomy} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ServiceTaxonomyDeleteDialog} />
  </>
);

export default Routes;
