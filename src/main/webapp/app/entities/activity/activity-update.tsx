import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IMetadata } from 'app/shared/model/metadata.model';
import { getEntities as getMetadata } from 'app/entities/metadata/metadata.reducer';
import { IOrganization } from 'app/shared/model/organization.model';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { getEntity, updateEntity, createEntity, reset } from './activity.reducer';
import { IActivity } from 'app/shared/model/activity.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IActivityUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IActivityUpdateState {
  isNew: boolean;
  idsreviewers: any[];
  userId: string;
  metadataId: string;
  organizationId: string;
}

export class ActivityUpdate extends React.Component<IActivityUpdateProps, IActivityUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsreviewers: [],
      userId: '0',
      metadataId: '0',
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
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
    this.props.getMetadata();
    this.props.getOrganizations();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { activityEntity } = this.props;
      const entity = {
        ...activityEntity,
        ...values,
        reviewers: mapIdList(values.reviewers)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/activity');
  };

  render() {
    const { activityEntity, users, metadata, organizations, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.activity.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.activity.home.createOrEditLabel">Create or edit a Activity</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : activityEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="activity-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label for="user.login">
                    <Translate contentKey="serviceNetApp.activity.user">User</Translate>
                  </Label>
                  <AvInput id="activity-user" type="select" className="form-control" name="userId">
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="users">
                    <Translate contentKey="serviceNetApp.activity.reviewers">Reviewers</Translate>
                  </Label>
                  <AvInput
                    id="activity-reviewers"
                    type="select"
                    multiple
                    className="form-control"
                    name="reviewers"
                    value={activityEntity.reviewers && activityEntity.reviewers.map(e => e.id)}
                  >
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
                <AvGroup>
                  <Label for="metadata.id">
                    <Translate contentKey="serviceNetApp.activity.metadata">Metadata</Translate>
                  </Label>
                  <AvInput id="activity-metadata" type="select" className="form-control" name="metadataId">
                    {metadata
                      ? metadata.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.fieldName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="organization.id">
                    <Translate contentKey="serviceNetApp.activity.organization">Organization</Translate>
                  </Label>
                  <AvInput id="activity-organization" type="select" className="form-control" name="organizationId">
                    {organizations
                      ? organizations.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.locationName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/activity" replace color="info">
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
  metadata: storeState.metadata.entities,
  organizations: storeState.organization.entities,
  activityEntity: storeState.activity.entity,
  loading: storeState.activity.loading,
  updating: storeState.activity.updating,
  updateSuccess: storeState.activity.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getMetadata,
  getOrganizations,
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
)(ActivityUpdate);
