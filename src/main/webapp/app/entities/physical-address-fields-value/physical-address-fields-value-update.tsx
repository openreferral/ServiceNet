import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './physical-address-fields-value.reducer';
import { IPhysicalAddressFieldsValue } from 'app/shared/model/physical-address-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPhysicalAddressFieldsValueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPhysicalAddressFieldsValueUpdateState {
  isNew: boolean;
}

export class PhysicalAddressFieldsValueUpdate extends React.Component<
  IPhysicalAddressFieldsValueUpdateProps,
  IPhysicalAddressFieldsValueUpdateState
> {
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
      const { physicalAddressFieldsValueEntity } = this.props;
      const entity = {
        ...physicalAddressFieldsValueEntity,
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
    this.props.history.push('/entity/physical-address-fields-value');
  };

  render() {
    const { physicalAddressFieldsValueEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.physicalAddressFieldsValue.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.physicalAddressFieldsValue.home.createOrEditLabel">
                Create or edit a PhysicalAddressFieldsValue
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : physicalAddressFieldsValueEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="physical-address-fields-value-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="physical-address-fields-value-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="physicalAddressFieldLabel" for="physical-address-fields-value-physicalAddressField">
                    <Translate contentKey="serviceNetApp.physicalAddressFieldsValue.physicalAddressField">Physical Address Field</Translate>
                  </Label>
                  <AvInput
                    id="physical-address-fields-value-physicalAddressField"
                    type="select"
                    className="form-control"
                    name="physicalAddressField"
                    value={(!isNew && physicalAddressFieldsValueEntity.physicalAddressField) || 'ATTENTION'}
                  >
                    <option value="ATTENTION">{translate('serviceNetApp.PhysicalAddressFields.ATTENTION')}</option>
                    <option value="ADDRESS_1">{translate('serviceNetApp.PhysicalAddressFields.ADDRESS_1')}</option>
                    <option value="CITY">{translate('serviceNetApp.PhysicalAddressFields.CITY')}</option>
                    <option value="REGION">{translate('serviceNetApp.PhysicalAddressFields.REGION')}</option>
                    <option value="STATE_PROVINCE">{translate('serviceNetApp.PhysicalAddressFields.STATE_PROVINCE')}</option>
                    <option value="POSTAL_CODE">{translate('serviceNetApp.PhysicalAddressFields.POSTAL_CODE')}</option>
                    <option value="COUNTRY">{translate('serviceNetApp.PhysicalAddressFields.COUNTRY')}</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/physical-address-fields-value" replace color="info">
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
  physicalAddressFieldsValueEntity: storeState.physicalAddressFieldsValue.entity,
  loading: storeState.physicalAddressFieldsValue.loading,
  updating: storeState.physicalAddressFieldsValue.updating,
  updateSuccess: storeState.physicalAddressFieldsValue.updateSuccess
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
)(PhysicalAddressFieldsValueUpdate);
