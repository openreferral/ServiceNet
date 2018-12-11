import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './activity.reducer';
import { IActivity } from 'app/shared/model/activity.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IActivityDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ActivityDetail extends React.Component<IActivityDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { activityEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.activity.detail.title">Activity</Translate> [<b>{activityEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <Translate contentKey="serviceNetApp.activity.user">User</Translate>
            </dt>
            <dd>{activityEntity.userLogin ? activityEntity.userLogin : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.activity.reviewers">Reviewers</Translate>
            </dt>
            <dd>
              {activityEntity.reviewers
                ? activityEntity.reviewers.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.login}</a>
                      {i === activityEntity.reviewers.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}{' '}
            </dd>
            <dt>
              <Translate contentKey="serviceNetApp.activity.metadata">Metadata</Translate>
            </dt>
            <dd>{activityEntity.metadataId ? activityEntity.metadataId : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.activity.organization">Organization</Translate>
            </dt>
            <dd>{activityEntity.organizationId ? activityEntity.organizationId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/activity" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/activity/${activityEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ activity }: IRootState) => ({
  activityEntity: activity.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ActivityDetail);
