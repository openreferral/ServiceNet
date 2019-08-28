import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './activity-filter.reducer';
import { IActivityFilter } from 'app/shared/model/activity-filter.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IActivityFilterDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ActivityFilterDetail extends React.Component<IActivityFilterDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { activityFilterEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.activityFilter.detail.title">ActivityFilter</Translate> [<b>{activityFilterEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="serviceNetApp.activityFilter.name">Name</Translate>
              </span>
            </dt>
            <dd>{activityFilterEntity.name}</dd>
            <dt>
              <span id="citiesFilterList">
                <Translate contentKey="serviceNetApp.activityFilter.citiesFilterList">Cities Filter List</Translate>
              </span>
            </dt>
            <dd>{activityFilterEntity.citiesFilterList ? activityFilterEntity.citiesFilterList.join(', ') : ''}</dd>
            <dt>
              <span id="regionFilterList">
                <Translate contentKey="serviceNetApp.activityFilter.regionFilterList">Region Filter List</Translate>
              </span>
            </dt>
            <dd>{activityFilterEntity.regionFilterList ? activityFilterEntity.regionFilterList.join(', ') : ''}</dd>
            <dt>
              <span id="postalCodesFilterList">
                <Translate contentKey="serviceNetApp.activityFilter.postalCodesFilterList">Postal Codes Filter List</Translate>
              </span>
            </dt>
            <dd>{activityFilterEntity.postalCodesFilterList ? activityFilterEntity.postalCodesFilterList.join(', ') : ''}</dd>
            <dt>
              <span id="taxonomiesFilterList">
                <Translate contentKey="serviceNetApp.activityFilter.taxonomiesFilterList">Taxonomies Filter List</Translate>
              </span>
            </dt>
            <dd>{activityFilterEntity.taxonomiesFilterList ? activityFilterEntity.taxonomiesFilterList.join(', ') : ''}</dd>
            <dt>
              <span id="searchOn">
                <Translate contentKey="serviceNetApp.activityFilter.searchFor">Search For</Translate>
              </span>
            </dt>
            <dd>{activityFilterEntity.searchOn}</dd>
            <dt>
              <span id="searchFields">
                <Translate contentKey="serviceNetApp.activityFilter.searchFields">Search Fields</Translate>
              </span>
            </dt>
            <dd>{activityFilterEntity.searchFields ? activityFilterEntity.searchFields.join(', ') : ''}</dd>
            <dt>
              <span id="partnerFilterList">
                <Translate contentKey="serviceNetApp.activityFilter.partnerFilterList">Partner Filter List</Translate>
              </span>
            </dt>
            <dd>{activityFilterEntity.partnerFilterList ? activityFilterEntity.partnerFilterList.join(', ') : ''}</dd>
            <dt>
              <span id="dateFilter">
                <Translate contentKey="serviceNetApp.activityFilter.dateFilter">Date Filter</Translate>
              </span>
            </dt>
            <dd>{activityFilterEntity.dateFilter}</dd>
            <dt>
              <span id="fromDate">
                <Translate contentKey="serviceNetApp.activityFilter.fromDate">From Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={activityFilterEntity.fromDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="toDate">
                <Translate contentKey="serviceNetApp.activityFilter.toDate">To Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={activityFilterEntity.toDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="hiddenFilter">
                <Translate contentKey="serviceNetApp.activityFilter.hiddenFilter">Hidden Filter</Translate>
              </span>
            </dt>
            <dd>{activityFilterEntity.hiddenFilter ? 'true' : 'false'}</dd>
            <dt>
              <span id="showPartner">
                <Translate contentKey="serviceNetApp.activityFilter.showPartner">Show Partner</Translate>
              </span>
            </dt>
            <dd>{activityFilterEntity.showPartner ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.activityFilter.user">User</Translate>
            </dt>
            <dd>{activityFilterEntity.userLogin ? activityFilterEntity.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/activity-filter" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/activity-filter/${activityFilterEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ activityFilter }: IRootState) => ({
  activityFilterEntity: activityFilter.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ActivityFilterDetail);
