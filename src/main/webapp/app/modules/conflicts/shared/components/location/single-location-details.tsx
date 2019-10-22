import React from 'react';
import { Col, Row, Button } from 'reactstrap';
import '../../shared-record-view.scss';
import { TextFormat, Translate } from 'react-jhipster';
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
}

export interface ISingleLocationDetailsState {
  isAreaOpen: boolean;
}

export class SingleLocationDetails extends React.Component<ISingleLocationDetailsProp, ISingleLocationDetailsState> {
  state: ISingleLocationDetailsState = {
    isAreaOpen: this.props.isAreaOpen
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  changeRecord = record => {
    this.setState({ isAreaOpen: true });
    this.props.changeRecord(record.value);
  };

  render() {
    const { record, isOnlyOne, columnSize, locationsCount, selectOptions } = this.props;
    const customHeader = (
      <div className="title d-flex justify-content-start align-items-center mb-1">
        <div className="col-3 collapseBtn d-flex justify-content-start align-items-center pr-1 h4 mb-0 pl-0" onClick={this.toggleAreaOpen}>
          <div className="collapseIcon">
            <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
          </div>
          <Translate contentKey="singleRecordView.details.titleLocations" /> <span className="text-blue">{locationsCount}</span>
        </div>
        {isOnlyOne ? null : (
          <div className="w-100">
            <Select onChange={this.changeRecord} options={selectOptions} />
          </div>
        )}
      </div>
    );

    const itemHeader = (
      <div>
        <h5>
          <Translate contentKey="multiRecordView.lastCompleteReview" />
          <TextFormat value={record.location.lastVerifiedOn} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
        </h5>
        <h5>
          <Translate contentKey="multiRecordView.lastUpdated" />
          <TextFormat value={record.location.updatedAt} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
        </h5>
      </div>
    );

    const additionalFields = [
      <PhysicalAddressDetails key="physical-address-details" {...this.props} address={record.physicalAddress} />,
      <PostalAddressDetails key="postal-address-details" {...this.props} address={record.postalAddress} />,
      <OpeningHoursDetails key="opening-hours-details" {...this.props} hours={record.regularScheduleOpeningHours} />,
      <LanguagesDetails key="languages-details" {...this.props} langs={record.langs} />,
      <HolidaySchedulesDetails key="holiday-schedule-details" {...this.props} schedules={record.holidaySchedules} />
    ];

    const fields = [
      getTextField(record.location, 'name'),
      getTextField(record.location, 'alternateName'),
      getTextAreaField(record.location, 'description'),
      getTextField(record.location, 'transportation'),
      getTextField(record.location, 'latitude'),
      getTextField(record.location, 'longitude'),
      getTextAreaField(record, 'regularScheduleNotes')
    ];
    return (
      <Row>
        <Col sm={columnSize}>
          <hr />
          <AdditionalDetails
            {...this.props}
            fields={fields}
            entityClass={'Location'}
            customHeader={customHeader}
            additionalFields={additionalFields}
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

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SingleLocationDetails);
