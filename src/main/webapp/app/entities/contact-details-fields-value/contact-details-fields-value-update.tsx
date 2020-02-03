import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './contact-details-fields-value.reducer';
import { IContactDetailsFieldsValue } from 'app/shared/model/contact-details-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IContactDetailsFieldsValueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IContactDetailsFieldsValueUpdateState {
  isNew: boolean;
}

export class ContactDetailsFieldsValueUpdate extends React.Component<
  IContactDetailsFieldsValueUpdateProps,
  IContactDetailsFieldsValueUpdateState
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
      const { contactDetailsFieldsValueEntity } = this.props;
      const entity = {
        ...contactDetailsFieldsValueEntity,
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
    this.props.history.push('/entity/contact-details-fields-value');
  };

  render() {
    const { contactDetailsFieldsValueEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.contactDetailsFieldsValue.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.contactDetailsFieldsValue.home.createOrEditLabel">
                Create or edit a ContactDetailsFieldsValue
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : contactDetailsFieldsValueEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="contact-details-fields-value-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="contact-details-fields-value-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="contactDetailsFieldLabel" for="contact-details-fields-value-contactDetailsField">
                    <Translate contentKey="serviceNetApp.contactDetailsFieldsValue.contactDetailsField">Contact Details Field</Translate>
                  </Label>
                  <AvInput
                    id="contact-details-fields-value-contactDetailsField"
                    type="select"
                    className="form-control"
                    name="contactDetailsField"
                    value={(!isNew && contactDetailsFieldsValueEntity.contactDetailsField) || 'NAME'}
                  >
                    <option value="NAME">{translate('serviceNetApp.ContactDetailsFields.NAME')}</option>
                    <option value="TITLE">{translate('serviceNetApp.ContactDetailsFields.TITLE')}</option>
                    <option value="DEPARTMENT">{translate('serviceNetApp.ContactDetailsFields.DEPARTMENT')}</option>
                    <option value="EMAIL">{translate('serviceNetApp.ContactDetailsFields.EMAIL')}</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/contact-details-fields-value" replace color="info">
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
  contactDetailsFieldsValueEntity: storeState.contactDetailsFieldsValue.entity,
  loading: storeState.contactDetailsFieldsValue.loading,
  updating: storeState.contactDetailsFieldsValue.updating,
  updateSuccess: storeState.contactDetailsFieldsValue.updateSuccess
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
)(ContactDetailsFieldsValueUpdate);
