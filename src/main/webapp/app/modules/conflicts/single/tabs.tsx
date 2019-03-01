import React from 'react';
import { TabContent, TabPane, Nav, NavItem, NavLink } from 'reactstrap';
import classnames from 'classnames';
import Details from './details';
import { connect } from 'react-redux';
import { Translate } from 'react-jhipster';
import { IActivity } from 'app/shared/model/activity.model';
import { RouteComponentProps } from 'react-router-dom';

export interface ISingleRecordViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {
  activity: IActivity;
}

export interface ISingleRecordViewState {
  activeTab: string;
}

export class Tabs extends React.Component<ISingleRecordViewProp, ISingleRecordViewState> {
  state: ISingleRecordViewState = {
    activeTab: '1'
  };

  toggle(tab) {
    if (this.state.activeTab !== tab) {
      this.setState({
        activeTab: tab
      });
    }
  }

  render() {
    return (
      <div>
        <Nav tabs>
          <NavItem>
            <NavLink className={classnames({ active: this.state.activeTab === '1' })} onClick={this.toggle('1')}>
              <Translate contentKey="singleRecordView.tabs.details" />
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink className={classnames({ active: false, disabled: true })}>
              <Translate contentKey="singleRecordView.tabs.otherTabs" />
            </NavLink>
          </NavItem>
        </Nav>
        <TabContent activeTab={this.state.activeTab}>
          <TabPane tabId="1">
            <Details {...this.props} isBaseRecord />
          </TabPane>
        </TabContent>
      </div>
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
)(Tabs);
