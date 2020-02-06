import React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';

import { IRootState } from 'app/shared/reducers';
import { logout } from 'app/shared/reducers/authentication';
import { reset } from 'app/entities/fields-display-settings/fields-display-settings.reducer';

export interface ILogoutProps extends StateProps, DispatchProps {}

export class Logout extends React.Component<ILogoutProps> {
  componentDidMount() {
    this.props.reset();
    this.props.logout();
  }

  render() {
    return (
      <div className="p-5">
        <h4>Logged out successfully!</h4>
        <Redirect
          to={{
            pathname: '/',
            state: { loggingOut: true }
          }}
        />
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({});

const mapDispatchToProps = { logout, reset };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Logout);
