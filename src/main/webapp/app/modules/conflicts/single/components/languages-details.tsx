import React from 'react';
import '../single-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { AdditionalDetails } from './additional-details';
import { ILanguage } from 'app/shared/model/language.model';

export interface ILanguagesDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  langs: ILanguage[];
}

export class LanguagesDetails extends React.Component<ILanguagesDetailsProp> {
  getTextField = (language, fieldName) => ({
    type: 'text',
    fieldName,
    defaultValue: language[fieldName]
  });

  render() {
    const { langs } = this.props;
    const fields = langs.map(language => this.getTextField(language, 'language'));

    return fields.length > 0 ? (
      <AdditionalDetails
        {...this.props}
        fields={fields}
        entityClass={'Language'}
        customHeader={false}
        additionalFields={false}
        toggleAvailable
        isCustomToggle={false}
        customToggleValue={false}
      />
    ) : null;
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LanguagesDetails);
