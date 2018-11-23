import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './funding.reducer';
import { IFunding } from 'app/shared/model/funding.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFundingProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Funding extends React.Component<IFundingProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { fundingList, match } = this.props;
    return (
      <div>
        <h2 id="funding-heading">
          <Translate contentKey="serviceNetApp.funding.home.title">Fundings</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.funding.home.createLabel">Create new Funding</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.funding.source">Source</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.funding.organization">Organization</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.funding.srvc">Srvc</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {fundingList.map((funding, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${funding.id}`} color="link" size="sm">
                      {funding.id}
                    </Button>
                  </td>
                  <td>{funding.source}</td>
                  <td>
                    {funding.organizationName ? <Link to={`organization/${funding.organizationId}`}>{funding.organizationName}</Link> : ''}
                  </td>
                  <td>{funding.srvcName ? <Link to={`service/${funding.srvcId}`}>{funding.srvcName}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${funding.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${funding.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${funding.id}/delete`} color="danger" size="sm">
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
      </div>
    );
  }
}

const mapStateToProps = ({ funding }: IRootState) => ({
  fundingList: funding.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Funding);
