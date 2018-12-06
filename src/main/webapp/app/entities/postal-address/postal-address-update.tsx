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
import { getEntity, updateEntity, createEntity, reset } from './postal-address.reducer';
import { IPostalAddress } from 'app/shared/model/postal-address.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPostalAddressUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPostalAddressUpdateState {
  isNew: boolean;
  locationId: string;
}

export class PostalAddressUpdate extends React.Component<IPostalAddressUpdateProps, IPostalAddressUpdateState> {
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
      const { postalAddressEntity } = this.props;
      const entity = {
        ...postalAddressEntity,
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
    this.props.history.push('/entity/postal-address');
  };

  render() {
    const { postalAddressEntity, locations, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.postalAddress.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.postalAddress.home.createOrEditLabel">Create or edit a PostalAddress</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : postalAddressEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="postal-address-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="attentionLabel" for="attention">
                    <Translate contentKey="serviceNetApp.postalAddress.attention">Attention</Translate>
                  </Label>
                  <AvField id="postal-address-attention" type="text" name="attention" />
                </AvGroup>
                <AvGroup>
                  <Label id="address1Label" for="address1">
                    <Translate contentKey="serviceNetApp.postalAddress.address1">Address 1</Translate>
                  </Label>
                  <AvField
                    id="postal-address-address1"
                    type="text"
                    name="address1"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cityLabel" for="city">
                    <Translate contentKey="serviceNetApp.postalAddress.city">City</Translate>
                  </Label>
                  <AvField
                    id="postal-address-city"
                    type="text"
                    name="city"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="regionLabel" for="region">
                    <Translate contentKey="serviceNetApp.postalAddress.region">Region</Translate>
                  </Label>
                  <AvField id="postal-address-region" type="text" name="region" />
                </AvGroup>
                <AvGroup>
                  <Label id="stateProvinceLabel" for="stateProvince">
                    <Translate contentKey="serviceNetApp.postalAddress.stateProvince">State Province</Translate>
                  </Label>
                  <AvField
                    id="postal-address-stateProvince"
                    type="text"
                    name="stateProvince"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="postalCodeLabel" for="postalCode">
                    <Translate contentKey="serviceNetApp.postalAddress.postalCode">Postal Code</Translate>
                  </Label>
                  <AvField id="postal-address-postalCode" type="text" name="postalCode" />
                </AvGroup>
                <AvGroup>
                  <Label id="countryLabel" for="country">
                    <Translate contentKey="serviceNetApp.postalAddress.country">Country</Translate>
                  </Label>
                  <AvField id="postal-address-country" type="text" name="country" />
                </AvGroup>
                <AvGroup>
                  <Label for="location.name">
                    <Translate contentKey="serviceNetApp.postalAddress.location">Location</Translate>
                  </Label>
                  <AvInput id="postal-address-location" type="select" className="form-control" name="locationId">
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
                <Button tag={Link} id="cancel-save" to="/entity/postal-address" replace color="info">
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
  postalAddressEntity: storeState.postalAddress.entity,
  loading: storeState.postalAddress.loading,
  updating: storeState.postalAddress.updating,
  updateSuccess: storeState.postalAddress.updateSuccess
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
)(PostalAddressUpdate);
