import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IExclusionsConfig } from 'app/shared/model/exclusions-config.model';
import { getEntities as getExclusionsConfigs } from 'app/entities/exclusions-config/exclusions-config.reducer';
import { getEntity, updateEntity, createEntity, reset } from './location-exclusion.reducer';
import { ILocationExclusion } from 'app/shared/model/location-exclusion.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILocationExclusionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ILocationExclusionUpdateState {
  isNew: boolean;
  configId: string;
}

export class LocationExclusionUpdate extends React.Component<ILocationExclusionUpdateProps, ILocationExclusionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      configId: '0',
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

    this.props.getExclusionsConfigs();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { locationExclusionEntity } = this.props;
      const entity = {
        ...locationExclusionEntity,
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
    this.props.history.push('/entity/location-exclusion');
  };

  render() {
    const { locationExclusionEntity, exclusionsConfigs, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.locationExclusion.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.locationExclusion.home.createOrEditLabel">Create or edit a LocationExclusion</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : locationExclusionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="location-exclusion-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="location-exclusion-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="regionLabel" for="location-exclusion-region">
                    <Translate contentKey="serviceNetApp.locationExclusion.region">Region</Translate>
                  </Label>
                  <AvField id="location-exclusion-region" type="text" name="region" />
                </AvGroup>
                <AvGroup>
                  <Label id="cityLabel" for="location-exclusion-city">
                    <Translate contentKey="serviceNetApp.locationExclusion.city">City</Translate>
                  </Label>
                  <AvField id="location-exclusion-city" type="text" name="city" />
                </AvGroup>
                <AvGroup>
                  <Label for="location-exclusion-config">
                    <Translate contentKey="serviceNetApp.locationExclusion.config">Config</Translate>
                  </Label>
                  <AvInput id="location-exclusion-config" type="select" className="form-control" name="configId">
                    <option value="" key="0" />
                    {exclusionsConfigs
                      ? exclusionsConfigs.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/location-exclusion" replace color="info">
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
  exclusionsConfigs: storeState.exclusionsConfig.entities,
  locationExclusionEntity: storeState.locationExclusion.entity,
  loading: storeState.locationExclusion.loading,
  updating: storeState.locationExclusion.updating,
  updateSuccess: storeState.locationExclusion.updateSuccess
});

const mapDispatchToProps = {
  getExclusionsConfigs,
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
)(LocationExclusionUpdate);
