import React from 'react';
import { Col, Row, Button } from 'reactstrap';
import '../../shared-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
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
import { HolidayScheduleDetails } from '../holiday-schedule-details';
import { ContactsDetails } from '../contact/contacts-details';
import { PhonesDetails } from '../phone/phones-details';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface ISingleServiceDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  record: IServiceRecord;
  servicesCount: string;
  changeRecord: any;
  isOnlyOne: boolean;
  columnSize: number;
  showClipboard: boolean;
}

export interface ISingleServiceDetailsState {
  isAreaOpen: boolean;
}

export class SingleServiceDetails extends React.Component<ISingleServiceDetailsProp, ISingleServiceDetailsState> {
  state: ISingleServiceDetailsState = {
    isAreaOpen: false
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  changeRecord = offset => () => {
    this.setState({ isAreaOpen: true });
    this.props.changeRecord(offset);
  };

  render() {
    const { record, isOnlyOne, columnSize } = this.props;
    const customHeader = (
      <h4 className="title">
        <div className="collapseBtn" onClick={this.toggleAreaOpen}>
          <div className="collapseIcon">
            <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
          </div>
          <Translate contentKey="singleRecordView.details.titleServices" /> <span className="text-blue">{this.props.servicesCount}</span>
        </div>
        {isOnlyOne ? null : (
          <span>
            <span role="button" onClick={this.changeRecord(-1)}>
              <span className="text-blue">〈</span> Prev
            </span>
            <span role="button" onClick={this.changeRecord(1)}>
              Next <span className="text-blue">〉</span>
            </span>
          </span>
        )}
      </h4>
    );
    const additionalFields = [
      <EligibilityDetails key="eligibility-details" {...this.props} eligibility={record.eligibility} />,
      <FundingDetails key="funding-details" {...this.props} funding={record.funding} />,
      <RequiredDocumentsDetails key="required-documents-details" {...this.props} docs={record.docs} />,
      <PaymentsAcceptedDetails key="payments-accepted-details" {...this.props} payments={record.paymentsAccepteds} />,
      <ServiceTaxonomiesDetails key="service-taxonomies-details" {...this.props} taxonomies={record.taxonomies} />,
      <OpeningHoursDetails key="opening-hours-details" {...this.props} hours={record.regularScheduleOpeningHours} />,
      <LanguagesDetails key="languages-details" {...this.props} langs={record.langs} />,
      <HolidayScheduleDetails key="holiday-schedule-details" {...this.props} schedule={record.holidaySchedule} />,
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
