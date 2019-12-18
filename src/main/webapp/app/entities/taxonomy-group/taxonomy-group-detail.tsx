import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './taxonomy-group.reducer';
import { ITaxonomyGroup } from 'app/shared/model/taxonomy-group.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITaxonomyGroupDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TaxonomyGroupDetail extends React.Component<ITaxonomyGroupDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { taxonomyGroupEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.taxonomyGroup.detail.title">TaxonomyGroup</Translate> [<b>{taxonomyGroupEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <Translate contentKey="serviceNetApp.taxonomyGroup.taxonomies">Taxonomies</Translate>
            </dt>
            <dd>
              {taxonomyGroupEntity.taxonomies
                ? taxonomyGroupEntity.taxonomies.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.name ? val.name : val.taxonomyId}</a>
                      {i === taxonomyGroupEntity.taxonomies.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/taxonomy-group" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/taxonomy-group/${taxonomyGroupEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ taxonomyGroup }: IRootState) => ({
  taxonomyGroupEntity: taxonomyGroup.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TaxonomyGroupDetail);
