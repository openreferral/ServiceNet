import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './taxonomy.reducer';
import { ITaxonomy } from 'app/shared/model/taxonomy.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITaxonomyDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TaxonomyDetail extends React.Component<ITaxonomyDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { taxonomyEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.taxonomy.detail.title">Taxonomy</Translate> [<b>{taxonomyEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="serviceNetApp.taxonomy.name">Name</Translate>
              </span>
            </dt>
            <dd>{taxonomyEntity.name}</dd>
            <dt>
              <span id="vocabulary">
                <Translate contentKey="serviceNetApp.taxonomy.vocabulary">Vocabulary</Translate>
              </span>
            </dt>
            <dd>{taxonomyEntity.vocabulary}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.taxonomy.parent">Parent</Translate>
            </dt>
            <dd>{taxonomyEntity.parentName ? taxonomyEntity.parentName : ''}</dd>
            <dt>
              <span id="externalDbId">
                <Translate contentKey="serviceNetApp.taxonomyEntity.externalDbId" />
              </span>
            </dt>
            <dd>{taxonomyEntity.externalDbId}</dd>
            <dt>
              <span id="providerName">
                <Translate contentKey="serviceNetApp.taxonomyEntity.providerName" />
              </span>
            </dt>
            <dd>{taxonomyEntity.providerName}</dd>
          </dl>
          <Button tag={Link} to="/entity/taxonomy" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/taxonomy/${taxonomyEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ taxonomy }: IRootState) => ({
  taxonomyEntity: taxonomy.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TaxonomyDetail);
