import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './match-similarity.reducer';
import { IMatchSimilarity } from 'app/shared/model/match-similarity.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMatchSimilarityProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class MatchSimilarity extends React.Component<IMatchSimilarityProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { matchSimilarityList, match } = this.props;
    return (
      <div>
        <h2 id="match-similarity-heading">
          <Translate contentKey="serviceNetApp.matchSimilarity.home.title">Match Similarities</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.matchSimilarity.home.createLabel">Create new Match Similarity</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {matchSimilarityList && matchSimilarityList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.matchSimilarity.similarity">Similarity</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.matchSimilarity.resourceClass">Resource Class</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.matchSimilarity.fieldName">Field Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.matchSimilarity.organizationMatch">Organization Match</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {matchSimilarityList.map((matchSimilarity, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${matchSimilarity.id}`} color="link" size="sm">
                        {matchSimilarity.id}
                      </Button>
                    </td>
                    <td>{matchSimilarity.similarity}</td>
                    <td>{matchSimilarity.resourceClass}</td>
                    <td>{matchSimilarity.fieldName}</td>
                    <td>
                      {matchSimilarity.organizationMatchId ? (
                        <Link to={`organization-match/${matchSimilarity.organizationMatchId}`}>{matchSimilarity.organizationMatchId}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${matchSimilarity.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${matchSimilarity.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${matchSimilarity.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="serviceNetApp.matchSimilarity.home.notFound">No Match Similarities found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ matchSimilarity }: IRootState) => ({
  matchSimilarityList: matchSimilarity.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MatchSimilarity);
