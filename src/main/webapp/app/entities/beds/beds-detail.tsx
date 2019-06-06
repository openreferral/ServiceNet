import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './beds.reducer';
import { IBeds } from 'app/shared/model/beds.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBedsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class BedsDetail extends React.Component<IBedsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { bedsEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.beds.detail.title">Beds</Translate> [<b>{bedsEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="availableBeds">
                <Translate contentKey="serviceNetApp.beds.availableBeds">Available Beds</Translate>
              </span>
            </dt>
            <dd>{bedsEntity.availableBeds}</dd>
            <dt>
              <span id="waitlist">
                <Translate contentKey="serviceNetApp.beds.waitlist">Waitlist</Translate>
              </span>
            </dt>
            <dd>{bedsEntity.waitlist}</dd>
            <dt>
              <span id="updatedAt">
                <Translate contentKey="serviceNetApp.beds.updatedAt">Updated At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={bedsEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="serviceNetApp.beds.shelter">Shelter</Translate>
            </dt>
            <dd>{bedsEntity.shelter ? bedsEntity.shelter.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/beds" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/beds/${bedsEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ beds }: IRootState) => ({
  bedsEntity: beds.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(BedsDetail);
