import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './service-taxonomies-details-fields-value.reducer';
import { IServiceTaxonomiesDetailsFieldsValue } from 'app/shared/model/service-taxonomies-details-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServiceTaxonomiesDetailsFieldsValueDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ServiceTaxonomiesDetailsFieldsValueDetail extends React.Component<IServiceTaxonomiesDetailsFieldsValueDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { serviceTaxonomiesDetailsFieldsValueEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.serviceTaxonomiesDetailsFieldsValue.detail.title">
              ServiceTaxonomiesDetailsFieldsValue
            </Translate>{' '}
            [<b>{serviceTaxonomiesDetailsFieldsValueEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="serviceTaxonomiesDetailsField">
                <Translate contentKey="serviceNetApp.serviceTaxonomiesDetailsFieldsValue.serviceTaxonomiesDetailsField">
                  Service Taxonomies Details Field
                </Translate>
              </span>
            </dt>
            <dd>{serviceTaxonomiesDetailsFieldsValueEntity.serviceTaxonomiesDetailsField}</dd>
          </dl>
          <Button tag={Link} to="/entity/service-taxonomies-details-fields-value" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button
            tag={Link}
            to={`/entity/service-taxonomies-details-fields-value/${serviceTaxonomiesDetailsFieldsValueEntity.id}/edit`}
            replace
            color="primary"
          >
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

const mapStateToProps = ({ serviceTaxonomiesDetailsFieldsValue }: IRootState) => ({
  serviceTaxonomiesDetailsFieldsValueEntity: serviceTaxonomiesDetailsFieldsValue.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceTaxonomiesDetailsFieldsValueDetail);
