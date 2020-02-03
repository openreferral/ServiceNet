import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './service-fields-value.reducer';
import { IServiceFieldsValue } from 'app/shared/model/service-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IServiceFieldsValueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IServiceFieldsValueUpdateState {
  isNew: boolean;
}

export class ServiceFieldsValueUpdate extends React.Component<IServiceFieldsValueUpdateProps, IServiceFieldsValueUpdateState> {
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
      const { serviceFieldsValueEntity } = this.props;
      const entity = {
        ...serviceFieldsValueEntity,
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
    this.props.history.push('/entity/service-fields-value');
  };

  render() {
    const { serviceFieldsValueEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.serviceFieldsValue.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.serviceFieldsValue.home.createOrEditLabel">
                Create or edit a ServiceFieldsValue
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : serviceFieldsValueEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="service-fields-value-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="service-fields-value-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="serviceFieldLabel" for="service-fields-value-serviceField">
                    <Translate contentKey="serviceNetApp.serviceFieldsValue.serviceField">Service Field</Translate>
                  </Label>
                  <AvInput
                    id="service-fields-value-serviceField"
                    type="select"
                    className="form-control"
                    name="serviceField"
                    value={(!isNew && serviceFieldsValueEntity.serviceField) || 'NAME'}
                  >
                    <option value="NAME">{translate('serviceNetApp.ServiceFields.NAME')}</option>
                    <option value="ALTERNATE_NAME">{translate('serviceNetApp.ServiceFields.ALTERNATE_NAME')}</option>
                    <option value="DESCRIPTION">{translate('serviceNetApp.ServiceFields.DESCRIPTION')}</option>
                    <option value="URL">{translate('serviceNetApp.ServiceFields.URL')}</option>
                    <option value="EMAIL">{translate('serviceNetApp.ServiceFields.EMAIL')}</option>
                    <option value="STATUS">{translate('serviceNetApp.ServiceFields.STATUS')}</option>
                    <option value="INTERPRETATION_SERVICES">{translate('serviceNetApp.ServiceFields.INTERPRETATION_SERVICES')}</option>
                    <option value="APPLICATION_PROCESS">{translate('serviceNetApp.ServiceFields.APPLICATION_PROCESS')}</option>
                    <option value="WAIT_TIME">{translate('serviceNetApp.ServiceFields.WAIT_TIME')}</option>
                    <option value="FEES">{translate('serviceNetApp.ServiceFields.FEES')}</option>
                    <option value="ACCREDITATIONS">{translate('serviceNetApp.ServiceFields.ACCREDITATIONS')}</option>
                    <option value="LICENSES">{translate('serviceNetApp.ServiceFields.LICENSES')}</option>
                    <option value="TYPE">{translate('serviceNetApp.ServiceFields.TYPE')}</option>
                    <option value="ELIGIBILITY">{translate('serviceNetApp.ServiceFields.ELIGIBILITY')}</option>
                    <option value="FUNDING">{translate('serviceNetApp.ServiceFields.FUNDING')}</option>
                    <option value="DOCS">{translate('serviceNetApp.ServiceFields.DOCS')}</option>
                    <option value="PAYMENTS_ACCEPTEDS">{translate('serviceNetApp.ServiceFields.PAYMENTS_ACCEPTEDS')}</option>
                    <option value="TAXONOMIES">{translate('serviceNetApp.ServiceFields.TAXONOMIES')}</option>
                    <option value="REGULAR_SCHEDULE_OPENING_HOURS">
                      {translate('serviceNetApp.ServiceFields.REGULAR_SCHEDULE_OPENING_HOURS')}
                    </option>
                    <option value="LANGS">{translate('serviceNetApp.ServiceFields.LANGS')}</option>
                    <option value="HOLIDAY_SCHEDULES">{translate('serviceNetApp.ServiceFields.HOLIDAY_SCHEDULES')}</option>
                    <option value="CONTACTS">{translate('serviceNetApp.ServiceFields.CONTACTS')}</option>
                    <option value="PHONES">{translate('serviceNetApp.ServiceFields.PHONES')}</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/service-fields-value" replace color="info">
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
  serviceFieldsValueEntity: storeState.serviceFieldsValue.entity,
  loading: storeState.serviceFieldsValue.loading,
  updating: storeState.serviceFieldsValue.updating,
  updateSuccess: storeState.serviceFieldsValue.updateSuccess
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
)(ServiceFieldsValueUpdate);
