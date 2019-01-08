import './home.scss';

import React from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Translate, getSortState, IPaginationBaseState, TextFormat } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert, Card, CardText, CardBody, CardTitle, CardGroup, Badge } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { getEntities, reset } from 'app/shared/reducers/activity.reducer';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import ActivityElement from './ActivityElement';

export interface IHomeProp extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IHomeState = IPaginationBaseState;

export class Home extends React.Component<IHomeProp, IHomeState> {
  state: IHomeState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate(prevProps) {
    if (this.props.updateSuccess || (this.props.loginSuccess === true && prevProps.loginSuccess === false)) {
      this.reset();
    }
  }

  reset = () => {
    Promise.all([this.props.getSession()]).then(() => {
      this.props.reset();
      this.setState({ activePage: 1 }, () => {
        this.getEntities();
      });
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    if (this.props.isAuthenticated) {
      this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
    }
  };

  render() {
    const { account, activityList } = this.props;
    return (
      <div>
        <Row>
          <Col>
            {account && account.login ? null : (
              <div>
                <Alert color="warning">
                  <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>
                  <Link to="/login" className="alert-link">
                    <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
                  </Link>
                  <Translate contentKey="global.messages.info.authenticated.suffix">
                    , you can try the default accounts:
                    <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
                    <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
                  </Translate>
                </Alert>

                <Alert color="warning">
                  <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>
                  &nbsp;
                  <Link to="/register" className="alert-link">
                    <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
                  </Link>
                </Alert>
              </div>
            )}
          </Col>
        </Row>
        {account && account.login ? (
          <div>
            <Row>
              <Col md="8">
                <h2 id="main-page-title">
                  <Translate contentKey="serviceNetApp.activity.unresolved.title" />
                </h2>
              </Col>
            </Row>
            {activityList.map(activity => (
              <ActivityElement activity={activity} />
            ))}
            {activityList.length == 0 ? (
              <Row>
                <Col md="8">
                  <Translate contentKey="serviceNetApp.activity.empty" />
                </Col>
              </Row>
            ) : null}
          </div>
        ) : null}
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  loginSuccess: storeState.authentication.loginSuccess,
  activityList: storeState.activity.entities,
  totalItems: storeState.activity.totalItems,
  links: storeState.activity.links,
  entity: storeState.activity.entity,
  updateSuccess: storeState.activity.updateSuccess
});

const mapDispatchToProps = {
  getSession,
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);
