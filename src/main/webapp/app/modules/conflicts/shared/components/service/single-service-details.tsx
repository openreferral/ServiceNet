import React from 'react';
import { Col, Row } from 'reactstrap';
import '../../shared-record-view.scss';
import { TextFormat, Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { OpeningHoursDetails } from '../opening-hours-details';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { AdditionalDetails } from '../additional-details';
import { IServiceRecord } from 'app/shared/model/service-record.model';
import { EligibilityDetails } from './eligibility-details';
import { FundingDetails } from './funding-details';
import { RequiredDocumentsDetails } from './required-documents-details';
import { ServiceTaxonomiesDetails } from './service-taxonomies-details';
import { PaymentsAcceptedDetails } from './payments-accepted-details';
import { LanguagesDetails } from '../languages-details';
import { HolidaySchedulesDetails } from '../holiday-schedules-details';
import { ContactsDetails } from '../contact/contacts-details';
import { PhonesDetails } from '../phone/phones-details';
import { getTextField } from 'app/shared/util/single-record-view-utils';
import { APP_DATE_FORMAT } from 'app/config/constants';
import Select from 'react-select';

export interface ISingleServiceDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  record: IServiceRecord;
  servicesCount: string;
  changeRecord: any;
  isOnlyOne: boolean;
  columnSize: number;
  showClipboard: boolean;
  isAreaOpen: boolean;
  selectOptions: any;
}

export interface ISingleServiceDetailsState {
  isAreaOpen: boolean;
}

export class SingleServiceDetails extends React.Component<ISingleServiceDetailsProp, ISingleServiceDetailsState> {
  state: ISingleServiceDetailsState = {
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
    const { record, isOnlyOne, columnSize, selectOptions, servicesCount } = this.props;
    const customHeader = (
      <div className="title d-flex justify-content-start align-items-center mb-1">
        <div
          className="col collapseBtn d-flex justify-content-start align-items-center pr-3 h4 mb-0 pl-0 flex-grow-0"
          onClick={this.toggleAreaOpen}
        >
          <div className="collapseIcon">
            <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
          </div>
          <Translate contentKey="singleRecordView.details.titleServices" /> <span className="text-blue ml-1">{servicesCount}</span>
        </div>
        {isOnlyOne ? null : (
          <div className="flex-grow-1">
            <Select onChange={this.changeRecord} options={selectOptions} />
          </div>
        )}
      </div>
    );

    const itemHeader = (
      <div>
        <h5>
          <Translate contentKey="multiRecordView.lastCompleteReview" />
          {record.service.lastVerifiedOn ? (
            <TextFormat value={record.service.lastVerifiedOn} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
          ) : (
            <Translate contentKey="multiRecordView.unknown" />
          )}
        </h5>
        <h5>
          <Translate contentKey="multiRecordView.lastUpdated" />
          {record.service.updatedAt ? (
            <TextFormat value={record.service.updatedAt} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
          ) : (
            <Translate contentKey="multiRecordView.unknown" />
          )}
        </h5>
      </div>
    );

    const additionalFields = [
      <EligibilityDetails key="eligibility-details" {...this.props} eligibility={record.eligibility} />,
      <FundingDetails key="funding-details" {...this.props} funding={record.funding} />,
      <RequiredDocumentsDetails key="required-documents-details" {...this.props} docs={record.docs} />,
      <PaymentsAcceptedDetails key="payments-accepted-details" {...this.props} payments={record.paymentsAccepteds} />,
      <ServiceTaxonomiesDetails key="service-taxonomies-details" {...this.props} taxonomies={record.taxonomies} />,
      <OpeningHoursDetails key="opening-hours-details" {...this.props} hours={record.regularScheduleOpeningHours} />,
      <LanguagesDetails key="languages-details" {...this.props} langs={record.langs} />,
      <HolidaySchedulesDetails key="holiday-schedules-details" {...this.props} schedules={record.holidaySchedules} />,
      <ContactsDetails key="contacts-details" {...this.props} contacts={record.contacts} />,
      <PhonesDetails key="phones-details" {...this.props} phones={record.phones} />
    ];

    const fields = [
      getTextField(record.service, 'name'),
      getTextField(record.service, 'alternateName'),
      {
        type: 'textarea',
        fieldName: 'description',
        defaultValue: record.service.description
      },
      getTextField(record.service, 'url'),
      getTextField(record.service, 'email'),
      getTextField(record.service, 'status'),
      {
        type: 'textarea',
        fieldName: 'interpretationServices',
        defaultValue: record.service.interpretationServices
      },
      {
        type: 'textarea',
        fieldName: 'applicationProcess',
        defaultValue: record.service.applicationProcess
      },
      {
        type: 'textarea',
        fieldName: 'waitTime',
        defaultValue: record.service.waitTime
      },
      {
        type: 'textarea',
        fieldName: 'fees',
        defaultValue: record.service.fees
      },
      {
        type: 'textarea',
        fieldName: 'accreditations',
        defaultValue: record.service.accreditations
      },
      {
        type: 'textarea',
        fieldName: 'licenses',
        defaultValue: record.service.licenses
      },
      getTextField(record, 'type')
    ];
    return (
      <Row>
        <Col sm={columnSize}>
          <hr />
          <AdditionalDetails
            {...this.props}
            fields={fields}
            entityClass={'Service'}
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
)(SingleServiceDetails);
