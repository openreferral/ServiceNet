import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './postal-address-fields-value.reducer';
import { IPostalAddressFieldsValue } from 'app/shared/model/postal-address-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPostalAddressFieldsValueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPostalAddressFieldsValueUpdateState {
  isNew: boolean;
}

export class PostalAddressFieldsValueUpdate extends React.Component<
  IPostalAddressFieldsValueUpdateProps,
  IPostalAddressFieldsValueUpdateState
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
      const { postalAddressFieldsValueEntity } = this.props;
      const entity = {
        ...postalAddressFieldsValueEntity,
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
    this.props.history.push('/entity/postal-address-fields-value');
  };

  render() {
    const { postalAddressFieldsValueEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.postalAddressFieldsValue.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.postalAddressFieldsValue.home.createOrEditLabel">
                Create or edit a PostalAddressFieldsValue
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : postalAddressFieldsValueEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="postal-address-fields-value-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="postal-address-fields-value-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="postalAddressFieldLabel" for="postal-address-fields-value-postalAddressField">
                    <Translate contentKey="serviceNetApp.postalAddressFieldsValue.postalAddressField">Postal Address Field</Translate>
                  </Label>
                  <AvInput
                    id="postal-address-fields-value-postalAddressField"
                    type="select"
                    className="form-control"
                    name="postalAddressField"
                    value={(!isNew && postalAddressFieldsValueEntity.postalAddressField) || 'ATTENTION'}
                  >
                    <option value="ATTENTION">{translate('serviceNetApp.PostalAddressFields.ATTENTION')}</option>
                    <option value="ADDRESS1">{translate('serviceNetApp.PostalAddressFields.ADDRESS1')}</option>
                    <option value="CITY">{translate('serviceNetApp.PostalAddressFields.CITY')}</option>
                    <option value="REGION">{translate('serviceNetApp.PostalAddressFields.REGION')}</option>
                    <option value="STATE_PROVINCE">{translate('serviceNetApp.PostalAddressFields.STATE_PROVINCE')}</option>
                    <option value="POSTAL_CODE">{translate('serviceNetApp.PostalAddressFields.POSTAL_CODE')}</option>
                    <option value="COUNTRY">{translate('serviceNetApp.PostalAddressFields.COUNTRY')}</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/postal-address-fields-value" replace color="info">
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
  postalAddressFieldsValueEntity: storeState.postalAddressFieldsValue.entity,
  loading: storeState.postalAddressFieldsValue.loading,
  updating: storeState.postalAddressFieldsValue.updating,
  updateSuccess: storeState.postalAddressFieldsValue.updateSuccess
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
)(PostalAddressFieldsValueUpdate);
