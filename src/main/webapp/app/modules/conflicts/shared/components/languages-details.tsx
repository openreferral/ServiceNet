import React from 'react';
import '../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { AdditionalDetails } from './additional-details';
import { ILanguage } from 'app/shared/model/language.model';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface ILanguagesDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  langs: ILanguage[];
  showClipboard: boolean;
}

export class LanguagesDetails extends React.Component<ILanguagesDetailsProp> {
  render() {
    const { langs } = this.props;
    const fields = langs.map(language => getTextField(language, 'language'));

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
