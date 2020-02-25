import React from 'react';
import { Col, Row, Button } from 'reactstrap';
import '../../shared-record-view.scss';
import { TextFormat, translate, Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { PhysicalAddressDetails } from './physical-address-details';
import { PostalAddressDetails } from './postal-address-details';
import { OpeningHoursDetails } from '../opening-hours-details';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { AdditionalDetails } from '../additional-details';
import { ILocationRecord } from 'app/shared/model/location-record.model';
import { LanguagesDetails } from '../languages-details';
import { HolidaySchedulesDetails } from '../holiday-schedules-details';
import { getTextField, getTextAreaField } from 'app/shared/util/single-record-view-utils';
import { APP_DATE_FORMAT } from 'app/config/constants';
import Select from 'react-select';
import _ from 'lodash';
import { createLocationMatch, deleteLocationMatch } from 'app/modules/conflicts/shared/shared-record-view.reducer';
import LocationMatchesDetails from 'app/modules/conflicts/shared/components/location/location-matches-details';

export interface ISingleLocationDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  record: ILocationRecord;
  locationsCount: string;
  changeRecord: any;
  isOnlyOne: boolean;
  columnSize: number;
  showClipboard: boolean;
  isAreaOpen: boolean;
  selectOptions: any;
  matchLocations?: boolean;
  toggleMatchLocations?: any;
  isBaseRecord: boolean;
  locationNumber?: number;
  settings?: any;
  locationMatches?: any;
  baseRecord?: any;
  orgId?: string;
}

export interface ISingleLocationDetailsState {
  isAreaOpen: boolean;
  id: string;
}

export class SingleLocationDetails extends React.Component<ISingleLocationDetailsProp, ISingleLocationDetailsState> {
  state: ISingleLocationDetailsState = {
    isAreaOpen: this.props.isAreaOpen,
    id: `location_${Math.random()
      .toString()
      .replace(/0\./, '')}`
  };

  static getSelectOption = (record, locationNumber) => ({
    value: locationNumber,
    label:
      record.physicalAddress !== null
        ? `${record.physicalAddress.address1} ${record.physicalAddress.city}`
        : translate('multiRecordView.noPhysicalAddress')
  });

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  changeRecord = record => {
    this.setState({ isAreaOpen: true });
    this.props.changeRecord(record.value);
  };

  getFields = fieldsMap => {
    const { settings } = this.props;

    if (settings === undefined || (!!settings && !settings.id)) {
      return _.values(fieldsMap);
    }

    const { locationFields } = settings;

    const fieldsMapKeys = _.keys(fieldsMap);
    const keysFiltered = _.filter(fieldsMapKeys, k => locationFields.indexOf(k) > -1);
    return _.values(_.pick(fieldsMap, keysFiltered));
  };

  matchLocation = () => {
    const matchingLocationId = this.props.record.location.id;
    const locationId = this.props.baseLocation;
    const orgId = this.props.baseRecord.organization.id;
    this.props.createLocationMatch(locationId, matchingLocationId, orgId);
  };

  unmatchLocation = () => {
    const matchingLocationId = this.props.record.location.id;
    const locationId = this.props.baseLocation;
    const orgId = this.props.baseRecord.organization.id;
    this.props.deleteLocationMatch(locationId, matchingLocationId, orgId);
  };

  isMatched = () => {
    const { locationMatches, baseLocation } = this.props;
    const matchingLocationId = this.props.record.location.id;
    if (!!locationMatches) {
      return _.some(
        _.reduce(locationMatches, (total, current) => total.concat(current), []),
        val => val.matchingLocation === matchingLocationId && val.location === baseLocation
      );
    }
    return true;
  };

  render() {
    const {
      record,
      isOnlyOne,
      columnSize,
      locationsCount,
      selectOptions,
      isBaseRecord,
      matchLocations,
      toggleMatchLocations,
      locationNumber,
      settings,
      locationMatches,
      orgId
    } = this.props;
    const customHeader = (
      <div className="title d-flex justify-content-between align-items-center mb-1">
        <div
          className="col collapseBtn d-flex justify-content-start align-items-center pr-1 h4 mb-0 pl-0 flex-grow-0"
          onClick={this.toggleAreaOpen}
        >
          <div className="collapseIcon">
            <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
          </div>
          <Translate contentKey="singleRecordView.details.titleLocations" /> <span className="text-blue ml-1">{locationsCount}</span>
        </div>
        {isOnlyOne ? null : (
          <div className={isBaseRecord ? 'col-8 changeRecordSelect flex-grow-1' : 'w-100'}>
            <Select
              onChange={this.changeRecord}
              options={selectOptions}
              value={SingleLocationDetails.getSelectOption(record, locationNumber)}
            />
          </div>
        )}
        {isBaseRecord ? (
          <div className="col-1 d-flex justify-content-center align-items-center">
            <input
              checked={matchLocations}
              onChange={toggleMatchLocations}
              type="checkbox"
              title={translate('multiRecordView.locationCheckboxTooltip')}
              id={this.state.id}
            />
            <label htmlFor={this.state.id} className="chainIcon">
              <FontAwesomeIcon size="sm" icon="link" />
            </label>
          </div>
        ) : null}
      </div>
    );

    const matchButton = (
      <Button onClick={this.matchLocation} replace color="info">
        <FontAwesomeIcon icon="arrow-left" />{' '}
        <span className="d-none d-md-inline">
          <Translate contentKey="multiRecordView.matchesMyLocationRecord" />
        </span>
      </Button>
    );

    const unmatchButton = (
      <Button onClick={this.unmatchLocation} replace color="danger">
        <FontAwesomeIcon icon="arrow-left" />{' '}
        <span className="d-none d-md-inline">
          <Translate contentKey="multiRecordView.unmatchThisRecord" />
        </span>
      </Button>
    );

    const matchOrUnMatchButton = (
      <h5>
        {locationMatches && this.props.baseRecord && this.props.baseRecord.organization.id !== record.location.organizationId
          ? this.isMatched()
            ? unmatchButton
            : matchButton
          : ''}
      </h5>
    );

    const itemHeader = (
      <div>
        {matchOrUnMatchButton}
        <h5>
          <Translate contentKey="multiRecordView.lastCompleteReview" />
          {record.location.lastVerifiedOn ? (
            <TextFormat value={record.location.lastVerifiedOn} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
          ) : (
            <Translate contentKey="multiRecordView.unknown" />
          )}
        </h5>
        <h5>
          <Translate contentKey="multiRecordView.lastUpdated" />
          {record.location.updatedAt ? (
            <TextFormat value={record.location.updatedAt} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
          ) : (
            <Translate contentKey="multiRecordView.unknown" />
          )}
        </h5>
      </div>
    );

    const additionalFields = {
      PHYSICAL_ADDRESS: (
        <PhysicalAddressDetails key="physical-address-details" {...this.props} address={record.physicalAddress} settings={settings} />
      ),
      POSTAL_ADDRESS: (
        <PostalAddressDetails key="postal-address-details" {...this.props} address={record.postalAddress} settings={settings} />
      ),
      OPENING_HOURS: <OpeningHoursDetails key="opening-hours-details" {...this.props} hours={record.regularScheduleOpeningHours} />,
      LANGUAGE: <LanguagesDetails key="languages-details" {...this.props} langs={record.langs} />,
      HOLIDAY_SCHEDULE: <HolidaySchedulesDetails key="holiday-schedule-details" {...this.props} schedules={record.holidaySchedules} />,
      LOCATION_MATCHES: (
        <LocationMatchesDetails
          key="location-matches"
          orgId={orgId}
          locationMatches={locationMatches}
          locationId={record.location.id}
          isBaseRecord={isBaseRecord}
        />
      )
    };

    const fields = {
      NAME: getTextField(record.location, 'name'),
      ALTERNATE_NAME: getTextField(record.location, 'alternateName'),
      DESCRIPTION: getTextAreaField(record.location, 'description'),
      TRANSPORTATION: getTextField(record.location, 'transportation'),
      LATITUDE: getTextField(record.location, 'latitude'),
      LONGITUDE: getTextField(record.location, 'longitude'),
      REGULAR_SCHEDULE_NOTES: getTextAreaField(record, 'regularScheduleNotes')
    };

    return (
      <Row>
        <Col sm={columnSize}>
          <hr />
          <AdditionalDetails
            {...this.props}
            fields={this.getFields(fields)}
            entityClass={'Location'}
            customHeader={customHeader}
            additionalFields={this.getFields(additionalFields)}
            itemHeader={itemHeader}
            toggleAvailable
            isCustomToggle
            customToggleValue={this.state.isAreaOpen}
          />
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = state => ({
  baseLocation: state.sharedRecordView.openLocation
});

const mapDispatchToProps = { createLocationMatch, deleteLocationMatch };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SingleLocationDetails);
