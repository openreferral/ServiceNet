import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IOrganization } from 'app/shared/model/organization.model';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './organization-match.reducer';
import { IOrganizationMatch } from 'app/shared/model/organization-match.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrganizationMatchUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOrganizationMatchUpdateState {
  isNew: boolean;
  organizationRecordId: string;
  partnerVersionId: string;
}

export class OrganizationMatchUpdate extends React.Component<IOrganizationMatchUpdateProps, IOrganizationMatchUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      organizationRecordId: '0',
      partnerVersionId: '0',
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

    this.props.getOrganizations();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.timestamp = new Date(values.timestamp);

    if (errors.length === 0) {
      const { organizationMatchEntity } = this.props;
      const entity = {
        ...organizationMatchEntity,
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
    this.props.history.push('/entity/organization-match');
  };

  render() {
    const { organizationMatchEntity, organizations, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.organizationMatch.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.organizationMatch.home.createOrEditLabel">Create or edit a OrganizationMatch</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : organizationMatchEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="organization-match-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="timestampLabel" for="timestamp">
                    <Translate contentKey="serviceNetApp.organizationMatch.timestamp">Timestamp</Translate>
                  </Label>
                  <AvInput
                    id="organization-match-timestamp"
                    type="datetime-local"
                    className="form-control"
                    name="timestamp"
                    value={isNew ? null : convertDateTimeFromServer(this.props.organizationMatchEntity.timestamp)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="deletedLabel" check>
                    <AvInput id="organization-match-deleted" type="checkbox" className="form-control" name="deleted" />
                    <Translate contentKey="serviceNetApp.organizationMatch.deleted">Deleted</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="organizationRecord.name">
                    <Translate contentKey="serviceNetApp.organizationMatch.organizationRecord">Organization Record</Translate>
                  </Label>
                  <AvInput id="organization-match-organizationRecord" type="select" className="form-control" name="organizationRecordId">
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
                  <Label for="partnerVersion.name">
                    <Translate contentKey="serviceNetApp.organizationMatch.partnerVersion">Partner Version</Translate>
                  </Label>
                  <AvInput id="organization-match-partnerVersion" type="select" className="form-control" name="partnerVersionId">
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
                <Button tag={Link} id="cancel-save" to="/entity/organization-match" replace color="info">
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
  organizationMatchEntity: storeState.organizationMatch.entity,
  loading: storeState.organizationMatch.loading,
  updating: storeState.organizationMatch.updating,
  updateSuccess: storeState.organizationMatch.updateSuccess
});

const mapDispatchToProps = {
  getOrganizations,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrganizationMatchUpdate);
