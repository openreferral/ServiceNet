import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './eligibility.reducer';
import { IEligibility } from 'app/shared/model/eligibility.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEligibilityDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class EligibilityDetail extends React.Component<IEligibilityDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { eligibilityEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.eligibility.detail.title">Eligibility</Translate> [<b>{eligibilityEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="eligibility">
                <Translate contentKey="serviceNetApp.eligibility.eligibility">Eligibility</Translate>
              </span>
            </dt>
            <dd>{eligibilityEntity.eligibility}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.eligibility.srvc">Srvc</Translate>
            </dt>
            <dd>{eligibilityEntity.srvcName ? eligibilityEntity.srvcName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/eligibility" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/eligibility/${eligibilityEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ eligibility }: IRootState) => ({
  eligibilityEntity: eligibility.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EligibilityDetail);
