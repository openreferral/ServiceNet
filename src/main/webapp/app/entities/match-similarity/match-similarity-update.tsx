import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IOrganizationMatch } from 'app/shared/model/organization-match.model';
import { getEntities as getOrganizationMatches } from 'app/entities/organization-match/organization-match.reducer';
import { getEntity, updateEntity, createEntity, reset } from './match-similarity.reducer';
import { IMatchSimilarity } from 'app/shared/model/match-similarity.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMatchSimilarityUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMatchSimilarityUpdateState {
  isNew: boolean;
  organizationMatchId: string;
}

export class MatchSimilarityUpdate extends React.Component<IMatchSimilarityUpdateProps, IMatchSimilarityUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      organizationMatchId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getOrganizationMatches();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { matchSimilarityEntity } = this.props;
      const entity = {
        ...matchSimilarityEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/match-similarity');
  };

  render() {
    const { matchSimilarityEntity, organizationMatches, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.matchSimilarity.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.matchSimilarity.home.createOrEditLabel">Create or edit a MatchSimilarity</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : matchSimilarityEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="match-similarity-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="match-similarity-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="similarityLabel" for="match-similarity-similarity">
                    <Translate contentKey="serviceNetApp.matchSimilarity.similarity">Similarity</Translate>
                  </Label>
                  <AvField id="match-similarity-similarity" type="string" className="form-control" name="similarity" />
                </AvGroup>
                <AvGroup>
                  <Label id="resourceClassLabel" for="match-similarity-resourceClass">
                    <Translate contentKey="serviceNetApp.matchSimilarity.resourceClass">Resource Class</Translate>
                  </Label>
                  <AvField id="match-similarity-resourceClass" type="text" name="resourceClass" />
                </AvGroup>
                <AvGroup>
                  <Label id="fieldNameLabel" for="match-similarity-fieldName">
                    <Translate contentKey="serviceNetApp.matchSimilarity.fieldName">Field Name</Translate>
                  </Label>
                  <AvField id="match-similarity-fieldName" type="text" name="fieldName" />
                </AvGroup>
                <AvGroup>
                  <Label for="match-similarity-organizationMatch">
                    <Translate contentKey="serviceNetApp.matchSimilarity.organizationMatch">Organization Match</Translate>
                  </Label>
                  <AvInput id="match-similarity-organizationMatch" type="select" className="form-control" name="organizationMatchId">
                    <option value="" key="0" />
                    {organizationMatches
                      ? organizationMatches.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/match-similarity" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  organizationMatches: storeState.organizationMatch.entities,
  matchSimilarityEntity: storeState.matchSimilarity.entity,
  loading: storeState.matchSimilarity.loading,
  updating: storeState.matchSimilarity.updating,
  updateSuccess: storeState.matchSimilarity.updateSuccess
});

const mapDispatchToProps = {
  getOrganizationMatches,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MatchSimilarityUpdate);
