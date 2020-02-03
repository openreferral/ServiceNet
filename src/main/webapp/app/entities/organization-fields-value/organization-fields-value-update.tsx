import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './organization-fields-value.reducer';
import { IOrganizationFieldsValue } from 'app/shared/model/organization-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrganizationFieldsValueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOrganizationFieldsValueUpdateState {
  isNew: boolean;
}

export class OrganizationFieldsValueUpdate extends React.Component<
  IOrganizationFieldsValueUpdateProps,
  IOrganizationFieldsValueUpdateState
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
      const { organizationFieldsValueEntity } = this.props;
      const entity = {
        ...organizationFieldsValueEntity,
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
    this.props.history.push('/entity/organization-fields-value');
  };

  render() {
    const { organizationFieldsValueEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.organizationFieldsValue.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.organizationFieldsValue.home.createOrEditLabel">
                Create or edit a OrganizationFieldsValue
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : organizationFieldsValueEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="organization-fields-value-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="organization-fields-value-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="organizationFieldLabel" for="organization-fields-value-organizationField">
                    <Translate contentKey="serviceNetApp.organizationFieldsValue.organizationField">Organization Field</Translate>
                  </Label>
                  <AvInput
                    id="organization-fields-value-organizationField"
                    type="select"
                    className="form-control"
                    name="organizationField"
                    value={(!isNew && organizationFieldsValueEntity.organizationField) || 'NAME'}
                  >
                    <option value="NAME">{translate('serviceNetApp.OrganizationFields.NAME')}</option>
                    <option value="ALTERNATE_NAME">{translate('serviceNetApp.OrganizationFields.ALTERNATE_NAME')}</option>
                    <option value="DESCRIPTION">{translate('serviceNetApp.OrganizationFields.DESCRIPTION')}</option>
                    <option value="EMAIL">{translate('serviceNetApp.OrganizationFields.EMAIL')}</option>
                    <option value="URL">{translate('serviceNetApp.OrganizationFields.URL')}</option>
                    <option value="TAX_STATUS">{translate('serviceNetApp.OrganizationFields.TAX_STATUS')}</option>
                    <option value="ACTIVE">{translate('serviceNetApp.OrganizationFields.ACTIVE')}</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/organization-fields-value" replace color="info">
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
  organizationFieldsValueEntity: storeState.organizationFieldsValue.entity,
  loading: storeState.organizationFieldsValue.loading,
  updating: storeState.organizationFieldsValue.updating,
  updateSuccess: storeState.organizationFieldsValue.updateSuccess
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
)(OrganizationFieldsValueUpdate);
