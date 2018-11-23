import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { IDocumentUpload } from 'app/shared/model/document-upload.model';
import { getEntities as getDocumentUploads } from 'app/entities/document-upload/document-upload.reducer';
import { ISystemAccount } from 'app/shared/model/system-account.model';
import { getEntities as getSystemAccounts } from 'app/entities/system-account/system-account.reducer';
import { IFunding } from 'app/shared/model/funding.model';
import { getEntities as getFundings } from 'app/entities/funding/funding.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './organization.reducer';
import { IOrganization } from 'app/shared/model/organization.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrganizationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOrganizationUpdateState {
  isNew: boolean;
  locationId: string;
  replacedById: string;
  sourceDocumentId: string;
  accountId: string;
  fundingId: string;
}

export class OrganizationUpdate extends React.Component<IOrganizationUpdateProps, IOrganizationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      locationId: '0',
      replacedById: '0',
      sourceDocumentId: '0',
      accountId: '0',
      fundingId: '0',
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

    this.props.getLocations();
    this.props.getOrganizations();
    this.props.getDocumentUploads();
    this.props.getSystemAccounts();
    this.props.getFundings();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.updatedAt = new Date(values.updatedAt);

    if (errors.length === 0) {
      const { organizationEntity } = this.props;
      const entity = {
        ...organizationEntity,
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
    this.props.history.push('/entity/organization');
  };

  render() {
    const { organizationEntity, locations, organizations, documentUploads, systemAccounts, fundings, loading, updating } = this.props;
    const { isNew } = this.state;

    const { description } = organizationEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.organization.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.organization.home.createOrEditLabel">Create or edit a Organization</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : organizationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="organization-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="serviceNetApp.organization.name">Name</Translate>
                  </Label>
                  <AvField
                    id="organization-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="alternateNameLabel" for="alternateName">
                    <Translate contentKey="serviceNetApp.organization.alternateName">Alternate Name</Translate>
                  </Label>
                  <AvField id="organization-alternateName" type="text" name="alternateName" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="serviceNetApp.organization.description">Description</Translate>
                  </Label>
                  <AvInput id="organization-description" type="textarea" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    <Translate contentKey="serviceNetApp.organization.email">Email</Translate>
                  </Label>
                  <AvField
                    id="organization-email"
                    type="text"
                    name="email"
                    validate={{
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="urlLabel" for="url">
                    <Translate contentKey="serviceNetApp.organization.url">Url</Translate>
                  </Label>
                  <AvField id="organization-url" type="text" name="url" />
                </AvGroup>
                <AvGroup>
                  <Label id="taxStatusLabel" for="taxStatus">
                    <Translate contentKey="serviceNetApp.organization.taxStatus">Tax Status</Translate>
                  </Label>
                  <AvField id="organization-taxStatus" type="text" name="taxStatus" />
                </AvGroup>
                <AvGroup>
                  <Label id="taxIdLabel" for="taxId">
                    <Translate contentKey="serviceNetApp.organization.taxId">Tax Id</Translate>
                  </Label>
                  <AvField id="organization-taxId" type="text" name="taxId" />
                </AvGroup>
                <AvGroup>
                  <Label id="yearIncorporatedLabel" for="yearIncorporated">
                    <Translate contentKey="serviceNetApp.organization.yearIncorporated">Year Incorporated</Translate>
                  </Label>
                  <AvField id="organization-yearIncorporated" type="date" className="form-control" name="yearIncorporated" />
                </AvGroup>
                <AvGroup>
                  <Label id="legalStatusLabel" for="legalStatus">
                    <Translate contentKey="serviceNetApp.organization.legalStatus">Legal Status</Translate>
                  </Label>
                  <AvField id="organization-legalStatus" type="text" name="legalStatus" />
                </AvGroup>
                <AvGroup>
                  <Label id="activeLabel" check>
                    <AvInput id="organization-active" type="checkbox" className="form-control" name="active" />
                    <Translate contentKey="serviceNetApp.organization.active">Active</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="updatedAtLabel" for="updatedAt">
                    <Translate contentKey="serviceNetApp.organization.updatedAt">Updated At</Translate>
                  </Label>
                  <AvInput
                    id="organization-updatedAt"
                    type="datetime-local"
                    className="form-control"
                    name="updatedAt"
                    value={isNew ? null : convertDateTimeFromServer(this.props.organizationEntity.updatedAt)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="location.name">
                    <Translate contentKey="serviceNetApp.organization.location">Location</Translate>
                  </Label>
                  <AvInput id="organization-location" type="select" className="form-control" name="locationId">
                    <option value="" key="0" />
                    {locations
                      ? locations.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="replacedBy.id">
                    <Translate contentKey="serviceNetApp.organization.replacedBy">Replaced By</Translate>
                  </Label>
                  <AvInput id="organization-replacedBy" type="select" className="form-control" name="replacedById">
                    <option value="" key="0" />
                    {organizations
                      ? organizations.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="sourceDocument.dateUploaded">
                    <Translate contentKey="serviceNetApp.organization.sourceDocument">Source Document</Translate>
                  </Label>
                  <AvInput id="organization-sourceDocument" type="select" className="form-control" name="sourceDocumentId">
                    <option value="" key="0" />
                    {documentUploads
                      ? documentUploads.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.dateUploaded}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="account.name">
                    <Translate contentKey="serviceNetApp.organization.account">Account</Translate>
                  </Label>
                  <AvInput id="organization-account" type="select" className="form-control" name="accountId">
                    <option value="" key="0" />
                    {systemAccounts
                      ? systemAccounts.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/organization" replace color="info">
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
  locations: storeState.location.entities,
  organizations: storeState.organization.entities,
  documentUploads: storeState.documentUpload.entities,
  systemAccounts: storeState.systemAccount.entities,
  fundings: storeState.funding.entities,
  organizationEntity: storeState.organization.entity,
  loading: storeState.organization.loading,
  updating: storeState.organization.updating,
  updateSuccess: storeState.organization.updateSuccess
});

const mapDispatchToProps = {
  getLocations,
  getOrganizations,
  getDocumentUploads,
  getSystemAccounts,
  getFundings,
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
)(OrganizationUpdate);
