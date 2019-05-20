import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './conflict.reducer';
import { IConflict } from 'app/shared/model/conflict.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IConflictDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ConflictDetail extends React.Component<IConflictDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { conflictEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.conflict.detail.title">Conflict</Translate> [<b>{conflictEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="currentValue">
                <Translate contentKey="serviceNetApp.conflict.currentValue">Current Value</Translate>
              </span>
            </dt>
            <dd>{conflictEntity.currentValue}</dd>
            <dt>
              <span id="currentValueDate">
                <Translate contentKey="serviceNetApp.conflict.currentValueDate">Current Value Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={conflictEntity.currentValueDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="offeredValue">
                <Translate contentKey="serviceNetApp.conflict.offeredValue">Offered Value</Translate>
              </span>
            </dt>
            <dd>{conflictEntity.offeredValue}</dd>
            <dt>
              <span id="offeredValueDate">
                <Translate contentKey="serviceNetApp.conflict.offeredValueDate">Offered Value Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={conflictEntity.offeredValueDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="fieldName">
                <Translate contentKey="serviceNetApp.conflict.fieldName">Field Name</Translate>
              </span>
            </dt>
            <dd>{conflictEntity.fieldName}</dd>
            <dt>
              <span id="entityPath">
                <Translate contentKey="serviceNetApp.conflict.entityPath">Entity Path</Translate>
              </span>
            </dt>
            <dd>{conflictEntity.entityPath}</dd>
            <dt>
              <span id="state">
                <Translate contentKey="serviceNetApp.conflict.state">State</Translate>
              </span>
            </dt>
            <dd>{conflictEntity.state}</dd>
            <dt>
              <span id="stateDate">
                <Translate contentKey="serviceNetApp.conflict.stateDate">State Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={conflictEntity.stateDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="createdDate">
                <Translate contentKey="serviceNetApp.conflict.createdDate">Created Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={conflictEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="resourceId">
                <Translate contentKey="serviceNetApp.conflict.resourceId">Resource Id</Translate>
              </span>
            </dt>
            <dd>{conflictEntity.resourceId}</dd>
            <dt>
              <span id="partnerResourceId">
                <Translate contentKey="serviceNetApp.conflict.partnerResourceId">Partner Resource Id</Translate>
              </span>
            </dt>
            <dd>{conflictEntity.partnerResourceId}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.conflict.owner">Owner</Translate>
            </dt>
            <dd>{conflictEntity.ownerId ? conflictEntity.ownerId : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.conflict.partner">Partner</Translate>
            </dt>
            <dd>{conflictEntity.partnerId ? conflictEntity.partnerId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/conflict" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/conflict/${conflictEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ conflict }: IRootState) => ({
  conflictEntity: conflict.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ConflictDetail);
