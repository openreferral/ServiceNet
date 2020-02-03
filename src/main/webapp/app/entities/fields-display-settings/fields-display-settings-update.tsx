import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './fields-display-settings.reducer';
import { IFieldsDisplaySettings } from 'app/shared/model/fields-display-settings.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFieldsDisplaySettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IFieldsDisplaySettingsUpdateState {
  isNew: boolean;
  userId: string;
}

export class FieldsDisplaySettingsUpdate extends React.Component<IFieldsDisplaySettingsUpdateProps, IFieldsDisplaySettingsUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
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

    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { fieldsDisplaySettingsEntity } = this.props;
      const entity = {
        ...fieldsDisplaySettingsEntity,
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
    this.props.history.push('/entity/fields-display-settings');
  };

  render() {
    const { fieldsDisplaySettingsEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.fieldsDisplaySettings.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.fieldsDisplaySettings.home.createOrEditLabel">
                Create or edit a FieldsDisplaySettings
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : fieldsDisplaySettingsEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="fields-display-settings-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="fields-display-settings-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="fields-display-settings-name">
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.name">Name</Translate>
                  </Label>
                  <AvField
                    id="fields-display-settings-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="locationFieldsLabel" for="fields-display-settings-locationFields">
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.locationFields">Location Fields</Translate>
                  </Label>
                  <AvField id="fields-display-settings-locationFields" type="text" name="locationFields" />
                </AvGroup>
                <AvGroup>
                  <Label id="organizationFieldsLabel" for="fields-display-settings-organizationFields">
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.organizationFields">Organization Fields</Translate>
                  </Label>
                  <AvField id="fields-display-settings-organizationFields" type="text" name="organizationFields" />
                </AvGroup>
                <AvGroup>
                  <Label id="physicalAddressFieldsLabel" for="fields-display-settings-physicalAddressFields">
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.physicalAddressFields">Physical Address Fields</Translate>
                  </Label>
                  <AvField id="fields-display-settings-physicalAddressFields" type="text" name="physicalAddressFields" />
                </AvGroup>
                <AvGroup>
                  <Label id="postalAddressFieldsLabel" for="fields-display-settings-postalAddressFields">
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.postalAddressFields">Postal Address Fields</Translate>
                  </Label>
                  <AvField id="fields-display-settings-postalAddressFields" type="text" name="postalAddressFields" />
                </AvGroup>
                <AvGroup>
                  <Label id="serviceFieldsLabel" for="fields-display-settings-serviceFields">
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.serviceFields">Service Fields</Translate>
                  </Label>
                  <AvField id="fields-display-settings-serviceFields" type="text" name="serviceFields" />
                </AvGroup>
                <AvGroup>
                  <Label id="serviceTaxonomiesDetailsFieldsLabel" for="fields-display-settings-serviceTaxonomiesDetailsFields">
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.serviceTaxonomiesDetailsFields">
                      Service Taxonomies Details Fields
                    </Translate>
                  </Label>
                  <AvField id="fields-display-settings-serviceTaxonomiesDetailsFields" type="text" name="serviceTaxonomiesDetailsFields" />
                </AvGroup>
                <AvGroup>
                  <Label id="contactDetailsFieldsLabel" for="fields-display-settings-contactDetailsFields">
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.contactDetailsFields">Contact Details Fields</Translate>
                  </Label>
                  <AvField id="fields-display-settings-contactDetailsFields" type="text" name="contactDetailsFields" />
                </AvGroup>
                <AvGroup>
                  <Label for="fields-display-settings-user">
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.user">User</Translate>
                  </Label>
                  <AvInput id="fields-display-settings-user" type="select" className="form-control" name="userId">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/fields-display-settings" replace color="info">
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
  users: storeState.userManagement.users,
  fieldsDisplaySettingsEntity: storeState.fieldsDisplaySettings.entity,
  loading: storeState.fieldsDisplaySettings.loading,
  updating: storeState.fieldsDisplaySettings.updating,
  updateSuccess: storeState.fieldsDisplaySettings.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
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
)(FieldsDisplaySettingsUpdate);
