import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './activity-filter.reducer';
import { IActivityFilter } from 'app/shared/model/activity-filter.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { getSearchFieldOptions } from 'app/modules/home/filter.constants';

export interface IActivityFilterUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IActivityFilterUpdateState {
  isNew: boolean;
  userId: string;
}

export class ActivityFilterUpdate extends React.Component<IActivityFilterUpdateProps, IActivityFilterUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
  }

  mapValuesToString = entity => {
    const { citiesFilterList, regionFilterList, postalCodesFilterList, taxonomiesFilterList, partnerFilterList } = entity;
    return {
      ...entity,
      citiesFilterList: !citiesFilterList ? '' : citiesFilterList.join(', '),
      regionFilterList: !regionFilterList ? '' : regionFilterList.join(', '),
      postalCodesFilterList: !postalCodesFilterList ? '' : postalCodesFilterList.join(', '),
      taxonomiesFilterList: !taxonomiesFilterList ? '' : taxonomiesFilterList.join(', '),
      partnerFilterList: !partnerFilterList ? '' : partnerFilterList.join(', ')
    };
  };

  mapStringToList = value => (!value ? [] : value.split(',').map(val => val.trim()));

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { activityFilterEntity } = this.props;
      const { citiesFilterList, regionFilterList, postalCodesFilterList, taxonomiesFilterList, partnerFilterList } = values;
      const entity = {
        ...activityFilterEntity,
        ...values,
        citiesFilterList: this.mapStringToList(citiesFilterList),
        regionFilterList: this.mapStringToList(regionFilterList),
        postalCodesFilterList: this.mapStringToList(postalCodesFilterList),
        taxonomiesFilterList: this.mapStringToList(taxonomiesFilterList),
        partnerFilterList: this.mapStringToList(partnerFilterList)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/activity-filter');
  };

  render() {
    const { activityFilterEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;
    const options = getSearchFieldOptions(activityFilterEntity.searchOn || 'ORGANIZATION');
    const entity = this.mapValuesToString(activityFilterEntity);

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.activityFilter.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.activityFilter.home.createOrEditLabel">Create or edit a ActivityFilter</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : entity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="activity-filter-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="activity-filter-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="activity-filter-name">
                    <Translate contentKey="serviceNetApp.activityFilter.name">Name</Translate>
                  </Label>
                  <AvField id="activity-filter-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="citiesFilterListLabel" for="activity-filter-citiesFilterList">
                    <Translate contentKey="serviceNetApp.activityFilter.citiesFilterList">Cities Filter List</Translate>
                  </Label>
                  <AvField id="activity-filter-citiesFilterList" type="text" name="citiesFilterList" />
                </AvGroup>
                <AvGroup>
                  <Label id="regionFilterListLabel" for="activity-filter-regionFilterList">
                    <Translate contentKey="serviceNetApp.activityFilter.regionFilterList">Region Filter List</Translate>
                  </Label>
                  <AvField id="activity-filter-regionFilterList" type="text" name="regionFilterList" />
                </AvGroup>
                <AvGroup>
                  <Label id="postalCodesFilterListLabel" for="activity-filter-postalCodesFilterList">
                    <Translate contentKey="serviceNetApp.activityFilter.postalCodesFilterList">Postal Codes Filter List</Translate>
                  </Label>
                  <AvField id="activity-filter-postalCodesFilterList" type="text" name="postalCodesFilterList" />
                </AvGroup>
                <AvGroup>
                  <Label id="taxonomiesFilterListLabel" for="activity-filter-taxonomiesFilterList">
                    <Translate contentKey="serviceNetApp.activityFilter.taxonomiesFilterList">Taxonomies Filter List</Translate>
                  </Label>
                  <AvField id="activity-filter-taxonomiesFilterList" type="text" name="taxonomiesFilterList" />
                </AvGroup>
                <AvGroup>
                  <Label id="searchOnLabel" for="activity-filter-searchOn">
                    <Translate contentKey="serviceNetApp.activityFilter.searchOn">Search On</Translate>
                  </Label>
                  <AvInput
                    id="activity-filter-searchOn"
                    type="select"
                    className="form-control"
                    name="searchOn"
                    value={(!isNew && entity.searchOn) || 'ORGANIZATION'}
                  >
                    <option value="ORGANIZATION">{translate('serviceNetApp.SearchOn.ORGANIZATION')}</option>
                    <option value="SERVICES">{translate('serviceNetApp.SearchOn.SERVICES')}</option>
                    <option value="LOCATIONS">{translate('serviceNetApp.SearchOn.LOCATIONS')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="searchFieldsLabel" for="activity-filter-searchFields">
                    <Translate contentKey="serviceNetApp.activityFilter.searchFields">Search Fields</Translate>
                  </Label>
                  <AvInput id="activity-filter-searchFields" type="select" name="searchFields" multiple>
                    {options.map(field => (
                      <option value={field.value} key={field.value}>
                        {field.label}
                      </option>
                    ))}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="partnerFilterListLabel" for="activity-filter-partnerFilterList">
                    <Translate contentKey="serviceNetApp.activityFilter.partnerFilterList">Partner Filter List</Translate>
                  </Label>
                  <AvField id="activity-filter-partnerFilterList" type="text" name="partnerFilterList" />
                </AvGroup>
                <AvGroup>
                  <Label id="dateFilterLabel" for="activity-filter-dateFilter">
                    <Translate contentKey="serviceNetApp.activityFilter.dateFilter">Date Filter</Translate>
                  </Label>
                  <AvInput
                    id="activity-filter-dateFilter"
                    type="select"
                    className="form-control"
                    name="dateFilter"
                    value={(!isNew && entity.dateFilter) || 'LAST_7_DAYS'}
                  >
                    <option value="LAST_7_DAYS">{translate('serviceNetApp.DateFilter.LAST_7_DAYS')}</option>
                    <option value="LAST_30_DAYS">{translate('serviceNetApp.DateFilter.LAST_30_DAYS')}</option>
                    <option value="DATE_RANGE">{translate('serviceNetApp.DateFilter.DATE_RANGE')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="fromDateLabel" for="activity-filter-fromDate">
                    <Translate contentKey="serviceNetApp.activityFilter.fromDate">From Date</Translate>
                  </Label>
                  <AvField id="activity-filter-fromDate" type="date" className="form-control" name="fromDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="toDateLabel" for="activity-filter-toDate">
                    <Translate contentKey="serviceNetApp.activityFilter.toDate">To Date</Translate>
                  </Label>
                  <AvField id="activity-filter-toDate" type="date" className="form-control" name="toDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="hiddenFilterLabel" check>
                    <AvInput id="activity-filter-hiddenFilter" type="checkbox" className="form-control" name="hiddenFilter" />
                    <Translate contentKey="serviceNetApp.activityFilter.hiddenFilter">Hidden Filter</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="showPartnerLabel" check>
                    <AvInput id="activity-filter-showPartner" type="checkbox" className="form-control" name="showPartner" />
                    <Translate contentKey="serviceNetApp.activityFilter.showPartner">Show Partner</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="activity-filter-user">
                    <Translate contentKey="serviceNetApp.activityFilter.user">User</Translate>
                  </Label>
                  <AvInput id="activity-filter-user" type="select" className="form-control" name="userId">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/activity-filter" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  activityFilterEntity: storeState.activityFilter.entity,
  loading: storeState.activityFilter.loading,
  updating: storeState.activityFilter.updating,
  updateSuccess: storeState.activityFilter.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ActivityFilterUpdate);
