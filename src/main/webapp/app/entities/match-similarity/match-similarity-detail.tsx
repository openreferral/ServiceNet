import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './match-similarity.reducer';
import { IMatchSimilarity } from 'app/shared/model/match-similarity.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMatchSimilarityDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MatchSimilarityDetail extends React.Component<IMatchSimilarityDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { matchSimilarityEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.matchSimilarity.detail.title">MatchSimilarity</Translate> [
            <b>{matchSimilarityEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="similarity">
                <Translate contentKey="serviceNetApp.matchSimilarity.similarity">Similarity</Translate>
              </span>
            </dt>
            <dd>{matchSimilarityEntity.similarity}</dd>
            <dt>
              <span id="resourceClass">
                <Translate contentKey="serviceNetApp.matchSimilarity.resourceClass">Resource Class</Translate>
              </span>
            </dt>
            <dd>{matchSimilarityEntity.resourceClass}</dd>
            <dt>
              <span id="fieldName">
                <Translate contentKey="serviceNetApp.matchSimilarity.fieldName">Field Name</Translate>
              </span>
            </dt>
            <dd>{matchSimilarityEntity.fieldName}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.matchSimilarity.organizationMatch">Organization Match</Translate>
            </dt>
            <dd>{matchSimilarityEntity.organizationMatchId ? matchSimilarityEntity.organizationMatchId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/match-similarity" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/match-similarity/${matchSimilarityEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ matchSimilarity }: IRootState) => ({
  matchSimilarityEntity: matchSimilarity.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MatchSimilarityDetail);
