import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './physical-address.reducer';
import { IPhysicalAddress } from 'app/shared/model/physical-address.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPhysicalAddressProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class PhysicalAddress extends React.Component<IPhysicalAddressProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { physicalAddressList, match } = this.props;
    return (
      <div>
        <h2 id="physical-address-heading">
          <Translate contentKey="serviceNetApp.physicalAddress.home.title">Physical Addresses</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.physicalAddress.home.createLabel">Create new Physical Address</Translate>
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
                  <Translate contentKey="serviceNetApp.physicalAddress.attention">Attention</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.physicalAddress.address1">Address 1</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.physicalAddress.city">City</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.physicalAddress.region">Region</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.physicalAddress.stateProvince">State Province</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.physicalAddress.postalCode">Postal Code</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.physicalAddress.country">Country</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.physicalAddress.location">Location</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {physicalAddressList.map((physicalAddress, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${physicalAddress.id}`} color="link" size="sm">
                      {physicalAddress.id}
                    </Button>
                  </td>
                  <td>{physicalAddress.attention}</td>
                  <td>{physicalAddress.address1}</td>
                  <td>{physicalAddress.city}</td>
                  <td>{physicalAddress.region}</td>
                  <td>{physicalAddress.stateProvince}</td>
                  <td>{physicalAddress.postalCode}</td>
                  <td>{physicalAddress.country}</td>
                  <td>
                    {physicalAddress.locationName ? (
                      <Link to={`location/${physicalAddress.locationId}`}>{physicalAddress.locationName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${physicalAddress.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${physicalAddress.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${physicalAddress.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ physicalAddress }: IRootState) => ({
  physicalAddressList: physicalAddress.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PhysicalAddress);
