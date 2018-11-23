import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './funding.reducer';
import { IFunding } from 'app/shared/model/funding.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFundingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FundingDetail extends React.Component<IFundingDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { fundingEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.funding.detail.title">Funding</Translate> [<b>{fundingEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="source">
                <Translate contentKey="serviceNetApp.funding.source">Source</Translate>
              </span>
            </dt>
            <dd>{fundingEntity.source}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.funding.organization">Organization</Translate>
            </dt>
            <dd>{fundingEntity.organizationName ? fundingEntity.organizationName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.funding.srvc">Srvc</Translate>
            </dt>
            <dd>{fundingEntity.srvcName ? fundingEntity.srvcName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/funding" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/funding/${fundingEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ funding }: IRootState) => ({
  fundingEntity: funding.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FundingDetail);
