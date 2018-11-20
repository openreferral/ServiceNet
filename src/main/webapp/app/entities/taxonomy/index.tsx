import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Taxonomy from './taxonomy';
import TaxonomyDetail from './taxonomy-detail';
import TaxonomyUpdate from './taxonomy-update';
import TaxonomyDeleteDialog from './taxonomy-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TaxonomyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TaxonomyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TaxonomyDetail} />
      <ErrorBoundaryRoute path={match.url} component={Taxonomy} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TaxonomyDeleteDialog} />
  </>
);

export default Routes;
