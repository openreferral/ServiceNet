import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import {
  Translate,
  translate,
  ICrudGetAllAction,
  TextFormat,
  JhiPagination,
  getPaginationItemsNumber,
  getSortState,
  IPaginationBaseState
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import PageSizeSelector from '../page-size-selector';
import { IRootState } from 'app/shared/reducers';
import { getEntities, updateEntity } from './organization-match.reducer';
import { ITEMS_PER_PAGE_ENTITY } from 'app/shared/util/pagination.constants';
import { IOrganizationMatch } from 'app/shared/model/organization-match.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrganizationMatchProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IOrganizationMatchState extends IPaginationBaseState {
  dropdownOpenTop: boolean;
  dropdownOpenBottom: boolean;
  itemsPerPage: number;
}

export class OrganizationMatch extends React.Component<IOrganizationMatchProps, IOrganizationMatchState> {
  constructor(props) {
    super(props);

    this.toggleTop = this.toggleTop.bind(this);
    this.toggleBottom = this.toggleBottom.bind(this);
    this.select = this.select.bind(this);
    this.state = {
      dropdownOpenTop: false,
      dropdownOpenBottom: false,
      itemsPerPage: ITEMS_PER_PAGE_ENTITY,
      ...getSortState(this.props.location, ITEMS_PER_PAGE_ENTITY)
    };
  }

  componentDidMount() {
    this.getEntities();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
    window.scrollTo(0, 0);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  toggleTop() {
    this.setState(prevState => ({
      dropdownOpenTop: !prevState.dropdownOpenTop
    }));
  }

  toggleBottom() {
    this.setState(prevState => ({
      dropdownOpenBottom: !prevState.dropdownOpenBottom
    }));
  }

  select = prop => () => {
    this.setState(
      {
        itemsPerPage: prop
      },
      () => this.sortEntities()
    );
  };

  render() {
    const { organizationMatchList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="organization-match-heading">
          <Translate contentKey="serviceNetApp.organizationMatch.home.title">Organization Matches</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.organizationMatch.home.createLabel">Create new Organization Match</Translate>
          </Link>
        </h2>
        <Row className="justify-content-center">
          <PageSizeSelector
            dropdownOpen={this.state.dropdownOpenTop}
            toggleSelect={this.toggleTop}
            itemsPerPage={this.state.itemsPerPage}
            selectFunc={this.select}
          />
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.timestamp">Timestamp</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.dismissed">Dismissed</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.dismissComment">Dismiss Comment</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.dismissDate">Dismiss Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.dismissedBy">Dismissed By</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.organizationRecord">Organization Record</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.partnerVersion">Partner Version</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {organizationMatchList.map((organizationMatch, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${organizationMatch.id}`} color="link" size="sm">
                      {organizationMatch.id}
                    </Button>
                  </td>
                  <td>
                    <TextFormat type="date" value={organizationMatch.timestamp} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{organizationMatch.dismissed ? 'true' : 'false'}</td>
                  <td>{organizationMatch.dismissComment}</td>
                  <td>
                    <TextFormat type="date" value={organizationMatch.dismissDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{organizationMatch.dismissedByName ? organizationMatch.dismissedByName : ''}</td>
                  <td>
                    {organizationMatch.organizationRecordName ? (
                      <Link to={`organization/${organizationMatch.organizationRecordId}`}>{organizationMatch.organizationRecordName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {organizationMatch.partnerVersionName ? (
                      <Link to={`organization/${organizationMatch.partnerVersionId}`}>{organizationMatch.partnerVersionName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${organizationMatch.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${organizationMatch.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${organizationMatch.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
        <Row className="justify-content-center">
          <PageSizeSelector
            dropdownOpen={this.state.dropdownOpenBottom}
            toggleSelect={this.toggleBottom}
            itemsPerPage={this.state.itemsPerPage}
            selectFunc={this.select}
          />
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ organizationMatch }: IRootState) => ({
  organizationMatchList: organizationMatch.entities,
  totalItems: organizationMatch.totalItems
});

const mapDispatchToProps = {
  getEntities,
  updateEntity
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrganizationMatch);
