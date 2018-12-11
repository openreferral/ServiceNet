import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRegularSchedule } from 'app/shared/model/regular-schedule.model';
import { getEntities as getRegularSchedules } from 'app/entities/regular-schedule/regular-schedule.reducer';
import { getEntity, updateEntity, createEntity, reset } from './opening-hours.reducer';
import { IOpeningHours } from 'app/shared/model/opening-hours.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOpeningHoursUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOpeningHoursUpdateState {
  isNew: boolean;
  regularScheduleId: string;
}

export class OpeningHoursUpdate extends React.Component<IOpeningHoursUpdateProps, IOpeningHoursUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      regularScheduleId: '0',
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

    this.props.getRegularSchedules();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { openingHoursEntity } = this.props;
      const entity = {
        ...openingHoursEntity,
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
    this.props.history.push('/entity/opening-hours');
  };

  render() {
    const { openingHoursEntity, regularSchedules, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.openingHours.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.openingHours.home.createOrEditLabel">Create or edit a OpeningHours</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : openingHoursEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="opening-hours-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="weekdayLabel" for="weekday">
                    <Translate contentKey="serviceNetApp.openingHours.weekday">Weekday</Translate>
                  </Label>
                  <AvField
                    id="opening-hours-weekday"
                    type="string"
                    className="form-control"
                    name="weekday"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="opensAtLabel" for="opensAt">
                    <Translate contentKey="serviceNetApp.openingHours.opensAt">Opens At</Translate>
                  </Label>
                  <AvField id="opening-hours-opensAt" type="text" name="opensAt" />
                </AvGroup>
                <AvGroup>
                  <Label id="closesAtLabel" for="closesAt">
                    <Translate contentKey="serviceNetApp.openingHours.closesAt">Closes At</Translate>
                  </Label>
                  <AvField id="opening-hours-closesAt" type="text" name="closesAt" />
                </AvGroup>
                <AvGroup>
                  <Label for="regularSchedule.id">
                    <Translate contentKey="serviceNetApp.openingHours.regularSchedule">Regular Schedule</Translate>
                  </Label>
                  <AvInput id="opening-hours-regularSchedule" type="select" className="form-control" name="regularScheduleId">
                    <option value="" key="0" />
                    {regularSchedules
                      ? regularSchedules.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/opening-hours" replace color="info">
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
  regularSchedules: storeState.regularSchedule.entities,
  openingHoursEntity: storeState.openingHours.entity,
  loading: storeState.openingHours.loading,
  updating: storeState.openingHours.updating,
  updateSuccess: storeState.openingHours.updateSuccess
});

const mapDispatchToProps = {
  getRegularSchedules,
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
)(OpeningHoursUpdate);
