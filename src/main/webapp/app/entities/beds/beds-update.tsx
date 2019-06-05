import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IShelter } from 'app/shared/model/shelter.model';
import { getEntities as getShelters } from 'app/entities/shelter/shelter.reducer';
import { getEntity, updateEntity, createEntity, reset } from './beds.reducer';
import { IBeds } from 'app/shared/model/beds.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBedsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IBedsUpdateState {
  isNew: boolean;
  shelterId: string;
}

export class BedsUpdate extends React.Component<IBedsUpdateProps, IBedsUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      shelterId: '0',
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

    this.props.getShelters();
  }

  saveEntity = (event, errors, values) => {
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    if (errors.length === 0) {
      const { bedsEntity } = this.props;
      const entity = {
        ...bedsEntity,
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
    this.props.history.push('/entity/beds');
  };

  render() {
    const { bedsEntity, shelters, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.beds.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.beds.home.createOrEditLabel">Create or edit a Beds</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : bedsEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="beds-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="availableBedsLabel" for="availableBeds">
                    <Translate contentKey="serviceNetApp.beds.availableBeds">Available Beds</Translate>
                  </Label>
                  <AvField id="beds-availableBeds" type="string" className="form-control" name="availableBeds" />
                </AvGroup>
                <AvGroup>
                  <Label id="waitlistLabel" for="waitlist">
                    <Translate contentKey="serviceNetApp.beds.waitlist">Waitlist</Translate>
                  </Label>
                  <AvField id="beds-waitlist" type="string" className="form-control" name="waitlist" />
                </AvGroup>
                <AvGroup>
                  <Label id="updatedAtLabel" for="updatedAt">
                    <Translate contentKey="serviceNetApp.beds.updatedAt">Updated At</Translate>
                  </Label>
                  <AvInput
                    id="beds-updatedAt"
                    type="datetime-local"
                    className="form-control"
                    name="updatedAt"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.bedsEntity.updatedAt)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="shelter.id">
                    <Translate contentKey="serviceNetApp.beds.shelter">Shelter</Translate>
                  </Label>
                  <AvInput id="beds-shelter" type="select" className="form-control" name="shelter.id">
                    <option value="" key="0" />
                    {shelters
                      ? shelters.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/beds" replace color="info">
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
  shelters: storeState.shelter.entities,
  bedsEntity: storeState.beds.entity,
  loading: storeState.beds.loading,
  updating: storeState.beds.updating,
  updateSuccess: storeState.beds.updateSuccess
});

const mapDispatchToProps = {
  getShelters,
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
)(BedsUpdate);
