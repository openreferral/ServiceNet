import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './data-import-report.reducer';
import { IDataImportReport } from 'app/shared/model/data-import-report.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDataImportReportUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IDataImportReportUpdateState {
  isNew: boolean;
  userId: string;
}

export class DataImportReportUpdate extends React.Component<IDataImportReportUpdateProps, IDataImportReportUpdateState> {
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
    values.startDate = new Date(values.startDate);
    values.endDate = new Date(values.endDate);

    if (errors.length === 0) {
      const { dataImportReportEntity } = this.props;
      const entity = {
        ...dataImportReportEntity,
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
    this.props.history.push('/entity/data-import-report');
  };

  render() {
    const { dataImportReportEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.dataImportReport.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.dataImportReport.home.createOrEditLabel">Create or edit a DataImportReport</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : dataImportReportEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="data-import-report-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="numberOfUpdatedServicesLabel" for="numberOfUpdatedServices">
                    <Translate contentKey="serviceNetApp.dataImportReport.numberOfUpdatedServices">Number Of Updated Services</Translate>
                  </Label>
                  <AvField
                    id="data-import-report-numberOfUpdatedServices"
                    type="string"
                    className="form-control"
                    name="numberOfUpdatedServices"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="numberOfCreatedServicesLabel" for="numberOfCreatedServices">
                    <Translate contentKey="serviceNetApp.dataImportReport.numberOfCreatedServices">Number Of Created Services</Translate>
                  </Label>
                  <AvField
                    id="data-import-report-numberOfCreatedServices"
                    type="string"
                    className="form-control"
                    name="numberOfCreatedServices"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="numberOfUpdatedOrgsLabel" for="numberOfUpdatedOrgs">
                    <Translate contentKey="serviceNetApp.dataImportReport.numberOfUpdatedOrgs">Number Of Updated Orgs</Translate>
                  </Label>
                  <AvField
                    id="data-import-report-numberOfUpdatedOrgs"
                    type="string"
                    className="form-control"
                    name="numberOfUpdatedOrgs"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="numberOfCreatedOrgsLabel" for="numberOfCreatedOrgs">
                    <Translate contentKey="serviceNetApp.dataImportReport.numberOfCreatedOrgs">Number Of Created Orgs</Translate>
                  </Label>
                  <AvField
                    id="data-import-report-numberOfCreatedOrgs"
                    type="string"
                    className="form-control"
                    name="numberOfCreatedOrgs"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="startDateLabel" for="startDate">
                    <Translate contentKey="serviceNetApp.dataImportReport.startDate">Start Date</Translate>
                  </Label>
                  <AvInput
                    id="data-import-report-startDate"
                    type="datetime-local"
                    className="form-control"
                    name="startDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.dataImportReportEntity.startDate)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="endDateLabel" for="endDate">
                    <Translate contentKey="serviceNetApp.dataImportReport.endDate">End Date</Translate>
                  </Label>
                  <AvInput
                    id="data-import-report-endDate"
                    type="datetime-local"
                    className="form-control"
                    name="endDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.dataImportReportEntity.endDate)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="user.login">
                    <Translate contentKey="serviceNetApp.dataImportReport.user">User</Translate>
                  </Label>
                  <AvInput id="data-import-report-user" type="select" className="form-control" name="userId">
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
                <Button tag={Link} id="cancel-save" to="/entity/data-import-report" replace color="info">
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
  dataImportReportEntity: storeState.dataImportReport.entity,
  loading: storeState.dataImportReport.loading,
  updating: storeState.dataImportReport.updating,
  updateSuccess: storeState.dataImportReport.updateSuccess
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
)(DataImportReportUpdate);
