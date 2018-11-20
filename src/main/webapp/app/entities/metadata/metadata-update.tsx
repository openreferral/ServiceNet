import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './metadata.reducer';
import { IMetadata } from 'app/shared/model/metadata.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMetadataUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMetadataUpdateState {
  isNew: boolean;
  userId: string;
}

export class MetadataUpdate extends React.Component<IMetadataUpdateProps, IMetadataUpdateState> {
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

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.lastActionDate = new Date(values.lastActionDate);

    if (errors.length === 0) {
      const { metadataEntity } = this.props;
      const entity = {
        ...metadataEntity,
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
    this.props.history.push('/entity/metadata');
  };

  render() {
    const { metadataEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    const { previousValue, replacementValue } = metadataEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.metadata.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.metadata.home.createOrEditLabel">Create or edit a Metadata</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : metadataEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="metadata-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="resourceIdLabel" for="resourceId">
                    <Translate contentKey="serviceNetApp.metadata.resourceId">Resource Id</Translate>
                  </Label>
                  <AvField
                    id="metadata-resourceId"
                    type="text"
                    name="resourceId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastActionDateLabel" for="lastActionDate">
                    <Translate contentKey="serviceNetApp.metadata.lastActionDate">Last Action Date</Translate>
                  </Label>
                  <AvInput
                    id="metadata-lastActionDate"
                    type="datetime-local"
                    className="form-control"
                    name="lastActionDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.metadataEntity.lastActionDate)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastActionTypeLabel">
                    <Translate contentKey="serviceNetApp.metadata.lastActionType">Last Action Type</Translate>
                  </Label>
                  <AvInput
                    id="metadata-lastActionType"
                    type="select"
                    className="form-control"
                    name="lastActionType"
                    value={(!isNew && metadataEntity.lastActionType) || 'CREATE'}
                  >
                    <option value="CREATE">
                      <Translate contentKey="serviceNetApp.ActionType.CREATE" />
                    </option>
                    <option value="UPDATE">
                      <Translate contentKey="serviceNetApp.ActionType.UPDATE" />
                    </option>
                    <option value="DELETE">
                      <Translate contentKey="serviceNetApp.ActionType.DELETE" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="fieldNameLabel" for="fieldName">
                    <Translate contentKey="serviceNetApp.metadata.fieldName">Field Name</Translate>
                  </Label>
                  <AvField
                    id="metadata-fieldName"
                    type="text"
                    name="fieldName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="previousValueLabel" for="previousValue">
                    <Translate contentKey="serviceNetApp.metadata.previousValue">Previous Value</Translate>
                  </Label>
                  <AvInput id="metadata-previousValue" type="textarea" name="previousValue" />
                </AvGroup>
                <AvGroup>
                  <Label id="replacementValueLabel" for="replacementValue">
                    <Translate contentKey="serviceNetApp.metadata.replacementValue">Replacement Value</Translate>
                  </Label>
                  <AvInput id="metadata-replacementValue" type="textarea" name="replacementValue" />
                </AvGroup>
                <AvGroup>
                  <Label for="user.login">
                    <Translate contentKey="serviceNetApp.metadata.user">User</Translate>
                  </Label>
                  <AvInput id="metadata-user" type="select" className="form-control" name="userId">
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/metadata" replace color="info">
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
  metadataEntity: storeState.metadata.entity,
  loading: storeState.metadata.loading,
  updating: storeState.metadata.updating,
  updateSuccess: storeState.metadata.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
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
)(MetadataUpdate);
