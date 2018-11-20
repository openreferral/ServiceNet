import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { getEntity, updateEntity, createEntity, reset } from './accessibility-for-disabilities.reducer';
import { IAccessibilityForDisabilities } from 'app/shared/model/accessibility-for-disabilities.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAccessibilityForDisabilitiesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAccessibilityForDisabilitiesUpdateState {
  isNew: boolean;
  locationId: string;
}

export class AccessibilityForDisabilitiesUpdate extends React.Component<
  IAccessibilityForDisabilitiesUpdateProps,
  IAccessibilityForDisabilitiesUpdateState
> {
  constructor(props) {
    super(props);
    this.state = {
      locationId: '0',
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

    this.props.getLocations();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { accessibilityForDisabilitiesEntity } = this.props;
      const entity = {
        ...accessibilityForDisabilitiesEntity,
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
    this.props.history.push('/entity/accessibility-for-disabilities');
  };

  render() {
    const { accessibilityForDisabilitiesEntity, locations, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.accessibilityForDisabilities.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.accessibilityForDisabilities.home.createOrEditLabel">
                Create or edit a AccessibilityForDisabilities
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : accessibilityForDisabilitiesEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="accessibility-for-disabilities-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="accessibilityLabel" for="accessibility">
                    <Translate contentKey="serviceNetApp.accessibilityForDisabilities.accessibility">Accessibility</Translate>
                  </Label>
                  <AvField
                    id="accessibility-for-disabilities-accessibility"
                    type="text"
                    name="accessibility"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="detailsLabel" for="details">
                    <Translate contentKey="serviceNetApp.accessibilityForDisabilities.details">Details</Translate>
                  </Label>
                  <AvField id="accessibility-for-disabilities-details" type="text" name="details" />
                </AvGroup>
                <AvGroup>
                  <Label for="location.name">
                    <Translate contentKey="serviceNetApp.accessibilityForDisabilities.location">Location</Translate>
                  </Label>
                  <AvInput id="accessibility-for-disabilities-location" type="select" className="form-control" name="locationId">
                    <option value="" key="0" />
                    {locations
                      ? locations.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/accessibility-for-disabilities" replace color="info">
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
  locations: storeState.location.entities,
  accessibilityForDisabilitiesEntity: storeState.accessibilityForDisabilities.entity,
  loading: storeState.accessibilityForDisabilities.loading,
  updating: storeState.accessibilityForDisabilities.updating,
  updateSuccess: storeState.accessibilityForDisabilities.updateSuccess
});

const mapDispatchToProps = {
  getLocations,
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
)(AccessibilityForDisabilitiesUpdate);
