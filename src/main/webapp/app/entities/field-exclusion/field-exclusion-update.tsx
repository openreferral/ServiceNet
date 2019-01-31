import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, getConfigs, updateEntity, createEntity, reset } from './field-exclusion.reducer';
import { IFieldExclusion } from 'app/shared/model/field-exclusion.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFieldExclusionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IFieldExclusionUpdateState {
  isNew: boolean;
}

export class FieldExclusionUpdate extends React.Component<IFieldExclusionUpdateProps, IFieldExclusionUpdateState> {
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
    this.props.getConfigs();
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { fieldExclusionEntity } = this.props;
      const entity = {
        ...fieldExclusionEntity,
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
    this.props.history.push('/entity/field-exclusion');
  };

  render() {
    const { fieldExclusionEntity, loading, updating, configs } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.fieldExclusion.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.fieldExclusion.home.createOrEditLabel">Create or edit a FieldExclusion</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : fieldExclusionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="field-exclusion-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="fieldsLabel" for="fields">
                    <Translate contentKey="serviceNetApp.fieldExclusion.fields">Fields</Translate>
                  </Label>
                  <AvField id="field-exclusion-fields" type="text" name="fields" />
                </AvGroup>
                <AvGroup>
                  <Label id="entityLabel" for="entity">
                    <Translate contentKey="serviceNetApp.fieldExclusion.entity">Entity</Translate>
                  </Label>
                  <AvField id="field-exclusion-entity" type="text" name="entity" />
                </AvGroup>
                <AvGroup>
                  <Label for="configId">
                    <Translate contentKey="serviceNetApp.fieldExclusion.config" />
                  </Label>
                  <AvInput id="field-exclusion-config" type="select" className="form-control" name="configId">
                    <option value="" key="0" />
                    {configs
                      ? configs.map(config => (
                          <option value={config.id} key={config.id}>
                            {config.accountName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/field-exclusion" replace color="info">
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
  fieldExclusionEntity: storeState.fieldExclusion.entity,
  configs: storeState.fieldExclusion.configs,
  loading: storeState.fieldExclusion.loading,
  updating: storeState.fieldExclusion.updating,
  updateSuccess: storeState.fieldExclusion.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  getConfigs,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FieldExclusionUpdate);
