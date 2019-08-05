import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import axios from 'axios';
import { toast } from 'react-toastify';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { getHiddenMatches } from '../shared/shared-record-view.reducer';

export interface IHiddenMatchesProp extends StateProps, DispatchProps, RouteComponentProps<{}> {}

export class HiddenMatches extends React.Component<IHiddenMatchesProp> {
  componentDidMount() {
    this.props.getHiddenMatches();
  }

  revertHideOrganizationMatch = matchId => () => {
    axios
      .post(`/api/organization-matches/${matchId}/revertHide`)
      .then(() => {
        toast.success(translate('hiddenMatches.reinstatedSuccessfully'));
        Promise.all([this.props.getHiddenMatches()]).then(() => {
          if (!this.props.hiddenMatches.length) {
            this.props.history.push(`/`);
          }
        });
      })
      .catch(() => {
        toast.error(translate('hiddenMatches.reinstateError'));
      });
  };

  render() {
    const { hiddenMatches, orgId } = this.props;
    return (
      <div>
        <h2 className="mt-1">
          <Translate contentKey="hiddenMatches.header">Hidden Organization Matches</Translate>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="hiddenMatches.recordId">Record ID</Translate>
                </th>
                <th>
                  <Translate contentKey="hiddenMatches.recordName">Record Name</Translate>
                </th>
                <th>
                  <Translate contentKey="hiddenMatches.dateHidden">Date Hidden</Translate>
                </th>
                <th>
                  <Translate contentKey="hiddenMatches.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {hiddenMatches.map((organizationMatch, i) => (
                <tr key={`entity-${i}`}>
                  <td>{organizationMatch.organizationRecordId}</td>
                  <td>{organizationMatch.organizationRecordName}</td>
                  <td>
                    <TextFormat type="date" value={organizationMatch.hiddenDate} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{organizationMatch.hiddenByName}</td>
                  <td className="text-right">
                    <div className="d-flex">
                      <Button color="primary" size="sm" onClick={this.revertHideOrganizationMatch(organizationMatch.id)}>
                        <Translate contentKey="hiddenMatches.reinstate">Reinstate</Translate>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = (storeState, { match }) => ({
  orgId: match.params.orgId,
  hiddenMatches: storeState.sharedRecordView.hiddenMatches
});

const mapDispatchToProps = { getHiddenMatches };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HiddenMatches);
