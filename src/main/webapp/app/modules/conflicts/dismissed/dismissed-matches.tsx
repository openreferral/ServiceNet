import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table, Input } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';
import { toast } from 'react-toastify';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { getMatches } from '../shared/shared-record-view.reducer';

export interface IDismissedMatchesProp extends StateProps, DispatchProps, RouteComponentProps<{}> {}

export class DismissedMatches extends React.Component<IDismissedMatchesProp> {
  componentDidMount() {
    this.props.getMatches(this.props.orgId);
  }

  revertDismissOrganizationMatch = matchId => () => {
    const { orgId } = this.props;

    axios
      .post(`/api/organization-matches/${matchId}/revertDismiss`)
      .then(() => {
        toast.success(translate('dismissedMatches.reinstatedSuccessfully'));
        Promise.all([this.props.getMatches(this.props.orgId)]).then(() => {
          if (!this.props.dismissedMatches.length) {
            this.props.history.push(`/multi-record-view/${orgId}`);
          }
        });
      })
      .catch(() => {
        toast.error(translate('dismissedMatches.reinstateError'));
      });
  };

  render() {
    const { dismissedMatches, orgId } = this.props;
    return (
      <div>
        <Link to={`/${this.props.matches.length > 0 ? 'multi' : 'single'}-record-view/${orgId}`}>
          <FontAwesomeIcon icon="arrow-left" />
          &nbsp;
          <span className="d-none d-md-inline">
            <Translate contentKey="dismissedMatches.back">Back</Translate>
          </span>
        </Link>
        <h2 className="mt-1">
          <Translate contentKey="dismissedMatches.header">Mismatches for [Individual Record Name]</Translate>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="dismissedMatches.record">Record</Translate>
                </th>
                <th>
                  <Translate contentKey="dismissedMatches.dateDismissed">Date Dismissed</Translate>
                </th>
                <th>
                  <Translate contentKey="dismissedMatches.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="dismissedMatches.notes">Notes</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {dismissedMatches.map((organizationMatch, i) => (
                <tr key={`entity-${i}`}>
                  <td>{organizationMatch.organizationRecordName}</td>
                  <td>
                    <TextFormat type="date" value={organizationMatch.dismissDate} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{organizationMatch.dismissedByName}</td>
                  <td>
                    <Input
                      disabled
                      type="textarea"
                      placeholder={translate('dismissedMatches.notesPlaceholder')}
                      value={organizationMatch.dismissComment}
                    />
                  </td>
                  <td className="text-right">
                    <div className="d-flex">
                      <Link to={`/dismissed-match-view/${orgId}/partner/${organizationMatch.partnerVersionId}`} className="mr-3">
                        <Translate contentKey="dismissedMatches.view">View</Translate>
                      </Link>
                      <Button color="primary" size="sm" onClick={this.revertDismissOrganizationMatch(organizationMatch.id)}>
                        <Translate contentKey="dismissedMatches.reinstate">Reinstate</Translate>
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
  matches: storeState.sharedRecordView.matches,
  dismissedMatches: storeState.sharedRecordView.dismissedMatches
});

const mapDispatchToProps = { getMatches };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DismissedMatches);
