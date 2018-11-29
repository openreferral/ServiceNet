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
import { getEntity, updateEntity, createEntity, reset } from './document-upload.reducer';
import { IDocumentUpload } from 'app/shared/model/document-upload.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDocumentUploadUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IDocumentUploadUpdateState {
  isNew: boolean;
  uploaderId: string;
}

export class DocumentUploadUpdate extends React.Component<IDocumentUploadUpdateProps, IDocumentUploadUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      uploaderId: '0',
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
    values.dateUploaded = new Date(values.dateUploaded);

    if (errors.length === 0) {
      const { documentUploadEntity } = this.props;
      const entity = {
        ...documentUploadEntity,
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
    this.props.history.push('/entity/document-upload');
  };

  render() {
    const { documentUploadEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.documentUpload.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.documentUpload.home.createOrEditLabel">Create or edit a DocumentUpload</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : documentUploadEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="document-upload-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dateUploadedLabel" for="dateUploaded">
                    <Translate contentKey="serviceNetApp.documentUpload.dateUploaded">Date Uploaded</Translate>
                  </Label>
                  <AvInput
                    id="document-upload-dateUploaded"
                    type="datetime-local"
                    className="form-control"
                    name="dateUploaded"
                    value={isNew ? null : convertDateTimeFromServer(this.props.documentUploadEntity.dateUploaded)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="originalDocumentIdLabel" for="originalDocumentId">
                    <Translate contentKey="serviceNetApp.documentUpload.originalDocumentId">Original Document Id</Translate>
                  </Label>
                  <AvField
                    id="document-upload-originalDocumentId"
                    type="text"
                    name="originalDocumentId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="parsedDocumentIdLabel" for="parsedDocumentId">
                    <Translate contentKey="serviceNetApp.documentUpload.parsedDocumentId">Parsed Document Id</Translate>
                  </Label>
                  <AvField id="document-upload-parsedDocumentId" type="text" name="parsedDocumentId" validate={{}} />
                </AvGroup>
                <AvGroup>
                  <Label for="uploader.login">
                    <Translate contentKey="serviceNetApp.documentUpload.uploader">Uploader</Translate>
                  </Label>
                  <AvInput id="document-upload-uploader" type="select" className="form-control" name="uploaderId">
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/document-upload" replace color="info">
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
  documentUploadEntity: storeState.documentUpload.entity,
  loading: storeState.documentUpload.loading,
  updating: storeState.documentUpload.updating,
  updateSuccess: storeState.documentUpload.updateSuccess
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
)(DocumentUploadUpdate);
