import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './postal-address.reducer';
import { IPostalAddress } from 'app/shared/model/postal-address.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPostalAddressProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class PostalAddress extends React.Component<IPostalAddressProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { postalAddressList, match } = this.props;
    return (
      <div>
        <h2 id="postal-address-heading">
          <Translate contentKey="serviceNetApp.postalAddress.home.title">Postal Addresses</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.postalAddress.home.createLabel">Create new Postal Address</Translate>
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
                  <Translate contentKey="serviceNetApp.postalAddress.attention">Attention</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.address1">Address 1</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.address2">Address 2</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.city">City</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.region">Region</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.stateProvince">State Province</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.postalCode">Postal Code</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.country">Country</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.location">Location</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {postalAddressList.map((postalAddress, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${postalAddress.id}`} color="link" size="sm">
                      {postalAddress.id}
                    </Button>
                  </td>
                  <td>{postalAddress.attention}</td>
                  <td>{postalAddress.address1}</td>
                  <td>{postalAddress.address2}</td>
                  <td>{postalAddress.city}</td>
                  <td>{postalAddress.region}</td>
                  <td>{postalAddress.stateProvince}</td>
                  <td>{postalAddress.postalCode}</td>
                  <td>{postalAddress.country}</td>
                  <td>
                    {postalAddress.locationName ? (
                      <Link to={`location/${postalAddress.locationId}`}>{postalAddress.locationName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${postalAddress.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${postalAddress.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${postalAddress.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ postalAddress }: IRootState) => ({
  postalAddressList: postalAddress.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PostalAddress);
