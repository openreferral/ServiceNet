import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ITaxonomy } from 'app/shared/model/taxonomy.model';
import { getEntities as getTaxonomies } from 'app/entities/taxonomy/taxonomy.reducer';
import { getEntity, updateEntity, createEntity, reset } from './taxonomy-group.reducer';
import { ITaxonomyGroup } from 'app/shared/model/taxonomy-group.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITaxonomyGroupUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ITaxonomyGroupUpdateState {
  isNew: boolean;
  idstaxonomies: any[];
}

export class TaxonomyGroupUpdate extends React.Component<ITaxonomyGroupUpdateProps, ITaxonomyGroupUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idstaxonomies: [],
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

    this.props.getTaxonomies(0, 100000, 'taxonomyId');
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { taxonomyGroupEntity } = this.props;
      const entity = {
        ...taxonomyGroupEntity,
        ...values,
        taxonomies: mapIdList(values.taxonomies)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/taxonomy-group');
  };

  render() {
    const { taxonomyGroupEntity, taxonomies, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.taxonomyGroup.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.taxonomyGroup.home.createOrEditLabel">Create or edit a TaxonomyGroup</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : taxonomyGroupEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="taxonomy-group-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="taxonomy-group-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label for="taxonomy-group-taxonomies">
                    <Translate contentKey="serviceNetApp.taxonomyGroup.taxonomies">Taxonomies</Translate>
                  </Label>
                  <AvInput
                    id="taxonomy-group-taxonomies"
                    type="select"
                    multiple
                    className="form-control"
                    name="taxonomies"
                    value={taxonomyGroupEntity.taxonomies && taxonomyGroupEntity.taxonomies.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {taxonomies
                      ? taxonomies.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name ? otherEntity.name : otherEntity.taxonomyId}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/taxonomy-group" replace color="info">
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
  taxonomies: storeState.taxonomy.entities,
  taxonomyGroupEntity: storeState.taxonomyGroup.entity,
  loading: storeState.taxonomyGroup.loading,
  updating: storeState.taxonomyGroup.updating,
  updateSuccess: storeState.taxonomyGroup.updateSuccess
});

const mapDispatchToProps = {
  getTaxonomies,
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
)(TaxonomyGroupUpdate);
