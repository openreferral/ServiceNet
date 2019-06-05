import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './option.reducer';
import { IOption } from 'app/shared/model/option.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOptionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOptionUpdateState {
  isNew: boolean;
}

export class OptionUpdate extends React.Component<IOptionUpdateProps, IOptionUpdateState> {
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
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { optionEntity } = this.props;
      const entity = {
        ...optionEntity,
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
    this.props.history.push('/entity/option');
  };

  render() {
    const { optionEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.option.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.option.home.createOrEditLabel">Create or edit a Option</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : optionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="option-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="typeLabel">
                    <Translate contentKey="serviceNetApp.option.type">Type</Translate>
                  </Label>
                  <AvInput
                    id="option-type"
                    type="select"
                    className="form-control"
                    name="type"
                    value={(!isNew && optionEntity.type) || 'LANGUAGE'}
                  >
                    <option value="LANGUAGE">
                      <Translate contentKey="serviceNetApp.OptionType.LANGUAGE" />
                    </option>
                    <option value="DEFINED_COVERAGE_AREA">
                      <Translate contentKey="serviceNetApp.OptionType.DEFINED_COVERAGE_AREA" />
                    </option>
                    <option value="TAG">
                      <Translate contentKey="serviceNetApp.OptionType.TAG" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="valueLabel" for="value">
                    <Translate contentKey="serviceNetApp.option.value">Value</Translate>
                  </Label>
                  <AvField id="option-value" type="string" className="form-control" name="value" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/option" replace color="info">
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
  optionEntity: storeState.option.entity,
  loading: storeState.option.loading,
  updating: storeState.option.updating,
  updateSuccess: storeState.option.updateSuccess
});

const mapDispatchToProps = {
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
)(OptionUpdate);
