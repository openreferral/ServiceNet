import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { SingleContactDetails } from './single-contact-details';
import { IContact } from 'app/shared/model/contact.model';

export interface IContactsDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  contacts: IContact[];
  columnSize: number;
  showClipboard: boolean;
}

export interface IContactsDetailsState {
  contactsNumber: number;
}

export class ContactsDetails extends React.Component<IContactsDetailsProp, IContactsDetailsState> {
  state: IContactsDetailsState = {
    contactsNumber: 0
  };

  changeRecord = () => {
    let contactsNumber = 0;
    if (this.state.contactsNumber !== this.props.contacts.length - 1) {
      contactsNumber = this.state.contactsNumber + 1;
    }
    this.setState({ contactsNumber });
  };

  render() {
    const { contacts } = this.props;
    const { contactsNumber } = this.state;
    const contact = contacts[contactsNumber];
    const contactDetails =
      contacts.length > contactsNumber ? (
        <SingleContactDetails
          {...this.props}
          changeRecord={this.changeRecord}
          isOnlyOne={contacts.length <= 1}
          contact={contact}
          contactsCount={`(${contactsNumber + 1}/${contacts.length}) `}
        />
      ) : null;

    return contactDetails;
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ContactsDetails);
