import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISystemAccount } from 'app/shared/model/system-account.model';
import { getEntities as getSystemAccounts } from 'app/entities/system-account/system-account.reducer';
import { getEntity, updateEntity, createEntity, reset } from './exclusions-config.reducer';
import { IExclusionsConfig } from 'app/shared/model/exclusions-config.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IExclusionsConfigUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IExclusionsConfigUpdateState {
  isNew: boolean;
  accountId: string;
}

export class ExclusionsConfigUpdate extends React.Component<IExclusionsConfigUpdateProps, IExclusionsConfigUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      accountId: '0',
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
    if (errors.length === 0) {
      const { exclusionsConfigEntity } = this.props;
      const entity = {
        ...exclusionsConfigEntity,
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
    this.props.history.push('/entity/exclusions-config');
  };

  render() {
    const { exclusionsConfigEntity, systemAccounts, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.exclusionsConfig.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.exclusionsConfig.home.createOrEditLabel">Create or edit a ExclusionsConfig</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : exclusionsConfigEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="exclusions-config-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label for="account.id">
                    <Translate contentKey="serviceNetApp.exclusionsConfig.account">Account</Translate>
                  </Label>
                  <AvInput id="exclusions-config-account" type="select" className="form-control" name="accountId">
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
                <Button tag={Link} id="cancel-save" to="/entity/exclusions-config" replace color="info">
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
  exclusionsConfigEntity: storeState.exclusionsConfig.entity,
  loading: storeState.exclusionsConfig.loading,
  updating: storeState.exclusionsConfig.updating,
  updateSuccess: storeState.exclusionsConfig.updateSuccess
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
)(ExclusionsConfigUpdate);
