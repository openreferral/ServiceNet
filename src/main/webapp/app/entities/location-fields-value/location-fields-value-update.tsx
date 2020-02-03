import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './location-fields-value.reducer';
import { ILocationFieldsValue } from 'app/shared/model/location-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILocationFieldsValueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ILocationFieldsValueUpdateState {
  isNew: boolean;
}

export class LocationFieldsValueUpdate extends React.Component<ILocationFieldsValueUpdateProps, ILocationFieldsValueUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { locationFieldsValueEntity } = this.props;
      const entity = {
        ...locationFieldsValueEntity,
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
    this.props.history.push('/entity/location-fields-value');
  };

  render() {
    const { locationFieldsValueEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.locationFieldsValue.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.locationFieldsValue.home.createOrEditLabel">
                Create or edit a LocationFieldsValue
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : locationFieldsValueEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="location-fields-value-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="location-fields-value-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="locationFieldLabel" for="location-fields-value-locationField">
                    <Translate contentKey="serviceNetApp.locationFieldsValue.locationField">Location Field</Translate>
                  </Label>
                  <AvInput
                    id="location-fields-value-locationField"
                    type="select"
                    className="form-control"
                    name="locationField"
                    value={(!isNew && locationFieldsValueEntity.locationField) || 'NAME'}
                  >
                    <option value="NAME">{translate('serviceNetApp.LocationFields.NAME')}</option>
                    <option value="ALTERNATE_NAME">{translate('serviceNetApp.LocationFields.ALTERNATE_NAME')}</option>
                    <option value="DESCRIPTION">{translate('serviceNetApp.LocationFields.DESCRIPTION')}</option>
                    <option value="TRANSPORTATION">{translate('serviceNetApp.LocationFields.TRANSPORTATION')}</option>
                    <option value="LATITUDE">{translate('serviceNetApp.LocationFields.LATITUDE')}</option>
                    <option value="LONGITUDE">{translate('serviceNetApp.LocationFields.LONGITUDE')}</option>
                    <option value="REGULAR_SCHEDULE_NOTES">{translate('serviceNetApp.LocationFields.REGULAR_SCHEDULE_NOTES')}</option>
                    <option value="PHYSICAL_ADDRESS">{translate('serviceNetApp.LocationFields.PHYSICAL_ADDRESS')}</option>
                    <option value="POSTAL_ADDRESS">{translate('serviceNetApp.LocationFields.POSTAL_ADDRESS')}</option>
                    <option value="REGULAR_SCHEDULE_OPENING_HOURS">
                      {translate('serviceNetApp.LocationFields.REGULAR_SCHEDULE_OPENING_HOURS')}
                    </option>
                    <option value="LANGS">{translate('serviceNetApp.LocationFields.LANGS')}</option>
                    <option value="HOLIDAY_SCHEDULES">{translate('serviceNetApp.LocationFields.HOLIDAY_SCHEDULES')}</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/location-fields-value" replace color="info">
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
  locationFieldsValueEntity: storeState.locationFieldsValue.entity,
  loading: storeState.locationFieldsValue.loading,
  updating: storeState.locationFieldsValue.updating,
  updateSuccess: storeState.locationFieldsValue.updateSuccess
});

const mapDispatchToProps = {
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
)(LocationFieldsValueUpdate);
