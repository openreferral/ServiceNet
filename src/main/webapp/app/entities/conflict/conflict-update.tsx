import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISystemAccount } from 'app/shared/model/system-account.model';
import { getEntities as getSystemAccounts } from 'app/entities/system-account/system-account.reducer';
import { getEntity, updateEntity, createEntity, reset } from './conflict.reducer';
import { IConflict } from 'app/shared/model/conflict.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IConflictUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IConflictUpdateState {
  isNew: boolean;
  idsacceptedThisChange: any[];
  ownerId: string;
}

export class ConflictUpdate extends React.Component<IConflictUpdateProps, IConflictUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsacceptedThisChange: [],
      ownerId: '0',
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

    this.props.getSystemAccounts();
  }

  saveEntity = (event, errors, values) => {
    values.currentValueDate = new Date(values.currentValueDate);
    values.offeredValueDate = new Date(values.offeredValueDate);
    values.stateDate = new Date(values.stateDate);
    values.createdDate = new Date(values.createdDate);

    if (errors.length === 0) {
      const { conflictEntity } = this.props;
      const entity = {
        ...conflictEntity,
        ...values,
        acceptedThisChanges: mapIdList(values.acceptedThisChanges)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/conflict');
  };

  render() {
    const { conflictEntity, systemAccounts, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.conflict.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.conflict.home.createOrEditLabel">Create or edit a Conflict</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : conflictEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="conflict-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="currentValueLabel" for="currentValue">
                    <Translate contentKey="serviceNetApp.conflict.currentValue">Current Value</Translate>
                  </Label>
                  <AvField id="conflict-currentValue" type="text" name="currentValue" />
                </AvGroup>
                <AvGroup>
                  <Label id="currentValueDateLabel" for="currentValueDate">
                    <Translate contentKey="serviceNetApp.conflict.currentValueDate">Current Value Date</Translate>
                  </Label>
                  <AvInput
                    id="conflict-currentValueDate"
                    type="datetime-local"
                    className="form-control"
                    name="currentValueDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.conflictEntity.currentValueDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="offeredValueLabel" for="offeredValue">
                    <Translate contentKey="serviceNetApp.conflict.offeredValue">Offered Value</Translate>
                  </Label>
                  <AvField id="conflict-offeredValue" type="text" name="offeredValue" />
                </AvGroup>
                <AvGroup>
                  <Label id="offeredValueDateLabel" for="offeredValueDate">
                    <Translate contentKey="serviceNetApp.conflict.offeredValueDate">Offered Value Date</Translate>
                  </Label>
                  <AvInput
                    id="conflict-offeredValueDate"
                    type="datetime-local"
                    className="form-control"
                    name="offeredValueDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.conflictEntity.offeredValueDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="fieldNameLabel" for="fieldName">
                    <Translate contentKey="serviceNetApp.conflict.fieldName">Field Name</Translate>
                  </Label>
                  <AvField id="conflict-fieldName" type="text" name="fieldName" />
                </AvGroup>
                <AvGroup>
                  <Label id="entityPathLabel" for="entityPath">
                    <Translate contentKey="serviceNetApp.conflict.entityPath">Entity Path</Translate>
                  </Label>
                  <AvField id="conflict-entityPath" type="text" name="entityPath" />
                </AvGroup>
                <AvGroup>
                  <Label id="stateLabel">
                    <Translate contentKey="serviceNetApp.conflict.state">State</Translate>
                  </Label>
                  <AvInput
                    id="conflict-state"
                    type="select"
                    className="form-control"
                    name="state"
                    value={(!isNew && conflictEntity.state) || 'PENDING'}
                  >
                    <option value="PENDING">
                      <Translate contentKey="serviceNetApp.ConflictStateEnum.PENDING" />
                    </option>
                    <option value="ACCEPTED">
                      <Translate contentKey="serviceNetApp.ConflictStateEnum.ACCEPTED" />
                    </option>
                    <option value="REJECTED">
                      <Translate contentKey="serviceNetApp.ConflictStateEnum.REJECTED" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="stateDateLabel" for="stateDate">
                    <Translate contentKey="serviceNetApp.conflict.stateDate">State Date</Translate>
                  </Label>
                  <AvInput
                    id="conflict-stateDate"
                    type="datetime-local"
                    className="form-control"
                    name="stateDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.conflictEntity.stateDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="createdDateLabel" for="createdDate">
                    <Translate contentKey="serviceNetApp.conflict.createdDate">Created Date</Translate>
                  </Label>
                  <AvInput
                    id="conflict-createdDate"
                    type="datetime-local"
                    className="form-control"
                    name="createdDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.conflictEntity.createdDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="resourceIdLabel" for="resourceId">
                    <Translate contentKey="serviceNetApp.conflict.resourceId">Resource Id</Translate>
                  </Label>
                  <AvField id="conflict-resourceId" type="text" name="resourceId" />
                </AvGroup>
                <AvGroup>
                  <Label for="owner.id">
                    <Translate contentKey="serviceNetApp.conflict.owner">Owner</Translate>
                  </Label>
                  <AvInput id="conflict-owner" type="select" className="form-control" name="ownerId">
                    {systemAccounts
                      ? systemAccounts.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="systemAccounts">
                    <Translate contentKey="serviceNetApp.conflict.acceptedThisChange">Accepted This Change</Translate>
                  </Label>
                  <AvInput
                    id="conflict-acceptedThisChange"
                    type="select"
                    multiple
                    className="form-control"
                    name="acceptedThisChanges"
                    value={conflictEntity.acceptedThisChanges && conflictEntity.acceptedThisChanges.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {systemAccounts
                      ? systemAccounts.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/conflict" replace color="info">
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
  systemAccounts: storeState.systemAccount.entities,
  conflictEntity: storeState.conflict.entity,
  loading: storeState.conflict.loading,
  updating: storeState.conflict.updating,
  updateSuccess: storeState.conflict.updateSuccess
});

const mapDispatchToProps = {
  getSystemAccounts,
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
)(ConflictUpdate);
