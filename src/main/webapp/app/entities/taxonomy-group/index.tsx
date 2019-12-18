import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TaxonomyGroup from './taxonomy-group';
import TaxonomyGroupDetail from './taxonomy-group-detail';
import TaxonomyGroupUpdate from './taxonomy-group-update';
import TaxonomyGroupDeleteDialog from './taxonomy-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TaxonomyGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TaxonomyGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TaxonomyGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={TaxonomyGroup} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TaxonomyGroupDeleteDialog} />
  </>
);

export default Routes;
