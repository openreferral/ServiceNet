import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './service-taxonomy.reducer';
import { IServiceTaxonomy } from 'app/shared/model/service-taxonomy.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServiceTaxonomyDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ServiceTaxonomyDetail extends React.Component<IServiceTaxonomyDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { serviceTaxonomyEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.serviceTaxonomy.detail.title">ServiceTaxonomy</Translate> [
            <b>{serviceTaxonomyEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="taxonomyDetails">
                <Translate contentKey="serviceNetApp.serviceTaxonomy.taxonomyDetails">Taxonomy Details</Translate>
              </span>
            </dt>
            <dd>{serviceTaxonomyEntity.taxonomyDetails}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.serviceTaxonomy.srvc">Srvc</Translate>
            </dt>
            <dd>{serviceTaxonomyEntity.srvcName ? serviceTaxonomyEntity.srvcName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.serviceTaxonomy.taxonomy">Taxonomy</Translate>
            </dt>
            <dd>{serviceTaxonomyEntity.taxonomyName ? serviceTaxonomyEntity.taxonomyName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/service-taxonomy" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/service-taxonomy/${serviceTaxonomyEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ serviceTaxonomy }: IRootState) => ({
  serviceTaxonomyEntity: serviceTaxonomy.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceTaxonomyDetail);
