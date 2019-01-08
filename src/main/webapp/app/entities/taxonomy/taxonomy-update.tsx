import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntities as getTaxonomies } from 'app/entities/taxonomy/taxonomy.reducer';
import { getEntity, updateEntity, createEntity, reset } from './taxonomy.reducer';
import { ITaxonomy } from 'app/shared/model/taxonomy.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITaxonomyUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ITaxonomyUpdateState {
  isNew: boolean;
  parentId: string;
}

export class TaxonomyUpdate extends React.Component<ITaxonomyUpdateProps, ITaxonomyUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      parentId: '0',
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

    this.props.getTaxonomies();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { taxonomyEntity } = this.props;
      const entity = {
        ...taxonomyEntity,
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
    this.props.history.push('/entity/taxonomy');
  };

  render() {
    const { taxonomyEntity, taxonomies, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.taxonomy.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.taxonomy.home.createOrEditLabel">Create or edit a Taxonomy</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : taxonomyEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="taxonomy-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="serviceNetApp.taxonomy.name">Name</Translate>
                  </Label>
                  <AvField id="taxonomy-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="vocabularyLabel" for="vocabulary">
                    <Translate contentKey="serviceNetApp.taxonomy.vocabulary">Vocabulary</Translate>
                  </Label>
                  <AvField id="taxonomy-vocabulary" type="text" name="vocabulary" />
                </AvGroup>
                <AvGroup>
                  <Label for="parent.name">
                    <Translate contentKey="serviceNetApp.taxonomy.parent">Parent</Translate>
                  </Label>
                  <AvInput id="taxonomy-parent" type="select" className="form-control" name="parentId">
                    <option value="" key="0" />
                    {taxonomies
                      ? taxonomies.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="externalDbIdLabel" for="externalDbId">
                    <Translate contentKey="serviceNetApp.organization.externalDbId" />
                  </Label>
                  <AvInput id="organization-externalDbId" type="textarea" name="externalDbId" />
                </AvGroup>
                <AvGroup>
                  <Label id="providerNameLabel" for="providerName">
                    <Translate contentKey="serviceNetApp.organization.providerName" />
                  </Label>
                  <AvInput id="organization-providerName" type="textarea" name="providerName" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/taxonomy" replace color="info">
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
  taxonomyEntity: storeState.taxonomy.entity,
  loading: storeState.taxonomy.loading,
  updating: storeState.taxonomy.updating,
  updateSuccess: storeState.taxonomy.updateSuccess
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
)(TaxonomyUpdate);
