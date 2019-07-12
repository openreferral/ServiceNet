import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MatchSimilarity from './match-similarity';
import MatchSimilarityDetail from './match-similarity-detail';
import MatchSimilarityUpdate from './match-similarity-update';
import MatchSimilarityDeleteDialog from './match-similarity-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MatchSimilarityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MatchSimilarityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MatchSimilarityDetail} />
      <ErrorBoundaryRoute path={match.url} component={MatchSimilarity} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={MatchSimilarityDeleteDialog} />
  </>
);

export default Routes;
