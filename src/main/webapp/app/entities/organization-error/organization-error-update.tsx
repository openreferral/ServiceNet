import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IOrganization } from 'app/shared/model/organization.model';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { getEntities as getDataImportReports } from 'app/entities/data-import-report/data-import-report.reducer';
import { getEntity, updateEntity, createEntity, reset } from './organization-error.reducer';
import { IOrganizationError } from 'app/shared/model/organization-error.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrganizationErrorUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOrganizationErrorUpdateState {
  isNew: boolean;
  organizationId: string;
}

export class OrganizationErrorUpdate extends React.Component<IOrganizationErrorUpdateProps, IOrganizationErrorUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      organizationId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getOrganizations();
    this.props.getDataImportReports();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { organizationErrorEntity } = this.props;
      const entity = {
        ...organizationErrorEntity,
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
    this.props.history.push('/entity/organization-error');
  };

  render() {
    const { organizationErrorEntity, organizations, dataImportReports, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.organizationError.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.organizationError.home.createOrEditLabel">Create or edit a OrganizationError</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : organizationErrorEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="organization-error-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="organization-error-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="entityNameLabel" for="organization-error-entityName">
                    <Translate contentKey="serviceNetApp.organizationError.entityName">Entity Name</Translate>
                  </Label>
                  <AvField id="organization-error-entityName" type="text" name="entityName" />
                </AvGroup>
                <AvGroup>
                  <Label id="fieldNameLabel" for="organization-error-fieldName">
                    <Translate contentKey="serviceNetApp.organizationError.fieldName">Field Name</Translate>
                  </Label>
                  <AvField id="organization-error-fieldName" type="text" name="fieldName" />
                </AvGroup>
                <AvGroup>
                  <Label id="externalDbIdLabel" for="organization-error-externalDbId">
                    <Translate contentKey="serviceNetApp.organizationError.externalDbId">External Db Id</Translate>
                  </Label>
                  <AvField id="organization-error-externalDbId" type="text" name="externalDbId" />
                </AvGroup>
                <AvGroup>
                  <Label id="invalidValueLabel" for="organization-error-invalidValue">
                    <Translate contentKey="serviceNetApp.organizationError.invalidValue">Invalid Value</Translate>
                  </Label>
                  <AvField id="organization-error-invalidValue" type="text" name="invalidValue" />
                </AvGroup>
                <AvGroup>
                  <Label id="causeLabel" for="organization-error-cause">
                    <Translate contentKey="serviceNetApp.organizationError.cause">Cause</Translate>
                  </Label>
                  <AvField id="organization-error-cause" type="text" name="cause" />
                </AvGroup>
                <AvGroup>
                  <Label for="organization-error-organization">
                    <Translate contentKey="serviceNetApp.organizationError.organization">Organization</Translate>
                  </Label>
                  <AvInput id="organization-error-organization" type="select" className="form-control" name="organization.id">
                    <option value="" key="0" />
                    {organizations
                      ? organizations.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="organization-error-dataImportReport">
                    <Translate contentKey="serviceNetApp.organizationError.dataImportReport">Data Import Report</Translate>
                  </Label>
                  <AvInput id="organization-error-dataImportReport" type="select" className="form-control" name="dataImportReport.id">
                    <option value="" key="0" />
                    {dataImportReports
                      ? dataImportReports.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.startDate}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/organization-error" replace color="info">
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
  organizations: storeState.organization.entities,
  dataImportReports: storeState.dataImportReport.entities,
  organizationErrorEntity: storeState.organizationError.entity,
  loading: storeState.organizationError.loading,
  updating: storeState.organizationError.updating,
  updateSuccess: storeState.organizationError.updateSuccess
});

const mapDispatchToProps = {
  getOrganizations,
  getDataImportReports,
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
)(OrganizationErrorUpdate);
